package de.escidoc.admintool.view.orgunit;

import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.view.ViewConstants;

@SuppressWarnings("serial")
public class OrgUnitView extends SplitPanel {

    public OrgUnitView(final OrgUnitList orgUnitTable,
        final OrgUnitEditForm orgUnitEditForm) {

        this.addStyleName("view");
        this.setFirstComponent(orgUnitTable);
        this.setSecondComponent(orgUnitEditForm);
        this.setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        this.setOrientation(ORIENTATION_HORIZONTAL);
    }
}