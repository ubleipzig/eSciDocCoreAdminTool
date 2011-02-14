package de.escidoc.admintool.view.resource;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.view.ViewConstants;

public class SaveAndCancelButtons extends CustomComponent {
    private static final long serialVersionUID = 5031656063216982470L;

    final HorizontalLayout hl = new HorizontalLayout();

    private final HorizontalLayout footers = new HorizontalLayout();

    private final Button cancelBtn = new Button(ViewConstants.CANCEL);

    private final Button saveBtn = new Button(ViewConstants.SAVE);

    public SaveAndCancelButtons() {
        setCompositionRoot(footers);
        footers.setWidth(100, UNITS_PERCENTAGE);

        hl.addComponent(saveBtn);
        hl.addComponent(cancelBtn);
        footers.addComponent(hl);
        footers.setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
    }

    public void setOkButtonListener(
        final ResourceBtnListener resourceBtnListener) {
        saveBtn.addListener(resourceBtnListener);
    }

    public void setCancelButtonListener(
        final CancelButtonListener cancelButtonListener) {
        cancelBtn.addListener(cancelButtonListener);
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }
}