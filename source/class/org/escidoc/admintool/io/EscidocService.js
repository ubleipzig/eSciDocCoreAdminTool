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
qx.Class.define("org.escidoc.admintool.io.EscidocService", {
			extend : qx.core.Object,
			properties : {
				tweets : {
					nullable : true,
					event : "changeTweets"
				}
			},
			members : {
				__store : null,
				retrieveUserAccountsUsingJsonpStore : function() {
					if (this.__store == null) {
						var url = "http://localhost:8181/v1.2/users";
						this.__store = new qx.data.store.Jsonp(url, null,
								"callback");
						// more to do
						this.__store.bind("model", this, "tweets");
					} else {
						this.__store.reload();
					}
				},
				retrieveUserAccounts : function() {
				}
			}
		});