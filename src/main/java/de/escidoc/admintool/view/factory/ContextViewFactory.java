package de.escidoc.admintool.view.factory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextEditForm;
import de.escidoc.admintool.view.context.ContextListView;
import de.escidoc.admintool.view.context.ContextView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class ContextViewFactory {

    private ContextListView contextList;

    private ContextEditForm contextForm;

    private final AdminToolApplication app;

    private final ContextService contextService;

    private final OrgUnitService orgUnitService;

    private ContextView contextView;

    private Window mainWindow;

    public ContextViewFactory(final AdminToolApplication app,
        final Window mainWindow, final ContextService contextService,
        final OrgUnitService orgUnitService) {

        Preconditions.checkNotNull(app, "app can not be null: %s", app);
        Preconditions.checkNotNull(mainWindow,
            "mainWindow can not be null: %s", mainWindow);
        Preconditions.checkNotNull(contextService,
            " contextService can not be null: %s", contextService);
        Preconditions.checkNotNull(orgUnitService,
            " orgUnitService can not be null: %s", orgUnitService);

        this.app = app;
        this.contextService = contextService;
        this.orgUnitService = orgUnitService;
    }

    public ContextView getContexView() throws EscidocException,
        InternalClientException, TransportException {
        if (contextView == null) {
            contextView = create();
        }
        return contextView;
    }

    public ContextView create() throws EscidocException,
        InternalClientException, TransportException {
        contextList = new ContextListView(app, contextService);
        contextForm =
            new ContextEditForm(app, mainWindow, contextService, orgUnitService);
        contextForm.setContextList(contextList);
        final ContextAddView contextAddView =
            new ContextAddView(app, contextList, contextService, orgUnitService);
        return new ContextView(app, contextList, contextForm, contextAddView);
    }
}
