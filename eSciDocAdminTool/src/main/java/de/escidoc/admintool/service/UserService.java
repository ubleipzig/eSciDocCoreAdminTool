package de.escidoc.admintool.service;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.domain.UserAccountFactory;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.UserAccountHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.UserAccountHandlerClientInterface;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.aa.useraccount.Attribute;
import de.escidoc.core.resources.aa.useraccount.Attributes;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.GrantProperties;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.common.reference.Reference;
import de.escidoc.core.resources.common.reference.RoleRef;

public class UserService {

    private UserAccountHandlerClientInterface client;

    private final Map<String, UserAccount> userAccountById =
        new ConcurrentHashMap<String, UserAccount>();

    private final String eSciDocUri;

    private final String handle;

    private Collection<UserAccount> userAccounts;

    private UserAccount user;

    private GrantProperties grantProps;

    public UserService(final String eSciDocUri, final String handle)
        throws InternalClientException {
        this.eSciDocUri = eSciDocUri;
        this.handle = handle;
        initClient();
    }

    private void initClient() throws InternalClientException {
        client = new UserAccountHandlerClient(eSciDocUri);
        client.setTransport(TransportProtocol.REST);
        client.setHandle(handle);
    }

    public Collection<UserAccount> findAll() throws EscidocClientException {
        userAccounts =
            client.retrieveUserAccountsAsList(new SearchRetrieveRequestType());
        for (final UserAccount user : userAccounts) {
            userAccountById.put(user.getObjid(), user);
        }
        return userAccounts;
    }

    public UserAccount retrieve(final String userObjectId)
        throws EscidocException, InternalClientException, TransportException {
        return client.retrieve(userObjectId);
    }

    public void update(final String objid, final String newName)
        throws EscidocClientException {
        assert !(newName == null || newName.isEmpty()) : "name must not be null or empty";

        // TODO name the class with its responsibility
        final UserAccount updatedUserAccount =
            new UserAccountFactory()
                .update(getSelectedUser(objid)).name(newName).build();

        client.update(updatedUserAccount);
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

        client.activate(userAccount.getObjid(),
            lastModificationDate(userAccount));
    }

    public void deactivate(final String selectedItemId)
        throws InternalClientException, TransportException,
        EscidocClientException {
        assert !(selectedItemId == null || selectedItemId.isEmpty()) : "selectedItemId must not be null or empty";

        final UserAccount userAccount = getSelectedUser(selectedItemId);
        assert userAccount.getProperties().isActive() : "User account is not active.";

        client.deactivate(userAccount.getObjid(),
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

        final UserAccount createdUserAccount = client.create(backedUserAccount);
        assert createdUserAccount != null : "Got null reference from the server.";
        assert createdUserAccount.getObjid() != null : "ObjectID can not be null.";
        assert userAccountById != null : "userAccountById is null";
        final int sizeBefore = userAccountById.size();
        userAccountById.put(createdUserAccount.getObjid(), createdUserAccount);
        final int sizeAfter = userAccountById.size();
        assert sizeAfter > sizeBefore : "user account is not added to map.";
        return createdUserAccount;
    }

    public UserAccount delete(final String objectId)
        throws EscidocClientException {
        client.delete(objectId);
        return userAccountById.remove(objectId);
    }

    public UserAccount getUserById(final String objectId)
        throws EscidocClientException {

        if (userAccounts == null) {
            findAll();
        }

        return userAccountById.get(objectId);
    }

    public Collection<Grant> retrieveCurrentGrants(final String objectId)
        throws InternalClientException, TransportException,
        EscidocClientException {
        return getRestClient().retrieveCurrentGrants(objectId).getGrants();
    }

    private UserAccountHandlerClientInterface getRestClient() {
        client.setTransport(TransportProtocol.REST);
        return client;
    }

    public void assign(final String userId, final String roleId)
        throws EscidocClientException {
        final Grant grant = new Grant();
        final GrantProperties gProp = new GrantProperties();
        gProp.setRole(new RoleRef(roleId));
        grant.setGrantProperties(gProp);
        client.createGrant(userId, grant);
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

    public UserService onResources(final Set<ContextRef> selectedResources) {
        for (final Reference resourceRef : selectedResources) {
            grantProps.setAssignedOn(resourceRef);
        }
        return this;
    }

    public void execute() throws EscidocClientException {
        final Grant grant = new Grant();
        grant.setGrantProperties(grantProps);
        client.createGrant(user.getObjid(), grant);
    }

    public void revokeGrant(
        final String userId, final Grant grant, final String comment)
        throws EscidocClientException {
        final TaskParam tp = new TaskParam();
        tp.setLastModificationDate(grant.getLastModificationDate());
        tp.setComment(comment);
        client.revokeGrant(userId, grant.getObjid(), tp);
    }

    public void updatePassword(final UserAccount user, final String newPassword)
        throws EscidocClientException {

        preconditions(user, newPassword);
        client.updatePassword(user.getObjid(), with(user, newPassword));
    }

    public void updatePassword(
        final String userId, final String newPassword,
        final DateTime lastModificationDate) throws EscidocClientException {
        preconditions(userId, newPassword, lastModificationDate);

        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(lastModificationDate);
        taskParam.setPassword(newPassword);

        client.updatePassword(userId, taskParam);
    }

    private void preconditions(
        final String userId, final String newPassword,
        final DateTime lastModificationDate) {

        Preconditions.checkNotNull(userId, "userId is null: %s", userId);
        Preconditions.checkNotNull(newPassword, "newPassword is null: %s",
            newPassword);
        Preconditions.checkNotNull(lastModificationDate,
            "lastModificationDate is null: %s", lastModificationDate);

        Preconditions.checkArgument(!userId.isEmpty(), "userId is empty: %s",
            userId);
        Preconditions.checkArgument(!newPassword.isEmpty(),
            "newPassword is empty: %s", newPassword);
    }

    private TaskParam with(final UserAccount user, final String newPassword) {
        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(user.getLastModificationDate());
        taskParam.setPassword(newPassword);
        return taskParam;
    }

    private void preconditions(final UserAccount user, final String newPassword) {

        Preconditions.checkNotNull(user, "user is null: %s", user);
        Preconditions.checkNotNull(newPassword, "newPassword is null: %s",
            newPassword);

        Preconditions.checkNotNull(user.getObjid(),
            "user.getObjid() is null: %s", user.getObjid());
        Preconditions.checkArgument(!newPassword.isEmpty(),
            "newPassword is empty: %s", newPassword);
    }

    public List<String> retrieveOrgUnitsFor(final String objectId)
        throws EscidocClientException {
        Preconditions.checkNotNull(objectId, "objectId is null: %s", objectId);
        Preconditions.checkArgument(!objectId.isEmpty(), objectId,
            "objectId is empty: %s");

        final List<String> orgUnits = new ArrayList<String>();
        for (final Attribute attribute : client.retrieveAttributes(objectId)) {
            if (nameIsEqualsO(attribute)) {
                orgUnits.add(attribute.getValue());
            }
        }

        return orgUnits;
    }

    public Attributes retrieveAttributes(final String objectId)
        throws EscidocClientException {
        return client.retrieveAttributes(objectId);
    }

    private boolean nameIsEqualsO(final Attribute attribute) {
        return !attribute.getName().isEmpty()
            && AppConstants.ORGANIZATIONAL_UNIT_DEFAULT_ATTRIBUTE_NAME
                .equals(attribute.getName());
    }

    public void assign(final String objectId, final Attribute attribute)
        throws EscidocClientException {
        Preconditions.checkNotNull(objectId, "objectId is null: %s", objectId);
        Preconditions.checkNotNull(attribute, "attribute is null: %s",
            attribute);
        client.createAttribute(objectId, attribute);
    }

    public void updateAttribute(final String objectId, final Attribute attribute)
        throws EscidocClientException {
        Preconditions.checkNotNull(objectId, "objectId is null: %s", objectId);
        Preconditions.checkNotNull(attribute, "attribute is null: %s",
            attribute);
        client.updateAttribute(objectId, attribute);
    }

    public UserAccount getCurrentUser() throws EscidocClientException {
        return client.retrieveCurrentUser();
    }

    public void removeAttribute(final String objectId, final Attribute attribute)
        throws EscidocClientException {
        Preconditions.checkNotNull(objectId, "objectId is null: %s", objectId);
        Preconditions.checkNotNull(attribute, "attribute is null: %s",
            attribute);
        client.deleteAttribute(objectId, attribute);
    }

}