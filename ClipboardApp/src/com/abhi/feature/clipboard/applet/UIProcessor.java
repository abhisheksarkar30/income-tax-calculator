/**
 * 
 */
package com.abhi.feature.clipboard.applet;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;

/**
 * @author abhis
 *
 */
@SuppressWarnings("serial")
public abstract class UIProcessor extends Applet implements ActionListener {

	protected static final String STRING_ELEMENT = "String";
	protected static final String NUMBER_ELEMENT = "Number";
	private int FIXED_COLUMN_SIZE = 6;
	private TextField delimiter, separator;
	private TextField tokenStartsWith, tokenEndsWith, finalStartsWith, finalEndsWith;
	private TextField replaceThis, replaceWith;
	protected String delimiterStr, separatorStr;
	protected String tokenStartsWithStr, tokenEndsWithStr, finalStartsWithStr, finalEndsWithStr;
	protected String replaceThisStr, replaceWithStr;
	protected TextArea originalContent, processedContent;
	private Button submit;
	private Checkbox sort, desc;
	private Choice elementType;
	protected boolean sortV, descV;
	protected String elementTypeV;
	
	private void setWindowProperties() {
        setFont(new Font("TimesRoman", Font.ITALIC, 12));
        setBackground(new Color(255, 250, 240));
        setSize(650, 250);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Panel rows[] = new Panel[4];
        for(int rowCount = 1; rowCount <= 4; rowCount++)
        	add(rows[rowCount-1] = new Panel(new GridLayout(1, FIXED_COLUMN_SIZE)));
		populateRowPanels(rows);
    }
	
    private void populateRowPanels(Panel args[]) {
    	delimiter = addFieldWithLabel("Delimiter", Boolean.TRUE, args[0], "\\n");
    	tokenStartsWith = addFieldWithLabel("Token Starts With", Boolean.TRUE, args[0], "'");
    	tokenEndsWith = addFieldWithLabel("Token Ends With", Boolean.TRUE, args[0], "'");
    	separator= addFieldWithLabel("Separator", Boolean.TRUE, args[1], ",");
    	finalStartsWith = addFieldWithLabel("Final Starts With", Boolean.TRUE, args[1]);
    	finalEndsWith = addFieldWithLabel("Final Ends With", Boolean.TRUE, args[1]);
    	replaceThis = addFieldWithLabel("Replace", Boolean.TRUE, args[2]);
    	replaceWith = addFieldWithLabel("Replace With", Boolean.TRUE, args[2]);
    	
    	sort = (Checkbox) args[2].add(new Checkbox("Sort"));
    	desc = (Checkbox) args[2].add(new Checkbox("Desc"));

    	args[3].add(new Label("Element Type"));
    	elementType = (Choice) args[3].add(new Choice());
    	elementType.add(STRING_ELEMENT);
    	elementType.add(NUMBER_ELEMENT);
    	
    	args[3].add(new Label());
    	args[3].add(new Label());
    	args[3].add(new Label());
    	submit = (Button) args[3].add(new Button("Submit"));
    	submit.addActionListener(this);
	}
    
    protected TextField addFieldWithLabel(String text, Boolean isEditable, Panel row) {
		return addFieldWithLabel(text, isEditable, row, "");
    }

	protected TextField addFieldWithLabel(String text, Boolean isEditable, Panel row, String defaultValue) {
        Label la = new Label(text);
        row.add(la);
        TextField t;
        t = new TextField(defaultValue);
        if(isEditable)
        	t.setBackground(new Color(210, 237, 249));
        else
        	t.setEditable(isEditable);
        row.add(t);
        return t;
    }
    
	@Override
	public void init() {
		setWindowProperties();
		
		addCopyrightCredits();
		addLogDisplay();
		actionPerformed(new ActionEvent("", 0, "dummy"));
	}

	protected void addCopyrightCredits() {
		Panel lastRow = new Panel(new GridLayout(1, FIXED_COLUMN_SIZE));
		lastRow.add(new Label());
		lastRow.add(new Label("© Developed"));
		lastRow.add(new Label("By :"));
		lastRow.add(new Label("Abhishek"));
		lastRow.add(new Label("Sarkar"));
		lastRow.add(new Label());
		add(lastRow);
    }
	
	protected void addLogDisplay() {
		Color logBackground = Color.BLACK, logForeground = Color.GREEN;
		String message = "----------------------------------------------------";
		message = message + "Log of originally copied content" + message;
		originalContent = new TextArea(message, 5, FIXED_COLUMN_SIZE);
		originalContent.setBackground(logBackground);
		originalContent.setForeground(logForeground);
		originalContent.setEditable(Boolean.FALSE);
		add(originalContent);
		message = "--------------------------------------------------------";
		message = message + "Log of processed content" + message;
		processedContent = new TextArea(message, 5, FIXED_COLUMN_SIZE);
		processedContent.setBackground(logBackground);
		processedContent.setForeground(logForeground);
		processedContent.setEditable(Boolean.FALSE);
		add(processedContent);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		delimiterStr = delimiter.getText();
		separatorStr = separator.getText();
		separatorStr = separatorStr.equals("\\n")?"\n":separatorStr;
		tokenStartsWithStr = tokenStartsWith.getText();
		tokenEndsWithStr = tokenEndsWith.getText();
		finalStartsWithStr = finalStartsWith.getText();
		finalEndsWithStr = finalEndsWith.getText();
		replaceThisStr = replaceThis.getText();
		replaceWithStr = replaceWith.getText();
		sortV = sort.getState();
		descV = desc.getState();
		elementTypeV = elementType.getSelectedItem();
	}

}