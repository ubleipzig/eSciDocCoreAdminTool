package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.ReplacementPredecessorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class ReplacementPredecessorEditor extends AbstractPredecessorEditor {

    private static final Logger log = LoggerFactory
        .getLogger(ReplacementPredecessorEditor.class);

    private static final String EDITOR_DESCRIPTION =
        "Select one organizational unit to be replaced.";

    private static final String LABEL = "Set Predecessor Replacement";

    private static final long serialVersionUID = 6885054493792037547L;

    public ReplacementPredecessorEditor(final OrgUnitService service) {
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
        final AbstractPredecessorView addedPredecessorView =
            createPredecessorView(super.getSelectedPredecessors());
        orgUnitEditorView.showAddedPredecessors(addedPredecessorView);
    }

    // TODO move to a factory class.DRY
    private AbstractPredecessorView createPredecessorView(
        final List<ResourceRefDisplay> selectedPredecessors) {
        for (final ResourceRefDisplay resourceRefDisplay : selectedPredecessors) {
            log.info("selected: " + resourceRefDisplay);
        }
        final AbstractPredecessorView addedPredecessorView =
            new ReplacementPredecessorView(selectedPredecessors
                .get(0).getTitle(), super.getOrgUnitName());
        addedPredecessorView.setResourceRefDisplay(selectedPredecessors.get(0));
        return addedPredecessorView;
    }

    @Override
    protected void showErrorMessage() {
        super.getMainWindow().addWindow(
            new ErrorDialog(super.getMainWindow(), "Error",
                "Select at least one item, please."));
    }
}