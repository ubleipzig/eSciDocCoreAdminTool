qx.Class.define("org.escidoc.admintool.tabview.Experiments",
{
  extend: org.escidoc.admintool.tabview.TabView,
  construct: function()
  {
    this.base(arguments, "User Accounts", "This view shows existing user accounts and allows to"
	+" create, update and delete such accounts.");

    /*
      enhance info page
    */

    /*
      New Experiment Button
    */
    var addNewUserButton = new qx.ui.form.Button("Add new user");
    addNewUserButton.addListener("execute", this.addNewUserTab, this);
    this.pageInfo.add(addNewUserButton);

    /*
      load experiments from infrastructure
    */
  },

  members : {

    addNewUserTab : function(){
      var tab = new org.escidoc.admintool.tabview.page.Experiment();
      this.add(tab);
      this.setSelection([tab]);
    }
  }

});
