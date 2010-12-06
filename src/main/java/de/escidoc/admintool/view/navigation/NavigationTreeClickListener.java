package de.escidoc.admintool.view.navigation;

import com.google.common.base.Preconditions;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;

class NavigationTreeClickListener implements ItemClickListener {

    private static final long serialVersionUID = 3387642828574003867L;

    private final AdminToolApplication app;

    public NavigationTreeClickListener(final AdminToolApplication app) {
        this.app = app;
    }

    @Override
    public void itemClick(final ItemClickEvent event) {
        Preconditions.checkNotNull(event, "event is null: %s", event);

        final Object itemId = event.getItemId();

        if (itemId == null || !(itemId instanceof String)) {
            return;
        }
        else if (ViewConstants.RESOURCES.equals(itemId)
            || ViewConstants.ADMIN_TASKS_LABEL.equals(itemId)) {
            return;
        }
        else if (ViewConstants.CONTEXTS.equals(itemId)) {
            app.showContextView();
        }
        else if (ViewConstants.LAB.equals(itemId)) {
            app.showResourceView();
        }
        else if (ViewConstants.USERS.equals(itemId)) {
            app.showUserView();
        }
        else if (ViewConstants.ROLE.equals(itemId)) {
            app.showRoleView();
        }
        else if (ViewConstants.LOAD_EXAMPLES_TITLE.equals(itemId)) {
            app.showLoadExampleView();
        }
        else if (ViewConstants.REINDEX.equals(itemId)) {
            app.showReindexView();
        }
        else if (ViewConstants.SHOW_REPOSITORY_INFO.equals(itemId)) {
            app.showRepoInfoView();
        }
        else if (ViewConstants.FILTERING_RESOURCES_TITLE.equals(itemId)) {
            app.showFilterResourceView();
        }
        else {
            throw new IllegalArgumentException("Unknown type.");
        }

    }
}