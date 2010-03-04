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
qx.Class.define("org.escidoc.admintool.view.Toolbar", {
			extend : qx.ui.toolbar.ToolBar,
			construct : function(controller) {
				this.base(arguments);

				var mainPart = new qx.ui.toolbar.Part;
				this.add(mainPart);

				// Reload button
				// FIXME: fix error while loading icons.
				// add behaviour to the buttons.
				var reloadBtn = new qx.ui.toolbar.Button(this.tr("Reload"));

				// var reloadCmd = controller.getCommand("reload");
				// reloadBtn.setCommand(reloadCmd);
				// reloadBtn.setToolTipText(this.tr("Reload the data. (%1)",
				// reloadCmd.toString()));

				mainPart.add(reloadBtn);

				// Add a spacer
				this.addSpacer();

				// Info part
				var infoPart = new qx.ui.toolbar.Part;
				this.add(infoPart);

				// Login button
				// TODO: add behaviour to the login button. Temporary fix, open
				// new tab or window showing old admin tool login page.
				var aboutBtn = new qx.ui.toolbar.Button(this.tr("Login"));
				// var aboutCmd = controller.getCommand("about");
				// aboutBtn.setCommand(aboutCmd);
				// aboutBtn.setToolTipText("(" + aboutCmd.toString() + ")");
				infoPart.add(aboutBtn);
			}
		});