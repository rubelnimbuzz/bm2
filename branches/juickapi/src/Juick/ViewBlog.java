/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Juick;

import Client.Msg;
import Menu.MenuCommand;
import Messages.MessageItem;
import Messages.MessageList;
import com.alsutton.jabber.datablocks.Iq;
import ui.MainBar;
import ui.VirtualList;

/**
 *
 * @author Vitaly
 */
public class ViewBlog extends MessageList implements JuickEvents{

    MenuCommand cmdHome = new MenuCommand("Home", MenuCommand.SCREEN, 1);
    MenuCommand cmdLast = new MenuCommand("Last", MenuCommand.SCREEN, 2);
    MenuCommand cmdView = new MenuCommand("View message", MenuCommand.SCREEN, 3);

    int state = 1;

    int currentId = 0;
    int lastState = 1;

    public ViewBlog() {
        super();
        show();                
        addMenuCommands();
        setState(cmdHome);
    }

    public void messageArrived(Msg m) {
            messages.addElement(new MessageItem(m, this, true));
        }

    public int getItemCount() {
        return messages.size();
    }

    protected Msg getMessage(int index) {
        return ((MessageItem)messages.elementAt(index)).msg;
    }

    final void setState(MenuCommand c) {
        lastState = state;
        state = c.pos;
        setMainBarItem(new MainBar("Juick - " + c.name));
        messages.removeAllElements();
        runCommand(state);        
    }

    void runCommand(int state) {
        Iq query = null;
        switch (state) {            
            case 1:
               query = JuickApi.queryMsgs(true);
               break;
            case 2:
               query = JuickApi.queryMsgs(false);
               break;
            case 3:
                if (currentId > 0) {
                    query = JuickApi.queryMsg(currentId, false);
                    sd.roster.theStream.send(query);
                    query = JuickApi.queryMsg(currentId, true); // TODO: kill @ugnich
                }
               break;
        }
        sd.roster.theStream.send(query);

    }

    public void menuAction(MenuCommand c, VirtualList d) {
        super.menuAction(c, d);
        if (c == cmdView) {
                try {
                Msg m = ((MessageItem)getFocusedObject()).msg;
                currentId = m.juickMid;
                } catch (Exception e) {
                    e.printStackTrace();
                    currentId = 0;
                }
        } else {
            currentId = 0;
        }        
        setState(c);
    }

    public final void addMenuCommands() {
        menuCommands.addElement(cmdHome);
        menuCommands.addElement(cmdLast);
        menuCommands.addElement(cmdView);
        super.addMenuCommands();
    }

    public void cmdCancel() {
        if (state == 1)
            destroyView();
        else {
            setState((MenuCommand)menuCommands.elementAt(lastState - 1));
        }

    }

   
    
}
