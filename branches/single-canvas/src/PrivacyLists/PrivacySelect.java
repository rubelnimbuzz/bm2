/*
 * PrivacySelect.java
 *
 * Created on 26.08.2005, 23:04
 * Copyright (c) 2005-2008, Eugene Stahov (evgs), http://bombus-im.org
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
 *
 */

package PrivacyLists;

import Client.StaticData;
import javax.microedition.lcdui.TextField;
import images.RosterIcons;
import Menu.Command;
import locale.SR;
import ui.*;
import ui.controls.AlertBox;
import java.util.*;
import com.alsutton.jabber.*;
import ui.controls.form.DefForm;

/**
 *
 * @author EvgS
 */
public class PrivacySelect 
        extends DefForm
        implements        
        JabberBlockListener,
        MIDPTextBox.TextBoxNotify
{
//#ifdef PLUGINS
    public static String plugin = new String("PLUGIN_PRIVACY");
//#endif
    
    private Vector list=new Vector();
    
    private Command cmdActivate=new Command (SR.MS_ACTIVATE, Command.SCREEN, 10);
    private Command cmdDefault=new Command (SR.MS_SETDEFAULT, Command.SCREEN, 11);
    private Command cmdNewList=new Command (SR.MS_NEW_LIST, Command.SCREEN, 12);
    private Command cmdDelete=new Command (SR.MS_DELETE_LIST, Command.SCREEN, 13);
    //private Command cmdEdit=new Command (SR.MS_EDIT_LIST, Command.SCREEN, 14);
    private Command cmdIL=new Command (SR.MS_MK_ILIST, Command.SCREEN, 16);
    
    JabberStream stream=StaticData.getInstance().roster.theStream;
    
    /** Creates a new instance of PrivacySelect
     * @param pView
     */
    public PrivacySelect(VirtualList pView) {
        super(null);
        this.parentView=pView;

        setMainBarItem(new MainBar(2, null, SR.MS_PRIVACY_LISTS, false));

        itemsList.addElement(new PrivacyList(null));//none
        
        setCommandListener(this);
        
        getLists();
        
        show(parentView);        
    }
    
    public void commandState() {
        menuCommands.removeAllElements();
        addCommand(cmdActivate);
        addCommand(cmdDefault);
        addCommand(cmdNewList);
        addCommand(cmdDelete);
        addCommand(cmdIL);
    }

    private void processIcon(boolean processing){
        getMainBarItem().setElementAt((processing)?(Object)new Integer(RosterIcons.ICON_PROGRESS_INDEX):(Object)null, 0);
        redraw();
    }
   
    private void getLists(){
        try {
            Thread.sleep(500);
        } catch (Exception e) { }
        stream.addBlockListener(this);
        processIcon(true);
        PrivacyList.privacyListRq(false, null, "getplists");
    }    
    
    public void commandAction(Command c, VirtualList d) {
        if (c==cmdCancel) {
            destroyView();
            stream.cancelBlockListener(this);
            return;
        }
        if (c==cmdActivate || c==cmdDefault) {
            PrivacyList active=((PrivacyList)getFocusedObject());
            for (Enumeration e=list.elements(); e.hasMoreElements(); ) {
                PrivacyList pl=(PrivacyList)e.nextElement();
                boolean state=(pl==active);
                if (c==cmdActivate)
                    pl.isActive=state;
                else
                    pl.isDefault=state;
            }
            ((PrivacyList)getFocusedObject()).activate( (c==cmdActivate)? "active":"default" ); 
            getLists();
        }
        if (c==cmdIL) {
            generateIgnoreList();
            getLists();
        }
        if (c==cmdDelete) {
            PrivacyList pl=(PrivacyList) getFocusedObject();
            if (pl!=null) {
                if (pl.name!=null)
                        pl.deleteList();
                getLists();
            }
        }
        if (c==cmdNewList)
            new MIDPTextBox( this, SR.MS_NEW, "", this, TextField.ANY);
        super.commandAction(c, d);
    }    
        
    // MIDPTextBox interface
    public void OkNotify(String listName) {
        if (listName.length()>0)
            new PrivacyModifyList(this, new PrivacyList(listName), true);
    }
    
    public int blockArrived(JabberDataBlock data){
        try {
            if (data.getTypeAttribute().equals("result"))
                if (data.getAttribute("id").equals("getplists")) {
                data=data.findNamespace("query", "jabber:iq:privacy");
                if (data!=null) {
                    itemsList.removeAllElements();
                    String activeList="";
                    String defaultList="";
                    try {
                        for (Enumeration e=data.getChildBlocks().elements(); e.hasMoreElements();) {
                            JabberDataBlock pe=(JabberDataBlock) e.nextElement();
                            String tag=pe.getTagName();
                            String name=pe.getAttribute("name");
                            if (tag.equals("active")) activeList=name;
                            if (tag.equals("default")) defaultList=name;
                            if (tag.equals("list")) {
                                PrivacyList pl=new PrivacyList(name);
                                pl.isActive=(name.equals(activeList));
                                pl.isDefault=(name.equals(defaultList));
                                itemsList.addElement(pl);
                            }
                        }
                    } catch (Exception e) {}
                    PrivacyList nullList=new PrivacyList(null);
                    nullList.isActive=activeList.length()==0;
                    nullList.isDefault=defaultList.length()==0;
                    itemsList.addElement(nullList);//none
                }
                
                processIcon(false);
                
                return JabberBlockListener.NO_MORE_BLOCKS;
                }
        } catch (Exception e) { }
        return JabberBlockListener.BLOCK_REJECTED;
    }

    public void eventOk(){
        PrivacyList pl=(PrivacyList) getFocusedObject();
        if (pl!=null) {
            if (pl.name!=null) new PrivacyModifyList(this, pl, false);
        }
    }
    private void generateIgnoreList(){
        JabberDataBlock ignoreList=new JabberDataBlock("list", null, null);
        ignoreList.setAttribute("name", SR.MS_IGNORE_LIST);
        JabberDataBlock item=PrivacyItem.itemIgnoreList().constructBlock();
        ignoreList.addChild(item);
        PrivacyList.privacyListRq(true, ignoreList, "ignlst");
    }
    
    public void keyGreen() {
        new MIDPTextBox( this, SR.MS_NEW, "", this, TextField.ANY);
    }
        
    protected void keyClear(){
        new AlertBox(SR.MS_DELETE, getFocusedObject().toString()) {
            public void yes() {
                PrivacyList pl=(PrivacyList) getFocusedObject();
                if (pl!=null) {
                    if (pl.name!=null) pl.deleteList();
                    getLists();
                }
            }
            public void no() {}
        };
    }
    public void touchLeftPressed() { showMenu(); }
    public String touchLeftCommand() {return SR.MS_MENU; }
}
