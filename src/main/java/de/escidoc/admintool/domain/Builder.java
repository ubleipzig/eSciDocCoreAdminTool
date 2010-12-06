package de.escidoc.admintool.domain;

import javax.xml.parsers.ParserConfigurationException;

public interface Builder<T> {

  T build() throws ParserConfigurationException;
}
