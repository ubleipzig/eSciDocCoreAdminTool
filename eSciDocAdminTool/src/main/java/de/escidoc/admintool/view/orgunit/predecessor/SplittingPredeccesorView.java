package de.escidoc.admintool.view.orgunit.predecessor;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

import de.escidoc.admintool.view.ViewConstants;

public class SplittingPredeccesorView extends CustomComponent {

    private static final long serialVersionUID = 3801050681139229927L;

    private final Panel mainLayout = new Panel();

    private final List<String> symbolList = new ArrayList<String>();

    public SplittingPredeccesorView(String predecessor,
        List<Object> newOrgUnitName) {

        fillSymbolList(newOrgUnitName.size());

        mainLayout.setWidth("400px");
        mainLayout.setSizeFull();
        FormLayout fl = new FormLayout();
        Label left =
            new Label("<span align=\"center\">" + predecessor + "</span>",
                Label.CONTENT_XHTML);
        fl.addComponent(left);
        int count = newOrgUnitName.size();
        for (int i = 0; i < count; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(symbolList.get(i));
            sb.append(newOrgUnitName.get(i));
            fl.addComponent(new Label(sb.toString(), Label.CONTENT_XHTML));
        }

        mainLayout.addComponent(fl);
        setCompositionRoot(mainLayout);
    }

    private void fillSymbolList(int size) {
        symbolList.add("&nbsp;&#9121;");
        for (int i = 1; i < size; i++) {
            if (i == size / 2) {
                symbolList.add(ViewConstants.DOWN_RIGHT_ARROW + "|");
            }
            else {
                symbolList.add("&nbsp;" + "|");
            }
        }
        symbolList.add("&nbsp;&#9123;");
    }
}
