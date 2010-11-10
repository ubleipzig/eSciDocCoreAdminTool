package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class ResourceEditViewImpl extends CustomComponent
    implements ResourceEditView {

    private static final String EDIT_RESOURCE = "Edit Resource";

    private static final long serialVersionUID = -7860622778922198542L;

    private final VerticalLayout layout = new VerticalLayout();

    private final ResourceToolbar resourceToolbar;

    private final FolderHeader header = new FolderHeaderImpl(EDIT_RESOURCE);

    private final PropertiesFields fields = new PropertiesFieldsImpl();

    private final AddSaveAndCancelButtons footers =
        new AddSaveAndCancelButtons();

    public ResourceEditViewImpl(final ResourceViewImpl resourceViewImpl) {
        Preconditions.checkNotNull(resourceViewImpl,
            "resourceViewImpl is null: %s", resourceViewImpl);

        resourceToolbar = new ResourceToolbar(resourceViewImpl);
        setCompositionRoot(layout);
        buildView();
    }

    private void buildView() {
        layout.setHeight(100, UNITS_PERCENTAGE);
        layout.addComponent(header);
        layout.addComponent(resourceToolbar);
        layout.addComponent(fields);
        addSaveAndCancelButtons();
    }

    private void addSaveAndCancelButtons() {
        footers.setOkButtonListener(new OkButtonListener(fields.getAllFields(),
            null));

        layout.addComponent(footers);
        layout.setComponentAlignment(footers, Alignment.BOTTOM_RIGHT);
    }

    @Override
    public void bind(final Item item) {
        fields.bind(item);
    }
}