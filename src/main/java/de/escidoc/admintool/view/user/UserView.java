package de.escidoc.admintool.view.user;

import com.vaadin.data.Item;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class UserView extends SplitPanel {

    private final AdminToolApplication app;

    private final UserListView userLabList;

    private final UserEditView userLabEditView;

    public UserView(final AdminToolApplication app,
        final UserListView userLabList, final UserEditView userLabEditView) {
        this.app = app;
        this.userLabList = userLabList;
        this.userLabEditView = userLabEditView;
        buildUI();
    }

    private void buildUI() {
        setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);
        setFirstComponent(userLabList);
    }

    public UserListView getUserList() {
        return userLabList;
    }

    public Item getSelectedItem() {
        return userLabList.getItem(userLabList.getValue());
    }

    public UserView showAddView() {
        setSecondComponent(app.newUserLabAddView());
        return this;
    }

    public void showEditView(final Item item) {
        setSecondComponent(userLabEditView);
        userLabEditView.setSelected(item);
    }

    public void remove(final UserAccount deletedUser) {
        userLabList.remove(deletedUser);
        showAddView();
    }

}