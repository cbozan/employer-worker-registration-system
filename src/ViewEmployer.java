import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class ViewEmployer extends JPanel{
	
	JButton newSearch_button, search_button;
	JTextField searchBox_text;
	JPanel searchBottom_panel;
	JLabel searchBoxInfoMessage_label, searchBoxTitle_label;
	private /*final*/ String[][] employers; // DataBase
	
	public ViewEmployer() {
		setLayout(null);
		
		newSearch_button = new JButton("New search");
		newSearch_button.setFocusPainted(false);
		newSearch_button.setBackground(new Color(255, 255, 255));
		newSearch_button.setBounds(10, 535, 150, 30);
		newSearch_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI();
			}
		});
		
		GUI();
	}
	
	void GUI() {
		
		this.removeAll();
		
		searchBox_text = new JTextField();
		searchBox_text.setBounds(250, 150, 420, 30);
		searchBox_text.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		searchBox_text.setHorizontalAlignment(SwingConstants.CENTER);
		searchBox_text.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(searchBox_text.getText().length() > 0) {
					updateBottomPanel(((JTextField)e.getSource()).getText());
				} else {
					searchBottom_panel.setVisible(false);
				}
				
			}

			
		});
		searchBox_text.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				search_button.doClick();
				
			}
		});
		this.add(searchBox_text);
		
		search_button = new JButton("Search");
		search_button.setBounds(searchBox_text.getX() + searchBox_text.getWidth() + 1, searchBox_text.getY(), 60, 30);
		search_button.setBackground(new Color(214, 214, 214));
		search_button.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				searchResultsGUI(searchBox_text.getText().toUpperCase());
				
			}
		});
		this.add(search_button);
		
		searchBoxInfoMessage_label = new JLabel("If to view all employers, leave the field blank and press the Search button.");
		searchBoxInfoMessage_label.setFont(new Font(Font.DIALOG, Font.ITALIC, 9));
		searchBoxInfoMessage_label.setForeground(new Color(0, 190, 0));
		searchBoxInfoMessage_label.setBounds(searchBox_text.getX(), searchBox_text.getY() + searchBox_text.getHeight(), searchBox_text.getWidth(), 10);
		searchBoxInfoMessage_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(searchBoxInfoMessage_label);
		
		searchBoxTitle_label = new JLabel("SEARCH EMPLOYER");
		searchBoxTitle_label.setBounds(searchBox_text.getX(), searchBox_text.getY() - 50, searchBox_text.getWidth(), 24);
		searchBoxTitle_label.setFont(new Font("Tahoma", Font.BOLD + Font.ITALIC, 18));
		searchBoxTitle_label.setForeground(new Color(38, 38, 38));
		searchBoxTitle_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(searchBoxTitle_label);
		
		searchBottom_panel = new JPanel();
		searchBottom_panel.setBounds(searchBox_text.getX(), searchBox_text.getY() + searchBox_text.getHeight() + 11, searchBox_text.getWidth(), 0);
		searchBottom_panel.setVisible(true);
		searchBottom_panel.setLayout(null);
		this.add(searchBottom_panel);
		
		revalidate();
		repaint();
	}
	
	protected void searchResultsGUI(String searchText) {
		
		//
		
	}
	
	
	protected void updateBottomPanel(String text) {
		
		// 
		
	}
	

}
