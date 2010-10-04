package de.escidoc.admintool.view.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class OpenContextModalWindow extends VerticalLayout {

    private static final Logger log = LoggerFactory
        .getLogger(OpenContextModalWindow.class);

    private static final long serialVersionUID = -6815586703714340558L;

    private final Window subwindow = new Window(ViewConstants.OPEN);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL,
        new CancelClickListener(subwindow));

    private final Button submitBtn = new Button(ViewConstants.OPEN,
        new SubmitOpenContextClickListener());

    private final ContextEditForm contextForm;

    private TextField commentField;

    private VerticalLayout layout;

    public OpenContextModalWindow(final ContextEditForm contextForm) {
        this.contextForm = contextForm;
        buildUI();
    }

    private void buildUI() {
        modalWindow().commentTextField().footer();
    }

    private class SubmitOpenContextClickListener
        implements Button.ClickListener {

        private static final long serialVersionUID = -8527938690355384964L;

        public void buttonClick(final ClickEvent event) {
            try {
                final String enteredComment = (String) commentField.getValue();
                assert enteredComment != null;
                contextForm.open(enteredComment);
                subwindow.getParent().removeWindow(subwindow);
            }
            catch (final EscidocException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
            }
            catch (final InternalClientException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
            }
            catch (final TransportException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
            }
        }
    }

    private OpenContextModalWindow footer() {
        final HorizontalLayout footer = new HorizontalLayout();
        footer.addComponent(submitBtn);
        footer.addComponent(cancelBtn);
        layout.addComponent(footer);
        return this;
    }

    private OpenContextModalWindow commentTextField() {
        commentField = new TextField("Comment");
        commentField.setWidth("400px");
        commentField.setRows(3);
        subwindow.addComponent(commentField);
        return this;
    }

    private OpenContextModalWindow modalWindow() {
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