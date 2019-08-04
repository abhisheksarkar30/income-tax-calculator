/**
 * 
 */
package com.abhi.feature.tax.applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.util.ArrayList;

/**
 * @author abhishek
 *
 */
@SuppressWarnings("serial")
public abstract class CalculatorLayoutManager extends Applet {

	protected ArrayList<TextField> monthlySalaryList;
	protected ArrayList<TextField> annualAdditionList;
	protected ArrayList<TextField> annualDeductionList;
	protected ArrayList<TextField> monthlyDeductionList;

	protected TextField taxableIncome;
	protected TextField tax;
	protected TextField taxWithCess;
	
	protected void setWindowProperties() {
        setFont(new Font("TimesRoman", Font.ITALIC, 12));
        setBackground(new Color(255, 210, 128));
        setSize(650, 250);
        GridLayout gl=new GridLayout(0,6);
        setLayout(gl);
    }
	
    protected TextField addFieldWithLabel(String text, Boolean isEditable) {
        Label la = new Label(text);
        add(la);
        TextField t;
        if(text.equals("Std Exempt:"))
            t = new TextField("40000");
        else
            t = new TextField("");
        if(isEditable)
        	t.setBackground(new Color(210, 237, 249));
        else
        	t.setEditable(isEditable);
        add(t);
        return t;
    }
    
    protected void clearAll() {
    	for(TextField field:monthlySalaryList)
            field.setText("");
    	for(TextField field:annualAdditionList)
            field.setText("");
    	for(TextField field:monthlyDeductionList)
            field.setText("");
    	for(TextField field:annualDeductionList)
    		if(field != annualDeductionList.get(3)) //Std Exempt
    			field.setText("");
    }
	
	protected void addCopyrightCredits() {
        for(int i=1; i<=7; i++)
            add(new Label());
        add(new Label("© Developed"));
        add(new Label("By :"));
        add(new Label("Abhishek"));
        add(new Label("Sarkar"));
        add(new Label());
    }

}
