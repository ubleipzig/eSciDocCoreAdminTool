package de.escidoc.admintool.view;

import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

import de.escidoc.admintool.app.AdminToolApplication;

@SuppressWarnings("serial")
public class NavigationTree extends Tree {

    private final String[] MENU_ACTIONS = { ViewConstants.ORGANIZATIONAL_UNIT, ViewConstants.CONTEXT,
        ViewConstants.USERS_LAB, ViewConstants.ROLE };

    public NavigationTree(final AdminToolApplication app) {
        for (final String action : MENU_ACTIONS) {
            this.addItem(action);
            setChildrenAllowed(action, false);
        }
        setSelectable(true);
        setNullSelectionAllowed(false);
        this.addListener((ItemClickListener) app);
    }
}