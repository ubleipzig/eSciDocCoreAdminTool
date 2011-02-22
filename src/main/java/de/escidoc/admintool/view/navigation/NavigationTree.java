package de.escidoc.admintool.view.navigation;

import com.vaadin.ui.Component;

import de.escidoc.core.resources.aa.useraccount.UserAccount;

public interface NavigationTree extends Component {

    NavigationTree init(UserAccount currentUser);

}
