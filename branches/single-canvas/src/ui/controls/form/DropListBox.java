/*
 * DropListBox.java
 *
 * Created on 22 Май 2008 г., 16:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ui.controls.form;

import java.util.Vector;
import Menu.MenuListener;
import Menu.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import locale.SR;
import ui.MainBar;
import ui.VirtualElement;
import ui.VirtualList;

/**
 *
 * @author ad
 */
public class DropListBox 
        extends VirtualList 
        implements
        MenuListener
    {
    private Command cmdCancel=new Command(SR.MS_CANCEL, Command.BACK,99);
    private Command cmdOk=new Command(SR.MS_OK, Command.OK,1);
    private Vector listItems;

    private DropChoiceBox cb;

    public DropListBox(Display display, Vector listItems, DropChoiceBox cb) {
        super();
        this.listItems=listItems;
        this.cb=cb;
        
        setMainBarItem(new MainBar(SR.MS_SELECT));
        
        commandState();
        setCommandListener(this);
        
        moveCursorTo(cb.getSelectedIndex());
    }
    
    public void commandState() {
        menuCommands.removeAllElements();
        addCommand(cmdOk);
        addCommand(cmdCancel);
    }
    
    public void eventOk() {
        if (listItems.size()>0)cb.setSelectedIndex(cursor);
        
        destroyView();
    }

    public String touchLeftCommand() { return SR.MS_OK; }
    public void touchLeftPressed(){ eventOk(); }

    public String touchRightCommand() { return SR.MS_CANCEL; }
    public void touchRightPressed(){ destroyView(); }
    
    public void destroyView()	{
	if (display!=null)
            midlet.BombusMod.getInstance().setDisplayable(parentView);
    }

    public VirtualElement getItemRef(int index){ 
        return new ListItem((String) listItems.elementAt(index)); 
    }
    
    public int getItemCount() { return listItems.size(); }

    public void commandAction(Command command, Displayable displayable) {
        
    }

}