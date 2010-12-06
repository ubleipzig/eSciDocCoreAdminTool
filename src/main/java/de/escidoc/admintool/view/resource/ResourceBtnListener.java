package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public interface ResourceBtnListener extends ClickListener {

  void buttonClick(final ClickEvent event);

  void bind(final Item item);

}