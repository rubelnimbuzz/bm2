/*
 * XDataForm.java
 *
 * Created on 6 Май 2008 г., 0:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package xmpp.extensions;

import com.alsutton.jabber.JabberDataBlock;
import java.util.*;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import locale.SR;
import util.Strconv;

/**
 *
 * @author root
 */
public class XDataForm implements CommandListener {
    
    public interface NotifyListener {
        void XDataFormSubmit(JabberDataBlock form);
    }
    
    private Displayable parentView;
    private NotifyListener notifyListener;
    
    private Command cmdOk=new Command(SR.MS_SEND, Command.OK /*Command.SCREEN*/, 1);
    private Command cmdCancel=new Command(SR.MS_BACK, Command.BACK, 99);
    
    Vector items;
    
    Form f;
    /** Creates a new instance of XDataForm
     * @param form
     * @param notifyListener
     */
    public XDataForm(JabberDataBlock form, NotifyListener notifyListener) {
        this.notifyListener=notifyListener;

        String title=form.getChildBlockText("title");
        f=new Form(title);

        items=null;
        items=new Vector();
        
        for (Enumeration e=form.getChildBlocks().elements(); e.hasMoreElements(); ) {
            
            JabberDataBlock ch=(JabberDataBlock)e.nextElement();
            
            if (ch.getTagName().equals("instructions")) {
                f.append(ch.getText());
                f.append("\n");
                continue;
            }
            
            if (!ch.getTagName().equals("field")) continue;
            
            XDataField field=new XDataField(ch);
            ch = null;
            
            items.addElement(field);
            
            if (field.hidden) continue;
            
            if (field.media != null)
                field.mediaIndex = f.append(field.media);
            field.formIndex=f.append(field.formItem);
        }
        
        f.setCommandListener(this);
        f.addCommand(cmdOk);
        f.addCommand(cmdCancel);
        midlet.BombusMod.getInstance().setDisplayable(f);
    }
    
    public void fetchMediaElements(Vector bobCache) {
        //TODO: fetch external http bobs and non-cached in-band bobs
        byte [] bytes = null;
        Image img = null;
        String cid = null;
        XDataField field = null;
        JabberDataBlock data = null;
        int formItems = items.size();
        for (int i=0; i<formItems; i++) {
            field=(XDataField)items.elementAt(i);
            if (field.mediaUri==null) continue;
            if (!(field.media instanceof StringItem)) continue;

            if (field.mediaUri.startsWith("cid:")) {
                cid = field.mediaUri.substring(4);
                if (bobCache==null) continue; //TODO: in-band bob request

                for (int bob=0; bob<bobCache.size(); bob++) {
                    data = null;
                    data = (JabberDataBlock) bobCache.elementAt(bob);
                    if (data.isJabberNameSpace("urn:xmpp:bob") && cid.equals(data.getAttribute("cid"))) {
                        bytes = null;
                        bytes = Strconv.fromBase64(data.getText());
                        img = null;
                        img = Image.createImage(bytes, 0, bytes.length);
                        bytes = null;
                        data = null;
                        
                        //workaround for SE
                        if (field.media != null) {
                            f.delete(field.mediaIndex);
                            f.insert(field.mediaIndex, new ImageItem(null, img, Item.LAYOUT_CENTER, null));
                        }
                        img = null;
                    }
                }
            }
        }
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command==cmdOk) {
            JabberDataBlock resultForm=new JabberDataBlock("x", null, null);
            resultForm.setNameSpace("jabber:x:data");
            resultForm.setTypeAttribute("submit");
            
            for (Enumeration e = items.elements(); e.hasMoreElements();) {
                JabberDataBlock ch = ((XDataField) e.nextElement()).constructJabberDataBlock();
                if (ch != null) {
                    resultForm.addChild(ch);
                }
                ch = null;
            }
            notifyListener.XDataFormSubmit(resultForm);
        }
        midlet.BombusMod.getInstance().setDisplayable(parentView);
    }
    
}
