eSciDoc Admin Tool Documentation

Who are the users of the software?
	Administrator of eSciDoc Infrastructure 

SWA begin 

I think if we design a graphical interface for a user with administration permission than we have all functionality for a non-administrative user. SWA end 
What are the users' task and goal?
Manage eSciDoc resources, e.g. Organizational Units, Contexts, Item, etc, and user-related resources, e.g. User Accounts, Grants, Roles, etc. 


SWA begin 

I think here is to distinguish between administrative and non-administrative users. 
administrative users: 
your list, plus 
re-cache/re-index 
manage infrastrucutre configuration (yes, I mean these things from the config files) 
purge/delete resources (a real delete) 
(in the future) set to read only modus 
trigger sanity check and display results 
display warnings and system inconsistencies (all this stuff which helps administrators) 
(maybe) support backup 
non-administrative users: 
resource management (CRUD) 
search/filter 
comparing of resources (at least displaing side by side, section by section) 

Maybe loading of a example data set (not pretty sure if this is only an administrative task). SWA end 

MRA: Excellent list, but Christian was asking for "minimal requirements". I think this list is rather the fully-fledged tool with bells and whistles. So your list rather describes the ultimate goal, not the first or maybe second iteration. 

So, here is my list: 
Manage Organizational Units (complying with MPDL metadata schema), including parent/child and predecessor/successor relations 
Manage Contexts, 
including a free text field for every AdminDescriptor (AdminTool will only ensure if entered AdminDescriptor is well-formed) 
including simple GUI-based referencing of OUs 
Manage User Accounts 
including simple GUI-based referencing of OUs 
including preferences 
including simple GUI-based granting of roles (and scopes?) 
Manage User Groups 
including simple GUI-based granting of roles (and scopes?) 
Manage Roles 
plus integration of all functionality of the current AdminTool 

Next iteration: 
Manage Content Models (define them!) 
Manage Items, Containers, and ContentRelations (both light-weight and full-object ContentRelations) 
Search/browse in repository (Contexts/Containers/Items) 

Next iteration: 
Manage Search Configuration (XSL stylesheets) 
Manage infrastrucutre configuration (I always wanted to have that, but I've been told that this would require restarting JBoss, which of course would end the AdminTool session, if possible at all) 
User Stories[1]
Starting Application. The application begins by showing the login window. 
Closing? Application. If the user clicks the button with the label "logout", the application shows the login window again. 
If the authentication fails, the application shows an appropriate message to the user. 
As an administrative user, if she clicks the label "Organization Unit" in navigation tree, she can see all stored organization units showing their title and XXX in the list view. 
As an administrative user, if she clicks the label "User Accounts" in navigation tree, she can see all stored user account showing their name, login name and XXX in the list view. 
As an administrative user, if she clicks the label "Contexts" in navigation tree, she can see all stored context showing their XXX in the list view. 
As an administrative user, if she select on of the user account in the list view, she can see following non-editable properties in the detail view: objid, creation-date, creator, modified-by, last-modification-date, XXX, name, login-name, active, assigned-roles. If she click the button with the label “edit”, she can change following properties: name, login-name, active, assigned-roles, XXX. While she in edit mode, she can click the button with the label "cancel" to discard all the changes. 
As an administrative user, if she click the button with the label "Add User", the application shows a "Add User Form" with following required fields: name, login-name. If the entered login-name already exists in the repository, the application will show an error message "Login name X already exist." If she submit the form without filling one of the required fields, the application will show an error message "Please fill the X". 

@INBOX:
[] 9.6.2010 Mavenize Project
[] 9.6.2010 replace sysout with log
[] 9.6.2010 orgUnit.getCreator can not return UserAccount?

References:
[1] "User Story, " http://en.wikipedia.org/wiki/User_story