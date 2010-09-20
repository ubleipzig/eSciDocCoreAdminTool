package de.escidoc.admintool.view.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class OpenContextModalWindow extends VerticalLayout {

    private static final Logger log = LoggerFactory
        .getLogger(OpenContextModalWindow.class);

    private static final long serialVersionUID = -6815586703714340558L;

    private Window subwindow;

    private final ContextEditForm contextForm;

    final Button submitBtn = new Button("Open Context",
        new SubmitOpenContextClickListener());

    private TextField commentField;

    private VerticalLayout layout;

    @SuppressWarnings("serial")
    private final Button cancelBtn = new Button("Cancel",
        new Button.ClickListener() {

            public void buttonClick(final ClickEvent event) {
                (subwindow.getParent()).removeWindow(subwindow);
            }
        });

    public OpenContextModalWindow(final ContextEditForm contextForm) {
        this.contextForm = contextForm;
        buildUI();
    }

    private void buildUI() {
        modalWindow().commentTextField().footer();
    }

    private class SubmitOpenContextClickListener
        implements Button.ClickListener {

        public void buttonClick(final ClickEvent event) {
            try {
                final String enteredComment = (String) commentField.getValue();
                assert enteredComment != null;
                contextForm.openContext(enteredComment);
                (subwindow.getParent()).removeWindow(subwindow);
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
        subwindow = new Window("Open Context");
        subwindow.setModal(true);
        subwindow.setWidth("450px");
        subwindow.setHeight("300px");

        layout = (VerticalLayout) subwindow.getContent();
        layout.setMargin(true);
        layout.setSpacing(true);
        return this;
    }

    public Window getSubWindow() {
        return subwindow;
    }
}