import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class NewWorker extends JPanel implements ActionListener, FocusListener{

	private static final long serialVersionUID = 1L;
	
	/**
	 * y position of label
	 */
	private final int LY = 250;
	
	/**
	 * x position of label
	 */
	private final int LX = 300;
	
	/**
	 * width of the text field
	 */
	private final int TW = 150;
	
	/**
	 * height of the text field
	 */
	private final int TH = 25;
	
	/**
	 * width of the label
	 */
	private final int LW = 85;
	
	/**
	 * height of the label;
	 */
	private final int LH = 25;
	
	/**
	 * vertical spacing of the label
	 */
	private final int LVS = 40;
	
	/**
	 * horizontal spacing of the label
	 */
	private final int LHS = 30;
	
	/**
	 * width of the button
	 */
	private final int BW = 80;
	
	/**
	 * height of the button
	 */
	private final int BH = 30;
	
	private JLabel name_label, surname_label, phoneNumber_label, image_label;
	private JTextField name_text, surname_text, phoneNumber_text;
	private JButton save_button;
	//private JPanel saveButtonBack_panel;
	
	
	public NewWorker() {
		
		setLayout(null);
		
		image_label = new JLabel(new ImageIcon("src\\icons\\new_worker.png"));
		image_label.setBounds(427, 80, 128, 128);
		add(image_label);
		
		name_label = new JLabel("Name");
		name_label.setBounds(LX, LY, LW, LH);
		System.out.println(LX + "\n" + LW + "\n" + AdminPanel.INSETS.left + "\n" + image_label.getX() + "\n" + (name_label.getX() + name_label.getWidth()));
		add(name_label);
		
		name_text = new JTextField();
		name_text.setBounds(LX + name_label.getWidth() + LHS, name_label.getY(), TW, TH);
		name_text.addFocusListener(this);
		name_text.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!name_text.getText().equals(""))
					surname_text.requestFocus();
			}
		});
		add(name_text);
		
		surname_label = new JLabel("Surname");
		surname_label.setBounds(LX, name_label.getY() + LVS, LW, LH);
		add(surname_label);
		
		surname_text = new JTextField();
		surname_text.setBounds(LX + surname_label.getWidth() + LHS, surname_label.getY(), TW, TH);
		surname_text.addFocusListener(this);
		surname_text.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!surname_text.getText().equals(""))
					phoneNumber_text.requestFocus();
			}
		});
		add(surname_text);
		
		phoneNumber_label = new JLabel("Phone Number");
		phoneNumber_label.setBounds(LX, surname_label.getY() + LVS, LW, LH);
		add(phoneNumber_label);
		
		phoneNumber_text = new JTextField();
		phoneNumber_text.setBounds(LX + phoneNumber_label.getWidth() + LHS, phoneNumber_label.getY(),TW, TH);
		phoneNumber_text.addFocusListener(this);
		phoneNumber_text.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save_button.doClick();
			}
		});
		add(phoneNumber_text);
		
		//saveButtonBack_panel = new JPanel();
		//saveButtonBack_panel.setLayout(null);
		//saveButtonBack_panel.setBounds(phoneNumber_text.getX() + ((TW - BW) / 2), phoneNumber_text.getY() + LH + 20, BW, BH);
		//saveButtonBack_panel.setBackground(Color.CYAN);
		
		save_button = new JButton("SAVE");
		save_button.setBounds(0, 0, BW, BH);
		save_button.addActionListener(this);
		save_button.setBounds(phoneNumber_text.getX() + ((TW - BW) / 2), phoneNumber_text.getY() + LH + 20, BW, BH);
		save_button.setFocusPainted(false);
		
		//save_button.setContentAreaFilled(false);
		//save_button.setOpaque(false);
		//save_button.setBorder(new LineBorder(Color.GREEN, 2));

		//saveButtonBack_panel.add(save_button);
		
		add(save_button);
		
	}


	@Override
	public void focusGained(FocusEvent e) {

		((JTextField) e.getSource()).setBorder(new LineBorder(Color.blue));
		
	}


	@Override
	public void focusLost(FocusEvent e) {
		
		Color color = Color.green;
		if( ((JTextField) e.getSource()).getText().replaceAll("\\s+", "").equals("")) {
			
			if( e.getSource() == name_text || e.getSource() == surname_text)
				color = Color.red;
			else
				color = Color.white;
			
		} else {
			if(e.getSource() == phoneNumber_text) {
				if( phoneNumberControl(phoneNumber_text.getText())) {
					
					color = Color.white;
					
				} else {
					color = Color.red;
				}
			} 
		}
			
		((JTextField) e.getSource()).setBorder(new LineBorder(color));
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(name_text.getText().replaceAll("\\s+", "").equals("") 
				|| surname_text.getText().replaceAll("\\s+", "").equals("") 
				|| !phoneNumberControl(phoneNumber_text.getText())) {
			
			JOptionPane.showMessageDialog(this, "Please enter the information correctly", "ERROR", JOptionPane.ERROR_MESSAGE);
			
		} else {
			
			JLabel nameLabel, surnameLabel, phoneNumberLabel;
			JTextField nameText, surnameText, phoneNumberText;
			
			nameLabel = new JLabel(name_label.getText());
			
			surnameLabel = new JLabel(surname_label.getText());
			
			phoneNumberLabel = new JLabel(phoneNumber_label.getText());
			
			nameText = new JTextField(name_text.getText().toUpperCase());
			nameText.setSize(80, 24);
			nameText.setEditable(false);
			
			surnameText = new JTextField(surname_text.getText().toUpperCase());
			surnameText.setSize(80, 24);
			surnameText.setEditable(false);
			
			phoneNumberText = new JTextField(phoneNumber_text.getText().toUpperCase());
			phoneNumberText.setSize(80, 24);
			phoneNumberText.setEditable(false);
			
			
			Object [] approvalForm = new Object[] {
					
					nameLabel,
					nameText,
					surnameLabel,
					surnameText,
					phoneNumberLabel,
					phoneNumberText
					
			};
			
			int result = JOptionPane.showOptionDialog(this, approvalForm, "Save Form", JOptionPane.OK_OPTION, -1, new ImageIcon("src//icons//accounting_icon_1_32.png"), new Object[] {"SAVE",  "CANCEL"}, "CANCEL");
			System.out.print(result);
			//SAVE = 0, CANCEL = 1
			if(result == 0) {
				
				if( DataBase.addWorker(nameText.getText().toUpperCase(), surnameText.getText().toUpperCase(), 
						phoneNumberText.getText().toUpperCase()) ) {
					
					JOptionPane.showMessageDialog(this, "SAVED");
					clearPanel();
					
				} else {
					
					JOptionPane.showMessageDialog(this, "NOT SAVED", "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
					
				}
			}
		}
	}


	private boolean phoneNumberControl(String phoneNumber) {
		
		if(phoneNumber.equals("")) {
			return true;
		} else if( (phoneNumber.charAt(0) == '0' && phoneNumber.length() == 11) 
				|| (phoneNumber.charAt(0) != '0' && phoneNumber.length() == 10) 
				|| (phoneNumber.charAt(0) == '+' && phoneNumber.length() == 13)) {
			
			return true;
			
		}
		
		return false;
		
	}
	
	
	private void clearPanel() {
		
		name_text.setText("");
		surname_text.setText("");
		phoneNumber_text.setText("");
		
		name_text.setBorder(new LineBorder(Color.white));
		surname_text.setBorder(new LineBorder(Color.white));
		phoneNumber_text.setBorder(new LineBorder(Color.white));
		
		
	}
	

	@Override
	public String toString() {
		return "New Worker";
	}
	
}
