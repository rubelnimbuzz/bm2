/*
 * InfoWindow.java
 *
 * Created on 25.05.2008, 19:29
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

package Info;

import Client.Config;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
//#ifndef MENU_LISTENER
import javax.microedition.lcdui.Command;
//#else
//# import Menu.Command;
//#endif
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import locale.SR;
import midlet.BombusMod;
import ui.controls.form.DefForm;
import ui.controls.form.LinkString;
import ui.controls.form.MultiLine;
import util.ClipBoard;

/**
 *
 * @author ad
 */
public class InfoWindow
        extends DefForm {
    
    LinkString siteUrl;
    MultiLine description;
    MultiLine name;
    MultiLine memory;
    MultiLine abilities;
    
//#ifdef CLIPBOARD
//#ifndef MENU
//#     public Command cmdOk = new Command(SR.MS_COPY, Command.OK, 1);
//#endif
//#     private ClipBoard clipboard; 
//#endif
    
    /**
     * Creates a new instance of InfoWindow
     */
    public InfoWindow(Display display, Displayable pView) {
        super(display, pView, SR.MS_ABOUT);
        this.display=display;

        name=new MultiLine(Version.getName(), Version.getVersionNumber(), super.superWidth);
        name.selectable=true;
        itemsList.addElement(name);

        description=new MultiLine("Mobile Jabber client", Config.getOs()+"\nCopyright (c) 2005-2008, Eugene Stahov (evgs), Daniel Apatin (ad)\nDistributed under GNU Public License (GPL) v2.0", super.superWidth);
        description.selectable=true;
        itemsList.addElement(description);
        
        siteUrl=new LinkString("http://bombusmod.net.ru"){ public void doAction() { try { BombusMod.getInstance().platformRequest("http://bombusmod.net.ru"); } catch (ConnectionNotFoundException ex) { }}};
        itemsList.addElement(siteUrl);
        
        StringBuffer memInfo=new StringBuffer(SR.MS_FREE);
        System.gc();
        memInfo.append(Runtime.getRuntime().freeMemory()>>10)
               .append("\n")
               .append(SR.MS_TOTAL)
               .append(Runtime.getRuntime().totalMemory()>>10);
        memory=new MultiLine(SR.MS_MEMORY, memInfo.toString(), super.superWidth);
        memory.selectable=true;
        itemsList.addElement(memory);
        
        abilities=new MultiLine("Abilities", getAbilities(), super.superWidth);
        abilities.selectable=true;
        itemsList.addElement(abilities);
//#ifdef CLIPBOARD
//#         if (Config.getInstance().useClipBoard) {
//#             clipboard=ClipBoard.getInstance(); 
//#         }
//#endif
        
//#ifndef MENU
        super.removeCommand(super.cmdOk);
//#ifdef CLIPBOARD
//#         if (Config.getInstance().useClipBoard) {
//#             addCommand(cmdOk);
//#         }
//#endif
//#endif
        enableListWrapping(false);
        
        moveCursorTo(0);
        attachDisplay(display);
        this.parentView=pView;
    }
//#ifdef MENU_LISTENER
//#     public String touchLeftCommand(){ return SR.MS_COPY; }
//#     
//#     public void touchLeftPressed(){
//#         eventOk();
//#     }
//#endif
    
    public void cmdOk(){
//#ifdef CLIPBOARD
//#         clipboard.setClipBoard(name.toString()+"\n"+memory.toString()+"\n"+abilities.toString());
//#         destroyView();
//#endif
    }
//#ifdef MENU
//#ifdef CLIPBOARD
//#     public String getLeftCommand() { return SR.MS_COPY; }
//#endif
//#     public String getRightCommand() { return SR.MS_BACK; }
//#else
    public void commandAction(Command command, Displayable displayable) {
	if (command==cmdOk) {
	    cmdOk();
	}
        super.commandAction(command, displayable);
    }
//#endif
    
    private String getAbilities() {
        Vector abilitiesList=new Vector();
//#ifdef ADHOC
//#         abilitiesList.addElement((String)"ADHOC");
//#endif
//#ifdef ANTISPAM
//#         abilitiesList.addElement((String)"ANTISPAM");
//#endif
//#ifdef ARCHIVE
        try {
            Class.forName("Archive.ArchiveList"); abilitiesList.addElement((String)"ARCHIVE");
        } catch (ClassNotFoundException ignore2) { }
//#endif
//#ifdef AUTOSTATUS
//#         abilitiesList.addElement((String)"AUTOSTATUS");
//#endif
//#ifdef CAPTCHA
//#         abilitiesList.addElement((String)"CAPTCHA");
//#endif
//#ifdef CHANGE_TRANSPORT
//#         try {
//#             Class.forName("Client.ChangeTransport"); abilitiesList.addElement((String)"CHANGE_TRANSPORT");
//#         } catch (ClassNotFoundException ignore2) { }
//#endif
//#ifdef CHECK_VERSION
//#         abilitiesList.addElement((String)"CHECK_VERSION");
//#endif
//#ifdef CLIENTS_ICONS
//#         abilitiesList.addElement((String)"CLIENTS_ICONS");
//#endif
//#ifdef CLIPBOARD
//#         abilitiesList.addElement((String)"CLIPBOARD");
//#endif
//#ifdef CONSOLE
//#         try {
//#             Class.forName("Console.XMLList"); abilitiesList.addElement((String)"CONSOLE");
//#         } catch (ClassNotFoundException ignore3) { }
//#endif
//#ifdef COLOR_TUNE
//#         abilitiesList.addElement((String)"COLOR_TUNE");
//#endif
//#ifdef DETRANSLIT
//#         abilitiesList.addElement((String)"DETRANSLIT");
//#endif
//#ifdef ELF
//#         abilitiesList.addElement((String)"ELF");
//#endif
//#ifdef FILE_TRANSFER
        try {
            Class.forName("io.file.transfer.TransferDispatcher"); abilitiesList.addElement((String)"FILE_TRANSFER");
        } catch (ClassNotFoundException ignore3) { }
//#endif
//#ifdef GRADIENT
//#         abilitiesList.addElement((String)"GRADIENT");
//#endif
//#ifdef HISTORY
//#         try {
//#             Class.forName("History.HistoryConfig"); abilitiesList.addElement((String)"HISTORY");
//#         } catch (ClassNotFoundException ignore2) { }
//#endif
//#ifdef HISTORY_READER
//#         try {
//#             Class.forName("History.HistoryConfig"); abilitiesList.addElement((String)"HISTORY_READER");
//#         } catch (ClassNotFoundException ignore2) { }
//#endif
//#ifdef IMPORT_EXPORT
//#         abilitiesList.addElement((String)"IMPORT_EXPORT");
//#endif
//#ifdef LAST_MESSAGES
//#         try {
//#             Class.forName("History.HistoryConfig"); abilitiesList.addElement((String)"LAST_MESSAGES");
//#         } catch (ClassNotFoundException ignore2) { }
//#endif
//#ifdef LOGROTATE
//#         abilitiesList.addElement((String)"LOGROTATE");
//#endif
//#ifdef MENU_LISTENER
//#         abilitiesList.addElement((String)"MENU_LISTENER");
//#endif
//#ifdef NEW_SKIN
//#         abilitiesList.addElement((String)"NEW_SKIN");
//#endif
//#ifdef NICK_COLORS
        abilitiesList.addElement((String)"NICK_COLORS");
//#endif
//#ifdef PEP
//#         try {
//#             Class.forName("xmpp.extensions.PepListener"); abilitiesList.addElement((String)"PEP");
//#         } catch (ClassNotFoundException ignore2) { }
//#endif
//#ifdef PEP_TUNE
//#         try {
//#             Class.forName("xmpp.extensions.PepListener"); abilitiesList.addElement((String)"PEP_TUNE");
//#         } catch (ClassNotFoundException ignore2) { }
//#endif
//#ifdef POPUPS
        abilitiesList.addElement((String)"POPUPS");
//#endif
//#ifdef REQUEST_VOICE
//#         abilitiesList.addElement((String)"REQUEST_VOICE");
//#endif
//#ifdef PRIVACY
        try {
            Class.forName("PrivacyLists.PrivacySelect"); abilitiesList.addElement((String)"PRIVACY");
        } catch (ClassNotFoundException ignore2) { }
//#endif
//#ifdef SE_LIGHT
//#         abilitiesList.addElement((String)"SE_LIGHT");
//#endif
//#ifdef SERVICE_DISCOVERY
        abilitiesList.addElement((String)"SERVICE_DISCOVERY");
//#endif
//#ifdef SMILES
        abilitiesList.addElement((String)"SMILES");
//#endif
//#ifdef STATS
//#         abilitiesList.addElement((String)"STATS");
//#endif
//#ifdef TEMPLATES
        try {
            Class.forName("Archive.ArchiveList"); abilitiesList.addElement((String)"TEMPLATES");
        } catch (ClassNotFoundException ignore2) { }
//#endif
//#ifdef USER_KEYS
//#         abilitiesList.addElement((String)"USER_KEYS");
//#endif
//#ifdef USE_ROTATOR
        abilitiesList.addElement((String)"USE_ROTATOR");
//#endif
//#ifdef WMUC
//#         abilitiesList.addElement((String)"WMUC");
//#endif
//#ifdef WSYSTEMGC
//#         abilitiesList.addElement((String)"WSYSTEMGC");
//#endif
        
        StringBuffer abilities=new StringBuffer();
        
	for (Enumeration ability=abilitiesList.elements(); ability.hasMoreElements(); ) {
            abilities.append((String)ability.nextElement());
            abilities.append(", ");
	}
        String ab=abilities.toString();
        abilities=null;
        abilitiesList=null;
        return ab.substring(0, ab.length()-2);
    }
}
