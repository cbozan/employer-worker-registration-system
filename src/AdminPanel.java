import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


public class AdminPanel extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "Registration System";
	/**
	 * AdminPanel window width
	 */
	public static int W_FRAME = 960;
	
	/**
	 * AdminPanel window height
	 */
	public static int H_FRAME = 2 * W_FRAME / 3;
	
	/**
	 * frame edge
	 */
	public static Insets INSETS;
	
	/**
	 * current settings
	 */
	
	private JMenuBar menuBar_menubar;
	private JMenu homePage_menu, record_menu, view_menu, bill_menu, system_menu;
	private JMenuItem addEmployer_item, addWorker_item, addRecord_item, viewRecord_item;
	private JMenuItem viewWorker_item, billEmployer_item, billWorker_item;
	private JMenuItem settings_item, reset_item, logout_item;
	private HomePage homePage;
	private ArrayList<JPanel> components = new ArrayList<>();
	private int currentComponent = 0;
	
	public AdminPanel() {
		
		super("Registration System");
		setIconImage(Toolkit.getDefaultToolkit().getImage("src\\icons\\admin_user.png"));
		setSize(W_FRAME, H_FRAME);
		setResizable(false);
		setLayout(null);
		setLocationRelativeTo(null);
		setLocation(getX() - 40, getY() - 20);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		INSETS = getInsets();
		
		GUI();
		
	}
	
	
	private void GUI() {
		
		createMenus();
		createComponents();
		init();
		
		
	}
	
	private void createMenus() {
		
		menuBar_menubar = new JMenuBar();
	
		
		homePage_menu = new JMenu("Home page");
		homePage_menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				currentComponent = 0;
				init();
				
			}
		});

		record_menu = new JMenu("Record");
		view_menu = new JMenu("View");
		bill_menu = new JMenu("Bill");
		system_menu = new JMenu("System");
		

		addWorker_item = new JMenuItem("New worker");
		record_menu.add(addWorker_item);
		addWorker_item.addActionListener(this);
		
		addRecord_item = new JMenuItem("New record");
		record_menu.add(addRecord_item);
		addRecord_item.addActionListener(this);
		
		addEmployer_item = new JMenuItem("New employer");
		record_menu.add(addEmployer_item);
		addEmployer_item.addActionListener(this);
		
		viewRecord_item = new JMenuItem("View record");
		view_menu.add(viewRecord_item);
		viewRecord_item.addActionListener(this);
		
		viewWorker_item = new JMenuItem("View worker");
		view_menu.add(viewWorker_item);
		viewWorker_item.addActionListener(this);
		
		
		billWorker_item = new JMenuItem("Worker payment");
		bill_menu.add(billWorker_item);
		billWorker_item.addActionListener(this);
		
		billEmployer_item = new JMenuItem("Employer payment");
		bill_menu.add(billEmployer_item);
		billEmployer_item.addActionListener(this);
		
		settings_item = new JMenuItem("Settings");
		system_menu.add(settings_item);
		settings_item.addActionListener(this);
		
		reset_item = new JMenuItem("Reset");
		system_menu.add(reset_item);
		reset_item.addActionListener(this);
		
		logout_item = new JMenuItem("Logout");
		system_menu.add(logout_item);
		logout_item.addActionListener(this);
		
		
		menuBar_menubar.add(homePage_menu);
		menuBar_menubar.add(record_menu);
		menuBar_menubar.add(view_menu);
		menuBar_menubar.add(bill_menu);
		menuBar_menubar.add(system_menu);

		
		setJMenuBar(menuBar_menubar);
		
	}
	
	
	private void createComponents() {
		
		components.add(new HomePage());
		components.add(new NewWorker());
		components.add(new NewRecord());
		components.add(new NewEmployer());
		components.add(new ViewRecord());
		components.add(new ViewWorker());
		components.add(new WorkerPayment());
		components.add(new EmployerPayment());
		
	}
	
	
	private void init() {
		
		setContentPane(components.get(currentComponent));
		this.setTitle(NAME + " - " + components.get(currentComponent).toString());
		this.revalidate();
		this.repaint();
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if( ((JMenuItem)e.getSource()).getText().equals(addWorker_item.getText())) {
			
			if(currentComponent == 1) {
				components.set(currentComponent, new NewWorker());
			} else {
				currentComponent = 1;
			}
			
			init();
			
		} else if( ((JMenuItem)e.getSource()).getText().equals(addRecord_item.getText())) {
			
			if(currentComponent == 2) {
				components.set(currentComponent, new NewRecord());
			} else {
				currentComponent = 2;
			}
			
			init();
			
		} else if( ((JMenuItem)e.getSource()).getText().equals(addEmployer_item.getText())) {
			
			if(currentComponent == 3) {
				components.set(currentComponent, new NewEmployer());
			} else {
				currentComponent = 3;
			}
			
			init();
			
		} else if( ((JMenuItem)e.getSource()).getText().equals(viewRecord_item.getText())) {

			if(currentComponent == 4) {
				components.set(currentComponent, new ViewRecord());
			} else {
				currentComponent = 4;
			}
			
			init();
			
		} else if( ((JMenuItem)e.getSource()).getText().equals(viewWorker_item.getText())) {

			if(currentComponent == 5) {
				components.set(currentComponent, new ViewWorker());
			} else {
				currentComponent = 5;
			}
			
			init();
			
		} else if( ((JMenuItem)e.getSource()).getText().equals(billWorker_item.getText())) {

			if(currentComponent == 6) {
				components.set(currentComponent, new WorkerPayment());
			} else {
				currentComponent = 6;
			}
			
			init();
			
		} else if( ((JMenuItem)e.getSource()).getText().equals(billEmployer_item.getText())) {

			if(currentComponent == 7) {
				components.set(currentComponent, new EmployerPayment());
			} else {
				currentComponent = 7;
			}
			
			init();
			
		} else if( ((JMenuItem)e.getSource()).getText().equals(settings_item.getText())) {
			
			// Settings pane .. . 
			
		} else if( ((JMenuItem)e.getSource()).getText().equals(reset_item.getText())) {
			
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					AdminPanel.this.dispose();
					try {
						Thread.sleep(350);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					new AdminPanel();
				}
			});
		} else if( ((JMenuItem)e.getSource()).getText().equals(logout_item.getText())) {
			
			this.dispose();
			
		} 
		
		
	}
	
	

}
