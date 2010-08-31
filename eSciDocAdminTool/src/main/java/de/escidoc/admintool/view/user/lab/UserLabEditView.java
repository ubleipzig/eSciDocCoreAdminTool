package de.escidoc.admintool.view.user.lab;

import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UserLabEditView extends VerticalLayout {

    private final UserLabEditForm userLabEditForm;

    public UserLabEditView(final UserLabEditForm userLabForm) {
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