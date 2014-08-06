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
package lineage2.commons.net.nio.impl;

import java.nio.ByteOrder;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SelectorConfig
{
	public int READ_BUFFER_SIZE = 65536;
	public int WRITE_BUFFER_SIZE = 131072;
	public int MAX_SEND_PER_PASS = 32;
	public long SLEEP_TIME = 10;
	public long INTEREST_DELAY = 30;
	public final int HEADER_SIZE = 2;
	public final int PACKET_SIZE = 32768;
	public int HELPER_BUFFER_COUNT = 64;
	public final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
}
