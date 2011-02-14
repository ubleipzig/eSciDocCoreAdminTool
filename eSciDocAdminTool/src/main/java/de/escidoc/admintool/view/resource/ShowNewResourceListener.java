package de.escidoc.admintool.view.resource;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

final class ShowNewResourceListener implements ClickListener {
    private static final long serialVersionUID = 821488759494602652L;

    private final ResourceViewImpl resourceView;

    public ShowNewResourceListener(final ResourceViewImpl resourceView) {
        this.resourceView = resourceView;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        resourceView.showAddView();
    }
}