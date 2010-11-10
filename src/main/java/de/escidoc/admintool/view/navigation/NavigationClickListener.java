package de.escidoc.admintool.view.navigation;

import com.google.common.base.Preconditions;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;

class NavigationClickListener implements ItemClickListener {

    private static final long serialVersionUID = 3387642828574003867L;

    private final AdminToolApplication app;

    public NavigationClickListener(final AdminToolApplication app) {
        this.app = app;
    }

    @Override
    public void itemClick(final ItemClickEvent event) {
        Preconditions.checkNotNull(event, "event is null: %s", event);

        final Object itemId = event.getItemId();

        if (itemId == null || !(itemId instanceof String)) {
            return;
        }

        else if (ViewConstants.RESOURCES.equals(itemId)) {
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
        else if (ViewConstants.LOAD_EXAMPLES_TITLE.equals(itemId)) {
            app.showAdminTaskView();
        }
        else if (ViewConstants.REINDEX.equals(itemId)) {
            app.showReindexView();
        }
        else if (ViewConstants.SHOW_REPOSITORY_INFO.equals(itemId)) {
            app.showAdminTaskView();
        }
        else if (ViewConstants.FILTERING_RESOURCES_TITLE.equals(itemId)) {
            app.showAdminTaskView();
        }
        else {
            throw new IllegalArgumentException("Unknown type.");
        }

    }
}