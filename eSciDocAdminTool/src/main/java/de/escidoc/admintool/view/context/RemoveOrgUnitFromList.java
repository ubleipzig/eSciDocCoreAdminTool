package de.escidoc.admintool.view.context;

import java.util.Set;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ListSelect;

final class RemoveOrgUnitFromList implements ClickListener {

    private final ListSelect orgUnitList;

    RemoveOrgUnitFromList(final ListSelect orgUnitList) {
        this.orgUnitList = orgUnitList;
    }

    private static final long serialVersionUID = 5568510941842372569L;

    @Override
    public void buttonClick(final ClickEvent event) {
        final Object o = orgUnitList.getValue();

        if (o instanceof Set) {
            final Set set = (Set) o;
            for (final Object ob : set) {
                orgUnitList.removeItem(ob);
            }
        }
        else if (o != null) {
            orgUnitList.removeItem(o);
        }
    }
}