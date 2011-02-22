package de.escidoc.admintool.view;

import de.escidoc.admintool.service.PdpService;
import de.escidoc.admintool.view.navigation.NavigationTree;
import de.escidoc.admintool.view.navigation.NavigationTreeClickListener;
import de.escidoc.admintool.view.navigation.NavigationTreeImpl;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class NavigationTreeFactory {

    public static NavigationTree createViewFor(
        final NavigationTreeClickListener listener,
        final UserAccount currentUser, final PdpService pdpService) {
        return new NavigationTreeImpl(listener, pdpService).init(currentUser);
    }

}
