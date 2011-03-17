package de.escidoc.admintool.view.navigation;

import com.google.common.base.Preconditions;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;

public class NavigationTreeClickListener implements ItemClickListener {

    private static final long serialVersionUID = 3387642828574003867L;

    private final AdminToolApplication app;

    private ExpandCollapseCommand command;

    public NavigationTreeClickListener(final AdminToolApplication app) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.app = app;
    }

    public void setCommand(final ExpandCollapseCommand command) {
        Preconditions.checkNotNull(command, "command is null: %s", command);
        this.command = command;
    }

    @Override
    public void itemClick(final ItemClickEvent event) {
        Preconditions.checkNotNull(event, "event is null: %s", event);
        showClickedView(event.getItemId());
    }

    private void showClickedView(final Object itemId) {
        if (isNullAndNotString(itemId)) {
            return;
        }
        else if (ViewConstants.CONTEXTS.equals(itemId)) {
            app.showContextView();
        }
        else if (ViewConstants.ORG_UNITS.equals(itemId)) {
            app.showResourceView();
        }
        else if (ViewConstants.CONTENT_MODELS.equals(itemId)) {
            app.showContentModelView();
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
        else if (ViewConstants.RESOURCES.equals(itemId)
            || ViewConstants.ADMIN_TASKS_LABEL.equals(itemId)) {
            command.execute(itemId);
        }
        else {
            throw new IllegalArgumentException("Unknown type.");
        }
    }

    private boolean isNullAndNotString(final Object itemId) {
        return (itemId == null) || !(itemId instanceof String);
    }
}