/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.commons.data.xml;

import java.io.File;
import java.io.InputStream;

import lineage2.commons.data.xml.helpers.ErrorHandlerImpl;
import lineage2.commons.data.xml.helpers.SimpleDTDEntityResolver;
import lineage2.commons.logging.LoggerObject;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public abstract class AbstractParser<H extends AbstractHolder> extends LoggerObject
{
	protected final H _holder;
	
	protected String _currentFile;
	protected SAXReader _reader;
	
	protected AbstractParser(H holder)
	{
		_holder = holder;
		_reader = new SAXReader();
		_reader.setValidation(true);
		_reader.setErrorHandler(new ErrorHandlerImpl(this));
	}
	
	protected void initDTD(File f)
	{
		_reader.setEntityResolver(new SimpleDTDEntityResolver(f));
	}
	
	protected void parseDocument(InputStream f, String name) throws Exception
	{
		_currentFile = name;
		
		org.dom4j.Document document = _reader.read(f);
		
		readData(document.getRootElement());
	}
	
	protected abstract void readData(Element rootElement) throws Exception;
	
	protected abstract void parse();
	
	protected H getHolder()
	{
		return _holder;
	}
	
	public String getCurrentFileName()
	{
		return _currentFile;
	}
	
	public void load()
	{
		parse();
		_holder.process();
		_holder.log();
	}
	
	public void reload()
	{
		info("reload start...");
		_holder.clear();
		load();
	}
}
