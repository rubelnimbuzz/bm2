/*
 * StanzaEdit.java
 *
 * Created on 7.04.2008, 16:05
 *
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

package Console;

import Client.StaticData;
import java.io.IOException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;
import locale.SR;
import ui.controls.ExTextBox;

/**
 *
 * @author ad
 */
public class StanzaEdit 
        extends ExTextBox
        implements CommandListener {

    private Display display;
    private Displayable parentView;

    private String body;

    private Command cmdCancel=new Command(SR.MS_CANCEL, Command.BACK,99);
    private Command cmdSend=new Command(SR.MS_SEND, Command.OK,1);

    private Command cmdPasteIQDisco=new Command("disco#info", Command.SCREEN,11);
    private Command cmdPasteIQVersion=new Command("jabber:iq:version", Command.SCREEN,12);
    private Command cmdPastePresence=new Command("presence", Command.SCREEN,13);
    private Command cmdPasteMessage=new Command("message", Command.SCREEN,14);
    
    private static final String TEMPLATE_IQ_DISCO="<iq to='???' type='get'>\n<query xmlns='http://jabber.org/protocol/disco#info'/>\n</iq>";
    private static final String TEMPLATE_IQ_VERSION="<iq to='???' type='get'>\n<query xmlns='jabber:iq:version'/>\n</iq>";
    private static final String TEMPLATE_PRESENCE="<presence to='???'>\n<show>???</show>\n<status>???</status>\n</presence>";
    private static final String TEMPLATE_MESSAGE="<message to='???' type='???'>\n<body>???</body>\n</message>";

    public StanzaEdit(Display display, String body) {
        super(display, body, SR.MS_XML_CONSOLE, TextField.ANY);
        
        this.display=display;
        parentView=display.getCurrent();
        
        addCommand(cmdSend);

        addCommand(cmdPasteIQDisco);
        addCommand(cmdPasteIQVersion);
        addCommand(cmdPastePresence);
        addCommand(cmdPasteMessage);
        
        addCommand(cmdCancel);
        setCommandListener(this);
        
        display.setCurrent(this);
    }
    
    public void setParentView(Displayable parentView){
        this.parentView=parentView;
    }
    
    public void commandAction(Command c, Displayable d){
        if (executeCommand(c, d)) return;
        
        body=getString();
        if (body.length()==0) body=null;

        int caretPos=getCaretPos();

        if (c==cmdPasteIQDisco) { insert(TEMPLATE_IQ_DISCO, caretPos); return; }
        if (c==cmdPasteIQVersion) { insert(TEMPLATE_IQ_VERSION, caretPos); return; }
        if (c==cmdPastePresence) { insert(TEMPLATE_PRESENCE, caretPos); return; }
        if (c==cmdPasteMessage) { insert(TEMPLATE_MESSAGE, caretPos); return; }

        if (c==cmdCancel) { 
            body=null;
        }

        if (c==cmdSend && body!=null) {
            try {
                StaticData.getInstance().roster.theStream.send(body.trim());
            } catch (IOException ex) { }
        }

        destroyView();
    }
}