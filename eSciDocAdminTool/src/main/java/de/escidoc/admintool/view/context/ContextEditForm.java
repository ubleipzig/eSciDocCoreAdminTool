package de.escidoc.admintool.view.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.terminal.SystemError;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.exception.ResourceNotFoundException;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.OrgUnitEditor;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.om.context.AdminDescriptor;
import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.vaadin.dialog.ErrorDialog;
import de.escidoc.vaadin.utilities.Converter;
import de.escidoc.vaadin.utilities.LayoutHelper;

@SuppressWarnings("serial")
public class ContextEditForm extends CustomComponent implements ClickListener {
    private static final String EDIT_USER_ACCOUNT = "Edit Context";

    private static final Logger log =
        LoggerFactory.getLogger(ContextEditForm.class);

    private final AdminToolApplication app;

    private final ContextService contextService;

    private final OrgUnitService orgUnitService;

    private final List<Field> fields = new ArrayList<Field>();

    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private final TextField nameField = new TextField();

    private Item item;

    private final TextField descriptionField = new TextField();

    private final Label objIdField = new Label();

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

    private final Button addAdminDescButton = new Button("Add");

    private final Button editAdminDescButton = new Button("Edit");

    private final Button delAdminDescButton = new Button("Delete");

    private HorizontalLayout footer;

    private Accordion adminDescriptorAccordion;

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton =
        new Button(ViewConstants.REMOVE_LABEL);

    private final int labelWidth = 140;

    private ContextToolbar editToolbar;

    private ContextListView contextList;

    public ContextEditForm(final AdminToolApplication adminToolApplication,
        final ContextService contextService, final OrgUnitService orgUnitService) {
        app = adminToolApplication;
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
        nameField.setWidth("400px");
        // fields.add(nameField);
        nameField.setWriteThrough(false);
        form.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
            nameField, labelWidth, true));

        // Desc
        descriptionField.setWidth("400px");
        fields.add(descriptionField);
        panel.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL,
            descriptionField, labelWidth, true));

        // objectid
        form.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL,
            objIdField, labelWidth, false));

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
        typeField.setWidth("400px");
        fields.add(typeField);
        form.addComponent(LayoutHelper.create("Type", typeField, labelWidth,
            false));

        // OrgUnit
        orgUnitList.setRows(5);
        orgUnitList.setWidth("400px");
        orgUnitList.setNullSelectionAllowed(true);
        orgUnitList.setMultiSelect(true);
        orgUnitList.setImmediate(true);

        form.addComponent(LayoutHelper.create(
            ViewConstants.ORGANIZATION_UNITS_LABEL, new OrgUnitEditor(
                ViewConstants.ORGANIZATION_UNITS_LABEL, orgUnitList,
                addOrgUnitButton, removeOrgUnitButton, orgUnitService),
            labelWidth, 140, true, new Button[] { addOrgUnitButton,
                removeOrgUnitButton }));
        // AdminDescriptor
        adminDescriptorAccordion = new Accordion();
        adminDescriptorAccordion.setWidth("400px");
        adminDescriptorAccordion.setSizeFull();

        final Panel accordionPanel = new Panel();
        accordionPanel.setContent(adminDescriptorAccordion);
        accordionPanel.setSizeFull();
        accordionPanel.setWidth("400px");

        // final Window mainWindow = (Window) panel.getParent();
        final Window mainWindow = app.getMainWindow();
        addAdminDescButton.addListener(new NewAdminDescriptorListener(
            mainWindow, adminDescriptorAccordion));
        editAdminDescButton.addListener(new EditAdminDescriptorListener(
            mainWindow, adminDescriptorAccordion));
        delAdminDescButton.addListener(new RemoveAdminDescriptorListener(
            adminDescriptorAccordion));

        panel.addComponent(LayoutHelper.create("Admin Descriptors",
            accordionPanel, labelWidth, 300, false, new Button[] {
                addAdminDescButton, editAdminDescButton, delAdminDescButton }));

        // Footer
        panel.addComponent(addFooter());
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
            throw new RuntimeException("Unknown Button " + clickedButton);
        }
    }

    private HorizontalLayout addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);

        footer.addComponent(saveButton);
        footer.addComponent(cancelButton);
        return footer;
    }

    private void setFieldsWriteThrough(final boolean b) {
        for (final Field field : fields) {
            field.setWriteThrough(b);
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

                // final String name =
                // (String) item
                // .getItemProperty(ViewConstants.NAME_ID).getValue();
                // final String desc =
                // (String) item
                // .getItemProperty(PropertyId.DESCRIPTION).getValue();
                // final String type =
                // (String) item.getItemProperty(PropertyId.TYPE).getValue();
                // final Context newContext =
                // contextService.update((String) objIdField.getValue(), name,
                // desc, type, selectedOrgUnitRefs, adminDescriptors);

                final Context newContext =
                    contextService.update((String) objIdField.getValue(),
                        (String) nameField.getValue(),
                        (String) descriptionField.getValue(),
                        (String) typeField.getValue(),
                        // TODO: Replace by real call.
                        selectedOrgUnitRefs, adminDescriptors);

                // commitFields();
                nameField.commit();

                // final Context newContext =
                // contextService.update((String) objIdField.getValue(),
                // (String) nameField.getValue(),
                // (String) descriptionField.getValue(),
                // (String) typeField.getValue(),
                // // TODO: Replace by real call.
                // selectedOrgUnitRefs, adminDescriptors);

                nameField.setComponentError(null);
                descriptionField.setComponentError(null);
                typeField.setComponentError(null);
                orgUnitList.setComponentError(null);
                adminDescriptorAccordion.setComponentError(null);
            }
            catch (final EscidocException e) {
                log.error("root cause: "
                    + ExceptionUtils.getRootCauseMessage(e), e);
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), "Error", e
                        .getMessage()));
                e.printStackTrace();
            }
            catch (final InternalClientException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                setComponentError(new UserError(e.getMessage()));
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), "Error", e
                        .getMessage()));
                e.printStackTrace();
            }
            catch (final TransportException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), "Error", e
                        .getMessage()));
                setComponentError(new UserError(e.getMessage()));
                e.printStackTrace();
            }
        }
    }

    private void commitFields() {
        for (final Field field : fields) {
            field.commit();
        }
    }

    private OrganizationalUnitRefs getEnteredOrgUnitRefs() {
        final OrganizationalUnitRefs organizationalUnitRefs =
            new OrganizationalUnitRefs();

        for (final String objectId : getEnteredOrgUnits()) {
            organizationalUnitRefs.add(new ResourceRef(objectId));
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
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), "Error", e
                        .getMessage()));
                setComponentError(new SystemError(e.getMessage()));
                e.printStackTrace();
            }
            catch (final SAXException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), "Error", e
                        .getMessage()));
                setComponentError(new SystemError(e.getMessage()));
                e.printStackTrace();
            }
            catch (final IOException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), "Error", e
                        .getMessage()));
                setComponentError(new SystemError(e.getMessage()));
                e.printStackTrace();
            }
        }

        return adminDescriptors;
    }

    private void discard() {
        nameField.setValue("");
        nameField.setComponentError(null);
        descriptionField.setValue("");
        descriptionField.setComponentError(null);
        typeField.setValue("");
        typeField.setComponentError(null);
        orgUnitList.removeAllItems();
        orgUnitList.setComponentError(null);
        adminDescriptorAccordion.removeAllComponents();
    }

    @SuppressWarnings("unchecked")
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
        final List<ResourceRef> refs =
            (List<ResourceRef>) item
                .getItemProperty(PropertyId.ORG_UNIT_REFS).getValue();

        for (final ResourceRef resourceRef : refs) {
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
                adminDescriptorAccordion.addTab(new Label(adminDescriptor
                    .getContentAsString(), Label.CONTENT_PREFORMATTED),
                    adminDescriptor.getName(), null);
            }
            catch (final TransformerException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
        }
    }

    private String findOrgUnitTitle(final ResourceRef resourceRef) {
        try {
            return app.getOrgUnitService().findOrgUnitTitleById(
                resourceRef.getObjid());
        }
        catch (final ResourceNotFoundException e) {
            log
                .error("root cause: " + ExceptionUtils.getRootCauseMessage(e),
                    e);
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "Organizational Unit does not exist anymore."
                        + e.getMessage()));
            e.printStackTrace();
        }

        catch (final EscidocException e) {
            log
                .error("root cause: " + ExceptionUtils.getRootCauseMessage(e),
                    e);
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error", e.getMessage()));
            e.printStackTrace();
        }
        catch (final InternalClientException e) {
            log
                .error("root cause: " + ExceptionUtils.getRootCauseMessage(e),
                    e);
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error", e.getMessage()));
            e.printStackTrace();
        }
        catch (final TransportException e) {
            log
                .error("root cause: " + ExceptionUtils.getRootCauseMessage(e),
                    e);
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error", e.getMessage()));
            e.printStackTrace();
        }
        return "Organization does exist, please remove.";
    }

    public Context openContext(final String comment) throws EscidocException,
        InternalClientException, TransportException {
        final Context oldContext =
            contextService.getSelected(getSelectedItemId());
        final Context openedContext =
            contextService.open(getSelectedItemId(), comment);
        contextList.updateContext(oldContext, openedContext);
        editToolbar.setSelected(PublicStatus.OPENED);
        return openedContext;
    }

    public Context closeContext(final String comment) throws EscidocException,
        InternalClientException, TransportException {
        final Context oldContext =
            contextService.getSelected(getSelectedItemId());
        final Context closedContext =
            contextService.close(getSelectedItemId(), comment);
        footer.setVisible(false);
        contextList.updateContext(oldContext, closedContext);

        editToolbar.setSelected(PublicStatus.CLOSED);
        return closedContext;
    }

    private void setFormReadOnly(final boolean isReadOnly) {
        nameField.setReadOnly(isReadOnly);
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
        catch (final EscidocException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final InternalClientException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final TransportException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }

    }
}