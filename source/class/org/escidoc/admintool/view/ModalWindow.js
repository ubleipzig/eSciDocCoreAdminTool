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
qx.Class.define("org.escidoc.admintool.view.ModalWindow", {
			extend : qx.ui.window.Window,
			construct : function(model) {
				this.base(arguments);
				qx.core.Assert.assertNotNull(model, "model must not be null.");
				this.__model = model;
				this.__initSelf().__addUserForm();
			},
			members : {
				__model : null,
				__userAccountForm : null,
				__initSelf : function() {
					this.setLayout(new qx.ui.layout.VBox(10));
					this.set({
								modal : true,
								showMinimize : false,
								showMaximize : false,
								allowMaximize : false,
								showStatusbar : false,
								movable : false
							});
					this.__makeBackgroundDark().__moveToNearAddButton();
					return this;
				},
				__makeBackgroundDark : function() {
					this.getApplicationRoot().set({
								blockerColor : '#bfbfbf',
								blockerOpacity : 0.8
							});
					return this;
				},
				__moveToNearAddButton : function() {
					this.moveTo(350, 100);
					return this;
				},
				__addUserForm : function() {
					// FIXME: this is a hack! why pass the window object to its
					// child. Temporary hack to allow cancel button close the
					// window when it's clicked.
					this.__userAccountForm = new org.escidoc.admintool.view.Form(
							this, this.__model);

					this.add(this.__userAccountForm);
				},
				setTableModel : function(tableModel) {
					this.__userAccountForm.setTableModel(tableModel);
				}
			},
			destruct : function() {
				this._disposeObjects("__userAccountForm");
			}
		});