package de.escidoc.admintool.view.orgunit.editor;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.AffiliationPredecessorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class AffiliationPredecessorEditor extends AbstractPredecessorEditor {

    private static final String EDITOR_DESCRIPTION = "Coming Soon";

    private static final long serialVersionUID = 6885054493792037547L;

    private static final String LABEL = "Set Predecessor Affiliation";

    public AffiliationPredecessorEditor(OrgUnitService service) {
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
        AbstractPredecessorView addedPredecessorView =
            new AffiliationPredecessorView((String) super.getSelected().get(0),
                super.getOrgUnitName());
        orgUnitAddView.showAddedPredecessors(addedPredecessorView);
    }

    @Override
    protected void showErrorMessage() {
        super.getMainWindow().addWindow(
            new ErrorDialog(super.getMainWindow(), "Grave Error",
                "Select at least two items, please."));
    }
}