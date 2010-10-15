package de.escidoc.admintool.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.core.client.RoleHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.TaskParam;

public class RoleService {

    private static final Logger log = LoggerFactory // NOPMD by CHH on 9/17/10
                                                    // 10:34 AM
        .getLogger(RoleService.class);

    private final Map<String, Role> roleById = new HashMap<String, Role>();

    private final String authentification;

    private RoleHandlerClient roleClient;

    private Collection<Role> allRoles; // NOPMD by CHH on 9/17/10 10:34 AM

    private final String eSciDocUri;

    public RoleService(final String eSciDocUri, final String authentification)
        throws EscidocException, InternalClientException, TransportException {
        this.eSciDocUri = eSciDocUri;
        this.authentification = authentification;
        createClient();
    }

    private void createClient() throws EscidocException,
        InternalClientException, TransportException {
        roleClient = new RoleHandlerClient();
        roleClient.setHandle(authentification);
        roleClient.setServiceAddress(eSciDocUri);
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