/**
 * @author Hanno - Felix Wagner, 08.06.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.de.unidue.stud.sehawagn.contmas.measurement;

import jade.core.AID;
import jade.lang.acl.MessageTemplate;

/**
 * @author Hanno - Felix Wagner
 *
 */
public interface Measurer{
	public void processStatusUpdate(AID sender, Long eventTime, String content);

	/**
	 * @return
	 */
	public AID getMeasureTopic();
	
}
