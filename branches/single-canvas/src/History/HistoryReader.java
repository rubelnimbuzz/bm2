/*
 * HistoryReader.java
 *
 * Created on 18.06.2008, 10:39
 *
 * Copyright (c) 2006-2008, Daniel Apatin (ad), http://apatin.net.ru
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
 *
 */

package History;

import Client.Contact;
import Client.Msg;
import Menu.MenuCommand;
import Messages.MessageList;
import java.util.Vector;
import locale.SR;
import ui.MainBar;
import ui.VirtualList;

/**
 *
 * @author ad
 */
public class HistoryReader extends MessageList {

//#ifdef PLUGINS
    public static String plugin = new String("PLUGIN_HISTORY");
//#endif
    
    private HistoryLoader hl;
    MenuCommand cmdNext, cmdPrev;

    /** Creates a new instance of HistoryReader
     * @param c 
     */
    public HistoryReader(Contact c) {
        super();
        cmdNext = new MenuCommand(SR.MS_NEXT, MenuCommand.ITEM, 1);
        cmdPrev = new MenuCommand(SR.MS_PREVIOUS, MenuCommand.ITEM, 1);
        hl = new HistoryLoader(c.bareJid);
	MainBar mb=new MainBar(c.getName() + ": " + SR.MS_HISTORY);
	mb.addElement(null);
	mb.addRAlign();
	mb.addElement(null);
	//mb.addElement(SR.MS_FREE /*"free "*/);
        setMainBarItem(mb);

        removeAllMessages();
        addMenuCommands();
        removeMenuCommand(cmdxmlSkin);
        setMenuListener(this);
        addMenuCommand(cmdPrev);
        addMenuCommand(cmdNext);
	addMenuCommand(cmdBack);
        hl.getNext();
        moveCursorTo(1);
        show(parentView);
    }

    public void keyPressed(int keyCode) {
        if ((keyCode == KEY_NUM5) || (getGameAction(keyCode) == FIRE)) {
           if (cursor == 0) {
               removeAllMessages();
               hl.getPrev();
           } else if (cursor == (getItemCount()-1)) {
               removeAllMessages();
               hl.getNext();
           }           
        }
        super.keyPressed(keyCode);
    }

    public void menuAction(MenuCommand c, VirtualList d) {
        if(c==cmdNext) {
            removeAllMessages();
            hl.getNext();
            return;
        } else if (c == cmdPrev) {
            removeAllMessages();
            hl.getPrev();
            return;
        }
        super.menuAction(c, d);
    }

    private void removeAllMessages() {
        messages = null;
        messages = new Vector();
    }

    public int getItemCount() {
        if (hl != null)
           return hl.listMessages.size()+2;
        else return 0;
    }

    public Msg getMessage(int i) {
        if (i==0)
            return new Msg(Msg.MESSAGE_TYPE_SYSTEM, null, null, "<---");
        if (i==(getItemCount()-1))
            return new Msg(Msg.MESSAGE_TYPE_SYSTEM, null, null, "--->");
        return (Msg) hl.listMessages.elementAt(i-1);
    }
}