package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.SpinOffPredecessorView;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;

public class SpinOffPPredecessorEditor extends AbstractPredecessorEditor
    implements IPredecessorEditor {

    private static final Logger LOG = LoggerFactory
        .getLogger(SpinOffPPredecessorEditor.class);

    private static final String EDITOR_DESCRIPTION =
        "Select one organizational unit to be replaced.";

    private static final String SET_PREDECESSOR_SPIN_OFF =
        "Set Predecessor SpinOff";

    private static final long serialVersionUID = 6885054493792037547L;

    public SpinOffPPredecessorEditor(final OrgUnitService service) {
        super(service);
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
        final AbstractPredecessorView addedPredecessorView =
            createPredecessorView(super.getSelectedPredecessors());
        orgUnitEditorViewLab.showAddedPredecessors(addedPredecessorView);
    }

    // TODO move to a factory class.DRY
    private AbstractPredecessorView createPredecessorView(
        final List<ResourceRefDisplay> selectedPredecessors) {
        for (final ResourceRefDisplay resourceRefDisplay : selectedPredecessors) {
            LOG.info("selected: " + resourceRefDisplay);
        }
        final AbstractPredecessorView addedPredecessorView =
            new SpinOffPredecessorView(selectedPredecessors.get(0).getTitle(),
                super.getOrgUnitName());
        addedPredecessorView.setResourceRefDisplay(selectedPredecessors.get(0));
        return addedPredecessorView;
    }

    @Override
    protected void showErrorMessage() {
        super.getMainWindow().addWindow(
            new ErrorDialog(super.getMainWindow(), "Error",
                "Select at least two items, please."));
    }

}
