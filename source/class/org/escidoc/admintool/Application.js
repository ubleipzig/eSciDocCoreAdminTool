/* ************************************************************************
 Copyright:
 License:
 Authors:
 ************************************************************************ */
/* ************************************************************************
 #asset(org/escidoc/admintool/*)
 #asset(qx/icon/${qx.icontheme}/16/apps/*)
 ************************************************************************ */
/**
 * This is the main application class of your custom application "AdminTool"
 */
qx.Class.define("org.escidoc.admintool.Application", {
    extend: qx.application.Standalone,
    /*
     *****************************************************************************
     MEMBERS
     *****************************************************************************
     */
    members: {
        /**
         * This method contains the initial application code and gets called
         * during startup of the application
         *
         * @lint ignoreDeprecated(alert)
         */
        main: function(){
            // Call super class
            this.base(arguments);
            
            // Enable logging in debug variant
            if (qx.core.Variant.isSet("qx.debug", "on")) {
                // support native logging capabilities, e.g. Firebug for Firefox
                qx.log.appender.Native;
                // support additional cross-browser console. Press F7 to toggle visibility
                qx.log.appender.Console;
            }
            
            /*
             -------------------------------------------------------------------------
             Below is your actual application code...
             -------------------------------------------------------------------------
             */
            /*
             admintool Scene
             */
            var scroller = new qx.ui.container.Scroll();
            this.container = new qx.ui.container.Composite(new qx.ui.layout.HBox(10, "center"));
            scroller.add(this.container);
            
            // Document doc is the application root
            var doc = this.getRoot();
            doc.add(scroller, {
                edge: -1
            });
            
            // container is layout root
            var theTree = this.getTree();
            this.container.add(theTree, {
                flex: 1
            });
            this.container.add(theTree.getRoot().tabView, {
                flex: 5
            });
            
            this.showFolderView(theTree.getRoot());
        },
        
        /*
         Create the Tree widget for navigation.
         */
        getTree: function(){
            var navigationTree = new qx.ui.tree.Tree();
            navigationTree.setMinWidth(100);
            
            var adminToolRootFolder = new qx.ui.tree.TreeFolder("Admin Tool");
            adminToolRootFolder.tabView = new org.escidoc.admintool.tabview.TabView("Admin Tool", "Admin Tool is an eSciDoc solution supporting management of user accounts, organizational units, etc.");
            adminToolRootFolder.setOpen(true);
            navigationTree.setRoot(adminToolRootFolder);
            
            //TODO: replace all comments with a function or class.
            
            //Resources
            var resourceFolder = new qx.ui.tree.TreeFolder("Resources");
            resourceFolder.tabView = new qx.ui.tabview.TabView();
            resourceFolder.tabView.setMinWidth(360);
            resourceFolder.tabView.setPadding(20);
            resourceFolder.setOpen(true);
            
            var resourcePageInfo = new qx.ui.tabview.Page("Resources", "icon/16/apps/utilities-notes.png");
            resourcePageInfo.setLayout(new qx.ui.layout.VBox());
            resourcePageInfo.html = new qx.ui.embed.Html();
            resourcePageInfo.html.setHtml("<h1>Resources</h1><p>You can manage ....</p><p>... soon!</p>");
            resourcePageInfo.add(resourcePageInfo.html, {
                flex: 1
            });
            resourceFolder.tabPage = resourcePageInfo
            resourceFolder.tabView.add(resourceFolder.tabPage);
            
            //User Accounts
            var userAccountsTreeFile = new qx.ui.tree.TreeFile("User Accounts");
            var userAccountIcon = "icon/16/apps/utilities-system-monitor.png";
            userAccountsTreeFile.setIcon(userAccountIcon);
            userAccountsTreeFile.tabPage = new qx.ui.tabview.Page("User Accounts", userAccountIcon);
            resourceFolder.tabView.add(userAccountsTreeFile.tabPage);
            resourceFolder.add(userAccountsTreeFile);
            
            //Custom Resource to be added.
            var aResourceTreeFile = new qx.ui.tree.TreeFile("a Resource");
            aResourceTreeFile.setIcon(userAccountIcon);
            resourceFolder.add(aResourceTreeFile);
            
            //Tab page for aResource
            aResourceTreeFile.tabPage = new qx.ui.tabview.Page("A Resource", userAccountIcon);
            aResourceTreeFile.tabPage.setLayout(new qx.ui.layout.HBox());
            
            //add table to  resource Page.
            aResourceTreeFile.tabPage.add(this.createTable());

            resourceFolder.tabView.add(aResourceTreeFile.tabPage);
            adminToolRootFolder.add(resourceFolder);
            
            
            // listener
            navigationTree.addListener("changeSelection", function(e){
                var data = e.getData();
                var theClass = data[0].name;
                //alert("Got e: " + theClass + "[" + label + "]");
                
                if (theClass == "qx.ui.tree.TreeFolder") {
                    var folder = data[0];
                    this.showFolderView(folder, folder.tabPage);
                    //folder.tabPage.add(new qx.ui.embed.Html("x"));
                }
                else 
                    if (theClass == "qx.ui.tree.TreeFile") {
                        var entry = data[0];
                        var folder = entry.getParent();
                        // check if appropriate TabView is already shown
                        if (this.container.getChildren()[1] != folder.tabView) {
                            this.showFolderView(folder, entry.tabPage);
                        }
                        else {
                            folder.tabView.setSelection([entry.tabPage]);
                        }
                    }
                    else {
                        alert("Unknown class: " + theClass);
                    }
            }, this);
            return navigationTree;
        },
        
        createTable: function(){
            // Create the initial data
            //var rowData = this.createRandomRows(50);
            
            // table model
            var tableModel = new qx.ui.table.model.Simple();
            
            tableModel.setColumns(["ID", "A number", "A date", "Boolean"]);
            // tableModel.setData(rowData);
            tableModel.setColumnEditable(1, true);
            tableModel.setColumnEditable(2, true);
            tableModel.setColumnSortable(3, false);
            
            // table
            var table = new qx.ui.table.Table(tableModel);
            
            table.set({
                width: 600,
                height: 400,
                decorator: null
            });
            
            table.getSelectionModel().setSelectionMode(qx.ui.table.selection.Model.MULTIPLE_INTERVAL_SELECTION);
            
            var tcm = table.getTableColumnModel();
            
            // Display a checkbox in column 3
            tcm.setDataCellRenderer(3, new qx.ui.table.cellrenderer.Boolean());
            
            // use a different header renderer
            tcm.setHeaderCellRenderer(2, new qx.ui.table.headerrenderer.Icon("icon/16/apps/office-calendar.png", "A date"));
            
            return table;
        },
        
        /*
         Displays the appropriate tabs for a navigationTree entry.
         */
        showFolderView: function(folder, tab){
            var label = folder.getLabel();
            if (folder.tabView) {
                this.container.removeAt(1);
                this.container.add(folder.tabView, {
                    flex: 5
                });
                if (tab) {
                    var index = folder.tabView.indexOf(tab);
                    if (index < 0) {
                        folder.tabView.add(tab);
                    }
                    folder.tabView.setSelection([tab]);
                }
            }
            else {
                alert("Folder without view.");
            }
        }
    }
});
