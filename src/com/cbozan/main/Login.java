package com.cbozan.main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.cbozan.dao.LoginDAO; 

public class Login extends JFrame{

	public static final int H_FRAME = 360;
	public static final int W_FRAME = 540;
	private JPanel contentPane;
	private JButton button_login;
	private JLabel label_username, label_password, label_icon, label_errorText;
	private JTextField textField_username;
	private JPasswordField passwordField_password;
	private Insets insets;
	String errorText = "ErrorText";
	
	
	public Login() {
		
		super("Login");
		setIconImage(Toolkit.getDefaultToolkit().getImage("src\\icon\\Login_user_24.png"));
		//setResizable(false);
		setLayout(null);
		setSize(W_FRAME, H_FRAME);
		setLocationRelativeTo(null);
		setLocation(getX() - 80, getY() - 80);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		
		
		insets = this.getInsets();
		
		
		GUI();
		
	}
	
	private void GUI() {
		
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBounds(insets.left, insets.top, W_FRAME - insets.left - insets.right, 
				H_FRAME - insets.bottom - insets.top);
	
		label_username = new JLabel("Username");
		label_username.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_username.setBounds(120, 140, 70, 20);
		contentPane.add(label_username);
		
		label_password = new JLabel("Password");
		label_password.setFont(label_username.getFont());
		label_password.setBounds(label_username.getX(), label_username.getY() + 40, 
				label_username.getWidth(), label_username.getHeight());
		contentPane.add(label_password);
		
		textField_username = new JTextField();
		textField_username.setBounds(label_username.getX() + label_username.getWidth() + 30, 
				label_username.getY(), 120, label_username.getHeight());
		textField_username.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				passwordField_password.requestFocus();
			}
		});
		contentPane.add(textField_username);
		
		passwordField_password = new JPasswordField();
		passwordField_password.setBounds(textField_username.getX(), label_password.getY(), 
				120, label_password.getHeight());
		passwordField_password.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button_login.doClick();
			}
		});
		contentPane.add(passwordField_password);
		
		button_login = new JButton("Login");
		button_login.setBounds(textField_username.getX() + 20, label_username.getY() + 80, 80, 22);
		button_login.setFocusPainted(false);
		button_login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		
				if(textField_username.getText().equals("") || String.valueOf(passwordField_password.getPassword()).equals("")) {
					
					label_errorText.setText(errorText);
				
				} else {
					
					label_errorText.setText("");
					LoginDAO loginDAO = new LoginDAO();
					if(loginDAO.verifyLogin(textField_username.getText(), 
							String.valueOf(passwordField_password.getPassword()))) {
						JOptionPane.showMessageDialog(contentPane, "Login successful. Welcome", "Login", 
								JOptionPane.INFORMATION_MESSAGE);
						
						EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								Login.this.dispose();
								//new AdminPanel();
							}
						});
						
					} else {
						label_errorText.setText(errorText);
					}
					
				}
				
			}
		});
		contentPane.add(button_login);
		
		label_icon = new JLabel(new ImageIcon("src\\icon\\Login_user_72.png"));
		label_icon.setBounds(textField_username.getX() + 20, textField_username.getY() - 100, 72, 72);
		contentPane.add(label_icon);
		
		label_errorText = new JLabel();
		label_errorText.setForeground(Color.RED);
		label_errorText.setBounds(button_login.getX() - 45, button_login.getY() + 30, 
				170, 30);
		label_errorText.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 11));
		contentPane.add(label_errorText);
		
		setContentPane(contentPane);
		
	}
	
	
}
