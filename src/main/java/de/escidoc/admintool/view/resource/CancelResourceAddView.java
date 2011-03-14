package de.escidoc.admintool.view.resource;

import java.util.Collection;
import java.util.Map;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;

final class CancelResourceAddView implements ClickListener {

    private final Map<String, Field> fieldByName;

    CancelResourceAddView(final Map<String, Field> fieldByName) {
        this.fieldByName = fieldByName;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Collection<Field> values = fieldByName.values();
        for (final Field field : values) {
            field.discard();
        }
    }
}