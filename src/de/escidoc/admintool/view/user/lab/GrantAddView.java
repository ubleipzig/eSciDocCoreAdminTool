package de.escidoc.admintool.view.user.lab;

import java.util.Collection;

import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.core.resources.aa.role.Role;

@SuppressWarnings("serial")
public class GrantAddView extends CustomComponent {

    public GrantAddView() {
        // A layout structure used for composition
        final Panel panel = new Panel("Assingn Roles");
        final VerticalLayout layout = new VerticalLayout();
        panel.setContent(layout);

        // Compose from multiple components
        final Label label = new Label("Test");

        label.setSizeUndefined(); // Shrink
        panel.addComponent(label);
        panel.addComponent(new Button("Ok"));

        final Collection<Role> predefinedRoles = null;
        final POJOContainer<Role> rolesContainer =
            new POJOContainer<Role>(predefinedRoles, "properties.name");

        final TwinColSelect listBuilder = new TwinColSelect();
        panel.addComponent(listBuilder);

        // Set the size as undefined at all levels
        panel.getContent().setSizeUndefined();
        panel.setSizeUndefined();
        setSizeUndefined();
        // The composition root MUST be set
        setCompositionRoot(panel);
    }
}
