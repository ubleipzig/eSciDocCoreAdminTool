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
package de.escidoc.admintool.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

public class EscidocPagedTable extends Table {

    interface PageChangeListener {
        public void pageChanged(PagedTableChangeEvent event);
    }

    public class PagedTableChangeEvent {

        final EscidocPagedTable table;

        public PagedTableChangeEvent(final EscidocPagedTable escidocPagedTable) {
            table = escidocPagedTable;
        }

        public EscidocPagedTable getTable() {
            return table;
        }

        public int getCurrentPage() {
            return table.getCurrentPage();
        }

        public int getTotalAmountOfPages() {
            return table.getTotalAmountOfPages();
        }
    }

    private static final long serialVersionUID = 6881455780158545828L;

    // first item shown in the view for the moment
    private int index = 0;

    private List<PageChangeListener> listeners = null;

    // Real container
    private Container.Indexed realContainer;

    private IndexedContainer shownContainer = new IndexedContainer();

    public EscidocPagedTable() {
        this(null);
    }

    public EscidocPagedTable(final String caption) {
        super(caption);
        setPageLength(50);
        addStyleName("pagedtable");
    }

    public HorizontalLayout createControls() {
        // final Label itemsPerPageLabel = new Label("Items per page:");
        final Label pageLabel = new Label("Page:&nbsp;", Label.CONTENT_XHTML);
        final TextField currentPageTextField = new TextField();
        currentPageTextField.setValue(String.valueOf(getCurrentPage()));
        currentPageTextField.addValidator(new IntegerValidator(null));
        final Label separatorLabel = new Label("&nbsp;/&nbsp;", Label.CONTENT_XHTML);
        final Label totalPagesLabel = new Label(String.valueOf(getTotalAmountOfPages()), Label.CONTENT_XHTML);
        currentPageTextField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        currentPageTextField.setImmediate(true);
        currentPageTextField.addListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;

            public void valueChange(final com.vaadin.data.Property.ValueChangeEvent event) {
                if (currentPageTextField.isValid() && currentPageTextField.getValue() != null) {
                    final int page = Integer.valueOf(String.valueOf(currentPageTextField.getValue()));
                    setCurrentPage(page);
                }
            }
        });
        pageLabel.setWidth(null);
        currentPageTextField.setWidth("20px");
        separatorLabel.setWidth(null);
        totalPagesLabel.setWidth(null);

        final HorizontalLayout controlBar = new HorizontalLayout();
        final HorizontalLayout pageSize = new HorizontalLayout();
        final HorizontalLayout pageManagement = new HorizontalLayout();
        final Button first = new Button("<<", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            public void buttonClick(final ClickEvent event) {
                setCurrentPage(0);
            }
        });
        final Button previous = new Button("<", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            public void buttonClick(final ClickEvent event) {
                previousPage();
            }
        });
        final Button next = new Button(">", new ClickListener() {
            private static final long serialVersionUID = -1927138212640638452L;

            public void buttonClick(final ClickEvent event) {
                nextPage();
            }
        });
        final Button last = new Button(">>", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            public void buttonClick(final ClickEvent event) {
                setCurrentPage(getTotalAmountOfPages());
            }
        });
        first.setStyleName(Reindeer.BUTTON_LINK);
        previous.setStyleName(Reindeer.BUTTON_LINK);
        next.setStyleName(Reindeer.BUTTON_LINK);
        last.setStyleName(Reindeer.BUTTON_LINK);

        pageLabel.addStyleName("pagedtable-pagecaption");
        currentPageTextField.addStyleName("pagedtable-pagefield");
        separatorLabel.addStyleName("pagedtable-separator");
        totalPagesLabel.addStyleName("pagedtable-total");
        first.addStyleName("pagedtable-first");
        previous.addStyleName("pagedtable-previous");
        next.addStyleName("pagedtable-next");
        last.addStyleName("pagedtable-last");

        pageLabel.addStyleName("pagedtable-label");
        currentPageTextField.addStyleName("pagedtable-label");
        separatorLabel.addStyleName("pagedtable-label");
        totalPagesLabel.addStyleName("pagedtable-label");
        first.addStyleName("pagedtable-button");
        previous.addStyleName("pagedtable-button");
        next.addStyleName("pagedtable-button");
        last.addStyleName("pagedtable-button");

        pageSize.setSpacing(true);
        pageManagement.addComponent(first);
        pageManagement.addComponent(previous);
        pageManagement.addComponent(pageLabel);
        pageManagement.addComponent(currentPageTextField);
        pageManagement.addComponent(separatorLabel);
        pageManagement.addComponent(totalPagesLabel);
        pageManagement.addComponent(next);
        pageManagement.addComponent(last);
        pageManagement.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(previous, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(pageLabel, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(currentPageTextField, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(separatorLabel, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(totalPagesLabel, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(next, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(last, Alignment.MIDDLE_LEFT);
        pageManagement.setWidth(null);
        pageManagement.setSpacing(true);
        controlBar.addComponent(pageSize);
        controlBar.addComponent(pageManagement);
        controlBar.setComponentAlignment(pageManagement, Alignment.MIDDLE_CENTER);
        controlBar.setWidth("100%");
        controlBar.setExpandRatio(pageSize, 1);
        addListener(new PageChangeListener() {
            public void pageChanged(final PagedTableChangeEvent event) {
                previous.setEnabled(true);
                next.setEnabled(true);
                currentPageTextField.setValue(String.valueOf(getCurrentPage()));
                totalPagesLabel.setValue(getTotalAmountOfPages());
            }
        });
        return controlBar;
    }

    @Override
    public Container.Indexed getContainerDataSource() {
        return realContainer;
    }

    @Override
    public void setContainerDataSource(final Container newDataSource) {
        if (!(newDataSource instanceof Container.Indexed)) {
            throw new IllegalArgumentException("PagedTable can only use containers that implement Container.Indexed");
        }
        final Container.Indexed realContainer = (Container.Indexed) newDataSource;
        this.realContainer = realContainer;
        shownContainer = new IndexedContainer();
        for (final Object object : realContainer.getContainerPropertyIds()) {
            shownContainer.addContainerProperty(object, realContainer.getType(object), null);
        }
        fillVisibleContainer(0);
        super.setContainerDataSource(shownContainer);
    }

    private void setPageFirstIndex(int firstIndex) {
        if (realContainer != null) {
            if (firstIndex <= 0) {
                firstIndex = 0;
            }
            if (firstIndex > realContainer.size() - 1) {
                final int size = realContainer.size() - 1;
                int pages = 0;
                if (getPageLength() != 0) {
                    pages = (int) Math.floor(0.0 + size / getPageLength());
                }
                firstIndex = pages * getPageLength();
            }
            shownContainer.removeListener((Container.ItemSetChangeListener) this);
            fillVisibleContainer(firstIndex);
            shownContainer.addListener((Container.ItemSetChangeListener) this);
            containerItemSetChange(new Container.ItemSetChangeEvent() {
                private static final long serialVersionUID = -5083660879306951876L;

                public Container getContainer() {
                    return shownContainer;
                }
            });
            if (alwaysRecalculateColumnWidths) {
                for (final Object columnId : shownContainer.getContainerPropertyIds()) {
                    setColumnWidth(columnId, -1);
                }
            }
        }
    }

    private void fillVisibleContainer(final int firstIndex) {
        shownContainer.removeAllItems();
        if (realContainer.size() != 0) {
            Object itemId = realContainer.getIdByIndex(firstIndex);
            addShownItem(itemId);
            for (int i = 1; i < getPageLength(); i++) {
                itemId = realContainer.nextItemId(itemId);
                if (itemId == null) {
                    break;
                }
                addShownItem(itemId);
            }
        }
        index = firstIndex;
        if (listeners != null) {
            final PagedTableChangeEvent event = new PagedTableChangeEvent(this);
            for (final PageChangeListener listener : listeners) {
                listener.pageChanged(event);
            }
        }
    }

    private void addShownItem(final Object itemId) {
        final Item realItem = realContainer.getItem(itemId);
        final Item shownItem = shownContainer.addItem(itemId);
        for (final Object property : realContainer.getContainerPropertyIds()) {
            shownItem.getItemProperty(property).setValue(realItem.getItemProperty(property).getValue());
        }
    }

    @Override
    public void setPageLength(final int pageLength) {
        if (pageLength >= 0 && getPageLength() != pageLength) {
            super.setPageLength(pageLength);
            setPageFirstIndex(index);
        }
    }

    public void nextPage() {
        setPageFirstIndex(index + getPageLength());
    }

    public void previousPage() {
        setPageFirstIndex(index - getPageLength());
    }

    public int getCurrentPage() {
        final double pageLength = getPageLength();
        int page = (int) Math.floor(index / pageLength) + 1;
        if (page < 1) {
            page = 1;
        }
        return page;
    }

    public void setCurrentPage(final int page) {
        int newIndex = (page - 1) * getPageLength();
        if (newIndex < 0) {
            newIndex = 0;
        }
        if (newIndex >= 0 && newIndex != index) {
            setPageFirstIndex(newIndex);
        }
    }

    public int getTotalAmountOfPages() {
        final int size = realContainer.size();
        final double pageLength = getPageLength();
        int pageCount = (int) Math.ceil(size / pageLength);
        if (pageCount < 1) {
            pageCount = 1;
        }
        return pageCount;
    }

    public void addListener(final PageChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<PageChangeListener>();
        }
        listeners.add(listener);
    }

    public void removeListener(final PageChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<PageChangeListener>();
        }
        listeners.remove(listener);
    }

    @Override
    public void sort(final Object[] propertyId, final boolean[] ascending) throws UnsupportedOperationException {
        if (realContainer instanceof Container.Sortable) {
            ((Container.Sortable) realContainer).sort(propertyId, ascending);
        }
        else if (realContainer != null) {
            throw new UnsupportedOperationException("Underlying Data does not allow sorting");
        }
        setPageFirstIndex(index);
    }

    public void setAlwaysRecalculateColumnWidths(final boolean alwaysRecalculateColumnWidths) {
        this.alwaysRecalculateColumnWidths = alwaysRecalculateColumnWidths;
    }

}
