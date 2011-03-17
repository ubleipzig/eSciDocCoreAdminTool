/**
 * 
 */
package de.escidoc.admintool.view.context;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window;

/**
 * @author ASP
 * 
 */
public class EditAdminDescriptorListener implements Button.ClickListener {
    private static final long serialVersionUID = 2603421967112465661L;

    private final Accordion adminDescriptorAccordion;

    private final Window mainWindow;

    public EditAdminDescriptorListener(final Window mainWindow,
        final Accordion adminDescriptorAccordion) {
        Preconditions.checkNotNull(mainWindow,
            "mainWindow can not be null: %s", mainWindow);
        Preconditions.checkNotNull(adminDescriptorAccordion,
            "adminDescriptorAccordion can not be null: %s",
            adminDescriptorAccordion);

        this.mainWindow = mainWindow;
        this.adminDescriptorAccordion = adminDescriptorAccordion;
    }

    public void buttonClick(final ClickEvent event) {
        final Component selectedTab = adminDescriptorAccordion.getSelectedTab();
        if (selectedTab == null) {
            return;
        }

        mainWindow.addWindow(new AdminDescriptorEditView(mainWindow,
            adminDescriptorAccordion, getName(adminDescriptorAccordion
                .getTab(selectedTab)), getContent(selectedTab)));
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
