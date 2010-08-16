package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;

import de.escidoc.admintool.view.orgunit.predecessor.ReplacementPredecessorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class ReplacementPredecessorEditor extends AbstractPredecessorEditor {

    private static final String EDITOR_DESCRIPTION =
        "Set one organizational unit as a replacement.";

    private static final String LABEL = "Set Predecessor Replacement";

    private static final long serialVersionUID = 6885054493792037547L;

    private final FormLayout mainLayout = new FormLayout();

    @Override
    protected void onOkClicked(ClickEvent event) {
        List<Object> ret = super.getSelected();
        if (ret.size() != 1) {
            new ErrorDialog(super.getWidgetParent(), "Grave Error",
                "Select exact one item, please.");
        }
        ReplacementPredecessorView replacementPredecessorView =
            new ReplacementPredecessorView();

        super.replacePredecessorView(replacementPredecessorView);
        super.setPredecessorResult(replacementPredecessorView);

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
