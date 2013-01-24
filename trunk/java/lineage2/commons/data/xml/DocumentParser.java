/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.commons.data.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastList;
import lineage2.commons.util.fileio.XMLFilter;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public abstract class DocumentParser
{
	private final static Logger _log = Logger.getLogger(DocumentParser.class);
	private static final XMLFilter xmlFilter = new XMLFilter();
	private File _currentFile;
	private Document _currentDocument;
	
	public abstract void load();
	
	protected void parseFile(File f)
	{
		if (!xmlFilter.accept(f))
		{
			_log.warn("Could not parse " + f.getName() + " is not a file or it doesn't exist!");
			return;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(true);
		dbf.setIgnoringComments(true);
		_currentDocument = null;
		_currentFile = f;
		try
		{
			dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new XMLErrorHandler());
			_currentDocument = db.parse(f);
		}
		catch (Exception e)
		{
			_log.warn("Could not parse " + f.getName() + " file: " + e.getMessage());
			return;
		}
		parseDocument();
	}
	
	public File getCurrentFile()
	{
		return _currentFile;
	}
	
	protected Document getCurrentDocument()
	{
		return _currentDocument;
	}
	
	protected boolean parseDirectory(String path)
	{
		return parseDirectory(new File(path));
	}
	
	protected boolean parseDirectory(File dir)
	{
		if (!dir.exists())
		{
			_log.warn("Folder " + dir.getAbsolutePath() + " doesn't exist!");
			return false;
		}
		FastList<File> listOfFiles = getAllFileList(dir, "xml");
		for (File f : listOfFiles)
		{
			parseFile(f);
		}
		return true;
	}
	
	protected void parseDocument(Document doc)
	{
	}
	
	protected abstract void parseDocument();
	
	protected static int parseInt(NamedNodeMap n, String name)
	{
		return Integer.parseInt(n.getNamedItem(name).getNodeValue());
	}
	
	protected static Integer parseInteger(NamedNodeMap n, String name)
	{
		return Integer.valueOf(n.getNamedItem(name).getNodeValue());
	}
	
	protected static int parseInt(Node n)
	{
		return Integer.parseInt(n.getNodeValue());
	}
	
	protected static Integer parseInteger(Node n)
	{
		return Integer.valueOf(n.getNodeValue());
	}
	
	protected static Long parseLong(NamedNodeMap n, String name)
	{
		return Long.valueOf(n.getNamedItem(name).getNodeValue());
	}
	
	protected static Long parseLong(Node n)
	{
		return Long.valueOf(n.getNodeValue());
	}
	
	protected static boolean parseBoolean(NamedNodeMap n, String name)
	{
		Node b = n.getNamedItem(name);
		return (b != null) && (Boolean.parseBoolean(b.getNodeValue()));
	}
	
	protected static boolean parseBoolean(Node n)
	{
		return Boolean.parseBoolean(n.getNodeValue());
	}
	
	protected static byte parseByte(Node n)
	{
		return Byte.valueOf(n.getNodeValue()).byteValue();
	}
	
	protected static double parseDouble(NamedNodeMap n, String name)
	{
		return Double.valueOf(n.getNamedItem(name).getNodeValue()).doubleValue();
	}
	
	protected static float parseFloat(NamedNodeMap n, String name)
	{
		return Float.valueOf(n.getNamedItem(name).getNodeValue()).floatValue();
	}
	
	protected static float parseFloat(Node n)
	{
		return Float.valueOf(n.getNodeValue()).floatValue();
	}
	
	protected class XMLErrorHandler implements ErrorHandler
	{
		protected XMLErrorHandler()
		{
		}
		
		@Override
		public void warning(SAXParseException e) throws SAXParseException
		{
			throw e;
		}
		
		@Override
		public void error(SAXParseException e) throws SAXParseException
		{
			throw e;
		}
		
		@Override
		public void fatalError(SAXParseException e) throws SAXParseException
		{
			throw e;
		}
	}
	
	public static FastList<File> getAllFileList(File dir, String pathName)
	{
		FastList<File> list = new FastList<>();
		if ((!dir.toString().endsWith("/")) && (!dir.toString().endsWith("\\")))
		{
			dir = new File(new StringBuilder().append(dir.toString()).append("/").toString());
		}
		if (!dir.exists())
		{
			_log.warn(new StringBuilder().append(" Folder ").append(dir.getAbsolutePath()).append(" doesn't exist!").toString());
		}
		for (File file : dir.listFiles())
		{
			if (file.isDirectory())
			{
				for (File fileName : getAllFileList(file, pathName))
				{
					if (fileName.toString().endsWith(pathName))
					{
						list.add(fileName);
					}
				}
			}
			else
			{
				if (!file.toString().endsWith(pathName))
				{
					continue;
				}
				list.add(file);
			}
		}
		return list;
	}
}
