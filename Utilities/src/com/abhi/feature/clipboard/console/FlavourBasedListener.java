package com.abhi.feature.clipboard.console;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class FlavourBasedListener extends Clipboard{

    private boolean isModified;
    private String last;

    /**
     * Creates a clipboard object.
     *
     * @param name Already assigned
     * @see Toolkit#getSystemClipboard
     */
    private FlavourBasedListener(String name) {
        super(name);
        isModified = Boolean.FALSE;
        last = "";
    }

    public static void main(String args[]) {

        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final FlavourBasedListener listener = new FlavourBasedListener(clipboard.getName());

        FlavorListener lis = new FlavorListener(){
            @Override
            public void flavorsChanged(FlavorEvent e) {
                listener.setContent(clipboard);
            }
        };
        clipboard.addFlavorListener(lis);
        while(true) {}
    }

    private void setContent(Clipboard clipboard) {
        if(isModified)
            isModified = Boolean.FALSE;
        else {
            Transferable t = clipboard.getContents(this);
            if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                clipboard.setContents(processContents(t), owner);
            }
            isModified = Boolean.TRUE;
        }
    }

    private StringSelection processContents(Transferable t) {
        String content = getString(t);
        StringBuilder processed = new StringBuilder();
        for(String iter : content.split("\n")) {
            if(iter != null && !iter.equals(""))
                processed.append(String.format("'%s',", iter));
        }
        if(processed.length() != 0)
            processed = new StringBuilder(processed.substring(0, processed.length() - 1));
        return new StringSelection(processed.toString());
    }

    private String getString(Transferable t) {
        String content = "";
        try {
            content = (String) t.getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e1) {
            e1.printStackTrace();
        }
        if(!last.equals(content)) {
            System.out.println("Processing: " + content);
            last = content;
        }
        return content;
    }
}