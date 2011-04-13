package de.escidoc.admintool.view.admintask.filter;

import java.util.Set;

import de.escidoc.core.resources.Resource;

interface ShowFilterResultCommand {
    void execute(Set<Resource> filterResult);
}