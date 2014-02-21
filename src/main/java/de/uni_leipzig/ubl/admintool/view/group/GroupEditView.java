package de.uni_leipzig.ubl.admintool.view.group;

import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class GroupEditView extends VerticalLayout {

	private final GroupEditForm groupEditForm;
	
	public GroupEditView(final GroupEditForm grpEditForm) {
		groupEditForm = grpEditForm;
		buildUI();
	}
	
	private void buildUI() {
		addStyleName("view");
		addComponent(groupEditForm);
	}
	
	public void setSelected(final Item item) {
		groupEditForm.setSelected(item);
	}
	
}
