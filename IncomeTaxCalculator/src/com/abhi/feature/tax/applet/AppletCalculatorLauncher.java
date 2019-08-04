package com.abhi.feature.tax.applet;

public class AppletCalculatorLauncher {
	 
    public static void main(String [] args)
     {
        // create an object of type CaesarCode which is the main applet class
    	AppletCalculatorService theApplet = new AppletCalculatorService();
        theApplet.init();   // invoke the applet's init() method
        theApplet.start();  // starts the applet

        // Create a window (JFrame) and make applet the content pane.
         javax.swing.JFrame window = new javax.swing.JFrame(theApplet.getAppHeader());
         window.setContentPane(theApplet);
         window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
         window.pack();              // Arrange the components.
         window.setVisible(true);    // Make the window visible.
       }

}