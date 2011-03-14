package de.escidoc.admintool.view.navigation;

import com.vaadin.ui.Component;

public interface NavigationTree extends Component {

    NavigationTree init();

    void selectUserView();

    boolean isExpanded(Object itemId);

    void expandItem(Object itemId);

    void collapseItem(Object itemId);
}
