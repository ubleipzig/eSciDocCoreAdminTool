package de.escidoc.admintool.view.user;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.view.resource.PropertiesFieldsImpl;

public class UserAccountPropertiesField extends PropertiesFieldsImpl {
    private static final long serialVersionUID = -6239507022687068300L;

    public UserAccountPropertiesField(final VerticalLayout vLayout,
        final FormLayout formLayout, final Map<String, Field> fieldByName) {
        super(vLayout, formLayout, fieldByName);
        configure();
    }

    private void configure() {
        // getTitleField().setCaption(ViewConstants.NAME_LABEL);
        // final TextField loginField =
        // new TextField(ViewConstants.LOGIN_NAME_LABEL);
        // getFormLayout().addComponent(loginField, 1);
        // getFormLayout().removeComponent(getDescField());
        // getFormLayout().removeComponent(getStatusField());
        // getFormLayout().removeComponent(getStatusComment());
    }

    @Override
    public void bind(final Item item) {
        Preconditions.checkNotNull(item, "item is null: %s", item);
        // Preconditions.checkNotNull(getBinder(), "binder is null: %s",
        // getBinder());
    }
}
