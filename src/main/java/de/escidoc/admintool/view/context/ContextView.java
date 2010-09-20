package de.escidoc.admintool.view.context;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.exception.ResourceNotFoundException;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.vaadin.dialog.ErrorDialog;

@SuppressWarnings("serial")
public class ContextView extends SplitPanel {
    private final Logger log = LoggerFactory.getLogger(ContextView.class);

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
        try {
            contextEditForm.setSelected(item);
        }
        catch (final ResourceNotFoundException e) {
            log
                .error("root cause: " + ExceptionUtils.getRootCauseMessage(e),
                    e);
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error", e.getMessage()));
            e.printStackTrace();
        }
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
}