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
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.NavigationTree;
import de.escidoc.admintool.view.Toolbar;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextEditForm;
import de.escidoc.admintool.view.context.ContextListView;
import de.escidoc.admintool.view.context.ContextView;
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
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.vaadin.dialog.ErrorDialog;

@SuppressWarnings("serial")
public class AdminToolApplication extends Application
    implements ValueChangeListener, ItemClickListener {
    // TODO / FIXME
    // too many method
    // too many fields
    // =>God Class=>refactor logical code to an extra class.
    private static final Logger log = LoggerFactory
        .getLogger(AdminToolApplication.class);

    private final Window mainWindow = new Window(
        ViewConstants.MAIN_WINDOW_TITLE);

    private final SplitPanel horizontalSplit = new SplitPanel(
        SplitPanel.ORIENTATION_HORIZONTAL);

    private final Button logoutButton = new Button(
        Messages.getString("AdminToolApplication.6")); //$NON-NLS-1$

    private final Button newUserButton = new Button(
        Messages.getString("AdminToolApplication.7")); //$NON-NLS-1$

    private final StartPage startPage = new StartPage();

    private final NavigationTree tree = new NavigationTree(this);

    // TODO: move Context related views to a class.
    private ContextView contextView;

    private ContextEditForm contextForm;

    private ContextListView contextList;

    private ContextService contextService;

    private OrgUnitAddView orgUnitAddForm;

    private OrgUnitEditView orgUnitEditForm;

    private OrgUnitListView orgUnitList;

    private OrgUnitService orgUnitService;

    private OrgUnitView orgUnitView;

    // FIXME: FindBugs reports following warning:
    /*
     * "This Serializable class defines a non-primitive instance field which is
     * neither transient, Serializable, or java.lang.Object, and does not appear
     * to implement the Externalizable interface or the readObject() and
     * writeObject() methods. Objects of this class will not be deserialized
     * correctly if a non-Serializable object is stored in this field."
     */
    private RoleService roleService;

    private RoleView roleView;

    private UserEditForm userEditForm;

    private UserListView userListView;

    private UserService userService;

    private UserView userView;

    @Override
    public void init() {
        initMainWindow();
        addParameterHandler();
    }

    private void addLogoutButton() {
        logoutButton.setIcon(new ThemeResource(Messages
            .getString("AdminToolApplication.1")));
        logoutButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                mainWindow.open(new ExternalResource(
                    AdminToolContants.LOGOUT_URL + getURL()));
            }
        });
    }

    private void addParameterHandler() {
        mainWindow.addParameterHandler(new ParamaterHandlerImpl(mainWindow,
            this));
    }

    public void authenticate(final String token) throws AuthenticationException {
        try {
            loadProtectedResources(token);
        }
        catch (final AuthenticationException e) {
            getMainWindow().showNotification(
                new Notification(ViewConstants.SERVER_INTERNAL_ERROR, e
                    .getMessage(), Notification.TYPE_ERROR_MESSAGE));
            log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final EscidocException e) {
            getMainWindow().showNotification(
                new Notification(ViewConstants.SERVER_INTERNAL_ERROR, e
                    .getMessage(), Notification.TYPE_ERROR_MESSAGE));
            log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final InternalClientException e) {
            getMainWindow().showNotification(
                new Notification(ViewConstants.SERVER_INTERNAL_ERROR, e
                    .getMessage(), Notification.TYPE_ERROR_MESSAGE));
            log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final TransportException e) {
            getMainWindow().showNotification(
                new Notification(ViewConstants.SERVER_INTERNAL_ERROR, e
                    .getMessage(), Notification.TYPE_ERROR_MESSAGE));
            log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
    }

    private void buildMainLayout() {
        mainWindow.setSizeFull();
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        layout.addComponent(Toolbar.createToolbar(getMainWindow(),
            new Button[] { logoutButton }));
        layout.addComponent(horizontalSplit);
        layout.setExpandRatio(horizontalSplit, 1);

        horizontalSplit.setSplitPosition(200, SplitPanel.UNITS_PIXELS);
        horizontalSplit.setFirstComponent(tree);

        getMainWindow().setContent(layout);
        addLogoutButton();
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        // if (source == newUserButton) {
        // showUserAddForm();
        // }
        if (source.equals(newUserButton)) {
            showUserAddForm();
        }
        else {
            throw new RuntimeException(
                Messages.getString("AdminToolApplication.17") + source); //$NON-NLS-1$
        }
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
        contextView.showContextAddView();
        return contextView;
    }

    public OrgUnitService getOrgUnitService() {
        return orgUnitService;
    }

    public OrgUnitListView getOrgUnitTable() {
        if (orgUnitList == null) {
            orgUnitList = new OrgUnitListView(this, orgUnitService);
        }
        return orgUnitList;
    }

    /*
     * View getters exist so we can lazily generate the views, resulting in
     * faster application startup time.
     */
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
                getMainWindow().addWindow(
                    new ErrorDialog(getMainWindow(),
                        ViewConstants.SERVER_INTERNAL_ERROR, e.getMessage())); //$NON-NLS-1$
            }
            catch (final InternalClientException e) {
                getMainWindow().addWindow(
                    new ErrorDialog(getMainWindow(),
                        ViewConstants.SERVER_INTERNAL_ERROR, e.getMessage()));
            }
            catch (final TransportException e) {
                getMainWindow().addWindow(
                    new ErrorDialog(getMainWindow(),
                        ViewConstants.SERVER_INTERNAL_ERROR, e.getMessage()));
            }
            catch (final UnsupportedOperationException e) {
                getMainWindow().addWindow(
                    new ErrorDialog(getMainWindow(),
                        ViewConstants.SERVER_INTERNAL_ERROR, e.getMessage()));
            }
            catch (final Exception e) {
                getMainWindow().addWindow(
                    new ErrorDialog(getMainWindow(),
                        ViewConstants.SERVER_INTERNAL_ERROR, e.getMessage()));
            }
        }
        orgUnitView.showOrganizationalUnitAddForm();

        return orgUnitView;
    }

    private RoleService getRoleService() {
        return roleService;
    }

    private RoleView getRoleView() {
        if (roleView == null) {
            roleView =
                new RoleView(this, roleService, userService, contextService);
        }
        return roleView;
    }

    private UserView getUsersLabView() {
        if (userView == null) {
            userListView = new UserListView(this, userService);
            userEditForm =
                new UserEditForm(this, userService, getRoleService());
            final UserEditView userEditView = new UserEditView(userEditForm);
            userView = new UserView(this, userListView, userEditView);
        }
        userView.showAddView();
        return userView;
    }

    private void initMainWindow() {
        setTheme(AdminToolContants.THEME_NAME);
        setMainWindow(mainWindow);
    }

    private void initServices(final String token) throws EscidocException,
        InternalClientException, TransportException {
        final ServiceFactory serviceFactory = new ServiceFactory(token);
        orgUnitService = serviceFactory.createOrgService();
        userService = serviceFactory.createUserService();
        contextService = serviceFactory.createContextService();
        roleService = serviceFactory.createRoleService();
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
                        log.error(Messages.getString("AdminToolApplication.8"), //$NON-NLS-1$
                            e);
                    }
                    catch (final InternalClientException e) {
                        log.error(Messages.getString("AdminToolApplication.8"), //$NON-NLS-1$
                            e);
                    }
                    catch (final TransportException e) {
                        log.error(Messages.getString("AdminToolApplication.9"), //$NON-NLS-1$
                            e);
                    }
                }
                else if (NavigationTree.USERS_LAB.equals(itemId)) {
                    showUsersLabView();
                }
                else if (NavigationTree.ROLE.equals(itemId)) {
                    showRoleView();
                }
                else {
                    throw new RuntimeException(
                        Messages.getString("AdminToolApplication.10")); //$NON-NLS-1$
                }
            }
        }
    }

    private void loadProtectedResources(final String token)
        throws EscidocException, InternalClientException, TransportException {
        initServices(token);
        buildMainLayout();
        setMainComponent(startPage);
    }

    public ContextAddView newContextAddView() {
        return new ContextAddView(this, contextList, contextService,
            orgUnitService);
    }

    public Component newOrgUnitAddView() throws EscidocException,
        InternalClientException, TransportException {
        return new OrgUnitAddView(this, orgUnitService);
    }

    public UserAddView newUserLabAddView() {
        return new UserAddView(this, userView.getUserList(), userService,
            orgUnitService);
    }

    private void setMainComponent(final Component component) {
        horizontalSplit.setSecondComponent(component);
    }

    public void showContextView() throws EscidocException,
        InternalClientException, TransportException {
        setMainComponent(getContextView());
    }

    public void showOrganizationalUnitView() {
        setMainComponent(getOrgUnitView());
    }

    public void showRoleView() {
        setMainComponent(getRoleView());
    }

    public void showRoleView(final UserAccount userAccount) {
        roleView.selectUser(userAccount);
        setMainComponent(roleView);
    }

    private void showUserAddForm() {
        showUsersLabView();
        getUsersLabView();
    }

    public void showUserInEditView(final UserAccount user) {
        getUsersLabView();
        userListView.select(user);
    }

    private void showUsersLabView() {
        setMainComponent(getUsersLabView());
    }

    public void showUsersView() {
        setMainComponent(getUsersLabView());
    }

    public void valueChange(final ValueChangeEvent event) {
        final Property property = event.getProperty();
        // TODO this is not OOP, redesign this part of code.
        if (property.equals(orgUnitList)) {
            final Item item = orgUnitList.getItem(orgUnitList.getValue());

            if (item != null) {
                orgUnitView.showEditView(item);
            }
        }
        else if (property.equals(contextList)) {
            final Item item = contextView.getSelectedItem();
            if (item == null) {
                contextView.showContextAddView();
            }
            else {
                contextView.showEditView(item);
            }
        }
        else if (property.equals(userView.getUserList())) {
            final Item item = userView.getSelectedItem();

            if (item == null) {
                userView.showAddView();
            }
            else {
                userView.showEditView(item);
            }
        }
        else {
            throw new RuntimeException(
                Messages.getString("AdminToolApplication.16")); //$NON-NLS-1$
        }
    }
}