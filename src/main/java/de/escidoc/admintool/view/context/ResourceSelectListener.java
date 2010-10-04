package de.escidoc.admintool.view.context;

import com.vaadin.event.ItemClickEvent.ItemClickListener;

import de.escidoc.admintool.view.ResourceView;

public interface ResourceSelectListener extends ItemClickListener {

    ResourceView getView();
}
