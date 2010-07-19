package de.escidoc.admintool.view.user;

//TODO move the commented line after development is finished.
//import static de.escidoc.admintool.ui.ViewConstants.*;
import java.util.Collection;
import java.util.Date;

import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class UserList extends Table {

    private final AdminToolApplication app;

    private final UserService service;

    public UserList(final AdminToolApplication app, final UserService service)
        throws EscidocClientException {

        assert app != null : "app must not be null.";
        assert service != null : "service must not be null.";

        this.app = app;
        this.service = service;

        buildUI();
        bindDataSource();
    }

    private void buildUI() {
        setSizeFull();
        setColumnCollapsingAllowed(true);
        setColumnReorderingAllowed(true);

        setSelectable(true);
        setImmediate(true);
        this.addListener((ValueChangeListener) app);

        /* We don't want to allow users to de-select a row */
        setNullSelectionAllowed(false);
    }

    private void bindDataSource() throws EscidocClientException {
        addColumnsHeader();
        fillTable(service.all());
        this.setColumnHeaders();
        // this.setColumnWidth(ViewConstants.NAME_ID, 400);
        collapseColumns();
    }

    // NOTE: this method should be call only after container property
    // already defined
    private void setColumnHeaders() {
        this.setColumnHeaders(new String[] { ViewConstants.NAME_LABEL,
            ViewConstants.LOGIN_NAME_LABEL, ViewConstants.OBJECT_ID_LABEL,
            ViewConstants.IS_ACTIVE_LABEL, ViewConstants.CREATED_ON_LABEL,
            ViewConstants.CREATED_BY_LABEL, ViewConstants.MODIFIED_ON_LABEL,
            ViewConstants.MODIFIED_BY_LABEL });
    }

    private void collapseColumns() {
        try {
            setColumnCollapsed(ViewConstants.OBJECT_ID, true);
            setColumnCollapsed(ViewConstants.LOGIN_NAME_ID, true);
            setColumnCollapsed(ViewConstants.IS_ACTIVE_ID, true);
            setColumnCollapsed(ViewConstants.CREATED_ON_ID, true);
            setColumnCollapsed(ViewConstants.CREATED_BY_ID, true);
            setColumnCollapsed(ViewConstants.MODIFIED_ON_ID, true);
            setColumnCollapsed(ViewConstants.MODIFIED_BY_ID, true);
        }
        catch (final IllegalAccessException e) {
            // TODO log the exception
            e.printStackTrace();
        }
    }

    private void addColumnsHeader() {
        this.addContainerProperty(ViewConstants.NAME_ID, String.class, "");
        this
            .addContainerProperty(ViewConstants.LOGIN_NAME_ID, String.class, "");
        this.addContainerProperty(ViewConstants.OBJECT_ID, String.class, "");
        this
            .addContainerProperty(ViewConstants.IS_ACTIVE_ID, Boolean.class, "");
        this.addContainerProperty(ViewConstants.CREATED_ON_ID, Date.class, "");
        this
            .addContainerProperty(ViewConstants.CREATED_BY_ID, String.class, "");
        this.addContainerProperty(ViewConstants.MODIFIED_ON_ID, Date.class, "");
        this.addContainerProperty(ViewConstants.MODIFIED_BY_ID, String.class,
            "");
    }

    private void fillTable(final Collection<UserAccount> userAccounts) {
        for (final UserAccount user : userAccounts) {
            addUser(user);
        }
        this.sort(new Object[] { ViewConstants.MODIFIED_ON_ID },
            new boolean[] { false });
    }

    public void addUser(final UserAccount user) {
        assert user != null : "user must not be null.";
        // this.debug(user);
        assert !containsId(user.getObjid()) : "user already in the table";

        final Object itemId =
            this.addItem(new Object[] { user.getProperties().getName(),
                user.getProperties().getLoginName(), user.getObjid(),
                user.getProperties().isActive(),
                user.getProperties().getCreationDate().toDate(),
                user.getProperties().getCreatedBy().getObjid(),
                user.getLastModificationDate().toDate(),
                user.getProperties().getModifiedBy().getObjid() }, user
                .getObjid());
        assert itemId != null : "Adding item to the table failed.";
    }

    private void debug(final UserAccount user) {
        System.out.println(user.getProperties().getName()
            + user.getProperties().getLoginName() + user.getObjid()
            + user.getProperties().isActive()
            + user.getProperties().getCreationDate()
            + user.getProperties().getCreatedBy().getObjid()
            + user.getLastModificationDate()
            + user.getProperties().getModifiedBy().getObjid());
    }
}