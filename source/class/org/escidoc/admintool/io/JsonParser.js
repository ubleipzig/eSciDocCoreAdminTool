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

// this class should be called factory instead of Parser.
qx.Class.define("org.escidoc.admintool.io.JsonParser", {
	statics : {
		iso2jsDate : function(isoDate) {
			return this.__eSciDocDate.parse(isoDate);
		},
		parseUserAccounts : function(json) {
			var items = [];
			if (json) {
				var userAccounts = json['userAccounts'];
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
			if (userAccountProperties['name']
					&& userAccountProperties['loginName']
					&& userAccountProperties['active']
					&& userAccountProperties['creationDateAsString']) {

				// FIXME: some data contains new line characters.
				var creationDateValue = org.escidoc.admintool.io.JsonParser
						.__isoDateStringToDate(userAccountProperties['creationDateAsString']);

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
							name : userAccountProperties['name'] || "",
							loginName : userAccountProperties['loginName']
									|| "",
							isActive : userAccountProperties['active'],
							creationDate : creationDateValue || null,
							email : null
						});
			} else {
				throw new Error("Error data: missing properties value");
			}
			return userAccount;
		},
		__isoDateStringToDate : function(datestr) {
			if (!this.re) {
				// The date in YYYY-MM-DD or YYYYMMDD format
				var datere = "(\\d{4})-?(\\d{2})-?(\\d{2})";
				// The time in HH:MM:SS[.uuuu] or HHMMSS[.uuuu] format
				var timere = "(\\d{2}):?(\\d{2}):?(\\d{2}(?:\\.\\d+)?)";
				// The timezone as Z or in +HH[:MM] or -HH[:MM] format
				var tzre = "(Z|(?:\\+|-)\\d{2}(?:\\:\\d{2})?)?";
				this.re = new RegExp("^" + datere + "[ T]" + timere + tzre
						+ "$");
			}

			var matches = this.re.exec(datestr);
			if (!matches)
				return null;

			var year = matches[1];
			var month = matches[2] - 1;
			var day = matches[3];
			var hour = matches[4];
			var minute = matches[5];
			var second = Math.floor(matches[6]);
			var ms = matches[6] - second;
			var tz = matches[7];
			var ms = 0;
			var offset = 0;

			if (tz && tz != "Z") {
				var tzmatches = tz.match(/^(\+|-)(\d{2})(\:(\d{2}))$/);
				if (tzmatches) {
					offset = Number(tzmatches[2]) * 60 + Number(tzmatches[4]);
					if (tzmatches[1] == "-")
						offset = -offset;
				}
			}

			offset *= 60 * 1000;
			var dateval = Date.UTC(year, month, day, hour, minute, second, ms)
					- offset;

			return new Date(dateval);
		},
		/**
		 * {qx.util.format.DateFormat} eSciDoc data format standard
		 */
		__eSciDocDate : new qx.util.format.DateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS+ZZ:ZZ", "en_US")
	}
});