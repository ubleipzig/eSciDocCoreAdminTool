qx.Class.define("org.escidoc.admintool.tabview.page.Resource", {
    extend: qx.ui.tabview.Page,
    construct: function(){
        this.base(arguments, this.title, this.icon);
        this.setLayout(new qx.ui.layout.VBox());
        this.setShowCloseButton(true);
        var container = this.createContainer();
        this.add(container);
        this.addToolbar(container);
        this.add(this.createTable());
        this.bind("title", this, "label");
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
        createTable: function(){
            // Create the initial data
            var rowData = this.createRandomRows(10);
            
            // table model
            var tableModel = new qx.ui.table.model.Simple();
            
            tableModel.setColumns(["Resource ID", "Name", "Created On", "Active", "a Column", "a Column", "a Column", "a Column", "a Column", "a Column"]);
            tableModel.setData(rowData);
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
        createRandomRows: function(numberOfRows){
            var now = new Date().getTime();
            var dateRange = 365 * 24 * 60 * 60 * 1000;
            var userId = 0;
            
            var rowData = [];
            for (var rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
                //What this line of code actually doing? => refactor to a function
                var date = new Date(now + Math.random() * dateRange - dateRange / 2);
                rowData.push([userId++, Math.random() * 10000, date, (Math.random() > 0.5)]);
            }
            return rowData;
        }
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
            init: "A Resource",
            check: "String",
            nullable: false,
            event: "changeTitle"
        }
    }
});
