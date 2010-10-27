package de.escidoc.admintool.view.admintask;

import java.util.Map;
import java.util.Map.Entry;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

final class ShowRepoInfoCommandImpl
    implements RepositoryInfoView.ShowRepoInfoCommand {
    private final FormLayout formLayout = new FormLayout();

    private final RepositoryInfoView repositoryInfoView;

    /**
     * @param repositoryInfoView
     */
    ShowRepoInfoCommandImpl(final RepositoryInfoView repositoryInfoView) {
        this.repositoryInfoView = repositoryInfoView;
    }

    @Override
    public void execute(final Map<String, String> repoInfos) {
        repositoryInfoView.getViewLayout().addComponent(formLayout);
        formLayout.removeAllComponents();

        for (final Entry<String, String> entry : repoInfos.entrySet()) {
            formLayout.addComponent(createReadOnlyField(entry));
        }
    }

    private TextField createReadOnlyField(final Entry<String, String> entry) {
        final TextField textField = new TextField();
        textField.setCaption(entry.getKey());
        textField.setValue(entry.getValue());
        textField.setWidth(400, RepositoryInfoView.UNITS_PIXELS);
        textField.setReadOnly(true);
        return textField;
    }
}