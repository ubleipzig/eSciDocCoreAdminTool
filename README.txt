If isolation is needed:
WEB-INF/jboss-web.xml
<?xml version="1.0" encoding="UTF-8"?>
<jboss-web>
  <class-loading>
        <loader-repository>
            org.escidoc.admintool:loader=adminToolClassLoader
        </loader-repository>
    </class-loading>
</jboss-web>