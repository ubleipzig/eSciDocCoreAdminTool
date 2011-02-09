package de.escidoc.admintool.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.appfoundation.view.ViewHandler;

import com.google.common.base.Preconditions;
import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

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
import de.escidoc.admintool.view.admintask.AddToResourceContainer;
import de.escidoc.admintool.view.admintask.FilterView;
import de.escidoc.admintool.view.admintask.LoadExample;
import de.escidoc.admintool.view.admintask.ReindexView;
import de.escidoc.admintool.view.admintask.RepositoryInfoFooView;
import de.escidoc.admintool.view.contentmodel.ContentModelAddView;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextView;
import de.escidoc.admintool.view.factory.ContextViewFactory;
import de.escidoc.admintool.view.login.WelcomePage;
import de.escidoc.admintool.view.resource.AddChildrenCommandImpl;
import de.escidoc.admintool.view.resource.FolderHeaderImpl;
import de.escidoc.admintool.view.resource.ResourceContainer;
import de.escidoc.admintool.view.resource.ResourceContainerFactory;
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

    private RoleView roleView;

    public String escidocLoginUrl;

    public String escidocLogoutUrl;

    private WelcomePage welcomePage;

    private EscidocService containerService;

    private ItemService itemService;

    private String token;

    private String eSciDocUri;

    private ResourceContainerFactory resourceContainerFactory;

    private ContextViewFactory contextViewFactory;

    @Override
    public void init() {
        configureLogger();
        registerViewHandler();
        setMainWindowAndTheme();
        setFullSize();
        addParameterHandler();
    }

    private void registerViewHandler() {
        ViewHandler.initialize(this);
    }

    private void configureLogger() {
        try {
            ConfigureLogger.execute();
        }
        catch (final IOException e) {
            LOG.warn("Can not configure logger.");
        }
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
            createFactories();
            buildMainLayout();
        }
        else {
            showLandingView();
        }
    }

    private void createFactories() {
        resourceContainerFactory =
            new ResourceContainerFactory(orgUnitServiceLab);
        contextViewFactory =
            new ContextViewFactory(this, mainWindow, orgUnitService,
                contextService);
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
            contentModelService = serviceFactory.createContentModelService();
        }
        else {
            showLandingView();
        }
    }

    private void buildMainLayout() {
        viewManager.setMainView(new MainView(this));
        viewManager.showMainView();
    }

    private ResourceViewComponent containerViewComponent;

    private ResourceService orgUnitServiceLab;

    private ResourceContainer resourceContainer;

    private FilterView filterResourceView;

    private LoadExample loadExampleView;

    private ReindexView reindexView;

    private RepositoryInfoFooView repoInfoView;

    private AdminService adminService;

    private ContextView contextView;

    private ContentModelAddView contentModelAddView;

    private ResourceService contentModelService;

    private void createRepoInfoView() {
        repoInfoView = new RepositoryInfoFooView(services, mainWindow);
        repoInfoView.addView();
    }

    private void createFilterView() {
        filterResourceView = new FilterView(services, mainWindow);
        filterResourceView.addView();
    }

    private void createLoadExampleView() {
        loadExampleView = new LoadExample(services, mainWindow);
        loadExampleView.setCommand(new AddToResourceContainer(mainWindow,
            services, getResourceContainer()));
        loadExampleView.addView();
    }

    private void createReindexView() {
        reindexView = new ReindexView(services, mainWindow);
        reindexView.addView();
    }

    public void showReindexView() {
        createReindexView();
        Preconditions.checkNotNull(reindexView, "reindexView is null: %s",
            reindexView);
        viewManager.showView(reindexView);
    }

    public void showFilterResourceView() {
        createFilterView();
        Preconditions.checkNotNull(filterResourceView,
            "filterResourceView is null: %s", reindexView);
        viewManager.showView(filterResourceView);
    }

    public void showLoadExampleView() {
        createLoadExampleView();
        Preconditions.checkNotNull(loadExampleView,
            "loadExampleView is null: %s", loadExampleView);

        viewManager.showView(loadExampleView);
    }

    public void showRepoInfoView() {
        createRepoInfoView();
        Preconditions.checkNotNull(repoInfoView, "repoInfoView is null: %s",
            repoInfoView);
        viewManager.showView(repoInfoView);
    }

    private ResourceContainer getResourceContainer() {
        if (resourceContainer == null) {
            try {
                return resourceContainerFactory.getResourceContainer();
            }
            catch (final EscidocClientException e) {
                ModalDialog.show(mainWindow, e);
                LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
            }
        }
        return resourceContainer;
    }

    private void setMainView(final Component component) {
        viewManager.setSecondComponent(component);
    }

    private ResourceTreeView createResourceTreeView() {
        final FolderHeaderImpl header = new FolderHeaderImpl("");

        ResourceTreeView resourceTreeView = null;
        try {
            final ResourceContainer resourceContainer = getResourceContainer();

            final ResourceViewComponent resourceViewComponent =
                new ResourceViewComponentImpl(this, mainWindow,
                    orgUnitServiceLab, resourceContainer);
            resourceViewComponent.init();
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

    public ContextAddView newContextAddView() {
        return contextViewFactory
            .createContextAddView(createResourceTreeView());
    }

    public UserAddView newUserAddView() {
        return new UserAddView(this, userViewComp.getUserView().getUserList(),
            userService, createResourceTreeView());
    }

    public void showContextView() {
        contextView =
            contextViewFactory.createContextView(createResourceTreeView());
        contextView.showFirstItemInEditView();
        setMainView(contextView);
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
        createUserViewComponent();
        userViewComp.showFirstItemInEditView();
        final UserView userView = userViewComp.getUserView();
        setMainView(userView);
    }

    public UserView getUserView() {
        return userViewComp.getUserView();
    }

    private void createUserViewComponent() {
        userViewComp =
            new UserViewComponent(this, userService, orgUnitServiceLab,
                createResourceTreeView());
        userViewComp.init();
    }

    public void showResourceView() {
        try {
            createResourceViewComponent();
            containerViewComponent.showFirstItemInEditView();
            setMainView(getResourceView());
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
    }

    private void createResourceViewComponent() throws EscidocClientException {
        containerViewComponent =
            new ResourceViewComponentImpl(this, mainWindow, orgUnitServiceLab,
                getResourceContainer());
        containerViewComponent.init();
    }

    private Component getResourceView() throws EscidocClientException {
        return containerViewComponent.getResourceView();
    }

    public ContextView getContextView() {
        return contextView;
    }

    // Content Model View
    public void showContentModelView() {
        createContentModelView();
        setMainView(getContentModelView());
    }

    private void createContentModelView() {
        final ContentModelContainer contentModelContainer =
            new ContentModelContainer();
        contentModelAddView =
            new ContentModelAddView(mainWindow, contentModelService,
                contentModelContainer);
        contentModelAddView.init();
    }

    private Component getContentModelView() {
        return contentModelAddView;
    }
}