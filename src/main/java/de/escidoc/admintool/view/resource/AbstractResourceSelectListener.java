package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;

import de.escidoc.admintool.view.context.ResourceSelectListener;

public abstract class AbstractResourceSelectListener
    implements ResourceSelectListener {

    private static final long serialVersionUID = -3920068730830520162L;

    @Override
    public void itemClick(final ItemClickEvent event) {
        final Item item = event.getItem();

        final ResourceView view = getView();
        if (item == null) {
            view.showAddView();
        }
        else {
            view.showEditView(item);
        }
    }

    public abstract ResourceView getView();

}
