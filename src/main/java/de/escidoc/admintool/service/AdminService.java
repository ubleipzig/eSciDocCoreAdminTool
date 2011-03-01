package de.escidoc.admintool.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;
import de.escidoc.core.resources.adm.MessagesStatus;

public interface AdminService extends EscidocService {

    List<Entry> loadCommonExamples() throws EscidocException,
        InternalClientException, TransportException;

    void loginWith(String handle) throws InternalClientException;

    Map<String, String> getRepositoryInfo() throws EscidocException,
        InternalClientException, TransportException;

    MessagesStatus purge(Set<String> list) throws EscidocException,
        InternalClientException, TransportException;

    MessagesStatus retrievePurgeStatus() throws EscidocException,
        InternalClientException, TransportException;

    MessagesStatus reindexAll(boolean shouldClearIndex)
        throws EscidocException, InternalClientException, TransportException;

    MessagesStatus retrieveReindexStatus() throws EscidocException,
        InternalClientException, TransportException;

    MessagesStatus reindex(Boolean shouldClearIndex, String indexNamePrefix)
        throws EscidocException, InternalClientException, TransportException;
}
