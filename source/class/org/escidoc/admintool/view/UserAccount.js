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
qx.Class.define("org.escidoc.admintool.view.UserAccount", {
	extend : qx.ui.container.Composite,
	construct : function() {
		this.base(arguments);
		this._createLayout();
		this._loadRowData();
		this._initTableModel();
		this._createTable();
	},
	properties : {
		userAccounts : {
			apply : "_applyUserAccounts",
			nullable : true,
			check : "Object"
		}
	},
	members : {
		__toolbar : null,
		__rowData : [],
		__tableModel : null,
		__table : null,
		_createLayout : function() {
			this.setLayout(new qx.ui.layout.VBox());

			this.__toolbar = new qx.ui.toolbar.ToolBar();
			this.add(this.__toolbar);

			var toolbarPart = new qx.ui.toolbar.Part();
			this.__toolbar.add(toolbarPart);

			var refreshButton = new qx.ui.toolbar.Button("Refresh");
			toolbarPart.add(refreshButton);

			var newButton = new qx.ui.toolbar.Button("New");
			toolbarPart.add(newButton);
			this.__createListenerForNewButton(newButton);

			var editButton = new qx.ui.toolbar.Button("Edit");
			toolbarPart.add(editButton);

			var deleteButton = new qx.ui.toolbar.Button("Delete");
			toolbarPart.add(deleteButton);
		},
		__createListenerForNewButton : function(newButton) {
			newButton.addListener("execute", function() {
						var modalWindow = new org.escidoc.admintool.view.ModalWindow();
						modalWindow.open();
					}, this);
		},
		_loadRowData : function() {
			var userAccounts = new qx.data.Array(org.escidoc.admintool.io.Parser
					.parseUserAccounts(this.__getUserAccounts()));
			for (var index = 0; index < userAccounts.length; index++) {
				var userAccount = userAccounts.getItem(index);
				this.__rowData.push([userAccount.getName(),
						userAccount.getLoginName(),
						userAccount.getCreationDate()]);
			}
		},
		_initTableModel : function() {
			this.__tableModel = new qx.ui.table.model.Simple();
			this.__tableModel.setData(this.__rowData);
			this.__tableModel.setColumns(["Name", "Login Name", "Created On"]);
			this.__tableModel.setColumnSortable(1, true);
			this.__tableModel.setColumnSortable(2, true);
			this.__tableModel.setColumnSortable(3, true);
		},
		_createTable : function() {
			var custom = {
				tableColumnModel : function(obj) {
					return new qx.ui.table.columnmodel.Resize(obj);
				}
			};
			this.__table = new qx.ui.table.Table(this.__tableModel, custom);
			this.__table.set({
						width : 300,
						height : 200,
						decorator : null
					});
			this.add(this.__table, {
						flex : 1
					});
		},
		_applyUserAccounts : function(value, old) {
			var userAccounts = this.__getUserAccounts();
		},
		__getUserAccounts : function() {
			return org.escidoc.admintool.data.UserAccounts.getData();
		}
	}
});