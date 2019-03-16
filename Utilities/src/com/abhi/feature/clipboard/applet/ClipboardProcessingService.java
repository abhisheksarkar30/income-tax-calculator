package com.abhi.feature.clipboard.applet;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class ClipboardProcessingService extends UIProcessor implements ClipboardOwner {

	private Clipboard sysClip;
    private String last;

    public ClipboardProcessingService() {
    	while(true) {
    		try {
    			sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
    			break;
    		} catch (HeadlessException e) {
    			System.out.println("Exception occurred: " + e);
    			try {
					Thread.sleep(20);
				} catch(Exception e1) {
					System.out.println("Exception: " + e1);
				}
    		}
    	}
        last = "";
    }

    @Override
   	public void init() {
   		super.init();
   		Transferable trans = getContents();
        regainOwnership(trans);
        System.out.println("Listening to board...");
    }

    @Override
    public void lostOwnership(Clipboard c, Transferable t) {
        try {
            Thread.sleep(20);
        } catch(Exception e) {
            System.out.println("Exception: " + e);
        }
        Transferable contents = getContents();
        regainOwnership(processContents(contents));
    }

    private StringSelection processContents(Transferable t) {
        String content = getString(t);
        StringBuilder processed = new StringBuilder();
        for(String iter : content.split(delimiterStr)) {
            if(iter != null && !iter.equals(""))
                processed.append(String.format("%s%s%s%s", tokenStartsWithStr, iter,
                		tokenEndsWithStr, separatorStr));
        }
        if(processed.length() != 0)
            processed = new StringBuilder(finalStartsWithStr + processed.substring(0, processed.lastIndexOf(separatorStr))
            	+ finalEndsWithStr);
        processedContent.append("\n" + processed.toString());
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
        originalContent.append("\n" + content);
        return content;
    }

    private void regainOwnership(Object t) {
    	while(true) {
    		try {
    			if(t instanceof Transferable) {
    				sysClip.setContents((Transferable) t, this);
    			} else {
    				sysClip.setContents((StringSelection) t, this);
    			}
    			break;
    		} catch (IllegalStateException e) {
    			System.out.println("Exception occurred: " + e);
    			try {
    				Thread.sleep(20);
    			} catch(Exception e1) {
    				System.out.println("Exception: " + e1);
    			}
    		}
    	}
    }
    
    private Transferable getContents() {
    	while(true) {
    		try {
    			return sysClip.getContents(this);
    		} catch (IllegalStateException e) {
    			System.out.println("Exception occurred: " + e);
    			try {
    				Thread.sleep(20);
    			} catch(Exception e1) {
    				System.out.println("Exception: " + e1);
    			}
    		}
    	}
    }
}