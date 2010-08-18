/**
 * 
 */
package de.escidoc.admintool.view.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.context.workflow.AbstractState;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

/**
 * @author ASP
 * 
 */
public class EditToolbar extends CustomComponent {
    private static final long serialVersionUID = -4522925443822439322L;

    private final Logger log = LoggerFactory.getLogger(EditToolbar.class);

    private final HorizontalLayout header = new HorizontalLayout();

    private final Button newContextBtn = new Button("New",
        new NewContextListener());

    private final Button deleteContextBtn = new Button("Delete",
        new DeleteContextListener());

    private final Button openContextBtn = new Button("Open",
        new OpenContextListener());

    private final Button closeContextBtn = new Button("Close",
        new CloseContextListener());

    private final ContextEditForm parent;

    private final AdminToolApplication app;

    public EditToolbar(ContextEditForm parent, AdminToolApplication app) {
        this.parent = parent;
        this.app = app;
        header.setMargin(true);
        header.setSpacing(true);
        header.addComponent(this.newContextBtn);
        header.addComponent(this.deleteContextBtn);
        header.addComponent(this.openContextBtn);
        header.addComponent(this.closeContextBtn);
        header.setVisible(true);
        setCompositionRoot(header);
    }

    public void setSelected(final PublicStatus publicStatus) {
        final Class<?>[] buttonArgsClass =
            new Class<?>[] { Button.class, Button.class, Button.class,
                Button.class };
        Object[] buttonArgs =
            new Object[] { this.newContextBtn, this.deleteContextBtn,
                this.openContextBtn, this.closeContextBtn };
        try {
            final Class<?> c =
                Class.forName(AbstractState.class.getPackage() + "."
                    + publicStatus.toString() + "State");
            Constructor<?> constructor = c.getConstructor(buttonArgsClass);
            Object object = constructor.newInstance(buttonArgs);
            final AbstractState workflowState = (AbstractState) object;
            workflowState.changeState();
        }
        catch (InstantiationException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (SecurityException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }

    }

    private class OpenContextListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow().addWindow(
                new OpenContextModalWindow(parent).getSubWindow());
        }
    }

    private class CloseContextListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow().addWindow(
                new CloseContextModalWindow(parent).getSubWindow());

        }
    }

    private class DeleteContextListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            // try {
            // // contextEditForm.deleteContext();
            // ((ContextView) getParent()).remove();
            // }
            // catch (final EscidocException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // catch (final InternalClientException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // catch (final TransportException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }
    }

    private class NewContextListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            try {
                app.getContextView().showContextAddView();
            }
            catch (EscidocException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            catch (InternalClientException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            catch (TransportException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
        }
    }

}
