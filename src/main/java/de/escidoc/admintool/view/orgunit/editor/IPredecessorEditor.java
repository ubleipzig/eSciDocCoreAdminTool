/**
 * 
 */
package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.lab.orgunit.AbstractOrgUnitViewLab;
import de.escidoc.admintool.view.orgunit.AbstractOrgUnitView;

/**
 * @author ASP
 * 
 */
public interface IPredecessorEditor {
    Window getWidget();

    void setMainWindow(final Window parent);

    void setList(final ListSelect select);

    List<ResourceRefDisplay> getSelectedPredecessors();

    void setOrgUnitEditorView(AbstractOrgUnitView orgUnitEditorView);

    void setNewOrgUnit(String orgUnitName);

    void setOrgUnitEditorView(AbstractOrgUnitViewLab orgUnitEditorViewLab);
}
