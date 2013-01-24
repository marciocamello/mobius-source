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
package lineage2.commons.data.xml.helpers;

import java.io.File;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class SimpleDTDEntityResolver implements EntityResolver
{
	private final String _fileName;
	
	public SimpleDTDEntityResolver(File f)
	{
		_fileName = f.getAbsolutePath();
	}
	
	@Override
	public InputSource resolveEntity(String publicId, String systemId)
	{
		return new InputSource(_fileName);
	}
}
