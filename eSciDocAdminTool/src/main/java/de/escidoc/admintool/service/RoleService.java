package de.escidoc.admintool.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.RoleHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.TaskParam;

public class RoleService {

    private static final Logger log =
        LoggerFactory.getLogger(RoleService.class);

    private static final String ESCIDOC_SERVICE_ROOT_URI =
        "http://localhost:8080";

    private final Authentication authentification;

    private RoleHandlerClient roleClient;

    private Collection<Role> allRoles;

    private final Map<String, Role> roleById = new HashMap<String, Role>();

    public RoleService(final Authentication authentification)
        throws EscidocException, InternalClientException, TransportException {
        this.authentification = authentification;
        createClient();
    }

    private void createClient() throws EscidocException,
        InternalClientException, TransportException {
        roleClient = new RoleHandlerClient();
        roleClient.setHandle(authentification.getHandle());
        roleClient.setServiceAddress(ESCIDOC_SERVICE_ROOT_URI);
    }

    public Role retrieve(final String roleObjectId) throws EscidocException,
        InternalClientException, TransportException {
        return roleClient.retrieve(roleObjectId);
    }

    @SuppressWarnings("deprecation")
    public Collection<Role> findAll() throws EscidocClientException {
        allRoles = roleClient.retrieveRoles(emptyFilter()).getRoles();
        for (final Role r : allRoles) {
            log.info("role name: " + r.getProperties().getName());
            log.info("role des: " + r.getProperties().getDescription());
            log.info("role id: " + r.getObjid());
            roleById.put(r.getObjid(), r);
        }
        return allRoles;

    }

    private TaskParam emptyFilter() {
        final Collection<Filter> filters = TaskParam.filtersFactory();
        filters.add(getFilter(
            "http://escidoc.de/core/01/structural-relations/created-by",
            "escidoc:exuser1", null));

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