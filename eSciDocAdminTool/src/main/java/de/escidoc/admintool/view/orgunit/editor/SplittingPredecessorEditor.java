package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.orgunit.predecessor.SplittingPredeccesorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class SplittingPredecessorEditor extends AbstractPredecessorEditor {
    private static final String SET_PREDECESSOR_SPLITTING =
        "Set Predecessor Splitting";

    private static final String COMMING_SOON = "Comming soon...";

    private static final long serialVersionUID = 6885054493792037547L;

    @Override
    protected void onOkClicked(ClickEvent event) {
        List<Object> ret = super.getSelected();
        if (ret.size() != 1) {
            new ErrorDialog(super.getWidgetParent(), "Grave Error",
                "Select exact one item, please.");
        }
        super.replacePredecessorView(new SplittingPredeccesorView());
        super.getWidgetParent().removeWindow(modalWindow);
        // TODO create new ones.
    }

    @Override
    protected String getLabel() {
        return SET_PREDECESSOR_SPLITTING;
    }

    @Override
    protected String getEditorDescription() {
        return COMMING_SOON;
    }
}
