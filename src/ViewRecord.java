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
import java.awt.font.TextAttribute;
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
	private JButton button_button, detail_button, print_button;
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
		note_area.setBounds(note_label.getX(), note_label.getY() + note_label.getHeight(), note_label.getWidth(), 80);
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
						JOptionPane.showOptionDialog(null, "Already displayed", "CALM DOWN", 0, JOptionPane.WARNING_MESSAGE, null, new Object[] {"OK I CALMED DOWN"}, 0);
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
		employer_comboBox.setBounds(chooseEmployer_label.getX(), chooseEmployer_label.getY() + chooseEmployer_label.getHeight(), chooseEmployer_label.getWidth(), 24);
		employer_comboBox.setSelectedItem(null);
		add(employer_comboBox);
		
		chooseDate_label = new JLabel("Selection date");
		chooseDate_label.setForeground(new Color(76, 76, 76));
		chooseDate_label.setBounds(chooseDate_label.getX() + chooseDate_label.getWidth() + (TW / 4), chooseDate_label.getHeight(), chooseDate_label.getWidth(), 24);
		add(chooseDate_label);
		
		
		
		
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
