package de.escidoc.admintool.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.ContextServiceLab;
import de.escidoc.admintool.service.EscidocService;
import de.escidoc.admintool.service.ItemService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.service.ServiceContaiterImpl;
import de.escidoc.admintool.service.ServiceFactory;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.MainView;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.ViewManager;
import de.escidoc.admintool.view.ViewManagerImpl;
import de.escidoc.admintool.view.admintask.AdminTaskView;
import de.escidoc.admintool.view.admintask.AdminTaskViewImpl;
import de.escidoc.admintool.view.admintask.FilterView;
import de.escidoc.admintool.view.admintask.LoadExample;
import de.escidoc.admintool.view.admintask.ReindexView;
import de.escidoc.admintool.view.admintask.RepositoryInfoFooView;
import de.escidoc.admintool.view.context.AddOrgUnitToTheList;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextEditForm;
import de.escidoc.admintool.view.context.ContextListView;
import de.escidoc.admintool.view.context.ContextView;
import de.escidoc.admintool.view.lab.orgunit.OrgUnitViewLabFactory;
import de.escidoc.admintool.view.login.WelcomePage;
import de.escidoc.admintool.view.orgunit.OrgUnitAddView;
import de.escidoc.admintool.view.orgunit.OrgUnitEditView;
import de.escidoc.admintool.view.orgunit.OrgUnitListView;
import de.escidoc.admintool.view.orgunit.OrgUnitView;
import de.escidoc.admintool.view.resource.AddChildrenCommandImpl;
import de.escidoc.admintool.view.resource.FolderHeaderImpl;
import de.escidoc.admintool.view.resource.ResourceContainer;
import de.escidoc.admintool.view.resource.ResourceTreeView;
import de.escidoc.admintool.view.resource.ResourceViewComponent;
import de.escidoc.admintool.view.resource.ResourceViewComponentImpl;
import de.escidoc.admintool.view.role.RoleView;
import de.escidoc.admintool.view.user.UserAddView;
import de.escidoc.admintool.view.user.UserView;
import de.escidoc.admintool.view.user.UserViewComponent;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class AdminToolApplication extends Application {

    private static final Logger LOG = LoggerFactory
        .getLogger(AdminToolApplication.class);

    private final Window mainWindow = new Window(
        ViewConstants.MAIN_WINDOW_TITLE);

    private final ViewManager viewManager = new ViewManagerImpl(mainWindow);

    private final ServiceContainer services = new ServiceContaiterImpl();

    private final VerticalLayout appLayout = new VerticalLayout();

    private UserViewComponent userViewComp;

    private ContextService contextService;

    private OrgUnitService orgUnitService;

    private RoleService roleService;

    private UserService userService;

    private ContextView contextView;

    private ContextListView contextList;

    private ContextEditForm contextForm;

    private OrgUnitAddView orgUnitAddForm;

    private OrgUnitEditView orgUnitEditForm;

    private OrgUnitListView orgUnitList;

    private OrgUnitView orgUnitView;

    private RoleView roleView;

    private AdminTaskView adminTaskView;

    private OrgUnitViewLabFactory orgUnitViewFactory;

    public String escidocLoginUrl;

    public String escidocLogoutUrl;

    private WelcomePage welcomePage;

    private EscidocService containerService;

    private ItemService itemService;

    private String token;

    private String eSciDocUri;

    @Override
    public void init() {
        setMainWindowAndTheme();
        setFullSize();
        addParameterHandler();
    }

    private void setMainWindowAndTheme() {
        setMainWindow(mainWindow);
        setTheme(AppConstants.ESCIDOC_THEME);
    }

    private void setFullSize() {
        mainWindow.setSizeFull();
        appLayout.setSizeFull();
    }

    public void showLandingView() {
        welcomePage = new WelcomePage(this);
        viewManager.setLandingView(welcomePage);
        viewManager.showLandingView();
    }

    public void setEscidocUri(final String eSciDocUri) {
        this.eSciDocUri = eSciDocUri;

        if (eSciDocUri == null) {
            mainWindow.showNotification("eSciDoc URI is unknown",
                Notification.TYPE_ERROR_MESSAGE);
        }
        else {
            escidocLoginUrl = eSciDocUri + "/aa/login?target=";
            escidocLogoutUrl = eSciDocUri + "/aa/logout?target=";

            assert escidocLoginUrl != null && escidocLogoutUrl != null : "LoginUrl and Logout url can not be null.";
        }
    }

    private void addParameterHandler() {
        final ParamaterHandlerImpl handler =
            new ParamaterHandlerImpl(mainWindow, this);
        mainWindow.addParameterHandler(handler);
    }

    public void loadProtectedResources(final String token)
        throws EscidocClientException {
        this.token = token;
        if (eSciDocUri != null && !eSciDocUri.isEmpty()) {

            createServices();
            buildMainLayout();
            createViews();
        }
        else {
            showLandingView();
        }
    }

    private void createServices() throws InternalClientException,
        EscidocException, TransportException {

        if (eSciDocUri != null && !eSciDocUri.isEmpty()) {

            Preconditions.checkArgument(
                eSciDocUri != null && !eSciDocUri.isEmpty(),
                "Escidoc URI can not be empty nor null");

            final ServiceFactory serviceFactory =
                new ServiceFactory(eSciDocUri, token);
            orgUnitService = serviceFactory.createOrgService();
            userService = serviceFactory.createUserService();
            final ContextServiceLab contextServiceLab =
                serviceFactory.createContextServiceLab();
            services.add(contextServiceLab);
            contextService = serviceFactory.createContextService();
            roleService = serviceFactory.createRoleService();
            containerService = serviceFactory.createContainerService();
            services.add(containerService);
            itemService = serviceFactory.createItemService();
            services.add(itemService);
            adminService = serviceFactory.createAdminService();
            services.add(adminService);
            orgUnitServiceLab = serviceFactory.createOrgUnitService();
            services.add(orgUnitServiceLab);
            final AdminService adminService = services.getAdminService();
            Preconditions.checkNotNull(adminService,
                "can not get AdminService from service container");
        }
        else {
            showLandingView();
        }
    }

    private void buildMainLayout() {
        viewManager.setMainView(new MainView(this));
        viewManager.showMainView();
    }

    private void createViews() throws EscidocClientException {
        createUserViewComponent();
        createAdminTaskView();
    }

    private void createUserViewComponent() {
        userViewComp = new UserViewComponent(this, userService);
    }

    private ResourceViewComponent containerViewComponent;

    private AdminService adminService;

    private ResourceService orgUnitServiceLab;

    private FilterView filterResourceView;

    private LoadExample loadExampleView;

    private void createResourceView() throws EscidocClientException {
        containerViewComponent =
            new ResourceViewComponentImpl(mainWindow, orgUnitServiceLab);
    }

    private void createAdminTaskView() {
        adminTaskView = new AdminTaskViewImpl(mainWindow, services);
        reindexView = new ReindexView(services, mainWindow);
        loadExampleView = new LoadExample(services, mainWindow);
        filterResourceView = new FilterView(services, mainWindow);
        repoInfoView = new RepositoryInfoFooView(services, mainWindow);
    }

    private void setMainView(final Component component) {
        viewManager.setSecondComponent(component);
    }

    public OrgUnitListView getOrgUnitTable() {
        if (orgUnitList == null) {
            orgUnitList = new OrgUnitListView(this, orgUnitService);
        }
        return orgUnitList;
    }

    public OrgUnitView getOrgUnitView() {
        if (orgUnitView == null) {
            try {
                orgUnitList = new OrgUnitListView(this, orgUnitService);
                orgUnitEditForm = new OrgUnitEditView(this, orgUnitService);
                orgUnitEditForm.setOrgUnitList(orgUnitList);
                orgUnitView =
                    new OrgUnitView(this, orgUnitList, orgUnitEditForm,
                        orgUnitAddForm);
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
            // FIXME check if this necassary
            catch (final Exception e) {
                ModalDialog.show(mainWindow, e);
                LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
            }
        }
        orgUnitView.showAddView();
        return orgUnitView;
    }

    public ContextView getContextView() throws EscidocClientException {
        if (contextView == null) {
            contextList = new ContextListView(this, contextService);

            final ResourceTreeView rtv = createResourceTreeView();
            contextForm =
                new ContextEditForm(this, mainWindow, contextService,
                    orgUnitService, new AddOrgUnitToTheList(mainWindow, rtv));
            contextForm.setContextList(contextList);
            final ContextAddView contextAddView =
                new ContextAddView(this, mainWindow, contextList,
                    contextService, new AddOrgUnitToTheList(mainWindow, rtv));

            contextView =
                new ContextView(this, contextList, contextForm, contextAddView);
        }
        contextView.showAddView();
        return contextView;
    }

    private ResourceTreeView createResourceTreeView() {
        final FolderHeaderImpl header = new FolderHeaderImpl("");
        ResourceContainer resourceContainer;

        ResourceTreeView resourceTreeView = null;
        try {
            resourceContainer =
                new ResourceViewComponentImpl(mainWindow, orgUnitServiceLab)
                    .createResourceContainer();
            resourceTreeView =
                new ResourceTreeView(mainWindow, header, resourceContainer);

            resourceTreeView.setCommand(new AddChildrenCommandImpl(
                (OrgUnitServiceLab) orgUnitServiceLab, resourceContainer));
            resourceTreeView.addResourceNodeExpandListener();
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        return resourceTreeView;
    }

    private RoleView getRoleView() {
        roleView =
            new RoleView(this, roleService, userService, contextService,
                services);
        return roleView;
    }

    public UserView getUserView() {
        final UserView userView = userViewComp.getUserView();
        userView.showAddView();
        return userView;
    }

    public ContextAddView newContextAddView() {
        final ResourceTreeView rtv = createResourceTreeView();
        final AddOrgUnitToTheList addOrgUnitToTheList =
            new AddOrgUnitToTheList(mainWindow, rtv);
        return new ContextAddView(this, mainWindow,
            contextView.getContextList(), contextService, addOrgUnitToTheList);
    }

    public Component newOrgUnitAddView() throws EscidocException,
        InternalClientException, TransportException {
        return new OrgUnitAddView(this, orgUnitService);
    }

    public UserAddView newUserLabAddView() {
        return new UserAddView(this, userViewComp.getUserView().getUserList(),
            userService, orgUnitService);
    }

    public void showContextView() {
        ContextView contextView;
        try {
            contextView = getContextView();
            setMainView(contextView);
        }
        catch (final EscidocClientException e) {
            LOG.error(Messages.getString("AdminToolApplication.8"), e);
            ModalDialog.show(mainWindow, e);
        }
    }

    public void showOrganizationalUnitView() {
        setMainView(getOrgUnitView());
    }

    public void showRoleView() {
        setMainView(getRoleView());
    }

    public void showRoleView(final UserAccount userAccount) {
        if (roleView == null) {
            roleView = getRoleView();
        }
        roleView.selectUser(userAccount);
        setMainView(roleView);
    }

    public void showUserInEditView(final UserAccount user) {
        userViewComp.getUserView().getUserList().select(user);
        userViewComp.getUserView().showEditView(
            userViewComp.getUserView().getSelectedItem());
        setMainView(userViewComp.getUserView());
    }

    public void showUserView() {
        setMainView(getUserView());
    }

    public void showOrgUnitViewLab() {
        setMainView(getOrgUnitViewLab());
    }

    private Component getOrgUnitViewLab() {
        if (orgUnitViewFactory == null) {
            createOrgUnitFactory();
        }

        assert (orgUnitViewFactory != null) : "orgUnitViewFactory can not be null.";
        try {
            return orgUnitViewFactory.getOrgUnitViewLab();
        }
        catch (final EscidocException e) {
            ModalDialog.show(mainWindow, e);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(mainWindow, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(mainWindow, e);
        }
        return new VerticalLayout();
    }

    private void createOrgUnitFactory() {
        try {
            orgUnitViewFactory =
                new OrgUnitViewLabFactory(orgUnitService,
                    (OrgUnitServiceLab) orgUnitServiceLab, mainWindow);
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
    }

    public void showResourceView() {
        try {
            setMainView(getResourceView());
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
    }

    private Component getResourceView() throws EscidocClientException {
        createResourceView();
        return containerViewComponent.getResourceView();
    }

    public void showAdminTaskView() {
        setMainView(getAdminTaskView());
    }

    private AdminTaskView getAdminTaskView() {
        return adminTaskView;
    }

    private ComponentContainer reindexView;

    private ComponentContainer repoInfoView;

    public void showReindexView() {
        Preconditions.checkNotNull(reindexView, "reindexView is null: %s",
            reindexView);
        viewManager.showView(reindexView);
    }

    public void showFilterResourceView() {
        Preconditions.checkNotNull(filterResourceView,
            "filterResourceView is null: %s", reindexView);
        viewManager.showView(filterResourceView);
    }

    public void showLoadExampleView() {
        Preconditions.checkNotNull(loadExampleView,
            "loadExampleView is null: %s", loadExampleView);
        viewManager.showView(loadExampleView);
    }

    public void showRepoInfoView() {
        Preconditions.checkNotNull(repoInfoView, "repoInfoView is null: %s",
            repoInfoView);
        viewManager.showView(repoInfoView);
    }
}