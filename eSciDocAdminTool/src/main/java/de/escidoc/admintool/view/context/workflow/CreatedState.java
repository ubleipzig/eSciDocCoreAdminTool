package de.escidoc.admintool.view.context.workflow;

import com.vaadin.ui.Button;

public class CreatedState implements AbstractState {
    private final Button deleteContextBtn;

    private final Button openContextBtn;

    private final Button closeContextBtn;

    public CreatedState(Button deleteContextBtn, Button openContextBtn,
        Button closeContextBtn) {
        this.deleteContextBtn = deleteContextBtn;
        this.openContextBtn = openContextBtn;
        this.closeContextBtn = closeContextBtn;
    }

    public void changeState() {
        openContextBtn.setVisible(true);
        closeContextBtn.setVisible(false);
        deleteContextBtn.setVisible(true);
    }
}
