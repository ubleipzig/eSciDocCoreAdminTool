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
package de.escidoc.admintool.service.internal;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis.types.NonNegativeInteger;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.core.client.RoleHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.RoleHandlerClientInterface;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.sb.RecordPacking;

public class RoleService {

    private RoleHandlerClientInterface client;

    private final Map<String, Role> roleById = new HashMap<String, Role>();

    private final String eSciDocUri;

    private final String handle;

    private Collection<Role> allRoles;

    public RoleService(final String eSciDocUri, final String handle) throws InternalClientException {
        this.eSciDocUri = eSciDocUri;
        this.handle = handle;
        initClient();
    }

    private void initClient() throws InternalClientException {
        client = new RoleHandlerClient(eSciDocUri);
        client.setHandle(handle);
    }

    public Role retrieve(final String roleObjectId) throws EscidocException, InternalClientException,
        TransportException {
        return client.retrieve(roleObjectId);
    }

    public Collection<Role> findAll() throws EscidocClientException {
        final SearchRetrieveRequestType s = withEmptyFilter();

        s.setRecordPacking(RecordPacking.XML.getXmlValue());
        allRoles = client.retrieveRolesAsList(s);
        for (final Role r : allRoles) {
            roleById.put(r.getObjid(), r);
        }
        return allRoles;

    }

    private SearchRetrieveRequestType withEmptyFilter() {
        final SearchRetrieveRequestType request = new SearchRetrieveRequestType();
        request.setMaximumRecords(new NonNegativeInteger(AppConstants.MAX_RESULT_SIZE));
        return request;
    }
}