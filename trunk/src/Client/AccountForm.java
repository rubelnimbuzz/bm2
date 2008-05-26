/*
 * newAccountForm.java
 *
 * Created on 20.05.2008, 13:05
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

package Client;

import javax.microedition.lcdui.Display;
import locale.SR;
import ui.SplashScreen;
import ui.controls.AlertBox;
import ui.controls.form.BoldString;
import ui.controls.form.CheckBox;
import ui.controls.form.ChoiceBox;
import ui.controls.form.DefForm;
import ui.controls.form.NumberInput;
import ui.controls.form.PasswordInput;
import ui.controls.form.SpacerItem;
import ui.controls.form.TextInput;

/**
 *
 * @author ad
 */
public class AccountForm 
        extends DefForm {

    private final AccountSelect accountSelect;

    private TextInput userbox;
    private PasswordInput passbox;
    private TextInput servbox;
    private TextInput ipbox;
    private NumberInput portbox;
    private TextInput resourcebox;
    private TextInput nickbox;
    private CheckBox sslbox;
    private CheckBox plainPwdbox;
    private CheckBox noComprbox;
    private CheckBox confOnlybox;
//#if HTTPCONNECT
//#       private CheckBox proxybox;
//#elif HTTPPOLL        
//#       private CheckBox pollingbox;
//#endif
    private CheckBox registerbox;
	
    private NumberInput keepAlive;
    private ChoiceBox keepAliveType;
    
//#if HTTPPOLL || HTTPCONNECT  
//#     private TextInput proxyHost;
//#     private TextInput proxyPort;
//#endif

    Account account;
    
    boolean newaccount;
    
    /** Creates a new instance of newAccountForm */
    public AccountForm(AccountSelect accountSelect, Display display, Account account) {
        super(display, null);
	this.accountSelect = accountSelect;
        this.display=display;
        
	newaccount=(account==null);
	if (newaccount) account=new Account();
	this.account=account;
	
	String mainbar = (newaccount)?SR.MS_NEW_ACCOUNT:(account.toString());
        getMainBarItem().setElementAt(mainbar, 0);
        
        itemsList.addElement(new BoldString(SR.MS_USERNAME));
        userbox = new TextInput(display, account.getUserName(), null); //, 64, TextField.ANY
        itemsList.addElement(userbox);
    
        itemsList.addElement(new BoldString(SR.MS_PASSWORD));
	passbox = new PasswordInput(display, account.getPassword());//, 64, TextField.PASSWORD
        itemsList.addElement(passbox);
        
        itemsList.addElement(new BoldString(SR.MS_SERVER));
        servbox = new TextInput(display, account.getServer(), null);//, 64, TextField.ANY
        itemsList.addElement(servbox);
        
        itemsList.addElement(new BoldString(SR.MS_HOST_IP));
	ipbox = new TextInput(display, account.getHostAddr(), null);//, 64, TextField.ANY
        itemsList.addElement(ipbox);
        
        itemsList.addElement(new BoldString(SR.MS_PORT));
        portbox = new NumberInput(display, Integer.toString(account.getPort()), 0, 65535);//, 0, 65535
        itemsList.addElement(portbox);
        
        sslbox = new CheckBox(SR.MS_SSL, account.getUseSSL()); itemsList.addElement(sslbox);
        plainPwdbox = new CheckBox(SR.MS_PLAIN_PWD, account.getPlainAuth()); itemsList.addElement(plainPwdbox);
        noComprbox = new CheckBox(SR.MS_NO_COMPRESSION, !account.useCompression()); itemsList.addElement(noComprbox);
        confOnlybox = new CheckBox(SR.MS_CONFERENCES_ONLY, account.isMucOnly()); itemsList.addElement(confOnlybox);
//#if HTTPCONNECT
//#        proxybox = new CheckBox("proxybox", SR.MS_PROXY_ENABLE, account.isEnableProxy()); itemsList.addElement(proxybox);
//#elif HTTPPOLL        
//#        pollingbox = new CheckBox("pollingbox", "HTTP Polling", false); itemsList.addElement(pollingbox);
//#endif
        registerbox = new CheckBox(SR.MS_REGISTER_ACCOUNT, false); itemsList.addElement(registerbox);
        
        itemsList.addElement(new BoldString(SR.MS_KEEPALIVE));
        keepAliveType=new ChoiceBox();
        keepAliveType.append("by socket");
        keepAliveType.append("1 byte");
        keepAliveType.append("<iq/>");
        keepAliveType.append("ping");
        keepAliveType.setSelectedIndex(account.keepAliveType);
        itemsList.addElement(keepAliveType);

        itemsList.addElement(new BoldString(SR.MS_KEEPALIVE_PERIOD));
        keepAlive = new NumberInput(display, Integer.toString(account.keepAlivePeriod), 10, 2048);//10, 2096
        itemsList.addElement(keepAlive);

//#if HTTPPOLL || HTTPCONNECT  
//#     private TextInput proxyHost;
//#     private TextInput proxyPort;
//#endif
        
        itemsList.addElement(new BoldString(SR.MS_RESOURCE));
        resourcebox = new TextInput(display, account.getResource(), null);//64, TextField.ANY
        itemsList.addElement(resourcebox);
        
        itemsList.addElement(new BoldString(SR.MS_NICKNAME));
        nickbox = new TextInput(display, account.getNick(), null);//64, TextField.ANY
        itemsList.addElement(nickbox);

//#if HTTPCONNECT
//# 	itemsList.addElement(new BoldString(SR.MS_PROXY_HOST);
//# 	proxyHost = new TextInput(display, "proxyHost", account.getProxyHostAddr());//32, TextField.URL
//# 	itemsList.addElement(proxyHost);
//#
//# 	itemsList.addElement(new BoldString(SR.MS_PROXY_PORT);
//# 	proxyPort = new TextInput(display, "proxyPort", Integer.toString(account.getProxyPort()));//0, 65535
//# 	itemsList.addElement(proxyPort);
//#elif HTTPPOLL        
//# 	itemsList.addElement(new BoldString(SR.MS_PROXY_HOST);
//# 	proxyHost = new TextInput(display, "proxyHost", account.getProxyHostAddr());//32, TextField.URL
//# 	itemsList.addElement(proxyHost);
//#endif

        itemsList.addElement(new SpacerItem(0));
        
        moveCursorTo(getNextSelectableRef(-1));
        attachDisplay(display);
    }
    
    public void cmdOk() {
        String user = userbox.getValue().trim().toLowerCase();
        String server = servbox.getValue().trim().toLowerCase();
        String pass = passbox.getValue();
        int port=Integer.parseInt(portbox.getValue());

        int at = user.indexOf('@');
        if (at>-1) {
            server=user.substring(at+1);
            user=user.substring(0, at);
        }
        if (server=="" || user=="" || pass=="" || port==0)
            return;
        
        account.setUserName(user);
        account.setServer(server);
        account.setPassword(pass);
        account.setPort(port);
        account.setHostAddr(ipbox.getValue());
        account.setResource(resourcebox.getValue());
        account.setNick(nickbox.getValue());
        account.setUseSSL(sslbox.getValue());
        account.setPlainAuth(plainPwdbox.getValue());
        account.setUseCompression(!noComprbox.getValue());
        account.setMucOnly(confOnlybox.getValue());

//#if HTTPPOLL || HTTPCONNECT            
//#         account.setEnableProxy(proxybox.getValue());
//#         account.setProxyHostAddr(proxyHost.getValue());
//#         account.setProxyPort(proxyPort.getValue());
//#endif

        account.keepAlivePeriod=Integer.parseInt(keepAlive.getValue());
        account.keepAliveType=keepAliveType.getValue();

        if (newaccount) 
            accountSelect.accountList.addElement(account);
        accountSelect.rmsUpdate();
        accountSelect.commandState();

        if (registerbox.getValue())
            new AccountRegister(account, display, parentView); 
        else {
            destroyView();
        }
        account=null;
    }

    public void destroyView(){
        if (newaccount) {
            new AlertBox(SR.MS_CONECT_TO, account.getBareJid()+"?", display, StaticData.getInstance().roster) {
                public void yes() { startLogin();}
                public void no() { if (display!=null) display.setCurrent(parentView); }
            };
        } else if (display!=null)  
            display.setCurrent(parentView);
    }
    
    private void startLogin(){
        Account.loadAccount(true);
        SplashScreen.getInstance().close();
    }
}