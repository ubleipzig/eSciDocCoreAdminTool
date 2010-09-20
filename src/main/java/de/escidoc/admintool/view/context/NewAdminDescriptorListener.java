/**
 * 
 */
package de.escidoc.admintool.view.context;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

/**
 * @author ASP
 * 
 */
public class NewAdminDescriptorListener implements ClickListener {
    private static final long serialVersionUID = 2401999112178265686L;

    private final Accordion adminDescriptorAccordion;

    private final Window mainWindow;

    public NewAdminDescriptorListener(final Window mainWindow,
        final Accordion adminDescriptorAccordion) {
        this.mainWindow = mainWindow;
        this.adminDescriptorAccordion = adminDescriptorAccordion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    public void buttonClick(final ClickEvent event) {
        mainWindow.addWindow(new AdminDescriptorAddView(mainWindow,
            adminDescriptorAccordion));
    }
}
