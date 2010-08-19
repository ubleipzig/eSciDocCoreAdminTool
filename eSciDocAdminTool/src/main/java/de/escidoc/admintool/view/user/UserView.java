package de.escidoc.admintool.view.user;

import com.vaadin.data.Item;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class UserView extends SplitPanel {
    private static final long serialVersionUID = -3544643662536040641L;

    private final UserList userList;

    private final UserEditForm userEditForm;

    private final UserAddForm userAddForm;

    public UserView(final AdminToolApplication addressBookApplication,
        final UserService userService) throws EscidocClientException {

        userList = new UserList(addressBookApplication, userService);
        userEditForm = new UserEditForm(userService);
        userAddForm =
            new UserAddForm(userService, userList, addressBookApplication);
        addStyleName("view");

        setFirstComponent(userList);
        setSecondComponent(userEditForm);

        // TODO a hack
        this.showEditForm();
        this.setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);
    }

    public UserList getUserList() {
        return userList;
    }

    public Item getSelectedItem() {
        return userList.getItem(userList.getValue());
    }

    // TODO refactor this. not the responsibility of this class.
    public UserView showUserAddForm() {
        removeComponent(getSecondComponent());
        userAddForm.discard();
        userAddForm.setReadOnly(false);
        userAddForm.setVisible(true);
        userAddForm.setValidationVisible(false);

        setSecondComponent(userAddForm);
        return this;
    }

    public UserView showEditForm() {
        removeComponent(getSecondComponent());
        setSecondComponent(userEditForm);
        return this;
    }

    // TODO refactor this method: too much responsibility
    public void showEditForm(final Item item) {
        assert item != null : "Item must not be null.";

        this.showEditForm();
        userEditForm.setSelected(item);
    }
}