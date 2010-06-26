/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package io.file.transfer;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;
import ui.controls.form.DefForm;
import ui.controls.form.TextInput;


/**
 *
 * @author Vitaly
 */
public class TransferSetupForm extends DefForm {

    private TextInput streamhost;
    private TextInput port;

    public TransferSetupForm(Displayable parentView) {
        super ("Transfer settings");
        
        streamhost = new TextInput("SOCKS5 proxy", TransferDispatcher.getInstance().ProxyJID, "ft_proxyjid", 0);
        port = new TextInput("SOCKS5 port", Integer.toString(TransferDispatcher.getInstance().ProxyPort), "ft_proxyport", TextField.NUMERIC);
        itemsList.addElement(streamhost);
        itemsList.addElement(port);

        show(parentView);
    }

    public void cmdOk() {
        TransferDispatcher.getInstance().ProxyJID = streamhost.getValue();
        TransferDispatcher.getInstance().ProxyPort = Integer.parseInt(port.getValue());
        destroyView();
    }

}
