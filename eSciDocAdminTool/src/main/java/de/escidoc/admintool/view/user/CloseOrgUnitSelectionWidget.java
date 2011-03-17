package de.escidoc.admintool.view.user;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

final class CloseOrgUnitSelectionWidget implements ClickListener {
    /**
     * 
     */
    private final UserAddView userAddView;

    /**
     * @param userAddView
     */
    CloseOrgUnitSelectionWidget(UserAddView userAddView) {
        this.userAddView = userAddView;
    }

    private static final long serialVersionUID = -1514595787473181424L;

    @Override
    public void buttonClick(final ClickEvent event) {
        Preconditions.checkNotNull(this.userAddView.app.getMainWindow(),
            "app.getMainWindow() is null: %s",
            this.userAddView.app.getMainWindow());
        Preconditions.checkNotNull(this.userAddView.modalWindow,
            "modalWindow is null: %s", this.userAddView.modalWindow);
        this.userAddView.app.getMainWindow().removeWindow(
            this.userAddView.modalWindow);
    }
}