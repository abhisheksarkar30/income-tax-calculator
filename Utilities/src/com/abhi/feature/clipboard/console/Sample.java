package com.abhi.feature.clipboard.console;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class Sample implements ClipboardOwner {

    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public Sample() {

        FlavorListener lis = new FlavorListener(){
            @Override
            public void flavorsChanged(FlavorEvent e) {
                System.out.println(e);
            }
        };
        clipboard.addFlavorListener(lis);
        for(String temp=""; true; temp+=1)
        clipboard.setContents(new StringSelection(temp), this);
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        System.out.println("Clipboard contents replaced");
    }

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        Sample test = new Sample();
    }
}