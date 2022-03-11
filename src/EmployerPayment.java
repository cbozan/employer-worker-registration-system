import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class EmployerPayment extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	JButton newSearch_button, search_button;
	JTextField searchBox_text;
	JPanel searchBottom_panel;
	JLabel searchBoxInfoMessage_label, searchBoxTitle_label;
	private String[][] employers;
	
	public EmployerPayment() {
		setLayout(null);
		
		employers = getData("employer", "all");
		
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
		search_button.setBounds(searchBox_text.getX() + searchBox_text.getWidth() + 1, searchBox_text.getY(), 80, 30);
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
	
	
	void viewGUI(String employer) {
		
		this.removeAll();
		this.setBackground(Color.white);
		this.add(newSearch_button);
		
		JLabel title_label = new JLabel("Payment form for " + employer);
		title_label.setFont(new Font(Font.SERIF, Font.BOLD + Font.ITALIC, 21));
		title_label.setBounds(0, 60, this.getWidth(), 28);
		title_label.setHorizontalAlignment(SwingConstants.CENTER);
		title_label.setForeground(new Color(152, 30, 58));
		this.add(title_label);
		
		JTextField amount_text = new JTextField();
		amount_text.setBounds(180, 270, 130, 26);
		amount_text.setHorizontalAlignment(SwingConstants.CENTER);
		amount_text.setBorder(new LineBorder(new Color(0, 160, 0), 1));
		this.add(amount_text);
		
		JLabel amountTitle_label = new JLabel("amount of payment");
		amountTitle_label.setBounds(180, 240, 130, 30);
		amountTitle_label.setForeground(new Color(0, 160, 0));
		amountTitle_label.setFont(new Font("Tahoma", Font.PLAIN, 9));
		amountTitle_label.setHorizontalAlignment(SwingConstants.CENTER);
		amountTitle_label.setVerticalAlignment(SwingConstants.CENTER);
		this.add(amountTitle_label);
		
		JTextField amount2_text = new JTextField();
		amount2_text.setBounds(341, 270, 65, 26);
		amount2_text.setHorizontalAlignment(SwingConstants.CENTER);
		amount2_text.setBorder(new LineBorder(new Color(20, 20, 220), 1));
		amount2_text.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = amount2_text.getText();
				if(!text.equals("")) {
					
					try {
						int numberOfDays = Integer.parseInt(text);
						int wage = 100;//Integer.parseInt(getSettingsWage());
						amount_text.setText("" + (wage * numberOfDays));
					} catch (NumberFormatException nfe) {
						amount_text.setText("a lot of money");
					}
					
				} else {
					amount_text.setText("" + 0);
				}
			}
		});
		this.add(amount2_text);
		
		JLabel amountTitle2_label = new JLabel("Wage");
		amountTitle2_label.setBounds(341, 240, 65, 30);
		amountTitle2_label.setForeground(new Color(20, 20, 220));
		amountTitle2_label.setFont(new Font("Tahoma", Font.PLAIN, 9));
		amountTitle2_label.setHorizontalAlignment(SwingConstants.CENTER);
		amountTitle2_label.setVerticalAlignment(SwingConstants.CENTER);
		this.add(amountTitle2_label);
		
		String id = nameConvertToId(employer);
		String [][] tableData_2array = idConvertName(getData("employer_payment", "WHERE employer_id="+id), 1);//{{"1", "EMPLOYER-1", "300", "02.03.2022"}, {"1", "EMPLOYER-2", "8300", "07.03.2022"}, {"3", "EMPLOYER-3", "500", "02.02.2022"},{"1", "EMPLOYER-4", "300", "18.11.2022"}, {"5", "EMPLOYER-5", "2348", "22.06.2022"}};//DataBase.getEmployerData(employerId);
		String [] titleTable_array = {"ID", "Employer", "Date", "Paid"};
		
		JTable bottom_table = new JTable(tableData_2array, titleTable_array) {
			DefaultTableCellRenderer render = new DefaultTableCellRenderer();
			{
				render.setHorizontalAlignment(SwingConstants.CENTER);
			}
			public TableCellRenderer getCellRenderer (int row, int col) {	
				return render;
			}
			public boolean isCellEditable(int row, int col) {
				return false;
			}
			
		};
		bottom_table.setRowHeight(24);
		
		bottom_table.getColumnModel().getColumn(0).setPreferredWidth(5);
		bottom_table.getColumnModel().getColumn(1).setPreferredWidth(80);
		bottom_table.getColumnModel().getColumn(2).setPreferredWidth(22);
		bottom_table.getColumnModel().getColumn(3).setPreferredWidth(40);
		bottom_table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		JScrollPane bottomTable_scroll = new JScrollPane(bottom_table);
		bottomTable_scroll.setBounds(500, 200, 400, 210);
		bottomTable_scroll.getViewport().setBackground(Color.white);
		
		this.add(bottomTable_scroll);
		
		JButton getPaid_button = new JButton("Get paid");
		getPaid_button.setBounds(150, 380, 286, 30);
		getPaid_button.setFocusPainted(false);
		getPaid_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!amount_text.getText().equals("") && !amount_text.getText().equals("0")) {
					boolean state = true;
					int amount;
					try {
						
						amount = Integer.parseInt(amount_text.getText());
						String employerId = nameConvertToId(employer);
						
						if(DataBase.employerPayment("employer_payment", employerId, amount) ) {
							JOptionPane.showMessageDialog(null, "PAYMENT SUCCESSFUL", "PAYMENT RESULT", JOptionPane.INFORMATION_MESSAGE);
							viewGUI(employer); // rebuild
						}
						
					} catch(NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Enter only the number values \nin the amount field.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		this.add(getPaid_button);
		
		revalidate();
		repaint();
		
	}
	
	
	protected void searchResultsGUI(String searchText) {
		
		this.removeAll();
		this.add(newSearch_button);
		
		DefaultListModel<String> searchResultsListModel = new DefaultListModel<String>();
		for(int i = 0; i < (employers == null ? 0 : employers.length); i++) {
			searchResultsListModel.addElement(employers[i][1] + " " + employers[i][2]);
		}
		
		String[] temp = new String[searchResultsListModel.getSize()];
		searchResultsListModel.copyInto(temp);
		Arrays.sort(temp);
		
		searchResultsListModel = new DefaultListModel<String>();
		for(int i = 0; i < temp.length; i++) {
			searchResultsListModel.addElement(temp[i]);
		}
		
		JList<String> searchResultsList = new JList<String>(searchResultsListModel);
		searchResultsList.setBackground(new Color(244, 244, 244));
		
		((DefaultListCellRenderer)searchResultsList.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		searchResultsList.setFixedCellHeight(32);
		searchResultsList.addMouseListener(new MouseAdapter() {
			@Override 
			public void mouseClicked(MouseEvent evt) {
				if( evt.getClickCount() == 2) {
					viewGUI( (String) ((JList)evt.getSource()).getSelectedValue());
				}
			}
		});
		
		MyScrollPane searchResultsScroll = new MyScrollPane(searchResultsList);
		searchResultsScroll.setBounds(0, 40, 944, 210);
		this.add(searchResultsScroll);
		
		JLabel scroll_bottom_info = new JLabel("Double click on the person you want to select");
		scroll_bottom_info.setBounds(searchResultsScroll.getX(), searchResultsScroll.getY() + 
				searchResultsScroll.getHeight(), searchResultsScroll.getWidth(), 12);
		scroll_bottom_info.setFont(new Font(Font.DIALOG, Font.ITALIC, 10));
		scroll_bottom_info.setForeground(new Color(0, 180, 0));
		
		this.add(scroll_bottom_info);
		
		revalidate();
		repaint();
		
	}
	
	
	protected void updateBottomPanel(String text) {
		
		searchBottom_panel.removeAll();
		
		int position = 0;
		for(int i = 0; i < (employers == null ? 0 : employers.length); i++) {
			
			if((employers[i][1] + " " + employers[i][2]).contains(text.toUpperCase())) {
				searchBottom_panel.add((JTextField)getBottomComponent(employers[i][1].toUpperCase() + " " + employers[i][2], position));
				searchBottom_panel.add((JButton)getBottomComponent("", position));
				position++;
			}
			
			
		}
		
		searchBottom_panel.setSize(502, (position > 10 ? 10 : position) * 29);
		searchBottom_panel.setVisible(true);

		this.repaint();
		
	}
	
	
	private Component getBottomComponent(String string, int position) {
		Component comp = null;
		
		if(string.equals("")) {
			
			JButton button = new JButton("Choose");
			button.setBounds(421, position * 29, 80, 28);
			button.setName(string + position);
			button.setFocusPainted(false);
			button.setBackground(new Color(214, 214, 214));
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int compIndex = Integer.parseInt(((JButton)e.getSource()).getName());
					viewGUI( ((JTextField)searchBottom_panel.getComponent(compIndex * 2)).getText() );
				}
			});
			
			comp = button;
			
		} else {
			
			JTextField tf = new JTextField(string);
			tf.setHorizontalAlignment(SwingConstants.CENTER);
			tf.setEditable(false);
			tf.setBackground(new Color(238, 238, 238));
			tf.setBorder(new LineBorder(Color.white));
			tf.setBounds(0, position * 29, 420, 28);
			tf.addMouseListener(new MouseAdapter() {
				public void mouseExited(MouseEvent e) {
					tf.setBackground(new Color(238, 238, 238));
				}
				public void mouseEntered(MouseEvent e) {
					tf.setBackground(Color.WHITE);
				}
				public void mouseClicked(MouseEvent e) {
					viewGUI( ((JTextField)e.getSource()).getText() );
				}
			});
			
			comp = tf;
		}
		
				
		return comp;
	}
	
	
	public String[][] getData(String tableName, String operation){
		ArrayList<String[]> temp;
	
		if(operation.equals("all")) {
			temp = DataBase.getData(tableName);
			
		} else {
			
			temp = DataBase.getData(tableName, operation);
			
		}
		
		
		return listConvertToArray(temp);
		
	}
	
	public String[][] listConvertToArray(ArrayList<String[]> temp){
		String[][] data = new String[][] {};
		if(temp.size() != 0) {
			data = new String[temp.size()][temp.get(0).length];
			for(int i = 0; i < data.length; i++) {
				for(int j = 0; j < data[i].length; j++) {
					data[i][j] = temp.get(i)[j];
				}
			}
		}
		return data;
		
	}
	
	
	private String nameConvertToId(String name) {
		
		for(int i = 0; i < employers.length; i++) {
			
			if((employers[i][1] + " " + employers[i][2]).equals(name)){
				return employers[i][0];
			}
			
		}
		
		
		return null;
	}
	
	
	private String[][] idConvertName(String data[][], int indis) {
		
		String temp[][] = data;
		
		for(int i = 0; i < temp.length; i++) {
			temp[i][indis] = DataBase.getData("employer", "WHERE employer_id='" + Integer.parseInt(data[i][indis]) + "'").get(0)[1] + " " +
					DataBase.getData("employer", "WHERE employer_id='" + Integer.parseInt(data[i][indis]) + "'").get(0)[2]; 
			
		}
		
		return temp;
	}
	

	@Override
	public String toString() {
		return "Employer Payment";
	}
	
	
}
