package de.escidoc.admintool.view.user;

import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UserEditView extends VerticalLayout {

    private final UserEditForm userLabEditForm;

    public UserEditView(final UserEditForm userLabForm) {
        userLabEditForm = userLabForm;
        buildUI();
    }

    private void buildUI() {
        addStyleName("view");
        addComponent(userLabEditForm);
    }

    public void setSelected(final Item item) {
        userLabEditForm.setSelected(item);
    }
}