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
import de.escidoc.vaadin.dialog.ErrorDialog;

@SuppressWarnings("serial")
public class UserLabListView extends Table {

    private static final Logger log =
        LoggerFactory.getLogger(UserLabListView.class);

    private final AdminToolApplication app;

    private final UserService userService;

    private Collection<UserAccount> allUserAccounts;

    private POJOContainer<UserAccount> userContainer;

    public UserLabListView(final AdminToolApplication app,
        final UserService userService) {
        assert app != null : "app must not be null.";
        assert userService != null : "userService must not be null.";
        this.app = app;
        this.userService = userService;
        buildView();
        findAllUsers();
        bindDataSource();
    }

    private void buildView() {
        setSizeFull();
        setColumnCollapsingAllowed(false);
        setColumnReorderingAllowed(false);

        setSelectable(true);
        setImmediate(true);
        addListener((ValueChangeListener) app);
        setNullSelectionAllowed(false);
    }

    private void findAllUsers() {
        try {
            allUserAccounts = userService.findAll();
        }
        catch (final EscidocClientException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See log for details."));
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
    }

    private void bindDataSource() {
        if (isUserExists()) {
            initUserContainer();
        }
    }

    private boolean isUserExists() {
        return !allUserAccounts.isEmpty();
    }

    private void initUserContainer() {
        userContainer =
            new POJOContainer<UserAccount>(allUserAccounts,
                PropertyId.OBJECT_ID, PropertyId.NAME, PropertyId.CREATED_ON,
                PropertyId.CREATED_BY, PropertyId.LAST_MODIFICATION_DATE,
                PropertyId.MODIFIED_BY, "properties.loginName",
                "properties.active");
        setContainerDataSource(userContainer);
        setVisibleColumns(new Object[] { "properties.name" });
        setColumnHeader(PropertyId.NAME, ViewConstants.TITLE_LABEL);
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { false });
    }

    public void remove(final UserAccount deletedUser) {
        final boolean removeItem = userContainer.removeItem(deletedUser);
        assert removeItem == true : "Failed to remove user account from the container";
    }

    public void addUser(final UserAccount createdUserAccount) {
        userContainer.addItem(createdUserAccount);
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { true });
    }
}