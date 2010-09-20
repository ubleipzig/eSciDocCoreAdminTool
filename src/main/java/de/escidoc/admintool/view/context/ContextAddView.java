package de.escidoc.admintool.view.context;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.terminal.SystemError;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.OrgUnitEditor;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.om.context.AdminDescriptor;
import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;
import de.escidoc.vaadin.dialog.ErrorDialog;
import de.escidoc.vaadin.utilities.LayoutHelper;

public class ContextAddView extends CustomComponent implements ClickListener {
    private static final long serialVersionUID = 1100228979605484119L;

    private final Logger log = LoggerFactory.getLogger(ContextAddView.class);

    private static final int LABEL_WIDTH = 140;

    private final Button save = new Button("Save", this);

    private final Button cancel = new Button("Cancel", this);

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton = new Button(
        ViewConstants.REMOVE_LABEL);

    private final AdminToolApplication app;

    private final ContextListView contextListView;

    private final ContextService contextService;

    private final OrgUnitService orgUnitService;

    private TextField nameField;

    private TextField descriptionField;

    private TextField typeField;

    private HorizontalLayout footer;

    private Accordion adminDescriptorAccordion;

    private ListSelect orgUnitList;

    public ContextAddView(final AdminToolApplication app,
        final ContextListView contextListView,
        final ContextService contextService, final OrgUnitService orgUnitService) {
        this.app = app;
        this.contextListView = contextListView;
        this.contextService = contextService;
        this.orgUnitService = orgUnitService;
        init();
    }

    private ObjectProperty mapBinding(final String initText, final TextField tf) {
        final ObjectProperty op = new ObjectProperty(initText, String.class);
        tf.setPropertyDataSource(op);
        return op;
    }

    private void init() {
        final Panel panel = new Panel();
        final FormLayout form = new FormLayout();
        panel.setContent(form);
        form.setSpacing(false);
        panel.setCaption("Add a new Context");
        nameField = new TextField();
        nameField.setWidth("400px");
        panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
            nameField, LABEL_WIDTH, true));
        final ObjectProperty nameProperty = mapBinding("", nameField);

        descriptionField = new TextField();
        descriptionField.setWidth("400px");
        descriptionField.setRows(3);
        panel.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL,
            descriptionField, LABEL_WIDTH, 80, true));
        final ObjectProperty descriptionProperty =
            mapBinding("", descriptionField);

        typeField = new TextField();
        typeField.setWidth("400px");
        panel.addComponent(LayoutHelper.create(ViewConstants.TYPE_LABEL,
            typeField, LABEL_WIDTH, true));
        final ObjectProperty typeProperty = mapBinding("", typeField);

        orgUnitList = new ListSelect();
        orgUnitList.setRows(5);
        orgUnitList.setWidth("400px");
        orgUnitList.setNullSelectionAllowed(true);
        orgUnitList.setMultiSelect(true);
        orgUnitList.setImmediate(true);

        form.addComponent(LayoutHelper.create(
            ViewConstants.ORGANIZATION_UNITS_LABEL, new OrgUnitEditor(
                ViewConstants.ORGANIZATION_UNITS_LABEL, orgUnitList,
                addOrgUnitButton, removeOrgUnitButton, orgUnitService),
            LABEL_WIDTH, 140, true, new Button[] { addOrgUnitButton,
                removeOrgUnitButton }));

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

        addButton.addListener(new NewAdminDescriptorListener(app
            .getMainWindow(), adminDescriptorAccordion));
        editButton.addListener(new EditAdminDescriptorListener(app
            .getMainWindow(), adminDescriptorAccordion));
        delButton.addListener(new RemoveAdminDescriptorListener(
            adminDescriptorAccordion));

        panel.addComponent(LayoutHelper.create("Admin Descriptors",
            accordionPanel, LABEL_WIDTH + 2, 300, false, new Button[] {
                addButton, editButton, delButton }));

        // Footer
        panel.addComponent(addFooter());

        setCompositionRoot(panel);
    }

    private HorizontalLayout addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);

        footer.addComponent(save);
        footer.addComponent(cancel);
        return footer;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == cancel) {
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
        else if (source == save) {
            boolean valid = true;
            valid =
                EmptyFieldValidator
                    .isValid(nameField, "Name can not be empty.");
            valid &=
                EmptyFieldValidator.isValid(descriptionField,
                    "Description can not be empty.");
            valid &=
                EmptyFieldValidator.isValid(typeField, ViewConstants.TYPE_LABEL
                    + " can not be empty.");
            valid &=
                EmptyFieldValidator.isValid(orgUnitList,
                    ViewConstants.ORGANIZATION_UNITS_LABEL
                        + " can not be empty");

            if (valid) {
                try {
                    nameField.setComponentError(null);
                    descriptionField.setComponentError(null);
                    typeField.setComponentError(null);
                    orgUnitList.setComponentError(null);
                    adminDescriptorAccordion.setComponentError(null);

                    final AdminDescriptors adminDescriptors =
                        enteredAdminDescriptors();
                    final OrganizationalUnitRefs selectedOrgUnitRefs =
                        getEnteredOrgUnitRefs();

                    final Context newContext =
                        contextService.create((String) nameField.getValue(),
                            (String) descriptionField.getValue(),
                            (String) typeField.getValue(),
                            // TODO: Replace by real call.
                            selectedOrgUnitRefs, adminDescriptors);
                    contextListView.addContext(newContext);
                    contextListView.sort();
                    contextListView.select(newContext);
                }
                catch (final EscidocException e) {
                    log.error(
                        "root cause: " + ExceptionUtils.getRootCauseMessage(e),
                        e);
                    app.getMainWindow().addWindow(
                        new ErrorDialog(app.getMainWindow(), "Error", e
                            .getMessage()));
     ;
                }
                catch (final InternalClientException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    setComponentError(new UserError(e.getMessage()));
                    app.getMainWindow().addWindow(
                        new ErrorDialog(app.getMainWindow(), "Error", e
                            .getMessage()));
     ;
                }
                catch (final TransportException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    app.getMainWindow().addWindow(
                        new ErrorDialog(app.getMainWindow(), "Error", e
                            .getMessage()));
                    setComponentError(new UserError(e.getMessage()));
     ;
                }
                catch (final ParserConfigurationException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    app.getMainWindow().addWindow(
                        new ErrorDialog(app.getMainWindow(), "Error", e
                            .getMessage()));
                    setComponentError(new SystemError(e.getMessage()));
     ;
                }
            }
        }
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
 ;
            }
            catch (final SAXException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), "Error", e
                        .getMessage()));
                setComponentError(new SystemError(e.getMessage()));
 ;
            }
            catch (final IOException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), "Error", e
                        .getMessage()));
                setComponentError(new SystemError(e.getMessage()));
 ;
            }
        }

        return adminDescriptors;
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
}