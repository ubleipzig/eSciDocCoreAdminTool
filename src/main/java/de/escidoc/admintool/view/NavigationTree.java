package de.escidoc.admintool.view;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.messages.Messages;

@SuppressWarnings("serial")
public class NavigationTree extends Tree {

    // TODO refactor to enum
    private final String[] MENU_ACTIONS = { ViewConstants.ORGANIZATIONAL_UNIT,
        ViewConstants.CONTEXT, ViewConstants.USERS, ViewConstants.ROLE,
        ViewConstants.ADMIN_TASK };

    private final AdminToolApplication app;

    public NavigationTree(final AdminToolApplication app) {
        this.app = app;
        init();
    }

    private void init() {
        for (final String action : MENU_ACTIONS) {
            addItem(action);
            setChildrenAllowed(action, false);
        }
        setSelectable(true);
        setNullSelectionAllowed(true);
        addListener(new NavigationClickListener());
    }

    private class NavigationClickListener implements ItemClickListener {

        @Override
        public void itemClick(final ItemClickEvent event) {
            final Object itemId = event.getItemId();
            if (itemId == null) {
                return;
            }
            else if (ViewConstants.CONTEXT.equals(itemId)) {
                app.showContextView();
            }
            else if (ViewConstants.CONTAINER.equals(itemId)) {
                app.showContainerView();
            }
            else if (ViewConstants.USERS.equals(itemId)) {
                app.showUserView();
            }
            else if (ViewConstants.ROLE.equals(itemId)) {
                app.showRoleView();
            }
            else if (ViewConstants.ORGANIZATIONAL_UNIT.equals(itemId)) {
                app.showOrgUnitViewLab();
            }
            else if (ViewConstants.ADMIN_TASK.equals(itemId)) {
                app.showAdminTaskView();
            }

            else {
                throw new IllegalArgumentException(
                    Messages.getString("AdminToolApplication.10"));
            }
        }
    }
}