/**
 * 
 */
package de.escidoc.admintool.view.orgunit.editor;

import java.util.List;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Window;

/**
 * @author ASP
 * 
 */
public interface IPredecessorEditor {
    public Window getWidget();

    public void setParent(final Window parent);

    public void setList(final ListSelect select);

    public List<Object> getSelected();

    public void setPredecessorResult(AbstractComponent predecessor);

    public void setPredecessorLayout(HorizontalLayout predecessorLayout);

    public void replacePredecessorView(AbstractComponent predecessor);

}
