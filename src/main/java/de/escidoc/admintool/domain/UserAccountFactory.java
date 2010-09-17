package de.escidoc.admintool.domain;

import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.aa.useraccount.UserAccountProperties;

public class UserAccountFactory {
    private UserAccount userAccount;

    public UserAccountFactory update(final UserAccount userAccount) {
        this.userAccount = userAccount;
        return this;
    }

    public UserAccountFactory name(final String name) {
        final UserAccountProperties properties = userAccount.getProperties();
        properties.setName(name);

        userAccount.setProperties(properties);
        return this;
    }

    public UserAccount build() {
        return userAccount;
    }

    public UserAccountFactory create(final String name, final String loginName) {
        assert name != null : "name must not be null";
        assert loginName != null : "loginName must not be null";

        userAccount = new UserAccount();

        final UserAccountProperties properties = new UserAccountProperties();
        properties.setName(name);
        properties.setLoginName(loginName);
        userAccount.setProperties(properties);

        return this;
    }
}