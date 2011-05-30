package de.escidoc.admintool.view.user;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.resource.ResourceTreeView;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class UserViewComponent {

    private static final Logger LOG = LoggerFactory.getLogger(UserViewComponent.class);

    private UserEditForm userEditForm;

    private UserView userView;

    private UserEditView userEditView;

    private final AdminToolApplication app;

    private final UserService userService;

    private UserListView listView;

    private UserEditForm editForm;

    private final OrgUnitServiceLab orgUnitService;

    private final ResourceTreeView resourceTreeView;

    private final PdpRequest pdpRequest;

    public UserViewComponent(final AdminToolApplication app, final UserService userService,
        final ResourceService orgUnitService, final ResourceTreeView resourceTreeView, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(app, "AdminToolApplication is null.");
        Preconditions.checkNotNull(userService, "UserService is null.");
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        this.app = app;
        this.userService = userService;
        this.orgUnitService = (OrgUnitServiceLab) orgUnitService;
        this.resourceTreeView = resourceTreeView;
        this.pdpRequest = pdpRequest;
    }

    public void init() {
        createListView();
        createEditForm();
        createUserView();
        setUserView(userView);
    }

    private void createUserView() {
        userView = new UserView(app, listView, getUserEditView());
        userView.buildUI();
    }

    private void createEditForm() {
        editForm = new UserEditForm(app, userService, orgUnitService, resourceTreeView, pdpRequest);
        editForm.init();
        setUserEditForm(editForm);
        setUserEditView(new UserEditView(getUserEditForm()));
    }

    private void createListView() {
        listView = new UserListView(app, userService);
    }

    public UserView getUserView() {
        return userView;
    }

    public void showFirstItemInEditView() {
        if (listView.firstItemId() == null) {
            return;
        }
        listView.select(listView.firstItemId());
        userView.showEditView(getFirstItem());
    }

    private Item getFirstItem() {
        return listView.getContainerDataSource().getItem(listView.firstItemId());
    }

    /**
     * @param userView
     *            the userView to set
     */
    public void setUserView(final UserView userView) {
        this.userView = userView;
    }

    public void setUserEditForm(final UserEditForm userEditForm) {
        this.userEditForm = userEditForm;
    }

    public UserEditForm getUserEditForm() {
        return userEditForm;
    }

    public void setUserEditView(final UserEditView userEditView) {
        this.userEditView = userEditView;
    }

    /**
     * @return the userEditView
     */
    public UserEditView getUserEditView() {
        return userEditView;
    }

    public void showAddView() {
        userView.showAddView();
    }

    // FIXME: this is a hack, an won't perform well.
    // To improve: Implement equals() and hashCode in DTO or Domain Object i.e.
    // UserAccount or MyUserAccount
    public void showUserInEditView(final UserAccount user) {
        for (final UserAccount userAccount : getAllUserAccountsFromContainer()) {
            if (hasEqualsId(user, userAccount)) {
                selectFoundUserInListView(userAccount);
                showUserFoundUserInEditView(userAccount);
            }
        }
    }

    private void selectFoundUserInListView(final UserAccount userAccount) {
        listView.select(userAccount);
    }

    private void showUserFoundUserInEditView(final UserAccount userAccount) {
        userView.showEditView(listView.getContainerDataSource().getItem(userAccount));
    }

    @SuppressWarnings("unchecked")
    private Collection<UserAccount> getAllUserAccountsFromContainer() {
        return (Collection<UserAccount>) listView.getContainerDataSource().getItemIds();
    }

    private boolean hasEqualsId(final UserAccount user, final UserAccount userAccount) {
        return user.getObjid().equalsIgnoreCase(userAccount.getObjid());
    }
}