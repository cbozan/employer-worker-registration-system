package com.cbozan.view.component;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class TextArea extends JScrollPane implements FocusListener{

	private static final long serialVersionUID = 8546529312909157181L;
	JTextArea text;
	
	public TextArea() {
		text = new JTextArea();
		setViewportView(text);
		text.addFocusListener(this);
		this.addFocusListener(this);
	}
	
	public String getText(){
		return text.getText();
	}
	
	public void setText(String strText) {
		text.setText(strText);
	}
	
	public boolean isEditable() {
		return text.isEditable();
	}
	
	public void setEditable(boolean bool) {
		text.setEditable(bool);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		text.setBorder(new LineBorder(Color.BLUE));
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		text.setBorder(new LineBorder(Color.WHITE));
	}
	
	

}
