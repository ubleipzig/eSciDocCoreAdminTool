package de.escidoc.admintool.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.core.client.RoleHandlerClient;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.RoleHandlerClientInterface;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.TaskParam;

public class RoleService {

    private static final Logger LOG = LoggerFactory
        .getLogger(RoleService.class);

    private RoleHandlerClientInterface client;

    private final Map<String, Role> roleById = new HashMap<String, Role>();

    private final String eSciDocUri;

    private final String handle;

    private Collection<Role> allRoles;

    public RoleService(final String eSciDocUri, final String handle)
        throws InternalClientException {
        this.eSciDocUri = eSciDocUri;
        this.handle = handle;
        initClient();
    }

    private void initClient() throws InternalClientException {
        client = new RoleHandlerClient(eSciDocUri);
        client.setTransport(TransportProtocol.REST);
        client.setHandle(handle);
    }

    public Role retrieve(final String roleObjectId) throws EscidocException,
        InternalClientException, TransportException {
        return client.retrieve(roleObjectId);
    }

    @SuppressWarnings("deprecation")
    public Collection<Role> findAll() throws EscidocClientException {
        allRoles = client.retrieveRoles(emptyFilter()).getRoles();
        for (final Role r : allRoles) {
            LOG.info("role name: " + r.getProperties().getName());
            LOG.info("role des: " + r.getProperties().getDescription());
            LOG.info("role id: " + r.getObjid());
            roleById.put(r.getObjid(), r);
        }
        return allRoles;

    }

    private TaskParam emptyFilter() {
        final Set<Filter> filters = new HashSet<Filter>();

        filters.add(getFilter(AppConstants.CREATED_BY_FILTER,
            AppConstants.SYSADMIN_OBJECT_ID, null));

        final TaskParam filterParam = new TaskParam();
        filterParam.setFilters(filters);
        return filterParam;
    }

    // FIXME duplicate method in UserAccountService
    private Filter getFilter(
        final String name, final String value, final Collection<String> ids) {

        final Filter filter = new Filter();
        filter.setName(name);
        filter.setValue(value);
        filter.setIds(ids);
        return filter;
    }
}