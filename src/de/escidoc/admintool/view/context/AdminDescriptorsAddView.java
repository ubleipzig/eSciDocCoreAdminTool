package de.escidoc.admintool.view.context;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vaadin.data.Validator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.validator.EmptyStringValidator;
import de.escidoc.core.resources.om.context.AdminDescriptor;
import de.escidoc.core.resources.om.context.AdminDescriptors;

public class AdminDescriptorsAddView {

    private static final String ADMIN_DESCRIPTOR_CONTENT =
        "adminDescriptorContent";

    private static final String ADMIN_DESCRIPTOR_NAME = "adminDescriptorName";

    // TODO refactor method: using smaller private method
    private final ContextAddView contextAddView;

    private int index = 0;

    private AdminDescriptors adminDescriptors;

    public AdminDescriptorsAddView(final ContextAddView contextAddView) {
        this.contextAddView = contextAddView;
        initUI();
    }

    private void initUI() {
        contextAddView.getLayout().addComponent(new Label("Admin Descriptors"));
        addNameField()
            .addContentField().addDebugButton().moreAdminDescriptorBtn();
    }

    private AdminDescriptorsAddView addDebugButton() {
        // TODO add debug button to debug user input
        final Button debugInputBtn = new Button("debug");
        debugInputBtn.setVisible(true);
        contextAddView.addField("debugAdminDescBtn", debugInputBtn);

        // TODO if add clicked, do:
        // add more name, content and remove button
        debugInputBtn.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                try {
                    debug();
                }
                catch (final ParserConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        return this;
    }

    private AdminDescriptorsAddView moreAdminDescriptorBtn() {
        final Button addAdminDescBtn = new Button("add");
        contextAddView.addField("addAdminDescriptorBtn", addAdminDescBtn);
        addAdminDescBtn.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                index++;
                addAdminDescriptor();
            }
        });
        return this;
    }

    private AdminDescriptorsAddView addContentField() {
        final TextField adminDescriptorContentField = new TextField("Content");
        adminDescriptorContentField.setWidth("400px");

        adminDescriptorContentField.setRows(3);
        adminDescriptorContentField.addValidator(new EmptyStringValidator(
            "Admin Descriptor Content can not be empty"));
        contextAddView.addField(ADMIN_DESCRIPTOR_CONTENT + index,
            adminDescriptorContentField);
        return this;
    }

    private AdminDescriptorsAddView addNameField() {
        final TextField adminDescriptorNameField = new TextField("Name");
        adminDescriptorNameField.setWidth("400px");
        adminDescriptorNameField.addValidator(new EmptyStringValidator(
            "Admin Descriptor name can not be empty"));
        contextAddView.addField(ADMIN_DESCRIPTOR_NAME + index,
            adminDescriptorNameField);
        return this;
    }

    private void debug() throws ParserConfigurationException {

        final int size = index + 1;
        System.out.println("User wants to create: " + size + " AD");
        for (int i = 0; i < size; i++) {
            for (final Validator validator : contextAddView.getField(
                ADMIN_DESCRIPTOR_NAME + i).getValidators()) {
                System.out.println("validator: " + validator);
            }
            System.out.println("valid: "
                + contextAddView.getField(ADMIN_DESCRIPTOR_NAME + i).isValid());
            System.out.println("name: "
                + contextAddView.getField(ADMIN_DESCRIPTOR_NAME + i));
            // System.out.println("content: "
            // + contextAddView.getField(CONTENT + i));
        }
        buildAdminDescriptors();
        // TODO iterate over adminDescriptors, debug its value
        for (final AdminDescriptor ad : adminDescriptors) {
            System.out.println(ad.getName());
            System.out.println(ad.getContent());
        }

    }

    private void buildAdminDescriptors() throws ParserConfigurationException {
        adminDescriptors = new AdminDescriptors();

        final int size = index + 1;
        System.out.println("User wants to create: " + size + " AD");

        for (int i = 0; i < size; i++) {
            final String enteredName =
                (String) contextAddView
                    .getField(ADMIN_DESCRIPTOR_NAME + i).getValue();

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

    private void addAdminDescriptor() {
        addNameField().addContentField();

        // TODO add "Add": more admid desc.
        final Button removeAdminDescBtn = new Button("remove");
        contextAddView.addField("removeAdminDescBtn" + index,
            removeAdminDescBtn);

        // TODO if add clicked, do:
        // add more name, content and remove button
        removeAdminDescBtn.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                removeAdminDescriptor();
            }
        });
    }

    private void removeAdminDescriptor() {
        contextAddView.removeItemProperty(ADMIN_DESCRIPTOR_NAME + index);
        contextAddView.removeItemProperty(ADMIN_DESCRIPTOR_CONTENT + index);
        contextAddView.removeItemProperty("removeAdminDescBtn" + index);
        index--;
    }

    public AdminDescriptors getAdminDescriptors()
        throws ParserConfigurationException {
        buildAdminDescriptors();
        return adminDescriptors;
    }

    public void clear() {
        final int size = index + 1;
        for (int i = 0; i < size; i++) {
            contextAddView.getField(ADMIN_DESCRIPTOR_NAME + i).setValue("");
            contextAddView.getField(ADMIN_DESCRIPTOR_CONTENT + i).setValue("");
        }
    }
}