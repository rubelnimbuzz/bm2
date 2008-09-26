/*
 * Account.java
 *
 * Created on 19.03.2005, 21:52
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
import Client.Config;
import Client.StaticData;
import Info.Version;
import com.alsutton.jabber.JabberStream;
import com.alsutton.jabber.datablocks.Presence;
import images.RosterIcons;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ui.IconTextElement;
import io.NvStorage;

public class Account extends IconTextElement{
    
    //public final static String storage="accnt_db";
            
    private String userName="";
    private String password="";
    private String server="";
    private String hostAddr="";
    private int port=5222;
    private boolean active;
    private boolean useSSL;
    private boolean compression=true;
    private boolean plainAuth;
    private boolean mucOnly;
    
    private String nick="";
    private String resource=Version.NAME;
    
    private boolean enableProxy;
    private String proxyHostAddr="";
    private int proxyPort;
	
    private int keepAlivePeriod=200;
    private int keepAliveType=1;
    
    private static StaticData sd=StaticData.getInstance();
    
    private boolean dnsResolver=true;

    public Account() {
        super(RosterIcons.getInstance());
    }
    
    public static void loadAccount(boolean launch, int accountIndex){
	Account a=sd.account=Account.createFromStorage(accountIndex);
	if (a!=null) {
            if (sd.roster.isLoggedIn())
                sd.roster.logoff(null);
            
	    sd.roster.resetRoster();
            if (launch) {
                int loginstatus=Config.getInstance().loginstatus;
                if (loginstatus>=Presence.PRESENCE_OFFLINE) {
                    sd.roster.sendPresence(Presence.PRESENCE_INVISIBLE, null);    
                } else {
                    sd.roster.sendPresence(loginstatus, null);
                }
            }
        }
    }

    public String toString(){
        StringBuffer s=new StringBuffer();
        if (nick.length()!=0)
            s.append(nick);
        else {
            s.append(userName).append('@').append(server);
        }
        s.append('/').append(resource);
        return s.toString();
    }
    
    public String getJid(){
        return userName+'@'+server+'/'+resource;
    }
    
    public String getBareJid(){
        return userName+'@'+server;
    }
    
    public static Account createFromStorage(int index) {
        Account a=null;
        DataInputStream is=NvStorage.ReadFileRecord("accnt_db", 0); //storage
        if (is==null) return null;
        try {
            do {
                if (is.available()==0) {a=null; break;}
                a=createFromDataInputStream(is);
                index--;
            } while (index>-1);
            is.close();
            is=null;
        } catch (Exception e) { /*e.printStackTrace();*/ }
        return a;
    }

    public static Account createFromDataInputStream(DataInputStream inputStream){
        int version=0;
        Account a=new Account();
        try {
            version    = inputStream.readByte();
            a.userName = inputStream.readUTF();
            a.password = inputStream.readUTF();
            a.server   = inputStream.readUTF();
            a.hostAddr = inputStream.readUTF();
            a.port     = inputStream.readInt();

            a.nick     = inputStream.readUTF();
            a.resource = inputStream.readUTF();
	    
            a.useSSL=inputStream.readBoolean();
            a.plainAuth=inputStream.readBoolean();
            
	    a.mucOnly=inputStream.readBoolean();
            
            a.setEnableProxy(inputStream.readBoolean());
            a.setProxyHostAddr(inputStream.readUTF());
            a.setProxyPort(inputStream.readInt());

            a.compression=inputStream.readBoolean();

            a.keepAliveType=inputStream.readInt()%4;
            a.keepAlivePeriod=inputStream.readInt();
            
            a.dnsResolver=inputStream.readBoolean(); //firstrun
            
        } catch (IOException e) { /*e.printStackTrace();*/ }
            
        return (a.userName==null)?null:a;
    }

    public void saveToDataOutputStream(DataOutputStream outputStream){
        
        if (hostAddr==null) hostAddr="";
        if (proxyHostAddr==null) proxyHostAddr="";
        
        try {
            outputStream.writeByte(7);
            outputStream.writeUTF(userName);
            outputStream.writeUTF(password);
            outputStream.writeUTF(server);
            outputStream.writeUTF(hostAddr);
            outputStream.writeInt(port);
            
            outputStream.writeUTF(nick);
            outputStream.writeUTF(resource);

            outputStream.writeBoolean(useSSL);
            outputStream.writeBoolean(plainAuth);
	    
	    outputStream.writeBoolean(mucOnly);
            
            outputStream.writeBoolean(enableProxy);
            outputStream.writeUTF(proxyHostAddr);
            outputStream.writeInt(proxyPort);
            
            outputStream.writeBoolean(compression);
			
            outputStream.writeInt(keepAliveType);
            outputStream.writeInt(keepAlivePeriod);
            
            outputStream.writeBoolean(dnsResolver);  //firstrun    
        } catch (IOException e) {
            //e.printStackTrace();
        }
        
    }
    
    public int getImageIndex(){ return active?0:5; }

    public String getUserName() { return userName;  }
    public void setUserName(String userName) { this.userName = userName;  }

    public String getPassword() {  return password;  }
    public void setPassword(String password) { this.password = password;  }

    public String getServer() { return server; }
    public String getHostAddr() { return hostAddr; }
    
    public void setServer(String server) { this.server = server; }

    public void setHostAddr(String hostAddr) { this.hostAddr = hostAddr; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    
    public boolean getUseSSL() { return useSSL; }
    public void setUseSSL(boolean ssl) { this.useSSL = ssl; }

    public boolean getPlainAuth() { return plainAuth; }
    public void setPlainAuth(boolean plain) { this.plainAuth = plain; }
    
    public boolean getDnsResolver() { return dnsResolver; }
    public void setDnsResolver(boolean dnsResolver) { this.dnsResolver = dnsResolver; }
    
    public String getResource() { return resource;  }
    public void setResource(String resource) { this.resource = resource;  }

    public String getNickName() { return (nick.length()==0)?userName:nick;  }
    public String getNick() { return (nick.length()==0)? null:nick;  }
    public void setNick(String nick) { this.nick = nick;  }

    public boolean isMucOnly() { return mucOnly; }
    public void setMucOnly(boolean mucOnly) {  this.mucOnly = mucOnly; }

    public JabberStream openJabberStream() throws java.io.IOException{
        String proxy=null;
        String host=this.server;
        int tempPort=port;
        
        if (hostAddr!=null && hostAddr.length()>0) {
                host=hostAddr;
        } else if (dnsResolver) {
            io.DnsSrvResolver dns=new io.DnsSrvResolver();
            if (dns.getSrv(server)) {
                host=dns.getHost();
                tempPort=dns.getPort();
            } 
        }

	StringBuffer url=new StringBuffer(host).append(':').append(tempPort);
        if (!isEnableProxy()) {
	    url.insert(0, (useSSL)?"ssl://":"socket://");
        } else {
//#if HTTPPOLL
//#             proxy=getProxyHostAddr();
//#elif HTTPCONNECT
//#             proxy="socket://" + getProxyHostAddr() + ':' + getProxyPort();
//#endif  
        }
        return new JabberStream( getServer(), url.toString(), proxy);
    }

    public boolean isEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    public String getProxyHostAddr() {
        return proxyHostAddr;
    }

    public void setProxyHostAddr(String proxyHostAddr) {
        this.proxyHostAddr = proxyHostAddr;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }
 
    public boolean useCompression() { return compression; }
    
    public boolean isGmail() {
        return server.startsWith("gmail.com");
    }
    
    public String getTipString() { return getJid(); }
    
    public boolean useGoogleToken() {
        if (useSSL) return false;
        return isGmail();
    }

    public void setUseCompression(boolean value) {
        this.compression = value;
    }

    public void setActive(boolean b) {
        active=b;
    }

    public int getKeepAliveType() {
        return keepAliveType;
    }

    public int getKeepAlivePeriod() {
        return keepAlivePeriod;
    }

    public void setKeepAlivePeriod(int i) {
        keepAlivePeriod=i;
    }

    public void setKeepAliveType(int i) {
        keepAliveType=i;
    }
}