/*******************************************************************************
 * 
 * Copyright:
 * 
 * License:
 * 
 * Authors:
 * 
 ******************************************************************************/

/*******************************************************************************
 * 
 * #asset(admintoolclient/*)
 * 
 ******************************************************************************/

/**
 * This is the main application class of your custom application
 * "AdminToolClient"
 */
qx.Class.define("org.escidoc.admintool.Application", {
	extend : qx.application.Standalone,

	/*
	 * ****************************************************************************
	 * MEMBERS
	 * ****************************************************************************
	 */

	members : {
		/**
		 * This method contains the initial application code and gets called
		 * during startup of the application
		 * 
		 * @lint ignoreDeprecated(alert)
		 */
		main : function() {
			// Call super class
			this.base(arguments);
			this.enableLogging();
			this.showListFormAndLabelBinding();
		},
		enableLogging : function() {
			// Enable logging in debug variant
			if (qx.core.Variant.isSet("qx.debug", "on")) {
				// support native logging capabilities, e.g. Firebug for
				// Firefox
				qx.log.appender.Native;
				// support additional cross-browser console. Press F7 to
				// toggle visibility
				qx.log.appender.Console;
			}
		},
		showListFormAndLabelBinding : function() {
			// create the UI.
			// create a group box
			var groupBox = new qx.ui.groupbox.GroupBox("Simple Form");
			// set group box layout to vertical box.
			groupBox.setLayout(new qx.ui.layout.VBox(5));
			this.getRoot().add(groupBox, {
						left : 10,
						top : 10
					});

			// create the input form.
			var inputForm = new qx.ui.form.Form();
			inputForm.addGroupHeader("Registration");

			// add one form item i.e. a text field.
			var nameTextField = new qx.ui.form.TextField();
			nameTextField.setRequired(true);
			nameTextField.setWidth(200);

			inputForm.add(nameTextField, "Name", null, "name");

			// email field
			var emailTextField = new qx.ui.form.TextField();
			// emailTextField.setRequired(true);
			emailTextField.setWidth(100);
			// inputForm.add(emailTextField, "E-mail", qx.util.Validate.email(),
			// "email");
			inputForm.add(emailTextField, "E-mail", null, "email");

			var resetButton = new qx.ui.form.Button("Reset");
			resetButton.setWidth(70);
			inputForm.addButton(resetButton);

			var saveButton = new qx.ui.form.Button("Save");
			saveButton.setWidth(70);
			inputForm.addButton(saveButton);

			var debugButton = new qx.ui.form.Button("Debug");
			debugButton.setWidth(70);
			inputForm.addButton(debugButton);
			debugButton.addListener("execute", function() {
						this.debug("array wrapper now: "
								+ qx.util.Serializer.toJson(arrayWrapper))
					}, this);

			groupBox.add(new qx.ui.form.renderer.Single(inputForm));

			// create the output list
			var outputList = new qx.ui.form.List();
			outputList.setWidth(240);
			groupBox.add(outputList);

			// create a details view of an object model.
			var detailsView = new qx.ui.basic.Label("Details");
			groupBox.add(detailsView);

			// binding selected item in the list with details view.
			// user selects an item, details view shows its property.
			// for example: the user selects item "Anna", details view shows
			// name: Anna, email:x.y@example.com

			// we can already binds selecte item with button.
			// we get the model from the item view.
			// details view update selection.

			// when nothing is selected, nothing is showed.

			// intial ideas, we can add a listener to details view widget that
			// listen to event thrown by list.
			// which event data then?

			// detailsView.addListener("changeSelection", function() {
			// this.debug("list item selected");
			// }, this);

			// better way, use data binding: Object data binding.
			// create the controller

			var deleteButton = new qx.ui.form.Button("Delete");
			groupBox.add(deleteButton);

			// an array of raw data containing 2 objects.
			var rawData = [{
						"name" : "John",
						"email" : "john.dalton@example.com"
					}, {
						"name" : "Bill",
						"email" : "bill.joe@example.com"
					}];

			var arrayWrapper = qx.data.marshal.Json.createModel(rawData);

			// create the controller for list
			var listController = new qx.data.controller.List(arrayWrapper,
					outputList, "name");

			listController.bind("selection[0].name", detailsView, "value");

			// add behaviour to save Button.
			saveButton.addListener("execute", function() {
						if (inputForm.validate()) {
							arrayWrapper.push(qx.data.marshal.Json.createModel(
									{
										name : nameTextField.getValue(),
										email : emailTextField.getValue()
									}));
							inputForm.reset();
						}
					}, this);

			resetButton.addListener("execute", function() {
						inputForm.reset()
					}, this);
			// Update
			var editButton = new qx.ui.form.Button("Edit");
			groupBox.add(editButton);

			editButton.addListener("execute", function() {
				if (arrayWrapper.getLength() === 0) {
					this.debug("No item in the list.");
					return;
				}
				var selectedItem = listController.getSelection().getItem(0);
				if (selectedItem == null) {
					this.debug("Please select item to edit!");
					return;
				}
				this.debug("Editing this: "
						+ qx.dev.Debug.debugProperties(selectedItem));

				// Q1: How to create a form with existing values from
				// object
				// properties?
				// Q2: what should happens in the background?
				// A2:
				// 1. selectedItem is a model or object.
				// 2. we create a new form and fills it with the
				// properties
				// of this object.
				//
				// the form contains a save, cancel button.
				// if the user click save, array model will be updated.
				// Cancel, form will be closed? really? or just reset?
				//
				// @Note: Actually we can reuse the input form, however
				// for
				// further case we want to create in a tab page.
				// 
				// 3. how do we update the array wrapper?
				// @see. qx.data.controller.List.getSelection()
				// maybe we just manipulate the model, and store it
				// again.

				// there is certainly to do this.
				// nameTextField.setValue(selectedItem.getName());
				// emailTextField.setValue(selectedItem.getEmail());
				var editForm = new qx.ui.form.Form();
				editForm.addGroupHeader("Editing");

				// add one form item i.e. a text field.
				var nameTextFieldInEditForm = new qx.ui.form.TextField(selectedItem
						.getName());
				nameTextFieldInEditForm.setRequired(true);
				nameTextFieldInEditForm.setWidth(200);

				editForm.add(nameTextFieldInEditForm, "Name", null, "name");

				// email field
				var emailTextFieldInEditForm = new qx.ui.form.TextField(selectedItem
						.getEmail());
				// emailTextFieldInEditForm.setRequired(true);
				emailTextFieldInEditForm.setWidth(100);
				// editForm.add(emailTextFieldInEditForm, "E-mail",
				// qx.util.Validate.email(),
				// "email");
				editForm.add(emailTextFieldInEditForm, "E-mail", null, "email");

				var resetButton = new qx.ui.form.Button("Cancel");
				resetButton.setWidth(70);
				editForm.addButton(resetButton);

				var saveButton = new qx.ui.form.Button("Save");
				saveButton.setWidth(70);
				editForm.addButton(saveButton);

				var debugButton = new qx.ui.form.Button("Debug");
				debugButton.setWidth(70);
				editForm.addButton(debugButton);
				debugButton.addListener("execute", function() {
							this.debug("array wrapper now: "
									+ qx.util.Serializer.toJson(arrayWrapper))
						}, this);

				var renderedEditForm = new qx.ui.form.renderer.Single(editForm);
				groupBox.add(renderedEditForm);
				// add behaviour to save Button.
				saveButton.addListener("execute", function() {
							if (editForm.validate()) {
								// replace item with a new one.
								selectedItem.setName(nameTextFieldInEditForm
										.getValue());
								selectedItem.setEmail(emailTextFieldInEditForm
										.getValue());
								// reset the edit form
								editForm.reset();
								// close edit form
								groupBox.remove(renderedEditForm);
							}
						}, this);

				resetButton.addListener("execute", function() {
							// REFACTOR: extract method, duplicates with save
							// button in edit form.
							editForm.reset();
							groupBox.remove(renderedEditForm);
						}, this);
			}, this)

			// Delete
			deleteButton.addListener("execute", function() {
						// REFACTOR: extract method, duplicate in editButton.
						if (arrayWrapper.getLength() === 0) {
							this.debug("No item in the list.");
							return;
						}
						var selectedItem = listController.getSelection()
								.getItem(0);

						if (selectedItem == null) {
							this.debug("Please select item to delete!");
							return;
						}
						this.debug("delete this: "
								+ qx.dev.Debug.debugProperties(selectedItem));

						arrayWrapper.remove(selectedItem);
					}, this)
		}
	}
});