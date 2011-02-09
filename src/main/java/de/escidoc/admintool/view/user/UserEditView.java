package de.escidoc.admintool.view.user;

import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UserEditView extends VerticalLayout {

    private final UserEditForm usemEditForm;

    public UserEditView(final UserEditForm userLabForm) {
        usemEditForm = userLabForm;
        buildUI();
    }

    private void buildUI() {
        addStyleName("view");
        addComponent(usemEditForm);
    }

    public void setSelected(final Item item) {
        usemEditForm.setSelected(item);
    }
}