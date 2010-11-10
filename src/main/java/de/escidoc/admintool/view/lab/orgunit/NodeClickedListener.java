package de.escidoc.admintool.view.lab.orgunit;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

class NodeClickedListener implements ItemClickListener {

    private static final long serialVersionUID = -8942630039213394944L;

    private final OrgUnitTreeView treeView;

    /**
     * @param orgUnitTreeView
     */
    NodeClickedListener(final OrgUnitTreeView orgUnitTreeView) {
        treeView = orgUnitTreeView;
    }

    @Override
    public void itemClick(final ItemClickEvent event) {
        final Item item = event.getItem();
        if (item == null) {
            return;
        }
        assert treeView.orgUnitViewLab != null : "orgUnitViewLab can not be null";
        treeView.orgUnitViewLab.select(item);
    }
}