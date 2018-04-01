package com.metlab.clippingDaemon;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



/**
 * This class provides an easy to use interface to access XML-Data.
 * In the constructor you can can specify XML-Data as string. After
 * construction it is accessible by the method {@link #child(String)}.
 * These calls can be chained to get to the desired subtag. If the path is not found,
 * a nullptr-exception is thrown (as of version 1.0).
 * <p>
 * To extract the information from the input string, this class uses the Java XML-API
 * under the hood.
 * <p>
 * Usage example:
 * Suppose you have an XML-String and want to print out the action.
 * You can do this with the following code:
 * {@code
 * String data = "<test>
 * <name>Hello World</name>
 * <action>print</action>
 * </test>";
 * Tag t = new Tag(data);
 * System.out.println(t.child("action").value());
 * }
 *
 * @author Erik Hennig
 * @version 1.0
 */
public class Tag
{
	/**
	 * Alternate constructor. Use only if you need the Java XML-API anyway.
	 *
	 * @param d initialized XML-Document
	 */
	public Tag(Document d)
	{
		construct(d.getChildNodes().item(0));
	}

	/**
	 * constructor that should be used by default
	 * @param xmlData XML-data as string
	 */
	@SuppressWarnings("WeakerAccess")
	public Tag(String xmlData)
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document        XMLDoc   = dBuilder.parse(new ByteArrayInputStream(xmlData.getBytes("UTF-8")));
			construct(XMLDoc.getChildNodes().item(0));
		}
		catch(SAXException e)
		{
			e.printStackTrace();
			System.out.println("Parsing exception.");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("IO Exception.");
		}
		catch(ParserConfigurationException e)
		{
			e.printStackTrace();
			System.out.println("Parser is not correctly configured. Exception.");
		}
		//TODO implement correct logging and/or throw error in case of invalid xml
	}

	/**
	 * default method to access child of the current {@link Tag}
	 * Always the first child is returned.
	 * If you need all children with name x call {@link #children(String)}.
	 * If you need the n-th child with name x call {@link #child(String, int)}.
	 *
	 * @param tagName the name of the tag
	 * @return first subtag in XML that matches the tagName
	 * @throws NullPointerException Child is not found.
	 * @see #child(String, int) #children(String)
	 */
	public Tag child(String tagName)
	{
		return child(tagName, 0);
	}

	/**
	 * alternative method to access any child of the current {@link Tag}
	 * As a second parameter the positon of the tag element is entered counting only
	 * tag elements with the name tagName. position starts at 0.
	 *
	 * If you need all children with name x call {@link #children(String)}. This can be used
	 * if you want to iterate over all children having this name.
	 * If you need only the first (0th) child with name x consider calling {@link #child(String)} instead.
	 *
	 * @param tagName the name of the tag
	 * @param position the position of the tag in the xml string
	 * @return the position-th subtag that matches the tagName
	 * @throws NullPointerException Child is not found.
	 * @see #child(String) #children(String)
	 */
	@SuppressWarnings("WeakerAccess")
	public Tag child(String tagName, int position)
	{
		Key k = new Key(tagName, position);
		return m_children.get(k);
	}

	/**
	 * alternative method to access all children of the current {@link Tag}
	 * with the same name x. Useful if you want to iterate over all children
	 * of a tag that have the same name.
	 * If no child is found, the returned List is empty.
	 * <p>
	 * If you need only one child, call {@link #child(String)} or {@link #child(String, int)} instead.
	 *
	 * @param tagName the name of the tag
	 * @return all children with this name
	 * @see #child(String) #child(String, int)
	 */
	public ArrayList<Tag> children(String tagName)
	{
		Key            k   = new Key(tagName, 0);
		ArrayList<Tag> alt = new ArrayList<>();
		Tag            t;
		while((t = m_children.get(k)) != null)
		{
			alt.add(t);
			k.position++;
		}
		return alt;
	}

	/**
	 * method to access attributes of the current tag.
	 * @param name name of the attribute accessed
	 * @return value of the attribute if found; null otherwise
	 */
	public String attribute(String name)
	{
		return m_attributes.get(name);
	}

	/**
	 * method to get the name of the current tag.
	 *
	 * Example:
	 * {@code
	 * Tag t = new Tag("<profile>
	 *                    <value>test</value>
	 *                  </profile>");
	 * System.out.println(t.name()); //prints profile
	 * System.out.println(t.child("value").name()); //prints value
	 * }
	 * @return name of tag
	 */
	public String name()
	{
		return m_name;
	}

	/**
	 * method to get the value of the current tag.
	 * Only useful, if current tag has no subtags.
	 * Otherwise an empty string is returned.
	 *
	 * Example:
	 * {@code
	 * Tag t = new Tag("<profile>
	 *                    <value>test</value>
	 *                  </profile>");
	 * System.out.println(t.value()); //prints "" (empty string)
	 * System.out.println(t.child("value").value()); //prints "test"
	 * }
	 * @return value of tag
	 */
	public String value()
	{
		return m_value;
	}

	/**
	 * prints a correctly formatted and indented
	 * XML-File as String including newlines
	 * @return XML-data
	 */
	public String toString()
	{
		return print(0);
	}

	private Tag(Node n)
	{
		construct(n);
	}

	private void construct(Node n)
	{
		m_name = n.getNodeName();
		for(int i = 0; i < n.getAttributes().getLength(); i++)
		{
			m_attributes.put(n.getAttributes().item(i).getNodeName(), n.getAttributes().item(i).getNodeValue());
		}
		if((n.getChildNodes().getLength() == 1) && (n.getChildNodes().item(
				0).getNodeType() == Node.TEXT_NODE || n.getChildNodes().item(
				0).getNodeType() == Node.CDATA_SECTION_NODE))
		{
			m_value = n.getChildNodes().item(0).getNodeValue();
		}
		else
		{
			for(int i = 0; i < n.getChildNodes().getLength(); i++)
			{
				if(!n.getChildNodes().item(i).hasChildNodes())
				{
					continue;
				}
				Tag t = new Tag(n.getChildNodes().item(i));
				Key k = new Key(n.getChildNodes().item(i).getNodeName(), 0);
				while(m_children.containsKey(k))
				{
					k.position++;
				}
				m_children.put(k, t);
			}
		}
	}

	private String print(int indentlevel)
	{
		StringBuilder s      = new StringBuilder("<" + m_name);
		StringBuilder indent = new StringBuilder();
		for(int i = 0; i < indentlevel; i++)
		{
			indent.append(" ");
		}
		s.insert(0, indent);
		for(HashMap.Entry<String, String> entry : m_attributes.entrySet())
		{
			s.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
		}
		s.append(">\n");
		for(HashMap.Entry<Key, Tag> entry : m_children.entrySet())
		{
			s.append(entry.getValue().print(indentlevel + 2));
		}
		s.append(indent).append("</").append(m_name).append(">\n");
		return s.toString();
	}

	private HashMap<Key, Tag>       m_children   = new HashMap<>();
	private HashMap<String, String> m_attributes = new HashMap<>();
	private String m_name;
	private String m_value;

	private class Key
	{
		String name;
		int    position;

		Key(String n, int p)
		{
			name = n;
			position = p;
		}

		@Override
		public int hashCode()
		{
			return name.hashCode();
		}

		@Override
		public boolean equals(Object o)
		{
			if(o.getClass() != this.getClass())
			{
				return false;
			}
			Key k = (Key)o;
			return (name.equals(k.name)) && (position == k.position);
		}
	}
}

