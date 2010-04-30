/*
 * AccountSelect.java
 *
 * Created on 19.03.2005, 23:26
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

package Account;
import Client.*;
import javax.microedition.lcdui.Displayable;
import locale.SR;
import midlet.BombusMod;
import ui.*;
import java.io.*;
import java.util.*;
import Menu.Command;
import io.NvStorage;
import ui.controls.AlertBox;
//#ifdef IMPORT_EXPORT
//# import IE.*;
//#endif
import ui.controls.form.DefForm;


/**
 *
 * @author Eugene Stahov
 */
public class AccountSelect extends DefForm {
    public Vector accountList;
    int activeAccount;
    boolean enableQuit;
    
    Command cmdLogin=new Command(SR.MS_SELLOGIN, Command.OK,1);
    Command cmdSelect=new Command(SR.MS_NOLOGIN, Command.SCREEN,2);
    Command cmdAdd=new Command(SR.MS_NEW_ACCOUNT, Command.SCREEN,3);
    Command cmdEdit=new Command(SR.MS_EDIT,Command.ITEM,3);
    Command cmdDel=new Command(SR.MS_DELETE,Command.ITEM,4);
    Command cmdConfig=new Command(SR.MS_OPTIONS,Command.ITEM,5);
    Command cmdQuit=new Command(SR.MS_APP_QUIT,Command.SCREEN,10);
    
    private Config cf;
    
    /** Creates a new instance of AccountPicker */
    public AccountSelect(boolean enableQuit) {
        super(SR.MS_ACCOUNTS);
        this.enableQuit=enableQuit;
        
        enableListWrapping(true);
        cf=Config.getInstance();        
        
        if (enableQuit) {
            canBack = false;
        }
        accountList=null;
        accountList=new Vector();        
                
        activeAccount=cf.accountIndex;
        loadAccounts();
        show(StaticData.getInstance().roster);
    }
    public void show(Displayable pView) {
        super.show(pView);
        if (!accountList.isEmpty()) {
            moveCursorTo(activeAccount);
        } else {
//#ifdef IMPORT_EXPORT
//#ifdef PLUGINS
//#             if (StaticData.getInstance().IE) {
//#endif     
//#             new IE.Accounts("/def_accounts.txt", 0,  true);
//#ifdef PLUGINS                              
//#             }
//#endif
//#             loadAccounts();
//#         if (accountList.isEmpty()) {
//#endif
            new AccountForm(this, null).show(pView);
            return;
//#ifdef IMPORT_EXPORT
//#         }
//#endif
        }  

        
    }

    public final void loadAccounts() {
        Account a;
        int index=0;
        do {
            a=Account.createFromStorage(index);
            if (a!=null) {
                a.setActive(activeAccount==index);
                accountList.addElement(a);
                index++;
             }
       } while (a!=null);
    }

    public final void commandState(){
        menuCommands.removeAllElements();
        if ((accountList != null) && !accountList.isEmpty()) {
            addCommand(cmdLogin);
            addCommand(cmdSelect);
            
            addCommand(cmdEdit);
            addCommand(cmdDel);
        }
        addCommand(cmdAdd);
        addCommand(cmdConfig);
        if (enableQuit) 
            addCommand(cmdQuit);
    }


    public void touchRightPressed(){
        if (!canBack)
            return;
        destroyView();
    }
    public String touchLeftCommand() { return SR.MS_MENU; }
    public void touchLeftPressed() {
        showMenu();
    }

    public VirtualElement getItemRef(int Index) { return (VirtualElement)accountList.elementAt(Index); }
    protected int getItemCount() { return accountList.size();  }

    public void commandAction(Command c, Displayable d){
        if (c==cmdQuit) {
            destroyView();
            BombusMod.getInstance().notifyDestroyed();
            return;
        }
        if (c==cmdCancel) {
            destroyView();
        }
        if (c==cmdConfig) new ConfigForm(this);
        if (c==cmdLogin) switchAccount(true);
        if (c==cmdSelect) switchAccount(false);
        if (c==cmdEdit) new AccountForm(this, (Account)getFocusedObject()).show(this);
        if (c==cmdAdd) {
            new AccountForm(this, null).show(this);
        }
        if (c==cmdDel) {
            if (cursor==cf.accountIndex && StaticData.getInstance().roster.isLoggedIn()) return;
            //if (((Account)getFocusedObject()).equals(StaticData.getInstance().account)) return;
            
            new AlertBox(SR.MS_DELETE, getFocusedObject().toString()) {
                public void yes() {
                    delAccount();
                }
                public void no() { }
            };
        }
    }
    

    public void destroyView(){
        if(accountList.size()>0) {
            if (StaticData.getInstance().account==null)
                Account.loadAccount(false, cf.accountIndex);
            midlet.BombusMod.getInstance().setDisplayable(StaticData.getInstance().roster);
        }
    }

    private void delAccount(){
        if (accountList.size()==1) 
            cf.accountIndex=-1;
        else if (cf.accountIndex>cursor) cf.accountIndex--;

        cf.saveToStorage();

        accountList.removeElement(getFocusedObject());
        rmsUpdate();
        moveCursorHome();
        commandState();
        redraw();
    }
    
    private void switchAccount(boolean login){
        cf.accountIndex=cursor;
        cf.saveToStorage();
        Account.loadAccount(login, cursor);
        destroyView();
    }
    
    public void eventOk(){
        if (getItemCount()>0) {
            canBack = true;
            switchAccount(true);
        }
    }
    
    public void rmsUpdate(){
        DataOutputStream outputStream=NvStorage.CreateDataOutputStream();
        for (int i=0;i<accountList.size();i++) 
            ((Account)accountList.elementAt(i)).saveToDataOutputStream(outputStream);
        NvStorage.writeFileRecord(outputStream, "accnt_db", 0, true); //Account.storage
    }
    
    protected void keyRepeated(int keyCode) {
        super.keyRepeated(keyCode);
        if (kHold==keyCode) return;
        kHold=keyCode;
        
        if (keyCode==KEY_NUM6) {
            Config.fullscreen=!Config.fullscreen;
            cf.saveToStorage();
            VirtualList.fullscreen=Config.fullscreen;
            StaticData.getInstance().roster.setFullScreenMode(Config.fullscreen);
        }
    }
}
