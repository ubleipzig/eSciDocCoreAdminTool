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
	members : {
		__rawData : [{
					"name" : "John",
					"email" : "john.dalton@example.com",
					"creationDate" : new Date()
				}, {
					"name" : "Bill",
					"email" : "bill.joe@example.com",
					"creationDate" : new Date()
				}],
		/**
		 * This method contains the initial application code and gets called
		 * during startup of the application
		 * 
		 * @lint ignoreDeprecated(alert)
		 */
		main : function() {
			this.base(arguments);
			this.__enableLogging();
			this.__showListFormAndLabelBinding();
			// this.__createUserAccountFromJson();
			// this.__createUserAccountArrayFromJson();
		},

		__enableLogging : function() {
			if (qx.core.Variant.isSet("qx.debug", "on")) {
				qx.log.appender.Native;
				qx.log.appender.Console;
			}
		},
		__createUserAccountArrayFromJson : function() {
			var userAccounts = org.escidoc.admintool.io.Parser
					.parseUserAccounts(this.__userAccountsRawData);
			for (var index = 0; index < userAccounts.length; index++) {
				this.debug("user account: " + userAccounts[index].toString());
			}
		},
		__createUserAccountFromJson : function() {
			var userAccount = org.escidoc.admintool.io.Parser
					.parseUserAccount(this.__userAccountRawData);
			this.debug("user account: " + userAccount.toString());
		},
		__showListFormAndLabelBinding : function() {
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
					.parseUserAccounts(this.__userAccountsRawData));
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

		},
		destruct : function() {
			this.__rawData = null;
			this._disposeObjects("__rawData");
		},
		__userAccountsRawData : {
			"user-account-list" : {
				"@type" : "simple",
				"@title" : "User Account List",
				"@base" : "http://localhost:8080",
				"user-account" : [{
					"@type" : "simple",
					"@title" : "System Administrator User",
					"@href" : "/aa/user-account/escidoc:exuser1",
					"@last-modification-date" : "2010-02-10T10:04:08.688Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:08.688Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "System Administrator User"
						},
						"login-name" : {
							"$" : "sysadmin"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:exuser1/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:exuser1/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:exuser1/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "System Inspector User (Read Only Super User)",
					"@href" : "/aa/user-account/escidoc:exuser2",
					"@last-modification-date" : "2010-02-10T10:04:08.688Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:08.688Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "System Inspector User (Read Only SuperUser)"
						},
						"login-name" : {
							"$" : "sysinspector"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:exuser2/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:exuser2/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:exuser2/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "Depositor User",
					"@href" : "/aa/user-account/escidoc:exuser4",
					"@last-modification-date" : "2010-02-10T10:04:08.688Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:08.688Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "Depositor User"
						},
						"login-name" : {
							"$" : "depositor"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:exuser4/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:exuser4/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:exuser4/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "Ingestor User",
					"@href" : "/aa/user-account/escidoc:exuser5",
					"@last-modification-date" : "2010-02-10T10:04:08.688Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:08.688Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "Ingestor User"
						},
						"login-name" : {
							"$" : "ingester"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:exuser5/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:exuser5/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:exuser5/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "Collaborator User",
					"@href" : "/aa/user-account/escidoc:exuser6",
					"@last-modification-date" : "2010-02-10T10:04:08.688Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:08.688Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "Collaborator User"
						},
						"login-name" : {
							"$" : "collaborator"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:exuser6/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:exuser6/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:exuser6/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "roland",
					"@href" : "/aa/user-account/escidoc:user42",
					"@last-modification-date" : "2010-02-10T10:04:10.266Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:10.266Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "roland"
						},
						"login-name" : {
							"$" : "roland"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:user42/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:user42/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:user42/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "Inspector (Read Only Super User)",
					"@href" : "/aa/user-account/escidoc:user44",
					"@last-modification-date" : "2010-02-10T10:04:10.266Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:10.266Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "Inspector (Read Only Super User)"
						},
						"login-name" : {
							"$" : "inspector"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:user44/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:user44/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:user44/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "Test Depositor Scientist",
					"@href" : "/aa/user-account/escidoc:user1",
					"@last-modification-date" : "2010-02-10T10:04:10.266Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:10.266Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "Test Depositor Scientist",
							"@href" : "/aa/user-account/escidoc:user1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "Test Depositor Scientist",
							"@href" : "/aa/user-account/escidoc:user1"
						},
						"name" : {
							"$" : "Test Depositor Scientist"
						},
						"login-name" : {
							"$" : "test_dep_scientist"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:user1/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:user1/resources/..."
						}
					}
				}]
			}
		},
		__userAccountRawData : {
			"user-account" : {
				"@base" : "http://localhost:8080",
				"@type" : "simple",
				"@title" : "System Administrator User",
				"@href" : "/aa/user-account/escidoc:exuser1",
				"@last-modification-date" : "2010-02-10T10:04:08.688Z",
				"properties" : {
					"creation-date" : {
						"$" : "2010-02-10T10:04:08.688Z"
					},
					"created-by" : {
						"@type" : "simple",
						"@title" : "System Administrator User",
						"@href" : "/aa/user-account/escidoc:exuser1"
					},
					"modified-by" : {
						"@type" : "simple",
						"@title" : "System Administrator User",
						"@href" : "/aa/user-account/escidoc:exuser1"
					},
					"name" : {
						"$" : "System Administrator User"
					},
					"login-name" : {
						"$" : "sysadmin"
					},
					"active" : {
						"$" : true
					}
				},
				"resources" : {
					"@type" : "simple",
					"@title" : "Virtual Resources",
					"@href" : "/aa/user-account/escidoc:exuser1/resources",
					"current-grants" : {
						"@type" : "simple",
						"@title" : "Current Grants",
						"@href" : "/aa/user-account/escidoc:exuser1/resources/current-grants"
					},
					"preferences" : {
						"@type" : "simple",
						"@title" : "Preferences",
						"@href" : "/aa/user-account/escidoc:exuser1/resources/preferences"
					},
					"attributes" : {
						"@type" : "simple",
						"@title" : "Attributes",
						"@href" : "/aa/user-account/escidoc:exuser1/resources/attributes"
					}
				}
			}
		}
	}
});