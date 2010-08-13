//package de.escidoc.admintool.view.context;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import com.vaadin.data.Item;
//import com.vaadin.data.util.POJOContainer;
//import com.vaadin.terminal.UserError;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Component;
//import com.vaadin.ui.DefaultFieldFactory;
//import com.vaadin.ui.Field;
//import com.vaadin.ui.Form;
//import com.vaadin.ui.GridLayout;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.ListSelect;
//import com.vaadin.ui.TextField;
//import com.vaadin.ui.TwinColSelect;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.ui.Button.ClickListener;
//
//import de.escidoc.admintool.app.AdminToolApplication;
//import de.escidoc.admintool.service.ContextService;
//import de.escidoc.admintool.service.OrgUnitService;
//import de.escidoc.admintool.view.ViewConstants;
//import de.escidoc.admintool.view.validator.StringValidator;
//import de.escidoc.core.client.exceptions.EscidocException;
//import de.escidoc.core.client.exceptions.InternalClientException;
//import de.escidoc.core.client.exceptions.TransportException;
//import de.escidoc.core.resources.ResourceRef;
//import de.escidoc.core.resources.om.context.Context;
//import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;
//import de.escidoc.core.resources.oum.OrganizationalUnit;
//
//@SuppressWarnings("serial")
//public class ContextEditView extends Form implements ClickListener {
//
//    private final AdminToolApplication app;
//
//    private final ContextService contextService;
//
//    private final OrgUnitService orgUnitService;
//
//    public ContextEditView(final AdminToolApplication adminToolApplication,
//        final ContextService contextService, final OrgUnitService orgUnitService) {
//        app = adminToolApplication;
//        this.contextService = contextService;
//        this.orgUnitService = orgUnitService;
//        buildUI();
//    }
//
//    private GridLayout layout;
//
//    private void buildUI() {
//        addFooter();
//        layout = new GridLayout(10, 10);
//        layout.setMargin(true, false, false, true);
//        layout.setSpacing(true);
//        setLayout(layout);
//
//        setWriteThrough(false);
//        setInvalidCommitted(false);
//        // TODO test if it works.
//        setImmediate(true);
//        setValidationVisible(true);
//
//        // TODO populate Org Units
//        try {
//            organizationalUnits = orgUnitService.getOrganizationalUnits();
//        }
//        catch (final EscidocException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        catch (final InternalClientException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        catch (final TransportException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        // FieldFactory for customizing the fields and adding validators
//        setFormFieldFactory(new ContextFieldFactory());
//
//    }
//
//    /*
//     * Override to get control over where fields are placed.
//     */
//    @Override
//    protected void attachField(final Object propertyId, final Field field) {
//        if (propertyId.equals(ViewConstants.NAME_ID)) {
//            layout.addComponent(field, 0, 1);
//        }
//        else if (ViewConstants.DESCRIPTION_ID.equals(propertyId)) {
//            layout.addComponent(field, 0, 3, 3, 3);
//        }
//        else if (ViewConstants.OBJECT_ID.equals(propertyId)) {
//            layout.addComponent(field, 1, 1);
//        }
//        else if (ViewConstants.CREATED_ON_ID.equals(propertyId)) {
//            layout.addComponent(field, 0, 4);
//        }
//        else if (ViewConstants.CREATED_BY_ID.equals(propertyId)) {
//            layout.addComponent(field, 1, 4);
//        }
//        else if (ViewConstants.MODIFIED_ON_ID.equals(propertyId)) {
//            layout.addComponent(field, 0, 5);
//        }
//        else if (ViewConstants.MODIFIED_BY_ID.equals(propertyId)) {
//            layout.addComponent(field, 1, 5);
//        }
//        else if (ViewConstants.PUBLIC_STATUS_ID.equals(propertyId)) {
//            layout.addComponent(field, 0, 6);
//        }
//        else if (ViewConstants.PUBLIC_STATUS_COMMENT_ID.equals(propertyId)) {
//            layout.addComponent(field, 1, 6);
//        }
//        else if (ViewConstants.TYPE_ID.equals(propertyId)) {
//            layout.addComponent(field, 0, 7);
//        }
//        // else if (ViewConstants.ORGANIZATION_UNITS_ID.equals(propertyId)) {
//        // addOrgUnitsView();
//        // }
//    }
//
//    private void addOrgUnitsView() {
//        layout.addComponent(orgUnitSelectionView, 0, 8, 8, 8);
//    }
//
//    private TwinColSelect orgUnitSelectionView;
//
//    private class ContextFieldFactory extends DefaultFieldFactory {
//
//        @Override
//        public Field createField(
//            final Item item, final Object propertyId, final Component uiContext) {
//
//            final Field field = super.createField(item, propertyId, uiContext);
//            final boolean closed = isClosed(item);
//
//            if (closed) {
//                footer.setVisible(false);
//            }
//
//            // TODO fix me: not OOProgamming
//            if (ViewConstants.NAME_ID.equals(propertyId)) {
//                final TextField tf = (TextField) field;
//
//                if (closed) {
//                    tf.setReadOnly(true);
//                }
//
//                tf.setRequired(true);
//                tf.setRequiredError("Name is required");
//                tf.setWidth("200px");
//                tf.addValidator(new StringValidator("Name is required"));
//            }
//            else if (ViewConstants.DESCRIPTION_ID.equals(propertyId)) {
//                final TextField tf = (TextField) field;
//                tf.setWidth("400px");
//
//                if (closed) {
//                    tf.setReadOnly(true);
//                }
//
//                tf.setRequired(true);
//                tf.addValidator(new StringValidator("Description is required"));
//            }
//            else if (ViewConstants.OBJECT_ID.equals(propertyId)) {
//                final TextField tf = (TextField) field;
//                tf.setReadOnly(true);
//            }
//            else if (ViewConstants.CREATED_ON_ID.equals(propertyId)) {
//                field.setReadOnly(true);
//                field.setWidth("200px");
//            }
//            else if (ViewConstants.CREATED_BY_ID.equals(propertyId)) {
//                final TextField tf = (TextField) field;
//                tf.setReadOnly(true);
//            }
//            else if (ViewConstants.MODIFIED_ON_ID.equals(propertyId)) {
//                field.setReadOnly(true);
//                field.setWidth("200px");
//            }
//            else if (ViewConstants.MODIFIED_BY_ID.equals(propertyId)) {
//                final TextField tf = (TextField) field;
//                tf.setReadOnly(true);
//            }
//            else if (ViewConstants.PUBLIC_STATUS_ID.equals(propertyId)) {
//                final TextField tf = (TextField) field;
//                tf.setReadOnly(true);
//            }
//            else if (ViewConstants.PUBLIC_STATUS_COMMENT_ID.equals(propertyId)) {
//                final TextField tf = (TextField) field;
//                field.setWidth("400px");
//                tf.setReadOnly(true);
//            }
//            else if (ViewConstants.TYPE_ID.equals(propertyId)) {
//                final TextField tf = (TextField) field;
//
//                if (closed) {
//                    tf.setReadOnly(true);
//                }
//
//                tf.setRequired(true);
//                tf.setRequiredError("Type is required");
//                tf.setWidth("200px");
//                tf.addValidator(new StringValidator("Type is required"));
//            }
//            else if (ViewConstants.ORGANIZATION_UNITS_ID.equals(propertyId)) {
//                buildUiForOrgUnits(item);
//                // return simpleUiForOrgUnit(item);
//            }
//
//            return field;
//        }
//
//        private ListSelect simpleUiForOrgUnit(final Item item) {
//            final ListSelect orgUnitListSelect =
//                new ListSelect(ViewConstants.ORGANIZATION_UNITS_LABEL);
//            orgUnitListSelect.setRows(5);
//            orgUnitListSelect.setWidth("200px");
//
//            for (final OrganizationalUnit orgUnit : organizationalUnits) {
//                orgUnitListSelect.addItem(orgUnit.getObjid());
//            }
//
//            final Collection<ResourceRef> resourceRefs =
//                (Collection) item.getItemProperty(
//                    ViewConstants.ORGANIZATION_UNITS_ID).getValue();
//            final List<String> objids = new ArrayList<String>();
//            for (final ResourceRef resourceRef : resourceRefs) {
//                objids.add(resourceRef.getObjid());
//                System.out.println("select: " + resourceRef.getObjid());
//                orgUnitListSelect.select(resourceRef.getObjid());
//
//            }
//            layout.addComponent(orgUnitListSelect, 0, 8, 8, 8);
//
//            return orgUnitListSelect;
//        }
//
//        boolean isPaint = false;
//
//        private void buildUiForOrgUnits(final Item item) {
//            final POJOContainer<OrganizationalUnit> pojoContainer =
//                new POJOContainer<OrganizationalUnit>(organizationalUnits,
//                    "objid", "properties.name");
//            orgUnitSelectionView =
//                new TwinColSelect("Organizations", pojoContainer);
//            orgUnitSelectionView.setItemCaptionPropertyId("properties.name");
//            orgUnitSelectionView.setColumns(25);
//            orgUnitSelectionView.setMultiSelect(true);
//            orgUnitSelectionView.setRequired(true);
//            orgUnitSelectionView.setRequiredError("Organization is required");
//            if (!isPaint) {
//                layout.addComponent(orgUnitSelectionView, 0, 8, 8, 8);
//                isPaint = true;
//            }
//            final Collection<ResourceRef> resorceRefs =
//                (Collection) item.getItemProperty(
//                    ViewConstants.ORGANIZATION_UNITS_ID).getValue();
//            final List<String> objids = new ArrayList<String>();
//            for (final ResourceRef ref : resorceRefs) {
//                System.out.println("selected: " + ref.getObjid());
//                objids.add(ref.getObjid());
//            }
//
//            // for (final OrganizationalUnit orgUnit : orgUnitService
//            // .getOrgUnitsByIds(objids)) {
//            // orgUnitSelectionView.select(orgUnit);
//            // }
//            orgUnitSelectionView.setValue(orgUnitService
//                .getOrgUnitsByIds(objids));
//        }
//    }
//
//    private HorizontalLayout footer;
//
//    private final Button save = new Button("Save", (ClickListener) this);
//
//    private final Button cancel = new Button("Cancel", (ClickListener) this);
//
//    private Collection<OrganizationalUnit> organizationalUnits;
//
//    private void addFooter() {
//        footer = new HorizontalLayout();
//        footer.setSpacing(true);
//
//        footer.addComponent(save);
//        footer.addComponent(cancel);
//        footer.setVisible(false);
//
//        setFooter(footer);
//    }
//
//    private boolean isClosed(final Item item) {
//        return "closed".equals(item.getItemProperty(
//            ViewConstants.PUBLIC_STATUS_ID).getValue());
//    }
//
//    public void showFooter(final boolean isClosed) {
//        footer.setVisible(!isClosed);
//    }
//
//    public void setSelected(final Item item) {
//        if (item != getItemDataSource()) {
//            this.setItemDataSource(item);
//        }
//        showFooter(isClosed(item));
//    }
//
//    public void buttonClick(final ClickEvent event) {
//        final Button clickedButton = event.getButton();
//        if (clickedButton == save) {
//            save();
//        }
//        else if (clickedButton == cancel) {
//            discard();
//        }
//        else {
//            throw new RuntimeException("Unknown Button " + clickedButton);
//        }
//    }
//
//    private void save() {
//        if (isValid()) {
//            try {
//                contextService.update(getSelectedItemId(), (String) getField(
//                    ViewConstants.NAME_ID).getValue(), (String) getField(
//                    ViewConstants.DESCRIPTION_ID).getValue(),
//                    (String) getField(ViewConstants.TYPE_ID).getValue(),
//                    getSelectedOrgUnitRefs());
//                commit();
//                app.getMainWindow().showNotification("Saved");
//            }
//            catch (final EscidocException e) {
//                setComponentError(new UserError(e.getMessage()));
//                e.printStackTrace();
//            }
//            catch (final InternalClientException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            catch (final TransportException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private OrganizationalUnitRefs getSelectedOrgUnitRefs() {
//
//        final OrganizationalUnitRefs organizationalUnitRefs =
//            new OrganizationalUnitRefs();
//        for (final ResourceRef resourceRef : (Collection<ResourceRef>) orgUnitSelectionView
//            .getValue()) {
//            organizationalUnitRefs.addOrganizationalUnitRef(resourceRef);
//        }
//        return organizationalUnitRefs;
//    }
//
//    private String getSelectedItemId() {
//        return (String) getItemDataSource().getItemProperty(
//            ViewConstants.OBJECT_ID).getValue();
//    }
//
//    public Context openContext(final String comment) throws EscidocException,
//        InternalClientException, TransportException {
//        final Context openedContext =
//            contextService.open(getSelectedItemId(), comment);
//        getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(false);
//        getField(ViewConstants.PUBLIC_STATUS_ID).setValue("opened");
//        getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(true);
//        ((ContextView) getParent().getParent()).updateList(getSelectedItemId());
//
//        return openedContext;
//    }
//
//    public Context closeContext() throws EscidocException,
//        InternalClientException, TransportException {
//        final Context closedContext = contextService.close(getSelectedItemId());
//        getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(false);
//        getField(ViewConstants.PUBLIC_STATUS_ID).setValue("closed");
//        getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(true);
//        footer.setVisible(false);
//        setReadOnly(true);
//        ((ContextView) getParent().getParent()).updateList(getSelectedItemId());
//        return closedContext;
//    }
//
//    public void deleteContext() throws EscidocException,
//        InternalClientException, TransportException {
//        contextService.delete(getSelectedItemId());
//    }
//
//    public Context closeContext(final String comment) throws EscidocException,
//        InternalClientException, TransportException {
//        final Context closedContext =
//            contextService.close(getSelectedItemId(), comment);
//        getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(false);
//        getField(ViewConstants.PUBLIC_STATUS_ID).setValue("closed");
//        getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(true);
//        footer.setVisible(false);
//        setReadOnly(true);
//        ((ContextView) getParent().getParent()).updateList(getSelectedItemId());
//        return closedContext;
//    }
// }