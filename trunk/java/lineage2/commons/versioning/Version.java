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
package lineage2.commons.versioning;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Version
{
	private static final Logger _log = LoggerFactory.getLogger(Version.class);
	private String _revisionNumber = "exported";
	private String _versionNumber = "-1";
	private String _buildNumber = "-1";
	private String _buildDate = "";
	private String _buildJdk = "";
	private String _vendor = "";
	
	public Version(Class<?> c)
	{
		File jarName = null;
		try
		{
			jarName = Locator.getClassSource(c);
			if (!jarName.getName().endsWith(".jar"))
			{
				_log.warn("Version: Cannot find a jar library (" + jarName.getPath() + ")");
				return;
			}
			JarFile jarFile = new JarFile(jarName);
			Attributes attrs = jarFile.getManifest().getMainAttributes();
			setBuildJdk(attrs);
			setBuildDate(attrs);
			setRevisionNumber(attrs);
			setVersionNumber(attrs);
			setBuildNumber(attrs);
			setVendor(attrs);
		}
		catch (IOException e)
		{
			_log.error("Unable to get soft information\nFile name '" + (jarName.getAbsolutePath()) + "' isn't a valid jar", e);
		}
	}
	
	private void setBuildNumber(Attributes attrs)
	{
		String buildNumber = attrs.getValue("Implementation-Number");
		if (buildNumber != null)
		{
			_buildNumber = buildNumber;
		}
		else
		{
			_buildNumber = "-1";
		}
	}
	
	private void setVersionNumber(Attributes attrs)
	{
		String versionNumber = attrs.getValue("Implementation-Version");
		if (versionNumber != null)
		{
			_versionNumber = versionNumber;
		}
		else
		{
			_versionNumber = "-1";
		}
	}
	
	private void setRevisionNumber(Attributes attrs)
	{
		String revisionNumber = attrs.getValue("Implementation-Build");
		if (revisionNumber != null)
		{
			_revisionNumber = revisionNumber;
		}
		else
		{
			_revisionNumber = "-1";
		}
	}
	
	private void setBuildJdk(Attributes attrs)
	{
		String buildJdk = attrs.getValue("Build-Jdk");
		if (buildJdk != null)
		{
			_buildJdk = buildJdk;
		}
		else
		{
			buildJdk = attrs.getValue("Created-By");
			if (buildJdk != null)
			{
				_buildJdk = buildJdk;
			}
			else
			{
				_buildJdk = "-1";
			}
		}
	}
	
	private void setBuildDate(Attributes attrs)
	{
		String buildDate = attrs.getValue("Build-Date");
		if (buildDate != null)
		{
			_buildDate = buildDate;
		}
		else
		{
			_buildDate = "-1";
		}
	}
	
	public String getRevisionNumber()
	{
		return _revisionNumber;
	}
	
	public String getVersionNumber()
	{
		return _versionNumber;
	}
	
	public String getBuildNumber()
	{
		return _buildNumber;
	}
	
	public String getBuildDate()
	{
		return _buildDate;
	}
	
	public String getBuildJdk()
	{
		return _buildJdk;
	}
	
	public String getVendor()
	{
		return _vendor;
	}
	
	public void setVendor(Attributes attrs)
	{
		String vendor = attrs.getValue("Vendor");
		if (vendor != null)
		{
			_vendor = vendor;
		}
		else
		{
			_vendor = "There is something wrong going on here!";
		}
		_vendor = vendor;
	}
}
