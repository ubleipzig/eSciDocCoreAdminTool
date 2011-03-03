package de.escidoc.admintool.view;

import java.util.Arrays;
import java.util.List;

import de.escidoc.admintool.messages.Messages;

public class ViewConstants {

    // TODO move these to an extra file.
    public static final int VISIBLE_PARENTS = 7;

    public static final int SPLIT_POSITION_IN_PERCENT = 40;

    public static final String IDENTIFIER_LABEL = Messages
        .getString("ViewConstants.0"); //$NON-NLS-1$

    public static final String OBJECT_ID = Messages
        .getString("ViewConstants.1"); //$NON-NLS-1$

    public static final String OBJECT_ID_LABEL = Messages
        .getString("ViewConstants.2"); //$NON-NLS-1$

    public static final String TITLE_ID = Messages.getString("ViewConstants.3"); //$NON-NLS-1$

    public static final String NAME_ID = Messages.getString("ViewConstants.4"); //$NON-NLS-1$

    public static final String DESCRIPTION_ID = Messages
        .getString("ViewConstants.5"); //$NON-NLS-1$

    public static final String ALTERNATIVE_ID = Messages
        .getString("ViewConstants.6"); //$NON-NLS-1$

    public static final String CREATED_ON_ID = Messages
        .getString("ViewConstants.7"); //$NON-NLS-1$

    public static final String CREATED_BY_ID = Messages
        .getString("ViewConstants.8"); //$NON-NLS-1$

    public static final String PARENTS_ID = Messages
        .getString("ViewConstants.9"); //$NON-NLS-1$

    public static final String IDENTIFIER_ID = Messages
        .getString("ViewConstants.10"); //$NON-NLS-1$

    public static final String CITY_ID = Messages.getString("ViewConstants.11"); //$NON-NLS-1$

    public static final String COUNTRY_ID = Messages
        .getString("ViewConstants.12"); //$NON-NLS-1$

    public static final String ORG_TYPE_ID = Messages
        .getString("ViewConstants.13"); //$NON-NLS-1$

    public static final String ORGANIZATION_TYPE = Messages
        .getString("ViewConstants.14"); //$NON-NLS-1$

    public static final String LOGIN_NAME_ID = Messages
        .getString("ViewConstants.15"); //$NON-NLS-1$

    public static final String NAME_LABEL = Messages
        .getString("ViewConstants.16"); //$NON-NLS-1$

    public static final String LOGIN_NAME_LABEL = Messages
        .getString("ViewConstants.17"); //$NON-NLS-1$

    public static final String IS_ACTIVE_ID = Messages
        .getString("ViewConstants.18"); //$NON-NLS-1$

    public static final String MODIFIED_BY_ID = Messages
        .getString("ViewConstants.19"); //$NON-NLS-1$

    public static final Object MODIFIED_ON_ID = Messages
        .getString("ViewConstants.20"); //$NON-NLS-1$

    public static final String IS_ACTIVE_LABEL = Messages
        .getString("ViewConstants.21"); //$NON-NLS-1$

    public static final String CREATED_ON_LABEL = "Created on";

    public static final String CREATED_BY_LABEL = Messages
        .getString("ViewConstants.23"); //$NON-NLS-1$

    public static final String MODIFIED_ON_LABEL = "Modified on";

    public static final String MODIFIED_BY_LABEL = Messages
        .getString("ViewConstants.25"); //$NON-NLS-1$

    public static final String TITLE_LABEL = Messages
        .getString("ViewConstants.26"); //$NON-NLS-1$

    public static final String DESCRIPTION_LABEL = Messages
        .getString("ViewConstants.27"); //$NON-NLS-1$

    public static final String ALTERNATIVE_LABEL = "Alternative Title";

    public static final String TYPE_ID = Messages.getString("ViewConstants.29"); //$NON-NLS-1$

    public static final String TYPE_LABEL = Messages
        .getString("ViewConstants.30"); //$NON-NLS-1$

    public static final String COUNTRY_LABEL = Messages
        .getString("ViewConstants.31"); //$NON-NLS-1$

    public static final String CITY_LABEL = Messages
        .getString("ViewConstants.32"); //$NON-NLS-1$

    public static final String COORDINATES_ID = Messages
        .getString("ViewConstants.33"); //$NON-NLS-1$

    public static final String COORDINATES_LABEL = Messages
        .getString("ViewConstants.34"); //$NON-NLS-1$

    public static final String START_DATE_ID = Messages
        .getString("ViewConstants.35"); //$NON-NLS-1$

    public static final String START_DATE_LABEL = Messages
        .getString("ViewConstants.36"); //$NON-NLS-1$

    public static final String END_DATE_ID = Messages
        .getString("ViewConstants.37"); //$NON-NLS-1$

    public static final String END_DATE_LABEL = Messages
        .getString("ViewConstants.38"); //$NON-NLS-1$

    public static final String PARENTS_LABEL = Messages
        .getString("ViewConstants.39"); //$NON-NLS-1$

    public static final String PREDECESSORS_LABEL = Messages
        .getString("ViewConstants.40"); //$NON-NLS-1$

    public static final String PREDECESSORS_ID = Messages
        .getString("ViewConstants.41"); //$NON-NLS-1$

    public static final String PREDECESSORS_TYPE_LABEL = Messages
        .getString("ViewConstants.42"); //$NON-NLS-1$

    public static final String PREDECESSORS_TYPE_ID = Messages
        .getString("ViewConstants.43"); //$NON-NLS-1$

    public static final String CONTEXTS_LABEL = "Contexts";

    public static final String ORGANIZATION_UNITS_ID = Messages
        .getString("ViewConstants.45"); //$NON-NLS-1$

    public static final String ORGANIZATION_UNITS_LABEL = Messages
        .getString("ViewConstants.46"); //$NON-NLS-1$

    public static final String ADMIN_DESRIPTORS_LABEL = Messages
        .getString("ViewConstants.47"); //$NON-NLS-1$

    public static final String PUBLIC_STATUS_ID = Messages
        .getString("ViewConstants.48"); //$NON-NLS-1$

    public static final String PUBLIC_STATUS_LABEL = Messages
        .getString("ViewConstants.49"); //$NON-NLS-1$

    public static final String PUBLIC_STATUS_COMMENT_ID = Messages
        .getString("ViewConstants.50"); //$NON-NLS-1$

    public static final String PUBLIC_STATUS_COMMENT_LABEL = Messages
        .getString("ViewConstants.51"); //$NON-NLS-1$

    public static final String ADMIN_DESRIPTOR_NAME_ID = Messages
        .getString("ViewConstants.52"); //$NON-NLS-1$

    public static final String ADMIN_DESRIPTOR_NAME_LABEL = Messages
        .getString("ViewConstants.53"); //$NON-NLS-1$

    public static final Object ADMIN_DESRIPTORS_ID = Messages
        .getString("ViewConstants.54"); //$NON-NLS-1$

    public static final String ADD_LABEL = Messages
        .getString("ViewConstants.55"); //$NON-NLS-1$

    public static final String REMOVE_LABEL = Messages
        .getString("ViewConstants.56"); //$NON-NLS-1$

    public static final String OK_LABEL = Messages
        .getString("ViewConstants.57"); //$NON-NLS-1$

    public static final String CANCEL_LABEL = Messages
        .getString("ViewConstants.58"); //$NON-NLS-1$

    public static final String RIGHT_ARROW = Messages
        .getString("ViewConstants.59"); //$NON-NLS-1$

    public static final String DOWN_ARROW = Messages
        .getString("ViewConstants.60"); //$NON-NLS-1$

    public static final String DOWN_RIGHT_ARROW = Messages
        .getString("ViewConstants.61"); //$NON-NLS-1$

    public static final String ERROR_DIALOG_CAPTION = "Error";

    public static final String MAIN_WINDOW_TITLE = "eSciDoc Admin Tool";

    public static final String NO_TOKEN = "You are not logged in";

    public static final String WRONG_TOKEN_MESSAGE =
        "You are not authentificated";

    public static final String INVALID_TOKEN_ERROR_MESSAGE =
        "Invalid token, try again";

    public static final String SERVER_INTERNAL_ERROR = "Server Internal Error";

    // TODO put these in files or extra contents class,i.e. Label,
    public static final String ORGANIZATIONAL_UNIT = Messages
        .getString("NavigationTree.0"); //$NON-NLS-1$

    public static final String CONTEXTS = "Contexts";

    public static final String USERS = Messages.getString("NavigationTree.2"); //$NON-NLS-1$

    public static final String ROLE = "Roles";

    public static final String COMMENT = "Comment:";

    public static final String ORGANIZATIONAL_UNIT_LABEL =
        "Organizational Unit";

    public static final String EMPTY_STRING = "";

    public static final String ORG_UNIT_TREE = "OrgUnitTree";

    public static final String EDIT_USER_VIEW_CAPTION = "Edit User Account";

    public static final String[] ROLE_COLUMN_HEADERS = new String[] { "Title" };

    public static final String REQUESTED_ROLE_HAS_NO_SCOPE_DEFINITIONS =
        "Requested role has no scope-definitions";

    public static final String FIELD_WIDTH = "400px";

    public static final String MODAL_WINDOW_WIDTH = "460px";

    public static final String MODAL_WINDOW_HEIGHT = "200px";

    public static final int SPLIT_POSITION_FROM_LEFT = 200;

    public static final String CANCEL = "Cancel";

    public static final String SAVE_LABEL = "Save";

    public static final String EDIT = "Edit ";

    public static final String OPEN = "Open";

    public static final String CLOSE = "Close";

    public static final String PREDESSOR_TYPE = "Predessor Type";

    public static final String ADD_PARENT_LABEL = "Add Parent";

    public static final String DELETE = "Delete";

    public static final String NEW = "New";

    public static final String ROLES_LABEL = "Roles";

    public static final String AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS =
        "An unexpected error occured! See LOG for details.";

    public static final int LABEL_WIDTH = 140;

    public static final String WELCOMING_MESSAGE = "Welcome to esciDoc";

    public static final String ESCIDOC_URL_TEXTFIELD = "eSciDoc URL ";

    public static final String ERROR = "Error";

    public static final String CONTAINER = "Container";

    public static final String ADMIN_TASKS_LABEL = "Admin Tasks";

    public static final String LOAD_EXAMPLES = "Load Examples";

    public static final String SHOW_REPOSITORY_INFO = "Repository Information";

    public static final String FILTER_RESOURCES = "Filter Resources";

    public static final String PURGE = "Purge";

    public static final String NO_RESULT = "No result";

    public static final String LOGIN_LABEL = "Login";

    public static final String REINDEX = "Reindex";

    public static final String CLEAR_INDEX = "Clear Index";

    public static final String INDEX_NAME = "Index Name";

    public static final String FILTERED_RESOURCES = "Filtered Resources";

    public static final String LOAD_EXAMPLE_TITLE = "<h4>Load Example</h4>";

    public static final String FILTER_TEXT =
        "<p>Filters work on all resources, independent from their status in object lifecycle and existing access restrictions. Access policies are evaluated for each resource in the answer set. The answer set will only contain those resources the user has access to. Result sets consist of full object representations, support paging and sorting of results. Similar to searches, filter methods are based on the SRU standard, so queries are formulated in CQL. Filter methods are not provided by a dedicated service. Instead, they belong to the APIs of the respective resource services. They always retrieve resources of the same type, i.e. the filter method of the Item service will always retrieve Item representations exclusively. An exception to this rule are the retrieveMembers() methods in the Context and Container service: the result list may contain both Items and Containers.</p>";

    public static final String REPO_INFO_TEXT =
        "<p>Provides public configuration properties of the eSciDoc Infrastructure and the earliest creation date of eSciDoc repository objects.</p>";

    public static final String REINDEX_TEXT =
        "<p>Reinitialize the search index. The initialization runs asynchronously and returns some useful information to the user, e.g. the total number of objects found.</p>";

    public static final String ADMIN_TASK_VIEW_TITLE = "Welcome to eSciDoc";

    public static final String REINDEX_RESOURCES_TITLE = "Reindex Resources";

    public static final String LOAD_EXAMPLES_TITLE = "Load Examples";

    public static final String REPOSITORY_INFORMATION_TITLE =
        "Repository Information";

    public static final String FILTERING_RESOURCES_TITLE =
        "Filtering Resources";

    public static final String PLEASE_SELECT_A_RESOURCE_TYPE =
        "Please select a resource type";

    public static final String EMPTY = "Empty";

    static final String LOGOUT = "Logout";

    public static final int DESCRIPTION_ROWS = 3;

    public static final String SHOW_STATUS = "Show status";

    public static final String REINDEX_ESCIDOC_OAI_PMH = "escidocoaipmh";

    public static final String REINDEX_ESCIDOC_OU = "escidocou";

    public static final String REINDEX_ESCIDOC = "escidoc";

    public static final String REINDEX_ALL = "all";

    public static final List<String> INDEX_NAMES = Arrays.asList(new String[] {
        REINDEX_ALL, REINDEX_ESCIDOC, REINDEX_ESCIDOC_OU,
        REINDEX_ESCIDOC_OAI_PMH });

    public static final String ORG_UNITS = "Organizational Units";

    public static final String CONTENT_MODELS = "Content Models";

    public static final String[] RESOURCES_NODE = { ORG_UNITS, CONTEXTS,
        CONTENT_MODELS, USERS, ROLE };

    public static final String[] ADMIN_TASKS_NODE = { LOAD_EXAMPLES_TITLE,
        FILTERING_RESOURCES_TITLE, SHOW_REPOSITORY_INFO, REINDEX };

    public static final String _100_PERCENT = "100%";

    public static final String ADD_PARENT = "Add Parent";

    public static final String SELECT_A_PARENT_ORGANIZATIONAL_UNIT =
        "Select a parent organizational unit";

    public static final int FILTER_AREA_WIDTH = 800;

    public static final String STATUS = "Status";

    public static final String EXAMPLE_QUERY =
        "\"/properties/created-by/id\"=escidoc:exuser1";

    public static final String METADATA_LABEL = "Metadata";

    public static final String PUB_MAN_METADATA = "PubMan Metadata";

    public static final String RAW_XML = "Raw XML";

    public static final String RAW_METADATA = "Raw Metadata";

    public static final String FREE_FORM = "Free Form";

    public static final String REMOVE = "Remove";

    public static final String LOAD_EXAMPLE_TEXT =
        "<p>Loads a set of example objects into the framework.</p>";

    public static final String ADD_A_NEW_CONTEXT = "Add a new Context";

    public static final String EDIT_LABEL = "Edit";

    public static final String MODAL_DIALOG_HEIGHT = "650px";

    public static final String MODAL_DIALOG_WIDTH = "550px";

    public static final String SELECT_ORGANIZATIONAL_UNIT =
        "Select organizational unit.";

    public static final String EDIT_USER_ACCOUNT = "Edit Context";

    public static final String TOOLBAR_INVERT = "toolbar-invert";

    public static final String ADD_RESOURCE = "Add Resource";

    public static final String THIN_SPLIT = "small blue";

    public static final String EDIT_ORG_UNIT = "Edit Organizational Unit";

    public static final String ADD_ORG_UNIT = "Add a new Organizational Unit";

    public static final String RESOURCES = "Resources";

    public static final String LOGIN_WINDOW_WIDTH = "430px";

    public static final String PASSWORDS_DID_NOT_MATCH_MESSAGE =
        "Your passwords did not match";

    public static final String RETYPE_PASSWORD_CAPTION = "Re-Type Password";

    public static final String PASSWORD_CAPTION = "Password";

    public static final String TOO_SHORT_PASSWORD_MESSAGE =
        "Too Short: Password must be at least 6 characters";

    public static final String PASSWORD_UPDATED_MESSAGE = "Password is updated";

    public static final int PASSWORD_FIELD_WIDTH = 300;

    public static final String USER_ADD_VIEW_CAPTION = "Add a new User Account";

    public static final String SUCCESFULLY_UPDATED_ORGANIZATIONAL_UNIT =
        "Succesfully updated organizational unit.";

    public static final String NOT_AUTHORIZED = "Not Authorized";

    public static final int DEFAULT_LABEL_WIDTH = 100;

    public static final String NO_PARENTS_LABEL = "no parents";

    public static final String ADD = "Add";

    public static final String WELCOME_LABEL = "Welcome";

    public static final int MAX_TITLE_LENGTH = 50;

    public static final int MAX_DESC_LENGTH = 255;

    public static final String ACTIVE_STATUS = "Active status";

    public static final String GENERAL_ERROR_MESSAGE =
        "An unexpected error occured! See log for details";

    public static final String TOOLBAR_STYLE_NAME = "escidoc-toolbar";
}