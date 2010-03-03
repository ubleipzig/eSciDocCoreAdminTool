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
qx.Class.define("org.escidoc.admintool.view.List", {
	extend : qx.ui.core.Widget,
	construct : function() {
		this.base(arguments);

		// set the layout
		var layout = new qx.ui.layout.VBox();
		layout.setSeparator("separator-vertical");
		this._setLayout(layout);

		// Create the header of the list
		var listHeader = new qx.ui.basic.Label(this.tr("User Accounts"));
		listHeader.setBackgroundColor("background-medium");
		listHeader.setPadding(5);
		listHeader.setAllowGrowX(true);
		listHeader.setFont("bold");
		this._add(listHeader);

		// Create the stack for the list
		this.__stack = new qx.ui.container.Stack();
		this._add(this.__stack, {
					flex : 1
				});

		// create list view
		this.__list = new qx.ui.form.List();
		this.__list.setDecorator(null);
		this.__list.setSelectionMode("single");
		this.__stack.add(this.__list);

		// Create the loading image for the list
		this.__listLoadImage = new qx.ui.container.Composite(new qx.ui.layout.HBox(
				0, "center"));
		var loadImage = new qx.ui.basic.Image("feedreader/images/loading66.gif");
		loadImage.setAlignY("middle");
		this.__listLoadImage.add(loadImage);
		this.__stack.add(this.__listLoadImage);
	},
	properties : {
		/** Determinates if the loading image should be shown */
		loading : {
			check : "Boolean",
			init : false,
			apply : "_applyLoading"
		}
	},
	members : {
		// private members
		__stack : null,
		__list : null,
		__listLoadImage : null,

		// property apply
		_applyLoading : function(value, old) {
			if (value) {
				this.__stack.setSelection([this.__listLoadImage]);
			} else {
				this.__stack.setSelection([this.__list]);
			}
		},

		/**
		 * Returns the list widget used in the list view of the feedreader.
		 * 
		 * @return {qx.ui.form.List} The used List.
		 */
		getList : function() {
			return this.__list;
		}
	},
	destruct : function() {
		this._disposeObjects("__list", "__stack", "__listLoadImage");
	}
});