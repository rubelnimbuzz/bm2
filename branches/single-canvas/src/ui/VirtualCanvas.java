/*
 * NativeCanvas.java
 *
 * Created on 31 Август 2009 г., 19:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ui;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Vladimir Krukov
 */
public class VirtualCanvas extends Canvas {
    private VirtualList list;
    
    /** Creates a new instance of NativeCanvas */
    public VirtualCanvas() {
        setFullScreenMode(true);
    }
    public void show(VirtualList virtualList) {
        KeyRepeatTimer.stop();
        if (this == midlet.BombusMod.getInstance().getDisplay().getCurrent()
                && isShown()) {
            if (null != list) {
                list.hideNotify();
            }
            list = virtualList;
            list.showNotify();
            repaint();
            midlet.BombusMod.getInstance().getDisplay().setCurrent(this);
            
        } else {
            list = virtualList;
            midlet.BombusMod.getInstance().getDisplay().setCurrent(this);
        }
        
    }
    public VirtualList getList() {
        return list;
    }

    protected void paint(Graphics graphics) {
        list.paint(graphics);
    }
    protected void keyPressed(int keyCode) {
        list.keyPressed(keyCode);
        KeyRepeatTimer.start(keyCode, list);
    }
    protected final void keyRepeated(int keyCode){
        if (Canvas.KEY_POUND == keyCode) {
            list.keyReRepeated(keyCode);
        }
    }
    protected void keyReleased(int keyCode) {
        KeyRepeatTimer.stop();
        list.keyReleased(keyCode);
    }
    
    protected void pointerPressed(int x, int y) {
        list.pointerPressed(x, y);
    }
    protected void pointerDragged(int x, int y) {
        list.pointerDragged(x, y);
    }
    protected void pointerReleased(int x, int y) {
        list.pointerReleased(x, y);
    }

    protected void showNotify() {
        list.showNotify();
    }
    protected void hideNotify() {
        if (null != list) {
            KeyRepeatTimer.stop();
            list.hideNotify();
        }
    }
    
    protected void sizeChanged(int w, int h) {
        list.sizeChanged(w, h);
    }
    public static final VirtualCanvas nativeCanvas = new VirtualCanvas();
}

class KeyRepeatTimer extends TimerTask {
    private static Timer timer = new Timer();
    private int key;
    private VirtualList canvas;
    private int slowlyIterations = 8;
    
    
    public static void start(int key, VirtualList c) {
        if (Canvas.KEY_POUND == key) {
            return;
        }
        stop();
        timer = new Timer();
        KeyRepeatTimer repeater = new KeyRepeatTimer(key, c);
        timer.schedule(repeater, 400, 100);
    }
    public static void stop() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }
    
    private KeyRepeatTimer(int keyCode, VirtualList c) {
        key = keyCode;
        canvas = c;
    }
    
    public void run() {
        if (0 < slowlyIterations) {
            slowlyIterations--;
            if (0 != slowlyIterations % 2) {
                return;
            }
        }
        if (!canvas.isShown()) {
            KeyRepeatTimer.stop();
            return;
        }
        canvas.keyReRepeated(key);
    }
}
