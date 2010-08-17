package de.escidoc.admintool.view.context;

import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.OrgUnitEditor;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.vaadin.utilities.Converter;
import de.escidoc.vaadin.utilities.LayoutHelper;

@SuppressWarnings("serial")
public class ContextEditForm extends CustomComponent implements ClickListener {
    private static final String EDIT_USER_ACCOUNT = "Edit Context";

    private final AdminToolApplication app;

    private final ContextService contextService;

    private final OrgUnitService orgUnitService;

    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private final TextField nameField = new TextField();

    private Item item;

    private final Label modifiedOn = new Label();

    private final Label modifiedBy = new Label();

    private final Label createdOn = new Label();

    private final Label createdBy = new Label();

    private final Label status = new Label();

    private final TextField typeField = new TextField();

    private final ListSelect orgUnitList = new ListSelect();

    private Collection<OrganizationalUnit> organizationalUnits;

    private final Button saveButton = new Button("Save", this);

    private final Button cancelButton = new Button("Cancel", this);

    private HorizontalLayout footer;

    private Accordion adminDescriptorAccordion;

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton =
        new Button(ViewConstants.REMOVE_LABEL);

    public ContextEditForm(final AdminToolApplication adminToolApplication,
        final ContextService contextService, final OrgUnitService orgUnitService) {
        app = adminToolApplication;
        this.contextService = contextService;
        this.orgUnitService = orgUnitService;
        init();
    }

    private void init() {
        panel.addComponent(form);
        panel.setCaption(EDIT_USER_ACCOUNT);
        final int labelWidth = 170;
        final int height = 15;

        nameField.setWidth("400px");
        nameField.setWriteThrough(false);
        form.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL,
            nameField, labelWidth, false));

        // modified
        form.addComponent(LayoutHelper.create("Modified", "by", modifiedOn,
            modifiedBy, labelWidth, height, false));

        // created
        form.addComponent(LayoutHelper.create("Created", "by", createdOn,
            createdBy, labelWidth, height, false));

        // Status
        form.addComponent(LayoutHelper.create("Status", status, labelWidth,
            false));

        // Type
        form.addComponent(LayoutHelper.create("Type", typeField, labelWidth,
            false));

        // OrgUnit
        orgUnitList.setRows(5);
        orgUnitList.setWidth("400px");
        orgUnitList.setNullSelectionAllowed(true);
        orgUnitList.setMultiSelect(true);
        orgUnitList.setImmediate(true);
        // form.addComponent(LayoutHelper.create(
        // ViewConstants.ORGANIZATION_UNITS_LABEL, new OrgUnitEditor(
        // orgUnitList), labelWidth, 140, false));
        form.addComponent(LayoutHelper.create(
            ViewConstants.ORGANIZATION_UNITS_LABEL, new OrgUnitEditor(
                ViewConstants.ORGANIZATION_UNITS_LABEL, orgUnitList,
                addOrgUnitButton, removeOrgUnitButton), labelWidth, 140, false,
            new Button[] { addOrgUnitButton, removeOrgUnitButton }));
        // AdminDescriptor
        adminDescriptorAccordion = new Accordion();
        adminDescriptorAccordion.setWidth("400px");
        adminDescriptorAccordion.setSizeFull();

        final Panel accordionPanel = new Panel();
        accordionPanel.setContent(adminDescriptorAccordion);
        accordionPanel.setSizeFull();
        accordionPanel.setWidth("400px");

        final Button addButton = new Button("Add");
        final Button editButton = new Button("Edit");
        final Button delButton = new Button("Delete");
        final Window mainWindow = (Window) panel.getParent();
        addButton.addListener(new NewAdminDescriptorListener(mainWindow,
            adminDescriptorAccordion));
        editButton.addListener(new EditAdminDescriptorListener(mainWindow,
            adminDescriptorAccordion));
        delButton.addListener(new RemoveAdminDescriptorListener(
            adminDescriptorAccordion));

        panel.addComponent(LayoutHelper.create("Admin Descriptors",
            accordionPanel, labelWidth, 300, true, new Button[] { addButton,
                editButton, delButton }));

        // Footer
        panel.addComponent(addFooter());

        setCompositionRoot(panel);
    }

    private HorizontalLayout addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);

        footer.addComponent(saveButton);
        footer.addComponent(cancelButton);
        return footer;
    }

    public void buttonClick(final ClickEvent event) {
        final Button clickedButton = event.getButton();
        if (clickedButton == saveButton) {
            // save();
        }
        else if (clickedButton == cancelButton) {
            // discard();
        }
        else {
            throw new RuntimeException("Unknown Button " + clickedButton);
        }
    }

    public void setSelected(final Item item) {
        this.item = item;
        if (item != null) {
            nameField.setPropertyDataSource(item
                .getItemProperty(ViewConstants.NAME_ID));
            // objIdField.setPropertyDataSource(item.getItemProperty("objid"));
            modifiedOn.setCaption(Converter
                .dateTimeToString((org.joda.time.DateTime) item
                    .getItemProperty("lastModificationDate").getValue()));
            modifiedBy.setPropertyDataSource(item
                .getItemProperty("properties.modifiedBy.objid"));
            createdOn.setCaption(Converter
                .dateTimeToString((org.joda.time.DateTime) item
                    .getItemProperty("properties.creationDate").getValue()));
            createdBy.setPropertyDataSource(item
                .getItemProperty("properties.createdBy.objid"));
        }
    }

    // private AdminDescriptorsEditView adminDescriptorsEditView;
    //
    // private class ContextFieldFactory extends DefaultFieldFactory {
    //
    // @Override
    // public Field createField(
    // final Item item, final Object propertyId, final Component uiContext) {
    // final boolean closed = isClosed(item);
    //
    // if (ViewConstants.ORGANIZATION_UNITS_ID.equals(propertyId)) {
    // return buildUiForOrgUnits(item, closed);
    // }
    //
    // final Field field = super.createField(item, propertyId, uiContext);
    //
    // if (ViewConstants.ADMIN_DESRIPTORS_ID.equals(propertyId)) {
    // adminDescriptorsEditView =
    // new AdminDescriptorsEditView(
    // item.getItemProperty(ViewConstants.ADMIN_DESRIPTORS_ID),
    // closed);
    //
    // return adminDescriptorsEditView;
    // }
    // if (closed) {
    // footer.setVisible(false);
    // }
    // if (ViewConstants.NAME_ID.equals(propertyId)) {
    // final TextField tf = (TextField) field;
    // tf.setWidth("400px");
    //
    // if (closed) {
    // tf.setReadOnly(true);
    // }
    // tf.setRequired(true);
    // tf.setRequiredError("Name is required");
    // tf.addValidator(new EmptyStringValidator("Name is required"));
    // }
    // else if (ViewConstants.DESCRIPTION_ID.equals(propertyId)) {
    // final TextField tf = (TextField) field;
    // tf.setWidth("400px");
    // tf.setRows(3);
    //
    // if (closed) {
    // tf.setReadOnly(true);
    // }
    //
    // tf.setRequired(true);
    // tf.setRequiredError("Description is required");
    //
    // tf.addValidator(new EmptyStringValidator(
    // "Description is required"));
    // }
    // else if (ViewConstants.OBJECT_ID.equals(propertyId)) {
    // final TextField tf = (TextField) field;
    // tf.setReadOnly(true);
    // }
    // else if (ViewConstants.CREATED_ON_ID.equals(propertyId)) {
    // field.setReadOnly(true);
    // field.setWidth("200px");
    // }
    // else if (ViewConstants.CREATED_BY_ID.equals(propertyId)) {
    // final TextField tf = (TextField) field;
    // tf.setReadOnly(true);
    // }
    // else if (ViewConstants.MODIFIED_ON_ID.equals(propertyId)) {
    // field.setReadOnly(true);
    // field.setWidth("200px");
    // }
    // else if (ViewConstants.MODIFIED_BY_ID.equals(propertyId)) {
    // final TextField tf = (TextField) field;
    // tf.setReadOnly(true);
    // }
    // else if (ViewConstants.PUBLIC_STATUS_ID.equals(propertyId)) {
    // final TextField tf = (TextField) field;
    // tf.setReadOnly(true);
    // }
    // else if (ViewConstants.PUBLIC_STATUS_COMMENT_ID.equals(propertyId)) {
    // final TextField tf = (TextField) field;
    // tf.setCaption(ViewConstants.PUBLIC_STATUS_COMMENT_LABEL);
    // field.setWidth("400px");
    // tf.setReadOnly(true);
    // }
    // else if (ViewConstants.TYPE_ID.equals(propertyId)) {
    // final TextField tf = (TextField) field;
    // tf.setWidth("400px");
    //
    // if (closed) {
    // tf.setReadOnly(true);
    // }
    //
    // tf.setRequired(true);
    // tf.setRequiredError("Type is required");
    // tf.setWidth("400px");
    // tf.addValidator(new EmptyStringValidator("Type is required"));
    // }
    //
    // return field;
    // }
    // }
    //
    // private TwinColSelect orgUnitSelectionView;
    //
    // private TwinColSelect buildUiForOrgUnits(
    // final Item item, final boolean closed) {
    //
    // orgUnitSelectionView =
    // new TwinColSelect("Organizations", pojoContainer);
    //
    // orgUnitSelectionView.setItemCaptionPropertyId("properties.name");
    // orgUnitSelectionView.setColumns(25);
    // orgUnitSelectionView.setMultiSelect(true);
    // orgUnitSelectionView.setRequired(true);
    // orgUnitSelectionView.setRequiredError("Organization is required");
    // if (closed) {
    // orgUnitSelectionView.setReadOnly(true);
    // }
    //
    // return orgUnitSelectionView;
    // }
    //
    // private boolean isClosed(final Item item) {
    // return "closed".equals(item.getItemProperty(
    // ViewConstants.PUBLIC_STATUS_ID).getValue());
    // }
    //
    // private HorizontalLayout footer;
    //
    // private final Button save = new Button("Save", this);
    //
    // private final Button cancel = new Button("Cancel", this);
    //
    // private void addFooter() {
    // footer = new HorizontalLayout();
    // footer.setSpacing(true);
    //
    // footer.addComponent(save);
    // footer.addComponent(cancel);
    // footer.setVisible(false);
    //
    // setFooter(footer);
    // }
    //
    //
    // public void showFooter(final boolean isClosed) {
    // footer.setVisible(!isClosed);
    // }
    //
    //
    // private void save() {
    // if (isValid()) {
    // try {
    // final AdminDescriptors adminDescriptors = getAdminDescriptors();
    //
    // for (final AdminDescriptor enteredAdminDesc : adminDescriptors) {
    //
    // System.out.println("name: " + enteredAdminDesc.getName());
    // System.out.println("content: "
    // + enteredAdminDesc.getContent());
    // }
    //
    // contextService.update(getSelectedItemId(),
    // (String) getField(ViewConstants.NAME_ID).getValue(),
    // (String) getField(ViewConstants.DESCRIPTION_ID).getValue(),
    // (String) getField(ViewConstants.TYPE_ID).getValue(),
    // getSelectedOrgUnitRefs(), getAdminDescriptors());
    //
    // commit();
    //
    // adminDescriptorsEditView.commitForm();
    //
    // setComponentError(null);
    //
    // app.getMainWindow().showNotification("Saved");
    // }
    // catch (final EscidocException e) {
    // setComponentError(new UserError(e.getMessage()));
    // e.printStackTrace();
    // }
    // catch (final InternalClientException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // catch (final TransportException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // catch (final ParserConfigurationException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // private String getSelectedItemId() {
    // return (String) getItemDataSource().getItemProperty(
    // ViewConstants.OBJECT_ID).getValue();
    // }
    //
    // private OrganizationalUnitRefs getSelectedOrgUnitRefs() {
    //
    // final OrganizationalUnitRefs organizationalUnitRefs =
    // new OrganizationalUnitRefs();
    // for (final ResourceRef resourceRef : (Collection<ResourceRef>)
    // orgUnitSelectionView
    // .getValue()) {
    // organizationalUnitRefs.add(resourceRef);
    // }
    // return organizationalUnitRefs;
    // }
    //
    // private AdminDescriptors getAdminDescriptors()
    // throws ParserConfigurationException {
    // return adminDescriptorsEditView.getAdminDescriptors();
    // }
    //
    // public Context openContext(final String comment) throws EscidocException,
    // InternalClientException, TransportException {
    // final Context openedContext =
    // contextService.open(getSelectedItemId(), comment);
    // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(false);
    // getField(ViewConstants.PUBLIC_STATUS_ID).setValue("opened");
    // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(true);
    // ((ContextView) getParent().getParent()).updateList(getSelectedItemId());
    //
    // return openedContext;
    // }
    //
    // public Context closeContext() throws EscidocException,
    // InternalClientException, TransportException {
    // final Context closedContext = contextService.close(getSelectedItemId());
    // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(false);
    // getField(ViewConstants.PUBLIC_STATUS_ID).setValue("closed");
    // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(true);
    // footer.setVisible(false);
    // setReadOnly(true);
    // ((ContextView) getParent().getParent()).updateList(getSelectedItemId());
    // return closedContext;
    // }
    //
    // public void deleteContext() throws EscidocException,
    // InternalClientException, TransportException {
    // contextService.delete(getSelectedItemId());
    // }
    //
    // public Context closeContext(final String comment) throws
    // EscidocException,
    // InternalClientException, TransportException {
    // final Context closedContext =
    // contextService.close(getSelectedItemId(), comment);
    // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(false);
    // getField(ViewConstants.PUBLIC_STATUS_ID).setValue("closed");
    // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(true);
    // footer.setVisible(false);
    // setReadOnly(true);
    // ((ContextView) getParent().getParent()).updateList(getSelectedItemId());
    // return closedContext;
    // }
}
