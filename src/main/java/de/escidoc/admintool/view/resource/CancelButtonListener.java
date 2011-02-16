package de.escidoc.admintool.view.resource;

import java.util.Collection;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;

public class CancelButtonListener implements ClickListener {

    private static final long serialVersionUID = -8828673159888176138L;

    private final Collection<Field> allFields;

    public CancelButtonListener(final Collection<Field> allFields) {
        this.allFields = allFields;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        for (final Field field : allFields) {
            field.discard();
        }
    }
}
