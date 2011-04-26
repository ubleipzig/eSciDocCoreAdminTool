package de.escidoc.admintool.view.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.listener.CancelClickListener;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class CloseContextModalWindow extends VerticalLayout {
    private static final long serialVersionUID = -2916506925078723208L;

    private static final Logger LOG = LoggerFactory.getLogger(CloseContextModalWindow.class);

    private final Window subwindow = new Window("Close Context");

    private final Button submitBtn = new Button(ViewConstants.CLOSE, new SubmitCloseContextClickListener());

    private final Button cancelBtn = new Button(ViewConstants.CANCEL, new CancelClickListener(subwindow));

    private final ContextEditForm contextForm;

    private TextField commentField;

    private VerticalLayout layout;

    public CloseContextModalWindow(final ContextEditForm contextForm) {
        this.contextForm = contextForm;
        buildUI();
    }

    private void buildUI() {
        modalWindow().commentTextField().footer();
    }

    private class SubmitCloseContextClickListener implements Button.ClickListener {
        private static final long serialVersionUID = 5642227677682855700L;

        public void buttonClick(final ClickEvent event) {
            try {
                contextForm.close((String) commentField.getValue());
                subwindow.getParent().removeWindow(subwindow);
            }
            catch (final EscidocClientException e) {
                subwindow.getParent().removeWindow(subwindow);
                ErrorMessage.show(contextForm.getApplication().getMainWindow(), e);
            }
        }
    }

    private CloseContextModalWindow footer() {
        final HorizontalLayout footer = new HorizontalLayout();
        footer.addComponent(submitBtn);
        footer.addComponent(cancelBtn);
        layout.addComponent(footer);
        return this;
    }

    private CloseContextModalWindow commentTextField() {
        commentField = new TextField("Comment");
        commentField.setWidth("400px");
        commentField.setRows(3);
        subwindow.addComponent(commentField);
        return this;
    }

    private CloseContextModalWindow modalWindow() {
        subwindow.setModal(true);
        subwindow.setWidth(ViewConstants.MODAL_WINDOW_WIDTH);
        subwindow.setHeight(ViewConstants.MODAL_WINDOW_HEIGHT);
        layout = (VerticalLayout) subwindow.getContent();
        layout.setMargin(true);
        layout.setSpacing(true);
        return this;
    }

    public Window getSubWindow() {
        return subwindow;
    }
}