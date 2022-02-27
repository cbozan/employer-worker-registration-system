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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.TextAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Map;

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
	
	private final String[] column_array = {"ID", "Name", "Surname", "Number of workers", "Date", "Note"};
	private final String[] detailColumn_array = {"ID", "Name", "Surname", "Date"};
	private String[][] data_2array;
	private JButton filter_button, detail_button, print_button;
	private JCheckBox date_checkBox;
	private JScrollPane record_scroll, detailRecord_scroll;
	private JLabel chooseDate_label, chooseEmployer_label, chooseRecord_label, dateCheckBox_label;
	private JLabel button_label, dateRange_label, tableCount_label, note_label;
	private JTextField date_text, record_text, dateRangeStart_text, dateRangeEnd_text, detailNote_text;
	private JTextArea note_area;
	private JComboBox<String> employer_comboBox;
	private int tableSelectedRow = -1;
	
	private String employerName_s = null, employerSurname_s = null, recordId_s = null;
	
	
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
		
		data_2array = getData();
		
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
						
						employerName_s = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 1);
						employerSurname_s = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 2);
						recordId_s = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 0);
						
						String numberOfWorkers = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 3);
						String date = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 4);
						String note = (String) ((JTable)record_scroll.getViewport().getComponent(0)).getValueAt(tableSelectedRow, 5);
						
						detailRecord_scroll.getViewport().add(createTable(/*DataBase.get(...*/null, detailColumn_array));
						
						note_area.setText(note);
						
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
		
		employer_comboBox = new JComboBox<String>(/* DataBase.get(..)*/);
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
				boolean dateBool = false;
				String chooseEmployer = (String)employer_comboBox.getSelectedItem();
				String chooseDate = null;
				
				try {
					if(!date_text.getText().equals("")) {
						new SimpleDateFormat("yyyy-MM-dd").parse(date_text.getText());
						chooseDate = date_text.getText();
					}
					dateBool = true;
				} catch(ParseException e1) {
					JOptionPane.showMessageDialog(ViewRecord.this, "incorrect date format", "Date", JOptionPane.ERROR_MESSAGE);
				}
				
				if(dateBool) {
					
					//data_2array = DataBase.get(chooseEmployer, chooseDate);
					record_scroll.getViewport().add(createTable(data_2array, column_array));
					//((JTable)record_scroll.getViewport().getComponent(0)).getColumnModel().getColumn(5).setPreferredWidth(100);
					//((JTable)record_scroll.getViewport().getComponent(0)).setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
				}
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
				if(column == 1 || column == 2)
					return renderLeft;
				return renderCenter;
			}
		};
		
		tab.setRowHeight(25);
		tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		return tab;
		
	}
	
	private String[][] getData(){
		return new String[][] {{"1", "col0", "col1", "col2", "col3", "col4"}, 
			{"1", "col0", "col1", "col2", "col3", "col4"}, 
			{"1", "col0", "col1", "col2", "col3", "col4"}}; // DataBase.get(...);
	}
	
	
}
