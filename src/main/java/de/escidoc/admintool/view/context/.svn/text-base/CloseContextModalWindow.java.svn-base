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

@SuppressWarnings("serial")
public class CloseContextModalWindow extends VerticalLayout {

    private Window subwindow;

    private final ContextEditForm contextForm;

    private final Button cancelBtn =
        new Button("Cancel", new Button.ClickListener() {

            public void buttonClick(final ClickEvent event) {
                ((Window) subwindow.getParent()).removeWindow(subwindow);
            }
        });

    final Button submitBtn =
        new Button("Close Context", new SubmitCloseContextClickListener());

    private TextField commentField;

    private VerticalLayout layout;

    public CloseContextModalWindow(final ContextEditForm contextForm) {
        this.contextForm = contextForm;
        buildUI();
    }

    private void buildUI() {
        modalWindow().commentTextField().footer();
    }

    private class SubmitCloseContextClickListener
        implements Button.ClickListener {

        public void buttonClick(final ClickEvent event) {
            try {
                contextForm.closeContext((String) commentField.getValue());
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
        subwindow = new Window("Close Context");
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
