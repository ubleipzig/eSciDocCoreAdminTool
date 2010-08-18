package de.escidoc.admintool.view;

import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.messages.Messages;

@SuppressWarnings("serial")
public class NavigationTree extends Tree {

    // TODO put these in files or extra contents class,i.e. Label,
    public static final String ORGANIZATIONAL_UNIT = Messages.getString("NavigationTree.0"); //$NON-NLS-1$

    public static final String CONTEXT = Messages.getString("NavigationTree.1"); //$NON-NLS-1$

    public static final String USERS_LAB = Messages.getString("NavigationTree.2"); //$NON-NLS-1$

    private final String[] MENU_ACTIONS = { ORGANIZATIONAL_UNIT, CONTEXT,
        USERS_LAB };

    public NavigationTree(final AdminToolApplication adminToolApplication) {
        for (String action : MENU_ACTIONS) {
            this.addItem(action);
            setChildrenAllowed(action, false);

        }
        /*
         * We want items to be selectable but do not want the user to be able to
         * deselect an item.
         */
        setSelectable(true);
        setNullSelectionAllowed(false);

        // Make application handle item click events
        this.addListener((ItemClickListener) adminToolApplication);
    }
}