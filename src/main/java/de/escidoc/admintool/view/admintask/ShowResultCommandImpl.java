package de.escidoc.admintool.view.admintask;

import java.util.Collection;

import com.vaadin.ui.Label;

import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;

final class ShowResultCommandImpl
    implements LoadExampleResourceViewImpl.ShowResultCommand {

    private final LoadExampleResourceViewImpl loadExampleResourceViewImpl;

    /**
     * @param loadExampleResourceViewImpl
     */
    ShowResultCommandImpl(final LoadExampleResourceViewImpl loadExampleResourceViewImpl) {
        this.loadExampleResourceViewImpl = loadExampleResourceViewImpl;
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
        loadExampleResourceViewImpl.getViewLayout().addComponent(
            new Label(entry.getMessage()));
    }
}