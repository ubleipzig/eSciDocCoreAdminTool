Admin Tool Web Application/User Interface

Requirements
	I, as a system administrator, can CRUD following resources:
		-user accounts
		-organizational units
		-....
	I, as a sys admin, can multi select resources and delete them.

TODO
	add multi-selection of resources for delete.
	add edit button in resource table
		according to [1], we should use HTML renderer to create a button.
	add admin login form.
	load data
		load static JSON data from file system
		load data from server
			depends on: eSciDocXml2Json Service
	
Notes
	related:INFR-800 Creator VS Owner.
	
Features
	Manual ingest using user defined identifier.

References:

[1] "Re: [qooxdoo-devel] [SPAM] Buttons on a table, "
http://www.mail-archive.com/qooxdoo-devel@lists.sourceforge.net/msg19622.html