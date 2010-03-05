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
qx.Class.define("org.escidoc.admintool.view.Form", {
	// This may be not right. Is a form a GroupBox? No!
	extend : qx.ui.groupbox.GroupBox,
	// FIXME: too many dependencies in the constructor. Create a
	// setters.
	construct : function(window, model) {
		this.base(arguments);
		qx.core.Assert.assertNotNull(window, "window must not be null.");
		qx.core.Assert.assertNotNull(model, "model must not be null.");
		this.__window = window;
		this.__model = model;
		this.__setVboxAsLayout().__createInputForm().__addNameTextField()
				.__addLoginNameTextField().__addCancelButton()
				.__closeWindowWhenClicked().__addSaveButton()
				.__saveDataWhenClicked().__done();
	},
	members : {
		__window : null,
		__model : null,
		__tableModel : null,
		__inputForm : null,
		__nameTextField : null,
		__loginNameTextField : null,
		__cancelButton : null,
		__saveButton : null,
		__setVboxAsLayout : function() {
			this.setLayout(new qx.ui.layout.VBox(5));
			return this;
		},
		__createInputForm : function() {
			this.__inputForm = new qx.ui.form.Form();
			this.__inputForm.addGroupHeader("New User Account");
			return this;
		},
		__addNameTextField : function() {
			this.__nameTextField = new qx.ui.form.TextField();
			this.__nameTextField.setRequired(true);
			this.__nameTextField.setWidth(200);
			this.__inputForm.add(this.__nameTextField, "Name", null, "name");
			return this;
		},
		__addLoginNameTextField : function() {
			this.__loginNameTextField = new qx.ui.form.TextField();
			this.__loginNameTextField.setWidth(100);
			this.__inputForm.add(this.__loginNameTextField, "Login Name", null,
					"loginName");
			return this;
		},
		__addCancelButton : function() {
			this.__cancelButton = new qx.ui.form.Button("Cancel");
			this.__cancelButton.setWidth(70);
			this.__inputForm.addButton(this.__cancelButton);
			return this;
		},
		__closeWindowWhenClicked : function() {
			this.__cancelButton.addListener("execute", function() {
						this.__window.close();
					}, this);
			return this;
		},
		__addSaveButton : function() {
			this.__saveButton = new qx.ui.form.Button("Save");
			this.__saveButton.setWidth(70);
			this.__inputForm.addButton(this.__saveButton);
			return this;
		},
		__saveDataWhenClicked : function() {
			this.__saveButton.addListener("execute", function() {
						if (this.__inputForm.validate()) {
							// send data as JSON to eSciDoc.(async)

							// if succesful: add to both model and user
							// account model.
							// Why? both of them?
							var isSuccesful = true;
							if (isSuccesful) {
								// where do I get the table and user
								// account model?
								qx.core.Assert.assertNotNull(this.__model);
								var before = this.__model.length;

								var newUserAccount = qx.data.marshal.Json
										.createModel({
													name : this.__nameTextField
															.getValue(),
													loginName : this.__loginNameTextField
															.getValue(),
													creationDate : new Date()
												})
								this.__model.push(newUserAccount);
								qx.core.Assert.assertEquals(before + 1,
										this.__model.length,
										"Not inserted into the model array.");

								var rowData = [];
								rowData.push([newUserAccount.getName(),
										newUserAccount.getLoginName(),
										newUserAccount.getCreationDate()]);

								this.__tableModel.addRows(rowData);
							}
							this.__inputForm.reset();
							this.__window.close();
						}
					}, this);
			return this;
		},
		__done : function() {
			this.add(new qx.ui.form.renderer.Single(this.__inputForm));
			return this;
		},
		setTableModel : function(tableModel) {
			this.__tableModel = tableModel;
			return this;
		}
	},
	destruct : function() {
		this._disposeObjects("__inputForm", "_nameTextField",
				"__loginNameTextField", "__cancelButton", "__saveButton");
	}
});