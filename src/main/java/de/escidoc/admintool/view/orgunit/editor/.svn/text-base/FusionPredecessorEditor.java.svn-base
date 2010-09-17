package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.FusionPredecessorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class FusionPredecessorEditor extends AbstractPredecessorEditor {

    private static final Logger log =
        LoggerFactory.getLogger(FusionPredecessorEditor.class);

    private static final String EDITOR_DESCRIPTION =
        "Select minimal two organizational units, please.";

    private static final long serialVersionUID = 6885054493792037547L;

    public FusionPredecessorEditor(final OrgUnitService service) {
        super(service);
        setMultiSelect(true);
    }

    @Override
    protected boolean isValid(final int sizeOfSelectedOrgUnit) {
        return sizeOfSelectedOrgUnit >= 2;
    }

    @Override
    protected void showErrorMessage() {
        super.getMainWindow().addWindow(
            new ErrorDialog(super.getMainWindow(), "Error",
                "Select at least two items, please."));
    }

    @Override
    protected void showAddedPredecessors() {
        final AbstractPredecessorView addedPredecessorView =
            createPredecessorView(super.getSelectedPredecessors());
        orgUnitEditorView.showAddedPredecessors(addedPredecessorView);
    }

    private AbstractPredecessorView createPredecessorView(
        final List<ResourceRefDisplay> selectedPredecessors) {
        for (final ResourceRefDisplay resourceRefDisplay : selectedPredecessors) {
            log.info("selected: " + resourceRefDisplay);
        }
        final AbstractPredecessorView addedPredecessorView =
            new FusionPredecessorView(selectedPredecessors, super
                .getOrgUnitName());
        addedPredecessorView.setResourceRefDisplay(selectedPredecessors);
        return addedPredecessorView;
    }

    @Override
    protected String getLabel() {
        return "Set Predecessor Fusion";
    }

    @Override
    protected String getEditorDescription() {
        return EDITOR_DESCRIPTION;
    }

}