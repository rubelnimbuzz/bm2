/*
 * ColorUtils.java
 *
 * Created on 27 ���� 2008 �., 10:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ui;

//#ifdef COLORS
//# import Client.StaticData;
//# import io.file.FileIO;
//# import java.io.InputStream;
//# import java.util.Hashtable;
//# import io.NvStorage;
//# import java.io.DataInputStream;
//# import java.io.DataOutputStream;
//# import java.io.IOException;
//# import ui.ColorScheme;
//# import midlet.Colors;
//# import util.StringLoader;
//# import util.Translit;
//#endif
/**
 *
 * @author ad
 */
public class ColorUtils {
    
    /** Creates a new instance of ColorUtils */
    public ColorUtils() { }
    
//#ifdef COLORS
//# 
//#     private static Hashtable skin;
//#     private static String skinFile;
//#     private static int resourceType=1;
//# 
//#     private static Colors cs=Colors.getInstance();
//# 
//#     public static void loadSkin(String skinF, int resourceT){
//#         skinF=skinFile;
//#         resourceT=resourceType;
//#         try {
//#             cs.BALLOON_INK=loadInt("BALLOON_INK", cs.BALLOON_INK);
//#             cs.BALLOON_BGND=loadInt("BALLOON_BGND", cs.BALLOON_BGND);
//#             cs.LIST_BGND=loadInt("LIST_BGND", cs.LIST_BGND);
//#             cs.LIST_BGND_EVEN=loadInt("LIST_BGND_EVEN", cs.LIST_BGND_EVEN);
//#             cs.LIST_INK=loadInt("LIST_INK", cs.LIST_INK);
//#             cs.MSG_SUBJ=loadInt("MSG_SUBJ", cs.MSG_SUBJ);
//#             cs.MSG_HIGHLIGHT=loadInt("MSG_HIGHLIGHT", cs.MSG_HIGHLIGHT);
//#             cs.DISCO_CMD=loadInt("DISCO_CMD", cs.DISCO_CMD);
//#             cs.BAR_BGND=loadInt("BAR_BGND", cs.BAR_BGND);
//#             cs.BAR_BGND_BOTTOM=loadInt("BAR_BGND_BOTTOM", cs.BAR_BGND_BOTTOM);
//#             cs.BAR_INK=loadInt("BAR_INK", cs.BAR_INK);
//#             cs.CONTACT_DEFAULT=loadInt("CONTACT_DEFAULT", cs.CONTACT_DEFAULT);
//#             cs.CONTACT_CHAT=loadInt("CONTACT_CHAT", cs.CONTACT_CHAT);
//#             cs.CONTACT_AWAY=loadInt("CONTACT_AWAY", cs.CONTACT_AWAY);
//#             cs.CONTACT_XA=loadInt("CONTACT_XA", cs.CONTACT_XA);
//#             cs.CONTACT_DND=loadInt("CONTACT_DND", cs.CONTACT_DND);
//#             cs.GROUP_INK=loadInt("GROUP_INK", cs.GROUP_INK);
//#             cs.BLK_INK=loadInt("BLK_INK", cs.BLK_INK);
//#             cs.BLK_BGND=loadInt("BLK_BGND", cs.BLK_BGND);
//#             cs.MESSAGE_IN=loadInt("MESSAGE_IN", cs.MESSAGE_IN);
//#             cs.MESSAGE_OUT=loadInt("MESSAGE_OUT", cs.MESSAGE_OUT);
//#             cs.MESSAGE_PRESENCE=loadInt("MESSAGE_PRESENCE", cs.MESSAGE_PRESENCE);
//#if NICK_COLORS
//#             cs.MESSAGE_IN_S=loadInt("MESSAGE_IN_S", cs.MESSAGE_IN_S);
//#             cs.MESSAGE_OUT_S=loadInt("MESSAGE_OUT_S", cs.MESSAGE_OUT_S);
//#             cs.MESSAGE_PRESENCE_S=loadInt("MESSAGE_PRESENCE_S", cs.MESSAGE_PRESENCE_S);
//#endif
//#             cs.MESSAGE_AUTH=loadInt("MESSAGE_AUTH", cs.MESSAGE_AUTH);
//#             cs.MESSAGE_HISTORY=loadInt("MESSAGE_HISTORY", cs.MESSAGE_HISTORY);
//#             cs.PGS_REMAINED=loadInt("PGS_REMAINED", cs.PGS_REMAINED);
//#             cs.PGS_COMPLETE=loadInt("PGS_COMPLETE", cs.PGS_COMPLETE);
//#             //cs.PGS_BORDER=loadInt("PGS_BORDER", cs.PGS_BORDER);
//#             //cs.PGS_BGND=loadInt("PGS_BGND", cs.PGS_BGND);
//#             cs.HEAP_TOTAL=loadInt("HEAP_TOTAL", cs.HEAP_TOTAL);
//#             cs.HEAP_FREE=loadInt("HEAP_FREE", cs.HEAP_FREE);
//#             cs.CURSOR_BGND=loadInt("CURSOR_BGND", cs.CURSOR_BGND);
//#             cs.CURSOR_OUTLINE=loadInt("CURSOR_OUTLINE", cs.CURSOR_OUTLINE);
//#             cs.SCROLL_BRD=loadInt("SCROLL_BRD", cs.SCROLL_BRD);
//#             cs.SCROLL_BAR=loadInt("SCROLL_BAR", cs.SCROLL_BAR);
//#             cs.SCROLL_BGND=loadInt("SCROLL_BGND", cs.SCROLL_BGND);
//#             cs.CONTACT_J2J=loadInt("CONTACT_J2J", cs.CONTACT_J2J);
//#             ColorScheme.saveToStorage();
//#         } catch (Exception e) { }
//#         skin=null;
//#         skinFile=null;
//#     }
//#     
//#     private static int loadInt(String key, int defaultColor) {
//#         if (skin==null) {
//#             //System.out.println(skinFile);
//# 
//#             switch (resourceType) {
//#if FILE_IO
//#                 case 0:
//#                     byte[] b = null;
//#                     int len=0;
//#                     try {
//#                         FileIO f=FileIO.createConnection(skinFile);
//#                         InputStream is=f.openInputStream();
//#                         len=(int)f.fileSize();
//#                         b=new byte[len];
//# 
//#                         is.read(b);
//#                         is.close();
//#                         f.close();
//#                     } catch (Exception e) {}
//#                     if (b!=null) {
//#                         String str=new String(b, 0, len).toString().trim();
//#                         skin=new StringLoader().hashtableLoaderFromString(str);
//#                     } else
//#                     return defaultColor;
//#                     break;
//#endif
//#                 case 1:
//#                     skin=new StringLoader().hashtableLoader(skinFile);
//#                     break;
//#                     
//#                 case 2:
//#                     skin=new StringLoader().hashtableLoaderFromString(skinFile);
//#             }
//#         }
//#         try {
//#             String value=(String)skin.get(key);
//#             return getColorInt(value);
//#         } catch (Exception e) {
//#             //StaticData.getInstance().roster.errorLog(e.toString());
//#             return defaultColor;
//#         }
//#     }
//#     
//#     public static String getSkin(){
//#         StringBuffer body=new StringBuffer();
//#ifdef TRANSLIT
//#         body.append("xmlSkin\t"+Translit.translit(StaticData.getInstance().account.getNickName()));
//#else
//#         body.append("xmlSkin\t"+StaticData.getInstance().account.getNickName());
//#endif
//#         body.append("\r\n");
//#         body.append("BALLOON_INK\t"+getColorString(cs.BALLOON_INK)+"\r\n");
//#         body.append("BALLOON_BGND\t"+getColorString(cs.BALLOON_BGND)+"\r\n");
//#         body.append("LIST_BGND\t"+getColorString(cs.LIST_BGND)+"\r\n");
//#         body.append("LIST_BGND_EVEN\t"+getColorString(cs.LIST_BGND_EVEN)+"\r\n");
//#         body.append("LIST_INK\t"+getColorString(cs.LIST_INK)+"\r\n");
//#         body.append("MSG_SUBJ\t"+getColorString(cs.MSG_SUBJ)+"\r\n");
//#         body.append("MSG_HIGHLIGHT\t"+getColorString(cs.MSG_HIGHLIGHT)+"\r\n");
//#         body.append("DISCO_CMD\t"+getColorString(cs.DISCO_CMD)+"\r\n");
//#         body.append("BAR_BGND\t"+getColorString(cs.BAR_BGND)+"\r\n");
//#         body.append("BAR_BGND_BOTTOM\t"+getColorString(cs.BAR_BGND_BOTTOM)+"\r\n");
//#         body.append("BAR_INK\t"+getColorString(cs.BAR_INK)+"\r\n");
//#         body.append("CONTACT_DEFAULT\t"+getColorString(cs.CONTACT_DEFAULT)+"\r\n");
//#         body.append("CONTACT_CHAT\t"+getColorString(cs.CONTACT_CHAT)+"\r\n");
//#         body.append("CONTACT_AWAY\t"+getColorString(cs.CONTACT_AWAY)+"\r\n");
//#         body.append("CONTACT_XA\t"+getColorString(cs.CONTACT_XA)+"\r\n");
//#         body.append("CONTACT_DND\t"+getColorString(cs.CONTACT_DND)+"\r\n");
//#         body.append("GROUP_INK\t"+getColorString(cs.GROUP_INK)+"\r\n");
//#         body.append("BLK_INK\t"+getColorString(cs.BLK_INK)+"\r\n");
//#         body.append("BLK_BGND\t"+getColorString(cs.BLK_BGND)+"\r\n");
//#         body.append("MESSAGE_IN\t"+getColorString(cs.MESSAGE_IN)+"\r\n");
//#         body.append("MESSAGE_OUT\t"+getColorString(cs.MESSAGE_OUT)+"\r\n");
//#         body.append("MESSAGE_PRESENCE\t"+getColorString(cs.MESSAGE_PRESENCE)+"\r\n");
//#if NICK_COLORS
//#         body.append("MESSAGE_IN_S\t"+getColorString(cs.MESSAGE_IN_S)+"\r\n");
//#         body.append("MESSAGE_OUT_S\t"+getColorString(cs.MESSAGE_OUT_S)+"\r\n");
//#         body.append("MESSAGE_PRESENCE_S\t"+getColorString(cs.MESSAGE_PRESENCE_S)+"\r\n");
//#endif
//#         body.append("MESSAGE_AUTH\t"+getColorString(cs.MESSAGE_AUTH)+"\r\n");
//#         body.append("MESSAGE_HISTORY\t"+getColorString(cs.MESSAGE_HISTORY)+"\r\n");
//#         body.append("PGS_REMAINED\t"+getColorString(cs.PGS_REMAINED)+"\r\n");
//#         body.append("PGS_COMPLETE\t"+getColorString(cs.PGS_COMPLETE)+"\r\n");
//#         //body.append("PGS_BORDER\t"+getColorString(cs.PGS_BORDER)+"\r\n");
//#         //body.append("PGS_BGND\t"+getColorString(cs.PGS_BGND)+"\r\n");
//#         body.append("HEAP_TOTAL\t"+getColorString(cs.HEAP_TOTAL)+"\r\n");
//#         body.append("HEAP_FREE\t"+getColorString(cs.HEAP_FREE)+"\r\n");
//#         body.append("CURSOR_BGND\t"+getColorString(cs.CURSOR_BGND)+"\r\n");
//#         body.append("CURSOR_OUTLINE\t"+getColorString(cs.CURSOR_OUTLINE)+"\r\n");
//#         body.append("SCROLL_BRD\t"+getColorString(cs.SCROLL_BRD)+"\r\n");
//#         body.append("SCROLL_BAR\t"+getColorString(cs.SCROLL_BAR)+"\r\n");
//#         body.append("SCROLL_BGND\t"+getColorString(cs.SCROLL_BGND)+"\r\n");
//#         body.append("CONTACT_J2J\t"+getColorString(cs.CONTACT_J2J)+"\r\n");
//#         return body.toString();
//#     }
//# 
//#     public static String ColorToString(int cRed, int cGreen, int cBlue) {
//#         StringBuffer color=new StringBuffer(8);
//#         
//#         color.append("0x");
//#         color.append(expandHex(cRed));
//#         color.append(expandHex(cGreen));
//#         color.append(expandHex(cBlue));
//#         
//#         return color.toString();
//#     }
//#     
//#     public static String expandHex(int eVal) {
//#         String rVal=Integer.toHexString(eVal);
//#         if (rVal.length()==1) rVal="0"+rVal;
//#       
//#         return rVal;
//#     }
//#     
//#     public static int getColorInt(int color, int pos) {
//#         String ncolor = getColorString(color);
//# 
//#         switch (pos) {
//#             case 0:
//#                 return Integer.parseInt(ncolor.substring(2,4),16);
//#             case 1:
//#                 return Integer.parseInt(ncolor.substring(4,6),16);
//#             case 2:
//#                 return Integer.parseInt(ncolor.substring(6,8),16);
//#         }
//#         return -1;
//#     }
//#     
//#     public static String getColorString(int color) {
//#         StringBuffer ncolor=new StringBuffer();
//#         
//#         ncolor.append("0x");
//#         
//#         String col=Integer.toHexString(color);
//#         
//#         for (int i=0; i<6-col.length(); i++)
//#             ncolor.append("0");
//#         
//#         ncolor.append(col);
//# 
//#         return ncolor.toString();
//#     }
//#     
//#     public static int getColorInt(String color) { // 0x010000 -> 1
//#         return Integer.parseInt(color.substring(2),16);
//#     }
//#endif
}
