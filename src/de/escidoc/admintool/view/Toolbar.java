/**
 * 
 */
package de.escidoc.admintool.view;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * @author ASP
 *
 */
public class Toolbar {
	
    public static synchronized GridLayout createToolbar(final Window window//, final Button[] button) {
    		, final Button logoutButton){
    	
      	GridLayout layout = new GridLayout(3, 1);
  		layout.setDebugId("dashboard");

  		StringBuilder sb = new StringBuilder();
		sb.append("var host = window.location.host;");
		sb.append("var port = window.location.port;");
		sb.append("var element = document.getElementById('dashboard');");
		sb.append("element.style.backgroundImage = url(host+port+'/AdminTool/VAADIN/themes/contacts/images/escidoc-logo.jpg');");
		sb.append("alert(run);");
		sb.append("element.style.backgroundRepeat='no-repeat';");
		
		window.executeJavaScript(sb.toString());
    	
    	
//      final HorizontalLayout layout = new HorizontalLayout();
  		window.executeJavaScript("document.getElementById('dashboard').style.backgroundImage = \"url('http://localhost:" 
      			+"8181/AdminTool/VAADIN/themes/contacts/images/escidoc-logo.jpg')\";");
//  		window.executeJavaScript("document.getElementById('dashboard').style.backgroundRepeat=\"no-repeat\";");
//      	getMainWindow().executeJavaScript("document.getElementById('dashboard').style.background-size=\"100%\";");
      	


//        Label l = new Label("&nbsp;", Label.CONTENT_XHTML);
//        l.setWidth("10px");
//        layout.addComponent(l);
//        layout.setExpandRatio(l, 1);

      layout.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML), 0, 0);
      layout.addComponent(logoutButton, 1, 0);
      layout.setComponentAlignment(logoutButton, Alignment.MIDDLE_CENTER);

      layout.setMargin(true);
      layout.setSpacing(true);

      layout.setStyleName("toolbar");

      layout.setWidth("100%");
      return layout;
  }

}
