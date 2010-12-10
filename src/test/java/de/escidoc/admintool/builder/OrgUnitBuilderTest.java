package de.escidoc.admintool.builder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import de.escidoc.admintool.domain.OrgUnit;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class OrgUnitBuilderTest {

  @Test
  public void shouldReturnAnOrgUnit() throws ParserConfigurationException {
    final String name = "aName";
    final String desc = "aDesc";

    final OrganizationalUnit orgUnit = new OrgUnit.BuilderImpl(name, desc).build();
    assertThat(orgUnit.getProperties().getName(), is(name));
    assertThat(orgUnit.getProperties().getDescription(), is(desc));
  }

  @Test
  public void shouldReturnAnOrgUnitFromAnOldOrgUnit()
      throws ParserConfigurationException {
    final String name = "aName";
    final String desc = "aDesc";
    final OrganizationalUnit before = new OrganizationalUnit();
    final OrganizationalUnit orgUnit = new OrgUnit.BuilderImpl(before).name(
        name).description(desc).build();
    assertThat(orgUnit.getProperties().getName(), is(name));
    assertThat(orgUnit.getProperties().getDescription(), is(desc));
  }

  @Test
  public void shouldReturnAnOrgUnitWithType()
      throws ParserConfigurationException {
    final String name = "aName";
    final String desc = "aDesc";
    final Set<String> parentObjectIds = new HashSet<String>() {
      {
        add("a");
        add("b");
      }
    };

    final OrganizationalUnit orgUnit = new OrgUnit.BuilderImpl(name, desc).parents(
        parentObjectIds).build();
    assertThat(orgUnit.getProperties().getName(), is(name));
    assertThat(orgUnit.getProperties().getDescription(), is(desc));
  }
}
