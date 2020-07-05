# income-tax-calculator
![](https://img.shields.io/badge/Release-V1.0.0-blue.svg) ![](https://img.shields.io/badge/Build-Stable-green.svg) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) ![](https://img.shields.io/badge/By-Abhishek%20Sarkar-red.svg?style=social&logo=appveyor)

------------


This is a simple tool to calculate ***Income Tax*** of a salaried person, updated till ***FY2019-20***.

## Prerequisite
Java 7+

## Usage
Directly double-click on the ClipboardProcessingApp.jar file, if proper java.exe is set as default, else java -jar ClipboardProcessingApp.jar

## Supported features
 - Net earning calculation
 - Net taxable income calculation after deducting : EPF, Professional Tax, Other 80C exemptions, HRA, other company benefits, Standard exemptino for salaried person(fixed).
 - Net tax calculation with cess.

## Notes to follow
Points to remember while using the calculator: (This is a buttonless calculator, calculates as you provide inputs)
1. Only digits allowed to be entered in any editable fields of the calculator.
2. Month 1-12 fields: Should contain net salary of each month, not what you received in hand.
3. Bonus field: should only be provided if not already included in any of the Month 1-12 fields.
4. LTA field: should only be provided if not already included in any of the Month 1-12 fields. 
	This indicates the amount you received, not what is to be exempted.
5. Net other 80c field: should contain the amount you invested in 80 other than epf(deducted by company).
6. Net HRA field: you need to put at max the amount(annually) which is to be exempted as per your ctc letter.
7. Net other exempt field: includes flexi bucket(max amount as provided in you ctc slip), LTA to be exempted for your trip, & other exemptions if any.
8. Std Exempt field: Non-editable. Includes fixed exemption by govt for all salaried employee for medical and other exemptions.
9. The 3 fields at the bottom are the result fields, non-editable, separated by commas for readability.
10. From any field if you want to cut(CTRL+X) or copy(CTRL+C), it will automatically copy without the commas if present.
	If comma is needed to be copied, use cut/copy function via mouse click.
