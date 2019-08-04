package com.abhi.feature.tax.console;

import java.util.Scanner;

import static java.lang.System.*;

public class ConsoleCalculatorFY18_19 {

    public static void main(String[] args) {
	    Scanner sc = new Scanner(in);
        int sum = 0;
        out.println("Enter salary of each month(if any exceeds 2.5lacs will be considered lumpsum," +
                " and remaining months' salary has been already included!");
        for(int i=1; i <=12; i++) {
            out.print("Net Salary of month "+i+": ");
            sum += sc.nextInt();
            if(sum>250000)
                break;
        }
        out.println("Net Taxable: " + sum);
        out.print("Bonus received: ");
        sum += sc.nextInt();
        out.println("Net Taxable: " + sum);
        out.print("LTA received: ");
        sum += sc.nextInt();
        out.println("Net Taxable: " + sum);
        out.print("Professional Tax deducted(/month): ");
        sum -= sc.nextInt()*12;
        out.println("Net Taxable: " + sum);
        out.print("EPF deducted(/month) : ");
        int epf;
        sum -= epf = sc.nextInt()*12;
        out.println("Net Taxable: " + sum);
        out.print("Net Other 80c investments(excl. epf) of the fiscal: ");
        int sec80cRem = sc.nextInt();
        sum -= (sec80cRem + epf) > 150000? 150000-epf : sec80cRem;
        out.print("Net house rent paid annually: ");
        sum -= sc.nextInt();
        out.println("Net Taxable: " + sum);
        out.print("Net Other exempted investments(excl. 80c, 80g(HRA)) of the fiscal: ");
        sum -= sc.nextInt();
        out.println("Net Taxable: " + sum);
        out.print("Net flexi bucket availed of the fiscal: ");
        sum -= sc.nextInt();
        out.println("Net Taxable: " + sum);
        sum -= 40000;
        out.println("Net Taxable after standard deduction Rs 40000: " + sum);
        double tax = 0;
        if(sum>250000 && sum<=500000) {
            tax = (sum - 250000) * 0.05;
            if(sum<350000)
                tax = tax>2500? tax-2500:0;
        }
        else if(sum>500000 && sum<=1000000)
            tax = (sum-500000)*0.2 + 250000*0.05;
        else if(sum>1000000)
            tax = (sum-1000000)*0.3 + 250000*0.2 + 250000*0.05;
        out.println("Tax: " + tax);
        out.println("Tax incl. 4% cess: " + tax*1.04);
        sc.close();
    }
}
