package de.escidoc.admintool.view.context.workflow;

import com.vaadin.ui.Button;

public class ClosedState implements AbstractState {
    private final Button deleteContextBtn;

    private final Button openContextBtn;

    private final Button closeContextBtn;

    public ClosedState(Button deleteContextBtn, Button openContextBtn,
        Button closeContextBtn) {
        this.deleteContextBtn = deleteContextBtn;
        this.openContextBtn = openContextBtn;
        this.closeContextBtn = closeContextBtn;
    }

    public void changeState() {
        openContextBtn.setVisible(false);
        closeContextBtn.setVisible(false);
        deleteContextBtn.setVisible(false);
    }

}
