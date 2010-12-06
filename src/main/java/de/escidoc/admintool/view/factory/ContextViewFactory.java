// package de.escidoc.admintool.view.factory;
//
// import com.google.common.base.Preconditions;
// import com.vaadin.ui.Window;
// import de.escidoc.admintool.app.AdminToolApplication;
// import de.escidoc.admintool.service.ContextService;
// import de.escidoc.admintool.service.OrgUnitService;
// import de.escidoc.admintool.view.context.ContextAddView;
// import de.escidoc.admintool.view.context.ContextEditForm;
// import de.escidoc.admintool.view.context.ContextListView;
// import de.escidoc.admintool.view.context.ContextView;
// import de.escidoc.admintool.view.resource.ModalWindow;
// import de.escidoc.core.client.exceptions.EscidocException;
// import de.escidoc.core.client.exceptions.InternalClientException;
// import de.escidoc.core.client.exceptions.TransportException;
//
// public class ContextViewFactory {
//
// private ContextListView contextList;
//
// private ContextEditForm contextForm;
//
// private final AdminToolApplication app;
//
// private final ContextService contextService;
//
// private final OrgUnitService orgUnitService;
//
// private ContextView contextView;
//
// private Window mainWindow;
//
// private ModalWindow modalWindow;
// private ContextAddView contextAddView;
//
// public ContextViewFactory(final AdminToolApplication app,
// final Window mainWindow, final ContextService contextService,
// final OrgUnitService orgUnitService) {
//
// Preconditions.checkNotNull(app, "app can not be null: %s", app);
// Preconditions.checkNotNull(mainWindow,
// "mainWindow can not be null: %s", mainWindow);
// Preconditions.checkNotNull(contextService,
// " contextService can not be null: %s", contextService);
// Preconditions.checkNotNull(orgUnitService,
// " orgUnitService can not be null: %s", orgUnitService);
//
// this.app = app;
// this.mainWindow = mainWindow;
// this.contextService = contextService;
// this.orgUnitService = orgUnitService;
// }
//
// public ContextView getContexView() throws EscidocException,
// InternalClientException, TransportException {
// if (contextView == null) {
// contextView = createContextView();
// }
// return contextView;
// }
//
// public ContextView createContextView() throws EscidocException,
// InternalClientException, TransportException {
// createListView();
// createContextEditForm();
// contextForm.setContextList(contextList);
// createContextAddView();
// return new ContextView(app, contextList, contextForm, contextAddView);
// }
//
// private void createContextAddView() {
// contextAddView = new ContextAddView(mainWindow, contextList, contextService);
// }
//
// private void createContextEditForm() {
// contextForm =
// new ContextEditForm(app, mainWindow, contextService, orgUnitService);
// }
//
// private void createListView() throws EscidocException,
// InternalClientException, TransportException {
// contextList = new ContextListView(app, contextService);
// }
// }
