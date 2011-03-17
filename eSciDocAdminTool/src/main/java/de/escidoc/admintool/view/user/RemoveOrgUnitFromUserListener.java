package de.escidoc.admintool.view.user;

import java.util.Set;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;

public final class RemoveOrgUnitFromUserListener
    implements Button.ClickListener {
    private static final long serialVersionUID = -93040754103228754L;

    private final AbstractSelect orgUnitTable;

    public RemoveOrgUnitFromUserListener(final Table orgUnitTable) {
        this.orgUnitTable = orgUnitTable;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Object value = orgUnitTable.getValue();
        if (value instanceof Set<?>) {
            for (final Object object : (Set<?>) orgUnitTable.getValue()) {
                orgUnitTable.removeItem(object);
            }
        }

    }
}