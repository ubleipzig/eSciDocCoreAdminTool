/**
 * 
 */
package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.lab.orgunit.AbstractOrgUnitViewLab;
import de.escidoc.admintool.view.orgunit.AbstractOrgUnitView;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;

/**
 * @author ASP
 * 
 */
public interface IPredecessorEditor {
    Window getWidget();

    void setMainWindow(final Window parent);

    void setList(final ListSelect select);

    // TODO Remove after succesful refactoring
    // public List<Object> getSelected();

    List<ResourceRefDisplay> getSelectedPredecessors();

    void setOrgUnitEditorView(AbstractOrgUnitView orgUnitEditorView);

    void setNewOrgUnit(String orgUnitName);

    void setOrgUnitEditorView(AbstractOrgUnitViewLab orgUnitEditorViewLab);
}
