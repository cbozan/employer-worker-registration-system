package com.cbozan.view.add;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
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
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.cbozan.dao.DB;
import com.cbozan.dao.JobDAO;
import com.cbozan.dao.PaymentDAO;
import com.cbozan.dao.PaytypeDAO;
import com.cbozan.dao.WorkerDAO;
import com.cbozan.entity.Job;
import com.cbozan.entity.Payment;
import com.cbozan.entity.Paytype;
import com.cbozan.entity.Worker;
import com.cbozan.exception.EntityException;
import com.cbozan.view.component.SearchBox;
import com.cbozan.view.helper.Observer;

public class WorkerPaymentPanel extends JPanel implements Observer, FocusListener, ActionListener, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final List<Observer> observers;
	
	/*
	 * Left label x position
	 */
	private final int LLX = 100;
	
	/*
	 * Right label x position
	 */
	private final int RLX = 480;
	
	/*
	 * Left Label y positon
	 */
	private final int LLY = 220;
	
	/*
	 * Right Label y positon
	 */
	private final int RLY = 40;
	
	/*
	 * Left Label width
	 */
	private final int LLW = 200;
	
	/*
	 * Right Label width
	 */
	private final int RLW = 500;
	
	/*
	 * Label height
	 */
	private final int LH = 25;
	
	/*
	 * small height space
	 */
	private final int SHS = 1;
	
	/*
	 * mid height space
	 */
	private final int MHS = 10;
	
	
	
	private JLabel imageLabel;
	private JLabel workerLabel, jobLabel, paytypeLabel, amountLabel;
	private JTextField amountTextField;
	private JComboBox<Paytype> paytypeComboBox;
	private JButton payButton;
	
	private JLabel workerSearchImageLabel, jobSearchImageLabel;
	private SearchBox workerSearchBox, jobSearchBox;
	
	private JTextField workerTextField, jobTextField;
	
	private JCheckBox jobSearchCheckBox;
	
	private Worker selectedWorker; 
	private Job selectedJob;
	
	private Color defaultColor;
	private JScrollPane lastPaymentsScroll;
	private String[] paymentTableColumns = {"ID", "Job", "Payment Method", "Amount", "Date"};
	
	
	public WorkerPaymentPanel() {
		
		super();	
		setLayout(null);
		
		observers = new ArrayList<>();
		subscribe(this);
		
		imageLabel = new JLabel();
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setIcon(new ImageIcon("src\\icon\\new_worker_payment.png"));
		imageLabel.setBounds(LLX, 40, 128, 130);
		add(imageLabel);
		
		defaultColor = imageLabel.getForeground();
		selectedWorker = null;
		selectedJob = null;

		workerLabel = new JLabel("Worker selection");
		workerLabel.setBounds(LLX, LLY, LLW, LH);
		add(workerLabel);
		
		workerTextField = new JTextField("Please select worker");
		workerTextField.setEditable(false);
		workerTextField.setBounds(workerLabel.getX(), workerLabel.getY() + LH + SHS, LLW, LH);
		add(workerTextField);
		
		
		jobLabel = new JLabel("Job selection");
		jobLabel.setBounds(workerTextField.getX(), workerTextField.getY() + LH + MHS, LLW, LH);
		add(jobLabel);
		
		jobTextField = new JTextField("Please select job");
		jobTextField.setEditable(false);
		jobTextField.setBounds(jobLabel.getX(), jobLabel.getY() + LH + SHS, LLW, LH);
		add(jobTextField);
		
		
		paytypeLabel = new JLabel("Paymnet method");
		paytypeLabel.setBounds(jobTextField.getX(), jobTextField.getY() + LH + MHS, LLW, LH);
		add(paytypeLabel);
		
		paytypeComboBox = new JComboBox<>();
		paytypeComboBox.setBounds(paytypeLabel.getX(), paytypeLabel.getY() + LH + SHS, LLW, LH);
		add(paytypeComboBox);
		
		
		amountLabel = new JLabel("Amount of payment");
		amountLabel.setBounds(paytypeComboBox.getX(), paytypeComboBox.getY() + LH + MHS, LLW, LH);
		add(amountLabel);
		
		amountTextField= new JTextField();
		amountTextField.setBounds(amountLabel.getX(), amountLabel.getY() + LH + SHS, LLW, LH);
		amountTextField.setHorizontalAlignment(SwingConstants.CENTER);
		amountTextField.addFocusListener(this);
		amountTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!amountTextField.getText().replaceAll("\\s+", "").equals("") 
						&& decimalControl(amountTextField.getText())) {
				
					payButton.doClick();
					
				}
			}
		});
		add(amountTextField);
		
		
		payButton = new JButton("PAY (SAVE)");
		payButton.setBounds(amountTextField.getX(), amountTextField.getY() + 60, amountTextField.getWidth(), 30);
		payButton.setFocusPainted(false);
		payButton.addActionListener(this);
		add(payButton);
		
		workerSearchImageLabel = new JLabel(new ImageIcon("src\\icon\\search_worker.png"));
		workerSearchImageLabel.setBounds(RLX - 32 + (RLW - 30) / 4, RLY, 64, 64);
		add(workerSearchImageLabel);
		

		
		workerSearchBox = new SearchBox(WorkerDAO.getInstance().list(), new Dimension((RLW - 30) / 2, LH)) {
			private static final long serialVersionUID = 1L;
	
			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedWorker = (Worker) searchResultObject;
				workerSearchBox.setText(searchResultObject.toString());
				workerTextField.setText(searchResultObject.toString());
				workerSearchBox.setEditable(false);
				refreshData();
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		workerSearchBox.setBounds(RLX, RLY + 64 + 10, (RLW - 30) / 2, LH);
		add(workerSearchBox);
		
		workerSearchBox.getPanel().setBounds(workerSearchBox.getX(), workerSearchBox.getY() + LH, (RLW - 30) / 2, 0);
		add(workerSearchBox.getPanel());
		
		
	
		
		jobSearchImageLabel = new JLabel(new ImageIcon("src\\icon\\search.png"));
		jobSearchImageLabel.setBounds((RLX + workerSearchBox.getWidth() + 30) - 32 + (RLW - 30) / 4, RLY, 64, 64);
		add(jobSearchImageLabel);
		
		
		jobSearchBox = new SearchBox(JobDAO.getInstance().list(), new Dimension((RLW - 30) / 2, LH)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedJob = (Job) searchResultObject;
				jobSearchBox.setText(searchResultObject.toString());
				jobTextField.setText(searchResultObject.toString());
				jobSearchBox.setEditable(false);
				refreshData();
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		jobSearchBox.setBounds(RLX + workerSearchBox.getWidth() + 30, jobSearchImageLabel.getY() + 64 + 10, (RLW - 30) / 2, LH);
		add(jobSearchBox);
		
		jobSearchBox.getPanel().setBounds(jobSearchBox.getX(), jobSearchBox.getY() + LH, (RLW - 30) / 2, 0);
		add(jobSearchBox.getPanel());
		
		
		jobSearchCheckBox = new JCheckBox();
		jobSearchCheckBox.setBounds(jobSearchBox.getX(), jobSearchBox.getY() + jobSearchBox.getHeight() + 5, 18, 18);
		jobSearchCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				refreshData();
			}
		});
		add(jobSearchCheckBox);
		
		final String jobSearchText = "Only show payments for this job";
		JLabel checkBoxLabel = new JLabel(jobSearchText);
		checkBoxLabel.setBounds(jobSearchCheckBox.getX() + 25, jobSearchCheckBox.getY(), (RLW - 30) / 2 - 18, 20);
		checkBoxLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 10));
		checkBoxLabel.setForeground(new Color(0, 150, 0));
		add(checkBoxLabel);
		
		
		
		lastPaymentsScroll = new JScrollPane(new JTable(new String[][] {}, paymentTableColumns) {
			
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
			
			{
				setRowHeight(30);
				setShowVerticalLines(false);
				setShowHorizontalLines(false);
			}
		});
		lastPaymentsScroll.setBounds(RLX, workerTextField.getY(), RLW, 260);
		add(lastPaymentsScroll);
		
		update();
		
	}
	
	
	private boolean decimalControl(String ...args) {
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d{1,2})?$"); // decimal pattern xxx.xx
		boolean result = true;
		
		for(String arg : args)
			result = result && pattern.matcher(arg.replaceAll("\\s+", "")).find();
		
		return result;
	}

	@Override
	public void focusGained(FocusEvent e) {
		if(e.getSource() instanceof JTextField) {
			
			((JTextField)e.getSource()).setBorder(new LineBorder(Color.blue));
			
			if(((JTextField)e.getSource()) == amountTextField) {
				amountLabel.setForeground(Color.blue);
			}
			
		}
		
	}


	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource() instanceof JTextField) {
			
			Color color = Color.white;
			
			if(decimalControl(((JTextField)e.getSource()).getText())) {
				color = new Color(0, 180, 0);
			} else {
				color = Color.red;
			}
			
			((JTextField)e.getSource()).setBorder(new LineBorder(color));
		
			if(((JTextField)e.getSource()) == amountTextField) {
				amountLabel.setForeground(color);
			}
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == payButton) {
			
			Worker worker;
			Job job;
			Paytype paytype;
			String amount;
			
			worker = selectedWorker;
			job = selectedJob;
			paytype = (Paytype) paytypeComboBox.getSelectedItem();
			amount = amountTextField.getText().replaceAll("\\s+", "");

			if(!decimalControl(amount) || worker == null || job == null || paytype == null) {
				
				String message;
				message = "Please enter selection parts or format correctly (max 2 floating point)";
				JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
				
			} else {
				
				JTextArea workerTextArea, jobTextArea, paytypeTextArea, amountTextArea;
				
				workerTextArea = new JTextArea(worker.toString());
				workerTextArea.setEditable(false);
				
				jobTextArea = new JTextArea(job.toString());
				jobTextArea.setEditable(false);
				
				paytypeTextArea = new JTextArea(paytype.toString());
				paytypeTextArea.setEditable(false);
				
				amountTextArea = new JTextArea(amount + " ₺");
				amountTextArea.setEditable(false);
				
				Object[] pane = {
						new JLabel("Worker"),
						workerTextArea,
						new JLabel("Job"),
						jobTextArea,
						new JLabel("Payment method"),
						paytypeTextArea,
						new JLabel("Amount of payment"),
						amountTextArea
				};
		
				int result = JOptionPane.showOptionDialog(this, pane, "Confirmation", 1, 1, 
						new ImageIcon("src\\icon\\accounting_icon_1_32.png"), new Object[] {"SAVE", "CANCEL"}, "CANCEL");
				
				
				// System.out.println(result);
				// 0 -> SAVE
				// 1 -> CANCEL
				
				if(result == 0) {
					
					Payment.PaymentBuilder builder = new Payment.PaymentBuilder();
					builder.setId(Integer.MAX_VALUE);
					builder.setWorker(worker);
					builder.setJob(job);
					builder.setPaytype(paytype);
					builder.setAmount(new BigDecimal(amount));
					
					Payment payment = null;
					try {
						payment = builder.build();
					} catch (EntityException e1) {
						System.out.println(e1.getMessage());
					}
					
					if(PaymentDAO.getInstance().create(payment)) { 
						JOptionPane.showMessageDialog(this, "Registraion successful");
						notifyAllObservers();
					} else {
						JOptionPane.showMessageDialog(this, "Not saved", "Database error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
			
			
		}
		
	}
	
	
	private void clearPanel() {
		
		amountTextField.setText("");
		amountTextField.setBorder(new LineBorder(Color.white));
		amountLabel.setForeground(defaultColor);
		
		DB.destroyConnection();
		
		workerSearchBox.setObjectList(WorkerDAO.getInstance().list());
		jobSearchBox.setObjectList(JobDAO.getInstance().list());
		paytypeComboBox.setModel(new DefaultComboBoxModel<>(PaytypeDAO.getInstance().list().toArray(new Paytype[0])));
		
		if(selectedWorker != null) {
			
			String[] columnName;
			int[] id;
			
			if(selectedJob != null && jobSearchCheckBox.isSelected()) {
				columnName = new String[2];
				id = new int[2];
				
				columnName[0] = "worker_id";
				columnName[1] = "job_id";
				
				id[0] = selectedWorker.getId();
				id[1] = selectedJob.getId();
				
			} else {
				columnName = new String[1];
				id = new int[1];
				
				columnName[0] = "worker_id";
				id[0] = selectedWorker.getId();
			}
			
			List<Payment> paymentList = PaymentDAO.getInstance().list(columnName, id);
			String[][] tableData = new String[paymentList.size()][5];
			
			int i = 0;
			for(Payment pay : paymentList) {
				
				tableData[i][0] = pay.getId() + "";
				tableData[i][1] = pay.getJob().toString();
				tableData[i][2] = pay.getPaytype().toString();
				tableData[i][3] = NumberFormat.getInstance().format(pay.getAmount()) + " ₺";
				tableData[i][4] = new SimpleDateFormat("dd.MM.yyyy").format(pay.getDate()); 
				
				++i;
			}
			
			
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).setModel(new DefaultTableModel(tableData, paymentTableColumns));
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(0).setPreferredWidth(15);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(1).setPreferredWidth(25);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(2).setPreferredWidth(20);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(3).setPreferredWidth(20);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(4).setPreferredWidth(20);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
			
		}
		
	}
	
	public void subscribe(Observer observer) {
		observers.add(observer);
	}
	
	public void unsubscribe(Observer observer) {
		observers.remove(observer);
	}
	
	public void notifyAllObservers() {
		for(Observer observer : observers) {
			observer.update();
		}
	}
	
	public void refreshData() {
		clearPanel();
	}


	@Override
	public void update() {
		
		clearPanel();
		
	}
	
}
