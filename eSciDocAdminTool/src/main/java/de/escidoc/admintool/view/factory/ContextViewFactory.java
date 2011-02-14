package de.escidoc.admintool.view.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.AddOrgUnitToTheList;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextEditForm;
import de.escidoc.admintool.view.context.ContextListView;
import de.escidoc.admintool.view.context.ContextView;
import de.escidoc.admintool.view.resource.ResourceTreeView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class ContextViewFactory {

    private static final Logger LOG = LoggerFactory
        .getLogger(ContextViewFactory.class);

    private final Window mainWindow;

    private final OrgUnitService orgUnitService;

    private final AdminToolApplication app;

    private final ContextService contextService;

    public ContextViewFactory(final AdminToolApplication app,
        final Window mainWindow, final OrgUnitService orgUnitService,
        final ContextService contextService) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(contextService,
            "contextService is null: %s", contextService);
        this.app = app;
        this.mainWindow = mainWindow;
        this.orgUnitService = orgUnitService;
        this.contextService = contextService;

    }

    private ContextListView contextList;

    private ContextEditForm contextForm;

    private ContextView contextView;

    private ContextAddView contextAddView;

    public ContextView createContextView(final ResourceTreeView resourceTreeView) {
        try {
            createContextListView();
            createContextEditForm(resourceTreeView);
            contextForm.setContextList(contextList);
            createContextAddView(resourceTreeView);
            return new ContextView(app, contextList, contextForm,
                contextAddView);
        }
        catch (final EscidocException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        return contextView;
    }

    public ContextAddView createContextAddView(
        final ResourceTreeView resourceTreeView) {
        contextAddView =
            new ContextAddView(app, mainWindow, contextList, contextService,
                new AddOrgUnitToTheList(mainWindow, resourceTreeView));
        return contextAddView;
    }

    private void createContextEditForm(final ResourceTreeView resourceTreeView) {
        contextForm =
            new ContextEditForm(app, mainWindow, contextService,
                orgUnitService, new AddOrgUnitToTheList(mainWindow,
                    resourceTreeView));
    }

    private void createContextListView() throws EscidocException,
        InternalClientException, TransportException {
        contextList = new ContextListView(app, contextService);
    }
}