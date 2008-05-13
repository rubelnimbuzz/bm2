/*
 * IconTextList.java
 *
 * Created on 30.01.2005, 18:19
 *
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
 */

package ui;
import Colors.ColorScheme;
import Fonts.FontCache;
import images.RosterIcons;
import javax.microedition.lcdui.*;
import java.util.*;
import Colors.Colors;

abstract public class IconTextElement implements VirtualElement {
    int itemHeight;
    int imageYOfs;
    int fontYOfs;
    
    protected ImageList il;

    private int heightFirstLine=0;

    private int ilImageSize=0;

    private int fontHeight;
//#ifdef SECONDSTRING
//#     private int secondFontHeight;
//#     private Font secondFont;
//#endif
    abstract protected int getImageIndex();

    public int getFontIndex() { return 0;}
    
    private Font getFont() {
        return (getFontIndex()==0)?
            FontCache.getRosterNormalFont():
            FontCache.getRosterBoldFont();
    }

    public void drawItem(Graphics g, int ofs, boolean sel){
       g.setFont(getFont());
       
       String str=toString();
       int offset=4+ilImageSize;
//#ifdef SECONDSTRING
//#        String secstr="";
//#        if (hasSecondString()) {
//#            secstr=getSecondString();
//#        }
//#endif
       
       if (il!=null)
           il.drawImage(g, getImageIndex(), 2, imageYOfs);
           
       g.clipRect(offset, 0, g.getClipWidth(), itemHeight);
       
       g.drawString(str,offset-ofs, fontYOfs, Graphics.TOP|Graphics.LEFT);
       
//#ifdef SECONDSTRING
//#        if (hasSecondString()) {
//#            g.setFont(secondFont);
//#            g.setColor(Colors.SECOND_LINE);
//#            g.drawString(secstr, offset-ofs, fontYOfs+fontHeight-2, Graphics.TOP|Graphics.LEFT);
//#        }
//#endif
    }

    public int getVWidth(){ 
        int wft=getFont().stringWidth(toString());
//#ifdef SECONDSTRING
//#         int wst=0;
//#         if (hasSecondString())
//#             wst=secondFont.stringWidth(getSecondString());
//#         return ((wft>wst)?wft:wst)+ilImageSize+4;   
//#else
            return wft+ilImageSize+4;
//#endif
    }
    
    public int getVHeight(){ 
        itemHeight=heightFirstLine;

        fontYOfs=(itemHeight-fontHeight)/2;
//#ifdef SECONDSTRING
//#         if (hasSecondString()){
//#             itemHeight+=secondFontHeight;
//#         }
//#endif
        imageYOfs=(itemHeight-ilImageSize)/2;
        return itemHeight;
    }
    public int getColorBGnd(){ return Colors.LIST_BGND;}

    public void onSelect(){ };
    
    public IconTextElement(ImageList il) {
        super();
        this.il=il;
        fontHeight=FontCache.getRosterNormalFont().getHeight();
//#ifdef SECONDSTRING
//#         secondFont = FontCache.getBalloonFont();
//#         secondFontHeight=secondFont.getHeight()-2;
//#endif
	if (il!=null){
	    ilImageSize=il.getHeight();
	}
        heightFirstLine=(ilImageSize>fontHeight)?ilImageSize:fontHeight;
    }

    public String getTipString() {
        return null;
    }
    
//#ifdef SECONDSTRING
//#     private boolean hasSecondString() {
//#         try {
//#             String secstr=getSecondString();
//#             if (secstr!=null)
//#                 if (secstr.length()>0)
//#                     return true;
//#         } catch (Exception ex) {}
//#         return false;
//#     }
//# 
//#     public String getSecondString() { 
//#         return null;
//#     }
//#endif
    public int compare(IconTextElement right) { return 0; }
}
