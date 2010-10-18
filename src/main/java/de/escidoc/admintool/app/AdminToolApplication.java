package de.escidoc.admintool.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.MainView;
import de.escidoc.admintool.view.StartPage;
import de.escidoc.admintool.view.UserViewComponent;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.ViewManager;
import de.escidoc.admintool.view.ViewManagerImpl;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextEditForm;
import de.escidoc.admintool.view.context.ContextListView;
import de.escidoc.admintool.view.context.ContextView;
import de.escidoc.admintool.view.lab.orgunit.OrgUnitViewLabFactory;
import de.escidoc.admintool.view.orgunit.OrgUnitAddView;
import de.escidoc.admintool.view.orgunit.OrgUnitEditView;
import de.escidoc.admintool.view.orgunit.OrgUnitListView;
import de.escidoc.admintool.view.orgunit.OrgUnitView;
import de.escidoc.admintool.view.role.RoleView;
import de.escidoc.admintool.view.user.UserAddView;
import de.escidoc.admintool.view.user.UserView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class AdminToolApplication extends Application {
    private static final Logger log = LoggerFactory
        .getLogger(AdminToolApplication.class);

    private final Window mainWindow = new Window(
        ViewConstants.MAIN_WINDOW_TITLE);

    private final ViewManager viewManager = new ViewManagerImpl(mainWindow);

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

    private OrgUnitViewLabFactory orgUnitViewFactory;

    private static String eSciDocUri;

    public static String escidocLoginUrl;

    public String escidocLogoutUrl;

    private WelcomePage welcomePage;

    private final StartPage startPage = new StartPage();

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
        mainWindow.addParameterHandler(new ParamaterHandlerImpl(mainWindow,
            this));
    }

    public void authenticate(final String token)
        throws InternalClientException, EscidocException, TransportException {
        loadProtectedResources(token);
    }

    private void loadProtectedResources(final String token)
        throws InternalClientException, EscidocException, TransportException {
        initServices(token);
        initFactories();
        buildMainLayout();
        createUserViewComponent();
    }

    private void initServices(final String token)
        throws InternalClientException, EscidocException, TransportException {
        final ServiceFactory serviceFactory =
            new ServiceFactory(eSciDocUri, token);
        orgUnitService = serviceFactory.createOrgService();
        userService = serviceFactory.createUserService();
        contextService = serviceFactory.createContextService();
        roleService = serviceFactory.createRoleService();
    }

    private void initFactories() throws EscidocException,
        InternalClientException, TransportException {
        orgUnitViewFactory =
            new OrgUnitViewLabFactory(orgUnitService, mainWindow);
    }

    private void buildMainLayout() {
        viewManager.setMainView(new MainView(this));
        viewManager.showMainView();
    }

    private void createUserViewComponent() {
        userViewComp = new UserViewComponent(this, userService);
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
                ErrorMessage.show(mainWindow, e);
                log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
            }
            catch (final InternalClientException e) {
                ErrorMessage.show(mainWindow, e);
                log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
            }
            catch (final TransportException e) {
                ErrorMessage.show(mainWindow, e);
                log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
            }
            // FIXME check if this necassary
            catch (final Exception e) {
                ErrorMessage.show(mainWindow, e);
                log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
            }
        }
        orgUnitView.showAddView();
        return orgUnitView;
    }

    public ContextView getContextView() throws EscidocException,
        InternalClientException, TransportException {
        if (contextView == null) {
            contextList = new ContextListView(this, contextService);
            contextForm =
                new ContextEditForm(this, contextService, orgUnitService);
            contextForm.setContextList(contextList);
            final ContextAddView contextAddView =
                new ContextAddView(this, contextList, contextService,
                    orgUnitService);
            contextView =
                new ContextView(this, contextList, contextForm, contextAddView);
        }
        contextView.showAddView();
        return contextView;
    }

    private RoleView getRoleView() {
        if (roleView == null) {
            roleView =
                new RoleView(this, roleService, userService, contextService);
        }
        return roleView;
    }

    public UserView getUserView() {
        return userViewComp.getUserView();
    }

    public ContextAddView newContextAddView() {
        return new ContextAddView(this, contextView.getContextList(),
            contextService, orgUnitService);
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
        catch (final EscidocException e) {
            log.error(Messages.getString("AdminToolApplication.8"), e);
        }
        catch (final InternalClientException e) {
            log.error(Messages.getString("AdminToolApplication.8"), e);
        }
        catch (final TransportException e) {
            log.error(Messages.getString("AdminToolApplication.9"), e);
        }
    }

    public void showOrganizationalUnitView() {
        setMainView(getOrgUnitView());
    }

    public void showRoleView() {
        setMainView(getRoleView());
    }

    public void showRoleView(final UserAccount userAccount) {
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
        assert (orgUnitViewFactory != null) : "orgUnitViewFactory can not be null.";
        try {
            return orgUnitViewFactory.getOrgUnitViewLab();
        }
        catch (final EscidocException e) {
            ErrorMessage.show(mainWindow, e);
        }
        catch (final InternalClientException e) {
            ErrorMessage.show(mainWindow, e);
        }
        catch (final TransportException e) {
            ErrorMessage.show(mainWindow, e);
        }
        return new VerticalLayout();
    }
}