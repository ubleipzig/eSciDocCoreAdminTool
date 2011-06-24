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

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;
import de.escidoc.core.resources.adm.MessagesStatus;

public interface AdminService extends EscidocService {

    List<Entry> loadCommonExamples() throws EscidocClientException;

    void loginWith(String handle) throws InternalClientException;

    Map<String, String> getRepositoryInfo() throws EscidocException, InternalClientException, TransportException;

    MessagesStatus purge(Set<String> list) throws EscidocException, InternalClientException, TransportException;

    MessagesStatus retrievePurgeStatus() throws EscidocException, InternalClientException, TransportException;

    MessagesStatus reindexAll(boolean shouldClearIndex) throws EscidocException, InternalClientException,
        TransportException;

    MessagesStatus retrieveReindexStatus() throws EscidocClientException;

    MessagesStatus reindex(Boolean shouldClearIndex, String indexNamePrefix) throws EscidocClientException;
}
