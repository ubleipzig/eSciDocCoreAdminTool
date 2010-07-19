package de.escidoc.admintool.app;

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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.LoginWindow;
import de.escidoc.admintool.view.NavigationTree;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextEditForm;
import de.escidoc.admintool.view.context.ContextEditViewWithToolbar;
import de.escidoc.admintool.view.context.ContextListView;
import de.escidoc.admintool.view.context.ContextView;
import de.escidoc.admintool.view.orgunit.OrgUnitAddForm;
import de.escidoc.admintool.view.orgunit.OrgUnitEditForm;
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
import de.escidoc.core.test.client.EscidocClientTestBase;

//NOTE: the code of this prototype contains a lot of duplications, badly designed and implemented
// Thus a Hack, it needs a lot of refactoring. Reason was to create a functional prototype as fast as possible.
//After main functionality of the requirements are implemented, we can do the refactoring.
@SuppressWarnings("serial")
public class AdminToolApplication extends Application
    implements ApplicationContext.TransactionListener, ClickListener,
    ValueChangeListener, ItemClickListener {
    private final Logger log =
        LoggerFactory.getLogger(AdminToolApplication.class);

    @Override
    public void init() {
        log.info("Hello World!");

        getContext().addTransactionListener(this);
        showLoginWindow();
    }

    private void showLoginWindow() {
        setMainWindow(new LoginWindow());
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
        TransportException {

        final Authentication authentication = new Authentication();
        authentication.login(EscidocClientTestBase.DEFAULT_SERVICE_URL, login,
            password);

        currentUser = login;
        // TODO show error to the user.
        try {
            loadProtectedResources(authentication);
        }
        catch (final EscidocException e) {
            // TODO log exception.
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
        setMainComponent(getOrganizationalUnitView());
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
        setMainWindow(new Window("Admin Tool Prototype"));

        setTheme("contacts");

        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        layout.addComponent(createToolbar());
        layout.addComponent(horizontalSplit);
        layout.setExpandRatio(horizontalSplit, 1);

        horizontalSplit.setSplitPosition(200, SplitPanel.UNITS_PIXELS);
        horizontalSplit.setFirstComponent(tree);

        getMainWindow().setContent(layout);
    }

    private final Button newOrgUnitButton = new Button("Add OU");

    private final Button logoutButton = new Button("Sign out");

    private final Button newUserButton = new Button("Add User");

    // TODO move this to separate class
    private HorizontalLayout createToolbar() {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(newOrgUnitButton);
        layout.addComponent(logoutButton);

        newOrgUnitButton.addListener((ClickListener) this);
        newOrgUnitButton
            .setIcon(new ThemeResource("icons/32/document-add.png"));

        logoutButton.addListener(new Button.ClickListener() {
            public void buttonClick(final Button.ClickEvent event) {
                AdminToolApplication.this.getMainWindow().showNotification(
                    "Logout...");
                AdminToolApplication.this.close();
            }
        });
        logoutButton.setIcon(new ThemeResource("icons/32/user.png"));

        layout.setMargin(true);
        layout.setSpacing(true);

        layout.setStyleName("toolbar");

        layout.setWidth("100%");

        final Embedded em =
            new Embedded("", new ThemeResource("images/escidoc-small-logo.jpg"));
        layout.addComponent(em);
        layout.setComponentAlignment(em, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(em, 1);

        return layout;
    }

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
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (final TransportException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else if (NavigationTree.USERS_LAB.equals(itemId)) {
                    showUsersLabView();
                }
                else {
                    throw new RuntimeException("Unknown ItemID.");
                }
            }
        }
    }

    private OrgUnitView organizationalUnitlistView;

    // TODO refactor to lazy init later.
    private OrgUnitList orgUnitList = new OrgUnitList(this, orgUnitService);

    private OrgUnitEditForm orgUnitEditForm;

    private OrgUnitAddForm orgUnitAddForm;

    /*
     * View getters exist so we can lazily generate the views, resulting in
     * faster application startup time.
     */
    private OrgUnitView getOrganizationalUnitView() {
        if (organizationalUnitlistView == null) {

            orgUnitList = new OrgUnitList(this, orgUnitService);
            try {
                orgUnitEditForm = new OrgUnitEditForm(this, orgUnitService);
            }
            catch (final EscidocException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final InternalClientException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final TransportException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final UnsupportedOperationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            organizationalUnitlistView =
                new OrgUnitView(orgUnitList, orgUnitEditForm);
        }
        return organizationalUnitlistView;
    }

    // TODO: move Context related views to a class.
    private ContextView contextView;

    private ContextListView contextList;

    private ContextEditForm contextForm;

    private Component getContextView() throws EscidocException,
        InternalClientException, TransportException {
        if (contextView == null) {
            contextList =
                new ContextListView(this, contextService, orgUnitService);
            contextForm =
                new ContextEditForm(this, contextService, orgUnitService);
            final ContextEditViewWithToolbar contextEditView =
                new ContextEditViewWithToolbar(contextForm);
            final ContextAddView contextAddView =
                new ContextAddView(this, contextList, contextService,
                    orgUnitService);
            contextView =
                new ContextView(this, contextList, contextEditView,
                    contextAddView);
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
        setMainComponent(getOrganizationalUnitView());
    }

    private void showOrganizationalUnitAddForm() {
        setMainComponent(getOrganizationalUnitAddForm());
    }

    public void showContextView() throws EscidocException,
        InternalClientException, TransportException {
        setMainComponent(getContextView());
    }

    // TODO fix this, why do we have to create the add form every time?
    private OrgUnitAddForm getOrganizationalUnitAddForm() {
        orgUnitAddForm =
            new OrgUnitAddForm(this, orgUnitService, orgUnitList
                .getAllOrgUnits(), orgUnitList);
        return orgUnitAddForm;
    }

    public void valueChange(final ValueChangeEvent event) {
        final Property property = event.getProperty();
        // TODO this is not OOP, redesign this part of code.
        if (property == orgUnitList) {
            final Item item = orgUnitList.getItem(orgUnitList.getValue());

            if (item != orgUnitEditForm.getItemDataSource()) {
                orgUnitEditForm.setItemDataSource(item);
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
            throw new RuntimeException("Unknown property.");
        }
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == newOrgUnitButton) {
            showOrganizationalUnitAddForm();
        }
        else if (source == newUserButton) {
            showUserAddForm();
        }
        else {
            throw new RuntimeException("Unknown button" + source);
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
        return new UserLabAddView(this, userLabView.getUserList(), userService);
    }
}