package de.uni_leipzig.ubl.admintool.view.group.selector;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.AdminToolApplication;

public class AddInternalSelectorButtonListener implements ClickListener {

	private static final long serialVersionUID = -932605445557648169L;
	
	private final AdminToolApplication app;
	
	private final AddInternalSelector addInternalSelector;
	
	private Collection<Item> selectedItems;
	
	public AddInternalSelectorButtonListener(final AdminToolApplication app, final AddInternalSelector addInternalSelector) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(addInternalSelector, "addInternal Selector is null: %s", addInternalSelector);
		
		this.app = app;
		this.addInternalSelector = addInternalSelector;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		selectedItems = addInternalSelector.getSelected();
		
		app.getMainWindow().showNotification("internal Selectors (" + selectedItems + ") should be added.");
		}

}
