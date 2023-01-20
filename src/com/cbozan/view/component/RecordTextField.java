package com.cbozan.view.component;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class RecordTextField extends JTextField implements FocusListener{

	private static final long serialVersionUID = 904508252272394464L;
	
	private Color focusOnColor = Color.BLUE;
	private Color validFocusOffColor = Color.GREEN;
	private Color invalidFocusOffColor = Color.RED;
	private final Pattern pattern;
	
	public static final int NON_REQUIRED_TEXT = 1;
	public static final int REQUIRED_TEXT = 2;
	public static final int PHONE_NUMBER_TEXT = 3;
	// PHONE_NUMBER + NON REQURIRED = 4
	// PHONE_NUMBER + REQUIRED_TEXT = 5
	public static final int IBAN_NUMBER_TEXT = 6;
	// IbanNUmber + NONREQUIRED  = 7
	// IBANNUMBER + REQURIRED = 8
	public static final int DECIMAL_NUMBER_TEXT = 9;
	// DECIMALNUMBER + NONREQUIRED = 10
	// DECIMALNUMBER + REQUIRED = 11
	
	private final String requiredRegex = "^.+$";
	private final String nonRequiredRegex = "^.*$";
	private final String phoneNumberRegex = "^((((\\+90)?|(0)?)\\d{10})|())$";
	private final String requiredPhoneNumberRegex = "^((((\\+90)?|(0)?)\\d{10}))$";
	private final String ibanNumberRegex = "^((TR\\d{24})|())$";
	private final String requiredIbanNumberRegex = "^((TR\\d{24}))$";
	private final String decimalNumberRegex = "^((\\d+(\\.\\d{1,2})?)|())$";
	private final String requiredDecimalNumberRegex = "^(\\d+(\\.\\d{1,2})?)$";
	
	public RecordTextField(int regexNumber) {
		if(regexNumber <= 0 || regexNumber > 11) {
			this.pattern = Pattern.compile(nonRequiredRegex);
		} else if(regexNumber == NON_REQUIRED_TEXT) {
			this.pattern = Pattern.compile(nonRequiredRegex);
		} else if(regexNumber == REQUIRED_TEXT){
			this.pattern = Pattern.compile(requiredRegex);
		} else if(regexNumber == PHONE_NUMBER_TEXT || regexNumber == PHONE_NUMBER_TEXT + NON_REQUIRED_TEXT) {
			this.pattern = Pattern.compile(phoneNumberRegex);
		} else if(regexNumber == PHONE_NUMBER_TEXT + REQUIRED_TEXT){
			this.pattern = Pattern.compile(requiredPhoneNumberRegex);
		} else if(regexNumber == IBAN_NUMBER_TEXT || regexNumber == IBAN_NUMBER_TEXT + NON_REQUIRED_TEXT) {
			this.pattern = Pattern.compile(ibanNumberRegex);
		} else if(regexNumber == IBAN_NUMBER_TEXT + REQUIRED_TEXT) {
			this.pattern = Pattern.compile(requiredIbanNumberRegex);
		} else if(regexNumber == DECIMAL_NUMBER_TEXT || regexNumber == IBAN_NUMBER_TEXT + NON_REQUIRED_TEXT) {
			this.pattern = Pattern.compile(decimalNumberRegex);
		} else if(regexNumber == DECIMAL_NUMBER_TEXT + REQUIRED_TEXT) {
			this.pattern = Pattern.compile(requiredDecimalNumberRegex);
		} else {
			this.pattern = Pattern.compile(nonRequiredRegex);
		}
		
		DocumentFilter filter = new DocumentFilter() {
			public void insertString(DocumentFilter.FilterBypass fb, int offset,
		            String text, AttributeSet attr) throws BadLocationException {

		        fb.insertString(offset, text.toUpperCase(), attr);
		    }

		    public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
		            String text, AttributeSet attrs) throws BadLocationException {

		        fb.replace(offset, length, text.toUpperCase(), attrs);
		    }
		};
		
		((AbstractDocument)this.getDocument()).setDocumentFilter(filter);
		
		addFocusListener(this);
	}
	
	public RecordTextField(String regex) {
		this(Pattern.compile(regex));
	}
	
	public RecordTextField(Pattern pattern) {
		this.pattern = pattern;
		addFocusListener(this);
		
		DocumentFilter filter = new DocumentFilter() {
			public void insertString(DocumentFilter.FilterBypass fb, int offset,
		            String text, AttributeSet attr) throws BadLocationException {

		        fb.insertString(offset, text.toUpperCase(), attr);
		    }

		    public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
		            String text, AttributeSet attrs) throws BadLocationException {

		        fb.replace(offset, length, text.toUpperCase(), attrs);
		    }
		};
		
		((AbstractDocument)this.getDocument()).setDocumentFilter(filter);
		
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		this.setBorder(new LineBorder(getFocusOnColor()));
	}

	@Override
	public void focusLost(FocusEvent e) {
		
		if(pattern.matcher(this.getText().replaceAll("\\s+", "")).find()) {
			//System.out.print(pattern.matcher(this.getText().replaceAll("\\s+", "")) + " | ");
			//System.out.println(pattern.matcher(this.getText().replaceAll("\\s+", "")).find());
			setBorder(new LineBorder(getValidFocusOffColor()));
		} else {
			setBorder(new LineBorder(getInvalidFocusOffColor()));
		}
		
	}

	public Color getFocusOnColor() {
		return focusOnColor;
	}

	public void setFocusOnColor(Color focusOnColor) {
		this.focusOnColor = focusOnColor;
	}


	public Color getValidFocusOffColor() {
		return validFocusOffColor;
	}

	public void setValidFocusOffColor(Color validFocusOffColor) {
		this.validFocusOffColor = validFocusOffColor;
	}

	public Color getInvalidFocusOffColor() {
		return invalidFocusOffColor;
	}

	public void setInvalidFocusOffColor(Color invalidFocusOffColor) {
		this.invalidFocusOffColor = invalidFocusOffColor;
	}	

}
