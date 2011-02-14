package de.escidoc.admintool.view.factory;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.role.RoleView;

public class RoleViewFactory {

    private final AdminToolApplication app;

    private final RoleService roleService;

    private final UserService userService;

    private final ContextService contextService;

    private RoleView roleView;

    private final ServiceContainer services;

    public RoleViewFactory(final AdminToolApplication app,
        final RoleService roleService, final UserService userService,
        final ContextService contextService, final ServiceContainer services) {
        this.app = app;
        this.roleService = roleService;
        this.userService = userService;
        this.contextService = contextService;
        this.services = services;
    }

    public RoleView getRoleView() {
        if (roleView == null) {
            roleView =
                new RoleView(app, roleService, userService, contextService,
                    services);
        }
        return roleView;
    }
}