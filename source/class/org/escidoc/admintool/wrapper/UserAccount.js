/**
 * @author CHH
 */
qx.Class.define("org.escidoc.admintool.wrapper.UserAccount", {
			extend : qx.core.Object,
			construct : function(data) {
				this._data = data;
                this.name = data.name;
			},
			properties : {
				name : {
					check : "String",
					event : "changeName",
					nullable : false
				},

				loginName : {
					check : "String",
					event : "changeLoginName"
				},
				email : {
					check : "Boolean",
					event : "changeEmail"
				}
			}
            ,
			members : {
				getName : function() {
					return this._data.name;
				},
				getLoginName : function() {
					return this._data.loginName;
				},
				getEmail : function() {
					return this._data.email;
				},
				toString : function() {
					return "[name: " + this.getName() + "]" + "[loginName: "
							+ this.getLoginName() + "]" + "[e-mail: "
							+ this.getEmail() + "]";
				}
			}
		});