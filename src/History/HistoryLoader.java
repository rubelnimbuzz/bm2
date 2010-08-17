/*
 * HistoryLoader.java
 *
 * Created on 18.06.2008, 13:58
 *
 * Copyright (c) 2006-2008, Daniel Apatin (ad), http://apatin.net.ru
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
package History;

import Client.Config;
import Client.Msg;
import Messages.MessageItem;
import io.file.FileIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
//#ifdef DETRANSLIT
//# import util.DeTranslit;
//#endif
import util.Strconv;
import util.StringUtils;
import ui.VirtualList;

/**
 *
 * @author ad
 */
public class HistoryLoader {
    Config cf;
//#ifdef PLUGINS
//#     public static String plugin = new String("PLUGIN_HISTORY");
//#endif

    private VirtualList view;
    private boolean smiles;

    private String fileName="";
    public final long fileSize;

    private byte[] current_block = null;
    private long current_index = -1;
    private int first_m = -1; // index of '<' in first "<m>" in current_block
    private int last_m = -1; //  index of '<' in last "</m>" in current_block

    public static final int BLOCK_SIZE = 4096;

    public final static int MESSAGE_MARKER_OUT=1;
    public final static int MESSAGE_MARKER_PRESENCE=2;
    public final static int MESSAGE_MARKER_IN=3;
    public final static int MESSAGE_MARKER_OTHER=0;

    public HistoryLoader(String file, VirtualList view, boolean smiles) {
        this.view = view;
        this.smiles = smiles;
        cf = Config.getInstance();
//#ifdef DETRANSLIT
//#         file = DeTranslit.get_actual_filename(file);
//#endif
//#ifdef HISTORY
//#         fileName = cf.msgPath + StringUtils.replaceBadChars(file) + ".txt";
//#endif

        fileSize = getFileSize();
    }

    public Vector stepBegin() {
        current_index = -last_m-4;
        return getMIVector(true);
    }

    public Vector stepEnd() {
        current_index = fileSize - first_m + 1;
        return getMIVector(false);
    }

    public Vector stepBack() {
        return getMIVector(false);
    }

    public Vector stepNext() {
        return getMIVector(true);
    }

    private long getFileSize() {
        long size = -1;
        FileIO file = FileIO.createConnection(fileName);
        try {
            InputStream is = file.openInputStream();

            try {
                size = file.fileSize();
            } catch (Exception e) { }

            is.close();
            file.close();
            } catch (IOException e1) {
                try {
                    file.close();
                } catch (IOException e2) { }
            }
//#ifdef DEBUG
//#             System.out.println("Size of \""+fileName+"\" is "+size);
//#endif
        return size;
    }

    private void readByteBlock(long pos) {
        FileIO f = FileIO.createConnection(fileName);
        if (current_block == null) {
            current_block = new byte[BLOCK_SIZE];
        }
        try {
            InputStream is=f.openInputStream();
            is.skip(pos);
            is.read(current_block);
//#ifdef DEBUG
//#         System.out.println("!!! Readed byte block, begin: "+pos);
//#endif
            is.close();

        } catch (IOException e) {
            try { f.close();
                  return;
                } catch (IOException ex2) {/*No messages*/}
        } catch (Exception e) { return; }

        try {
            f.close();
        } catch (Exception e) {/*No messages*/}

        current_index = pos;
    }

    private String getStrFromBytes(int off, int length) {
        String str = new String(current_block, off, length);
        if (cf.cp1251) {
            return Strconv.convCp1251ToUnicode(str);
        }
        return str;
    }

    private long getCorrectIndex(long pos) {
        if (pos < 0) {
            return 0;
        } else if (pos > (fileSize - BLOCK_SIZE)) {
            return (fileSize - BLOCK_SIZE);
        }
        return pos;
    }

    public boolean inBegin() {
        return (current_index <= 0)?true:false;
    }

    public boolean inEnd() {
        return ((current_index+BLOCK_SIZE) >= fileSize)?true:false;
    }
    
    private void readBlock(boolean forward) {
        if (forward) {
            readByteBlock(getCorrectIndex(current_index+last_m+4));
        } else readByteBlock(getCorrectIndex(current_index+first_m-1-BLOCK_SIZE));
    }

    private Vector getMIVector(boolean forward) {
        readBlock(forward);

        Vector listMessages = new Vector();
        int pos = 0;
        first_m = last_m = -1;

        do {
            int start = findOpenStr(current_block, pos, true, 'm');
            int end = findCloseStr(current_block, start + 3, true, 'm');

            if (start < 0 || end < 0) {
                if ((first_m < 0) || (last_m < 0)) {
                    boolean can_step_back = true;
                    boolean can_step_next = true;
                    if (forward)
                        can_step_back = !inBegin();
                    else can_step_next = !inEnd();

                    StringBuffer bigMessage = new StringBuffer();

                    do {
//#ifdef DEBUG
//#                         System.out.println("do");
//#endif
                        if (forward) {
                            bigMessage.append(getStrFromBytes(start, current_block.length-start));
                            readBlock(forward);
                            end = findCloseStr(current_block, 0, true, 'm');
                        } else {
                            bigMessage.insert(0, getStrFromBytes(0, end));
                            readBlock(forward);
                            start = findOpenStr(current_block, current_block.length-3, false, 'm');
                        }
                        first_m = 0;
                        last_m = current_block.length - 3;
                    } while (start < 0 || end < 0);

                    if (forward) {
                        bigMessage.append(getStrFromBytes(start + 3, end - start - 3));
                        can_step_next = !inEnd();
                    } else {
                        bigMessage.insert(0, getStrFromBytes(start + 3, end - start - 3));
                        can_step_back = !inBegin();
                    }

                    current_block = null;

                    String current = bigMessage.toString();
                    bigMessage = null;
                    listMessages.addElement(getMessageItem(processMessage(
                            findBlock(current, "t"),
                            findBlock(current, "d"),
                            findBlock(current, "f"),
                            findBlock(current, "s"),
                            findBlock(current, "b"))));

                    if (can_step_back)
                        listMessages.insertElementAt(HistoryReader.MIPrev, 0);
                    if (can_step_next)
                        listMessages.addElement(HistoryReader.MINext);

                    return listMessages;
                }

                current_block = null;

                if (!inBegin())
                    listMessages.insertElementAt(HistoryReader.MIPrev, 0);
                if (!inEnd())
                    listMessages.addElement(HistoryReader.MINext);

                return listMessages;
            }

            if (first_m < 0)
                first_m = start;
            last_m = end;

            String current = getStrFromBytes(start + 3, end - start - 3);
            listMessages.addElement(getMessageItem(processMessage(
                    findBlock(current, "t"),
                    findBlock(current, "d"),
                    findBlock(current, "f"),
                    findBlock(current, "s"),
                    findBlock(current, "b"))));
            pos = end + 4;
        } while (true);
    }

    // Ищет строку <m> в b и возвращает индекс '<' в b
    private int findOpenStr(byte[] b, int i, boolean forward, char ch) {
        while ((i >= 0) && (i < (b.length-2))) {
            if ((b[i] == '<') && (b[i + 1] == ch) && (b[i + 2] == '>'))
                return i;
            i += forward?1:-1;
        }
        return -1;
    }

    // Ищет строку </m> в b и возвращает индекс '<' в b
    private int findCloseStr(byte[] b, int i, boolean forward, char ch) {
        while ((i >= 0) && (i < (b.length-3))) {
            if ((b[i] == '<') && (b[i + 1] == '/') && (b[i + 2] == ch) && (b[i + 3] == '>'))
                return i;
            i += forward?1:-1;
        }
        return -1;
    }

    static Msg processMessage(String marker, String date, String from, String subj, String body) {
        int msgType = Msg.MESSAGE_TYPE_HISTORY;

        int mrk = Integer.parseInt(marker);

        switch (mrk) {
            case MESSAGE_MARKER_IN:
                msgType = Msg.MESSAGE_TYPE_IN;
                break;
            case MESSAGE_MARKER_OUT:
                msgType = Msg.MESSAGE_TYPE_OUT;
                break;
            case MESSAGE_MARKER_PRESENCE:
                msgType = Msg.MESSAGE_TYPE_PRESENCE;
                break;
        }

        Msg msg = new Msg(msgType,
                StringUtils.unescapeTags(from),
                StringUtils.unescapeTags(subj),
                StringUtils.unescapeTags(body));
        msg.setDayTime(date);

        return msg;
    }

    private MessageItem getMessageItem(Msg msg) {
        return new MessageItem(msg, view, smiles);
    }

    private String findBlock(String source, String needle) {
        int start = source.indexOf("<"+needle+">");
        int end = source.indexOf("</"+needle+">",start);
        if (start<0 || end<0)
            return null;
        return source.substring(start+needle.length()+2, end);
    }
}
