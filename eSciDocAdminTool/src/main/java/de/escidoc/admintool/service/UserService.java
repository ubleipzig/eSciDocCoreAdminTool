package de.escidoc.admintool.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;

import de.escidoc.admintool.domain.UserAccountFactory;
import de.escidoc.core.client.UserAccountHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.TaskParam;

public class UserService {

    private static final String ESCIDOC_SERVICE_ROOT_URI =
        "http://localhost:8080";

    private final UserAccountHandlerClient client;

    private Collection<UserAccount> userAccounts;

    public UserService(final String handle) throws EscidocException,
        InternalClientException, TransportException {
        client = createUserClient(handle);
    }

    private UserAccountHandlerClient createUserClient(final String handle)
        throws EscidocException, InternalClientException, TransportException {
        final UserAccountHandlerClient client = new UserAccountHandlerClient();
        client.setHandle(handle);
        client.setServiceAddress(ESCIDOC_SERVICE_ROOT_URI);
        return client;
    }

    private final Map<String, UserAccount> userAccountById =
        new ConcurrentHashMap<String, UserAccount>();

    @SuppressWarnings("deprecation")
    public Collection<UserAccount> findAll() throws EscidocClientException {
        userAccounts =
            client.retrieveUserAccounts(emptyFilter()).getUserAccounts();
        for (final UserAccount user : userAccounts) {
            userAccountById.put(user.getObjid(), user);
        }
        return userAccounts;
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

    public UserAccount retrieve(final String userObjectId)
        throws EscidocException, InternalClientException, TransportException {
        return client.retrieve(userObjectId);
    }

    public void update(final String objid, final String newName)
        throws EscidocException, InternalClientException, TransportException {
        assert !(newName == null || newName.isEmpty()) : "name must not be null or empty";

        // TODO name the class with its responsibility
        final UserAccount updatedUserAccount =
            new UserAccountFactory().update(getSelectedUser(objid)).name(
                newName).build();

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
        assert userAccount.getProperties().isActive() == false : "User account is already active.";

        client.activate(userAccount.getObjid(),
            lastModificationDate(userAccount));
    }

    public void deactivate(final String selectedItemId)
        throws InternalClientException, TransportException,
        EscidocClientException {
        assert !(selectedItemId == null || selectedItemId.isEmpty()) : "selectedItemId must not be null or empty";

        final UserAccount userAccount = getSelectedUser(selectedItemId);
        assert userAccount.getProperties().isActive() == true : "User account is not active.";

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

    public UserAccount delete(final String objectId) throws EscidocException,
        InternalClientException, TransportException {
        client.delete(objectId);
        return userAccountById.remove(objectId);
    }

    public UserAccount getUserById(final String objectId) {
        userAccountById.containsKey(objectId);
        return userAccountById.get(objectId);
    }

    public Collection<Grant> retrieveCurrentGrants(final String objectId)
        throws InternalClientException, TransportException,
        EscidocClientException {
        return client.retrieveCurrentGrants(objectId).getGrants();
    }
}