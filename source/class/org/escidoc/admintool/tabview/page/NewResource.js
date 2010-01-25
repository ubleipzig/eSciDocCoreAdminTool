qx.Class.define("org.escidoc.admintool.tabview.page.NewResource", {
    extend: qx.ui.tabview.Page,
    construct: function(){
        this.base(arguments, this.title, this.icon);
        this.setLayout(new qx.ui.layout.HBox());
        this.setShowCloseButton(true);
        
        var container = this.createContainer();
        
        this.add(container);
        this.addToolbar(container);
        
        this.bind("title", this, "label");
    },
    
    statics: {
        DEFAULT_VALIDATOR: null
    },
    
    members: {
        icon: "icon/16/apps/utilities-system-monitor.png",
        form: null,
        createContainer: function(){
            var container = new qx.ui.container.Composite();
            container.setLayout(new qx.ui.layout.VBox());
            return container;
        },
        addToolbar: function(container){
            var toolbar = new qx.ui.toolbar.ToolBar();
            var createNewResourceButton = new qx.ui.toolbar.Button("Create New Resource");
            createNewResourceButton.addListener("execute", function(){
				this.fireEvent("addNewResource");
			}, this);
            toolbar.add(createNewResourceButton);
            container.add(toolbar);
            return container;
        },
    },
    events: {
        "addNewResource": "qx.event.type.Data"
    },
    properties: {
        id: {
            init: "",
            check: "String",
            nullable: false,
            event: "changeId"
        },
        title: {
            init: "New Resource",
            check: "String",
            nullable: false,
            event: "changeTitle"
        }
    }
});
