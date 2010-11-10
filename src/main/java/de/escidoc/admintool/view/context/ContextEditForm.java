package de.escidoc.admintool.view.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.terminal.SystemError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.exception.ResourceNotFoundException;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.OrgUnitEditor;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.admintool.view.util.Converter;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.reference.OrganizationalUnitRef;
import de.escidoc.core.resources.om.context.AdminDescriptor;
import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;

@SuppressWarnings("serial")
public class ContextEditForm extends CustomComponent implements ClickListener {
    private static final Logger log = LoggerFactory
        .getLogger(ContextEditForm.class);

    private static final String EDIT_USER_ACCOUNT = "Edit Context";

    private static final int LABEL_WIDTH = 140;

    private final List<Field> fields = new ArrayList<Field>();

    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private final TextField nameField = new TextField();

    private final TextField descriptionField = new TextField();

    private final Label objIdField = new Label();

    private final Label modifiedOn = new Label();

    private final Label modifiedBy = new Label();

    private final Label createdOn = new Label();

    private final Label createdBy = new Label();

    private final Label status = new Label();

    private final TextField typeField = new TextField();

    private final ListSelect orgUnitList = new ListSelect();

    private final Button saveButton = new Button("Save", this);

    private final Button cancelButton = new Button("Cancel", this);

    private final Button addAdminDescButton = new Button("Add");

    private final Button editAdminDescButton = new Button("Edit");

    private final Button delAdminDescButton = new Button("Delete");

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton = new Button(
        ViewConstants.REMOVE_LABEL);

    private final AdminToolApplication app;

    private final ContextService contextService;

    private final OrgUnitService orgUnitService;

    private HorizontalLayout footer;

    private Accordion adminDescriptorAccordion;

    private ContextToolbar editToolbar;

    private ContextListView contextList;

    private Item item;

    private final Window mainWindow;

    public ContextEditForm(final AdminToolApplication app,
        final Window mainWindow, final ContextService contextService,
        final OrgUnitService orgUnitService) {
        Preconditions.checkNotNull(mainWindow,
            "mainWindow can not be null: %s", mainWindow);
        Preconditions
            .checkNotNull(app, "AdminToolApplication can not be null.");
        Preconditions.checkNotNull(contextService,
            "ContextService can not be null.");
        Preconditions.checkNotNull(orgUnitService,
            "OrgUnitService can not be null.");

        this.app = app;
        this.mainWindow = mainWindow;
        this.contextService = contextService;
        this.orgUnitService = orgUnitService;
        init();
    }

    private void init() {
        panel.setContent(form);
        panel.setCaption(EDIT_USER_ACCOUNT);

        final int height = 15;
        editToolbar = new ContextToolbar(this, app);
        form.addComponent(editToolbar);

        // name
        nameField.setWidth(ViewConstants.FIELD_WIDTH);
        nameField.setWriteThrough(false);
        form.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
            nameField, LABEL_WIDTH, true));

        // Desc
        descriptionField.setWidth(ViewConstants.FIELD_WIDTH);
        descriptionField.setRows(3);
        fields.add(descriptionField);
        form.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL,
            descriptionField, LABEL_WIDTH, true));

        // objectid
        form.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL,
            objIdField, LABEL_WIDTH, false));

        // modified
        form.addComponent(LayoutHelper.create("Modified", "by", modifiedOn,
            modifiedBy, LABEL_WIDTH, height, false));

        // created
        form.addComponent(LayoutHelper.create("Created", "by", createdOn,
            createdBy, LABEL_WIDTH, height, false));

        // Status
        form.addComponent(LayoutHelper.create("Status", status, LABEL_WIDTH,
            false));

        // Type
        typeField.setWidth(ViewConstants.FIELD_WIDTH);
        fields.add(typeField);
        form.addComponent(LayoutHelper.create("Type", typeField, LABEL_WIDTH,
            false));

        // OrgUnit
        orgUnitList.setRows(5);
        orgUnitList.setWidth(ViewConstants.FIELD_WIDTH);
        orgUnitList.setNullSelectionAllowed(true);
        orgUnitList.setMultiSelect(true);
        orgUnitList.setWriteThrough(false);

        form.addComponent(LayoutHelper.create(
            ViewConstants.ORGANIZATION_UNITS_LABEL, new OrgUnitEditor(
                ViewConstants.ORGANIZATION_UNITS_LABEL, orgUnitList,
                addOrgUnitButton, removeOrgUnitButton, orgUnitService),
            LABEL_WIDTH, 140, true, new Button[] { addOrgUnitButton,
                removeOrgUnitButton }));

        // AdminDescriptor
        adminDescriptorAccordion = new Accordion();
        adminDescriptorAccordion.setWidth(ViewConstants.FIELD_WIDTH);
        adminDescriptorAccordion.setSizeFull();

        final Panel accordionPanel = new Panel();
        accordionPanel.setContent(adminDescriptorAccordion);
        accordionPanel.setSizeFull();
        accordionPanel.setWidth(ViewConstants.FIELD_WIDTH);

        addAdminDescButton.addListener(new NewAdminDescriptorListener(
            mainWindow, adminDescriptorAccordion));
        editAdminDescButton.addListener(new EditAdminDescriptorListener(
            mainWindow, adminDescriptorAccordion));
        delAdminDescButton.addListener(new RemoveAdminDescriptorListener(
            adminDescriptorAccordion));

        form.addComponent(LayoutHelper.create("Admin Descriptors",
            accordionPanel, LABEL_WIDTH, 300, false, new Button[] {
                addAdminDescButton, editAdminDescButton, delAdminDescButton }));

        // Footer
        form.addComponent(addFooter());
        setCompositionRoot(panel);
        setFieldsWriteThrough(false);
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Button clickedButton = event.getButton();
        if (clickedButton == saveButton) {
            save();
        }
        else if (clickedButton == cancelButton) {
            discard();
        }
        else {
            throw new IllegalArgumentException("Unknown Button "
                + clickedButton);
        }
    }

    private HorizontalLayout addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);

        footer.addComponent(saveButton);
        footer.addComponent(cancelButton);
        return footer;
    }

    private void setFieldsWriteThrough(final boolean isWriteThrough) {
        for (final Field field : fields) {
            field.setWriteThrough(isWriteThrough);
        }
    }

    private void save() {
        boolean valid = true;
        valid =
            EmptyFieldValidator.isValid(nameField, "Name can not be empty.");
        valid &=
            EmptyFieldValidator.isValid(descriptionField,
                "Description can not be empty.");
        valid &=
            EmptyFieldValidator.isValid(typeField, ViewConstants.TYPE_LABEL
                + " can not be empty.");
        valid &=
            EmptyFieldValidator.isValid(orgUnitList,
                ViewConstants.ORGANIZATION_UNITS_LABEL + " can not be empty");

        if (valid) {
            try {
                final AdminDescriptors adminDescriptors =
                    enteredAdminDescriptors();
                final OrganizationalUnitRefs selectedOrgUnitRefs =
                    getEnteredOrgUnitRefs();

                contextService.update((String) objIdField.getValue(),
                    (String) nameField.getValue(),
                    (String) descriptionField.getValue(),
                    (String) typeField.getValue(),
                    // TODO: Replace by real call.
                    selectedOrgUnitRefs, adminDescriptors);

                nameField.commit();

                nameField.setComponentError(null);
                descriptionField.setComponentError(null);
                typeField.setComponentError(null);
                orgUnitList.setComponentError(null);
                adminDescriptorAccordion.setComponentError(null);
            }
            catch (final EscidocException e) {
                log.error(
                    "root cause: " + ExceptionUtils.getRootCauseMessage(e), e);
                mainWindow.addWindow(new ErrorDialog(mainWindow,
                    ViewConstants.ERROR, e.getMessage()));
            }
            catch (final EscidocClientException e) {
                log.error(
                    "root cause: " + ExceptionUtils.getRootCauseMessage(e), e);
                mainWindow.addWindow(new ErrorDialog(mainWindow,
                    ViewConstants.ERROR, e.getMessage()));
            }
        }
    }

    private OrganizationalUnitRefs getEnteredOrgUnitRefs() {
        final OrganizationalUnitRefs organizationalUnitRefs =
            new OrganizationalUnitRefs();

        for (final String objectId : getEnteredOrgUnits()) {
            organizationalUnitRefs.add(new OrganizationalUnitRef(objectId));
        }

        return organizationalUnitRefs;
    }

    private Set<String> getEnteredOrgUnits() {
        if (orgUnitList.getContainerDataSource() == null
            || orgUnitList.getContainerDataSource().getItemIds() == null
            || orgUnitList.getContainerDataSource().getItemIds().size() == 0
            || !orgUnitList
                .getContainerDataSource().getItemIds().iterator().hasNext()) {
            return Collections.emptySet();
        }

        final ResourceRefDisplay orgUnit =
            (ResourceRefDisplay) orgUnitList
                .getContainerDataSource().getItemIds().iterator().next();
        final Set<String> orgUnits = new HashSet<String>() {

            {
                add(orgUnit.getObjectId());
            }
        };

        return orgUnits;

    }

    private AdminDescriptors enteredAdminDescriptors() {
        final AdminDescriptors adminDescriptors = new AdminDescriptors();
        final Iterator<Component> it =
            adminDescriptorAccordion.getComponentIterator();
        while (it != null && it.hasNext()) {
            final Component contentComp = it.next();
            final Tab tab = adminDescriptorAccordion.getTab(contentComp);
            final String adminDescName = tab.getCaption();
            String adminDescContent = "";
            if (contentComp instanceof Label) {
                adminDescContent = ((String) ((Label) contentComp).getValue());
            }
            final AdminDescriptor adminDescriptor = new AdminDescriptor();
            adminDescriptor.setName(adminDescName);
            try {
                adminDescriptor.setContent(adminDescContent);
                adminDescriptors.add(adminDescriptor);
                // TODO: move to appropriate class
            }
            catch (final ParserConfigurationException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                mainWindow.addWindow(new ErrorDialog(mainWindow,
                    ViewConstants.ERROR, e.getMessage()));
                setComponentError(new SystemError(e.getMessage()));
            }
            catch (final SAXException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                mainWindow.addWindow(new ErrorDialog(mainWindow,
                    ViewConstants.ERROR, e.getMessage()));
                setComponentError(new SystemError(e.getMessage()));
            }
            catch (final IOException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                mainWindow.addWindow(new ErrorDialog(mainWindow,
                    ViewConstants.ERROR, e.getMessage()));
                setComponentError(new SystemError(e.getMessage()));
            }
        }

        return adminDescriptors;
    }

    private void discard() {
        nameField.discard();
        nameField.setComponentError(null);
        descriptionField.discard();
        descriptionField.setComponentError(null);
        typeField.discard();
        typeField.setComponentError(null);
        orgUnitList.discard();
        orgUnitList.setComponentError(null);
        adminDescriptorAccordion.removeAllComponents();
    }

    public void setSelected(final Item item) {
        this.item = item;
        if (item == null) {
            return;
        }
        bindData();
    }

    @SuppressWarnings("unchecked")
    private void bindData() {
        final PublicStatus publicStatus =
            PublicStatus.valueOf(((String) item.getItemProperty(
                PropertyId.PUBLIC_STATUS).getValue()).toUpperCase());
        switch (publicStatus) {
            case CREATED: {
                setFormReadOnly(false);
                footer.setVisible(true);
                break;
            }
            case OPENED: {
                setFormReadOnly(false);
                break;
            }
            case CLOSED: {
                setFormReadOnly(true);
                footer.setVisible(false);
                break;
            }
        }
        nameField.setPropertyDataSource(item
            .getItemProperty(ViewConstants.NAME_ID));
        final Property objectIdProperty =
            item.getItemProperty(PropertyId.OBJECT_ID);
        objIdField.setPropertyDataSource(objectIdProperty);
        status.setPropertyDataSource(item
            .getItemProperty(PropertyId.PUBLIC_STATUS));
        modifiedOn.setCaption(Converter
            .dateTimeToString((org.joda.time.DateTime) item.getItemProperty(
                PropertyId.LAST_MODIFICATION_DATE).getValue()));
        modifiedBy.setPropertyDataSource(item
            .getItemProperty(PropertyId.MODIFIED_BY));
        createdOn.setCaption(Converter
            .dateTimeToString((org.joda.time.DateTime) item.getItemProperty(
                PropertyId.CREATED_ON).getValue()));
        createdBy.setPropertyDataSource(item
            .getItemProperty(PropertyId.CREATED_BY));
        typeField.setPropertyDataSource(item.getItemProperty(PropertyId.TYPE));
        descriptionField.setPropertyDataSource(item
            .getItemProperty(PropertyId.DESCRIPTION));
        editToolbar.setSelected(publicStatus);

        orgUnitList.removeAllItems();
        final List<OrganizationalUnitRef> refs =
            (List<OrganizationalUnitRef>) item.getItemProperty(
                PropertyId.ORG_UNIT_REFS).getValue();

        for (final OrganizationalUnitRef resourceRef : refs) {
            final String orgUnitTitle = findOrgUnitTitle(resourceRef);
            final ResourceRefDisplay resourceRefDisplay =
                new ResourceRefDisplay(resourceRef.getObjid(), orgUnitTitle);
            orgUnitList.addItem(resourceRefDisplay);
        }

        adminDescriptorAccordion.removeAllComponents();

        final List<AdminDescriptor> adminDescriptors =
            (List<AdminDescriptor>) item.getItemProperty(
                PropertyId.ADMIN_DESCRIPTORS).getValue();

        for (final AdminDescriptor adminDescriptor : adminDescriptors) {
            try {
                adminDescriptorAccordion.addTab(
                    new Label(adminDescriptor.getContentAsString(),
                        Label.CONTENT_PREFORMATTED), adminDescriptor.getName(),
                    null);
            }
            catch (final TransformerException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
            }
        }
    }

    private String findOrgUnitTitle(final OrganizationalUnitRef resourceRef) {
        try {
            return orgUnitService.findOrgUnitTitleById(resourceRef.getObjid());
        }
        catch (final ResourceNotFoundException e) {
            log
                .error("root cause: " + ExceptionUtils.getRootCauseMessage(e),
                    e);
            mainWindow
                .addWindow(new ErrorDialog(mainWindow, ViewConstants.ERROR,
                    "Organizational Unit does not exist anymore."
                        + e.getMessage()));
        }

        catch (final EscidocException e) {
            log
                .error("root cause: " + ExceptionUtils.getRootCauseMessage(e),
                    e);
            mainWindow.addWindow(new ErrorDialog(mainWindow,
                ViewConstants.ERROR, e.getMessage()));
        }
        catch (final InternalClientException e) {
            log
                .error("root cause: " + ExceptionUtils.getRootCauseMessage(e),
                    e);
            mainWindow.addWindow(new ErrorDialog(mainWindow,
                ViewConstants.ERROR, e.getMessage()));
        }
        catch (final TransportException e) {
            log
                .error("root cause: " + ExceptionUtils.getRootCauseMessage(e),
                    e);
            mainWindow.addWindow(new ErrorDialog(mainWindow,
                ViewConstants.ERROR, e.getMessage()));

        }
        return "Organization does exist, please remove.";
    }

    public Context open(final String comment) throws EscidocException,
        InternalClientException, TransportException {
        final Context cachedContext = getContextFromCache();
        final Context openedContext = openContext(comment);
        updateContextList(cachedContext, openedContext);
        updateEditView(PublicStatus.OPENED);
        return openedContext;
    }

    private Context getContextFromCache() {
        return contextService.getSelected(getSelectedItemId());
    }

    private Context openContext(final String comment) throws EscidocException,
        InternalClientException, TransportException {
        return contextService.open(getSelectedItemId(), comment);
    }

    private void updateContextList(
        final Context before, final Context openedContext) {
        contextList.updateContext(before, openedContext);
    }

    private void updateEditView(final PublicStatus publicStatus) {
        item.getItemProperty(PropertyId.PUBLIC_STATUS).setValue(publicStatus);
        editToolbar.setSelected(publicStatus);
        setSelected(item);
    }

    public Context close(final String comment) throws EscidocException,
        InternalClientException, TransportException {
        final Context cachedContext = getContextFromCache();
        final Context closedContext = closeContext(comment);
        updateContextList(cachedContext, closedContext);
        updateEditView(PublicStatus.CLOSED);

        footer.setVisible(false);
        return closedContext;
    }

    private Context closeContext(final String comment) throws EscidocException,
        InternalClientException, TransportException {
        return contextService.close(getSelectedItemId(), comment);
    }

    private void setFormReadOnly(final boolean isReadOnly) {
        nameField.setReadOnly(isReadOnly);
        descriptionField.setReadOnly(isReadOnly);
        typeField.setReadOnly(isReadOnly);
        orgUnitList.setReadOnly(isReadOnly);
        adminDescriptorAccordion.setReadOnly(isReadOnly);

        addOrgUnitButton.setVisible(!isReadOnly);
        removeOrgUnitButton.setVisible(!isReadOnly);
        addAdminDescButton.setVisible(!isReadOnly);
        editAdminDescButton.setVisible(!isReadOnly);
        delAdminDescButton.setVisible(!isReadOnly);
    }

    private String getSelectedItemId() {
        return (String) item
            .getItemProperty(ViewConstants.OBJECT_ID).getValue();
    }

    public void setContextList(final ContextListView contextList) {
        this.contextList = contextList;
    }

    public void deleteContext() {
        try {
            final Context selected =
                contextService.getSelected(getSelectedItemId());
            contextService.delete(getSelectedItemId());
            contextList.removeContext(selected);
            app.showContextView();
        }
        catch (final EscidocClientException e) {
            log.error("An unexpected error occured! See log for details.", e);
            mainWindow.addWindow(new ErrorDialog(mainWindow,
                ViewConstants.ERROR, e.getMessage()));
        }
    }
}