package de.escidoc.admintool.view;

import com.vaadin.event.ItemClickEvent.ItemClickListener;

import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.navigation.NavigationTree;
import de.escidoc.admintool.view.navigation.NavigationTreeImpl;

public class NavigationTreeFactory {

    public static NavigationTree createViewFor(final ItemClickListener listener, final PdpRequest pdpRequest) {
        return new NavigationTreeImpl(listener, pdpRequest).init();
    }

}
