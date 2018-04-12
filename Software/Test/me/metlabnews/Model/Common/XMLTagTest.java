package me.metlabnews.Model.Common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;



class XMLTagTest
{
	private String m_xml;

	@BeforeEach
	void setUp()
	{
		m_xml = "<CATALOG media-type=\"CDs\" >" +
				"<CD>" +
				"<TITLE>Empire Burlesque</TITLE>" +
				"<ARTIST>Bob Dylan</ARTIST>" +
				"<COUNTRY>USA</COUNTRY>" +
				"<COMPANY>Columbia</COMPANY>" +
				"<PRICE>10.90</PRICE>" +
				"<YEAR>1985</YEAR>" +
				"</CD>" +
				"<CD>" +
				"<TITLE>Hide your heart</TITLE>" +
				"<ARTIST>Bonnie Tyler</ARTIST>" +
				"<COUNTRY>UK</COUNTRY>" +
				"<COMPANY>CBS Records</COMPANY>" +
				"<PRICE>9.90</PRICE>" +
				"<YEAR>1988</YEAR>" +
				"</CD>" +
				"<CD>" +
				"<TITLE>Greatest Hits</TITLE>" +
				"<ARTIST>Dolly Parton</ARTIST>" +
				"<COUNTRY>USA</COUNTRY>" +
				"<COMPANY>RCA</COMPANY>" +
				"<PRICE>9.90</PRICE>" +
				"<YEAR>1982</YEAR>" +
				"</CD>" +
				"<CD>" +
				"<TITLE>Still got the blues</TITLE>" +
				"<ARTIST>Gary Moore</ARTIST>" +
				"<COUNTRY>UK</COUNTRY>" +
				"<COMPANY>Virgin records</COMPANY>" +
				"<PRICE>10.20</PRICE>" +
				"<YEAR>1990</YEAR>" +
				"</CD>" +
				"<CD>" +
				"<TITLE>Eros</TITLE>" +
				"<ARTIST>Eros Ramazzotti</ARTIST>" +
				"<COUNTRY>EU</COUNTRY>" +
				"<COMPANY>BMG</COMPANY>" +
				"<PRICE>9.90</PRICE>" +
				"<YEAR>1997</YEAR>" +
				"</CD>" +
				"<CD>" +
				"<TITLE>One night only</TITLE>" +
				"<ARTIST>Bee Gees</ARTIST>" +
				"<COUNTRY>UK</COUNTRY>" +
				"<COMPANY>Polydor</COMPANY>" +
				"<PRICE>10.90</PRICE>" +
				"<YEAR>1998</YEAR>" +
				"</CD>" +
				"<CD>" +
				"<TITLE>Sylvias Mother</TITLE>" +
				"<ARTIST>Dr.Hook</ARTIST>" +
				"<COUNTRY>UK</COUNTRY>" +
				"<COMPANY>CBS</COMPANY>" +
				"<PRICE>8.10</PRICE>" +
				"<YEAR>1973</YEAR>" +
				"</CD>" +
				"<CD>" +
				"<TITLE>Maggie May</TITLE>" +
				"<ARTIST>Rod Stewart</ARTIST>" +
				"<COUNTRY>UK</COUNTRY>" +
				"<COMPANY>Pickwick</COMPANY>" +
				"<PRICE>8.50</PRICE>" +
				"<YEAR>1990</YEAR>" +
				"</CD>" +
				"</CATALOG>";
	}

	@Test
	void child()
	{
		XMLTag xt = new XMLTag(m_xml);
		assertSame(null, xt.child("CATALOG"));
		assertNotNull(xt.child("CD"));
		assertNotNull(xt.child("CD").child("TITLE"));
		assertNotNull(xt.child("CD").child("ARTIST"));
		assertNotNull(xt.child("CD").child("COMPANY"));
		assertNotNull(xt.child("CD").child("COUNTRY"));
		assertNotNull(xt.child("CD").child("PRICE"));
		assertNotNull(xt.child("CD").child("YEAR"));
	}

	@Test
	void childWithPosition()
	{
		XMLTag xt = new XMLTag(m_xml);
		assertSame(null, xt.child("CATALOG", -1));
		for(int i = 0; i < 8; i++)
		{
			assertNotNull(xt.child("CD", i));
			assertNotNull(xt.child("CD", i).child("TITLE"));
			assertNotNull(xt.child("CD", i).child("ARTIST"));
			assertNotNull(xt.child("CD", i).child("COMPANY"));
			assertNotNull(xt.child("CD", i).child("COUNTRY"));
			assertNotNull(xt.child("CD", i).child("PRICE"));
			assertNotNull(xt.child("CD", i).child("YEAR"));
		}
	}

	@Test
	void children()
	{
		XMLTag xt = new XMLTag(m_xml);
		assertSame(0, xt.children("CATALOG").size());
		ArrayList<XMLTag> kids = xt.children("CD");
		assertSame(8, kids.size());
		for(int i = 0; i < 8; i++)
		{
			assertNotNull(kids.get(i));
			assertNotNull(kids.get(i).child("TITLE"));
			assertNotNull(kids.get(i).child("ARTIST"));
			assertNotNull(kids.get(i).child("COMPANY"));
			assertNotNull(kids.get(i).child("COUNTRY"));
			assertNotNull(kids.get(i).child("PRICE"));
			assertNotNull(kids.get(i).child("YEAR"));
		}
	}

	@Test
	void attribute()
	{
		XMLTag xt = new XMLTag(m_xml);
		assertEquals("CDs", xt.attribute("media-type"));
	}

	@Test
	void name()
	{
		XMLTag xt = new XMLTag(m_xml);
		assertEquals("CATALOG", xt.name());
	}

	@Test
	void value()
	{
		XMLTag xt = new XMLTag(m_xml);
		assertEquals("Bob Dylan", xt.child("CD").child("ARTIST").value());
	}
}