package de.escidoc.admintool.service;

import java.net.URI;
import java.net.URISyntaxException;
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
import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.PolicyDecisionPointHandlerClient;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.pdp.Decision;
import de.escidoc.core.resources.aa.pdp.Requests;
import de.escidoc.core.resources.aa.pdp.Results;

public class PdpServiceImpl implements PdpService {

    private static final Logger LOG = LoggerFactory
        .getLogger(PdpServiceImpl.class);

    private static final String SUBJECT_ID =
        "urn:oasis:names:tc:xacml:1.0:subject:subject-id";

    private static final String RESOURCE_ID =
        "urn:oasis:names:tc:xacml:1.0:resource:resource-id";

    private final PolicyDecisionPointHandlerClient client;

    private final Set<Attribute> actionAttrs = new HashSet<Attribute>();

    private final Set<Attribute> resourceAttrs = new HashSet<Attribute>();

    private final Set<Subject> subjects = new HashSet<Subject>();

    public PdpServiceImpl(final Authentication auth) {
        client = new PolicyDecisionPointHandlerClient(auth.getServiceAddress());
        client.setHandle(auth.getHandle());
        client.setTransport(TransportProtocol.REST);
    }

    public PdpService isAction(final String actionId) throws URISyntaxException {
        actionAttrs.removeAll(actionAttrs);
        actionAttrs.add(new Attribute(new URI(AppConstants.XACML_ACTION_ID),
            null, null, new StringAttribute(actionId)));
        return this;
    }

    @Override
    public PdpService forResource(final String resourceId)
        throws URISyntaxException {
        resourceAttrs.removeAll(resourceAttrs);
        resourceAttrs.add(new Attribute(new URI(RESOURCE_ID), null, null,
            new StringAttribute(resourceId)));
        return this;
    }

    @Override
    public PdpService forUser(final String userId) throws URISyntaxException {
        final Set<Attribute> subjectAttributes = new HashSet<Attribute>();
        subjectAttributes.add(new Attribute(new URI(SUBJECT_ID), null, null,
            new StringAttribute(userId)));
        subjects.removeAll(subjects);
        subjects.add(new Subject(Subject.DEFAULT_CATEGORY, subjectAttributes));
        return this;

    }

    @Override
    public boolean permitted() throws EscidocClientException {
        Preconditions.checkArgument(subjects.size() <= 1,
            "more than one subjects are not allowed");
        Preconditions.checkArgument(resourceAttrs.size() <= 1,
            "more than one subjects are not allowed");
        Preconditions.checkArgument(actionAttrs.size() == 1,
            "more than one subjects are not allowed");
        final Requests requests = new Requests();
        requests.add(new RequestCtx(subjects, resourceAttrs, actionAttrs,
            Requests.DEFAULT_ENVIRONMENT));
        return getDecisionFrom(client.evaluate(requests)).equals(
            Decision.permit);
    }

    private Decision getDecisionFrom(final Results results) {
        return results.get(0).getInterpretedDecision();
    }
}