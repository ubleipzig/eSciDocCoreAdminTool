package de.escidoc.admintool.view;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.user.UserEditForm;
import de.escidoc.admintool.view.user.UserEditView;
import de.escidoc.admintool.view.user.UserListView;
import de.escidoc.admintool.view.user.UserView;

public class UserViewComponent {

    private UserEditForm userEditForm;

    private UserListView userListView;

    private UserView userView;

    private UserEditView userEditView;

    private final AdminToolApplication app;

    private final UserService userService;

    public UserViewComponent(final AdminToolApplication app,
        final UserService userService) {
        Preconditions
            .checkNotNull(app, "AdminToolApplication can not be null.");
        Preconditions.checkNotNull(userService, "UserService can not be null.");
        this.app = app;
        this.userService = userService;
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
        if (userView == null) {
            setUserListView(new UserListView(app, userService));
            setUserEditForm(new UserEditForm(app, userService));
            setUserEditView(new UserEditView(getUserEditForm()));
            setUserView(new UserView(app, getUserListView(), getUserEditView()));
        }
        // showAddView();
        return userView;
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
}