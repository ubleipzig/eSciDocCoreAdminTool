package de.escidoc.admintool.view.context;

import com.vaadin.ui.Accordion;

@SuppressWarnings("serial")
public class AdminDescriptorEditView extends AdminDescriptorView {

    public AdminDescriptorEditView(final Accordion adminDescriptorAccordion,
        final String name, final String content) {
        super(adminDescriptorAccordion, name, content);
    }

    @Override
    protected void setWindowCaption() {
        setCaption(EDIT_ADMIN_DESCRIPTOR);
    }

    @Override
    protected void doSave() {
        final String content = (String) adminDescContent.getValue();
        if (validate(content)) {
            getApplication().getMainWindow().showNotification("Edit");

            // TODO replace selected tab with new name and content;

            // adminDesciptorAccordion.replaceComponent(oldComponent,
            // newComponent)

            // addTab(new Label(content, Label.CONTENT_PREFORMATTED),
            // (String) adminDescName.getValue(), null);
            closeWindow();
        }
    }

}
