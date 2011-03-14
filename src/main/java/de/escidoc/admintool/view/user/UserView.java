package de.escidoc.admintool.view.user;

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

    public UserView(final AdminToolApplication app,
        final UserListView userLabList, final UserEditView userLabEditView) {
        this.app = app;
        userList = userLabList;
        userEditView = userLabEditView;
        buildUI();
    }

    public void buildUI() {
        setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);

        final VerticalLayout vLayout = new VerticalLayout();
        vLayout.setHeight(100, UNITS_PERCENTAGE);
        final Label foo =
            new Label("<b>User Accounts</b>", Label.CONTENT_XHTML);
        vLayout.addComponent(foo);
        userList.setSizeFull();

        vLayout.addComponent(userList);
        vLayout.setExpandRatio(userList, 1.0f);
        setFirstComponent(vLayout);

        setFirstComponent(vLayout);
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