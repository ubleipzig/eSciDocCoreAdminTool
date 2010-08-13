package de.escidoc.admintool.view.context;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class AdminDescriptorAddView extends AdminDescriptorView {
    private static final String ADD_A_NEW_ADMIN_DESCRIPTOR =
        "Add a new Admin Descriptor";

    final String caption = ADD_A_NEW_ADMIN_DESCRIPTOR;

    public AdminDescriptorAddView(final Accordion adminDescriptorAccordion) {
        super(adminDescriptorAccordion);
    }

    @Override
    protected void setWindowCaption() {
        setCaption(ADD_A_NEW_ADMIN_DESCRIPTOR);
    }

    @Override
    protected void doSave() {
        getApplication().getMainWindow().showNotification("Add");

        final String content = (String) adminDescContent.getValue();
        if (validate(content)) {
            adminDescriptorAccordion.addTab(new Label(content,
                Label.CONTENT_PREFORMATTED), (String) adminDescName.getValue(),
                null);
            closeWindow();
        }
    }

}