package com.abhi.feature.tax.applet;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.ArrayList;
import java.util.HashMap;

import com.abhi.feature.tax.fy2018_19.IncomeTaxCalculatorFY2018_19;

@SuppressWarnings("serial")
public class AppletCalculatorTextBased extends IncomeTaxCalculatorFY2018_19 implements TextListener {

    private boolean isModified;
    private HashMap<TextField, String> contentList;

    @Override
    public void init() {
        isModified = Boolean.FALSE;
        contentList = new HashMap<>();
        monthlySalaryList = new ArrayList<>();
        annualAdditionList = new ArrayList<>();
        annualDeductionList = new ArrayList<>();
        monthlyDeductionList = new ArrayList<>();

        if(getLayout() instanceof GridLayout)
            removeAll();

        setWindowProperties();

        //Add input fields
        for(int i=1; i<=12; i++)
            addFieldWithLabel("Month "+i+":", monthlySalaryList);
        addFieldWithLabel("Bonus:", annualAdditionList);
        addFieldWithLabel("LTA:", annualAdditionList);
        addFieldWithLabel("Leave Encashed:", annualAdditionList);
        addFieldWithLabel("EPF/month:", monthlyDeductionList);
        addFieldWithLabel("Prof. Tax/month:", monthlyDeductionList);
        addFieldWithLabel("Net other 80C:", annualDeductionList);
        addFieldWithLabel("Net HRA:", annualDeductionList);
        addFieldWithLabel("Net other exempt:", annualDeductionList);
        addFieldWithLabel("Std Exempt:", annualDeductionList);

        //addResultFields();
        
        addCopyrightCredits();
    }

    protected void addFieldWithLabel(String text, ArrayList<TextField> typeList) {
        Label la = new Label(text);
        add(la);
        TextField t;
        if(text.equals("Std Exempt:")) {
            t = new TextField("40000");
            t.setEditable(Boolean.FALSE);
        } else {
            t = new TextField("");
            t.setBackground(new Color(210, 237, 249));
            t.addTextListener(this);
        }
        contentList.put(t, "");
        typeList.add(t);
        add(t);
    }

    @Override
    public void textValueChanged(TextEvent e) {
        if(isModified)
            isModified = Boolean.FALSE;
        else {
            TextField temp = (TextField) e.getSource();
            //String regEx = "[[0-9]*\\.?[0-9]*]{0,9}$";
            String regex = "[[0-9]*]{0,9}$";
            int selectionEnd = temp.getSelectionEnd();
            temp.setText(temp.getText().matches(regex) ? temp.getText(): contentList.get(temp));
            temp.setSelectionStart(selectionEnd);
            contentList.put(temp, temp.getText());
            calculateTax();
            isModified = Boolean.TRUE;
        }
    }
}