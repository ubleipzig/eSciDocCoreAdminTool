package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

public class ResourceNodeClickedListener implements ItemClickListener {

    private static final long serialVersionUID = -741237723882916675L;

    private final ShowEditResourceView showEditResourceView;

    ResourceNodeClickedListener(final ShowEditResourceView showEditResourceView) {
        Preconditions.checkNotNull(showEditResourceView, "showEditResourceView is null: %s", showEditResourceView);
        this.showEditResourceView = showEditResourceView;
    }

    @Override
    public void itemClick(final ItemClickEvent event) {
        final Item item = event.getItem();
        if (item == null) {
            return;
        }
        showEditResourceView.execute(item);
    }
}
