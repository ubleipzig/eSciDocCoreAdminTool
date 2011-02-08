package de.escidoc.admintool.view.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.admintool.view.util.LayoutHelper;

public class OrgUnitWidgetImpl extends CustomComponent implements OrgUnitWidget {

    private static final int _90 = 90;

    private static final long serialVersionUID = -7422123786660017161L;

    private final Table orgUnitTable = new Table();

    private Button addOrgUnitButton;

    private Button removeOrgUnitButton;

    public OrgUnitWidgetImpl() {
    }

    @Override
    public void addTable() {
        orgUnitTable.setHeight(ViewConstants.DEFAULT_LABEL_WIDTH, UNITS_PIXELS);
        orgUnitTable.setWidth(300, UNITS_PIXELS);
        orgUnitTable.setSelectable(true);
        orgUnitTable.setNullSelectionAllowed(true);
        orgUnitTable.setMultiSelect(true);
        orgUnitTable.setImmediate(true);
        orgUnitTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);

        final POJOContainer<ResourceRefDisplay> orgUnitContainer =
            new POJOContainer<ResourceRefDisplay>(ResourceRefDisplay.class,
                "objectId", "title");
        orgUnitTable.setContainerDataSource(orgUnitContainer);

        orgUnitTable.setVisibleColumns(new String[] { "title", "objectId" });
        orgUnitTable.setColumnHeaders(new String[] { "Title", "Object ID" });

    }

    public Table getTable() {
        return orgUnitTable;
    }

    @Override
    public OrgUnitWidget withAddButton() {
        addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);
        return this;
    }

    @Override
    public OrgUnitWidget withRemoveButton() {
        removeOrgUnitButton = new Button(ViewConstants.REMOVE_LABEL);
        return this;
    }

    public VerticalLayout build() {

        final Button[] footers =
            new Button[] { addOrgUnitButton, removeOrgUnitButton };

        for (final Button button : footers) {
            button.setStyleName(Reindeer.BUTTON_SMALL);
        }
        return LayoutHelper.create("", orgUnitTable, 0, _90, false, footers);

    }

    @Override
    public void addListener(final ClickListener clickListener) {
        addOrgUnitButton.addListener(clickListener);
    }

    @Override
    public Set<ResourceRefDisplay> getOrgUnitsFromTable() {
        final Set<ResourceRefDisplay> selectedSet =
            new HashSet<ResourceRefDisplay>();

        final Collection<?> itemIds = orgUnitTable.getItemIds();

        for (final Object objectId : itemIds) {
            if (objectId instanceof ResourceRefDisplay) {
                selectedSet.add((ResourceRefDisplay) objectId);
            }
        }
        return selectedSet;
    }

    @Override
    public void withListener(final ClickListener clickListener) {
        removeOrgUnitButton.addListener(clickListener);
    }

    @Override
    public Set<ResourceRefDisplay> getSelectedOrgUnits() {
        final Set<ResourceRefDisplay> selected =
            new HashSet<ResourceRefDisplay>();
        final Object value = orgUnitTable.getValue();
        if (value instanceof Set<?>) {
            final Set<?> valueAsSet = (Set<?>) value;
            for (final Object object : valueAsSet) {
                if (object instanceof ResourceRefDisplay) {
                    selected.add((ResourceRefDisplay) object);
                }
            }
            return selected;
        }
        else if (value instanceof ResourceRefDisplay) {
            selected.add((ResourceRefDisplay) value);
            return selected;
        }
        throw new UnsupportedOperationException("Unknown type: "
            + value.getClass());
    }
}
