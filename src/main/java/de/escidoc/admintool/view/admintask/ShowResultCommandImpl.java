package de.escidoc.admintool.view.admintask;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Label;

import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;

final class ShowResultCommandImpl
    implements LoadExampleResourceViewImpl.ShowResultCommand {

    private final AddToContainer addToContainer;

    private final LoadExampleResourceViewImpl loadExampleResourceViewImpl;

    private AddToContainer addToContextContainer;

    ShowResultCommandImpl(
        final LoadExampleResourceViewImpl loadExampleResourceViewImpl,
        final AddToContainer addExampleCommand) {
        this.loadExampleResourceViewImpl = loadExampleResourceViewImpl;
        addToContainer = addExampleCommand;
    }

    @Override
    public void execute(final Collection<?> entries) {
        for (final Object entry : entries) {
            if (entry instanceof Entry) {
                updateContainers((Entry) entry);
                showLoadedExamplesResult((Entry) entry);
            }
        }
    }

    private void updateContainers(final Entry entry) {
        Preconditions.checkNotNull(entry, "entry is null: %s", entry);
        if (entry.getResourceType() == null) {
            return;
        }
        switch (entry.getResourceType()) {
            case OrganizationalUnit:
                addToContainer.execute(entry);
                break;
            case Context:
                addToContextContainer.execute(entry);
                break;

        }
    }

    private void showLoadedExamplesResult(final Entry entry) {
        loadExampleResourceViewImpl.getViewLayout().addComponent(
            new Label(entry.getMessage()));
    }
}