package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.orgunit.predecessor.AffiliationPredecessorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class AffiliationPredecessorEditor extends AbstractPredecessorEditor {

    private static final String EDITOR_DESCRIPTION = "Coming Soon";

    private static final long serialVersionUID = 6885054493792037547L;

    private static final String LABEL = "Set Predecessor Affiliation";

    public AffiliationPredecessorEditor() {
        super.setMultiSelect(false);
    }

    @Override
    protected void onOkClicked(ClickEvent event) {
        List<Object> ret = super.getSelected();
        if (ret.size() != 1) {
            new ErrorDialog(super.getWidgetParent(), "Grave Error",
                "Select exact one item, please.");
        }
        super.replacePredecessorView(new AffiliationPredecessorView());
        super.getWidgetParent().removeWindow(modalWindow);
    }

    @Override
    protected String getLabel() {
        return LABEL;
    }

    @Override
    protected String getEditorDescription() {
        return EDITOR_DESCRIPTION;
    }
}
