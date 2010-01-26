/**
 * @author CHH
 */
qx.Class.define("org.escidoc.admintool.tabview.page.NewResource", {
    extend: qx.ui.tabview.Page,
    construct: function(id){
        this.base(arguments, this.title, this.icon);
        this.setLayout(new qx.ui.layout.HBox());
        this.setShowCloseButton(true);
        
        var container = new qx.ui.container.Composite();
        container.forms = new Array();
        container.setLayout(new qx.ui.layout.VBox());
        this.add(container);
        
        this.bind("title", this, "label");
        
        // Page display consists of several Forms
        
        /*
         Form containing User informations
         */
        this.form = new qx.ui.form.Form();
        container.forms.push(this.form);
        
        // name
        var tfName = new qx.ui.form.TextField(this.getTitle());
        tfName.setRequired(true);
        tfName.setWidth(400);
        this.form.add(tfName, "Name", org.escidoc.admintool.tabview.page.Experiment.DEFAULT_VALIDATOR, "name");
        tfName.bind("value", this, "title");
        
        var loginName = new qx.ui.form.TextField();
        loginName.setRequired(true);
        loginName.setWidth(400);
        this.form.add(loginName, "Login Name", org.escidoc.admintool.tabview.page.Experiment.DEFAULT_VALIDATOR, "login-name");
        //loginName.bind("value", this, "login-name"); */
        
		// mail
        var tfEmail = new qx.ui.form.TextField();
        this.form.add(tfEmail, "Email", qx.util.Validate.email(), "email");
        tfEmail.bind("value", this, "email");     
        
        // description
        var tfDescription = new qx.ui.form.TextArea();
        //tfDescription.setWidth(400);
        this.form.add(tfDescription, "Description", org.escidoc.admintool.tabview.page.Experiment.DEFAULT_VALIDATOR, "description");
        tfDescription.bind("value", this, "description");
        
        // context aka workspace
        var tfWorkspace = new qx.ui.form.TextField();
        tfWorkspace.setRequired(true);
        this.form.add(tfWorkspace, "Workspace", org.escidoc.admintool.tabview.page.Experiment.DEFAULT_VALIDATOR, "contextId");
        tfWorkspace.bind("value", this, "contextId");
        tfWorkspace.setValue("escidoc:persistent3");
        
        // eSyncDaemon
        var tfESyncDaemon = new qx.ui.form.TextField();
        tfESyncDaemon.setRequired(true);
        this.form.add(tfESyncDaemon, "eSyncDaemon URL", qx.util.Validate.url(), "eSyncDaemonUrl");
        tfESyncDaemon.bind("value", this, "eSyncDaemonUrl");
        
        // Deposit Service
        var tfDepositService = new qx.ui.form.TextField();
        tfDepositService.setRequired(true);
        this.form.add(tfDepositService, "Deposit Service URL", qx.util.Validate.url(), "depositServiceUrl");
        tfDepositService.bind("value", this, "depositServiceUrl");

        var formView = new qx.ui.form.renderer.Single(this.form);
        container.add(formView);
        
        /*
         Box containing informations of one Monitored Folder
         Later several folders should be supported
         */
        var folderBox = new qx.ui.groupbox.GroupBox("Folder");
        folderBox.setLayout(new qx.ui.layout.VBox());
        
        /*
         Form with Monitored Folder information and Buttons for test,
         start, and stop.
         */
        var folderForm = new qx.ui.form.Form();
        container.forms.push(folderForm);
        
        // Monitored Folder
        var tfMonitoredFolder = new qx.ui.form.TextField();
        tfMonitoredFolder.setRequired(true);
        tfMonitoredFolder.setWidth(250);
        folderForm.add(tfMonitoredFolder, "Monitored Folder", org.escidoc.admintool.tabview.page.Experiment.DEFAULT_VALIDATOR, "monitoredFolder");
        tfMonitoredFolder.bind("value", this, "monitoredFolder");
        
        // content model
        var tfContentModel = new qx.ui.form.TextField();
        tfContentModel.setRequired(true);
        tfContentModel.setWidth(100);
        folderForm.add(tfContentModel, "Content Model", org.escidoc.admintool.tabview.page.Experiment.DEFAULT_VALIDATOR, "contentModelId");
        tfContentModel.bind("value", this, "contentModelId");
        tfContentModel.setValue("escidoc:persistent4");
        
        /*
         Box containing Folders minitoring start time
         */
        var startTimeBox = new qx.ui.groupbox.GroupBox("Monitoring Start Time");
        startTimeBox.setLayout(new qx.ui.layout.HBox(5));
        
        // start date
        var dfStartDate = new qx.ui.form.DateField();
        //dfStartTime.setDateFormat(new qx.util.format.DateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSSZ", "de"));
        dfStartDate.setDateFormat(new qx.util.format.DateFormat("yyyy'-'MM'-'dd", "de"));
        dfStartDate.setPlaceholder("manually");
        dfStartDate.addListener("changeValue", function(e){
            this.startButton.setEnabled(false);
        }, container);
        startTimeBox.add(dfStartDate);
        
        // start time
        var ts = new org.escidoc.admintool.TimeSpinner(25);
        startTimeBox.add(ts);
        
        /* 
         Box containing Folders monitoring duration
         */
        var durationBox = new qx.ui.groupbox.GroupBox("Monitoring Duration");
        durationBox.setLayout(new qx.ui.layout.HBox());
        
        /*
         Form with duration hours and minutes
         */
        var durationForm = new qx.ui.form.Form();
        container.forms.push(durationForm);
        
        // hours duration
        var sHours = new qx.ui.form.Spinner(0, 12, 999);
        durationForm.add(sHours, "Hours", qx.util.Validate.number(), "durationHours");
        
        // minutes duration
        var sMinutes = new qx.ui.form.Spinner(0, 0, 59);
        durationForm.add(sMinutes, "Minutes", qx.util.Validate.range(0, 59), "durationMinutes");
        
        durationBox.add(new qx.ui.form.renderer.Double(durationForm));
        
        /*
         Buttons
         */
        // test configuration
        var buttonTest = new qx.ui.form.Button("Test Configuration");
        buttonTest.addListener("execute", this._testConfiguration, container);
        container.testButton = buttonTest;
        folderForm.addButton(buttonTest);
        
        // start monitoring
        var buttonStart = new qx.ui.form.Button("Start Monitoring");
        buttonStart.setEnabled(false);
        buttonStart.addListener("execute", function(e){
            this.startButton.setEnabled(false);
            this.stopButton.setEnabled(true);
        }, container);
        container.startButton = buttonStart;
        folderForm.addButton(buttonStart);
        
        // stop monitoring
        var buttonStop = new qx.ui.form.Button("Stop Monitoring");
        buttonStop.addListener("execute", function(e){
            this.startButton.setEnabled(true);
            this.stopButton.setEnabled(false);
        }, container);
        buttonStop.setEnabled(false);
        container.stopButton = buttonStop;
        folderForm.addButton(buttonStop);
        
        folderBox.add(new qx.ui.form.renderer.Double(folderForm));
        folderBox.add(startTimeBox);
        folderBox.add(durationBox);
        container.add(folderBox);
        
    },    
    statics: {
        DEFAULT_VALIDATOR: null
    },
    properties: {
        id: {
            init: "",
            check: "String",
            nullable: false,
            event: "changeId"
        },
        title: {
            init: "New User",
            check: "String",
            nullable: false,
            event: "changeTitle"
        },  
        description: {
            init: "",
            check: "String",
            nullable: false,
            event: "changeDescription"
        },       
        contextId: {
            init: "escidoc:persistent3",
            check: "String",
            nullable: false,
            event: "changeContextId"
        },   
        eSyncDaemonUrl: {
            init: "",
            check: "String",
            nullable: false,
            event: "changeESyncDaemonUrl"
        },
        
        depositServiceUrl: {
            init: "",
            check: "String",
            nullable: false,
            event: "changeDepositServiceUrl"
        },
        
        infrastructureUrl: {
            init: "http://localhost:8080",
            check: "String",
            nullable: false,
            event: "changeInfrastructureUrl"
        },
        
        email: {
            init: "",
            check: "String",
            nullable: false,
            event: "changeEmail"
        },
        
        contentModelId: {
            init: "escidoc:persistent4",
            check: "String",
            nullable: false,
            event: "changeContentModelId"
        },
        
        monitoredFolder: {
            init: "",
            check: "String",
            nullable: false,
            event: "changeMonitoredFolder"
        },
        
        startTime: {
            // XML compatible date-time-string
            init: null,
            check: "String",
            nullable: true,
            event: "changeStartTime"
        },
        
        duration: {
            // in minutes
            init: 720,
            check: "Number",
            nullable: false,
            event: "changeDuration"
        }
    },
    
    members: {
        icon: "icon/16/apps/utilities-system-monitor.png",
        form: null,
        
        _testConfiguration: function(e){
            for (var i = 0; i < this.forms.length; i++) {
                if (!this.forms[i].validate()) {
                    alert("Form validation failed!");
                    this.startButton.setEnabled(false);
                    //return;
                }
            }
            
            var container = this.getLayoutParent();
            var msg = "Experiment[" +
            "name[" +
            container.getTitle() +
            "]" +
            "description[" +
            container.getDescription() +
            "]" +
            "id[" +
            container.getId() +
            "]" +
            "workspace[" +
            container.getContextId() +
            "]" +
            "eSyncDaemonUrl[" +
            container.getESyncDaemonUrl() +
            "]" +
            "depostiServiceUrl[" +
            container.getDepositServiceUrl() +
            "]" +
            "email[" +
            container.getEmail() +
            "]" +
            "monitoredFolder[" +
            container.getMonitoredFolder() +
            "]" +
            "contentModel[" +
            container.getContentModelId() +
            "]" +
            "startTime[" +
            container.getStartTime() +
            "]" +
            "duration[" +
            container.getDuration() +
            "]" +
            "]";
            alert(msg);
            
            // on success enable start button
            this.startButton.setEnabled(true);
        }
    }

});
