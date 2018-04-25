package me.metlabnews.Model.Common;

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
 * XMLTag t = new XMLTag(data);
 * System.out.println(t.child("action").value());
 * }
 *
 * @author Erik Hennig
 * @version 1.1
 */
@SuppressWarnings("WeakerAccess")
public class XMLTag
{
	static
	{
		Logger.getInstance().register(XMLTag.class, Logger.Channel.XMLTag);
		Logger.getInstance().register(Key.class, Logger.Channel.XMLTag);
	}
	/**
	 * Alternate constructor. Use only if you need the Java XML-API anyway.
	 *
	 * @param d initialized XML-Document
	 */
	public XMLTag(Document d)
	{
		construct(d.getChildNodes().item(0));
	}

	/**
	 * constructor that should be used by default
	 *
	 * @param xmlData XML-data as string
	 */
	public XMLTag(String xmlData)
	{
		Logger                 l         = Logger.getInstance();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document        XMLDoc   = dBuilder.parse(new ByteArrayInputStream(xmlData.getBytes("UTF-8")));
			construct(XMLDoc.getChildNodes().item(0));
		}
		catch(SAXException e)
		{
			l.logError(this, "Parsing exception:\n" + e.toString());
		}
		catch(IOException e)
		{
			l.logError(this, "IO exception:\n" + e.toString());
		}
		catch(ParserConfigurationException e)
		{
			l.logError(this, "Parser is not correctly configured. Exception.");
		}
	}

	/**
	 * default method to access child of the current {@link XMLTag}
	 * Always the first child is returned.
	 * If you need all children with name x call {@link #children(String)}.
	 * If you need the n-th child with name x call {@link #child(String, int)}.
	 *
	 * @param tagName the name of the tag
	 * @return first subtag in XML that matches the tagName
	 * @throws NullPointerException Child is not found.
	 * @see #child(String, int) #children(String)
	 */
	public XMLTag child(String tagName)
	{
		return child(tagName, 0);
	}

	/**
	 * alternative method to access any child of the current {@link XMLTag}
	 * As a second parameter the position of the tag element is entered counting only
	 * tag elements with the name tagName. position starts at 0.
	 * <p>
	 * If you need all children with name x call {@link #children(String)}. This can be used
	 * if you want to iterate over all children having this name.
	 * If you need only the first (0th) child with name x consider calling {@link #child(String)} instead.
	 *
	 * @param tagName  the name of the tag
	 * @param position the position of the tag in the xml string
	 * @return the position-th subtag that matches the tagName, null if not found
	 * @throws NullPointerException Child is not found.
	 * @see #child(String) #children(String)
	 */
	public XMLTag child(String tagName, int position)
	{
		Logger.getInstance().logDebug(this,
		                              "child " + tagName + " at position " + position + " requested. (Parent:" + m_name + ")");
		Key k = new Key(tagName, position);
		return m_children.get(k);
	}

	/**
	 * alternative method to access all children of the current {@link XMLTag}
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
	public ArrayList<XMLTag> children(String tagName)
	{
		Key               k   = new Key(tagName, 0);
		ArrayList<XMLTag> alt = new ArrayList<>();
		XMLTag            t;
		Logger.getInstance().logDebug(this, "all children " + tagName + " requested. (Parent:" + m_name + ")");
		while((t = m_children.get(k)) != null)
		{
			alt.add(t);
			k.position++;
		}
		return alt;
	}

	/**
	 * method to access attributes of the current tag.
	 *
	 * @param name name of the attribute accessed
	 * @return value of the attribute if found; null otherwise
	 */
	public String attribute(String name)
	{
		Logger.getInstance().logDebug(this, "attribute " + name + " requested. (Parent:" + m_name + ")");
		return m_attributes.get(name);
	}

	/**
	 * method to get the name of the current tag.
	 * <p>
	 * Example:
	 * {@code
	 * XMLTag t = new XMLTag("<profile>
	 * <value>test</value>
	 * </profile>");
	 * System.out.println(t.name()); //prints profile
	 * System.out.println(t.child("value").name()); //prints value
	 * }
	 *
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
	 * <p>
	 * Example:
	 * {@code
	 * XMLTag t = new XMLTag("<profile>
	 * <value>test</value>
	 * </profile>");
	 * System.out.println(t.value()); //prints "" (empty string)
	 * System.out.println(t.child("value").value()); //prints "test"
	 * }
	 *
	 * @return value of tag
	 */
	public String value()
	{
		return m_value;
	}

	/**
	 * prints a correctly formatted and indented
	 * XML-File as String including newlines
	 *
	 * @return XML-data
	 */
	public String toString()
	{
		return print(0);
	}

	private XMLTag(Node n)
	{
		construct(n);
	}

	private void construct(Node n)
	{
		m_name = n.getNodeName();
		Logger.getInstance().logDebug(this, "constructing node " + m_name);
		for(int i = 0; i < n.getAttributes().getLength(); i++)
		{
			Logger.getInstance().logDebug(this, "adding attribute " + n.getAttributes().item(i).getNodeName());
			m_attributes.put(n.getAttributes().item(i).getNodeName(), n.getAttributes().item(i).getNodeValue());
		}
		if((n.getChildNodes().getLength() == 1) && (n.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE))
		{
			m_value = n.getChildNodes().item(0).getNodeValue();
			Logger.getInstance().logDebug(this, "added text " + m_value);
		}
		else
		{
			for(int i = 0; i < n.getChildNodes().getLength(); i++)
			{
				if(!n.getChildNodes().item(i).hasChildNodes())
				{
					continue;
				}
				XMLTag t = new XMLTag(n.getChildNodes().item(i));
				Key    k = new Key(n.getChildNodes().item(i).getNodeName(), 0);
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
		Logger.getInstance().logDebug(this, "printing level " + indentlevel + ". making indent string");
		for(int i = 0; i < indentlevel; i++)
		{
			indent.append(" ");
		}
		s.insert(0, indent);
		Logger.getInstance().logDebug(this, "printing level " + indentlevel + ". adding node attributes");
		for(HashMap.Entry<String, String> entry : m_attributes.entrySet())
		{
			s.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
		}
		s.append(">\n");
		Logger.getInstance().logDebug(this, "printing level " + indentlevel + ". printing children");
		for(HashMap.Entry<Key, XMLTag> entry : m_children.entrySet())
		{
			s.append(entry.getValue().print(indentlevel + 2));
		}
		s.append(indent).append("</").append(m_name).append(">\n");
		Logger.getInstance().logDebug(this, "done printing level " + indentlevel + ".");
		return s.toString();
	}

	private HashMap<Key, XMLTag>    m_children   = new HashMap<>();
	private HashMap<String, String> m_attributes = new HashMap<>();
	private String                  m_name;
	private String                  m_value;

	/**
	 * Class used for implementation of XMLTag.
	 * Necessary to put tags in Hashmap as they don't allow multiple values.
	 */
	private class Key
	{
		String name;
		int    position;

		Key(String n, int p)
		{
			Logger.getInstance().logDebug(this, "new Key " + n + " requested with position " + p);
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

