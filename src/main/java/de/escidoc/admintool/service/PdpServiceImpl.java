/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package de.escidoc.admintool.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.Subject;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.core.client.PolicyDecisionPointHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.pdp.Decision;
import de.escidoc.core.resources.aa.pdp.Requests;
import de.escidoc.core.resources.aa.pdp.Results;

public class PdpServiceImpl implements PdpService {

    private static final Logger LOG = LoggerFactory.getLogger(PdpServiceImpl.class);

    private final PolicyDecisionPointHandlerClient client;

    private Set<Subject> subjects = new HashSet<Subject>();

    private final Set<Attribute> actionAttrs = new HashSet<Attribute>();

    private final Set<Attribute> resourceAttrs = new HashSet<Attribute>();

    public PdpServiceImpl(final URL serviceAddress) {
        Preconditions.checkNotNull(serviceAddress, "serviceAddress is null: %s", serviceAddress);
        client = new PolicyDecisionPointHandlerClient(serviceAddress);
    }

    public PdpService isAction(final String actionId) throws URISyntaxException {
        actionAttrs
            .add(new Attribute(new URI(AppConstants.XACML_ACTION_ID), null, null, new StringAttribute(actionId)));
        return this;
    }

    @Override
    public PdpService forUser(final String userId) throws URISyntaxException {
        subjects = new HashSet<Subject>();
        subjects.add(new Subject(Subject.DEFAULT_CATEGORY, createSubjectAttribute(userId)));
        return this;
    }

    private Set<Attribute> createSubjectAttribute(final String userId) throws URISyntaxException {
        final Set<Attribute> subjectAttributes = new HashSet<Attribute>();
        subjectAttributes.add(new Attribute(new URI(AppConstants.SUBJECT_ID), null, null, new StringAttribute(userId)));
        return subjectAttributes;
    }

    @Override
    public PdpService forResource(final String resourceId) throws URISyntaxException {
        resourceAttrs
            .add(new Attribute(new URI(AppConstants.RESOURCE_ID), null, null, new StringAttribute(resourceId)));
        return this;
    }

    @Override
    public boolean permitted() throws EscidocClientException {
        Preconditions.checkArgument(subjects.size() <= 1, "more than one subjects are not allowed");
        Preconditions.checkArgument(resourceAttrs.size() <= 1, "more than one resource Attributes are not allowed");
        Preconditions.checkArgument(actionAttrs.size() == 1, "other than one action attributes are not supported");

        return toDecision(sendRequests()).equals(Decision.PERMIT);
    }

    private Results sendRequests() throws EscidocException, InternalClientException, TransportException {
        return client.evaluate(createNewRequests());
    }

    private Requests createNewRequests() {
        final Requests requests = new Requests();
        requests.add(new RequestCtx(getSubjects(), resourceAttrs, actionAttrs, Requests.DEFAULT_ENVIRONMENT));
        return requests;
    }

    private Set<Subject> getSubjects() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void userNotLoggedIn() {
        for (final Subject subject : subjects) {
            LOG.debug("sub: " + subject);
        }
        if (subjects.size() == 0) {
            final Set<Attribute> subjectAttributes = new HashSet<Attribute>();
            try {
                subjectAttributes.add(new Attribute(new URI(AppConstants.SUBJECT_ID), null, null, new StringAttribute(
                    " ")));
                subjects = new HashSet<Subject>();
                subjects.add(new Subject(Subject.DEFAULT_CATEGORY, subjectAttributes));
            }
            catch (final URISyntaxException e) {
                LOG.error("Wrong Syntax URI: " + e);
            }
        }
    }

    private Decision toDecision(final Results results) {
        return results.get(0).getInterpretedDecision();
    }

    @Override
    public boolean denied() throws EscidocClientException {
        return !permitted();
    }

    @Override
    public void loginWith(final String token) {
        client.setHandle(token);
    }
}