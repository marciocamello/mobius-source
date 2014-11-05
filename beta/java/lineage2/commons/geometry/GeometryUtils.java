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
package lineage2.commons.geometry;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
class GeometryUtils
{
	/**
	 * Constructor for GeometryUtils.
	 */
	private GeometryUtils()
	{
	}
	
	/**
	 * Method checkIfLinesIntersects.
	 * @param a Point2D
	 * @param b Point2D
	 * @param c Point2D
	 * @param d Point2D
	 * @return boolean
	 */
	static boolean checkIfLinesIntersects(Point2D a, Point2D b, Point2D c, Point2D d)
	{
		return checkIfLinesIntersects(a, b, c, d, null);
	}
	
	/**
	 * Method checkIfLinesIntersects.
	 * @param a Point2D
	 * @param b Point2D
	 * @param c Point2D
	 * @param d Point2D
	 * @param r Point2D
	 * @return boolean
	 */
	private static boolean checkIfLinesIntersects(Point2D a, Point2D b, Point2D c, Point2D d, Point2D r)
	{
		double distAB, theCos, theSin, newX, ABpos;
		
		if (((a.getX() == b.getX()) && (a.getY() == b.getY())) || ((c.getX() == d.getX()) && (c.getY() == d.getY())))
		{
			return false;
		}
		
		double Bx = b.getX() - a.getX();
		double By = b.getY() - a.getY();
		double Cx = c.getX() - a.getX();
		double Cy = c.getY() - a.getY();
		double Dx = d.getX() - a.getX();
		double Dy = d.getY() - a.getY();
		distAB = Math.sqrt((Bx * Bx) + (By * By));
		theCos = Bx / distAB;
		theSin = By / distAB;
		newX = (Cx * theCos) + (Cy * theSin);
		Cy = (int) ((Cy * theCos) - (Cx * theSin));
		Cx = newX;
		newX = (Dx * theCos) + (Dy * theSin);
		Dy = (int) ((Dy * theCos) - (Dx * theSin));
		Dx = newX;
		
		if (Cy == Dy)
		{
			return false;
		}
		
		ABpos = Dx + (((Cx - Dx) * Dy) / (Dy - Cy));
		
		if (r != null)
		{
			r.setX((int) (a.getX() + (ABpos * theCos)));
			r.setY((int) (a.getY() + (ABpos * theSin)));
		}
		
		return true;
	}
	
	/**
	 * Method checkIfLineSegementsIntersects.
	 * @param a Point2D
	 * @param b Point2D
	 * @param c Point2D
	 * @param d Point2D
	 * @return boolean
	 */
	static boolean checkIfLineSegementsIntersects(Point2D a, Point2D b, Point2D c, Point2D d)
	{
		return checkIfLineSegementsIntersects(a, b, c, d, null);
	}
	
	/**
	 * Method checkIfLineSegementsIntersects.
	 * @param a Point2D
	 * @param b Point2D
	 * @param c Point2D
	 * @param d Point2D
	 * @param r Point2D
	 * @return boolean
	 */
	private static boolean checkIfLineSegementsIntersects(Point2D a, Point2D b, Point2D c, Point2D d, Point2D r)
	{
		double distAB, theCos, theSin, newX, ABpos;
		
		if (((a.getX() == b.getX()) && (a.getY() == b.getY())) || ((c.getX() == d.getX()) && (c.getY() == d.getY())))
		{
			return false;
		}
		
		if (((a.getX() == c.getX()) && (a.getY() == c.getY())) || ((b.getX() == c.getX()) && (b.getY() == c.getY())) || ((a.getX() == d.getX()) && (a.getY() == d.getY())) || ((b.getX() == d.getX()) && (b.getY() == d.getY())))
		{
			return false;
		}
		
		double Bx = b.getX() - a.getX();
		double By = b.getY() - a.getY();
		double Cx = c.getX() - a.getX();
		double Cy = c.getY() - a.getY();
		double Dx = d.getX() - a.getX();
		double Dy = d.getY() - a.getY();
		distAB = Math.sqrt((Bx * Bx) + (By * By));
		theCos = Bx / distAB;
		theSin = By / distAB;
		newX = (Cx * theCos) + (Cy * theSin);
		Cy = (int) ((Cy * theCos) - (Cx * theSin));
		Cx = newX;
		newX = (Dx * theCos) + (Dy * theSin);
		Dy = (int) ((Dy * theCos) - (Dx * theSin));
		Dx = newX;
		
		if (((Cy < 0.) && (Dy < 0.)) || ((Cy >= 0.) && (Dy >= 0.)))
		{
			return false;
		}
		
		ABpos = Dx + (((Cx - Dx) * Dy) / (Dy - Cy));
		
		if ((ABpos < 0.) || (ABpos > distAB))
		{
			return false;
		}
		
		if (r != null)
		{
			r.setX((int) (a.getX() + (ABpos * theCos)));
			r.setY((int) (a.getY() + (ABpos * theSin)));
		}
		
		return true;
	}
}
