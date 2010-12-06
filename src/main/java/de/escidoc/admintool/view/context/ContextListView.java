package de.escidoc.admintool.view.context;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.data.util.POJOItem;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.om.context.Context;

public class ContextListView extends Table {

    private static final long serialVersionUID = -4845557717329696071L;

    private final Logger LOG = LoggerFactory.getLogger(ContextListView.class);

    private final AdminToolApplication app;

    private final ContextService contextService;

    private POJOContainer<Context> contextContainer;

    private Collection<Context> allContexts;

    public ContextListView(final AdminToolApplication app,
        final ContextService contextService) throws EscidocException,
        InternalClientException, TransportException {
        checkForNull(app, contextService);
        this.app = app;
        this.contextService = contextService;
        buildView();
        findAllContexts();
        bindDataSource();
    }

    private void checkForNull(
        final AdminToolApplication app, final ContextService contextService) {
        Preconditions.checkNotNull(app, " app can not be null: %s", app);
        Preconditions.checkNotNull(contextService,
            " contextService can not be null: %s", contextService);
    }

    private void buildView() {
        setSizeFull();
        setSelectable(true);
        setImmediate(true);
        addListener(new ContextSelectListener(app));
        setNullSelectionAllowed(false);
    }

    private void bindDataSource() {
        if (isContextExist()) {
            initContextContainer();
        }
    }

    private boolean isContextExist() {
        return allContexts != null && !allContexts.isEmpty();
    }

    private void initContextContainer() {
        contextContainer =
            new POJOContainer<Context>(allContexts, PropertyId.OBJECT_ID,
                PropertyId.NAME, PropertyId.DESCRIPTION,
                PropertyId.PUBLIC_STATUS, PropertyId.PUBLIC_STATUS_COMMENT,
                PropertyId.TYPE, PropertyId.CREATED_ON, PropertyId.CREATED_BY,
                PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
                PropertyId.ORG_UNIT_REFS, PropertyId.ADMIN_DESCRIPTORS);
        setContainerDataSource(contextContainer);
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { false });
        setVisibleColumns(new Object[] { PropertyId.NAME });
        setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
    }

    private void findAllContexts() {
        try {
            allContexts = contextService.findAll();
        }
        catch (final EscidocException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See LOG for details."));
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final InternalClientException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See LOG for details."));
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final TransportException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See LOG for details."));
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
    }

    public POJOItem<Context> addContext(final Context context) {
        assert context != null : "context must not be null.";
        if (contextContainer == null) {
            findAllContexts();
            initContextContainer();
        }
        final POJOItem<Context> addedItem = contextContainer.addItem(context);
        assert addedItem != null : "Adding context to the list failed.";
        sort();
        return addedItem;
    }

    @Override
    public void sort() {
        sort(new Object[] { ViewConstants.MODIFIED_ON_ID },
            new boolean[] { false });
    }

    public void removeContext(final Context selected) {
        assert selected != null : "context must not be null.";
        assert contextContainer.containsId(selected) : "Context not in the list view";

        final Object itemId = contextContainer.removeItem(selected);

        assert itemId != null : "Removing context to the list failed.";
    }

    public void updateContext(final Context oldContext, final Context newContext) {
        removeContext(oldContext);
        addContext(newContext);
        sort(new Object[] { ViewConstants.MODIFIED_ON_ID },
            new boolean[] { false });
        setValue(newContext);
    }
}