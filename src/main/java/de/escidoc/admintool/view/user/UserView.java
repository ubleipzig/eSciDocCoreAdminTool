package de.escidoc.admintool.view.user;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class UserView extends SplitPanel implements ResourceView {

    private final AdminToolApplication app;

    private final UserListView userList;

    private final UserEditView userEditView;

    private final VerticalLayout vLayout = new VerticalLayout();

    public UserView(final AdminToolApplication app,
        final UserListView userListView, final UserEditView userEditView) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(userListView, "userListView is null: %s",
            userListView);
        Preconditions.checkNotNull(userEditView, "userLabEditView is null: %s",
            userEditView);
        this.app = app;
        this.userList = userListView;
        this.userEditView = userEditView;
    }

    public void init() {
        setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);

        vLayout.setHeight(100, UNITS_PERCENTAGE);
        addHeader(vLayout);
        addListView(vLayout);
        setFirstComponent(vLayout);
    }

    private void addHeader(final VerticalLayout vLayout) {
        vLayout.addComponent(new Label("<b>User Accounts</b>",
            Label.CONTENT_XHTML));
    }

    private void addListView(final VerticalLayout vLayout) {
        userList.setSizeFull();
        vLayout.addComponent(userList);
        vLayout.setExpandRatio(userList, 1.0f);
    }

    public UserListView getUserList() {
        return userList;
    }

    public Item getSelectedItem() {
        return userList.getItem(userList.getValue());
    }

    @Override
    public void showAddView() {
        setSecondComponent(app.newUserAddView());
    }

    public Item toItem(final UserAccount user) {
        return userList.getContainerDataSource().getItem(user);
    }

    @Override
    public void showEditView(final Item item) {
        setSecondComponent(userEditView);
        userEditView.setSelected(item);
    }

    public void remove(final UserAccount deletedUser) {
        userList.remove(deletedUser);
        showAddView();
    }

    @Override
    public void selectInFolderView(final Resource resource) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}