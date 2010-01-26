/**
 * @author CHH
 */
qx.Class.define("org.escidoc.admintool.tabview.page.NewResource", {
			extend : qx.ui.tabview.Page,
			construct : function(id) {
				this.base(arguments, this.title, this.icon);
				this.setLayout(new qx.ui.layout.HBox());
				this.setShowCloseButton(true);

				var container = new qx.ui.container.Composite();
				container.forms = new Array();
				container.setLayout(new qx.ui.layout.VBox());
				this.add(container);

				this.bind("title", this, "label");

				// Page display consists of several Forms

				/*
				 * Form containing User informations
				 */
				this.form = new qx.ui.form.Form();
				container.forms.push(this.form);

				// name
				var nameTextField = new qx.ui.form.TextField(this.getTitle());
				nameTextField.setRequired(true);
				nameTextField.setWidth(200);
				this.form.add(nameTextField, "Name", "name");
				nameTextField.bind("value", this, "title");

				// login name
				var loginTextField = new qx.ui.form.TextField();
				loginTextField.setRequired(true);
				loginTextField.setWidth(400);
				this.form.add(loginTextField, "Login Name", "login-name");

				// password
				var passwordField = new qx.ui.form.PasswordField();
				passwordField.setRequired(true);
				var label = new qx.ui.basic.Label("Password");
				label.setBuddy(passwordField);
				this.form.add(passwordField, "Password", "password");
                
				// Email
				var emailTextField = new qx.ui.form.TextField();
				this.form.add(emailTextField, "Email", qx.util.Validate.email(),
						"email");
				emailTextField.bind("value", this, "email");

				// grants
				var comboBox = new qx.ui.form.ComboBox();
				comboBox.setPlaceholder("Grants");
				label = new qx.ui.basic.Label("Grant:");
				label.setBuddy(comboBox);
				this.form.add(comboBox, "Grant", "grant");
				this.createItems(comboBox);

				// create button
				var createButton = new qx.ui.form.Button("Create");
				this.form.addButton(createButton);

				// create validation manager
				var validationManager = new qx.ui.form.validation.Manager();
				validationManager.add(nameTextField, qx.util.Validate.required);
				validationManager
						.add(loginTextField, qx.util.Validate.required);
				validationManager.add(passwordField, qx.util.Validate.required);
                validationManager.add(emailTextField, qx.util.Validate.email());

				// invoke the serialization
				createButton.addListener("execute", function() {
							if (validationManager.validate()) {
								alert("All valid.");
							}
						}, this);
				// //////////////////////////////////

				var formView = new qx.ui.form.renderer.Single(this.form);
				container.add(formView);

				var folderBox = new qx.ui.groupbox.GroupBox("Folder");
				folderBox.setLayout(new qx.ui.layout.VBox());

				container.add(folderBox);
			},
			statics : {
				ITEM_SIZE : 5,
				DEFAULT_VALIDATOR : null
			},
			properties : {
				title : {
					init : "",
					check : "String",
					nullable : false,
					event : "changeTitle"
				},
				email : {
					init : "",
					check : "String",
					nullable : false,
					event : "changeEmail"
				},
				infrastructureUrl : {
					init : "http://localhost:8080",
					check : "String",
					nullable : false,
					event : "changeInfrastructureUrl"
				},
				startTime : {
					// XML compatible date-time-string
					init : null,
					check : "String",
					nullable : true,
					event : "changeStartTime"
				}
			},
			members : {
				icon : "icon/16/apps/preferences-users.png",
				form : null,
				createItems : function(widget) {
					for (var i = 0; i < this.self(arguments).ITEM_SIZE; i++) {
						var tempItem = new qx.ui.form.ListItem("Grant " + i);
						widget.add(tempItem);
					}
				}
			}
		});