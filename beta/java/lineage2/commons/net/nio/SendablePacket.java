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
package lineage2.commons.net.nio;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 * @param <T>
 */
public abstract class SendablePacket<T> extends AbstractPacket<T>
{
	protected final void putInt(int value)
	{
		getByteBuffer().putInt(value);
	}
	
	protected final void putDouble(double value)
	{
		getByteBuffer().putDouble(value);
	}
	
	protected final void putFloat(float value)
	{
		getByteBuffer().putFloat(value);
	}
	
	public final void writeC(int data)
	{
		getByteBuffer().put((byte) data);
	}
	
	protected final void writeF(double value)
	{
		getByteBuffer().putDouble(value);
	}
	
	public final void writeH(int value)
	{
		getByteBuffer().putShort((short) value);
	}
	
	public final void writeD(int value)
	{
		getByteBuffer().putInt(value);
	}
	
	public final void writeQ(long value)
	{
		getByteBuffer().putLong(value);
	}
	
	protected final void writeB(byte[] data)
	{
		getByteBuffer().put(data);
	}
	
	public final void writeS(String text)
	{
		if (text != null)
		{
			for (int i = 0; i < text.length(); i++)
			{
				getByteBuffer().putChar(text.charAt(i));
			}
		}
		
		getByteBuffer().putChar('\0');
	}
	
	protected abstract boolean write();
}
