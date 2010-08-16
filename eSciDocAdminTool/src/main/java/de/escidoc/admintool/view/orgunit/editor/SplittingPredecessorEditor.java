package de.escidoc.admintool.view.orgunit.editor;

import de.escidoc.admintool.view.orgunit.predecessor.SplittingPredeccesorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class SplittingPredecessorEditor extends AbstractPredecessorEditor {
    private static final long serialVersionUID = 6885054493792037547L;

    private static final String SET_PREDECESSOR_SPLITTING =
        "Set Predecessor Splitting";

    private static final String COMMING_SOON = "Comming soon...";

    public SplittingPredecessorEditor() {
        setMultiSelect(false);
    }

    @Override
    protected String getLabel() {
        return SET_PREDECESSOR_SPLITTING;
    }

    @Override
    protected String getEditorDescription() {
        return COMMING_SOON;
    }

    @Override
    protected boolean isValid(final int sizeOfSelectedOrgUnit) {
        return sizeOfSelectedOrgUnit == 1;
    }

    @Override
    protected void showAddedPredecessors() {
        orgUnitAddView.showAddedPredecessors(new SplittingPredeccesorView());
    }

    @Override
    protected void showErrorMessage() {
        super.getMainWindow().addWindow(
            new ErrorDialog(super.getMainWindow(), "Grave Error",
                "Select one organizational unit, please."));
    }
}