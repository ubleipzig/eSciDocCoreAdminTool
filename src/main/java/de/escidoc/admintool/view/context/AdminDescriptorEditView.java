package de.escidoc.admintool.view.context;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class AdminDescriptorEditView extends AdminDescriptorView {

    public AdminDescriptorEditView(final Window mainWindow,
        final Accordion adminDescriptorAccordion, final String name,
        final String content) {
        super(mainWindow, adminDescriptorAccordion, name, content);
    }

    @Override
    protected void setWindowCaption() {
        setCaption(EDIT_ADMIN_DESCRIPTOR);
    }

    @Override
    protected void doSave() {
        final String content = (String) adminDescContent.getValue();
        if (validate(content)) {
            getTabTitle().setCaption((String) adminDescName.getValue());
            adminDescriptorAccordion.replaceComponent(adminDescriptorAccordion
                .getSelectedTab(), new Label(content,
                Label.CONTENT_PREFORMATTED));
            closeWindow();
        }
    }

    private Tab getTabTitle() {
        return adminDescriptorAccordion.getTab(adminDescriptorAccordion
            .getSelectedTab());
    }
}