import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.TextAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;

public class ViewRecord extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * x position of the table
	 */
	private final int TX = 5;
	
	/**
	 * y position of the table
	 */
	private final int TY = 5;
	
	/**
	 * width of the table
	 */
	private final int TW = 500;
	
	/**
	 * height of the table
	 */
	private final int TH = 323;
	
	/**
	 * space
	 */
	private final int SPACE = 80;
	
	private final String[] column_array = {"ID", "Employer", "Date", "Note", "Number of workers", "Wage"};
	private final String[] detailColumn_array = {"ID", "Worker", "Employer", "Date"};
	private String[][] data_2array;
	private String[][] employer_2array;
	private JButton filter_button, detail_button, print_button;
	private JCheckBox date_checkBox;
	private JScrollPane record_scroll, detailRecord_scroll;
	private JLabel chooseDate_label, chooseEmployer_label, chooseRecord_label, dateCheckBox_label;
	private JLabel button_label, dateRange_label, tableCount_label, note_label;
	private JTextField date_text, record_text, dateRangeStart_text, dateRangeEnd_text, detailNote_text;
	private JTextArea note_area;
	private JComboBox<String> employer_comboBox;
	private int tableSelectedRow = -1;
	
	
	
	public ViewRecord() {
		
		setLayout(null);
		
		detailRecord_scroll = new JScrollPane(createTable(new String[][] {}, detailColumn_array));
		detailRecord_scroll.setBounds(TX + TW + 20, TY, TW - 100, TH + 24);
		add(detailRecord_scroll);
		
		note_label = new JLabel("NOTE");
		note_label.setHorizontalAlignment(SwingConstants.CENTER);
		note_label.setBounds(detailRecord_scroll.getX(), TY + TH + 30, TW - 100, 24);
		// ----- FONT ----
		Font font = note_label.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		// ----------------
		note_label.setFont(font.deriveFont(attributes));
		add(note_label);
		
		note_area = new JTextArea();
		note_area.setBounds(note_label.getX(), note_label.getY() + note_label.getHeight(), note_label.getWidth(), 100);
		note_area.setBorder(new LineBorder(Color.CYAN));
		note_area.setEditable(false);
		add(note_area);
		
		data_2array = idConvertName(getData("employer_record", "all"), "employer", 1);
		
		record_scroll = new JScrollPane(createTable(data_2array, column_array));
		record_scroll.setBounds(TX, TY, TW, TH);
		add(record_scroll);
		
		detail_button = new JButton("View detail");
		detail_button.setBounds(record_scroll.getX(), record_scroll.getY() + TH, TW, 24);
		detail_button.setFocusPainted(false);
		detail_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if( ((JTable)record_scroll.getViewport().getComponent(0)).getSelectedRow() != -1) {
					
					if(tableSelectedRow != ((JTable)record_scroll.getViewport().getComponent(0)).getSelectedRow()) {
						
						tableSelectedRow = ((JTable)record_scroll.getViewport().getComponent(0)).getSelectedRow();
						
						String record_id = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 1);
						String employer_id = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 2);
						String date = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 0);
						String note = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 3);
						String numberOfWorkers = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 4);
						String wage = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 5);
						
						
						detailRecord_scroll.getViewport().removeAll();
						detailRecord_scroll.getViewport().add(createTable(idConvertName(idConvertName(getData("worker_record", 
								employer_id + ",'" + date + "'"), "worker", 1), "employer", 2), detailColumn_array));
						
						
						
						
						
						//data_2array = idConvertName(getData("employer_record", "all"), "employer", 1);

						note_area.setText(note);
						
						revalidate();
						repaint();
						
					} else {
						JOptionPane.showOptionDialog(null, "Already displayed", "CALM DOWN", 0, JOptionPane.WARNING_MESSAGE, 
								null, new Object[] {"OK I CALMED DOWN"}, 0);
					}
					
				} else {
					JOptionPane.showMessageDialog(null, "Please select a row from the table", "NOT SELECTED", JOptionPane.WARNING_MESSAGE);
				}
				
			}
		});
		add(detail_button);
		
		tableCount_label = new JLabel(data_2array.length + " displaying");
		tableCount_label.setHorizontalAlignment(SwingConstants.RIGHT);
		tableCount_label.setBounds(TX, detail_button.getY() + detail_button.getHeight(), TW, 24);
		tableCount_label.setForeground(new Color(0, 180, 0));
		add(tableCount_label);
		
		chooseEmployer_label = new JLabel("Selection employer");
		chooseEmployer_label.setForeground(new Color(76, 76, 76));
		chooseEmployer_label.setBounds(TX + TW/7, TY + TH + SPACE, TW / 4, 24);
		add(chooseEmployer_label);
		
		
		employer_2array = listConvertToArray(DataBase.getData("employer"));
		employer_comboBox = new JComboBox<String>(listConvertToArray(DataBase.getData("employer"), 1, 2));
		employer_comboBox.setBounds(chooseEmployer_label.getX(), chooseEmployer_label.getY() + 
				chooseEmployer_label.getHeight(), chooseEmployer_label.getWidth(), 24);
		employer_comboBox.setSelectedItem(null);
		add(employer_comboBox);
		/* */
		
		chooseDate_label = new JLabel("Selection date");
		chooseDate_label.setForeground(new Color(76, 76, 76));
		chooseDate_label.setBounds(chooseEmployer_label.getX() + chooseEmployer_label.getWidth() + (TW / 4), 
				chooseEmployer_label.getY(), chooseEmployer_label.getWidth(), 24);
		add(chooseDate_label);
		
		date_text = new JTextField();
		date_text.setBounds(chooseDate_label.getX(), chooseDate_label.getY() + chooseDate_label.getHeight(), 
				chooseEmployer_label.getWidth(), 24);
		add(date_text);
		
		date_checkBox = new JCheckBox();
		date_checkBox.setBounds(date_text.getX() - 3, date_text.getY() + date_text.getHeight(), 18, 24);
		date_checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					date_text.setText("");
				} else {
					date_text.setText(""+LocalDate.now());
				}
				
			}
		});
		add(date_checkBox);
		
		dateCheckBox_label = new JLabel("Today");
		dateCheckBox_label.setForeground(new Color(146, 146, 146));
		dateCheckBox_label.setBounds(date_text.getX() + date_checkBox.getWidth(), date_checkBox.getY(), 
				date_text.getWidth() - date_checkBox.getWidth(), 24);
		add(dateCheckBox_label);
		
		filter_button = new JButton("Fetch matching data");
		filter_button.setBounds(chooseEmployer_label.getX() + chooseEmployer_label.getWidth() / 2, 
				date_text.getY() + date_text.getHeight() + SPACE / 2, TW - date_text.getWidth() - 
				employer_comboBox.getWidth(), 24);
		filter_button.setFocusPainted(false);
		filter_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				tableSelectedRow = -1;
				boolean dateBool = false, employerBool = false;
				String chooseEmployer = (String)employer_comboBox.getSelectedItem();
				int chooseEmployerId = -1;
				String operation = "";
				
				if(chooseEmployer != null) {
					for(int i = 0; i < employer_2array.length; i++) {
						if(chooseEmployer.equals(employer_2array[i][1] + " " + employer_2array[i][2])) {
							chooseEmployerId = Integer.parseInt(employer_2array[i][0]);
							employerBool = true;
							break;
						}
					}
				}
				String chooseDate = "";
				
				try {
					if(!date_text.getText().equals("")) {
						new SimpleDateFormat("yyyy-MM-dd").parse(date_text.getText());
						chooseDate = date_text.getText();
					}
					if(!chooseDate.equals(""))
						dateBool = true;
						
				} catch(ParseException e1) {
					JOptionPane.showMessageDialog(ViewRecord.this, "incorrect date format", "Date", JOptionPane.ERROR_MESSAGE);
				}
				
				if(dateBool) {
					operation += "WHERE date='" + chooseDate + "'";
				}
				
				if(employerBool) {
					
					if(operation.equals("")) {
						operation += "WHERE employer_id=" + chooseEmployerId;
					} else {
						operation += " AND employer_id=" + chooseEmployerId;
					}
				}
				
				if(operation.equals("")) {
					data_2array = new String[][] {};
				} else {
					data_2array = listConvertToArray(DataBase.getData("employer_record", operation));
				}
				
				System.out.println(operation);
				
				record_scroll.getViewport().removeAll();
				record_scroll.getViewport().add(createTable(idConvertName(data_2array, "employer", 1), column_array));
				
				
				
				
				tableCount_label.setText(data_2array.length + " displaying");
				employer_comboBox.setSelectedItem(null);
				
			}
		});
		add(filter_button);
		
		button_label = new JLabel("One or more can be selected");
		button_label.setBounds(filter_button.getX(), filter_button.getY() + filter_button.getHeight() + 10, filter_button.getWidth(), filter_button.getHeight());
		button_label.setHorizontalAlignment(SwingConstants.CENTER);
		button_label.setForeground(Color.RED);
		add(button_label);
		
		print_button = new JButton("Print the displayed data");
		print_button.setFocusPainted(false);
		print_button.setBackground(new Color(0, 188, 212));
		print_button.setBounds(note_area.getX(), note_area.getY() + note_area.getHeight() + 25, TW - 100, 50);
		print_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// button function
			}
		});
		add(print_button);
		
		
	}
	
	private String[][] idConvertName(String[][] data, String tableName, int indis) {
		String[][] temp = data;
		
		for(int i = 0; i < data.length; i++) {
			
			temp[i][indis] = DataBase.getData(tableName, "WHERE " + tableName + "_id='" + Integer.parseInt(data[i][0]) + "'").get(0)[1] + " " +
					DataBase.getData(tableName, "WHERE " + tableName + "_id='" + Integer.parseInt(data[i][0]) + "'").get(0)[2]; 
			
		}
		
		return temp;
	}

	private Component createTable(String[][] tableData, String[] tableColumn) {
		
		JTable tab = new JTable(tableData, tableColumn) {
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
			DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
			DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
			
			{ // initializer block
				renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
				renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
			}
			
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				/*if(column == 1 || column == 2)
					return renderLeft;*/
				return renderCenter;
			}
		};
		
		tab.setRowHeight(25);
		tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		if(tableColumn.length == 6) {
			
			tab.getColumnModel().getColumn(0).setPreferredWidth(18);
			tab.getColumnModel().getColumn(1).setPreferredWidth(150);
			tab.getColumnModel().getColumn(2).setPreferredWidth(80);
			tab.getColumnModel().getColumn(3).setPreferredWidth(30);
			tab.getColumnModel().getColumn(4).setPreferredWidth(100);
			tab.getColumnModel().getColumn(5).setPreferredWidth(25);
			
		} else if(tableColumn.length == 4) {
			
			tab.getColumnModel().getColumn(0).setPreferredWidth(18);
			tab.getColumnModel().getColumn(0).setPreferredWidth(150);
			tab.getColumnModel().getColumn(0).setPreferredWidth(60);
			tab.getColumnModel().getColumn(0).setPreferredWidth(30);
			
		}
		
		
		return tab;
		
	}
	
	public JTable setColumnWidth(JTable table, int ...column) {
		
		for(int i = 0; i < table.getColumnCount() && i < column.length; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(column[i]);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		return table;
	}
	
	public String[][] getData(String tableName, String operation){
		ArrayList<String[]> temp;
	
		if(operation.equals("all")) {
			temp = DataBase.getData(tableName);
			
		} else {
			
			StringTokenizer st = new StringTokenizer(operation, ",");
			
			operation = "WHERE";
			operation += " date='" + st.nextToken()+"'";
			operation += " AND employer_id=" + st.nextToken();
			
			
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
	
	public String[] listConvertToArray(ArrayList<String[]> temp, int...column) {
		
		String[][] data = listConvertToArray(temp);
		String[] array = new String[data.length];
		
		if(data[0].length < Arrays.stream(column).max().getAsInt()) {
			return null;
		}

		for(int i = 0; i < data.length; i++) {
			array[i] = "";
			for(int j = 0; j < column.length; j++) {
				array[i] += data[i][column[j]] + " ";
			}
			array[i] = array[i].substring(0, array[i].length() - 1);
			
		}
		
		return array;
		
	}

	@Override
	public String toString() {
		return "View Record";
	}

	
}
