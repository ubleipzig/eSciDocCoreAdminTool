package de.escidoc.admintool.view.user;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.resource.ResourceTreeView;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class UserViewComponent {

    private UserEditForm userEditForm;

    private UserListView userListView;

    private UserView userView;

    private UserEditView userEditView;

    private final AdminToolApplication app;

    private final UserService userService;

    private UserListView listView;

    private UserEditForm editForm;

    private final OrgUnitServiceLab orgUnitService;

    private final ResourceTreeView resourceTreeView;

    public UserViewComponent(final AdminToolApplication app,
        final UserService userService, final ResourceService orgUnitServiceLab,
        final ResourceTreeView resourceTreeView) {
        Preconditions
            .checkNotNull(app, "AdminToolApplication can not be null.");
        Preconditions.checkNotNull(userService, "UserService can not be null.");
        Preconditions.checkNotNull(orgUnitServiceLab,
            "orgUnitService is null: %s", orgUnitServiceLab);
        this.app = app;
        this.userService = userService;
        orgUnitService = (OrgUnitServiceLab) orgUnitServiceLab;
        this.resourceTreeView = resourceTreeView;
    }

    public void init() {
        listView = new UserListView(app, userService);
        setUserListView(listView);
        editForm =
            new UserEditForm(app, userService, orgUnitService, resourceTreeView);
        setUserEditForm(editForm);
        setUserEditView(new UserEditView(getUserEditForm()));
        userView = new UserView(app, getUserListView(), getUserEditView());
        setUserView(userView);
    }

    /**
     * @return the userListView
     */
    public UserListView getUserListView() {
        return userListView;
    }

    /**
     * @param userListView
     *            the userListView to set
     */
    public void setUserListView(final UserListView userListView) {
        this.userListView = userListView;
    }

    /**
     * @return the userView
     */
    public UserView getUserView() {
        return userView;
    }

    public void showFirstItemInEditView() {
        final Item item =
            listView.getContainerDataSource().getItem(listView.firstItemId());
        listView.select(listView.firstItemId());
        userView.showEditView(item);
    }

    /**
     * @param userView
     *            the userView to set
     */
    public void setUserView(final UserView userView) {
        this.userView = userView;
    }

    public void setUserEditForm(final UserEditForm userEditForm) {
        this.userEditForm = userEditForm;
    }

    public UserEditForm getUserEditForm() {
        return userEditForm;
    }

    public void setUserEditView(final UserEditView userEditView) {
        this.userEditView = userEditView;
    }

    /**
     * @return the userEditView
     */
    public UserEditView getUserEditView() {
        return userEditView;
    }

    public void showAddView() {
        userView.showAddView();
    }

    public void showUserInEditView(final UserAccount user) {
        final Item item = listView.getContainerDataSource().getItem(user);
        listView.select(user);
        userView.showEditView(item);
    }
}