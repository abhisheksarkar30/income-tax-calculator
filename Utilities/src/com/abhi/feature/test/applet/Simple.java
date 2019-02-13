package com.abhi.feature.test.applet;

import java.applet.Applet;
import java.awt.Graphics;
public class Simple extends Applet{

    /**
	 * 
	 */
	private static final long serialVersionUID = 7560346105217166222L;

	public void paint(Graphics g){
        g.drawString("welcome to applet",150,150);
    }

}