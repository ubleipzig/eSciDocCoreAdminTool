package de.escidoc.admintool.view.admintask;

import java.util.Map;

import com.vaadin.ui.Button;

import de.escidoc.admintool.view.ViewConstants;

public class RepositoryInfoView extends AbstractCustomView {
    private static final long serialVersionUID = -7206908685980457887L;

    private final Button repoInfoButton = new Button(
        ViewConstants.SHOW_REPOSITORY_INFO);

    private final RepoInfoClickListener listener;

    public RepositoryInfoView(final RepoInfoClickListener listener) {
        super();
        this.listener = listener;
        init();
    }

    private void init() {
        addGetRepositoryInfoButton();
    }

    private void addGetRepositoryInfoButton() {
        repoInfoButton.setWidth(150, UNITS_PIXELS);
        getViewLayout().addComponent(repoInfoButton);
        addListener();
    }

    interface ShowRepoInfoCommand {
        void execute(Map<String, String> repoInfos);
    }

    private void addListener() {
        final ShowRepoInfoCommand command = new ShowRepoInfoCommandImpl(this);
        listener.setCommand(command);
        repoInfoButton.addListener(listener);
    }
}