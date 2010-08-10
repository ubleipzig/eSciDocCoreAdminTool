package de.escidoc.admintool.view.context;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class AdminDescriptorEditor extends Window {
    private static final long serialVersionUID = -3955201753196948629L;

    private static final String ADD_A_NEW_ADMIN_DESCRIPTOR =
        "Add a new Admin Descriptor";

    private static final String EDIT_ADMIN_DESCRIPTOR = "Edit Admin Descriptor";

    private final TextField adminDescName = new TextField("Name: ");

    private final TextField adminDescContent = new TextField("Content: ");

    private final FormLayout formLayout = new FormLayout();

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button saveButton = new Button("Save");

    private final Button cancelButton = new Button("Cancel");

    private final Accordion adminDesciptorAccordion;

    private String name;

    private String content;

    @SuppressWarnings("serial")
    public AdminDescriptorEditor(final Accordion adminDesciptorAccordion) {
        this.adminDesciptorAccordion = adminDesciptorAccordion;
        String caption = ADD_A_NEW_ADMIN_DESCRIPTOR;
        if (this.isEditMode) {
            caption = EDIT_ADMIN_DESCRIPTOR;
        }
        setCaption(caption);
        setModal(true);
        setWidth("600px");
        setHeight("400px");

        adminDescName.setWidth("400px");
        adminDescContent.setRows(5);
        adminDescContent.setWidth("400px");

        if (this.name != null) {
            adminDescName.setValue(this.name);

            if (this.content != null) {
                adminDescContent.setValue(this.content);
            }
        }
        formLayout.addComponent(adminDescName);
        formLayout.addComponent(adminDescContent);

        footer.addComponent(saveButton);
        footer.addComponent(cancelButton);

        saveButton.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                String content = (String) adminDescContent.getValue();
                if (validate(content)) {
                    adminDesciptorAccordion.addTab(new Label(content,
                        Label.CONTENT_PREFORMATTED), (String) adminDescName
                        .getValue(), null);
                    closeWindow();
                }
            }
        });

        cancelButton.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                closeWindow();
            }
        });
        formLayout.addComponent(footer);
        addComponent(formLayout);
    }

    private boolean isEditMode = false;

    public void setAdminDescriptorName(String name) {
        this.name = name;
        isEditMode = true;
    }

    public void setAdminDescriptorContent(String content) {
        this.content = content;
        this.isEditMode = true;
    }

    private boolean validate(String value) {
        // TODO: parse XML snippet and check if it is well-formed?
        return true;
    }

    private void closeWindow() {
        getApplication().getMainWindow().removeWindow(
            AdminDescriptorEditor.this);
    }
}