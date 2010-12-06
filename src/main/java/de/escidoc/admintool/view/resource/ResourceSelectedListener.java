package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import de.escidoc.admintool.app.PropertyId;

final class ResourceSelectedListener implements ItemClickListener {

    private static final long serialVersionUID = 7022982222058387053L;

    private String selectedParent;

    ModalWindow modalWindow;

    ResourceSelectedListener(final ModalWindow modalWindow) {
        this.modalWindow = modalWindow;
    }

    public void itemClick(final ItemClickEvent event) {
        final Item item = event.getItem();
        if (item == null) {
            return;
        }
        modalWindow.setSelected(getSelectedParent(item));
    }

    private String getSelectedParent(final Item item) {
        final String selectedParent =
            (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
        return selectedParent;
    }
}