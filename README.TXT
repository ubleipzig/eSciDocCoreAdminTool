Admin Tool Web Application/User Interface

Requirements
	I, as a system administrator, can CRUD following resources:
		-user accounts
		-organizational units
		-....
	I, as a sys admin, can multi-select resources and delete them.

@next actions
	creating a parser to parse JSON sent by eSciDoc infrastructure.
		parse BadgerFish JSON without namespaces.
		Resource: http://news.qooxdoo.org/parsing-atom-feeds
	create multi-selection of resources for delete.
		committed. However, the current implementation is not intuitive for
		users. Better, add a check box in the first column of each rows.
		add modal dialog to confirm the user.
			"Are you sure want to delete resource a,b,c, ..."		
	create edit button in resource table
		according to [1], we should use HTML renderer to create a button.
		NOTES:
			work around: Buttons are in the toolbar.
	create login form.
		validate input
		mask password
	load data
		load static JSON data from file system
			experiment with user account.
			create a wrapper class for this.
				@See: UserAccount.js
		load data from server
			depends on: eSciDocXml2Json Service
	send data to the server
		depends on: eSciDocXml2Json Service
	R+D Smart Table Model[3], as subtitute for Qooxdoo Simple 
		Table Model
	add destructor in every classes.
		
Known bugs
	[Old] when the user navigate between folders, the tab size changes.
	
Notes
	related:INFR-800 Creator VS Owner.
	existing solutions using Qooxdoo
		"CAS PIA, " http://www.cas-pia.de/en/try.html
			great example, a lot of different features.
		"Bibliograph, "rsoz.rewi.hu-berlin.de/~baer/bibliograph/frontend/build/
		"Zed Build and Bugs, " http://demo.hericus.com 
		"Agrammon, " http://model.agrammon.ch
		
Features
	Manual ingest using user defined identifier.
	Check if a existing user with exact/similar login or name already exists
		in repository.

Lessons Learned(/QooxDoo BestPractices)
	Wrapping Backend Data[2]
	
References:

[1] "Re: [qooxdoo-devel] [SPAM] Buttons on a table, "
http://www.mail-archive.com/qooxdoo-devel@lists.sourceforge.net/msg19622.html

[2] "Wrapping Backend Data, " 
http://qooxdoo.org/documentation/1.0/enterprise_application_development#wrapping_backend_data

[3] "Smart Table Model, " 
http://qooxdoo.org/contrib/project/smarttablemodel