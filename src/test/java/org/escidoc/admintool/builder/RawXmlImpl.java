package org.escidoc.admintool.builder;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RawXmlImpl implements RawXml {

  private final String xml;
  private final Element element;

  public RawXmlImpl(String xml) throws SAXException, IOException,
      ParserConfigurationException {
    this.xml = xml;
    element = toElement();
  }

  public Element asElement() {
    return element;
  }

  private Element toElement() throws SAXException, IOException,
      ParserConfigurationException {
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder builder = factory.newDocumentBuilder();
    final InputSource is = new InputSource(new StringReader(xml));
    final Document d = builder.parse(is);
    return d.getDocumentElement();
  }
}