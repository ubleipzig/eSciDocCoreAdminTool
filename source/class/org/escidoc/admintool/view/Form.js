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
			// This may be not right. Why is a form a GroupBox? No!
			extend : qx.ui.groupbox.GroupBox,
			construct : function(window) {
				this.base(arguments);
				this.__window = window;
				this.__setVboxAsLayout().__createInputForm()
						.__addNameTextField().__addLoginNameTextField()
						.__addCancelButton().__closeWindowWhenClicked()
						.__addSaveButton().__done();
			},
			properties : {},
			events : {
				"exClose" : "qx.event.type.Data"
			},
			members : {
				__window : null,
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
					this.__inputForm.add(this.__nameTextField, "Name", null,
							"name");
					return this;
				},
				__addLoginNameTextField : function() {
					this.__loginNameTextField = new qx.ui.form.TextField();
					this.__loginNameTextField.setWidth(100);
					this.__inputForm.add(this.__loginNameTextField,
							"Login Name", null, "loginName");
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
				__done : function() {
					this.add(new qx.ui.form.renderer.Single(this.__inputForm));
					return this;
				}
			},
			destruct : function() {
				this._disposeObjects("__inputForm", "_nameTextField",
						"__loginNameTextField", "__cancelButton",
						"__saveButton");
			}
		});