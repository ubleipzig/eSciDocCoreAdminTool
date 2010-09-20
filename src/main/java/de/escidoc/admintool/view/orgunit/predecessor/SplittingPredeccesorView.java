package de.escidoc.admintool.view.orgunit.predecessor;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.view.ViewConstants;

public class SplittingPredeccesorView extends AbstractPredecessorView {

    private static final long serialVersionUID = 3801050681139229927L;

    private final Panel mainLayout = new Panel();

    private final List<String> symbolList = new ArrayList<String>();

    public SplittingPredeccesorView(final String predecessor,
        final List<Object> newOrgUnitName) {

        fillSymbolList(newOrgUnitName.size());

        mainLayout.setWidth("400px");
        mainLayout.setSizeFull();
        final VerticalLayout layout = new VerticalLayout();
        final Label left = new Label(predecessor, Label.CONTENT_XHTML);
        layout.addComponent(left);
        final int count = newOrgUnitName.size();

        layout.addComponent(new Label("| &nbsp;&#9121;", Label.CONTENT_XHTML));
        for (int i = 0; i < count; i++) {
            final StringBuilder sb = new StringBuilder();
            sb.append(symbolList.get(i));
            sb.append(newOrgUnitName.get(i));
            layout.addComponent(new Label(sb.toString(), Label.CONTENT_XHTML));
        }
        layout.addComponent(new Label("  &nbsp;&#9123;", Label.CONTENT_XHTML));

        mainLayout.addComponent(layout);
        setCompositionRoot(mainLayout);
    }

    private void fillSymbolList(final int size) {
        boolean with = false;
        for (int i = 0; i < size; i++) {
            if (i == size / 2) {
                if (!with) {
                    with = true;
                }
                symbolList.add(ViewConstants.DOWN_RIGHT_ARROW + "  &nbsp; "
                    + "|");
            }
            else {
                if (!with) {
                    symbolList.add("| &nbsp; |");
                }
                else {
                    symbolList.add("  &nbsp; |");
                }
            }
        }
    }
}
