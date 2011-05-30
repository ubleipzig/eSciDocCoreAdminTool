package de.escidoc.admintool.view.admintask;

import java.util.Collection;

import com.vaadin.ui.Label;

import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;

final class ShowResultCommandImpl implements LoadExampleResourceViewImpl.ShowResultCommand {

    private final LoadExampleResourceViewImpl loadExampleResourceViewImpl;

    ShowResultCommandImpl(final LoadExampleResourceViewImpl loadExampleResourceViewImpl,
        final AddToContainer addExampleCommand) {
        this.loadExampleResourceViewImpl = loadExampleResourceViewImpl;
    }

    @Override
    public void execute(final Collection<?> entries) {
        for (final Object entry : entries) {
            if (entry instanceof Entry) {
                showLoadedExamplesResult((Entry) entry);
            }
        }
    }

    private void showLoadedExamplesResult(final Entry entry) {
        loadExampleResourceViewImpl.getViewLayout().addComponent(new Label(entry.getMessage()));
    }
}