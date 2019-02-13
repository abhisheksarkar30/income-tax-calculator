package com.abhi.feature.tax.applet;

import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class AppletCalculatorService extends IncomeTaxCalculatorFY2018_19 
						implements KeyListener, TextListener {

	private String lastContent;
    private boolean isModified;
	
    @Override
    public void init() {
    	lastContent = "";
    	isModified = Boolean.FALSE;
        monthlySalaryList = new ArrayList<>();
        annualAdditionList = new ArrayList<>();
        annualDeductionList = new ArrayList<>();
        monthlyDeductionList = new ArrayList<>();

        if(getLayout() instanceof GridLayout)
            removeAll();

        setWindowProperties();

        //Add input fields
        for(int i=1; i<=12; i++)
        	monthlySalaryList.add(addFieldWithLabel("Month "+i+":", Boolean.TRUE));
        annualAdditionList.add(addFieldWithLabel("Bonus:", Boolean.TRUE));
        annualAdditionList.add(addFieldWithLabel("LTA:", Boolean.TRUE));
        annualAdditionList.add(addFieldWithLabel("Leave Encashed:", Boolean.TRUE));
        monthlyDeductionList.add(addFieldWithLabel("EPF/month:", Boolean.TRUE));
        monthlyDeductionList.add(addFieldWithLabel("Prof. Tax/month:", Boolean.TRUE));
        annualDeductionList.add(addFieldWithLabel("Net other 80C:", Boolean.TRUE));
        annualDeductionList.add(addFieldWithLabel("Net HRA:", Boolean.TRUE));
        annualDeductionList.add(addFieldWithLabel("Net other exempt:", Boolean.TRUE));
        annualDeductionList.add(addFieldWithLabel("Std Exempt:", Boolean.FALSE));

        //Add result fields
        taxableIncome = addFieldWithLabel("Net Taxable:", Boolean.FALSE);
        tax = addFieldWithLabel("Tax:", Boolean.FALSE);
        taxWithCess = addFieldWithLabel("Tax + 4% cess:", Boolean.FALSE);
        
        addCopyrightCredits();
    }

    @Override
    protected TextField addFieldWithLabel(String text, Boolean isEditable) {
    	TextField t = super.addFieldWithLabel(text, isEditable);
        t.addKeyListener(this);
        if(isEditable)
        	t.addTextListener(this);
        return t;
    }

	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if(c == KeyEvent.VK_ENTER || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_ESCAPE ||
				e.getModifiers() == KeyEvent.CTRL_MASK) {
			if(e.getModifiers() == KeyEvent.CTRL_MASK && (c==24 || c==3)) {//CTRL+X or CTRL+C
				StringSelection copiedText = new StringSelection(((TextField)e.getSource())
						.getSelectedText().replace(",",""));
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(copiedText, copiedText);
			} else if (c == KeyEvent.VK_ESCAPE)
				clearAll();
			return;
		}
		if(!Character.isDigit(c)) {
			e.consume();
			Toolkit.getDefaultToolkit().beep();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		lastContent = ((TextField)e.getSource()).getText();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//Do nothing
	}

	@Override
	public void textValueChanged(TextEvent e) {
		if(isModified)
			isModified = Boolean.FALSE;
		else {
			String regex = "[[0-9]*]{0,9}$";
			TextField field = (TextField)e.getSource();
			if( !field.getText().matches(regex)) {
				int selectionEnd = field.getSelectionEnd();
				field.setText(lastContent);
				field.setSelectionStart(selectionEnd);
				isModified = Boolean.TRUE;
				Toolkit.getDefaultToolkit().beep();
			}
			else {
				lastContent = ((TextField)e.getSource()).getText();
				calculateTaxableIncome();
				calculateTax();
			}
		}
	}
	
//	private boolean isModifiedKeyAcceptable(char c) {
//		
//		switch(c) {
//		case 24://CTRL+X
//		case  3://CTRL+C
//		case 22://CTRL+V
//		case  1://CTRL+A
//		case 26://CTRL+Z
//		case 25://CTRL+Y
//			return Boolean.TRUE;
//		default: return Boolean.FALSE;
//		}
//	}
}