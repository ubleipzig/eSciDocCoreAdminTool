/*
 * CDDL HEADER START
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License, Version 1.0 only (the "License"). You may not use
 * this file except in compliance with the License.
 * 
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE or
 * http://www.escidoc.de/license. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at license/ESCIDOC.LICENSE. If applicable, add the
 * following below this CDDL HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 * 
 * CDDL HEADER END
 */

/*
 * Copyright 2006-2010 Fachinformationszentrum Karlsruhe Gesellschaft fuer
 * wissenschaftlich-technische Information mbH and Max-Planck- Gesellschaft zur
 * Foerderung der Wissenschaft e.V. All rights reserved. Use is subject to
 * license terms.
 */
/**
 * @author CHH
 */
/**
 * This is the main application class of your custom application
 * "AdminToolClient"
 */
qx.Class.define("org.escidoc.admintool.Application", {
	extend : qx.application.Standalone,
	members : {
		__header : null,
		__toolBarView : null,

		__treeView : null,

		__horizontalSplitPane : null,
		__verticalSplitPane : null,

		__listView : null,

		__treeController : null,
		__rootFolder : null,
		main : function() {
			this.base(arguments);
			this.__enableLogging();
			this.__initAdminTool();
			// this._showListFormAndLabelBinding();
		},
		__initAdminTool : function() {
			this._createLayout();

			this._initializeModel();

			this._setUpBinding();

			// configure tree view
			this.__treeView.getRoot().setOpen(true);
			this.__treeView.setHideRoot(true);
		},
		__enableLogging : function() {
			if (qx.core.Variant.isSet("qx.debug", "on")) {
				qx.log.appender.Native;
				qx.log.appender.Console;
			}
		},
		_createLayout : function() {
			// TODO: refactor functions with comments into separate functions.

			// Create main layout
			var dockLayout = new qx.ui.layout.Dock();
			dockLayout.setSeparatorY("separator-vertical");
			var dockLayoutComposite = new qx.ui.container.Composite(dockLayout);
			this.getRoot().add(dockLayoutComposite, {
						edge : 0
					});

			// Create header
			this.__header = new org.escidoc.admintool.view.Header();
			dockLayoutComposite.add(this.__header, {
						edge : "north"
					});

			// Create toolbar
			this.__toolBarView = new org.escidoc.admintool.view.Toolbar(this);
			dockLayoutComposite.add(this.__toolBarView, {
						edge : "north"
					});

			// Create horizontal splitpane for tree and resource view
			this.__horizontalSplitPane = new qx.ui.splitpane.Pane();
			dockLayoutComposite.add(this.__horizontalSplitPane);

			// Create tree view
			this.__treeView = new qx.ui.tree.Tree();
			this.__treeView.setWidth(250);
			this.__treeView.setBackgroundColor("white");
			this.__horizontalSplitPane.add(this.__treeView, 0);

			// Create vertical splitpane for list and detail view
			this.__verticalSplitPane = new qx.ui.splitpane.Pane("vertical");
			this.__verticalSplitPane.setDecorator(null);
			this.__horizontalSplitPane.add(this.__verticalSplitPane, 1);

			// Create the list view
			this.__listView = new org.escidoc.admintool.view.UserAccount();
			this.__listView.setHeight(200);
			this.__listView.setDecorator("main");
			this.__verticalSplitPane.add(this.__listView, 0);
			//
			// // Create article view
			// this.__articleView = new playground.view.Article();
			// this.__articleView.setDecorator("main");
			// this.__verticalSplitPane.add(this.__articleView, 1);
		},
		_initializeModel : function() {
			this.__rootFolder = new org.escidoc.admintool.model.RootFolder("Root");
		},
		_setUpBinding : function() {
			this.__treeController = new qx.data.controller.Tree(
					this.__rootFolder, this.__treeView, "roots", "title");

			// Add static feeds
			this.__resourcesFolder = new org.escidoc.admintool.model.RootFolder(this
					.tr("Resources"));

			this.__rootFolder.getChildren().push(this.__resourcesFolder);

			this.__resourcesFolder
					.getChildren()
					.push(new org.escidoc.admintool.model.UserAccount("UserAccount"));
		},
		_showListFormAndLabelBinding : function() {
			var groupBox = new qx.ui.groupbox.GroupBox("Simple Form");
			groupBox.setLayout(new qx.ui.layout.VBox(5));
			this.getRoot().add(groupBox, {
						left : 10,
						top : 10
					});
			var inputForm = new qx.ui.form.Form();
			inputForm.addGroupHeader("Registration");

			var nameTextField = new qx.ui.form.TextField();
			nameTextField.setRequired(true);
			nameTextField.setWidth(200);

			inputForm.add(nameTextField, "Name", null, "name");

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
								+ qx.util.Serializer.toJson(arrayWrapper));
					}, this);

			groupBox.add(new qx.ui.form.renderer.Single(inputForm));

			var outputList = new qx.ui.form.List();
			outputList.setWidth(240);
			groupBox.add(outputList);

			var detailsView = new qx.ui.basic.Label("Details");
			groupBox.add(detailsView);

			var deleteButton = new qx.ui.form.Button("Delete");
			groupBox.add(deleteButton);

			var arrayWrapper = qx.data.marshal.Json.createModel(this.__rawData);

			var listController = new qx.data.controller.List(arrayWrapper,
					outputList, "name");
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
			listController.bind("selection[0].name", detailsView, "value");

			// add behaviour to save Button.
			saveButton.addListener("execute", function() {
						if (inputForm.validate()) {
							arrayWrapper.push(qx.data.marshal.Json.createModel(
									{
										name : nameTextField.getValue(),
										email : emailTextField.getValue(),
										creationDate : new Date()
									}));
							inputForm.reset();
						}
					}, this);

			resetButton.addListener("execute", function() {
						inputForm.reset();
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
			}, this);
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
					}, this);

			// show data as table(GET)
			// var tableComposite = new qx.ui.container.Composite(new
			// qx.ui.layout.VBox());
			var tableComposite = new qx.ui.window.Window().set({
						width : 600,
						height : 400,
						contentPadding : [0, 0, 0, 0],
						showClose : false,
						showMinimize : false,
						showMaximize : false
					});
			tableComposite.setLayout(new qx.ui.layout.VBox());
			tableComposite.open();

			this.getRoot().add(tableComposite, {
						left : 400,
						top : 20
					});
			// Toolbar
			// TODO: add icon
			var tableToolbar = new qx.ui.toolbar.ToolBar();
			tableComposite.add(tableToolbar);

			var toolbarPart = new qx.ui.toolbar.Part();
			tableToolbar.add(toolbarPart);
			var refreshButton = new qx.ui.toolbar.Button("Refresh");
			toolbarPart.add(refreshButton);
			var newButton = new qx.ui.toolbar.Button("New");
			toolbarPart.add(newButton);
			var editButton = new qx.ui.toolbar.Button("Edit");
			toolbarPart.add(editButton);
			var deleteButton = new qx.ui.toolbar.Button("Delete");
			toolbarPart.add(deleteButton);
			var debugToolbarButton = new qx.ui.toolbar.Button("Debug");
			toolbarPart.add(debugToolbarButton);

			var retrieveToolbarButton = new qx.ui.toolbar.Button("Retrieve");
			toolbarPart.add(retrieveToolbarButton);

			// FIXME: refactor this, no OO.
			// Argument: DTSTTCPW
			// http://c2.com/cgi/wiki?DoTheSimplestThingThatCouldPossiblyWork
			retrieveToolbarButton.addListener("execute", function() {
				this.debug("Retrieving...");
				// setup the Remote Request.
				// define POST or GET
				// define Accept MIME TYPE PARAM
				// define server and host ==> define Resource URI
				var RESOURCE_URI = "http://localhost:8181/v1.2/users";
				var request = new qx.io.remote.Request(RESOURCE_URI, "GET",
						"application/json");
				request.setCrossDomain(true);

				// timeout
				request.setTimeout(9999999999);
				// register a listerner for completed event.

				request.addListener("completed", function(response) {
					var json = response.getContent();
					var tmp = json['userAccounts'];
					// for (var index = 0; index < tmp.length; index++) {
					// this
					// .debug("creationDate:"
					// + tmp[index]['properties']['creationDateAsString']);
					// var creationDate = org.escidoc.admintool.io.JsonParser
					// .__isoDateStringToDate(tmp[index]['properties']['creationDateAsString']);
					//
					// this.debug(creationDate.toString());
					// }
					var userAccountArray = org.escidoc.admintool.io.JsonParser
							.parseUserAccounts(json);
					this.debug("UserAccounts object is created.");
					for (var index = 0; index < userAccountArray.length; index++) {
						this.debug(userAccountArray[index].getLoginName());
						this.debug(userAccountArray[index].getName());
						this.debug(userAccountArray[index].getIsActive());
						this.debug(userAccountArray[index].getCreationDate()
								.toString());
					}
				}, this);

				// // define error and time out handling function
				// // define what happens after succesful request.
				// finally send the request to the server.
				request.send();
			}, this);

			// Table
			var tableModel = new qx.ui.table.model.Simple();

			// the input for simple table model is a 2-dimensional array [] [].
			// The first dimension is for the row, second dimension is for the
			// column.

			// what we have right now, is an array of objects, i.e.,
			// arrayWrapper
			// so need to convert this 1-dim array to 2-dim array.
			// each properties of the object in arrayWrapper will be inserted in
			// second array.

			// How do we do that?
			// the naive way:
			// iterate the arrayWrapper, read each properties and put it in an
			// array
			// TODO: Re-factor:
			// 1. put table model in private variable, i.e., __tableModel
			// 2. extract row data init to a private function, i.e.,
			// loadData(..)
			var rowData = [];
			// for (var index = 0; index < arrayWrapper.length; index++) {
			// var person = arrayWrapper.getItem(index);
			// rowData.push([person.getName(), person.getEmail(),
			// person.getCreationDate()]);
			// }

			var userAccounts = new qx.data.Array(org.escidoc.admintool.io.Parser
					.parseUserAccounts(org.escidoc.admintool.data.UserAccounts
							.getData()));

			for (var index = 0; index < userAccounts.length; index++) {
				var userAccount = userAccounts.getItem(index);
				rowData.push([userAccount.getName(),
						userAccount.getLoginName(),
						userAccount.getCreationDate()]);
			}

			tableModel.setData(rowData);
			tableModel.setColumns(["Name", "E-mail", "Created On"]);

			tableModel.setColumnSortable(1, true);
			tableModel.setColumnSortable(2, true);
			tableModel.setColumnSortable(3, true);

			// create the table ui
			// TODO: how to set the width of a certain column.
			// @See: Qooxdoo DemoBrowser: Table_Resize_Bolumn.js
			// var table = new qx.ui.table.Table(tableModel);
			var custom = {
				tableColumnModel : function(obj) {
					return new qx.ui.table.columnmodel.Resize(obj);
				}
			};
			var table = new qx.ui.table.Table(tableModel, custom);

			table.set({
						width : 300,
						height : 200,
						decorator : null
					});

			tableComposite.add(table, {
						flex : 1
					});

			// Next steps is to sync model with table widget. Keep in mind that
			// currently qooxdoo does not have a controller for table widget.
			// However, there are a couple Qooxdoo real world application that
			// manipulate its model using table, e.g.,
			// http://demo.hericus.com/index.html
			refreshButton.addListener("execute", function() {
						// naive
						var rowData = [];
						for (var index = 0; index < arrayWrapper.length; index++) {
							var person = arrayWrapper.getItem(index);
							rowData.push([person.getName(), person.getEmail(),
									person.getCreationDate()]);
						}
						tableModel.setData(rowData);
					}, this);

			// add behaviour to the debug button in the toolbar.
			// it shows which row is selected(we start with single selection
			// first)
			// after that we need to get the object in the selected row.
			// after we achieve this, we can add double click feature on the
			// selected item to show the debug message.
			debugToolbarButton.addListener("execute", function(evt) {
						var selection = [];
						var selectionModel = table.getSelectionModel();
						// selectionModel.
						// selectionModel.iterateSelection(
						// function(ind) {
						// selection.push(ind + "");
						// });
						this.debug("selected: "
								+ qx.dev.Debug.debugProperties(selectionModel));
					}, this);
		}
	},
	destruct : function() {
		this._disposeObjects(" __header", "__treeView",
				"__horizontalSplitPane", "__verticalSplitPane", "__header",
				"__listView");
	}
});