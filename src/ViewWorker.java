import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class ViewWorker extends JPanel implements MouseListener{

	/**
	 * x position of the search box
	 */
	private final int SBX = 250;
	
	/**
	 * y position of the search box
	 */
	private final int SBY = 150;
	
	/**
	 * width of the search box
	 */
	private final int SBW = 420;
	
	/**
	 * height of the search box
	 */
	private final int SBH = 30;
	
	/**
	 * default font
	 */
	private final Font FONT = new Font("SansSerif", Font.BOLD, 18);
	
	/**
	 * column names of the table
	 */
	private final String[] column_array = {"ID", "Name", "Surname", "Employer", "Date"};
	
	private JPanel searchBottom_panel;
	private JScrollPane searchPressed_scroll;
	private JTextField searchBox_text;
	private JLabel resultSearch_label, searchBoxTitle_label, searchBoxInfoMessage_label;
	private JButton search_button, back_button, choose_button;
	private DefaultListModel<String> worker_model = new DefaultListModel<String>();
	private String[][] firstRecordData_2array, filterData_2array;
	
	
	public ViewWorker() {
		
		setLayout(null);
		
		//worker_model = DataBase.get();
		worker_model.addElement("Worker_model_1");
		worker_model.addElement("Worker_model_2");
		worker_model.addElement("Worker_model_3");
		worker_model.addElement("Worker_model_4");
		
		searchBox_text = new JTextField();
		searchBox_text.setHorizontalAlignment(SwingConstants.CENTER);
		searchBox_text.setForeground(Color.GRAY);
		searchBox_text.setFont(FONT);
		searchBox_text.setBounds(SBX, SBY, SBW, SBH);
		searchBox_text.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				search_button.doClick();
				
			}
		});
		add(searchBox_text);
		
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
		
		searchBottom_panel = new JPanel();
		add(searchBottom_panel);
		
		search_button = new JButton("Search");
		search_button.setBounds(SBX + SBW, SBY, 80, SBH - 1);
		search_button.setContentAreaFilled(false);
		search_button.setFocusPainted(false);
		search_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!searchBox_text.getText().equals("")) {
					
					search_button.setVisible(false);
					searchBox_text.setVisible(false);
					
					resultSearch_label = new JLabel("Search results");
					resultSearch_label.setBounds(SBX, (SBY - (2 * SBH)), SBW, SBH);
					resultSearch_label.setHorizontalAlignment(SwingConstants.CENTER);
					resultSearch_label.setFont(new Font("Tahoma", Font.BOLD + FONT.ITALIC, 18));
					
					DefaultListModel<String> model = new DefaultListModel<String>();
					
					for(int i = 0; i < searchBottom_panel.countComponents(); i+= 2) {
						model.addElement( ((JTextField)searchBottom_panel.getComponent(i)).getText());
					}
					
					ViewWorker.this.remove(searchBottom_panel);
					
					JList<String> list = new JList<String>(model);
					list.setFixedCellHeight(50);
					((DefaultListCellRenderer)list.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
					
					if(model.getSize() == 0) {
						model.addElement("DATA NOT FOUND");
						list.setModel(model);
						list.setSelectionModel(new NoSelectionModel());
					}
					ViewWorker.this.add(back_button);
					
					searchPressed_scroll = new JScrollPane(list);
					searchPressed_scroll.setBounds(SBX, SBY, SBW, 300);
					searchPressed_scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					searchPressed_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					ViewWorker.this.add(searchPressed_scroll);
					
					resultSearch_label.setText(resultSearch_label.getText() + " " + model.getSize() + " results found.");
					ViewWorker.this.add(resultSearch_label);
					
					choose_button = new JButton("Show");
					choose_button.setBounds(searchPressed_scroll.getX() + searchPressed_scroll.getWidth() - 121, 
							searchPressed_scroll.getY() + searchPressed_scroll.getHeight() + 20, 120, 40);
					choose_button.setContentAreaFilled(true);
					choose_button.setFocusPainted(false);
					choose_button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(list.getSelectedValue() != null) {
								preview(list.getSelectedValue());
							}
						}
					});
					ViewWorker.this.add(choose_button);
					
					revalidate();
					repaint();
				} else {
					//
				}
				
			}
		});
		add(search_button);
		
		
		searchBox_text.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
				
				ViewWorker.this.remove(searchBottom_panel);
				if(!searchBox_text.getText().equals("")) {
					addSearchBottomPanel(searchBox_text.getText());
				}
				
				revalidate();
				repaint();
				
			}
		});
		
		back_button = new JButton("Search again");
		back_button.setBounds(SBX, SBY + 320, 120, 40);
		back_button.setContentAreaFilled(true);
		back_button.setFocusPainted(false);
		back_button.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ViewWorker.this.remove(back_button);
				ViewWorker.this.remove(searchPressed_scroll);
				ViewWorker.this.remove(resultSearch_label);
				ViewWorker.this.remove(choose_button);
				searchBox_text.setText("");
				searchBox_text.setVisible(true);
				search_button.setVisible(true);
				
				revalidate();
				repaint();
				
			}
		});
		
		
	}
	
	public void addSearchBottomPanel(String text) {
		searchBottom_panel = new JPanel();
		searchBottom_panel.setLayout(null);
		
		JTextField tempField;
		JButton tempButton;
		int tCount = 0;
		for(int i = 0; i < worker_model.getSize(); i++) {
			if(worker_model.getElementAt(i).toUpperCase().contains(text.toUpperCase())) {
				tempField = new JTextField(worker_model.getElementAt(i));
				tempField.setEditable(false);
				tempField.setHorizontalAlignment(JTextField.CENTER);
				tempField.setFont(FONT);
				tempField.setBackground(new Color(238, 238, 238));
				tempField.setBounds(0, (tCount * (SBH + 1)), SBW, SBH);
				tempField.addMouseListener(this);
				
				tempButton = new JButton("Choose");
				tempButton.setName(""+((tCount * 2)));
				tempButton.setContentAreaFilled(true);
				tempButton.setFocusPainted(false);
				tempButton.setBounds(SBW, tempField.getY(), search_button.getWidth(), SBH - 1);
				tempButton.setForeground(Color.BLUE);
				tempButton.setBackground(new Color(238, 238, 238));
				tempButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println(Integer.parseInt(((JButton)e.getSource()).getName()));
						System.out.println();
						preview(((JTextField)searchBottom_panel.getComponent(Integer.parseInt(((JButton)e.getSource()).getName()))).getText());
						
					}
				});
				searchBottom_panel.add(tempField);
				searchBottom_panel.add(tempButton);
				tCount++;
			}
		}
		
		
		if(tCount > 0) {
			tCount = (tCount < 5 ? tCount : 5);
			searchBottom_panel.setBounds(SBX, SBY + SBH + 11, SBW + search_button.getWidth(), tCount * SBH + tCount);
			searchBottom_panel.setBackground(Color.WHITE);
		}
//		
		
		ViewWorker.this.add(searchBottom_panel);
	}
	
	
	public void preview(String text) {
		
		// connect database getData(text)
		
		ViewWorker.this.removeAll();
		
		firstRecordData_2array = new String[][] {{"1", "col1", "col2", "col3", "col4"}, {"2", "col1", "col2", "col3", "col4"}};//DataBase.getRecordData(text);
		filterData_2array = firstRecordData_2array;
		
		JScrollPane table_scroll = new JScrollPane(createTable(firstRecordData_2array, column_array));
		table_scroll.setBounds(SBX + 170, 10, 500, 400);
		ViewWorker.this.add(table_scroll);
		
		JLabel filter_label = new JLabel("Filter");
		filter_label.setHorizontalAlignment(SwingConstants.CENTER);
		filter_label.setBounds(0, 50, table_scroll.getX(), 24);
		filter_label.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 24));
		ViewWorker.this.add(filter_label);
		
		JLabel employer_label = new JLabel("Employer");
		employer_label.setBounds(140, filter_label.getY() + 100, 150, 24);
		ViewWorker.this.add(employer_label);
		
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();//DataBase.getEmployer();
		comboBoxModel.addElement("Employer - 1");
		comboBoxModel.addElement("Employer - 2");
		comboBoxModel.addElement("Employer - 3");
		comboBoxModel.addElement("Employer - 4");
		
		JComboBox<String> comboBox = new JComboBox<String>(comboBoxModel);
		comboBox.setSelectedItem(null);
		comboBox.setBounds(employer_label.getX(), employer_label.getY() + employer_label.getHeight(), employer_label.getWidth(), employer_label.getHeight());
		ViewWorker.this.add(comboBox);
		
		JLabel date_label = new JLabel("Date");
		date_label.setBounds(comboBox.getX(), comboBox.getY() + 80, comboBox.getWidth(), comboBox.getHeight());
		ViewWorker.this.add(date_label);
		
		JTextField date_text = new JTextField();
		date_text.setBounds(date_label.getX(), date_label.getY() + date_label.getHeight(),
				date_label.getWidth(), date_label.getHeight());
		ViewWorker.this.add(date_text);
		
		JCheckBox sampleDate_checkBox = new JCheckBox();
		sampleDate_checkBox.setBounds(date_text.getX() - 3, date_text.getY() + date_text.getHeight(), 14 + 3, 14);
		sampleDate_checkBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					date_text.setText("");
				} else {
					date_text.setText(""+LocalDate.now());
				}
				
			}
		});
		ViewWorker.this.add(sampleDate_checkBox);
		
		JLabel sampleDate_label = new JLabel("Sample date");
		sampleDate_label.setBounds(sampleDate_checkBox.getX() + sampleDate_checkBox.getWidth() + 10, sampleDate_checkBox.getY(), 
				date_text.getWidth() - sampleDate_checkBox.getWidth(), sampleDate_checkBox.getHeight());
		sampleDate_label.setForeground(new Color(122, 122, 122));
		ViewWorker.this.add(sampleDate_label);
		
		JButton apply_button = new JButton("Apply");
		apply_button.setBounds(date_label.getX(), date_label.getY() + 200, date_label.getWidth(), 40);
		apply_button.setFocusPainted(false);
		apply_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<Integer> rowData = new ArrayList<Integer>();
				
				//tablePane.getViewPort().add(createTable(filterData, columnData));
				
				String employer_s = (String)comboBox.getSelectedItem();
				String date_s = date_text.getText();
				
				for(int rowIndex = 0; rowIndex < firstRecordData_2array.length; rowIndex++) {
					
					if(firstRecordData_2array[rowIndex][3].contains((employer_s == null ? "" : employer_s))){
						rowData.add(rowIndex);
					}
					
				}
				
				filterData_2array = new String[rowData.size()][5];
				
				for(int i = 0; i < filterData_2array.length; i++) {
					for(int j = 0; j < filterData_2array[i].length; j++) {
						filterData_2array[i][j] = (String) firstRecordData_2array[rowData.get(i)][j];
					}
				}
				
				table_scroll.getViewport().add(createTable(filterData_2array, column_array));
				
				comboBox.setSelectedItem(null);
				revalidate();
				repaint();
				
			}
		});
		ViewWorker.this.add(apply_button);
		
		JButton print_button = new JButton("Print the displayed data");
		print_button.setBackground(Color.CYAN);
		print_button.setBounds(table_scroll.getX(), apply_button.getY(), table_scroll.getWidth(), 40);
		print_button.setFocusPainted(false);
		print_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int result = JOptionPane.showConfirmDialog(ViewWorker.this, "Print data\nyes or no", "PRINTER", JOptionPane.OK_CANCEL_OPTION);
				if(result == 0) {
					// PRINT.worker(filtreData);
				}
				
			}
		});
		ViewWorker.this.add(print_button);
		
		revalidate();
		repaint();
		
	}
	
	
public JTable createTable(String[][] data, String[] columnData) {
		
		JTable table = new JTable(data, columnData) {
			DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
			DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();

			{
				renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
				renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
			}

		    @Override
		    public TableCellRenderer getCellRenderer (int row, int col) {
		    	if(col == 3)
		    		return renderLeft;
		        return renderCenter;
		    }
			
			
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		table.getColumnModel().getColumn(3).setPreferredWidth(120);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setRowHeight(30);
		
		return table;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	
		preview(((JTextField)e.getSource()).getText());
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		((JTextField)e.getSource()).setBackground(Color.WHITE);
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		((JTextField)e.getSource()).setBackground(new Color(238, 238, 238));
		
	}
	
	class NoSelectionModel extends DefaultListSelectionModel{
		@Override
		   public void setAnchorSelectionIndex(final int anchorIndex) {}

		   @Override
		   public void setLeadAnchorNotificationEnabled(final boolean flag) {}

		   @Override
		   public void setLeadSelectionIndex(final int leadIndex) {}

		   @Override
		   public void setSelectionInterval(final int index0, final int index1) { }
	}

	@Override
	public String toString() {
		return "View Worker";
	}
	
	

}
