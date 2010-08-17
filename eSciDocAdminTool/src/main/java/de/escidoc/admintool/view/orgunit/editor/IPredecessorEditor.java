/**
 * 
 */
package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.orgunit.OrgUnitAddView;

/**
 * @author ASP
 * 
 */
public interface IPredecessorEditor {
    public Window getWidget();

    public void setMainWindow(final Window parent);

    public void setList(final ListSelect select);

    public List<Object> getSelected();

    public void setOrgUnitAddView(OrgUnitAddView orgUnitAddView);

    public void setNewOrgUnit(String orgUnitName);
}
