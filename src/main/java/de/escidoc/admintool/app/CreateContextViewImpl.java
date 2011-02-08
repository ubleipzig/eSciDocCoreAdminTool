package de.escidoc.admintool.app;

import com.vaadin.data.Item;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.context.AddOrgUnitToTheList;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextEditForm;
import de.escidoc.admintool.view.context.ContextListView;
import de.escidoc.admintool.view.context.ContextView;
import de.escidoc.admintool.view.resource.ResourceTreeView;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class CreateContextViewImpl {

    private final ContextListView contextList;

    private final ContextEditForm contextForm;

    private final ContextView contextView;

    public CreateContextViewImpl(final AdminToolApplication app,
        final ContextService contextService, final Window mainWindow,
        final ResourceTreeView createResourceTreeView,
        final OrgUnitService orgUnitService) throws EscidocClientException {

        contextList = new ContextListView(app, contextService);
        final ResourceTreeView resourceTreeView = createResourceTreeView;
        contextForm =
            new ContextEditForm(app, mainWindow, contextService,
                orgUnitService, new AddOrgUnitToTheList(mainWindow,
                    resourceTreeView));
        contextForm.setContextList(contextList);

        final ContextAddView contextAddView =
            new ContextAddView(app, mainWindow, contextList, contextService,
                new AddOrgUnitToTheList(mainWindow, resourceTreeView));

        contextView =
            new ContextView(app, contextList, contextForm, contextAddView);

        if (contextList.getContainerDataSource() != null
            && contextList.getContainerDataSource().size() > 0) {
            showFirstItemInEditView();
        }
        else {
            contextView.showAddView();
        }
    }

    private void showFirstItemInEditView() {
        final Item item =
            contextList.getContainerDataSource().getItem(
                contextList.firstItemId());
        contextList.select(contextList.firstItemId());
        contextView.showEditView(item);
    }

    public ContextView getContextView() {
        return contextView;
    }
}
