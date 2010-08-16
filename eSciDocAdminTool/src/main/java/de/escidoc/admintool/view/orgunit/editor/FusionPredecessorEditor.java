package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.orgunit.predecessor.FusionPredecessorView;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class FusionPredecessorEditor extends AbstractPredecessorEditor {
    private static final long serialVersionUID = 6885054493792037547L;

    @Override
    protected void onOkClicked(ClickEvent event) {
        List<Object> ret = super.getSelected();
        if (ret.size() < 2) {
            super.getWidgetParent().addWindow(
                new ErrorDialog(super.getWidgetParent(), "Grave Error",
                    "Select at least two items, please."));
        }
        super.replacePredecessorView(new FusionPredecessorView());
        super.getWidgetParent().removeWindow(modalWindow);
    }

    @Override
    protected String getLabel() {
        return "Set Predecessor Fusion";
    }

    @Override
    protected String getEditorDescription() {
        return "Select min. two organizational units, please.";
    }
}