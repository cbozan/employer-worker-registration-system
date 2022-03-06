import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class NewRecord extends JPanel implements CaretListener, ActionListener, ListSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * x position of the List box
	 */
	private final int LBX = 85;
	
	/**
	 * y position of the List box
	 */
	private final int LBY = 90;
	
	/**
	 * width of the List box
	 */
	private final int LBW = 200;
	
	/**
	 * height of the list box
	 */
	private final int LBH = 130;
	
	/**
	 * space of the list box
	 */
	private final int LBS = 400;
	
	/**
	 * height of the search box
	 */
	private final int SBH = 30;
	
	/**
	 * width of the search box
	 */
	private final int SBW = 200;
	
	/**
	 * width of button
	 */
	private final int BW = 120;
	
	/**
	 * height of button
	 */
	private final int BH = 30;
	
	/**
	 * space of button
	 */
	private final int BS = 70;
	
	/**
	 * count text
	 */
	private final String countText = "Selected : ";
	
	private JList<String> selectionBox_list, selectedBox_list;
	private DefaultListModel<String> selection_model, selected_model, data_model;
	private JTextField selectionSearchBox_text, selectedSearchBox_text, dateBox_text;
	private JScrollPane selection_scroll, selected_scroll, note_scroll;
	private JButton add_button, remove_button, save_button;
	
	private JLabel selectionBox_label, selectedBox_label, countText_label, employer_label;
	private JLabel dateBox_label, todayCheckBox_label, selectionEmployer_label, selectedEmployer_label;
	private JLabel selectionDate_label, selectedDate_label, note_label, noteRemainderCharacter_label;
	
	private JTextArea note_textArea;
	private String logNote;
	private JCheckBox today_checkBox;
	private JComboBox<String> employer_comboBox;
	private DefaultComboBoxModel<String> employer_model;
	
	
	public NewRecord() {
		
		setLayout(null);
		
		data_model = new DefaultListModel<>(); // get database
		data_model.addElement("JAMES");
		data_model.addElement("MARY");
		data_model.addElement("ROBERT");
		data_model.addElement("PATRICIA");
		data_model.addElement("JENNIFER");
		data_model.addElement("JOHN");
		
		selection_model = data_model;
		
		employer_model = new DefaultComboBoxModel<>(); // get database
		employer_model.addElement("JAMES");
		employer_model.addElement("MARY");
		employer_model.addElement("ROBERT");
		employer_model.addElement("PATRICIA");
		employer_model.addElement("JENNIFER");
		employer_model.addElement("JOHN");
		
		selected_model = new DefaultListModel<String>();
		
		selectionBox_list = new JList<String>(selection_model);
		selectionBox_list.setFixedCellHeight(24);
		selectionBox_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectionBox_list.addListSelectionListener(this);
		
		selectedBox_list = new JList<String>(selected_model);
		selectedBox_list.setFixedCellHeight(24);
		selectedBox_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectedBox_list.addListSelectionListener(this);
		
		selection_scroll = new JScrollPane(selectionBox_list);
		selection_scroll.setBounds(LBX, LBY, LBW, LBH);
		add(selection_scroll);
		
		selected_scroll = new JScrollPane(selectedBox_list);
		selected_scroll.setBounds(selection_scroll.getX() + LBS + LBW, selection_scroll.getY(), LBW, LBH);
		add(selected_scroll);
		
		selectionBox_label = new JLabel("Worker List");
		selectionBox_label.setBounds(LBX + (LBW - 50) / 2, LBY - SBH - 50, LBW, 50);
		add(selectionBox_label);
		
		selectedBox_label = new JLabel("Selected List");
		selectedBox_label.setBounds(LBX + LBW + LBS + (LBW - 100) / 2, selectionBox_label.getY(), LBW, 50);
		add(selectedBox_label);
		
		countText_label = new JLabel(countText + selected_model.getSize());
		countText_label.setBounds(LBX + LBW + LBS, LBY + LBH - 5, LBW, BH);
		add(countText_label);
		
		employer_label = new JLabel("Choose employer");
		employer_label.setBounds(LBX, LBY + LBH + 70, LBW, 24);
		add(employer_label);
		
		selectionEmployer_label = new JLabel("Selected employer");
		selectionEmployer_label.setBounds(employer_label.getX() + LBW + LBS, employer_label.getY(), LBW, 24);
		add(selectionEmployer_label);
		
		employer_comboBox = new JComboBox<String>(employer_model);
		employer_comboBox.setSelectedItem(null);
		employer_comboBox.setBounds(LBX, employer_label.getY() + employer_label.getHeight(), LBW, 24);
		employer_comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedEmployer_label.setText((String)employer_comboBox.getSelectedItem());
			}
		});
		add(employer_comboBox);
		
		selectedEmployer_label = new JLabel((String)employer_comboBox.getSelectedItem());
		selectedEmployer_label.setFont(new Font(Font.SANS_SERIF, Font.BOLD + Font.ITALIC, 16));
		selectedEmployer_label.setForeground(Color.gray);
		selectedEmployer_label.setBounds(employer_comboBox.getX() + LBW + LBS, employer_comboBox.getY(), LBW, 24);
		add(selectedEmployer_label);
		
		dateBox_label = new JLabel("Choose date (YYYY-MM-DD)");
		dateBox_label.setBounds(LBX, employer_comboBox.getY() + employer_comboBox.getHeight() + 50, LBW, 24);
		add(dateBox_label);
		
		selectionDate_label = new JLabel("Selected date");
		selectionDate_label.setBounds(dateBox_label.getX() + LBW + LBS, dateBox_label.getY(), LBW, 24);
		add(selectionDate_label);
		
		dateBox_text = new JTextField();
		dateBox_text.setDocument(new PlainDocument());
		dateBox_text.setBounds(dateBox_label.getX(), dateBox_label.getY() + dateBox_label.getHeight(), LBW, 24);
		dateBox_text.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				selectedDate_label.setText(dateBox_text.getText());
			}
		});
		add(dateBox_text);
		
		selectedDate_label = new JLabel(dateBox_text.getText());
		selectedDate_label.setBounds(dateBox_text.getX() + LBW + LBS, dateBox_text.getY(), LBW, 24);
		selectedDate_label.setFont(selectedEmployer_label.getFont());
		selectedDate_label.setForeground(Color.gray);
		add(selectedDate_label);
		
		today_checkBox = new JCheckBox();
		today_checkBox.setBounds(dateBox_text.getX(), dateBox_text.getY() + dateBox_text.getHeight(), 18, 18);
		today_checkBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.DESELECTED)
					dateBox_text.setText("");
				else if(e.getStateChange() == ItemEvent.SELECTED)
					dateBox_text.setText("" + LocalDate.now());
			}
		});
		add(today_checkBox);
		
		todayCheckBox_label = new JLabel("Today");
		todayCheckBox_label.setBounds(today_checkBox.getX() + today_checkBox.getWidth() + 5, 
				today_checkBox.getY(), LBW - today_checkBox.getWidth(), 18);
		add(todayCheckBox_label);
		
		note_label = new JLabel("NOTE");
		note_label.setBounds(dateBox_label.getX(), dateBox_label.getY() + 80, LBW, 24);
		add(note_label);
		
		note_textArea = new JTextArea();
		note_textArea.setBounds(0, 0, LBS, 60);
		note_textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(note_textArea.getText().length() < 256) {
					logNote = note_textArea.getText();
					noteRemainderCharacter_label.setText("Limited to 255 characters (Remainder : " + (255 - logNote.length()) + " )");
					noteRemainderCharacter_label.setForeground(new Color(0, 190, 0));
				} else {
					note_textArea.setText(logNote);
					noteRemainderCharacter_label.setForeground(new Color(190, 0, 0));
				}
			}
		});
		
		note_scroll = new JScrollPane(note_textArea);
		note_scroll.setBounds(note_label.getX(), note_label.getY() + note_label.getHeight(), LBS, 60);
		add(note_scroll);
		
		noteRemainderCharacter_label = new JLabel("Limited to 255 characters (Remainder : )");
		noteRemainderCharacter_label.setFont(new Font(Font.DIALOG, Font.ITALIC, 9));
		noteRemainderCharacter_label.setForeground(new Color(0, 190, 0));
		noteRemainderCharacter_label.setBounds(note_scroll.getX(), note_scroll.getY() + note_scroll.getHeight(), LBW, 24);
		add(noteRemainderCharacter_label);
		
		add_button = new JButton("Add");
		add_button.setFocusPainted(false);
		add_button.setEnabled(false);
		add_button.setBackground(Color.gray);
		add_button.setBounds(LBX + LBW + (LBS - BW) / 2, LBY + (LBH - 4 * BH) / 2, BW, BH);
		add_button.addActionListener(this);
		add(add_button);
		
		remove_button = new JButton("Remove");
		remove_button.setFocusPainted(false);
		remove_button.setEnabled(false);
		remove_button.setBackground(Color.gray);
		remove_button.setBounds(LBX + LBW + (LBS - BW) / 2, add_button.getY() + BS, BW, BH);
		remove_button.addActionListener(this);
		add(remove_button);
		
		selectionSearchBox_text = new JTextField();
		selectionSearchBox_text.setBounds(LBX, LBY - SBH, SBW, SBH);
		selectionSearchBox_text.addCaretListener(this);
		add(selectionSearchBox_text);
		
		selectedSearchBox_text = new JTextField();
		selectedSearchBox_text.setBounds(LBX + LBW + LBS, LBY - SBH, SBW, SBH);
		selectedSearchBox_text.addCaretListener(this);
		add(selectedSearchBox_text);
		
		save_button = new JButton("SAVE");
		save_button.setBounds(note_scroll.getX() + LBW + LBS, note_scroll.getY(), LBW, note_scroll.getHeight() - 10);
		save_button.setFocusPainted(false);
		save_button.setBackground(Color.CYAN);
		save_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean date_bool = false;
				
				try {
					new SimpleDateFormat("yyyy-MM-dd").parse(dateBox_text.getText());
					date_bool = true;
				} catch (ParseException e1) {
					
				}
				
				if(selected_model.getSize() > 0 && employer_comboBox.getSelectedItem() != null) {
					
					JTextArea employer_text_t, worker_text_t, date_text_t, count_text_t, note_text_t;
					String worker_s = "";
					for(int i = 0; i < selected_model.getSize(); i++) {
						worker_s += (i + 1) + " - " + selected_model.getElementAt(i) + "\n";
					}
					
					employer_text_t = new JTextArea((String)employer_comboBox.getSelectedItem());
					employer_text_t.setEditable(false);
					
					worker_text_t = new JTextArea(worker_s);
					worker_text_t.setEditable(false);
					
					date_text_t = new JTextArea(dateBox_text.getText());
					date_text_t.setEditable(false);
					
					count_text_t = new JTextArea(selected_model.getSize() + " Workers");
					count_text_t.setEditable(false);
					
					note_text_t = new JTextArea(note_textArea.getText());
					note_text_t.setEditable(false);
					
					JScrollPane worker_j = new JScrollPane(worker_text_t) {
						
						@Override
						public Dimension getPreferredSize() {
							return new Dimension(300, 200);
						}
						
					};
					
					JScrollPane note_j = new JScrollPane(note_text_t) {
						@Override
						public Dimension getPreferredSize() {
							return new Dimension(300, 100);
						}
					};
					
					
					if(date_bool) {
						
						Object[] pane = {
								new JLabel("Employer"),
								employer_text_t,
								new JLabel("Workers"),
								worker_j,
								new JLabel("Date"),
								date_text_t,
								new JLabel("Selected workers"),
								count_text_t,
								new JLabel("Note"),
								note_j
						};
						
						int result = JOptionPane.showOptionDialog(NewRecord.this, pane, "DOCUMENT", 1, 1, 
								new ImageIcon("src\\icons\\accounting_icon_1_32.png"), new Object[] {"SAVE", "CANCEL"}, "CANCEL");
						
						if(result == 0) {
							
							// save to database
							
						}
						
					} else {
						
						JOptionPane.showMessageDialog(NewRecord.this, "Please check the date field", "DATE", JOptionPane.ERROR_MESSAGE);
						
					}
					
				} else {
					JOptionPane.showMessageDialog(NewRecord.this, "Please fill in the blanks", "NULL", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		add(save_button);
		
		
		
		
	}
	
	
	public DefaultListModel<String> searchBoxSort(JTextField t){
		
		DefaultListModel<String> model = null, newModel = new DefaultListModel<String>();
		String text;
		
		if(t == selectionSearchBox_text)
			model = selection_model;
		else if(t == selectedSearchBox_text)
			model = selected_model;
		
		if(!t.getText().equals("")) {
			
			if(model != null) {
				
				text = t.getText().toUpperCase();
				
				for(int i = 0; i < model.getSize(); i++) {
					
					if( ((String)model.get(i)).contains(text)) {
						newModel.addElement(model.get(i));
					}
					
				}
				
			}
			
		} else {
			newModel = model;
		}
		
		return newModel;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == add_button && selectionBox_list.getSelectedValue() != null) {
			
			String text = (String) selectionBox_list.getSelectedValue();
			selection_model.removeElement(text);
			selected_model.addElement(text);
			selectionSearchBox_text.setText("");
			add_button.setBackground(Color.gray);
			add_button.setEnabled(false);
			
		} else if(e.getSource() == remove_button && selectedBox_list.getSelectedValue() != null) {
			
			String text = (String) selectedBox_list.getSelectedValue();
			selected_model.removeElement(text);
			selection_model.addElement(text);
			selectedSearchBox_text.setText("");
			remove_button.setBackground(Color.gray);
			remove_button.setEnabled(false);
			
		}
		
		countText_label.setText(countText + selected_model.getSize());
		
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		
		if(e.getSource() == selectionSearchBox_text) {
			selectionBox_list.setModel(searchBoxSort(selectionSearchBox_text));
		} else if(e.getSource() == selectedSearchBox_text) {
			selectedBox_list.setModel(searchBoxSort(selectedSearchBox_text));
		}
		
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if(e.getSource() == selectionBox_list && selectionBox_list.getSelectedValue() != null){//selectionBox_list.getModel().getSize() != 0) {
			
			remove_button.setBackground(Color.gray);
			remove_button.setEnabled(false);
			add_button.setBackground(Color.green);
			add_button.setEnabled(true);
			selectedBox_list.clearSelection();
			selectedSearchBox_text.setText("");
			
		} else if(e.getSource() == selectedBox_list && selectedBox_list.getSelectedValue() != null){ //&& selectedBox_list.getModel().getSize() != 0) {
			
			add_button.setBackground(Color.gray);
			add_button.setEnabled(false);
			remove_button.setBackground(Color.red);
			remove_button.setEnabled(true);
			selectionBox_list.clearSelection();
			selectionSearchBox_text.setText("");
			
		}
		
	}


	@Override
	public String toString() {
		return "New Record";
	}
	
	
	
	
	
}
