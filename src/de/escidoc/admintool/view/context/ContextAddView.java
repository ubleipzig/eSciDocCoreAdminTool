package de.escidoc.admintool.view.context;

import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.terminal.SystemError;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
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
import com.vaadin.ui.TabSheet.Tab;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.OrgUnitAddView;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;
import de.escidoc.vaadin.utilities.LayoutHelper;

@SuppressWarnings("serial")
public class ContextAddView extends CustomComponent implements ClickListener {
    private final Logger log = LoggerFactory.getLogger(ContextAddView.class);

    private final AdminToolApplication app;

    private final ContextListView contextListView;

    private final ContextService contextService;

    private OrgUnitAddView orgUnitAddView;

    private final OrgUnitService orgUnitService;

    private TextField nameField;

    private TextField descriptionField;

    private TextField typeField;

    private ObjectProperty nameProperty;

    private ObjectProperty descriptionProperty;

    private ObjectProperty typeProperty;

    private Object[][] orgUnits;

    private HorizontalLayout footer;

    private final Button save = new Button("Save", this);

    private final Button cancel = new Button("Cancel", this);

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
            nameField, "150px", true));
        nameProperty = mapBinding("", nameField);

        descriptionField = new TextField();
        descriptionField.setWidth("400px");
        descriptionField.setRows(3);
        panel.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL,
            descriptionField, "150px", 80, true));
        descriptionProperty = mapBinding("", descriptionField);

        typeField = new TextField();
        typeField.setWidth("400px");
        panel.addComponent(LayoutHelper.create(ViewConstants.TYPE_LABEL,
            typeField, "150px", true));
        typeProperty = mapBinding("", typeField);

        orgUnitList = new ListSelect();
        panel.addComponent(LayoutHelper.create(
            ViewConstants.ORGANIZATION_UNITS_LABEL, orgUnitList, "150px", 100,
            true));
        orgUnitList.setRows(5);
        orgUnitList.setWidth("400px");
        orgUnitList.setNullSelectionAllowed(true);
        orgUnitList.setMultiSelect(true);
        orgUnitList.setImmediate(true);
        orgUnitList.getValue();

        final Button addOrgUnitButton = new Button("Add");

        addOrgUnitButton.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                final Window openTreeButtonWindow =
                    new Window(ViewConstants.ORGANIZATION_UNITS_LABEL); //$NON-NLS-1$
                openTreeButtonWindow.setModal(true);
                final OrgUnitTree tree = new OrgUnitTree(openTreeButtonWindow);
                /* Set window size. */
                openTreeButtonWindow.setHeight("650px"); //$NON-NLS-1$
                openTreeButtonWindow.setWidth("550px"); //$NON-NLS-1$
                openTreeButtonWindow.addComponent(tree);
                final Button okButton = new Button("OK");
                okButton.addListener(new Button.ClickListener() {

                    public void buttonClick(final ClickEvent event) {
                        final Object o = tree.getSelectedItems();
                        o.getClass().toString();
                        if (o instanceof HashSet) {
                            final HashSet<String> set = (HashSet<String>) o;
                            for (final String str : set) {
                                orgUnitList.addItem(str);
                            }
                        }
                        else if (o instanceof Set) {
                            final Set<String> set = (Set<String>) o;
                            for (final String str : set) {
                                orgUnitList.addItem(str);
                            }
                        }
                        else if (o instanceof Object) {
                            orgUnitList.addItem(o);
                        }
                        ((Window) openTreeButtonWindow.getParent())
                            .removeWindow(openTreeButtonWindow);
                    }
                });
                final Button cancelButton = new Button("Cancel");
                cancelButton.addListener(new Button.ClickListener() {

                    public void buttonClick(final ClickEvent event) {
                        ((Window) openTreeButtonWindow.getParent())
                            .removeWindow(openTreeButtonWindow);
                    }
                });

                final HorizontalLayout hor =
                    LayoutHelper.create("", "", okButton, cancelButton, "10px",
                        false);
                openTreeButtonWindow.addComponent(hor);
                getApplication()
                    .getMainWindow().addWindow(openTreeButtonWindow);
            }
        });
        final Button removeOrgUnitButton = new Button("Remove");
        panel.addComponent(LayoutHelper.create("", "", addOrgUnitButton,
            removeOrgUnitButton, "150px", "0px", false));

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

        addButton.addListener(new NewAdminDescriptorListener());
        editButton.addListener(new EditAdminDescriptorListener());
        delButton.addListener(new RemoveAdminDescriptorListener());

        panel.addComponent(LayoutHelper.create("Admin Descriptors",
            accordionPanel, "150px", 300, true, new Button[] { addButton,
                editButton, delButton }));

        // Footer
        panel.addComponent(addFooter());

        setCompositionRoot(panel);
    }

    private class NewAdminDescriptorListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            getApplication().getMainWindow().addWindow(
                new AdminDescriptorAddView(adminDescriptorAccordion));

        }
    }

    private class EditAdminDescriptorListener implements Button.ClickListener {

        public void buttonClick(final ClickEvent event) {
            final Component selectedTab =
                adminDescriptorAccordion.getSelectedTab();
            if (selectedTab == null) {
                return;
            }

            getApplication().getMainWindow().addWindow(
                new AdminDescriptorEditView(adminDescriptorAccordion,
                    getName(adminDescriptorAccordion.getTab(selectedTab)),
                    getContent(selectedTab)));
        }

        private String getName(final Tab tab) {
            String name = "";
            if (tab != null) {
                name = tab.getCaption();
            }
            return name;
        }

        private String getContent(final Component selectedTab) {
            final String content = (String) ((Label) selectedTab).getValue();
            return content;
        }
    }

    private class RemoveAdminDescriptorListener implements Button.ClickListener {

        public void buttonClick(final ClickEvent event) {
            adminDescriptorAccordion.removeComponent(adminDescriptorAccordion
                .getSelectedTab());
        }
    }

    private HorizontalLayout addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);

        footer.addComponent(save);
        footer.addComponent(cancel);
        return footer;
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == cancel) {
            nameField.setValue("");
            descriptionField.setValue("");
            typeField.setValue("");
            // TODO: Do we need to clear the fields below?
            // adminDescriptorAccordion.removeTab();
            // adminDescriptorsAddView.clear();
            // orgUnitList.removeAllItems();
            // setComponentError(null);
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
                EmptyFieldValidator.isValid(orgUnitList, "TODO: fill me!!!!!"); // TODO:
            // fill
            // me!!!!!

            // TODO: write a validator for the Accordion
            valid &= false; // replace by the validator call.

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
                        enteredOrgUnits();

                    final Context newContext =
                        contextService.create((String) nameField.getValue(),
                            (String) descriptionField.getValue(),
                            (String) typeField.getValue(),
                            // TODO: Replace by real call.
                            selectedOrgUnitRefs, adminDescriptors);
                    contextListView.addContext(newContext);
                    contextListView.sort();
                    contextListView.select(newContext.getObjid());
                }
                catch (final EscidocException e) {
                    log.error("root cause: "
                        + ExceptionUtils.getRootCauseMessage(e), e);
                    setComponentError(new UserError(e.getMessage()));
                    // TODO: Where to set the error?
                    e.printStackTrace();
                }
                catch (final InternalClientException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    setComponentError(new UserError(e.getMessage()));
                    // TODO: Where to set the error?
                    e.printStackTrace();
                }
                catch (final TransportException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    // TODO: Where to set the error?
                    setComponentError(new UserError(e.getMessage()));
                    e.printStackTrace();
                }
                catch (final ParserConfigurationException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    // TODO: Where to set the error?
                    setComponentError(new SystemError(e.getMessage()));
                    e.printStackTrace();
                }
            }
        }
    }

    private AdminDescriptors enteredAdminDescriptors()
        throws ParserConfigurationException {
        return null;
    }

    private OrganizationalUnitRefs enteredOrgUnits() {
        return null;
    }

}