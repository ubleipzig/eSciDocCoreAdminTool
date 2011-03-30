package de.escidoc.admintool.service;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.AdminHandlerClientInterface;
import de.escidoc.core.client.interfaces.base.HandlerService;
import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;
import de.escidoc.core.resources.adm.MessagesStatus;
import de.escidoc.core.resources.adm.RepositoryInfo;
import de.escidoc.core.resources.common.MessagesResult;
import de.escidoc.core.resources.common.TaskParam;

public class AdminServiceImpl implements AdminService {

    private static final Logger LOG = LoggerFactory
        .getLogger(AdminServiceImpl.class);

    private final HandlerService client;

    public AdminServiceImpl(final HandlerService client) {
        this.client = client;
    }

    @Override
    public MessagesResult<Entry> loadCommonExamples() throws EscidocException,
        InternalClientException, TransportException {
        return getClient().loadExamples();
    }

    AdminHandlerClientInterface getClient() {
        return (AdminHandlerClientInterface) client;
    }

    @Override
    public void loginWith(final String handle) throws InternalClientException {
        client.setHandle(handle);
    }

    @Override
    public Map<String, String> getRepositoryInfo() throws EscidocException,
        InternalClientException, TransportException {
        final RepositoryInfo repositoryInfo = getClient().getRepositoryInfo();

        for (final String val : repositoryInfo.values()) {
            LOG.debug("" + val);

        }

        return repositoryInfo;
    }

    @Override
    public MessagesStatus purge(final Set<String> list)
        throws EscidocException, InternalClientException, TransportException {
        return getClient().deleteObjects(usingTaskParam(list));
    }

    private TaskParam usingTaskParam(final Set<String> ids) {
        final TaskParam param = new TaskParam();
        param.setKeepInSync(true);
        for (final String id : ids) {
            param.addResourceRef(id);
        }
        return param;
    }

    @Override
    public MessagesStatus retrievePurgeStatus() throws EscidocException,
        InternalClientException, TransportException {
        return getClient().getPurgeStatus();
    }

    @Override
    public MessagesStatus reindexAll(final boolean shouldClearIndex)
        throws EscidocException, InternalClientException, TransportException {
        return getClient().reindexAll(shouldClearIndex);
    }

    @Override
    public MessagesStatus retrieveReindexStatus() throws EscidocException,
        InternalClientException, TransportException {
        return getClient().getReindexStatus();
    }

    @Override
    public MessagesStatus reindex(
        final Boolean shouldClearIndex, final String indexNamePrefix)
        throws EscidocException, InternalClientException, TransportException {
        Preconditions.checkNotNull(indexNamePrefix,
            "indexNamePrefix can not be null: %s", indexNamePrefix);
        Preconditions.checkArgument(!indexNamePrefix.isEmpty(),
            "indexNamePrefix can not be empty: %s", indexNamePrefix);
        return getClient().reindex(shouldClearIndex.booleanValue(),
            indexNamePrefix);
    }
}
