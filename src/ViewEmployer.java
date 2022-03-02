import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
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

public class ViewEmployer extends JPanel{
	
	JButton newSearch_button, search_button;
	JTextField searchBox_text;
	JPanel searchBottom_panel;
	JLabel searchBoxInfoMessage_label, searchBoxTitle_label;
	private /*final*/ String[][] employers;// DataBase
	
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
		
		JTable t_isTable;
		String [] columnNames = {"ID", "Employer","Date", "Number of worker", "paid"};
		String [][] rowData = {{"r1c1", "r1c2", "sdf","r1c3","r1c4"},{"r2c1", "r2c2", "r2c3","r2c4"},{"r3c1", "r3c2", "r3c3","r3c4"},
								{"r4c1", "r4c2", "r4c3","r4c4"}, {"r5c1", "r5c2", "r5c3","r5c4"}, {"r6c1", "r6c2", "r6c3","r1c6n"},
								{"r4c1", "r4c2", "r4c3","r4c4"}, {"r5c1", "r5c2", "r5c3","r5c4"}, {"r6c1", "r6c2", "r6c3","r1c6n"},
								{"r4c1", "r4c2", "r4c3","r4c4"}, {"r5c1", "r5c2", "r5c3","r5c4"}, {"r6c1", "r6c2", "r6c3","r1c6n"}};
		t_isTable = new JTable(rowData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
			
			DefaultTableCellRenderer render = new DefaultTableCellRenderer();
			{
				render.setHorizontalAlignment(SwingConstants.CENTER);
			}
			public TableCellRenderer getCellRenderer (int row, int col) {
				
				return render;
			}
		};
		
		t_isTable.getColumnModel().getColumn(0).setPreferredWidth(5);
		t_isTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		t_isTable.getColumnModel().getColumn(2).setPreferredWidth(50);
		t_isTable.getColumnModel().getColumn(3).setPreferredWidth(5);
		t_isTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		
		
		t_isTable.setRowHeight(25);
		
		JScrollPane js_isTable = new JScrollPane(t_isTable);
		js_isTable.setBounds(175, 30, 600, 200);
		
		
		
		this.add(js_isTable);
		
		
		revalidate();
		repaint();
		
	}
	
	protected void searchResultsGUI(String searchText) {
		
		this.removeAll();
		this.add(newSearch_button);
		
		DefaultListModel<String> searchResultsListModel = new DefaultListModel<String>();
		for(int i = 0; i < employers.length; i++) {
			searchResultsListModel.addElement(employers[i][1]);
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
			
			if(employers[i][1].contains(text.toUpperCase())) {
				searchBottom_panel.add((JTextField)getBottomComponent(employers[i][1].toUpperCase(), position));
				searchBottom_panel.add((JButton)getBottomComponent("", position));
				position++;
			}
			
			
		}
		
		searchBottom_panel.setSize(482, (position > 10 ? 10 : position) * 29);
		searchBottom_panel.setVisible(true);
		
		//IsciOdeme.this.revalidate();
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
	

}
