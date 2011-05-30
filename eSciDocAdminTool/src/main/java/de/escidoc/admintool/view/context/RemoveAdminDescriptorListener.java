/**
 * 
 */
package de.escidoc.admintool.view.context;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author ASP
 * 
 */
public class RemoveAdminDescriptorListener implements Button.ClickListener {

    private final Accordion adminDescriptorAccordion;

    public RemoveAdminDescriptorListener(Accordion adminDescriptorAccordion) {
        this.adminDescriptorAccordion = adminDescriptorAccordion;
    }

    public void buttonClick(final ClickEvent event) {
        adminDescriptorAccordion.removeComponent(adminDescriptorAccordion.getSelectedTab());
    }
}
