package com.cbozan.main;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.cbozan.entity.Price;
import com.cbozan.view.add.WorkPanel;
import com.cbozan.view.add.WorkerPaymentPanel;
import com.cbozan.view.helper.Observer;
import com.cbozan.view.record.EmployerPanel;
import com.cbozan.view.record.JobPanel;
import com.cbozan.view.record.PricePanel;
import com.cbozan.view.record.WorkerPanel;



public class MainFrame extends JFrame implements ActionListener{

	
	public static final String FRAME_NAME = "Hesap-eProject";
	
	public static final int W_FRAME = 1080;
	public static final int H_FRAME = (int) (W_FRAME / ((Math.sqrt(5) + 1) / 2));
	public static final int MENU_BUTTON_WIDTH = 120;
	public static final int MENU_BUTTON_HEIGHT = 60;
	public static Insets INSETS;
	
	public final int NEW_EMPLOYER_ACTION_COMMAND = 0;
	public final int NEW_WORKER_ACTION_COMMAND = 1;
	
	private byte activePage = 0;
	
	private JMenuBar menuBar;
	private JMenu newRecordMenu, addMenu, displayMenu;
	private JMenuItem newEmployerItem, newWorkerItem, newJobItem, newPriceItem;
	private JMenuItem newWorkItem, newWorkerPaymentItem, newEmployerPaymentItem;
	private JMenuItem displayWorkerItem, displayEmployerItem, displayJobItem, displayWorkItem, displayWorkPayment, displayJobPayment;
	private List<Observer> components;
	
	public MainFrame() {
		this(0);
	}
	
	public MainFrame(int component) {
		super(FRAME_NAME);
		super.setName("MYFRAME");
		setLayout(null);
		setSize(W_FRAME, H_FRAME);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		activePage = (byte) component;
		
		INSETS = getInsets();
		
		GUI();
		
	}
	
	
	private void GUI() {
		createMenus();
		createComponents();
		//init();
		
	}
	
	
	
	private void createMenus() {
		
		menuBar = new JMenuBar();
		
		newRecordMenu = new JMenu("Record");
		addMenu = new JMenu("Add");
		displayMenu = new JMenu("Display");
		
		
		newJobItem = new JMenuItem("New Job");
		newJobItem.setActionCommand("0");
		newJobItem.addActionListener(this);
		
		newWorkerItem = new JMenuItem("New worker");
		newWorkerItem.setActionCommand("1");
		newWorkerItem.addActionListener(this);
		
		newEmployerItem = new JMenuItem("New employer");
		newEmployerItem.setActionCommand("2");
		newEmployerItem.addActionListener(this);
		
		newPriceItem = new JMenuItem("New price");
		newPriceItem.setActionCommand("3");
		newPriceItem.addActionListener(this);
		
		newRecordMenu.add(newJobItem);
		newRecordMenu.add(newWorkerItem);
		newRecordMenu.add(newEmployerItem);
		newRecordMenu.add(newPriceItem);
		
		
		newWorkerPaymentItem = new JMenuItem("Worker payment");
		newWorkerPaymentItem.setActionCommand("4");
		newWorkerPaymentItem.addActionListener(this);
		
		newWorkItem = new JMenuItem("Work");
		newWorkItem.setActionCommand("5");
		newWorkItem.addActionListener(this);
		
		newEmployerPaymentItem = new JMenuItem("Employer payment");
		newEmployerPaymentItem.setActionCommand("6");
		newEmployerPaymentItem.addActionListener(this);
		
		addMenu.add(newWorkerPaymentItem);
		addMenu.add(newWorkItem);
		addMenu.add(newEmployerPaymentItem);
		
		
		displayWorkerItem = new JMenuItem("Display worker");
		displayWorkerItem.setActionCommand("7");
		displayWorkerItem.addActionListener(this);
		
		displayEmployerItem = new JMenuItem("Display employer");
		displayEmployerItem.setActionCommand("8");
		displayEmployerItem.addActionListener(this);
		
		displayJobItem = new JMenuItem("Display job");
		displayJobItem.setActionCommand("9");
		displayJobItem.addActionListener(this);
		
		displayMenu.add(displayJobItem);
		displayMenu.add(displayWorkerItem);
		displayMenu.add(displayEmployerItem);
		
		menuBar.add(newRecordMenu);
		menuBar.add(addMenu);
		menuBar.add(displayMenu);
		
		
		this.setJMenuBar(menuBar);
		
		
	}
	
	
	private void createComponents() {
		
		// record menu panels
		JobPanel job = new JobPanel();
		WorkerPanel worker = new WorkerPanel();
		EmployerPanel employer = new EmployerPanel();
		PricePanel price = new PricePanel();
		
		// add menu panels
		WorkerPaymentPanel workerPayment = new WorkerPaymentPanel();
		WorkPanel work = new WorkPanel();
		
		components = new ArrayList<>();
		components.add(job);
		components.add(worker);
		components.add(employer);
		components.add(price);
		components.add(workerPayment);
		components.add(work);
		
		
		
		setContentPane((JPanel) components.get(activePage));
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(Integer.parseInt(e.getActionCommand())>= 0 && Integer.parseInt(e.getActionCommand()) < components.size()) {
			if(Integer.parseInt(e.getActionCommand()) == activePage) {
				components.get(activePage).update();
			} else {
				activePage = (byte) Integer.parseInt(e.getActionCommand());
			}
			setContentPane((JPanel)components.get(activePage));
			
			revalidate();
		}
		
	}

}
