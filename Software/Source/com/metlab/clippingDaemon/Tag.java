package com.metlab.clippingDaemon;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.HashMap;



public class Tag
{
	public Tag(Document d)
	{
		construct(d.getChildNodes().item(0));
	}

	public Tag child(String s)
	{
		return child(s, 0);
	}

	public Tag child(String s, int position)
	{
		Key k = new Key(s, position);
		Tag t = m_children.get(k);
		return t;
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
		if((n.getChildNodes().getLength() == 1) && (n.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE))
		{
			m_value = n.getChildNodes().item(0).getNodeValue();
		}
		else
		{
			for(int i = 0; i < n.getChildNodes().getLength(); i++)
			{
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
		String s      = m_name;
		String indent = "";
		for(int i = 0; i < indentlevel; i++)
		{
			indent += " ";
		}
		//for (Tag t: m_children)
		return s;
	}

	private HashMap<Key, Tag> m_children = new HashMap<>(); //position needs to be in there (multi hash map)
	private String m_name;
	private String m_value = "{empty value}";

	private class Key
	{
		public String name;
		public int    position;

		public Key(String n, int p)
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
			Key k = (Key)o;
			return (name.equals(k.name)) && (position == k.position);
		}
	}
}

