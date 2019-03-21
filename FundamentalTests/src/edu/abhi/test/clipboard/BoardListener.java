package edu.abhi.test.clipboard;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class BoardListener extends Thread implements ClipboardOwner {

    private Clipboard sysClip;
    private String last;

    private BoardListener() {
        sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        last = "";
    }

    public void run() {
        Transferable trans = sysClip.getContents(this);
        regainOwnership(trans);
        System.out.println("Listening to board...");
        while(true) {}
    }

    public void lostOwnership(Clipboard c, Transferable t) {
        try {
            Thread.sleep(20);
        } catch(Exception e) {
            System.out.println("Exception: " + e);
        }
        Transferable contents = sysClip.getContents(this);
        regainOwnership(processContents(contents));
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

    private void regainOwnership(Transferable t) {
        sysClip.setContents(t, this);
    }

    private void regainOwnership(StringSelection t) {
        sysClip.setContents(t, this);
    }

    public static void main(String[] args) {
        BoardListener b = new BoardListener();
        b.start();
    }
}