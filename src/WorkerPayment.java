import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class WorkerPayment extends JPanel{

	JPanel bottom_panel;
	JTextField searchBox_text;
	JButton search_button, newSearch_button;
	JLabel searchBoxTitle_label, searchBoxInfoMessage_label;
	private final String[][] workerData_2array; // = DataBase.getWorkerData();
	private final String[][] employerData_2array; // = DataBase.getEmployerData();
	
	public WorkerPayment() {
		
		setLayout(null);
		
		workerData_2array = new String[][]{{"1", "WORKER-1"}, {"2", "WORKER-2"}, {"3", "WORKER-3"}, {"4", "WORKER-4"}, {"5", "WORKER-5"}, {"6", "WORKER-6"}};
		employerData_2array = new String[][]{{"1", "EMPLOYER-1"}, {"2", "EMPLOYER-2"}, {"3", "EMPLOYER-3"}, {"4", "EMPLOYER-4"}, {"5", "EMPLOYER-5"}, {"6", "EMPLOYER-6"}};
		
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
		searchBox_text.setHorizontalAlignment(SwingConstants.CENTER);
		searchBox_text.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		searchBox_text.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(searchBox_text.getText().length() > 1)
					updateBottomPanel(((JTextField)e.getSource()).getText());
				else {
					bottom_panel.setVisible(false);
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
		//search_button.setFocusPainted(false);
		search_button.setBackground(new Color(214, 214, 214));
		search_button.setFocusPainted(false);
		search_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchResultsGUI(searchBox_text.getText().toUpperCase());
			}
		});
		
		this.add(search_button);
		
		searchBoxInfoMessage_label = new JLabel("If to view all workers, leave the field blank and press the Search button.");
		searchBoxInfoMessage_label.setFont(new Font(Font.DIALOG, Font.ITALIC, 9));
		searchBoxInfoMessage_label.setForeground(new Color(0, 190, 0));
		searchBoxInfoMessage_label.setBounds(searchBox_text.getX(), searchBox_text.getY() + searchBox_text.getHeight(), searchBox_text.getWidth(), 10);
		searchBoxInfoMessage_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(searchBoxInfoMessage_label);
		
		
		searchBoxTitle_label = new JLabel("SEARCH WORKER");
		searchBoxTitle_label.setBounds(searchBox_text.getX(), searchBox_text.getY() - 50, searchBox_text.getWidth(), 24);
		searchBoxTitle_label.setFont(new Font("Tahoma", Font.BOLD + Font.ITALIC, 18));
		searchBoxTitle_label.setForeground(new Color(38, 38, 38));
		searchBoxTitle_label.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(searchBoxTitle_label);
		
		bottom_panel = new JPanel();
		bottom_panel.setBounds(searchBox_text.getX(), searchBox_text.getY() + searchBox_text.getHeight() + 11, searchBox_text.getWidth(), 0);
		bottom_panel.setVisible(true);
		bottom_panel.setLayout(null);
		this.add(bottom_panel);
		
		revalidate();
		repaint();
		
	}
	
	
	protected void updateBottomPanel(String text) {
		
		bottom_panel.removeAll(); 
		
		
		int position = 0;
		for(int i = 0; i < workerData_2array.length; i++) {
			
			if(workerData_2array[i][1].contains(text.toUpperCase())) {
				bottom_panel.add((JTextField)getBottomComponent(workerData_2array[i][1].toUpperCase(), position));
				bottom_panel.add((JButton)getBottomComponent("", position));
				position++;
			}
			
			
		}
		
		bottom_panel.setSize(502, (position > 10 ? 10 : position) * 29);
		bottom_panel.setVisible(true);
		
		this.revalidate();
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
					paymentGUI( ((JTextField)bottom_panel.getComponent(compIndex * 2)).getText() );
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
					paymentGUI( ((JTextField)e.getSource()).getText() );
				}
			});
			
			comp = tf;
		}
		
				
		return comp;
		
	}
	
	
	protected void searchResultsGUI(String searchText) {
		
		this.removeAll();
		this.add(newSearch_button);
		
		DefaultListModel<String> searchResultsListModel = new DefaultListModel<String>();
		for(int i = 0; i < workerData_2array.length; i++) {
			if(workerData_2array[i][1].contains(searchText))
				searchResultsListModel.addElement(workerData_2array[i][1]);
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
					paymentGUI( (String) ((JList)evt.getSource()).getSelectedValue());
				}
			}
		});
		
		MyScrollPane searchResultsScroll = new MyScrollPane(searchResultsList);
		searchResultsScroll.setBounds(0, 40, 944, 210);
		this.add(searchResultsScroll);
		
		JLabel scroll_bottom_info = new JLabel("Double click on the person you want to select");
		scroll_bottom_info.setBounds(searchResultsScroll.getX(), searchResultsScroll.getY() + 
				searchResultsScroll.getHeight(), searchResultsScroll.getWidth(), 12);
		scroll_bottom_info.setHorizontalAlignment(SwingConstants.CENTER);
		scroll_bottom_info.setFont(new Font(Font.DIALOG, Font.ITALIC, 11));
		scroll_bottom_info.setForeground(new Color(0, 180, 0));
		this.add(scroll_bottom_info);
		
		
		revalidate();
		repaint();
		
	}
	
	
	protected void paymentGUI(String worker) {
		
		this.removeAll();
		this.setBackground(Color.white);
		this.add(newSearch_button);
		
		JLabel titleWorker_label = new JLabel(worker);
		titleWorker_label.setFont(new Font(Font.SERIF, Font.BOLD, 21));
		titleWorker_label.setBounds(AdminPanel.W_FRAME / 2 + 4, 60, AdminPanel.W_FRAME / 2 - 4, 28);
		titleWorker_label.setHorizontalAlignment(SwingConstants.LEFT);
		titleWorker_label.setForeground(Color.BLUE);
		this.add(titleWorker_label);
		
		JLabel title_label = new JLabel("Payment form for");
		title_label.setFont(new Font(Font.SERIF, Font.BOLD + Font.ITALIC, 21));
		title_label.setBounds(0, 60, AdminPanel.W_FRAME / 2 - 4, 28);
		title_label.setHorizontalAlignment(SwingConstants.RIGHT);
		title_label.setForeground(new Color(152, 30, 58));
		this.add(title_label);
		
		JLabel employer_label = new JLabel("Payer");
		employer_label.setBounds(150, 200, 170, 26);
		employer_label.setForeground(new Color(53,68,78));
		this.add(employer_label);
		
		DefaultComboBoxModel<String> employer_model = new DefaultComboBoxModel<String>();
		for(int i = 0; i < employerData_2array.length; i++) {
			employer_model.addElement(employerData_2array[i][1]);
		}
		
		JComboBox<String> employer_comboBox = new JComboBox<String>(employer_model);
		employer_comboBox.setBounds(260, 200, 170, 26);
		((JLabel)employer_comboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);

		employer_comboBox.setSelectedItem(null);
		
		this.add(employer_comboBox);
		
		JLabel amount_label = new JLabel("Amount");
		amount_label.setBounds(150,  300, 120,  26);
		amount_label.setForeground(new Color(53,68,78));
		this.add(amount_label);
		
		JTextField amount_text = new JTextField();
		amount_text.setBounds(260, 300, 95, 26);
		amount_text.setHorizontalAlignment(SwingConstants.CENTER);
		amount_text.setBorder(new LineBorder(new Color(0, 160, 0), 1));
		this.add(amount_text);
		
		
		JLabel amountTitle_label = new JLabel("Amount of payment");
		amountTitle_label.setBounds(260, 270, 95, 30);
		amountTitle_label.setForeground(new Color(0, 160, 0));
		amountTitle_label.setFont(new Font("Tahoma", Font.PLAIN, 9));
		amountTitle_label.setHorizontalAlignment(SwingConstants.CENTER);
		amountTitle_label.setVerticalAlignment(SwingConstants.CENTER);
		this.add(amountTitle_label);
		
		JTextField amount2_text = new JTextField();
		amount2_text.setBounds(365, 300, 65, 26);
		amount2_text.setHorizontalAlignment(SwingConstants.CENTER);
		amount2_text.setBorder(new LineBorder(new Color(20, 20, 220), 1));
		amount2_text.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = amount2_text.getText();
				
				if(!text.equals("")) {
					
					try {
						int numberOfDays = Integer.parseInt(text);
						int wage = Integer.parseInt(/*AdminPanel.SETTINGS[0]*/"100") - Integer.parseInt("10"/*AdminPanel.SETTINGS[1]*/);
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
		
		JLabel amount2Title_label = new JLabel("Wage");
		amount2Title_label.setBounds(365, 270, 65, 30);
		amount2Title_label.setForeground(new Color(20, 20, 220));
		amount2Title_label.setFont(new Font("Tahoma", Font.PLAIN, 9));
		amount2Title_label.setHorizontalAlignment(SwingConstants.CENTER);
		amount2Title_label.setVerticalAlignment(SwingConstants.CENTER);
		this.add(amount2Title_label);
		
		
		String id = "1";//getWorkerData(worker, 1);
		
		String [][] tableData = {{"1", "Worker-1", "Employer-1", "320", "03.03.2022"},
				{"3", "Worker-3", "Employer-3", "230", "03.03.2022"}, 
				{"2", "Worker-2", "Employer-2", "302", "03.03.2022"},
				{"4", "Worker-4", "Employer-4", "50", "03.03.2022"},}; //DataBase.getWorkerPaymentData(id, "");
		String [] titleTable = {"ID", "Worker", "Employer", "Amount", "Date"};
		
		
		JTable bottomTable = new JTable(tableData, titleTable) { 
			DefaultTableCellRenderer render = new DefaultTableCellRenderer();
			{render.setHorizontalAlignment(SwingConstants.CENTER);}
			public TableCellRenderer getCellRenderer (int row, int col) {
				return render;
			}
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		bottomTable.setRowHeight(24);
		bottomTable.getColumnModel().getColumn(0).setPreferredWidth(5);
		bottomTable.getColumnModel().getColumn(1).setPreferredWidth(60);
		bottomTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		bottomTable.getColumnModel().getColumn(3).setPreferredWidth(22);
		bottomTable.getColumnModel().getColumn(4).setPreferredWidth(40);
		bottomTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		JScrollPane bottomTable_scroll = new JScrollPane(bottomTable);
		bottomTable_scroll.setBounds(500, 200, 400, 210);
		bottomTable_scroll.getViewport().setBackground(Color.white);
		this.add(bottomTable_scroll);
		
		JButton pay_button = new JButton("Pay");
		pay_button.setBounds(150, 380, 286, 30);
		pay_button.setFocusPainted(false);
		this.add(pay_button);
		
		
		pay_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!amount_text.getText().equals("") && !amount_text.getText().equals("0")) {
					boolean state = true;
					int amount;
					try {
						
						amount = Integer.parseInt(amount_text.getText());
						
						String employerId = "1";//getEmployer();
						String WorkerId = "1";//getWorker();
						
						if( true) {//DataBase.payment(workerId, employerId, ""+amount) ) {
							JOptionPane.showMessageDialog(WorkerPayment.this, "PAYMENT SUCCESSFUL", "PAYMENT RESULT", JOptionPane.INFORMATION_MESSAGE);

							paymentGUI(worker);
							
							
						}
						
						
					} catch(NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Enter only the number values \nin the amount field.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
					
				}
				
			}
		});
		
		revalidate();
		repaint();
		
	}

	@Override
	public String toString() {
		return "Worker Payment";
	}
	
	
}
