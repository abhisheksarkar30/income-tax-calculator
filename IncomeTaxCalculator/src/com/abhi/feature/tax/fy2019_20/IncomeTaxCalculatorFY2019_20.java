/**
 * 
 */
package com.abhi.feature.tax.fy2019_20;

import java.awt.TextField;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.abhi.feature.tax.applet.CalculatorLayoutManager;

/**
 * @author abhishek
 *
 */
@SuppressWarnings("serial")
public abstract class IncomeTaxCalculatorFY2019_20 extends CalculatorLayoutManager {
	
	private long taxableSum;
	
	protected final String stdDeduction = "50000";
	private final String appHeader = "Income Tax Calculator: FY2019_20";
	
	@Override
	protected String getStdDeduction() {
		return stdDeduction;
	}
	
	@Override
	public String getAppHeader() {
		return appHeader;
	}

	protected void calculateTax() {
        double tax = 0;
        if(taxableSum>250000 && taxableSum<=500000) {
            tax = (taxableSum - 250000) * 0.05;
            if(taxableSum<=500000)
                tax = tax>12500? tax-12500:0; // No tax for this slab
        } else if(taxableSum>500000 && taxableSum<=1000000)
            tax = (taxableSum-500000)*0.2 + 250000*0.05;
        else if(taxableSum>1000000) {
            tax = (taxableSum-1000000)*0.3 + 250000*0.2 + 250000*0.05;
            
            //Surcharge on tax
            if(taxableSum>5000000 && taxableSum<=10000000)
            	tax *= 1.1;
            else if(taxableSum>10000000)
            	tax *= 1.15;
        }
        this.tax.setText(getRoundedAmount(tax));
        this.taxWithCess.setText(getRoundedAmount(tax*1.04));
    }

	protected void calculateTaxableIncome() {
		taxableSum = 0L;
        for(TextField field:monthlySalaryList)
            taxableSum += field.getText().isEmpty()? 0 : Long.parseLong(field.getText());
        for(TextField field:annualAdditionList)
            taxableSum += field.getText().isEmpty()? 0 : Long.parseLong(field.getText());
        for(int i= 0; i<annualDeductionList.size(); i++) {
            TextField field = annualDeductionList.get(i);
            if (i==0) {
                long sec80cRem = field.getText().isEmpty() ? 0L : Long.parseLong(field.getText());
                long epf = monthlyDeductionList.get(0).getText().isEmpty() ? 0L :
                	Long.parseLong(monthlyDeductionList.get(0).getText())*12;
                taxableSum -= (sec80cRem + epf) > 150000L ? 150000L: (sec80cRem + epf);
            }
            else
                taxableSum -= field.getText().isEmpty() ? 0L : Long.parseLong(field.getText());
        }
        taxableSum -= monthlyDeductionList.get(1).getText().isEmpty() ? 0L :
        	Long.parseLong(monthlyDeductionList.get(1).getText())*12;
            
        taxableIncome.setText(getRoundedAmount(taxableSum<0? 0: taxableSum));
    }
	
	private String getRoundedAmount(double amount) {
		double rounded = Math.round(amount*100.0)/100.0;
		NumberFormat decimalPattern = new DecimalFormat("#,##0.00");
		return String.valueOf(decimalPattern.format(rounded));
	}
	
}
