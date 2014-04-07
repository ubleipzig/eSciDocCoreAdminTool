/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package de.escidoc.admintool.app;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.appfoundation.view.ViewHandler;

import com.google.common.base.Preconditions;
import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.domain.PdpRequestImpl;
import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.service.EscidocService;
import de.escidoc.admintool.service.PdpService;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.service.internal.ContentModelService;
import de.escidoc.admintool.service.internal.ContentRelationService;
import de.escidoc.admintool.service.internal.ContextService;
import de.escidoc.admintool.service.internal.ContextServiceLab;
import de.escidoc.admintool.service.internal.ItemService;
import de.escidoc.admintool.service.internal.OrgUnitService;
import de.escidoc.admintool.service.internal.OrgUnitServiceLab;
import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.admintool.service.internal.RoleService;
import de.escidoc.admintool.service.internal.ServiceContaiterImpl;
//import de.escidoc.admintool.service.internal.ServiceFactory;
import de.uni_leipzig.ubl.admintool.service.internal.UBLServiceFactory;
import de.escidoc.admintool.service.internal.UserService;
import de.escidoc.admintool.view.EscidocServiceLocation;
import de.escidoc.admintool.view.MainView;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.ViewManager;
import de.escidoc.admintool.view.ViewManagerImpl;
import de.escidoc.admintool.view.admintask.ResourceType;
import de.escidoc.admintool.view.admintask.filter.FilterView;
import de.escidoc.admintool.view.admintask.loadexamples.LoadExample;
import de.escidoc.admintool.view.admintask.reindex.ReindexView;
import de.escidoc.admintool.view.admintask.repositoryinfo.RepositoryInfoMainView;
import de.escidoc.admintool.view.contentmodel.ContentModelAddView;
import de.escidoc.admintool.view.contentmodel.ContentModelContainerImpl;
import de.escidoc.admintool.view.contentmodel.ContentModelEditView;
import de.escidoc.admintool.view.contentmodel.ContentModelListView;
import de.escidoc.admintool.view.contentmodel.ContentModelListViewImpl;
import de.escidoc.admintool.view.contentmodel.ContentModelSelectListener;
import de.escidoc.admintool.view.contentmodel.ContentModelView;
import de.escidoc.admintool.view.contentmodel.ContentModelViewImpl;
import de.escidoc.admintool.view.contentmodel.DeleteContentModelListener;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextView;
import de.escidoc.admintool.view.factory.ContextViewFactory;
import de.escidoc.admintool.view.resource.AddChildrenCommandImpl;
import de.escidoc.admintool.view.resource.FolderHeaderImpl;
import de.escidoc.admintool.view.resource.OrgUnitTreeView;
import de.escidoc.admintool.view.resource.ResourceContainer;
import de.escidoc.admintool.view.resource.ResourceContainerFactory;
import de.escidoc.admintool.view.resource.ResourceViewComponent;
import de.escidoc.admintool.view.resource.ResourceViewComponentImpl;
import de.escidoc.admintool.view.role.RoleView;
import de.escidoc.admintool.view.start.LandingView;
import de.escidoc.admintool.view.user.SetOrgUnitsCommandImpl;
import de.escidoc.admintool.view.user.UserAddView;
import de.escidoc.admintool.view.user.UserView;
import de.escidoc.admintool.view.user.UserViewComponent;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.aa.useraccount.UserAccountProperties;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;
import de.uni_leipzig.ubl.admintool.view.group.GroupAddView;
import de.uni_leipzig.ubl.admintool.view.group.GroupEditForm;
import de.uni_leipzig.ubl.admintool.view.group.GroupEditView;
import de.uni_leipzig.ubl.admintool.view.group.GroupSummaryView;
import de.uni_leipzig.ubl.admintool.view.group.GroupView;
import de.uni_leipzig.ubl.admintool.view.group.GroupViewComponent;

@SuppressWarnings("serial")
public class AdminToolApplication extends Application {
    // TODO refactor this class, reasons: big class, too many fields and methods

    private static final Logger LOG = LoggerFactory.getLogger(AdminToolApplication.class);

    private final Window mainWindow = new Window(ViewConstants.MAIN_WINDOW_TITLE);

    private final ViewManager viewManager = new ViewManagerImpl(mainWindow);

    private final ServiceContainer services = new ServiceContaiterImpl();

    private final VerticalLayout appLayout = new VerticalLayout();

    private UserViewComponent userViewComp;
    
    private GroupViewComponent groupViewComp;

    private ContextService contextService;

    private OrgUnitService orgUnitService;

    private RoleService roleService;

    private UserService userService;
    
    private GroupService groupService;

    private RoleView roleView;

    public String escidocLoginUrl;

    public String escidocLogoutUrl;

    private LandingView landingView;

    private EscidocService containerService;

    private ItemService itemService;

    private String token;

    private ResourceContainerFactory resourceContainerFactory;

    private ContextViewFactory contextViewFactory;

    private UserAccount currentUser;

    private PdpService pdpService;

    private ResourceViewComponent containerViewComponent;

    private OrgUnitServiceLab orgUnitServiceLab;

    private ResourceContainer resourceContainer;

    private FilterView filterResourceView;

    private LoadExample loadExampleView;

    private ReindexView reindexView;

    private RepositoryInfoMainView repoInfoView;

    private AdminService adminService;

    private ContextView contextView;

    private ContentModelAddView addView;

    private ContentModelView contentModelView;

    private ResourceService contentModelService;

    private PdpRequest pdpRequest;

    private EscidocServiceLocation escidocServiceLocation;

    private MainView mainView;

    private ContentRelationService contentRelationService;

    @Override
    public void init() {
        showProxyInfoInLog();
        registerViewHandler();
        setMainWindowAndTheme();
        setFullSize();
        addParameterHandler();
    }

    private void showProxyInfoInLog() {
        LOG.info("http.proxyHost: " + System.getProperty("http.proxyHost"));
        LOG.info("http.proxyPort: " + System.getProperty("http.proxyPort"));
        LOG.info("http.nonProxyHosts: " + System.getProperty("http.nonProxyHosts"));
    }

    private void registerViewHandler() {
        ViewHandler.initialize(this);
    }

    private void setMainWindowAndTheme() {
        setMainWindow(mainWindow);
        setTheme(AppConstants.ESCIDOC_THEME);
    }

    private void setFullSize() {
        mainWindow.setSizeFull();
        appLayout.setSizeFull();
    }

    protected void showLandingView() {
        landingView = new LandingView(this);
        landingView.init();
        viewManager.setLandingView(landingView);
        viewManager.showLandingView();
    }

    public void setEscidocUri(final String eSciDocUri) {
        escidocServiceLocation = new EscidocServiceLocation(eSciDocUri, getURL());

        if (eSciDocUri == null) {
            mainWindow.showNotification("eSciDoc URI is unknown", Notification.TYPE_ERROR_MESSAGE);
        }
        else {
            escidocLoginUrl = escidocServiceLocation.getLoginUri();
            escidocLogoutUrl = escidocServiceLocation.getLogoutUri();
        }
    }

    private void addParameterHandler() {
        mainWindow.addParameterHandler(new EscidocParamaterHandlerImpl(this));
    }

    public void loadProtectedResources(final String token) {
        this.token = token;
        if (isEscidocUriKnown()) {
            tryInitApp();
        }
        else {
            showLandingView();
        }
    }

    private void tryInitApp() {
        try {
            initApplication();
        }
        catch (final IllegalArgumentException e) {
            LOG.error(Messages.getString("AdminToolApplication.3"), e);
            mainWindow.showNotification(new Notification(ViewConstants.WRONG_TOKEN_MESSAGE, "Wrong token",
                Notification.TYPE_ERROR_MESSAGE));
            showLandingView();
        }
        catch (final AuthenticationException e) {
            LOG.debug(ViewConstants.WRONG_TOKEN_MESSAGE, e);
            mainWindow.showNotification(new Notification(ViewConstants.WRONG_TOKEN_MESSAGE, e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
            showLandingView();
        }
        catch (final EscidocClientException e) {
            handleException(e);
        }
        catch (final MalformedURLException e) {
            handleException(e);
        }
    }

    private void handleException(final Exception e) {
        LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        ModalDialog.show(mainWindow, e);
    }

    private void initApplication() throws InternalClientException, EscidocException, TransportException,
        EscidocClientException, MalformedURLException {
        createServices();
        setCurrentUser();
        createPdpRequest();
        createFactories();
        buildMainLayout();
    }

    private boolean isEscidocUriKnown() {
        return !(escidocServiceLocation == null || escidocServiceLocation.getUri().isEmpty());
    }

    private void createPdpRequest() {
        pdpRequest = new PdpRequestImpl(pdpService, currentUser);
    }

    private void setCurrentUser() throws EscidocClientException {
        if (isTokenExists()) {
            userIsLoggedIn();
        }
        else {
            currentUserIsAnon();
        }
    }

    private void userIsLoggedIn() throws EscidocClientException {
        currentUser = userService.getCurrentUser();
    }

    private void currentUserIsAnon() {
        currentUser = new UserAccount();
        createAnonUser();
    }

    private void createAnonUser() {
        final UserAccountProperties properties = new UserAccountProperties();
        properties.setLoginName(ViewConstants.GUEST);
        currentUser.setProperties(properties);
    }

    private boolean isTokenExists() {
        return !(token == null || token.isEmpty());
    }

    private void createFactories() {
        resourceContainerFactory = new ResourceContainerFactory(orgUnitServiceLab);
        contextViewFactory = new ContextViewFactory(this, mainWindow, orgUnitService, contextService, pdpRequest);
    }

    private void createServices() throws InternalClientException, EscidocException, TransportException,
        MalformedURLException {
        LOG.info("Using escidoc instance  in: " + escidocServiceLocation.getUri());
        final UBLServiceFactory serviceFactory = new UBLServiceFactory(escidocServiceLocation, token);
        orgUnitService = serviceFactory.createOrgService();
        userService = serviceFactory.createUserService();
        final ContextServiceLab contextServiceLab = serviceFactory.createContextServiceLab();
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
        contentModelService = serviceFactory.createContentModelService();
        services.add(contentModelService);
        pdpService = serviceFactory.createPdpService();
        contentRelationService = serviceFactory.createContentRelationService();
        services.add(contentRelationService);
        groupService = serviceFactory.createGroupService();
    }

    private void buildMainLayout() {
        mainView = new MainView(this, pdpRequest, currentUser, escidocServiceLocation);
        mainView.init();

        viewManager.setMainView(mainView);
        viewManager.showMainView();
    }

    private void createRepoInfoView() {
        repoInfoView = new RepositoryInfoMainView(services, mainWindow);
        repoInfoView.addView();
    }

    private void createFilterView() {
        filterResourceView = new FilterView(services, mainWindow, pdpRequest);
        filterResourceView.addView();
    }

    private void createLoadExampleView() {
        loadExampleView = new LoadExample(services, mainWindow);
        loadExampleView.addView();
    }

    private void createReindexView() {
        reindexView = new ReindexView(services, mainWindow);
        reindexView.addView();
    }

    public void showReindexView() {
        createReindexView();
        Preconditions.checkNotNull(reindexView, "reindexView is null: %s", reindexView);
        viewManager.showView(reindexView);
    }

    public void showFilterResourceView() {
        createFilterView();
        Preconditions.checkNotNull(filterResourceView, "filterResourceView is null: %s", reindexView);
        viewManager.showView(filterResourceView);
    }

    public void showLoadExampleView() {
        createLoadExampleView();
        Preconditions.checkNotNull(loadExampleView, "loadExampleView is null: %s", loadExampleView);

        viewManager.showView(loadExampleView);
    }

    public void showRepoInfoView() {
        createRepoInfoView();
        Preconditions.checkNotNull(repoInfoView, "repoInfoView is null: %s", repoInfoView);
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

    private OrgUnitTreeView createResourceTreeView() {
        final FolderHeaderImpl header = new FolderHeaderImpl("");

        OrgUnitTreeView orgUnitTreeView = null;
        try {
            final ResourceContainer resourceContainer = getResourceContainer();

            final ResourceViewComponent resourceViewComponent =
                new ResourceViewComponentImpl(this, mainWindow, orgUnitServiceLab, resourceContainer, pdpRequest);
            resourceViewComponent.init();
            orgUnitTreeView = new OrgUnitTreeView(mainWindow, header, resourceContainer);

            orgUnitTreeView.setCommand(new AddChildrenCommandImpl(orgUnitServiceLab, resourceContainer));
            orgUnitTreeView.addResourceNodeExpandListener();
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        return orgUnitTreeView;
    }

    private RoleView getRoleView() {
        roleView = new RoleView(this, roleService, userService, contextService, services);
        roleView.init();
        return roleView;
    }

    public ContextAddView newContextAddView() {
        return contextViewFactory.createContextAddView(createResourceTreeView());
    }

    public UserAddView newUserAddView() {
        final UserAddView userAddView =
            new UserAddView(this, userViewComp.getUserView().getUserList(), userService, createResourceTreeView(),
                new SetOrgUnitsCommandImpl(userService));
        userAddView.init();
        return userAddView;
    }

    public GroupAddView newGroupAddView() {
		final GroupAddView groupAddView =
				new GroupAddView(this, groupService, pdpRequest, groupViewComp.getGroupView().getGroupList());
		groupAddView.init();
		return groupAddView;
	}

	public void showContextView() {
        contextView = contextViewFactory.createContextView(createResourceTreeView());
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

    public UserView getUserView() {
        return userViewComp.getUserView();
    }
    
    public GroupView getGroupView() {
    	return groupViewComp.getGroupView();
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
            new ResourceViewComponentImpl(this, mainWindow, orgUnitServiceLab, getResourceContainer(), pdpRequest);
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
        try {
            createContentModelView();
            setMainView(getContentModelView());
        }
        catch (final EscidocClientException e) {
            handleException(e);
        }
    }

    private void createContentModelView() throws EscidocClientException {

        final ContentModelContainerImpl container = new ContentModelContainerImpl(contentModelService);

        final ContentModelListView listView =
            new ContentModelListViewImpl(container, new ContentModelSelectListener(contentModelService, mainWindow));

        listView.init();

        addView = new ContentModelAddView(this, mainWindow, contentModelService, container, pdpRequest);
        addView.init();

        final DeleteContentModelListener deleteListener =
            new DeleteContentModelListener((ContentModelService) contentModelService, container, this);

        final ContentModelEditView editView =
            new ContentModelEditView(contentModelService, mainWindow, pdpRequest, deleteListener);
        editView.init();

        contentModelView = new ContentModelViewImpl(listView, addView, editView, pdpRequest);
        contentModelView.init();
        contentModelView.setSizeFull();

        editView.setContentModelView(contentModelView);
        addView.setContentModelView(contentModelView);
        listView.setContentModelView(contentModelView);
    }

    private ContentModelView getContentModelView() {
        contentModelView.selectFirstItem();
        return contentModelView;
    }

    public void showUserView() {
        createUserViewComponent();
        userViewComp.showFirstItemInEditView();
        final UserView userView = userViewComp.getUserView();
        setMainView(userView);
    }

    public void showUser(final UserAccount user) {
        if (userViewComp == null) {
            showUserView();
        }
        selectUserInNavigationTree();
        selectInListView(user);
        showUserInEditView();
        setMainView(userViewComp.getUserView());
    }

    private void createUserViewComponent() {
        userViewComp =
            new UserViewComponent(this, userService, orgUnitServiceLab, createResourceTreeView(), pdpRequest);
        userViewComp.init();
    }

    private void showUserInEditView() {
        userViewComp.getUserView().showEditView(userViewComp.getUserView().getSelectedItem());
    }

    private void selectUserInNavigationTree() {
        mainView.getNavigationTree().selectUserView();
    }

    private void selectInListView(final UserAccount user) {
        userViewComp.getUserView().getUserList().select(user);
    }

    public void showUser(final String userId) {
        UserAccount user;
        try {
            user = userService.getUserById(userId);
            if (userViewComp == null) {
                userViewComp =
                    new UserViewComponent(this, userService, orgUnitServiceLab, createResourceTreeView(), pdpRequest);
                userViewComp.init();
                userViewComp.getUserView().getUserList().select(user);
                userViewComp.showUserInEditView(user);
                final UserView userView = userViewComp.getUserView();
                setMainView(userView);
            }
            else {
                userViewComp.showUserInEditView(user);
                final UserView userView = userViewComp.getUserView();
                setMainView(userView);
            }
            selectUserInNavigationTree();
            selectInListView(user);
            showUserInEditView();
        }
        catch (final EscidocClientException e) {
            handleException(e);
        }

    }

    public PdpRequest getPdpRequest() {
        return pdpRequest;
    }

    public UserService getUserService() {
        return userService;
    }

    public void showGroupView() {
        createGroupViewComponent();
        groupViewComp.showFirstItemInEditView();
        final GroupView groupView = groupViewComp.getGroupView();
        setMainView(groupView);
    }

    private void createGroupViewComponent() {
        groupViewComp = new GroupViewComponent(this, groupService, pdpRequest);
        groupViewComp.init();
    }
    
    public void showGroup(final UserGroup createdUserGroup) {
    	UserGroup group;
    	try {
			group = groupService.getGroupById(createdUserGroup.getObjid());
			if (groupViewComp == null) {
				createGroupViewComponent();
				groupViewComp.getGroupView().getGroupList().select(group);
			}
			groupViewComp.showGroupInEditView(group);
			final GroupView groupView = groupViewComp.getGroupView();
			setMainView(groupView);
			selectInGroupListView(group);
            showGroupInEditView(group);
		} catch (final EscidocClientException e) {
			handleException(e);
		}
    }

	private void selectInGroupListView(UserGroup group) {
		groupViewComp.getGroupView().getGroupList().select(group);
	}

	private void showGroupInEditView(UserGroup group) {
		groupViewComp.showGroupInEditView(group);
	}

	public void showGroupRoleView() {
		final de.uni_leipzig.ubl.admintool.view.role.RoleView roleGroupView = new de.uni_leipzig.ubl.admintool.view.role.RoleView(
				this, roleService, userService, groupService, contextService, services, ResourceType.USERGROUP);
		try {
			roleGroupView.selectGroup(groupService.getGroupById(this.getGroupView().getSelectedItem().getItemProperty(PropertyId.OBJECT_ID).getValue().toString()));
			roleGroupView.init();
			roleGroupView.show();
		} catch (EscidocClientException e) {
			handleException(e);
		}
	}

	public void showGroupSummaryView(UserGroup userGroup) {
		final GroupSummaryView gsv = new GroupSummaryView(this, groupService, userService, orgUnitServiceLab, userGroup);
		gsv.show();
	}
}