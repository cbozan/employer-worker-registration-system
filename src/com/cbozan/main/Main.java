package com.cbozan.main;

import java.awt.EventQueue;
import java.util.Locale;

public class Main {
	
	public static void main(String[] args) {
		
		Locale.setDefault(new Locale("tr", "TR"));
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				new Login();
				
			}
		});
			
	}
	
}
