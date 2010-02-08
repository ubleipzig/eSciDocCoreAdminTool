/**
 * @author CHH
 */
qx.Class.define("org.escidoc.admintool.wrapper.UserAccount", {
			extend : qx.core.Object,
			construct : function(data) {
				this._data = data;
			},
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