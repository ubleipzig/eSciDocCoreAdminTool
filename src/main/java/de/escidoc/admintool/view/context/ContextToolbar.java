/**
 * 
 */
package de.escidoc.admintool.view.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.context.workflow.AbstractState;
import de.escidoc.admintool.view.navigation.ActionIdConstants;

/**
 * @author ASP
 * 
 */
public class ContextToolbar extends CustomComponent {
    private static final long serialVersionUID = -4522925443822439322L;

    private final Logger LOG = LoggerFactory.getLogger(ContextToolbar.class);

    private final HorizontalLayout header = new HorizontalLayout();

    private final Button newContextBtn = new Button("New",
        new NewContextListener());

    private final Button deleteContextBtn = new Button("Delete",
        new DeleteContextListener());

    private final Button openContextBtn = new Button("Open",
        new OpenContextListener());

    private final Button closeContextBtn = new Button("Close",
        new CloseContextListener());

    private final ContextEditForm contextEditForm;

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    private String selectedItemId;

    public ContextToolbar(final ContextEditForm contextEditForm,
        final AdminToolApplication app, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(contextEditForm,
            "contextEditForm can not be null.");
        Preconditions.checkNotNull(app, "app can not be null.");
        Preconditions.checkNotNull(pdpRequest, "pdpRequest can not be null.");

        this.contextEditForm = contextEditForm;
        this.app = app;
        this.pdpRequest = pdpRequest;
    }

    void init() {
        header.setMargin(true);
        header.setSpacing(true);
        header.addComponent(newContextBtn);
        header.addComponent(openContextBtn);
        header.addComponent(closeContextBtn);
        header.addComponent(deleteContextBtn);
        header.setVisible(true);
        setCompositionRoot(header);
    }

    // TODO refactor this to Map and factory pattern.
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
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final IllegalAccessException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final IllegalArgumentException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final InvocationTargetException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final ClassNotFoundException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final SecurityException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final NoSuchMethodException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
    }

    private class OpenContextListener implements Button.ClickListener {
        @Override
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow().addWindow(
                new OpenContextModalWindow(contextEditForm).getSubWindow());
        }
    }

    private class CloseContextListener implements Button.ClickListener {
        @Override
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow().addWindow(
                new CloseContextModalWindow(contextEditForm).getSubWindow());

        }
    }

    private class DeleteContextListener implements Button.ClickListener {
        @Override
        public void buttonClick(final ClickEvent event) {
            contextEditForm.deleteContext();
        }
    }

    private class NewContextListener implements Button.ClickListener {
        @Override
        public void buttonClick(final ClickEvent event) {
            app.getContextView().showAddView();
        }
    }

    public void bind(final String selectedItemId) {
        this.selectedItemId = selectedItemId;
        if (isCreateNotAllowed()) {
            newContextBtn.setVisible(false);
        }
        if (isOpenNotAllowed()) {
            openContextBtn.setVisible(false);
        }
        if (isCloseContextNotAllowed()) {
            closeContextBtn.setVisible(false);
        }
        if (isDeleteNotAllowed()) {
            deleteContextBtn.setVisible(false);
        }
    }

    private boolean isOpenNotAllowed() {
        return pdpRequest.isDenied(ActionIdConstants.OPEN_CONTEXT,
            selectedItemId);
    }

    private boolean isDeleteNotAllowed() {
        return pdpRequest.isDenied(ActionIdConstants.DELETE_CONTEXT,
            selectedItemId);
    }

    private boolean isCloseContextNotAllowed() {
        return pdpRequest.isDenied(ActionIdConstants.CLOSE_CONTEXT,
            selectedItemId);
    }

    private boolean isCreateNotAllowed() {
        return pdpRequest.isDenied(ActionIdConstants.CREATE_CONTEXT,
            selectedItemId);
    }
}