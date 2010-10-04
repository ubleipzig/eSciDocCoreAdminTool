package de.escidoc.admintool.app;

import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.user.UserEditForm;
import de.escidoc.admintool.view.user.UserEditView;
import de.escidoc.admintool.view.user.UserListView;
import de.escidoc.admintool.view.user.UserView;

public class UserViewFactory {

    private final AdminToolApplication app;

    private final UserService userService;

    private UserListView userListView;

    private UserEditForm userEditForm;

    private final RoleService roleService;

    private UserView userView;

    public UserViewFactory(final AdminToolApplication app,
        final UserService userService, final RoleService roleService) {
        this.app = app;
        this.userService = userService;
        this.roleService = roleService;
    }

    public UserView getUserView() {
        if (userView == null) {
            userView = create();
        }
        return userView;
    }

    private UserView create() {
        userListView = new UserListView(app, userService);
        userEditForm = new UserEditForm(app, userService, roleService);
        final UserEditView userEditView = new UserEditView(userEditForm);
        return new UserView(app, userListView, userEditView);
    }

}