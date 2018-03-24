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



public class Tag
{
	public Tag(Document d)
	{
		construct(d.getChildNodes().item(0));
	}

	@SuppressWarnings("WeakerAccess")
	public Tag(String s)
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document        XMLDoc   = dBuilder.parse(new ByteArrayInputStream(s.getBytes("UTF-8")));
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
	}

	public Tag child(String s)
	{
		return child(s, 0);
	}

	@SuppressWarnings("WeakerAccess")
	public Tag child(String s, int position)
	{
		Key k = new Key(s, position);
		return m_children.get(k);
	}

	public ArrayList<Tag> children(String s)
	{
		Key            k   = new Key(s, 0);
		ArrayList<Tag> alt = new ArrayList<>();
		Tag            t;
		while((t = m_children.get(k)) != null)
		{
			alt.add(t);
			k.position++;
		}
		return alt;
	}

	public String attribute(String name)
	{
		return m_attributes.get(name);
	}

	public String name()
	{
		return m_name;
	}

	public String value()
	{
		return m_value;
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
		if((n.getChildNodes().getLength() == 1) && (n.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE))
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

	public String toString()
	{
		return print(0);
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
	private String m_value = "{empty value}";

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

