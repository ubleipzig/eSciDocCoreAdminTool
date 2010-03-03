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
			extend : qx.ui.groupbox.GroupBox,
			construct : function() {
				this.base(arguments);
				this.setLayout(new qx.ui.layout.VBox(5));

				var inputForm = new qx.ui.form.Form();
				inputForm.addGroupHeader("New User Account");

				var nameTextField = new qx.ui.form.TextField();
				nameTextField.setRequired(true);
				nameTextField.setWidth(200);
				inputForm.add(nameTextField, "Name", null, "name");

				var loginNameTextField = new qx.ui.form.TextField();
				loginNameTextField.setWidth(100);
				inputForm.add(loginNameTextField, "Login Name", null,
						"loginName");

				var cancelButton = new qx.ui.form.Button("Cancel");
				cancelButton.setWidth(70);
				inputForm.addButton(cancelButton);

				var saveButton = new qx.ui.form.Button("Save");
				saveButton.setWidth(70);
				inputForm.addButton(saveButton);
                
				this.add(new qx.ui.form.renderer.Single(inputForm));
			},
			properties : {},
			members : {},
			destruct : function() {
				this._disposeObjects();
			}
		});