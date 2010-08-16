package de.escidoc.admintool.view.orgunit.editor;

import de.escidoc.admintool.view.orgunit.predecessor.SpinOffPredecessorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class SpinOffPPredecessorEditor extends AbstractPredecessorEditor
    implements IPredecessorEditor {

    private static final String EDITOR_DESCRIPTION =
        "Select one organizational unit, please";

    private static final String SET_PREDECESSOR_SPIN_OFF =
        "Set Predecessor SpinOff";

    private static final long serialVersionUID = 6885054493792037547L;

    public SpinOffPPredecessorEditor() {
        setMultiSelect(false);
    }

    @Override
    protected String getLabel() {
        return SET_PREDECESSOR_SPIN_OFF;
    }

    @Override
    protected String getEditorDescription() {
        return EDITOR_DESCRIPTION;
    }

    @Override
    protected boolean isValid(final int sizeOfSelectedOrgUnit) {
        return sizeOfSelectedOrgUnit == 1;
    }

    @Override
    protected void showAddedPredecessors() {
        orgUnitAddView.showAddedPredecessors(new SpinOffPredecessorView());
    }

    @Override
    protected void showErrorMessage() {
        super.getMainWindow().addWindow(
            new ErrorDialog(super.getMainWindow(), "Grave Error",
                "Select at least two items, please."));
    }
}
