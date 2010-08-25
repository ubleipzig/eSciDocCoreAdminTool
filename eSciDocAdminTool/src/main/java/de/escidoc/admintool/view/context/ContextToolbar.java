/**
 * 
 */
package de.escidoc.admintool.view.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.context.workflow.AbstractState;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

/**
 * @author ASP
 * 
 */
public class ContextToolbar extends CustomComponent {
    private static final long serialVersionUID = -4522925443822439322L;

    private final Logger log = LoggerFactory.getLogger(ContextToolbar.class);

    private final HorizontalLayout header = new HorizontalLayout();

    private final Button newContextBtn =
        new Button("New", new NewContextListener());

    private final Button deleteContextBtn =
        new Button("Delete", new DeleteContextListener());

    private final Button openContextBtn =
        new Button("Open", new OpenContextListener());

    private final Button closeContextBtn =
        new Button("Close", new CloseContextListener());

    private final ContextEditForm parent;

    private final AdminToolApplication app;

    public ContextToolbar(final ContextEditForm parent,
        final AdminToolApplication app) {
        this.parent = parent;
        this.app = app;
        header.setMargin(true);
        header.setSpacing(true);
        header.addComponent(newContextBtn);
        header.addComponent(openContextBtn);
        header.addComponent(closeContextBtn);
        header.addComponent(deleteContextBtn);
        header.setVisible(true);
        setCompositionRoot(header);
    }

    public void setSelected(final PublicStatus publicStatus) {
        final Class<?>[] buttonArgsClass =
            new Class<?>[] { Button.class, Button.class, Button.class };
        final Object[] buttonArgs =
            new Object[] { deleteContextBtn, openContextBtn, closeContextBtn };
        try {
            final String path =
                "de.escidoc.admintool.view.context.workflow."
                    + publicStatus.toString() + "State";

            final Class<?> c = Class.forName(path);
            final Constructor<?> constructor =
                c.getConstructor(buttonArgsClass);
            final Object object = constructor.newInstance(buttonArgs);
            final AbstractState workflowState = (AbstractState) object;
            workflowState.changeState();
        }
        catch (final InstantiationException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final IllegalAccessException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final IllegalArgumentException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final InvocationTargetException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final ClassNotFoundException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final SecurityException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final NoSuchMethodException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
    }

    private class OpenContextListener implements Button.ClickListener {
        @Override
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow().addWindow(
                new OpenContextModalWindow(parent).getSubWindow());
        }
    }

    private class CloseContextListener implements Button.ClickListener {
        @Override
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow().addWindow(
                new CloseContextModalWindow(parent).getSubWindow());

        }
    }

    private class DeleteContextListener implements Button.ClickListener {
        @Override
        public void buttonClick(final ClickEvent event) {
            parent.deleteContext();
        }
    }

    private class NewContextListener implements Button.ClickListener {
        @Override
        public void buttonClick(final ClickEvent event) {
            try {
                app.getContextView().showContextAddView();
            }
            catch (final EscidocException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            catch (final InternalClientException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            catch (final TransportException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
        }
    }

}
