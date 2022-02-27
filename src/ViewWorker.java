import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ViewWorker extends JPanel implements MouseListener{

	/**
	 * x position of the search box
	 */
	private final int SBX = 210;
	
	/**
	 * y position of the search box
	 */
	private final int SBY = 100;
	
	/**
	 * width of the search box
	 */
	private final int SBW = 500;
	
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
	private JLabel resultSearch_label;
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
		
		searchBottom_panel = new JPanel();
		add(searchBottom_panel);
		
		search_button = new JButton("Search");
		search_button.setBounds(SBX + SBW, SBY, 80, SBH - 1);
		search_button.setContentAreaFilled(false);
		search_button.setFocusPainted(false);
		search_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Action
				
			}
		});
		add(search_button);
		
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
