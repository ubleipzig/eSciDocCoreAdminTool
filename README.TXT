Admin Tool Web Application/User Interface

Requirements
	I, as a system administrator, can CRUD following resources:
		-user accounts
		-organizational units
		-....
	I, as a sys admin, can multi select resources and delete them.

TODO
	add multi-selection of resources for delete.
		commited. However, the current implementation is not intuitive for
		users. Better, add a check box in the first column of each rows.
		
		add modal dialog to confirm the user.
			"Are you sure want to delete resource a,b,c, ..."		
	add edit button in resource table
		according to [1], we should use HTML renderer to create a button.
	
	add admin login form.
	load data
		load static JSON data from file system
			create a wrapper class for this.
		load data from server
			depends on: eSciDocXml2Json Service

Known bugs
	when the user navigate between folders, the tab size changes.
	
Notes
	related:INFR-800 Creator VS Owner.
	
Features
	Manual ingest using user defined identifier.
	Check if a existing user with exact/similar login or name already exists
		in repository.
	
References:

[1] "Re: [qooxdoo-devel] [SPAM] Buttons on a table, "
http://www.mail-archive.com/qooxdoo-devel@lists.sourceforge.net/msg19622.html