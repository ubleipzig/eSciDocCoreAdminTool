package de.escidoc.admintool.view;

import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

import de.escidoc.admintool.app.AdminToolApplication;

@SuppressWarnings("serial")
public class NavigationTree extends Tree {

    // TODO put these in files or extra contants class,i.e. Label,
    public static final String ORGANIZATIONAL_UNIT = "Organizational Unit";

    public static final String CONTEXT = "Context";

    public static final String USERS_LAB = "User Accounts";

    public NavigationTree(final AdminToolApplication adminToolApplication) {
        this.addItem(ORGANIZATIONAL_UNIT);
        this.addItem(CONTEXT);
        // this.addItem(USERS);
        this.addItem(USERS_LAB);

        // TODO make it simpler, DRY principle
        setChildrenAllowed(ORGANIZATIONAL_UNIT, false);
        setChildrenAllowed(CONTEXT, false);
        // setChildrenAllowed(USERS, false);
        setChildrenAllowed(USERS_LAB, false);

        /*
         * We want items to be selectable but do not want the user to be able to
         * de-select an item.
         */
        setSelectable(true);
        setNullSelectionAllowed(false);

        // Make application handle item click events
        this.addListener((ItemClickListener) adminToolApplication);
    }
}