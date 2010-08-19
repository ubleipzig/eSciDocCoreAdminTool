package de.escidoc.admintool.view.orgunit.editor;

import java.util.ArrayList;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.ReplacementPredecessorView;

public class ReplacementPredecessorEditor extends AbstractPredecessorEditor {

    private static final String EDITOR_DESCRIPTION =
        "Set one organizational unit as a replacement.";

    private static final String LABEL = "Set Predecessor Replacement";

    private static final long serialVersionUID = 6885054493792037547L;

    public ReplacementPredecessorEditor(OrgUnitService service) {
        super(service);
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
        ArrayList<Object> l = (ArrayList<Object>) super.getSelected();
        AbstractPredecessorView addedPredecessorView =
            new ReplacementPredecessorView((String) l.get(0),
                super.getOrgUnitName());
        orgUnitAddView.showAddedPredecessors(addedPredecessorView);
    }

    @Override
    protected void showErrorMessage() {
        // TODO Auto-generated method stub

    }
}
