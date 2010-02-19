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
			/*
			 * enableLogging() is an instance function, must be called with
			 * this.x()
			 */
			this.enableLogging();
			// this.createWidgets();
			// this.testLoadingDataFromStore();
			// this.createJsonToList();
			// this.createTwoSlidersBindedWithEachOthers();
			// this.createSimpleList();
			// this.createListBindWithObject();
			// this.createListControllerWithObjects();
			// this.createSliderWithValidation();
			// this.showListAndFormBindingUsingMartinSuggestion();
			// this.showListAndFormBindingWith2FormItems();
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
		},
		initData : function() {
			var userAccountsRawDataAsArray = [{
						"loginName" : "Max",
						"name" : "Max M.",
						"email" : "max.m@example.com"
					}, {
						"loginName" : "B",
						"name" : "B",
						"email" : "b.b@example.com"
					}];
		},
		s : function() {

			// create the UI.
			// create a group box
			var groupBox = new qx.ui.groupbox.GroupBox("Simple Form");
			// set group box layout to canvas.
			// TODO: what is canvas?
			groupBox.setLayout(new qx.ui.layout.VBox(5));
			// always directtly add widget to its parent.
			this.getRoot().add(groupBox);
			// create the input form.
			var inputForm = new qx.ui.form.Form();
			inputForm.addGroupHeader("Personal Information");

			// add one form item i.e. a text field.
			var nameTextField = new qx.ui.form.TextField();
			// name is required.
			// @question: what happens if set to false?
			// @answers: if set to true, the validation manager check if the
			// value is empty or not.
			nameTextField.setRequired(true);
			// @question:can we set the width in the constructor?
			nameTextField.setWidth(200);

			inputForm.add(nameTextField, "Name:", null, "name");

			// age field
			var age = new qx.ui.form.TextField();
			age.setRequired(true);
			age.setWidth(100);
			// add form validation
			inputForm.add(age, "Age:", null, "age");

			// add reset button. The order when a widget is add to
			// its parent determines the order of the layout.
			var resetButton = new qx.ui.form.Button("Reset");
			resetButton.setWidth(70);
			inputForm.addButton(resetButton);
			// Create and add the save button.
			var saveButton = new qx.ui.form.Button("Save");
			saveButton.setWidth(70);
			inputForm.addButton(saveButton);

			var debugButton = new qx.ui.form.Button("Debug");
			debugButton.setWidth(70);
			inputForm.addButton(debugButton);
			debugButton.addListener("execute", function() {
						this.debug("content of the array are: "
								+ qx.util.Serializer.toJson(arrayWrapper))
					}, this);

			groupBox.add(new qx.ui.form.renderer.Single(inputForm));

			// create the output list
			var outputList = new qx.ui.form.List();
			outputList.setWidth(240);

			// add the list to the root document
			groupBox.add(outputList);

			// create a details view of an object model.
			var detailsView = new qx.ui.basic.Label();
			groupBox.add(detailsView);

			var rawData = {
				"name" : "Anna",
				"age" : 29
			};

			var personModel = qx.data.marshal.Json.createModel(rawData);
			this.debug("person created: "
					+ qx.dev.Debug.debugProperties(personModel));

			// create a Qooxdoo array.
			var arrayWrapper = new qx.data.Array();

			// add person object to the array.
			arrayWrapper.push(personModel);

			// create the controller for list
			var listController = new qx.data.controller.List(arrayWrapper,
					outputList, "name");

			detailsView.set({
						value : "",
						rich : true,
						width : 200
					});

			// add behaviour to the save button, if the user clicks it.
			saveButton.addListener("execute", function() {
						// validate input form .
						if (inputForm.validate()) {
							// create the form controller.
							var formController = new qx.data.controller.Form(
									null, inputForm);
							// create the model from the form, and add it to the
							// Qooxdoo array.
							arrayWrapper.push(formController.createModel());
							// after that, the form controller is reset. No more
							// binding.
							formController.resetModel();
						}
					}, this);

			// create the form resetter.
			var resetter = new qx.ui.form.Resetter();
			// add form item to reset.
			resetter.add(nameTextField);
			resetButton.addListener("execute", function() {
						resetter.reset()
					}, this);

			// selection

			// TODO: consider the possibility for label to listening this event,
			// and update itself.
			outputList.addListener("changeSelection", function(event) {
						var selectedObject = listController.getSelection()
								.getItem(0);
						this.debug("selected: "
								+ qx.dev.Debug.debugProperties(selectedObject));
						detailsView.set({
									value : "<b>Name: </b>"
											+ selectedObject.getName()
											+ "<br/><b>Age: </b>"
											+ selectedObject.getAge(),
									rich : true,
									width : 120
								});
					}, this);

			var deleteButton = new qx.ui.form.Button("Delete");
			groupBox.add(deleteButton);
			deleteButton.addListener("execute", function() {
						var toBeDeleted = listController.getSelection()
								.getItem(0);
						this.debug("delete this: "
								+ qx.dev.Debug.debugProperties(toBeDeleted));
						arrayWrapper.remove(toBeDeleted);
					}, this)

		},
		showListAndFormBindingWith2FormItems : function() {
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
			emailTextField.setRequired(true);
			emailTextField.setWidth(100);
			inputForm.add(emailTextField, "E-mail", qx.util.Validate.email(),
					"email");

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

			// add behaviour to save Button.
			saveButton.addListener("execute", function() {
						if (inputForm.validate()) {
							arrayWrapper.push(qx.data.marshal.Json.createModel(
									{
										name : nameTextField.getValue(),
										email : emailTextField.getValue()
									}));
						}
					}, this);

			resetButton.addListener("execute", function() {
						inputForm.reset()
					}, this);
		},
		showListAndFormBindingUsingMartinSuggestion : function() {
			// create the UI.
			// create a group box
			var groupBox = new qx.ui.groupbox.GroupBox("Simple Form");
			// set group box layout to canvas.
			groupBox.setLayout(new qx.ui.layout.Canvas());
			this.getRoot().add(groupBox, {
						left : 10,
						top : 10
					});
			// create the input form.
			var inputForm = new qx.ui.form.Form();
			inputForm.addGroupHeader("Personal Information");

			// add one form item i.e. a text field.
			var nameTextField = new qx.ui.form.TextField();
			nameTextField.setRequired(true);
			nameTextField.setWidth(200);

			inputForm.add(nameTextField, "Name:", null, "name");

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

			// add the list to the root document
			groupBox.add(outputList, {
						left : 10,
						top : 100
					});

			// raw data: array of objects.
			// an array of raw data containing 2 objects.
			var rawData = [{
						"name" : "John"
					}, {
						"name" : "Bill"
					}];

			var arrayWrapper = qx.data.marshal.Json.createModel(rawData);

			// create the controller for list
			var listController = new qx.data.controller.List(arrayWrapper,
					outputList, "name");

			// add behaviour to save Button.
			saveButton.addListener("execute", function() {
						if (inputForm.validate()) {
							var value = nameTextField.getValue();
							var newItem = qx.data.marshal.Json.createModel({
										name : value
									});
							arrayWrapper.push(newItem);
						}
					}, this);

			resetButton.addListener("execute", function() {
						inputForm.reset()
					}, this);
		},
		showListFormBindingPostToQooxdooForum : function() {
			// create the UI.
			// create a group box
			var groupBox = new qx.ui.groupbox.GroupBox("Simple Form");
			// set group box layout to canvas.
			groupBox.setLayout(new qx.ui.layout.Canvas());
			this.getRoot().add(groupBox, {
						left : 10,
						top : 10
					});
			// create the input form.
			var inputForm = new qx.ui.form.Form();
			inputForm.addGroupHeader("Personal Information");

			// add one form item i.e. a text field.
			var nameTextField = new qx.ui.form.TextField();
			nameTextField.setRequired(true);
			nameTextField.setWidth(200);

			inputForm.add(nameTextField, "Name:", null, "name");

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

			// add the list to the root document
			groupBox.add(outputList, {
						left : 10,
						top : 100
					});

			// raw data: array of objects.
			// an array of raw data containing 2 objects.
			var rawData = [{
						"name" : "John"
					}, {
						"name" : "Bill"
					}];

			// create a Qooxdoo array to wrap raw data array.
			var arrayWrapper = new qx.data.Array();

			// iterate over the raw data and create a model
			for (var index = 0; index < rawData.length; index++) {
				var rawObject = rawData[index];
				var personModel = qx.data.marshal.Json.createModel(rawObject);
				this.debug("person created: "
						+ qx.dev.Debug.debugProperties(personModel));
				arrayWrapper.push(personModel);
			}

			// create the controller for list
			var listController = new qx.data.controller.List(arrayWrapper,
					outputList, "name");

			// add behaviour to save Button.
			saveButton.addListener("execute", function() {
						if (inputForm.validate()) {
							var formController = new qx.data.controller.Form(
									null, inputForm);
							var formModel = formController.createModel();
							this.debug("You are saving: "
									+ qx.util.Serializer.toJson(formModel));
							this.debug("arrayWrapper before: "
									+ qx.util.Serializer.toJson(arrayWrapper));
							this.debug("form model: "
									+ qx.dev.Debug.debugProperties(formModel));
							arrayWrapper.push(formModel);
							formController.resetModel();
							this.debug("arrayWrapper after: "
									+ qx.util.Serializer.toJson(arrayWrapper));
						}
					}, this);

			var resetter = new qx.ui.form.Resetter();
			resetter.add(nameTextField);
			resetButton.addListener("execute", function() {
						resetter.reset()
					}, this);
			this.debug("arrayWrapper: "
					+ qx.util.Serializer.toJson(arrayWrapper))

		},
		createWidgets : function() {
			// initialize data
			var userAccountRawData = {
				"loginName" : "Max",
				"name" : "Max M.",
				"email" : "max.m@example.com"
			};

			var userAccount = new admintoolclient.UserAccount(userAccountRawData);

			// rootDocumentument is the application root
			var rootDocument = this.getRoot();

			// create a list.
			var list = new qx.ui.form.List();
			list.setWidth(120);
			rootDocument.add(list, {
						left : 100,
						top : 50
					});

			// create mock data for the list.
			var rawData = [];
			for (var i = 0; i < 1; i++) {
				rawData.push(userAccount.getName());
			}
			var data = new qx.data.Array(rawData);

			// create controller from view(list) and model(data)
			var listController = new qx.data.controller.List(data, list);

			// create addItemButton
			var addItemButton = new qx.ui.form.Button("Add an item.");
			rootDocument.add(addItemButton, {
						left : 250,
						top : 50
					});

			// add some actions to the button.
			addItemButton.addListener("execute", function() {
						data.push("Item " + data.length);
					}, this);

			// create remove item button
			var removeItemButton = new qx.ui.form.Button("Remove last item.");
			rootDocument.add(removeItemButton, {
						left : 250,
						top : 90
					});

			removeItemButton.addListener("execute", function() {
						data.pop();
					}, this);

			var anotherList = new qx.ui.form.List();
			rootDocument.add(anotherList, {
						left : 400,
						top : 50
					});

			var printMeButton = new qx.ui.form.Button("printMe.");
			rootDocument.add(printMeButton, {
						left : 250,
						top : 150
					});

			printMeButton.addListener("execute", function() {
						alert("name: " + userAccount.toString());
					}, this);

			// create addItemButton
			var addUser = new qx.ui.form.Button("Add new user");
			rootDocument.add(addUser, {
						left : 250,
						top : 200
					});

			// add some actions to the button.
			addUser.addListener("execute", function() {
						data.push(userAccount.getName());
					}, this);

			var userAccountsRawDataAsArray = [{
						"loginName" : "Max",
						"name" : "Max M.",
						"email" : "max.m@example.com"
					}, {
						"loginName" : "B",
						"name" : "B",
						"email" : "b.b@example.com"
					}];

			var rawDataForUserAccounts = [];

			for (var index = 0; index < userAccountsRawDataAsArray.length; index++) {
				var userAccount = new admintoolclient.UserAccount(userAccountsRawDataAsArray[index]);
				rawDataForUserAccounts.push(userAccount);
			}

			var qooxdooDataForUserAccounts = new qx.data.Array(rawDataForUserAccounts);

			var anotherListController = new qx.data.controller.List(
					qooxdooDataForUserAccounts, anotherList, "loginName");

			var personRawData = [];
			for (var indexOfPerson = 0; indexOfPerson < 4; indexOfPerson++) {
				var person = new admintoolclient.Person();
				person.setName("Person " + indexOfPerson);
				personRawData.push(person);
			}

			// create the widgets
			var personList = new qx.ui.form.List();
			personList.setWidth(150);

			rootDocument.add(personList, {
						left : 250,
						top : 400
					});

			var personData = new qx.data.Array(personRawData);
			new qx.data.controller.List(personData, personList, "name");
		},
		testLoadingDataFromStore : function() {
			var url = "data.json";
			var store = new qx.data.store.Json(url);
			alert(store);
			var model = store.getModel();
			// alert(model);
		},
		createJsonToList : function() {
			// create and add the list
			var list = new qx.ui.form.List();
			this.getRoot().add(list, {
						left : 10,
						top : 80
					});

			// create the controller
			var controller = new qx.data.controller.List(null, list);
			// set the name for the label property
			controller.setLabelPath("name");
			// set a converter for the icons
			controller.setIconOptions({
						converter : function(data) {
							return "icon/16/mimetypes/" + data + ".png";
						}
					});
			// set the name of the icon property
			controller.setIconPath("type");

			// create the data store
			var url = "resource/admintoolclient/data.json";
			var store = new qx.data.store.Json(url);

			// create the status label
			var status = new qx.ui.basic.Label("Loading...");
			this.getRoot().add(status, {
						left : 120,
						top : 80
					});

			// connect the store and the controller
			store.bind("model.items", controller, "model");

			// bind the status label
			store.bind("state", status, "value");

			/*******************************************************************
			 * DESCRIPTIONS *********************************************
			 */
			var description = new qx.ui.basic.Label();
			description.setRich(true);
			description.setWidth(260);
			description
					.setValue("<b>List bound to data in a json file</b><br/>"
							+ "Loading the json file <a href='"
							+ url
							+ "' target='_blank'>"
							+ "list.json</a> and bind the items to the list widget. The icons "
							+ " will be created by a converter which converts the type to an icon url.");
			this.getRoot().add(description, {
						left : 10,
						top : 10
					});

		},
		createTwoSlidersBindedWithEachOthers : function() {
			// create two sliders
			var slider1 = new qx.ui.form.Slider();
			var slider2 = new qx.ui.form.Slider();

			// create a controller and use the first slider as a model
			var controller = new qx.data.controller.Object(slider1);

			// add the second slider as a target
			controller.addTarget(slider2, "value", "value");

			this.getRoot().add(slider1, {
						left : 10,
						top : 10
					});
			this.getRoot().add(slider2, {
						left : 10,
						top : 40
					});
		},
		createSimpleList : function() {
			var model = new qx.data.Array([{
						"key" : "value"
					}]);
			var list = new qx.ui.form.List();
			var controller = new qx.data.controller.List(model, list);
			controller.setLabelPath("key");
			this.getRoot().add(list);
		},
		createSlider2LabelBinding : function() {
			// create and add a slider
			var slider = new qx.ui.form.Slider();
			slider.setWidth(200);
			this.getRoot().add(slider, {
						left : 10,
						top : 10
					});

			// create and add a label
			var label = new qx.ui.basic.Label();
			this.getRoot().add(label, {
						left : 220,
						top : 10
					});

			// add the listener
			slider.addListener("changeValue", function(e) {
						// convert the number to a string
						label.setValue(e.getData() + "");
					}, this);
		},
		createSliderWithValidation : function() {// create and add a slider
			var slider = new qx.ui.form.Slider();
			slider.setWidth(200);
			slider.setValue(100);
			this.getRoot().add(slider, {
						left : 10,
						top : 10
					});
			// set the invalid message
			slider.setInvalidMessage("Please use a number above 50.");

			// add the validation
			slider.addListener("changeValue", function(e) {
						if (e.getData() > 50) {
							slider.setValid(true);
						} else {
							slider.setValid(false);
						}
					}, this);
		}
	}
});