package com.abhi.feature.clipboard.applet;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
public class ClipboardProcessingService extends UIProcessor implements ClipboardOwner {

	private Clipboard sysClip;
    private String last;

    public ClipboardProcessingService() {
    	while(true) { //Retry on exception
    		try {
    			sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
    			break; //Continue normal operation if no exception occurs
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
            last = sortV? sortContent(content):content;
        }
        originalContent.append("\n" + content);
        return last;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private String sortContent(String content) {
    	String[] rElements = content.split(delimiterStr);
    	java.util.List elements = new ArrayList();
    	for(String element : rElements) {
    		if(NUMBER_ELEMENT.equals(elementTypeV))
    			elements.add(new BigDecimal(element));
    		else
    			elements.add(element);
    	}

    	if(descV)
    		Collections.sort(elements, Collections.reverseOrder());
    	else
    		Collections.sort(elements);

    	String mContent = "";
    	for(Object element : elements) {
    		mContent += String.valueOf(element)+
    				(delimiterStr.equals("\\n")?"\n":delimiterStr);
    	}
    	mContent = mContent.length()>1? mContent.substring(0,mContent.length()-1):mContent;
    	return mContent;
    }

    	private void regainOwnership(Transferable t) {
    		while(true) { //Retry on exception
    			try {
    				sysClip.setContents(t, this);
    				break; //Continue normal operation if no exception occurs
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
    		while(true) { //Retry on exception
    			try {
    				return sysClip.getContents(this); //Continue normal operation if no exception occurs
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