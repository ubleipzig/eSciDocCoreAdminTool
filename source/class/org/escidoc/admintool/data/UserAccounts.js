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
qx.Class.define("org.escidoc.admintool.data.UserAccounts", {
	statics : {
		getData : function() {
			return this.__data;
		},
		retrieveDataFromEscidoc : function() {
			// setup the Remote Request.
			// define POST or GET
			// define Accept MIME TYPE PARAM
			// define server and host ==> define Resource URI
			var RESOURCE_URI = "http://localhost:8181/v1.2/users";
			var request = new qx.io.remote.Request(RESOURCE_URI, "GET",
					"application/json");
			request.setCrossDomain(true);

			// timeout
			request.setTimeout(9999999999);
			// register a listerner for completed event.
			request.addListener("completed", function(response) {
				return response.getContent();

					// return json;
					// var tmp = json['userAccounts'];
					// for (var index = 0; index < tmp.length; index++) {
					// this
					// .debug("creationDate:"
					// + tmp[index]['properties']['creationDateAsString']);
					// var creationDate =
					// org.escidoc.admintool.io.JsonParser
					// .__isoDateStringToDate(tmp[index]['properties']['creationDateAsString']);
					//
					// this.debug(creationDate.toString());
					// }
					// var userAccountArray =
					// org.escidoc.admintool.io.JsonParser
					// .parseUserAccounts(json);
					// for (var index = 0; index < userAccountArray.length;
					// index++) {
					// this.debug(userAccountArray[index].getLoginName());
					// this.debug(userAccountArray[index].getName());
					// this.debug(userAccountArray[index].getIsActive());
					// this.debug(userAccountArray[index]
					// .getCreationDate().toString());
					// }
				}, this);

			// // define error and time out handling function
			// // define what happens after succesful request.
			// finally send the request to the server.
			request.send();
		},
		__data : {
			"user-account-list" : {
				"@type" : "simple",
				"@title" : "User Account List",
				"@base" : "http://localhost:8080",
				"user-account" : [{
					"@type" : "simple",
					"@title" : "System Administrator User",
					"@href" : "/aa/user-account/escidoc:exuser1",
					"@last-modification-date" : "2010-02-10T10:04:08.688Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:08.688Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "System Administrator User"
						},
						"login-name" : {
							"$" : "sysadmin"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:exuser1/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:exuser1/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:exuser1/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "System Inspector User (Read Only Super User)",
					"@href" : "/aa/user-account/escidoc:exuser2",
					"@last-modification-date" : "2010-02-10T10:04:08.688Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:08.688Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "System Inspector User (Read Only SuperUser)"
						},
						"login-name" : {
							"$" : "sysinspector"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:exuser2/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:exuser2/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:exuser2/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "Depositor User",
					"@href" : "/aa/user-account/escidoc:exuser4",
					"@last-modification-date" : "2010-02-10T10:04:08.688Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:08.688Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "Depositor User"
						},
						"login-name" : {
							"$" : "depositor"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:exuser4/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:exuser4/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:exuser4/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "Ingestor User",
					"@href" : "/aa/user-account/escidoc:exuser5",
					"@last-modification-date" : "2010-02-10T10:04:08.688Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:08.688Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "Ingestor User"
						},
						"login-name" : {
							"$" : "ingester"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:exuser5/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:exuser5/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:exuser5/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "Collaborator User",
					"@href" : "/aa/user-account/escidoc:exuser6",
					"@last-modification-date" : "2010-02-10T10:04:08.688Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:08.688Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "Collaborator User"
						},
						"login-name" : {
							"$" : "collaborator"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:exuser6/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:exuser6/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:exuser6/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "roland",
					"@href" : "/aa/user-account/escidoc:user42",
					"@last-modification-date" : "2010-02-10T10:04:10.266Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:10.266Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "roland"
						},
						"login-name" : {
							"$" : "roland"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:user42/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:user42/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:user42/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "Inspector (Read Only Super User)",
					"@href" : "/aa/user-account/escidoc:user44",
					"@last-modification-date" : "2010-02-10T10:04:10.266Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:10.266Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "System Administrator User",
							"@href" : "/aa/user-account/escidoc:exuser1"
						},
						"name" : {
							"$" : "Inspector (Read Only Super User)"
						},
						"login-name" : {
							"$" : "inspector"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:user44/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:user44/resources/current-grants"
						},
						"preferences" : {
							"@type" : "simple",
							"@title" : "Preferences",
							"@href" : "/aa/user-account/escidoc:user44/resources/preferences"
						},
						"attributes" : null
					}
				}, {
					"@type" : "simple",
					"@title" : "Test Depositor Scientist",
					"@href" : "/aa/user-account/escidoc:user1",
					"@last-modification-date" : "2010-02-10T10:04:10.266Z",
					"properties" : {
						"creation-date" : {
							"$" : "2010-02-10T10:04:10.266Z"
						},
						"created-by" : {
							"@type" : "simple",
							"@title" : "Test Depositor Scientist",
							"@href" : "/aa/user-account/escidoc:user1"
						},
						"modified-by" : {
							"@type" : "simple",
							"@title" : "Test Depositor Scientist",
							"@href" : "/aa/user-account/escidoc:user1"
						},
						"name" : {
							"$" : "Test Depositor Scientist"
						},
						"login-name" : {
							"$" : "test_dep_scientist"
						},
						"active" : {
							"$" : true
						}
					},
					"resources" : {
						"@type" : "simple",
						"@title" : "Virtual Resources",
						"@href" : "/aa/user-account/escidoc:user1/resources",
						"current-grants" : {
							"@type" : "simple",
							"@title" : "Current Grants",
							"@href" : "/aa/user-account/escidoc:user1/resources/..."
						}
					}
				}]
			}
		}
	}
});