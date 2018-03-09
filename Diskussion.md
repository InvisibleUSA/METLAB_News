- Java als Programmiersprache [b]
- IntelliJ als IDE [b]
- Visual Paradigm Community Edition als UML Tool [b]
- Doxygen?
- Vaadin für das Frontend (ohne Designer), kein JS
- Tomcat / TomEE als Server für Frontend und Backend?
  Alternative: WildFly Application Server (JBoss)
- Maven für Projektkonfiguration [b]
- JUnit für Unit Tests [b]
- Continuous-Integration-Server? Jenkins / Cruise Control?
- MariaDB für Benutzerverwaltung? Hibernate ORM? (Alternative: Data Warehouse)
- Firmen und deren Mitarbeiter in der selben Datenbank
- MongoDB für Nachrichtenverwaltung und Archivierung? RDBMS zur Indizierung?
  Alternative: eXist / BaseX?
- Benutzerprofile in OODBMS oder RDBMS (Hibernate ORM?) oder XMLDBMS? 
  Export als XML-Datei? (JAXB?)
- Lucene? mit Hibernate Search?
- ROME für RSS [z]


Vorschlag: Artikel komplett als XML in eXist oder BaseX, Indizierung mit Lucene
(evtl. über Hibernate Search), bei Bedarf JAXB
Benutzerprofile ebenfalls in XML, auf dem selben DBMS, ebenfalls mit JAXB
Benutzer in MariaDB, Anbindung über Hibernate ORM





[b]: beschlossene Sache
[z]: ziemlich sicher