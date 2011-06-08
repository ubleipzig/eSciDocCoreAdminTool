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
package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.core.resources.Resource;

public class OrgUnitTreeView extends CustomComponent implements ResourceFolderView {

    private static final long serialVersionUID = 6912762184225745680L;

    private final VerticalLayout treeLayout = new VerticalLayout();

    private final Tree tree = new Tree();

    private final ResourceContainer resourceContainer;

    private final FolderHeader header;

    private final Window mainWindow;

    private ShowEditResourceView showEditResourceView;

    private AddChildrenCommand addChildrenCommand;

    public OrgUnitTreeView(final Window mainWindow, final FolderHeader header, final ResourceContainer resourceContainer) {
        preconditions(mainWindow, header, resourceContainer);
        this.mainWindow = mainWindow;
        this.header = header;
        this.resourceContainer = resourceContainer;
        init();
    }

    private void preconditions(
        final Window mainWindow, final FolderHeader header, final ResourceContainer resourceContainer) {
        Preconditions.checkNotNull(resourceContainer, " containeris null: %s", resourceContainer);
    }

    private void init() {
        setCompositionRoot(treeLayout);

        treeLayout.addComponent(header);
        treeLayout.addComponent(tree);
        setDataSource();

    }

    @Override
    public void select(final Resource resource) {
        tree.select(resource);
    }

    public void addResourceNodeExpandListener() {
        tree.addListener(new ResourceNodeExpandListener(tree, mainWindow, addChildrenCommand));
    }

    public void addResourceNodeClickedListener() {
        tree.addListener(new ResourceNodeClickedListener(showEditResourceView));
    }

    public void addListener(final ItemClickListener itemClickListener) {
        tree.addListener(itemClickListener);
    }

    private void setDataSource() {
        tree.setContainerDataSource(resourceContainer.getContainer());
        tree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        tree.setItemCaptionPropertyId(PropertyId.NAME);
    }

    public void setEditView(final ShowEditResourceView showEditResourceView) {
        this.showEditResourceView = showEditResourceView;
    }

    public void setCommand(final AddChildrenCommand addChildrenCommand) {
        this.addChildrenCommand = addChildrenCommand;
    }

    public Object getSelected() {
        return tree.getValue();
    }

    public void multiSelect() {
        tree.setMultiSelect(true);
    }

    public void select(final Object firstResource) {
        tree.select(firstResource);
    }
}