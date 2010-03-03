qx.Class.define("org.escidoc.admintool.tabview.TabView",
{
  extend: qx.ui.tabview.TabView,
  construct: function(title, description)
  {
    this.base(arguments);
    this.setMinWidth(360);
    this.setPadding(20);

    if(title){
      this.title = title;
    }
	
    if(description){
      this.description = description;
    }

    this.pageInfo = new qx.ui.tabview.Page(this.title, this.icon);
    this.pageInfo.setLayout(new qx.ui.layout.VBox(5));
    this.pageInfo.html = new qx.ui.embed.Html();
    var htmlString = "<h1>" + this.title + "</h1><p>"+ this.description +"</p><p>... soon!</p>";
    this.pageInfo.html.setHtml(htmlString);
    this.pageInfo.html.setHeight(110);
    this.pageInfo.add(this.pageInfo.html);
    this.add(this.pageInfo);

  },
  members : {
    title : "Info",
    icon : "icon/16/apps/utilities-notes.png",
    description : "This is the info page of an admintool tab view.",
    pageInfo : null
  }
});
