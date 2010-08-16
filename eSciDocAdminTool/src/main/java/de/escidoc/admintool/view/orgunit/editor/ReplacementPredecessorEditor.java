package de.escidoc.admintool.view.orgunit.editor;

import de.escidoc.admintool.view.orgunit.predecessor.ReplacementPredecessorView;

public class ReplacementPredecessorEditor extends AbstractPredecessorEditor {

    private static final String EDITOR_DESCRIPTION =
        "Set one organizational unit as a replacement.";

    private static final String LABEL = "Set Predecessor Replacement";

    private static final long serialVersionUID = 6885054493792037547L;

    public ReplacementPredecessorEditor() {
        setMultiSelect(false);
    }

    @Override
    protected String getLabel() {
        return LABEL;
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
        orgUnitAddView.showAddedPredecessors(new ReplacementPredecessorView());
    }

    @Override
    protected void showErrorMessage() {
        // TODO Auto-generated method stub

    }
}
