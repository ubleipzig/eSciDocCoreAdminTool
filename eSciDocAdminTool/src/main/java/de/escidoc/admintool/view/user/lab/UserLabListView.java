package de.escidoc.admintool.view.user.lab;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class UserLabListView extends Table {

    private static final Logger log =
        LoggerFactory.getLogger(UserLabListView.class);

    private final AdminToolApplication app;

    private final UserService userService;

    private POJOContainer<UserAccount> pojoContainer;

    public UserLabListView(final AdminToolApplication app,
        final UserService userService) {
        assert app != null : "app must not be null.";
        assert userService != null : "userService must not be null.";

        this.app = app;
        this.userService = userService;
        buildUI();
        bindDataSource();
    }

    private void buildUI() {
        addStyleName("view");
        setSizeFull();
        setColumnCollapsingAllowed(false);
        setColumnReorderingAllowed(false);

        setSelectable(true);
        setImmediate(true);
        addListener((ValueChangeListener) app);

        /* We don't want to allow users to de-select a row */
        setNullSelectionAllowed(false);
    }

    // TODO handle exceptions
    private void bindDataSource() {
        Collection<UserAccount> allUserAccounts;
        try {
            allUserAccounts = userService.findAll();
            pojoContainer =
                new POJOContainer<UserAccount>(allUserAccounts,
                    ViewConstants.NAME_ID, "properties.loginName", "objid",
                    "properties.active", "properties.creationDate",
                    "properties.createdBy.objid", "lastModificationDate",
                    "properties.modifiedBy.objid");
            sort(new Object[] { "lastModificationDate" },
                new boolean[] { false });
            setContainerDataSource(pojoContainer);
            setColumnHeaders(new String[] { ViewConstants.NAME_LABEL,
                ViewConstants.LOGIN_NAME_LABEL, ViewConstants.OBJECT_ID_LABEL,
                ViewConstants.MODIFIED_ON_LABEL,
                ViewConstants.MODIFIED_BY_LABEL,
                ViewConstants.CREATED_ON_LABEL, ViewConstants.CREATED_BY_LABEL,
                ViewConstants.IS_ACTIVE_LABEL, });
            setVisibleColumns(new Object[] { "properties.name" });
        }
        catch (final EscidocClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void remove(final UserAccount deletedUser) {
        final boolean removeItem = pojoContainer.removeItem(deletedUser);
        assert removeItem == true : "Failed to remove user account from the container";
    }

    public void debugDelete(final String selectedItemId) {
        // final boolean containsId =
        // pojoContainer.containsId(userService.getUserById(selectedItemId));
        // System.out.println("containsID: " + containsId);
        // pojoContainer.removeItem(containsId);
        // System.out.println("containsID: " + containsId);
        final Collection<UserAccount> itemIds = pojoContainer.getItemIds();
        final UserAccount next = itemIds.iterator().next();
        final boolean removeItem = pojoContainer.removeItem(next);
        log.info("deleted: " + removeItem);
    }

    public void addUser(final UserAccount createdUserAccount) {
        pojoContainer.addItem(createdUserAccount);
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { false });
    }
}