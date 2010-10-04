package de.escidoc.admintool.view.context;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.exception.ResourceNotFoundException;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ResourceView;
import de.escidoc.admintool.view.ViewConstants;

@SuppressWarnings("serial")
public class ContextView extends SplitPanel implements ResourceView {
    private final Logger log = LoggerFactory.getLogger(ContextView.class);

    private final ContextListView contextList;

    private final ContextEditForm contextEditForm;

    private final AdminToolApplication app;

    public ContextView(final AdminToolApplication app,
        final ContextListView contextList,
        final ContextEditForm contextEditForm,
        final ContextAddView contextAddView) {
        Preconditions.checkNotNull(app, "App can not be null");
        Preconditions.checkNotNull(contextList, "contextList can not be null");
        Preconditions.checkNotNull(contextEditForm,
            "contextEditForm can not be null");
        Preconditions.checkNotNull(contextAddView,
            "contextAddView can not be null");

        this.app = app;
        this.contextList = contextList;
        this.contextEditForm = contextEditForm;

        addStyleName("view");
        setFirstComponent(contextList);
        setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);
        setSecondComponent(new Label("Place holder for creating new context."));
    }

    public ContextListView getContextList() {
        return contextList;
    }

    public void showAddView() {
        setSecondComponent(app.newContextAddView());
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
            ErrorMessage.show(app.getMainWindow(), e);
        }
    }

    public Item getSelectedItem() {
        return contextList.getItem(contextList.getValue());
    }
}