/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
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
import de.escidoc.admintool.domain.PublicStatus;
import de.escidoc.admintool.view.context.workflow.AbstractState;
import de.escidoc.admintool.view.navigation.ActionIdConstants;

/**
 * @author ASP
 * 
 */
public class ContextToolbar extends CustomComponent {
    private static final String CLOSE = "Close";

    private static final String OPEN = "Open";

    private static final String DELETE = "Delete";

    private static final String NEW = "New";

    private static final long serialVersionUID = -4522925443822439322L;

    private final Logger LOG = LoggerFactory.getLogger(ContextToolbar.class);

    private final HorizontalLayout header = new HorizontalLayout();

    private final Button newContextBtn = new Button(NEW, new NewContextListener());

    private final Button deleteContextBtn = new Button(DELETE, new DeleteContextListener());

    private final Button openContextBtn = new Button(OPEN, new OpenContextListener());

    private final Button closeContextBtn = new Button(CLOSE, new CloseContextListener());

    private final ContextEditForm contextEditForm;

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    private String selectedItemId;

    public ContextToolbar(final ContextEditForm contextEditForm, final AdminToolApplication app,
        final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(contextEditForm, "contextEditForm can not be null.");
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
        final Class<?>[] buttonArgsClass = new Class<?>[] { Button.class, Button.class, Button.class };
        final Object[] buttonArgs = new Object[] { deleteContextBtn, openContextBtn, closeContextBtn };
        try {
            final String path = "de.escidoc.admintool.view.context.workflow." + publicStatus.toString() + "State";

            final Class<?> c = Class.forName(path);
            final Constructor<?> constructor = c.getConstructor(buttonArgsClass);
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
            app.getMainWindow().addWindow(new OpenContextModalWindow(contextEditForm).getSubWindow());
        }
    }

    private class CloseContextListener implements Button.ClickListener {
        @Override
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow().addWindow(new CloseContextModalWindow(contextEditForm).getSubWindow());

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
        return pdpRequest.isDenied(ActionIdConstants.OPEN_CONTEXT, selectedItemId);
    }

    private boolean isDeleteNotAllowed() {
        return pdpRequest.isDenied(ActionIdConstants.DELETE_CONTEXT, selectedItemId);
    }

    private boolean isCloseContextNotAllowed() {
        return pdpRequest.isDenied(ActionIdConstants.CLOSE_CONTEXT, selectedItemId);
    }

    private boolean isCreateNotAllowed() {
        return pdpRequest.isDenied(ActionIdConstants.CREATE_CONTEXT);
    }
}
