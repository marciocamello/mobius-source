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
package lineage2.commons.net.nio;

public abstract class SendablePacket<T> extends AbstractPacket<T>
{
	public void writeC(int data)
	{
		getByteBuffer().put((byte) data);
	}
	
	protected void writeF(double value)
	{
		getByteBuffer().putDouble(value);
	}
	
	protected void writeH(int value)
	{
		getByteBuffer().putShort((short) value);
	}
	
	public void writeD(int value)
	{
		getByteBuffer().putInt(value);
	}
	
	public void writeQ(long value)
	{
		getByteBuffer().putLong(value);
	}
	
	protected void writeB(byte[] data)
	{
		getByteBuffer().put(data);
	}
	
	public void writeS(CharSequence charSequence)
	{
		if (charSequence != null)
		{
			int length = charSequence.length();
			for (int i = 0; i < length; i++)
			{
				getByteBuffer().putChar(charSequence.charAt(i));
			}
		}
		getByteBuffer().putChar('\000');
	}
	
	protected abstract boolean write();
}
