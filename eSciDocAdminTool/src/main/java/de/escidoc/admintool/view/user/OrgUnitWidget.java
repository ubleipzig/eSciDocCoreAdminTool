package de.escidoc.admintool.view.user;

import java.util.Set;

import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.view.resource.ResourceRefDisplay;

public interface OrgUnitWidget extends Component {

    void addTable();

    OrgUnitWidget withAddButton();

    OrgUnitWidget withRemoveButton();

    VerticalLayout build();

    void addListener(ClickListener clickListener);

    Table getTable();

    Set<ResourceRefDisplay> getOrgUnitsFromTable();

    void withListener(ClickListener clickListener);

    Set<ResourceRefDisplay> getSelectedOrgUnits();

}
