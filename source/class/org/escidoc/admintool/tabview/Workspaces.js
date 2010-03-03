qx.Class.define("org.escidoc.admintool.tabview.Workspaces",
{
  extend: org.escidoc.admintool.tabview.TabView,
  construct: function()
  {
    this.base(arguments, "Workspaces", "This view shows existing workspaces and allows to create new ones.");

    /*
      load experiments from infrastructure
    */
    var exp = new qx.ui.tabview.Page("Workspace 1", "icon/16/apps/utilities-system-monitor.png");
    exp.setLayout(new qx.ui.layout.VBox());
    exp.setShowCloseButton(true);
    exp.html = new qx.ui.embed.Html();
    exp.html.setHtml("<h1>Workspace 1</h1><p>This tab shows an existing workspace.</p><p>... soon!</p>");
    exp.add(exp.html, {flex:1});
    this.add(exp);
  },

  members : {
  }

});
