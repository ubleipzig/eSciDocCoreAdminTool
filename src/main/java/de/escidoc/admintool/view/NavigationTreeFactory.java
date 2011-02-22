package de.escidoc.admintool.view;

import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.navigation.NavigationTree;
import de.escidoc.admintool.view.navigation.NavigationTreeClickListener;
import de.escidoc.admintool.view.navigation.NavigationTreeImpl;

public class NavigationTreeFactory {

    public static NavigationTree createViewFor(
        final NavigationTreeClickListener listener, final PdpRequest pdpRequest) {
        return new NavigationTreeImpl(listener, pdpRequest).init();
    }

}
