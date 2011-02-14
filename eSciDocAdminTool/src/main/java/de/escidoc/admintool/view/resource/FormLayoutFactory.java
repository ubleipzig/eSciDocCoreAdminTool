package de.escidoc.admintool.view.resource;

import com.vaadin.ui.FormLayout;

public class FormLayoutFactory {

    public static FormLayout create() {
        final FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setWidth("800px");
        formLayout.setHeight("100%");
        return formLayout;
    }
}
