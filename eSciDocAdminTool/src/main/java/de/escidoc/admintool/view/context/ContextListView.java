package de.escidoc.admintool.view.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.POJOContainer;
import com.vaadin.data.util.POJOItem;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.oum.OrganizationalUnit;

@SuppressWarnings("serial")
public class ContextListView extends Table {

    private final Logger log = LoggerFactory.getLogger(ContextListView.class);

    private final AdminToolApplication app;

    private final ContextService contextService;

    private final OrgUnitService orgUnitService;

    private POJOContainer<Context> contextContainer;

    public ContextListView(final AdminToolApplication app,
        final ContextService service, final OrgUnitService orgUnitService)
        throws EscidocException, InternalClientException, TransportException {
        this.app = app;
        contextService = service;
        this.orgUnitService = orgUnitService;
        buildView();
        bindDataSource();
    }

    private void buildView() {
        setSizeFull();
        setColumnCollapsingAllowed(false);
        setColumnReorderingAllowed(false);

        setSelectable(true);
        setImmediate(true);
        addListener((ValueChangeListener) app);
        setNullSelectionAllowed(false);
    }

    private void bindDataSource() throws EscidocException,
        InternalClientException, TransportException {
        contextContainer =
            new POJOContainer<Context>(contextService.findAll(),
                PropertyId.OBJECT_ID, PropertyId.NAME, PropertyId.DESCRIPTION,
                PropertyId.PUBLIC_STATUS, PropertyId.PUBLIC_STATUS_COMMENT,
                PropertyId.TYPE, PropertyId.CREATED_ON, PropertyId.CREATED_BY,
                PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
                PropertyId.ORG_UNIT_REFS, PropertyId.ADMIN_DESCRIPTORS);
        setContainerDataSource(contextContainer);
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { false });
        setVisibleColumns(new Object[] { PropertyId.NAME });
        setColumnHeader(PropertyId.NAME, ViewConstants.TITLE_LABEL);
    }

    void addContext(final Context context) {
        assert context != null : "context must not be null.";
        final POJOItem<Context> addedItem = contextContainer.addItem(context);
        assert addedItem != null : "Adding context to the list failed.";
        sort();
    }

    private Collection<OrganizationalUnit> getOrgUnitsByResourceRef(
        final Collection<ResourceRef> resourceRef) {
        final List<String> objectIds = new ArrayList<String>();

        for (final ResourceRef ref : resourceRef) {
            objectIds.add(ref.getObjid());
        }

        return orgUnitService.getOrgUnitsByIds(objectIds);
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

    private void debug(final Context context) {
        log.info(context.getProperties().getName() + context.getObjid()
            + context.getProperties().getCreationDate()
            + context.getProperties().getCreatedBy().getObjid()
            + context.getLastModificationDate()
            + context.getProperties().getModifiedBy().getObjid());
    }
}