/**
 * 
 */
package de.escidoc.admintool.view.util;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * @author ASP
 *
 */
public class LayoutHelper {
	
	/** Helper method. Puts a blank in front of a component.
	 * @param label The label in front of the controll.
	 * @param comp The component to display.
	 * @param width the fixed size of the label. The parameter has to be in css style, i.e. 400px for instance.
	 * @param required should it be marked with an asterix.
	 * @return The component in an horizontal layout. A blank in front and afterwards is inserted.
	 */
	public static synchronized HorizontalLayout create(String label, Component comp, String width, boolean required){
		HorizontalLayout hor = new HorizontalLayout();
		hor.addComponent(new Label(" "));
		String text = "<p align=\"right\">"+label+"</p>";
		Label l;
		hor.addComponent(l = new Label(text, Label.CONTENT_XHTML));
		l.setSizeUndefined();
		l.setWidth(width);
		hor.setComponentAlignment(l, Alignment.MIDDLE_RIGHT);
		if (required){
			hor.addComponent(new Label("&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>", Label.CONTENT_XHTML));
		} else{
			hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
		}
		hor.addComponent(comp);
		hor.setComponentAlignment(comp, Alignment.MIDDLE_RIGHT);
		hor.addComponent(new Label(" "));
		hor.setSpacing(false);
		return hor;
	}

	/** Helper method. Puts a blank in front of a component. Two labels with different size in front of two components.
	 * @param label The label in front of the control.
	 * @param comp The component to display.
	 * @param width the fixed size of the label. The parameter has to be in CSS style, i.e. 400px for instance.
	 * @param required should it be marked with an asterisk.
	 * @return The component in an horizontal layout. A blank in front and afterwards is inserted.
	 */
	public static synchronized HorizontalLayout create(String labelLeft, String labelRight, Component compLeft, Component compRight, String widthLeft, String widthRight, boolean required){
		HorizontalLayout hor = new HorizontalLayout();
		hor.addComponent(new Label(" "));
		String text = "<p align=\"right\">"+labelLeft+"</p>";
		Label ll, lr;
		hor.addComponent(ll = new Label(text, Label.CONTENT_XHTML));
		ll.setSizeUndefined();
		ll.setWidth(widthLeft);
		hor.setComponentAlignment(ll, Alignment.MIDDLE_RIGHT);
		if (required){
			hor.addComponent(new Label("&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>", Label.CONTENT_XHTML));
		} else{
			hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
		}
		hor.addComponent(compLeft);
		hor.setComponentAlignment(compLeft, Alignment.MIDDLE_RIGHT);
		hor.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
		String text2 = "<p align=\"right\">"+labelRight+"</p>";
		hor.addComponent(lr = new Label(text2, Label.CONTENT_XHTML));
		lr.setSizeUndefined();
		lr.setWidth(widthRight);
		hor.setComponentAlignment(lr, Alignment.MIDDLE_RIGHT);
		if (required){
			hor.addComponent(new Label("&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>", Label.CONTENT_XHTML));
		} else{
			hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
		}
		hor.addComponent(compRight);
		hor.setComponentAlignment(compRight, Alignment.MIDDLE_RIGHT);
		hor.addComponent(new Label(" "));
		hor.setSpacing(false);
		return hor;
	}


	/** Helper method. Puts two element with a label on the left in a symmetric way.
	 * @param label The label in front of the control.
	 * @param comp The component to display.
	 * @param width the fixed size of the label. The parameter has to be in CSS style, i.e. 400px for instance.
	 * @param required should it be marked with an asterisk.
	 * @return The component in an horizontal layout. A blank in front and afterwards is inserted.
	 */
	public static synchronized HorizontalLayout create(String labelLeft, String labelRight, Component compLeft, Component compRight, String width, boolean required){
		HorizontalLayout hor = new HorizontalLayout();
		hor.addComponent(new Label(" "));
		String text = "<p align=\"right\">"+labelLeft+"</p>";
		Label ll, lr;
		hor.addComponent(ll = new Label(text, Label.CONTENT_XHTML));
		ll.setSizeUndefined();
		ll.setWidth(width);
		hor.setComponentAlignment(ll, Alignment.MIDDLE_RIGHT);
		if (required){
			hor.addComponent(new Label("&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>", Label.CONTENT_XHTML));
		} else{
			hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
		}
		hor.addComponent(compLeft);
		hor.setComponentAlignment(compLeft, Alignment.MIDDLE_RIGHT);
		hor.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
		String text2 = "<p align=\"right\">"+labelRight+"</p>";
		hor.addComponent(lr = new Label(text2, Label.CONTENT_XHTML));
		lr.setSizeUndefined();
		lr.setWidth(width);
		hor.setComponentAlignment(lr, Alignment.MIDDLE_RIGHT);
		if (required){
			hor.addComponent(new Label("&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>", Label.CONTENT_XHTML));
		} else{
			hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
		}
		hor.addComponent(compRight);
		hor.setComponentAlignment(compRight, Alignment.MIDDLE_RIGHT);
		hor.addComponent(new Label(" "));
		hor.setSpacing(false);
		return hor;
	}

	/** Helper method. Puts a blank in front of a component. Useful for buttons.
	 * @param comp The component to display.
	 * @return The component in an grid layout. A blank in front and afterwards is inserted.
	 */
	public static synchronized GridLayout create(Component comp){
		GridLayout hor = new GridLayout(3, 1);
		hor.addComponent(new Label(" "), 0, 0);
		hor.addComponent(comp, 1, 0);
		hor.setComponentAlignment(comp, Alignment.MIDDLE_RIGHT);
		hor.addComponent(new Label(" "), 2, 0);
		hor.setSpacing(false);
		return hor;
	}
	
}
