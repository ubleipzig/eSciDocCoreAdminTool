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
import de.escidoc.admintool.view.context.listener.CancelClickListener;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class OpenContextModalWindow extends VerticalLayout {

    private static final Logger LOG = LoggerFactory.getLogger(OpenContextModalWindow.class);

    private static final long serialVersionUID = -6815586703714340558L;

    private final Window subwindow = new Window(ViewConstants.OPEN);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL, new CancelClickListener(subwindow));

    private final Button submitBtn = new Button(ViewConstants.OPEN, new SubmitOpenContextClickListener());

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

    private class SubmitOpenContextClickListener implements Button.ClickListener {

        private static final long serialVersionUID = -8527938690355384964L;

        public void buttonClick(final ClickEvent event) {
            try {
                final String enteredComment = (String) commentField.getValue();
                assert enteredComment != null;
                contextForm.open(enteredComment);
                subwindow.getParent().removeWindow(subwindow);
            }
            catch (final EscidocException e) {
                LOG.error("An unexpected error occured! See LOG for details.", e);
            }
            catch (final InternalClientException e) {
                LOG.error("An unexpected error occured! See LOG for details.", e);
            }
            catch (final TransportException e) {
                LOG.error("An unexpected error occured! See LOG for details.", e);
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