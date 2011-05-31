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
package de.escidoc.admintool.view.contentmodel;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.EscidocPagedTable;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

@SuppressWarnings("serial")
public class ContentModelListViewImpl extends CustomComponent implements ContentModelListView {

    private final EscidocPagedTable pagedTable = new EscidocPagedTable();

    private final ContentModelSelectListener listener;

    private final ContentModelContainerImpl contentModelContainerImpl;

    public ContentModelListViewImpl(final ContentModelContainerImpl contentModelContainerImpl,
        final ContentModelSelectListener listener) {
        Preconditions.checkNotNull(contentModelContainerImpl, "contentModelContainer is null: %s",
            contentModelContainerImpl);
        Preconditions.checkNotNull(listener, "listener is null: %s", listener);
        this.contentModelContainerImpl = contentModelContainerImpl;
        this.listener = listener;
    }

    public void init() throws EscidocClientException {
        setCompositionRoot(pagedTable);
        pagedTable.setSizeFull();
        pagedTable.setSelectable(true);
        pagedTable.setImmediate(true);
        pagedTable.setNullSelectionAllowed(false);
        contentModelContainerImpl.reload();
        pagedTable.setContainerDataSource(contentModelContainerImpl.getDataSource());
        pagedTable.setVisibleColumns(new Object[] { PropertyId.X_LINK_TITLE });
        pagedTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
        pagedTable.addListener(listener);
    }

    @Override
    public void setContentModelView(final ContentModelView view) {
        Preconditions.checkNotNull(view, "view is null: %s", view);
        listener.setContentModelView(view);
    }

    @Override
    public void setContentModel(final Resource contentModel) {
        Preconditions.checkNotNull(contentModel, "contentModel is null: %s", contentModel);
        pagedTable.select(contentModel);
    }

    @Override
    public void selectFirstItem() {
        final Object firstItemId = pagedTable.firstItemId();
        pagedTable.select(firstItemId);
    }

    @Override
    public Resource firstItemId() {
        return (Resource) pagedTable.firstItemId();
    }

    @Override
    public Item firstItem() {
        return pagedTable.getItem(pagedTable.firstItemId());
    }

    @Override
    public Item getItem(final Resource contentModel) {
        Preconditions.checkNotNull(contentModel, "contentModel is null: %s", contentModel);
        Preconditions.checkNotNull(pagedTable, "table is null: %s", pagedTable);
        return pagedTable.getItem(contentModel);
    }

    @Override
    public Component createControls() {
        return pagedTable.createControls();
    }
}