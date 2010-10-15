package de.escidoc.admintool.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.domain.UserAccountFactory;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.UserAccountHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.GrantProperties;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.common.reference.RoleRef;

public class UserService {

    private final UserAccountHandlerClient client;

    private Collection<UserAccount> userAccounts;

    private final Map<String, UserAccount> userAccountById =
        new ConcurrentHashMap<String, UserAccount>();

    private UserAccount user;

    private GrantProperties grantProps;

    private final String eSciDocUri;

    public UserService(final String eSciDocUri, final String handle)
        throws EscidocException, InternalClientException, TransportException {
        this.eSciDocUri = eSciDocUri;
        client = createUserClient(handle);
    }

    private UserAccountHandlerClient createUserClient(final String handle)
        throws EscidocException, InternalClientException, TransportException {
        final UserAccountHandlerClient client = new UserAccountHandlerClient();
        client.setHandle(handle);
        client.setServiceAddress(eSciDocUri);
        return client;
    }

    @SuppressWarnings("deprecation")
    public Collection<UserAccount> findAll() throws EscidocClientException {
        userAccounts =
            getSoapClient()
                .retrieveUserAccounts(emptyFilter()).getUserAccounts();
        for (final UserAccount user : userAccounts) {
            userAccountById.put(user.getObjid(), user);
        }
        return userAccounts;
    }

    private UserAccountHandlerClient getSoapClient() {
        client.setTransport(TransportProtocol.SOAP);
        return client;
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

    public UserAccount retrieve(final String userObjectId)
        throws EscidocException, InternalClientException, TransportException {
        return getSoapClient().retrieve(userObjectId);
    }

    public void update(final String objid, final String newName)
        throws EscidocException, InternalClientException, TransportException {
        assert !(newName == null || newName.isEmpty()) : "name must not be null or empty";

        // TODO name the class with its responsibility
        final UserAccount updatedUserAccount =
            new UserAccountFactory()
                .update(getSelectedUser(objid)).name(newName).build();

        getSoapClient().update(updatedUserAccount);
    }

    // TODO ask Matthias, if we need one click button to activate OR deactivate
    // user account.
    public void update(
        final String objectId, final String newName, final Boolean isActive)
        throws EscidocClientException {

        this.update(objectId, newName);

        if (isActive) {
            activate(objectId);
        }
        else {
            deactivate(objectId);
        }
    }

    // TODO refactor to use polymorphism. Duplicate code in method body:
    // activate and deactivate
    public void activate(final String selectedItemId)
        throws InternalClientException, TransportException,
        EscidocClientException {

        assert !(selectedItemId == null || selectedItemId.isEmpty()) : "selectedItemId must not be null or empty";

        final UserAccount userAccount = getSelectedUser(selectedItemId);
        assert !userAccount.getProperties().isActive() : "User account is already active.";

        getSoapClient().activate(userAccount.getObjid(),
            lastModificationDate(userAccount));
    }

    public void deactivate(final String selectedItemId)
        throws InternalClientException, TransportException,
        EscidocClientException {
        assert !(selectedItemId == null || selectedItemId.isEmpty()) : "selectedItemId must not be null or empty";

        final UserAccount userAccount = getSelectedUser(selectedItemId);
        assert userAccount.getProperties().isActive() : "User account is not active.";

        getSoapClient().deactivate(userAccount.getObjid(),
            lastModificationDate(userAccount));
    }

    private TaskParam lastModificationDate(final UserAccount userAccount) {
        final TaskParam taskParam = new TaskParam();

        final DateTime lastModificationDate =
            userAccount.getLastModificationDate();
        assert lastModificationDate != null : "last modification date has not been set";

        taskParam
            .setLastModificationDate(userAccount.getLastModificationDate());
        return taskParam;
    }

    private UserAccount getSelectedUser(final String selectedItemId) {
        assert !(selectedItemId == null || selectedItemId.isEmpty()) : "selectedItemId must not be null or empty";
        final UserAccount selectedUser = userAccountById.get(selectedItemId);
        assert selectedUser != null : "User is not exist";
        return selectedUser;
    }

    public UserAccount create(final String name, final String loginName)
        throws EscidocException, InternalClientException, TransportException {
        assert !(name == null || name.isEmpty()) : "name can not be null or empty";
        assert !(loginName == null || loginName.isEmpty()) : "Login name can not be null or empty";

        final UserAccount backedUserAccount =
            new UserAccountFactory().create(name, loginName).build();

        final UserAccount createdUserAccount =
            getSoapClient().create(backedUserAccount);
        assert createdUserAccount != null : "Got null reference from the server.";
        assert createdUserAccount.getObjid() != null : "ObjectID can not be null.";
        assert userAccountById != null : "userAccountById is null";
        final int sizeBefore = userAccountById.size();
        userAccountById.put(createdUserAccount.getObjid(), createdUserAccount);
        final int sizeAfter = userAccountById.size();
        assert sizeAfter > sizeBefore : "user account is not added to map.";
        return createdUserAccount;
    }

    public UserAccount delete(final String objectId) throws EscidocException,
        InternalClientException, TransportException {
        getSoapClient().delete(objectId);
        return userAccountById.remove(objectId);
    }

    public UserAccount getUserById(final String objectId) {
        userAccountById.containsKey(objectId);
        return userAccountById.get(objectId);
    }

    public Collection<Grant> retrieveCurrentGrants(final String objectId)
        throws InternalClientException, TransportException,
        EscidocClientException {
        return getRestClient().retrieveCurrentGrants(objectId).getGrants();
    }

    private UserAccountHandlerClient getRestClient() {
        client.setTransport(TransportProtocol.REST);
        return client;
    }

    public void assign(final String userId, final String roleId)
        throws EscidocClientException {
        final Grant grant = new Grant();
        final GrantProperties gProp = new GrantProperties();
        gProp.setRole(new RoleRef(roleId));
        grant.setGrantProperties(gProp);
        getSoapClient().createGrant(userId, grant);
    }

    public UserService assign(final UserAccount user) {
        if (user == null) {
            throw new IllegalArgumentException("UserAccount can not be null.");
        }
        this.user = user;
        return this;
    }

    public UserService withRole(final Role selectedRole) {
        if (selectedRole == null) {
            throw new IllegalArgumentException("Role can not be null.");
        }
        if (user == null) {
            throw new IllegalArgumentException(
                "You must sign a role to a user.");
        }
        grantProps = new GrantProperties();
        grantProps.setRole(new RoleRef(selectedRole.getObjid()));
        return this;
    }

    public UserService onResources(final Set<RoleRef> selectedResources) {
        for (final RoleRef resourceRef : selectedResources) {
            grantProps.setAssignedOn(resourceRef);
        }
        return this;
    }

    public void execute() throws EscidocClientException {
        final Grant grant = new Grant();
        grant.setGrantProperties(grantProps);
        getSoapClient().createGrant(user.getObjid(), grant);
    }

    public void revokeGrant(
        final String userId, final Grant grant, final String comment)
        throws EscidocClientException {
        final TaskParam tp = new TaskParam();
        tp.setLastModificationDate(grant.getLastModificationDate());
        tp.setComment(comment);
        getSoapClient().revokeGrant(userId, grant.getObjid(), tp);
    }
}