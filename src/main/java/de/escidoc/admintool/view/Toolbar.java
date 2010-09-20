/**
 * 
 */
package de.escidoc.admintool.view;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Window;

/**
 * @author ASP
 * 
 */
public class Toolbar {

    public static synchronized GridLayout createToolbar(
        final Window window, final Button[] buttons) {
        final GridLayout layout = new GridLayout(3, 1);
        layout.setDebugId("dashboard");
        final StringBuilder sb = new StringBuilder();
        sb.append("var host = window.location.host; ");
        sb.append("var port = window.location.port; ");
        sb.append("var element = document.getElementById('dashboard'); ");
        sb
            .append("element.style.backgroundImage = url(host+port+'/AdminTool/VAADIN/themes/contacts/images/escidoc-logo.jpg'); ");
        sb.append("alert(run); ");
        sb.append("element.style.backgroundRepeat='no-repeat'; ");
        window.executeJavaScript(sb.toString());
        window
            .executeJavaScript("document.getElementById('dashboard').style.backgroundImage = \"url('http://localhost:"
                + "8181/AdminTool/VAADIN/themes/contacts/images/escidoc-logo.jpg')\";");
        final ThemeResource imageResource =
            new ThemeResource("images/SchriftLogo.jpg");
        final Embedded embedded = new Embedded("", imageResource);
        layout.addComponent(embedded, 0, 0);
        for (final Button button : buttons) {
            layout.addComponent(button, 2, 0);
            layout.setComponentAlignment(button, Alignment.MIDDLE_RIGHT);
        }
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.setStyleName("toolbar");
        layout.setWidth("100%");
        return layout;
    }
}