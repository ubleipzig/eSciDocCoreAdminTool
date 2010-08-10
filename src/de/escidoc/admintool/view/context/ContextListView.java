package de.escidoc.admintool.view.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.data.util.POJOContainer;
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

    private final AdminToolApplication app;

    private final ContextService contextService;

    private final OrgUnitService orgUnitService;

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
        final POJOContainer<Context> contextContainer =
            new POJOContainer<Context>(contextService.all(),
                PropertyId.OBJECT_ID, PropertyId.NAME,
                PropertyId.PUBLIC_STATUS, PropertyId.PUBLIC_STATUS_COMMENT,
                PropertyId.CREATED_ON, PropertyId.CREATED_BY,
                PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY);
        setContainerDataSource(contextContainer);
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { false });
        setVisibleColumns(new Object[] { PropertyId.NAME });
        setColumnHeader(PropertyId.NAME, ViewConstants.TITLE_LABEL);
    }

    void addContext(final Context context) {
        assert context != null : "context must not be null.";
        debug(context);

        final Object itemId =
            addItem(new Object[] {
                context.getProperties().getName(),
                context.getProperties().getDescription(),
                context.getObjid(),
                context.getProperties().getCreationDate().toDate(),
                context.getProperties().getCreatedBy().getObjid(),
                context.getLastModificationDate().toDate(),
                context.getProperties().getModifiedBy().getObjid(),
                context.getProperties().getPublicStatus(),
                context.getProperties().getPublicStatusComment(),
                context.getProperties().getType(),
                getOrgUnitsByResourceRef(context
                    .getProperties().getOrganizationalUnitRefs()),
                context.getAdminDescriptors() }, context.getObjid());
        assert itemId != null : "Adding context to the list failed.";
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

    public void removeContext(final String objectId) {
        assert objectId != null : "context must not be null.";
        assert containsId(objectId) : "Context not in the list view";

        final Object itemId = removeItem(objectId);

        assert itemId != null : "Removing context to the list failed.";
    }

    public void updateContext(final String objectId) {
        final Context selected = contextService.getSelected(objectId);
        removeContext(selected.getObjid());
        addContext(selected);
        sort(new Object[] { ViewConstants.MODIFIED_ON_ID },
            new boolean[] { false });
        setValue(objectId);
    }

    private void debug(final Context context) {
        System.out.println(context.getProperties().getName()
            + context.getObjid() + context.getProperties().getCreationDate()
            + context.getProperties().getCreatedBy().getObjid()
            + context.getLastModificationDate()
            + context.getProperties().getModifiedBy().getObjid());
    }
}