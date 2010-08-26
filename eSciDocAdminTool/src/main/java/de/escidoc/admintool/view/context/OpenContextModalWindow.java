package de.escidoc.admintool.view.context;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class OpenContextModalWindow extends VerticalLayout {
    private static final long serialVersionUID = -6815586703714340558L;

    private Window subwindow;

    private final ContextEditForm contextForm;

    @SuppressWarnings("serial")
    private final Button cancelBtn =
        new Button("Cancel", new Button.ClickListener() {

            public void buttonClick(final ClickEvent event) {
                ((Window) subwindow.getParent()).removeWindow(subwindow);
            }
        });

    final Button submitBtn =
        new Button("Open Context", new SubmitOpenContextClickListener());

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

        public void buttonClick(final ClickEvent event) {
            try {
                final String enteredComment = (String) commentField.getValue();
                assert enteredComment != null;
                contextForm.openContext(enteredComment);
                ((Window) subwindow.getParent()).removeWindow(subwindow);
            }
            catch (final EscidocException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final InternalClientException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final TransportException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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