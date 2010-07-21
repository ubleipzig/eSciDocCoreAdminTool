package de.escidoc.admintool.lab;

import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.validator.EmptyStringValidator;
import de.escidoc.core.resources.om.context.AdminDescriptor;
import de.escidoc.core.resources.om.context.AdminDescriptors;

public class AdminDescriptorsEditView extends CustomField {

    private static final String ADMIN_DESCRIPTOR_CONTENT =
        "adminDescriptorContent";

    private static final String ADMIN_DESCRIPTOR_NAME = "adminDescriptorName";

    private VerticalLayout layout;

    private final Collection<AdminDescriptor> existingAdminDescriptors;

    private Form adminDescriptorsForm;

    private final boolean isClosed;

    private final Property adminDescriptorsProperty;

    public AdminDescriptorsEditView(final Property property,
        final boolean isClosed) {
        adminDescriptorsProperty = property;
        existingAdminDescriptors =
            (Collection<AdminDescriptor>) adminDescriptorsProperty.getValue();
        this.isClosed = isClosed;
        System.out.println("ad: " + property);

        initUI();
    }

    private void initUI() {
        adminDescriptorsForm = new Form();
        createFields();
        setCompositionRoot(adminDescriptorsForm);
    }

    private int index = 0;

    private AdminDescriptors adminDescriptors;

    private boolean isMultiple = false;

    private AdminDescriptorsEditView createFields() {
        if (existingAdminDescriptors == null) {
            // No admin descriptor, create normal admin descriptor like
            // AdminDescriptorAddView
            addNameField().addContentField().moreAdminDescriptorBtn();
        }
        else {
            isMultiple = true;
            for (final AdminDescriptor adminDescriptor : existingAdminDescriptors) {
                addNameField(adminDescriptor.getName()).addContentField(
                    adminDescriptor.getContent()).removeBtn();
                index++;
            }
            moreAdminDescriptor2Btn();

        }
        return this;
    }

    private void removeBtn() {
        // TODO add "Add": more admid desc.
        final Button removeAdminDescBtn = new Button("remove");
        adminDescriptorsForm.addField("removeAdminDescBtn" + index,
            removeAdminDescBtn);

        // TODO if add clicked, do:
        // add more name, content and remove button
        removeAdminDescBtn.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                removeAdminDescriptor();
            }
        });
    }

    private AdminDescriptorsEditView addNameField() {
        final TextField adminDescriptorNameField = new TextField("Name");

        adminDescriptorNameField.setWidth("400px");
        adminDescriptorNameField.addValidator(new EmptyStringValidator(
            "Admin Descriptor name can not be empty"));
        adminDescriptorsForm.addField(ADMIN_DESCRIPTOR_NAME + index,
            adminDescriptorNameField);
        return this;
    }

    private AdminDescriptorsEditView addNameField(final String value) {
        System.out.println("index: " + index);
        final TextField adminDescriptorNameField = new TextField("Name");
        adminDescriptorNameField.setWidth("400px");
        adminDescriptorNameField.addValidator(new EmptyStringValidator(
            "Admin Descriptor name can not be empty"));
        adminDescriptorNameField.setValue(value);
        adminDescriptorsForm.addField(ADMIN_DESCRIPTOR_NAME + index,
            adminDescriptorNameField);
        return this;
    }

    private AdminDescriptorsEditView addContentField() {
        final TextField adminDescriptorContentField = new TextField("Content");
        adminDescriptorContentField.setWidth("400px");

        adminDescriptorContentField.setRows(3);
        adminDescriptorContentField.addValidator(new EmptyStringValidator(
            "Admin Descriptor Content can not be empty"));

        adminDescriptorsForm.addField(ADMIN_DESCRIPTOR_CONTENT + index,
            adminDescriptorContentField);
        return this;
    }

    private AdminDescriptorsEditView addContentField(final Element value) {
        System.out.println("index: " + index);

        final TextField adminDescriptorContentField = new TextField("Content");
        adminDescriptorContentField.setWidth("400px");

        adminDescriptorContentField.setRows(3);
        adminDescriptorContentField.addValidator(new EmptyStringValidator(
            "Admin Descriptor Content can not be empty"));
        adminDescriptorContentField.setValue(value);
        adminDescriptorsForm.addField(ADMIN_DESCRIPTOR_CONTENT + index,
            adminDescriptorContentField);
        return this;
    }

    private AdminDescriptorsEditView moreAdminDescriptorBtn() {
        final Button addAdminDescBtn = new Button("add");
        adminDescriptorsForm.addField("addAdminDescriptorBtn", addAdminDescBtn);
        addAdminDescBtn.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                index++;
                addAdminDescriptor();
            }
        });
        return this;
    }

    private AdminDescriptorsEditView moreAdminDescriptor2Btn() {
        final Button addAdminDescBtn = new Button("add");
        adminDescriptorsForm.addField("addAdminDescriptorBtn", addAdminDescBtn);
        addAdminDescBtn.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                addAdminDescriptor();
                index++;

            }
        });
        return this;
    }

    private void addAdminDescriptor() {
        addNameField().addContentField();

        // TODO add "Add": more admid desc.
        final Button removeAdminDescBtn = new Button("remove");
        adminDescriptorsForm.addField("removeAdminDescBtn" + index,
            removeAdminDescBtn);

        // TODO if add clicked, do:
        // add more name, content and remove button
        removeAdminDescBtn.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                removeAdminDescriptor();
            }
        });
    }

    private void removeAdminDescriptor(int index) {
        adminDescriptorsForm.removeItemProperty(ADMIN_DESCRIPTOR_NAME + index);
        adminDescriptorsForm.removeItemProperty(ADMIN_DESCRIPTOR_CONTENT
            + index);
        adminDescriptorsForm.removeItemProperty("removeAdminDescBtn" + index);
        index--;
    }

    private void removeAdminDescriptor() {
        adminDescriptorsForm.removeItemProperty(ADMIN_DESCRIPTOR_NAME + index);
        adminDescriptorsForm.removeItemProperty(ADMIN_DESCRIPTOR_CONTENT
            + index);
        adminDescriptorsForm.removeItemProperty("removeAdminDescBtn" + index);
        index--;
    }

    private void buildAdminDescriptors() throws ParserConfigurationException {
        adminDescriptors = new AdminDescriptors();

        int size = index + 1;
        if (isMultiple) {
            size = index;
        }

        System.out.println("User wants to create: " + size + " AD");

        for (int i = 0; i < size; i++) {
            final String enteredName =
                (String) adminDescriptorsForm.getField(
                    ADMIN_DESCRIPTOR_NAME + i).getValue();

            if (enteredName == null || enteredName.isEmpty()) {
                return;
            }
            final AdminDescriptor adminDescriptor = new AdminDescriptor();
            // TODO set admin descriptor name from user input.
            adminDescriptor.setName(enteredName);

            // TODO set admin descriptor content from user input.
            final DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document doc = builder.newDocument();
            final Element element =
                doc.createElementNS(
                    "http://www.escidoc.de/schemas/context/0.4",
                    "admin-descriptor-example");

            adminDescriptor.setContent(element);

            adminDescriptors.add(adminDescriptor);
        }
    }

    public void commitForm() {
        // TODO adapt to the new API
        adminDescriptorsProperty.setValue(adminDescriptors);
    }

    public AdminDescriptors getAdminDescriptors()
        throws ParserConfigurationException {
        buildAdminDescriptors();
        return adminDescriptors;
    }

    @Override
    public Class<?> getType() {
        return Collection.class;
    }
}
