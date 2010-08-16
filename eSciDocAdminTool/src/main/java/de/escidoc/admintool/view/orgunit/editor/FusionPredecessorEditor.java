package de.escidoc.admintool.view.orgunit.editor;

import de.escidoc.admintool.view.orgunit.predecessor.FusionPredecessorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class FusionPredecessorEditor extends AbstractPredecessorEditor {
    private static final long serialVersionUID = 6885054493792037547L;

    public FusionPredecessorEditor() {
        setMultiSelect(true);
    }

    @Override
    protected boolean isValid(final int sizeOfSelectedOrgUnit) {
        return sizeOfSelectedOrgUnit >= 2;
    }

    @Override
    protected void showErrorMessage() {
        super.getMainWindow().addWindow(
            new ErrorDialog(super.getMainWindow(), "Grave Error",
                "Select at least two items, please."));
    }

    @Override
    protected void showAddedPredecessors() {
        orgUnitAddView.showAddedPredecessors(new FusionPredecessorView());
    }

    @Override
    protected String getLabel() {
        return "Set Predecessor Fusion";
    }

    @Override
    protected String getEditorDescription() {
        return "Select min. two organizational units, please.";
    }

}