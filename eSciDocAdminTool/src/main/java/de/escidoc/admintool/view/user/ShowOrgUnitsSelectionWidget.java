package de.escidoc.admintool.view.user;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

final class ShowOrgUnitsSelectionWidget implements Button.ClickListener {
    /**
     * 
     */
    private final UserAddView userAddView;

    /**
     * @param userAddView
     */
    ShowOrgUnitsSelectionWidget(UserAddView userAddView) {
        this.userAddView = userAddView;
    }

    private static final long serialVersionUID = -1514595787473181424L;

    @Override
    public void buttonClick(final ClickEvent event) {
        this.userAddView.showModalWindow();
    }
}