package de.escidoc.admintool.domain;

import org.w3c.dom.Element;

import com.google.common.base.Preconditions;

public class RawXmlMetadataImpl implements RawXmlMetadata {

  private final String name;
  private final Element content;

  public RawXmlMetadataImpl(String metadataName, RawXml xml) {
    Preconditions.checkArgument(metadataName != null,
        "metadataName is null: %s", metadataName);
    Preconditions.checkArgument(xml != null, "xml is null: %s", xml);

    this.name = metadataName;
    this.content = xml.asElement();
  }

  public String name() {
    return name;
  }

  public Element content() {
    return content;
  }
}
