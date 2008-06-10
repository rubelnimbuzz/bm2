/*
 * Reconnect.java
 * Created on 25.04.2008, 17:42
 * Copyright (c) 2006-2008, Daniel Apatin (ad), http://apatin.net.ru
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * You can also redistribute and/or modify this program under the
 * terms of the Psi License, specified in the accompanied COPYING
 * file, as published by the Psi Project; either dated January 1st,
 * 2005, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package Client;

import javax.microedition.lcdui.Display;
import ui.controls.TimerBox;

public class MyReconnect extends TimerBox {

    private final static int WAITTIME=15;

    public MyReconnect(String title, String body, Display display) {
        super(title, body, WAITTIME, display, null);
    }

    public void yes() {
        StaticData.getInstance().roster.doReconnect();
    }

    public void no() { }
}