package de.escidoc.admintool.view.user;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.view.resource.ResourceRefDisplay;

final class RemoveSeletecteOrgUnitListener implements ClickListener {
    /**
     * 
     */
    private final UserAddView userAddView;

    /**
     * @param userAddView
     */
    RemoveSeletecteOrgUnitListener(UserAddView userAddView) {
        this.userAddView = userAddView;
    }

    private static final long serialVersionUID = -1514595787473181424L;

    @Override
    public void buttonClick(final ClickEvent event) {

        for (final ResourceRefDisplay toBeRemoved : this.userAddView.orgUnitWidget.getSelectedOrgUnits()) {
            removeFromTable(toBeRemoved);
        }
    }

    private boolean removeFromTable(final ResourceRefDisplay toBeRemoved) {
        return this.userAddView.orgUnitWidget.getTable().removeItem(toBeRemoved);
    }
}