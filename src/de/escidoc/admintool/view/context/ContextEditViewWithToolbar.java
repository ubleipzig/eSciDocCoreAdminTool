package de.escidoc.admintool.view.context;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

@SuppressWarnings("serial")
public class ContextEditViewWithToolbar extends VerticalLayout {

    private final ContextEditForm contextEditForm;

    private HorizontalLayout header;

    public ContextEditViewWithToolbar(final ContextEditForm contextEditForm) {
        this.contextEditForm = contextEditForm;
        buildUI();
    }

    private void buildUI() {
        addComponent(createHeader());
        addComponent(contextEditForm);
    }

    private final Button newContextBtn =
        new Button("New", new NewContextListener());

    private final Button deleteContextBtn =
        new Button("Delete", new DeleteContextListener());

    private final Button openContextBtn =
        new Button("Open", new OpenContextListener());

    private final Button closeContextBtn =
        new Button("Close", new CloseContextListener());

    private HorizontalLayout createHeader() {
        header = new HorizontalLayout();
        header.setMargin(true);
        header.setSpacing(true);
        header.addComponent(newContextBtn);
        header.addComponent(deleteContextBtn);

        header.addComponent(openContextBtn);
        header.addComponent(closeContextBtn);
        header.setVisible(true);
        return header;
    }

    public void setSelected(final Item item) {
        final Property itemProperty =
            item.getItemProperty(ViewConstants.PUBLIC_STATUS_ID);
        final String publicStatus = (String) itemProperty.getValue();
        if ("created".equals(publicStatus)) {
            openContextBtn.setVisible(true);
            closeContextBtn.setVisible(false);
            deleteContextBtn.setVisible(true);
        }
        else if ("opened".equals(publicStatus)) {
            openContextBtn.setVisible(false);
            closeContextBtn.setVisible(true);
            deleteContextBtn.setVisible(false);
        }
        else if ("closed".equals(publicStatus)) {
            openContextBtn.setVisible(false);
            closeContextBtn.setVisible(false);
            deleteContextBtn.setVisible(false);
        }
        contextEditForm.setSelected(item);
    }

    private class OpenContextListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            getWindow().addWindow(
                new OpenContextModalWindow(contextEditForm).getSubWindow());
        }
    }

    private class CloseContextListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            getWindow().addWindow(
                new CloseContextModalWindow(contextEditForm).getSubWindow());

        }
    }

    private class DeleteContextListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            try {
                contextEditForm.deleteContext();
                ((ContextView) getParent()).remove();
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

    private class NewContextListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            ((ContextView) getParent()).showContextAddView();
        }
    }
}