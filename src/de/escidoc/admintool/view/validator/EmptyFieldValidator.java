/**
 * 
 */
package de.escidoc.admintool.view.validator;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.TextField;

/**
 * @author ASP
 *
 */
public class EmptyFieldValidator {

	public static synchronized boolean isValid(TextField comp, String message){
    	if (!(comp.getValue() != null && ((String)comp.getValue()).trim().length()>0)){
    		comp.setComponentError(null);
    		comp.setComponentError(new UserError(message));
    		return false;
    	}
    	comp.setComponentError(null);
		return true;
	}
}
