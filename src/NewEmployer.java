import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewEmployer extends JPanel implements FocusListener, ActionListener{

	
	/**
	 * 
	 */
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
	 * width of the Textfield
	 */
	private final int TW = 150;
	
	/**
	 * height of the TextField
	 */
	private final int TH = 25;
	
	/**
	 * width of the label
	 */
	private final int LW = 95;
	
	/**
	 * height of the label
	 */
	private final int LH = 25;
	
	/**
	 * vertical space of the label
	 */
	private final int LVS = 40;
	
	/**
	 * horizontal space of the label
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
	
	private final int TEXT_EDGE = 30;
	
	private JLabel name_label, surname_label, business_label, phoneNumber_label, image_label, imageText_label;
	private JTextField name_text, surname_text, business_text, phoneNumber_text;
	
	private JButton save_button, paneSave_button, paneCancel_button;
	private JPanel saveButton_panel;
	
	private int currentTextArray = 0;
	private ArrayList<JTextField> textArray;
	
	public NewEmployer() {
		
		setLayout(null);
		
		image_label = new JLabel();
		image_label.setIcon(new ImageIcon("src\\icons\\new_employer.png"));
		image_label.setBounds(LX + 135, 40, 128, 130);
		add(image_label);
		
		imageText_label = new JLabel("NEW EMPLOYER");
		imageText_label.setBounds(425, 180, 150, 30);
		imageText_label.setFont(new Font("SansSerif", Font.PLAIN, 18));
		add(imageText_label);
		
		textArray = new ArrayList<JTextField>();
		
		name_label = new JLabel("Name");
		name_label.setBounds(LX, LY, LW, LH);
		add(name_label);
		
		name_text = new JTextField();
		name_text.setBounds(name_label.getX() + LW + LHS, name_label.getY(), TW, TH);
		name_text.addFocusListener(this);
		textArray.add(name_text);
		add(name_text);
		
		surname_label = new JLabel("Surname");
		surname_label.setBounds(name_label.getX(), name_label.getY() + LVS, LW, LH);
		add(surname_label);
		
		surname_text = new JTextField();
		surname_text.setBounds(surname_label.getX() + LW + LHS, surname_label.getY(), TW, TH);
		surname_text.addFocusListener(this);
		textArray.add(surname_text);
		add(surname_text);
		
		business_label = new JLabel("Business");
		business_label.setBounds(surname_label.getX(), surname_label.getY() + LVS, LW, LH);
		add(business_label);
		
		business_text = new JTextField();
		business_text.setBounds(business_label.getX() + LW + LHS, business_label.getY(), TW, TH);
		business_text.addFocusListener(this);
		textArray.add(business_text);
		add(business_text);
		
		phoneNumber_label = new JLabel("Phone Number");
		phoneNumber_label.setBounds(business_label.getX(), business_label.getY() + LVS, LW, LH);
		add(phoneNumber_label);
		
		phoneNumber_text = new JTextField();
		phoneNumber_text.setBounds(phoneNumber_label.getX() + LW + LHS, phoneNumber_label.getY(), TW, TH);
		phoneNumber_text.addFocusListener(this);
		textArray.add(phoneNumber_text);
		add(phoneNumber_text);
		
	}
	
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

}
