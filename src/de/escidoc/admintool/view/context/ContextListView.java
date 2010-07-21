package de.escidoc.admintool.view.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
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

    private final ContextService service;

    private final OrgUnitService orgUnitService;

    public ContextListView(final AdminToolApplication app,
        final ContextService service, final OrgUnitService orgUnitService)
        throws EscidocException, InternalClientException, TransportException {
        this.app = app;
        this.service = service;
        this.orgUnitService = orgUnitService;
        buildView();
        bindDataSource();
    }

    private void buildView() {
        setSizeFull();
        setColumnCollapsingAllowed(true);
        setColumnReorderingAllowed(true);
        setSelectable(true);
        setImmediate(true);
        addListener((ValueChangeListener) app);
        setNullSelectionAllowed(false);
    }

    private void bindDataSource() throws EscidocException,
        InternalClientException, TransportException {
        addColumnsHeader();
        fillTable(service.all());
        // NOTE: this method should be call only after container property
        // already defined
        setColumnHeaders();
        collapseColumns();
    }

    private void setColumnHeaders() {
        this.setColumnHeaders(new String[] { ViewConstants.NAME_LABEL,
            ViewConstants.DESCRIPTION_LABEL, ViewConstants.OBJECT_ID_LABEL,
            ViewConstants.CREATED_ON_LABEL, ViewConstants.CREATED_BY_LABEL,
            ViewConstants.MODIFIED_ON_LABEL, ViewConstants.MODIFIED_BY_LABEL,
            ViewConstants.PUBLIC_STATUS_LABEL,
            ViewConstants.PUBLIC_STATUS_COMMENT_LABEL,
            ViewConstants.TYPE_LABEL, ViewConstants.ORGANIZATION_UNITS_LABEL,
            ViewConstants.ADMIN_DESRIPTORS_LABEL });
    }

    private void collapseColumns() {
        try {
            setColumnCollapsed(ViewConstants.OBJECT_ID, true);
            setColumnCollapsed(ViewConstants.DESCRIPTION_ID, true);
            setColumnCollapsed(ViewConstants.CREATED_ON_ID, true);
            setColumnCollapsed(ViewConstants.CREATED_BY_ID, true);
            setColumnCollapsed(ViewConstants.MODIFIED_ON_ID, true);
            setColumnCollapsed(ViewConstants.MODIFIED_BY_ID, true);
            setColumnCollapsed(ViewConstants.PUBLIC_STATUS_ID, true);
            setColumnCollapsed(ViewConstants.PUBLIC_STATUS_COMMENT_ID, true);
            setColumnCollapsed(ViewConstants.TYPE_ID, true);
            setColumnCollapsed(ViewConstants.ORGANIZATION_UNITS_ID, true);
            setColumnCollapsed(ViewConstants.ADMIN_DESRIPTORS_ID, true);
        }
        catch (final IllegalAccessException e) {
            // TODO log the exception
            e.printStackTrace();
        }
    }

    private void addColumnsHeader() {
        addContainerProperty(ViewConstants.NAME_ID, String.class, "");
        this.addContainerProperty(ViewConstants.DESCRIPTION_ID, String.class,
            "");
        this.addContainerProperty(ViewConstants.OBJECT_ID, String.class, "");
        this.addContainerProperty(ViewConstants.CREATED_ON_ID, Date.class, "");
        this
            .addContainerProperty(ViewConstants.CREATED_BY_ID, String.class, "");
        this.addContainerProperty(ViewConstants.MODIFIED_ON_ID, Date.class, "");
        this.addContainerProperty(ViewConstants.MODIFIED_BY_ID, String.class,
            "");
        this.addContainerProperty(ViewConstants.PUBLIC_STATUS_ID, String.class,
            "");
        this.addContainerProperty(ViewConstants.PUBLIC_STATUS_COMMENT_ID,
            String.class, "");
        this.addContainerProperty(ViewConstants.TYPE_ID, String.class, "");
        this.addContainerProperty(ViewConstants.ORGANIZATION_UNITS_ID,
            Collection.class, "");
        this.addContainerProperty(ViewConstants.ADMIN_DESRIPTORS_ID,
            Collection.class, "");
    }

    private void fillTable(final Collection<Context> contexts) {
        for (final Context context : contexts) {
            addContext(context);
        }
        sort();
    }

    void addContext(final Context context) {
        assert context != null : "context must not be null.";
        debug(context);
        // assert !containsId(context.getObjid()) :
        // "Context already in the table";

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
        final Context selected = service.getSelected(objectId);
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