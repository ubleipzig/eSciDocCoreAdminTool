package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button.ClickEvent;

interface ModifyParentOrgUnitListener {

    public abstract void bind(final Item item);

    public abstract void buttonClick(final ClickEvent event);

    public abstract void setParentProperty(final Property parentProperty);

}