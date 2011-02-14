package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

interface AddOrChangeParentListener extends com.vaadin.ui.Button.ClickListener {

    void bind(final Item item);

    void setParentProperty(final Property parentProperty);

}