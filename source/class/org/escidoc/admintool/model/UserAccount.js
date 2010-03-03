/**
 * @author CHH
 */
qx.Class.define("org.escidoc.admintool.model.UserAccount", {
			extend : qx.core.Object,
			// TODO: change nullable to true in production code.
			properties : {
				name : {
					check : "String",
					event : "changeName"
				},
				creationDate : {
					check : Date,
					event : "changeDate"
				},
				loginName : {
					check : "String",
					event : "changeLoginName"
				},
				isActive : {
					check : Boolean,
					event : "changeIsActive"
				},
				email : {
					check : "String",
					event : "changeEmail",
					nullable : true
				}
			},
			members : {
				toString : function() {
					return "[name: " + this.getName() + "]" + "[creationDate: "
							+ this.getCreationDate() + "]" + "[loginName: "
							+ this.getLoginName() + "]"+ "[isActive: "
                            + this.getIsActive() + "]"+ "[e-mail: "
							+ this.getEmail() + "]";
				}
				// how to compare equality? implement equals() and hashCode()?
			}
		});