/*
 * MainBar.java
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

package ui;

import Fonts.FontCache;
import images.RosterIcons;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class MainBar extends ComplexString{

//#ifdef BACK_IMAGE
//#     public static Image bg;
//#endif
    
    public MainBar(int size, Object first, Object second, boolean bold) {
        this (size);
        if (first!=null) setElementAt(first,0);
        if (second!=null) setElementAt(second,1);
        
        font = FontCache.getFont(bold, FontCache.bar);
//#ifdef BACK_IMAGE
//#         try {
//#             if (bg==null)
//#                 bg=Image.createImage("/images/panelbg.png");
//#         } catch (Exception e) { }
//#endif        
    }
    
    public MainBar(Object obj) {
        this(1, obj, null, false);
    }
    
    public MainBar(Object obj, boolean bold) {
        this(1, obj, null, bold);
    }
    public MainBar(Object obj, boolean bold, boolean centered) {
        this(1, obj, null, bold);
        this.centered = centered;
    }
    
    public MainBar(int size) {
        super (RosterIcons.getInstance());
        setSize(size);
    }
    public int getVHeight() {
//#ifdef BACK_IMAGE
//#         if (bg != null)
//#             return Math.max(super.getVHeight(), bg.getHeight());
//#endif        
        return super.getVHeight();
    }
    public void drawItem(Graphics g, int offset, boolean selected) {
//#ifdef BACK_IMAGE
//#         if (bg != null) {
//#             for (int i=0; i < g.getClipWidth(); i++)
//#                 g.drawImage(bg, i, 0, Graphics.TOP|Graphics.LEFT);
//#         }
//#endif

        super.drawItem(g, offset, selected);
    }
}
