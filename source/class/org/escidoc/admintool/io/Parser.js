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
 * Copyright 2006-${year} Fachinformationszentrum Karlsruhe Gesellschaft fuer
 * wissenschaftlich-technische Information mbH and Max-Planck- Gesellschaft zur
 * Foerderung der Wissenschaft e.V. All rights reserved. Use is subject to
 * license terms.
 */
 
 //this class should be called factory instead of Parser.
qx.Class.define("org.escidoc.admintool.io.Parser", {
	statics : {
		iso2jsDate : function(isoDate) {
			return this.__eSciDocDate.parse(isoDate);
		},
		parseUserAccounts : function(json) {
			var items = [];
			if (json) {
				var userAccounts = json['user-account-list']['user-account'];
				for (var index = 0; index < userAccounts.length; index++) {
					items
							.push(this
									.__parseUserAccountProperties(userAccounts[index]['properties']));
				}
			} else {
				throw new Error("Invalid json: " + json);
			}
			return items;
		},
		// this function does too much. It parses and creates an object.TODO:
		// re-factor this.
		__parseUserAccountProperties : function(json) {
			var userAccountProperties = json;
			if (userAccountProperties['name']['$']
					&& userAccountProperties['login-name']['$']
					&& userAccountProperties['active']['$']
					&& userAccountProperties['creation-date']['$']) {

				// FIXME: some data contains new line characters.
				var creationDateValue = org.escidoc.admintool.io.Parser
						.iso2jsDate(qx.lang.String
								.clean(userAccountProperties['creation-date']['$']));

				// validate the date object.
				// Does the properties in UserAccount class do the validation?
				// if yes, then this code is not needed.
				if (!(creationDateValue instanceof Date)) {
					throw new Error("Date Error: " + creationDateValue);
				}

				// everthing we need are found and valid.
				// TODO: we create an User Account object.
				var userAccount = new org.escidoc.admintool.model.UserAccount();
				userAccount.set({
					name : userAccountProperties['name']['$'] || "",
					loginName : userAccountProperties['login-name']['$'] || "",
					isActive : userAccountProperties['active']['$'],
					creationDate : org.escidoc.admintool.io.Parser
							.iso2jsDate(userAccountProperties['creation-date']['$'])
							|| null,
					email : null
				});
			} else {
				throw new Error("Error data: missing properties value");
			}
			return userAccount;
		},
		/**
		 * {qx.util.format.DateFormat} eSciDoc data format standard
		 */
		__eSciDocDate : new qx.util.format.DateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "en_US")
	}
});