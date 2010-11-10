package de.escidoc.admintool.view.admintask;

import java.util.Collection;

import com.vaadin.ui.Label;

import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;

final class ShowResultCommandImpl
    implements LoadExampleViewImpl.ShowResultCommand {

    private final LoadExampleViewImpl loadExampleViewImpl;

    /**
     * @param loadExampleViewImpl
     */
    ShowResultCommandImpl(final LoadExampleViewImpl loadExampleViewImpl) {
        this.loadExampleViewImpl = loadExampleViewImpl;
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
        assert entry != null : "entry should not be null.";

        entry.getObjid();
        entry.getResourceType();
        // throw new UnsupportedOperationException("Not yet implemented");
    }

    private void showLoadedExamplesResult(final Entry entry) {
        loadExampleViewImpl.getViewLayout().addComponent(
            new Label(entry.getMessage()));
    }
}