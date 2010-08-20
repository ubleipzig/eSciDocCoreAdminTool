package de.escidoc.admintool.view.context;

import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;

@SuppressWarnings("serial")
public class ContextView extends SplitPanel {

    private final ContextListView contextList;

    private final ContextEditForm contextEditForm;

    private final ContextAddView contextAddView;

    private final AdminToolApplication app;

    public ContextView(final AdminToolApplication app,
        final ContextListView contextList,
        final ContextEditForm editContextWithToolbarView,
        final ContextAddView contextAddView) {
        addStyleName("view");
        this.app = app;
        this.contextList = contextList;
        contextEditForm = editContextWithToolbarView;
        this.contextAddView = contextAddView;

        setFirstComponent(contextList);
        setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);
        setSecondComponent(new Label("Place holder for creating new context."));
    }

    public ContextListView getContextList() {
        return contextList;
    }

    public ContextView showContextAddView() {
        setSecondComponent(app.newContextAddView());
        return this;
    }

    public void showEditView(final Item item) {
        setSecondComponent(contextEditForm);
        contextEditForm.setSelected(item);
    }

    public void remove() {
        System.out.println("removing: "
            + (String) getSelectedItem().getItemProperty(
                ViewConstants.OBJECT_ID).getValue());
        // contextList.removeContext(getSelectedItemId());
    }

    public Item getSelectedItem() {
        return contextList.getItem(contextList.getValue());
    }

    private String getSelectedItemId() {
        return (String) getSelectedItem().getItemProperty(
            ViewConstants.OBJECT_ID).getValue();
    }

    public void updateList(final String objectId) {
        contextList.updateContext(objectId);
    }
}