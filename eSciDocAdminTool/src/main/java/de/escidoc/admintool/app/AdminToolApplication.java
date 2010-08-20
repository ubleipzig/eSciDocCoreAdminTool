package de.escidoc.admintool.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.LoginWindow;
import de.escidoc.admintool.view.NavigationTree;
import de.escidoc.admintool.view.Toolbar;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextEditForm;
import de.escidoc.admintool.view.context.ContextListView;
import de.escidoc.admintool.view.context.ContextView;
import de.escidoc.admintool.view.orgunit.OrgUnitAddView;
import de.escidoc.admintool.view.orgunit.OrgUnitEditView;
import de.escidoc.admintool.view.orgunit.OrgUnitList;
import de.escidoc.admintool.view.orgunit.OrgUnitView;
import de.escidoc.admintool.view.user.UserView;
import de.escidoc.admintool.view.user.lab.UserLabAddView;
import de.escidoc.admintool.view.user.lab.UserLabEditForm;
import de.escidoc.admintool.view.user.lab.UserLabEditView;
import de.escidoc.admintool.view.user.lab.UserLabListView;
import de.escidoc.admintool.view.user.lab.UserLabView;
import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.test.client.EscidocClientTestBase;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class AdminToolApplication extends Application
    implements ApplicationContext.TransactionListener, ClickListener,
    ValueChangeListener, ItemClickListener {
    private static final long serialVersionUID = -8811871788712427578L;

    private static final String LOGIN_URL =
        "http://localhost:8080/aa/login?target=http://localhost:8181/AdminTool";

    private static final String ESCIDOC_USER_HANDLE_URL =
        "http://localhost:8181/AdminTool?eSciDocUserHandle=";

    private final Logger log =
        LoggerFactory.getLogger(AdminToolApplication.class);

    @Override
    public void init() {
        log.info("Starting application..."); //$NON-NLS-1$
        getContext().addTransactionListener(this);
        final String path = Messages.getString("AdminToolApplication.1");
        logoutButton.setIcon(new ThemeResource(path)); //$NON-NLS-1$
        logoutButton.addListener(new Button.ClickListener() {
            public void buttonClick(final Button.ClickEvent event) {
                AdminToolApplication.this.getMainWindow().showNotification(
                    Messages.getString("AdminToolApplication.2")); //$NON-NLS-1$
                AdminToolApplication.this.close();
            }
        });
        buildMainLayout();
        showLoginWindow();
    }

    // TODO: FIXME Login does not work!!!!
    private void showLoginWindow() {
        setMainWindow(new LoginWindow());
        /*
         * final Window window = new Window("Login");// final TextField
         * loginNameTextField; final TextField passwordTextField;
         * window.setModal(true);
         * 
         * window.setHeight("230px"); //$NON-NLS-1$ window.setWidth("400px");
         * //$NON-NLS-1$
         * 
         * FormLayout layout = new FormLayout(); loginNameTextField = new
         * TextField(); layout.addComponent(LayoutHelper.create("Login Name:",
         * loginNameTextField, "100px", true)); passwordTextField = new
         * TextField(); passwordTextField.setSecret(true);
         * layout.addComponent(LayoutHelper.create("Password:",
         * passwordTextField, "100px", true)); final Button okButton = new
         * Button("Login"); okButton.addListener(new Button.ClickListener() {
         * public void buttonClick(ClickEvent event) { try{
         * currentApplication.set(AdminToolApplication.this); String login =
         * (String) loginNameTextField.getValue(); String pwd = (String)
         * passwordTextField.getValue(); //
         * AdminToolApplication.getInstance().authenticate(login, pwd);
         * authenticate(login, pwd); getMainWindow().removeWindow(window); }
         * catch(Exception e){ e.printStackTrace(); } } });
         * layout.addComponent(LayoutHelper.create(okButton));
         * 
         * window.setContent(layout);
         * 
         * 
         * //setMainWindow(new LoginWindow());
         * getMainWindow().addWindow(window); window.addListener(new
         * Window.CloseListener() { public void windowClose(CloseEvent e) {
         * Resource r = new
         * ExternalResource(AdminToolApplication.getInstance().getURL());
         * getMainWindow().open(r); } });
         * currentApplication.set(AdminToolApplication.this); String login =
         * "sysadmin"; String pwd = "escidoc"; try{ authenticate(login, pwd); }
         * catch(Exception e){ e.printStackTrace(); }
         */

    }

    // === Authentification related methods ===
    // TODO create an extra class that related to authentification /login
    private static ThreadLocal<AdminToolApplication> currentApplication =
        new ThreadLocal<AdminToolApplication>();

    public void transactionStart(
        final Application application, final Object transactionData) {
        if (application == AdminToolApplication.this) {
            currentApplication.set(this);
        }
    }

    public void transactionEnd(
        final Application application, final Object transactionData) {
        if (application == AdminToolApplication.this) {
            currentApplication.set(null);
            currentApplication.remove();
        }
    }

    public static AdminToolApplication getInstance() {
        return currentApplication.get();
    }

    private String currentUser = null;

    private Object authentification;

    public void authenticate(final String login, final String password)
        throws AuthenticationException, InternalClientException,
        TransportException, IOException {

        final Authentication authentication = new Authentication();
        authentication.login(EscidocClientTestBase.DEFAULT_SERVICE_URL, login,
            password);

        currentUser = login;
        // TODO show error to the user.
        try {
            loadProtectedResources(authentication);
        }
        catch (final EscidocException e) {
            log.error(Messages.getString("AdminToolApplication.3"), e); //$NON-NLS-1$
            e.printStackTrace();
        }
    }

    public String getCurrentUser() {
        return currentUser;
    }

    private void loadProtectedResources(final Authentication authentication)
        throws EscidocException, InternalClientException, TransportException {
        buildMainLayout();
        initServices(authentication);
        setMainComponent(getOrgUnitView());
    }

    private void initServices(final Authentication authentication)
        throws EscidocException, InternalClientException, TransportException {
        createOrgService(authentication);
        createUserService(authentication);
        createContextService(authentication);
        createRoleService(authentication);
    }

    private RoleService roleService;

    private void createRoleService(final Authentication authentication) {
        roleService = new RoleService(authentication);
    }

    private ContextService contextService;

    private void createContextService(final Authentication authentication)
        throws EscidocException, TransportException, InternalClientException {
        contextService = new ContextService(authentication);
    }

    private OrgUnitService orgUnitService;

    private void createOrgService(final Authentication authentication)
        throws InternalClientException {
        orgUnitService = new OrgUnitService(authentication.getHandle());
    }

    private UserService userService;

    private void createUserService(final Authentication authentication)
        throws EscidocException, InternalClientException, TransportException {
        userService = new UserService(authentication.getHandle());
    }

    private final NavigationTree tree = new NavigationTree(this);

    private final SplitPanel horizontalSplit =
        new SplitPanel(SplitPanel.ORIENTATION_HORIZONTAL);

    private void buildMainLayout() {
        final Window window =
            new Window(Messages.getString("AdminToolApplication.4")); //$NON-NLS-1$
        window.setSizeFull();
        setMainWindow(window);

        setTheme(Messages.getString("AdminToolApplication.5")); //$NON-NLS-1$

        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        layout.addComponent(Toolbar.createToolbar(getMainWindow(),
            new Button[] { logoutButton }));
        layout.addComponent(horizontalSplit);
        layout.setExpandRatio(horizontalSplit, 1);

        horizontalSplit.setSplitPosition(200, SplitPanel.UNITS_PIXELS);
        horizontalSplit.setFirstComponent(tree);

        getMainWindow().setContent(layout);
    }

    private final Button logoutButton =
        new Button(Messages.getString("AdminToolApplication.6")); //$NON-NLS-1$

    private final Button newUserButton =
        new Button(Messages.getString("AdminToolApplication.7")); //$NON-NLS-1$

    /*
     * private HorizontalLayout createToolbar() { final HorizontalLayout layout
     * = new HorizontalLayout(); final Embedded em = new Embedded("", new
     * ThemeResource("images/escidoc-logo.jpg")); // new Embedded("", new
     * ThemeResource("images/escidoc-small-logo.jpg")); layout.addComponent(em);
     * layout.setComponentAlignment(em, Alignment.MIDDLE_LEFT);
     * layout.setExpandRatio(em, 1);
     * 
     * layout.addComponent(logoutButton);
     * 
     * logoutButton.addListener(new Button.ClickListener() { public void
     * buttonClick(final Button.ClickEvent event) {
     * AdminToolApplication.this.getMainWindow().showNotification( "Logout...");
     * AdminToolApplication.this.close(); } }); logoutButton.setIcon(new
     * ThemeResource("icons/32/user.png"));
     * 
     * layout.setMargin(true); layout.setSpacing(true);
     * 
     * layout.setStyleName("toolbar");
     * 
     * layout.setWidth("100%"); return layout; }
     */

    private void setMainComponent(final Component c) {
        horizontalSplit.setSecondComponent(c);
    }

    public void itemClick(final ItemClickEvent event) {
        if (event.getSource() == tree) {
            final Object itemId = event.getItemId();

            if (itemId == null) {
                return;
            }
            else {
                if (NavigationTree.ORGANIZATIONAL_UNIT.equals(itemId)) {
                    showOrganizationalUnitView();
                }
                else if (NavigationTree.CONTEXT.equals(itemId)) {
                    try {
                        showContextView();
                    }
                    catch (final EscidocException e) {
                        e.printStackTrace();
                    }
                    catch (final InternalClientException e) {
                        log.error(Messages.getString("AdminToolApplication.8"), //$NON-NLS-1$
                            e);
                        e.printStackTrace();
                    }
                    catch (final TransportException e) {
                        log.error(Messages.getString("AdminToolApplication.9"), //$NON-NLS-1$
                            e);
                        e.printStackTrace();
                    }
                }
                else if (NavigationTree.USERS_LAB.equals(itemId)) {
                    showUsersLabView();
                }
                else {
                    throw new RuntimeException(Messages
                        .getString("AdminToolApplication.10")); //$NON-NLS-1$
                }
            }
        }
    }

    private OrgUnitView orgUnitView;

    // TODO refactor to lazy init later.
    private OrgUnitList orgUnitList;

    private OrgUnitEditView orgUnitEditForm;

    private OrgUnitAddView orgUnitAddForm;

    private final List<OrganizationalUnit> todoList =
        new ArrayList<OrganizationalUnit>();

    /*
     * View getters exist so we can lazily generate the views, resulting in
     * faster application startup time.
     */
    public OrgUnitView getOrgUnitView() {
        if (orgUnitView == null) {
            orgUnitList = new OrgUnitList(this, orgUnitService);
            try {
                orgUnitEditForm = new OrgUnitEditView(this, orgUnitService);
                orgUnitEditForm.setOrgUnitList(orgUnitList);
                orgUnitView =
                    new OrgUnitView(this, orgUnitList, orgUnitEditForm,
                        orgUnitAddForm);
            }
            catch (final EscidocException e) {
                log.error(Messages.getString("AdminToolApplication.11"), //$NON-NLS-1$
                    e);
                e.printStackTrace();
            }
            catch (final InternalClientException e) {
                log.error(Messages.getString("AdminToolApplication.12"), //$NON-NLS-1$
                    e);
                e.printStackTrace();
            }
            catch (final TransportException e) {
                log.error(Messages.getString("AdminToolApplication.13"), //$NON-NLS-1$
                    e);
                e.printStackTrace();
            }
            catch (final UnsupportedOperationException e) {
                log.error(Messages.getString("AdminToolApplication.14"), //$NON-NLS-1$
                    e);
                e.printStackTrace();
            }
            catch (final Exception e) {
                new ErrorDialog(getMainWindow(), Messages
                    .getString("AdminToolApplication.15"), e.getMessage()); //$NON-NLS-1$
            }

        }
        orgUnitView.showOrganizationalUnitAddForm();

        return orgUnitView;
    }

    // TODO: move Context related views to a class.
    private ContextView contextView;

    private ContextListView contextList;

    private ContextEditForm contextForm;

    public ContextView getContextView() throws EscidocException,
        InternalClientException, TransportException {
        if (contextView == null) {
            contextList =
                new ContextListView(this, contextService, orgUnitService);
            contextForm =
                new ContextEditForm(this, contextService, orgUnitService);
            contextForm.setContextList(contextList);
            final ContextAddView contextAddView =
                new ContextAddView(this, contextList, contextService,
                    orgUnitService);
            contextView =
                new ContextView(this, contextList, contextForm, contextAddView);
        }
        contextView.showContextAddView();
        return contextView;
    }

    public ContextAddView newContextAddView() {
        return new ContextAddView(this, contextList, contextService,
            orgUnitService);
    }

    public OrgUnitList getOrgUnitTable() {
        if (orgUnitList == null) {
            orgUnitList = new OrgUnitList(this, orgUnitService);
        }
        return orgUnitList;
    }

    private void showUserAddForm() {
        showUsersView();
        getUsersView().showUserAddForm();
    }

    public void showOrganizationalUnitView() {
        setMainComponent(getOrgUnitView());
    }

    private void showOrganizationalUnitAddForm() {
        setMainComponent(getOrganizationalUnitAddForm());
    }

    public void showContextView() throws EscidocException,
        InternalClientException, TransportException {
        setMainComponent(getContextView());
    }

    // TODO fix this, why do we have to create the add form every time?
    private OrgUnitAddView getOrganizationalUnitAddForm() {
        orgUnitAddForm = new OrgUnitAddView(this, orgUnitService);
        return orgUnitAddForm;
    }

    public void valueChange(final ValueChangeEvent event) {
        final Property property = event.getProperty();
        // TODO this is not OOP, redesign this part of code.
        if (property == orgUnitList) {
            final Item item = orgUnitList.getItem(orgUnitList.getValue());

            if (item != null) {
                orgUnitView.showEditView(item);
            }
        }
        else if (property == contextList) {
            if (contextView.getSelectedItem() == null) {
                contextView.showContextAddView();
            }
            else {
                contextView.showEditView(contextView.getSelectedItem());
            }
        }
        else if (property == userLabView.getUserList()) {
            final Item item = userLabView.getSelectedItem();

            if (item == null) {
                userLabView.showAddView();
            }
            else {
                userLabView.showEditView(item);
            }
        }
        else {
            throw new RuntimeException(Messages
                .getString("AdminToolApplication.16")); //$NON-NLS-1$
        }
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == newUserButton) {
            showUserAddForm();
        }
        else {
            throw new RuntimeException(Messages
                .getString("AdminToolApplication.17") + source); //$NON-NLS-1$
        }
    }

    public void showUsersView() {
        setMainComponent(getUsersView());
    }

    private void showUsersLabView() {
        setMainComponent(getUsersLabView());
    }

    private UserView userView;

    private UserLabEditForm userLabEditForm;

    private UserView getUsersView() {
        if (userView == null) {
            try {

                userView = new UserView(this, getUserService());
            }
            catch (final EscidocClientException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        userView.showEditForm();
        return userView;
    }

    private UserLabView userLabView;

    private UserLabListView userLabList;

    private UserLabView getUsersLabView() {
        if (userLabView == null) {
            userLabList = new UserLabListView(this, userService);
            userLabEditForm = new UserLabEditForm(this, userService);
            final UserLabEditView userEditView =
                new UserLabEditView(userLabEditForm);
            userLabView =
                new UserLabView(this, getUserService(), userLabList,
                    userEditView);
        }
        userLabView.showAddView();
        return userLabView;
    }

    private UserService getUserService() {
        return userService;
    }

    public UserLabAddView newUserLabAddView() {
        return new UserLabAddView(this, userLabView.getUserList(), userService,
            orgUnitService);
    }

    public Component newOrgUnitAddView() throws EscidocException,
        InternalClientException, TransportException {
        return new OrgUnitAddView(this, orgUnitService);
    }
}