package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.orgunit.predecessor.SpinOffPredecessorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class SpinOffPPredecessorEditor extends AbstractPredecessorEditor
    implements IPredecessorEditor {
    private static final String EDITOR_DESCRIPTION =
        "Select one organizational unit, please";

    private static final String SET_PREDECESSOR_SPIN_OFF =
        "Set Predecessor SpinOff";

    private static final long serialVersionUID = 6885054493792037547L;

    @Override
    protected void onOkClicked(ClickEvent event) {
        List<Object> ret = super.getSelected();
        if (ret.size() != 1) {
            new ErrorDialog(super.getWidgetParent(), "Grave Error",
                "Select exact one item, please.");
        }
        super.replacePredecessorView(new SpinOffPredecessorView());
        super.getWidgetParent().removeWindow(modalWindow);
    }

    @Override
    protected String getLabel() {
        return SET_PREDECESSOR_SPIN_OFF;
    }

    @Override
    protected String getEditorDescription() {
        return EDITOR_DESCRIPTION;
    }
}
