package de.escidoc.admintool.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.NavigationTree;
import de.escidoc.admintool.view.StartPage;
import de.escidoc.admintool.view.ToolbarFactory;
import de.escidoc.admintool.view.ViewConstants;
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
import de.escidoc.admintool.view.user.UserEditForm;
import de.escidoc.admintool.view.user.UserEditView;
import de.escidoc.admintool.view.user.UserListView;
import de.escidoc.admintool.view.user.UserView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class AdminToolApplication extends Application {
    // TODO / FIXME
    // too many method
    // too many fields
    // =>God Class=>refactor logical code to an extra class.
    private static final Logger log = LoggerFactory
        .getLogger(AdminToolApplication.class);

    private final Window mainWindow = new Window(
        ViewConstants.MAIN_WINDOW_TITLE);

    private final VerticalLayout appLayout = new VerticalLayout();

    private final SplitPanel horizontalSplit = new SplitPanel(
        SplitPanel.ORIENTATION_HORIZONTAL);

    private final Button logoutButton = new Button(
        Messages.getString("AdminToolApplication.6")); //$NON-NLS-1$

    private final StartPage startPage = new StartPage();

    private final NavigationTree navigation = new NavigationTree(this);

    private final ToolbarFactory factory = new ToolbarFactory();

    private GridLayout toolbar;

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

    private UserEditForm userEditForm;

    private UserListView userListView;

    private UserView userView;

    private OrgUnitViewLabFactory orgUnitViewFactory;

    public static final String ESCIDOC_URI = "http://localhost:8080";

    private final static String eSciDocUri = AdminToolApplication.ESCIDOC_URI;

    public static final String ESCIDOC_LOGIN_URL = eSciDocUri
        + "/aa/login?target=";

    public static final String LOGOUT_URL = eSciDocUri + "/aa/logout?target=";

    @Override
    public void init() {
        initMainWindow();
        addParameterHandler();
    }

    private void initMainWindow() {
        setTheme(AppConstants.ESCIDOC_THEME);
        setMainWindow(mainWindow);
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
        setMainView(startPage);
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
        mainWindow.setContent(appLayout);

        setFullSize();
        addLogoutButton();

        addToolbar();
        addNavigationTree();
    }

    private void setFullSize() {
        mainWindow.setSizeFull();
        appLayout.setSizeFull();
    }

    private void setMainView(final Component component) {
        horizontalSplit.setSecondComponent(component);
    }

    private void addLogoutButton() {
        logoutButton.setEnabled(true);
        logoutButton.setStyleName(Reindeer.BUTTON_SMALL);
        setLogoutURL(AdminToolApplication.LOGOUT_URL + getURL());
        logoutButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(final ClickEvent event) {
                close();
            }
        });
    }

    private void addToolbar() {
        if (appLayout.getComponentIndex(toolbar) < 0) {
            toolbar = factory.createToolbar(new Button[] { logoutButton });
            appLayout.addComponent(toolbar);
        }
    }

    private void addNavigationTree() {
        appLayout.addComponent(horizontalSplit);
        appLayout.setExpandRatio(horizontalSplit, 1);
        horizontalSplit.setSplitPosition(
            ViewConstants.SPLIT_POSITION_FROM_LEFT, SplitPanel.UNITS_PIXELS);
        horizontalSplit.addStyleName("small blue white");
        horizontalSplit.setFirstComponent(navigation);
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
        if (userView == null) {
            userListView = new UserListView(this, userService);
            userEditForm = new UserEditForm(this, userService);
            final UserEditView userEditView = new UserEditView(userEditForm);
            userView = new UserView(this, userListView, userEditView);
        }
        userView.showAddView();
        return userView;
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
        return new UserAddView(this, userView.getUserList(), userService,
            orgUnitService);
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
        userView.getUserList().select(user);
        userView.showEditView(userView.getSelectedItem());
        setMainView(userView);
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