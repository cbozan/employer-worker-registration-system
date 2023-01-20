package com.cbozan.view.add;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import com.cbozan.dao.InvoiceDAO;
import com.cbozan.dao.JobDAO;
import com.cbozan.entity.Invoice;
import com.cbozan.entity.Job;
import com.cbozan.exception.EntityException;
import com.cbozan.view.component.SearchBox;
import com.cbozan.view.helper.Observer;

public class JobPaymentPanel extends JPanel implements Observer,FocusListener, ActionListener, Serializable{

	private static final long serialVersionUID = 291336645961737012L;
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
	private final int RLY = 30;
	
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
	private final int SHS = 5;
	
	/*
	 * mid height space
	 */
	private final int MHS = 15;
	
	private final String[] invoiceTableColumns = {"ID", "Amount", "Date"};
	
	private JLabel imageLabel, searchImageLabel;
	private JLabel amountLabel, jobTitleLabel, employerLabel;
	private JTextField amountTextField, jobTitleTextField, employerTextField;
	private JButton takePaymentButton;
	private JScrollPane lastPaymentsScroll;
	private SearchBox searchJobSearchBox;
	private Job selectedJob;
	private Color defaultColor;

	
	public JobPaymentPanel() {
		
		super();	
		setLayout(null);
		
		observers = new ArrayList<>();
		subscribe(this);
		
		imageLabel = new JLabel();
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setIcon(new ImageIcon("src\\icon\\new_job_payment.png"));
		imageLabel.setBounds(LLX, 40, 128, 130);
		add(imageLabel);
		
		defaultColor = imageLabel.getForeground();
		selectedJob = null;
		
		jobTitleLabel = new JLabel("Job title");
		jobTitleLabel.setBounds(LLX, LLY, LLW, LH);
		add(jobTitleLabel);
		
		jobTitleTextField = new JTextField("Please select job");
		jobTitleTextField.setEditable(false);
		jobTitleTextField.setBounds(jobTitleLabel.getX(), jobTitleLabel.getY() + LH + SHS, LLW, LH);
		add(jobTitleTextField);
		
		employerLabel = new JLabel("Employer");
		employerLabel.setBounds(jobTitleTextField.getX(), jobTitleTextField.getY() + LH + MHS, LLW, LH);
		add(employerLabel);
		
		employerTextField = new JTextField("Please select employer");
		employerTextField.setEditable(false);
		employerTextField.setBounds(employerLabel.getX(), employerLabel.getY() + LH + SHS, LLW, LH);
		add(employerTextField);
		
		amountLabel = new JLabel("Amount of payment");
		amountLabel.setBounds(employerTextField.getX(), employerTextField.getY() + LH + SHS + MHS + MHS, LLW, LH);
		add(amountLabel);
		
				
		amountTextField= new JTextField();
		amountTextField.setBounds(amountLabel.getX(), amountLabel.getY() + LH + SHS, LLW, LH);
		amountTextField.setHorizontalAlignment(SwingConstants.CENTER);
		amountTextField.addFocusListener(this);
		amountTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!amountTextField.getText().replaceAll("\\s+", "").equals("") 
						&& decimalControl(amountTextField.getText())) {
				
					takePaymentButton.doClick();
					
				}
			}
		});
		add(amountTextField);
		
		
		takePaymentButton = new JButton("TAKE PAYMENT (SAVE)");
		takePaymentButton.setBounds(amountTextField.getX(), amountTextField.getY() + LH + MHS + SHS, amountTextField.getWidth(), 30);
		takePaymentButton.setFocusPainted(false);
		takePaymentButton.addActionListener(this);
		add(takePaymentButton);
		
		
		searchImageLabel = new JLabel(new ImageIcon("src\\icon\\search.png"));
		searchImageLabel.setBounds(RLX - 32 + RLW / 2, RLY, 64, 64);
		add(searchImageLabel);
		
		searchJobSearchBox = new SearchBox(JobDAO.getInstance().list(), new Dimension(RLW, LH)) {
			private static final long serialVersionUID = 1L;
			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedJob = (Job) searchResultObject;
				searchJobSearchBox.setText(searchResultObject.toString());
				searchJobSearchBox.setEditable(false);
				jobTitleTextField.setText(selectedJob.toString());
				employerTextField.setText(selectedJob.getEmployer().toString());
				clearPanel();
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		searchJobSearchBox.setBounds(RLX, searchImageLabel.getY() + 64 + MHS, RLW, LH);
		add(searchJobSearchBox);
		
		searchJobSearchBox.getPanel().setBounds(RLX, searchJobSearchBox.getY() + searchJobSearchBox.getHeight(), searchJobSearchBox.getWidth(), 0);
		add(searchJobSearchBox.getPanel());
		
		
		lastPaymentsScroll = new JScrollPane(new JTable(new String[][] {}, invoiceTableColumns) {
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
		lastPaymentsScroll.setBounds(RLX, jobTitleTextField.getY(), RLW, 255);
		add(lastPaymentsScroll);
		
		clearPanel();
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
		if(e.getSource() == takePaymentButton) {
			
			Job job;
			String amount;
			
			job = selectedJob;
			amount = amountTextField.getText().replaceAll("\\s+", "");

			if(!decimalControl(amount) || job == null) {
				
				String message;
				message = "Please enter job selection section or format correctly (max 2 floating point)";
				JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
				
			} else {
				
				JTextArea jobTextArea, amountTextArea;
				
				jobTextArea = new JTextArea(job.toString());
				jobTextArea.setEditable(false);
				
				amountTextArea = new JTextArea(amount + " ₺");
				amountTextArea.setEditable(false);
				
				Object[] pane = {
						new JLabel("Job"),
						jobTextArea,
						new JLabel("Amount of payment"),
						amountTextArea
				};
		
				int result = JOptionPane.showOptionDialog(this, pane, "Confirmation", 1, 1, 
						new ImageIcon("src\\icon\\accounting_icon_1_32.png"), new Object[] {"SAVE", "CANCEL"}, "CANCEL");
				
				
				// System.out.println(result);
				// 0 -> SAVE
				// 1 -> CANCEL
				
				if(result == 0) {
					
					Invoice.InvoiceBuilder builder = new Invoice.InvoiceBuilder();
					builder.setId(Integer.MAX_VALUE);
					builder.setJob(selectedJob);
					builder.setAmount(new BigDecimal(amount));
					
					Invoice invoice = null;
					try {
						invoice = builder.build();
					} catch (EntityException e1) {
						System.out.println(e1.getMessage());
					}
					
					if(InvoiceDAO.getInstance().create(invoice)) { 
						JOptionPane.showMessageDialog(this, "Registration successful");
						notifyAllObservers();
					} else {
						JOptionPane.showMessageDialog(this, "Not saved", "DataBase error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
			
		}
		
	}
	
	private void clearPanel() {
		
		amountTextField.setText("");
		amountTextField.setBorder(new LineBorder(Color.white));
		amountLabel.setForeground(defaultColor);
		
		
		if(selectedJob != null) {
			
			List<Invoice> invoiceList = InvoiceDAO.getInstance().list("job_id", selectedJob.getId());
			String[][] tableData = new String[invoiceList.size()][invoiceTableColumns.length];
			int i = 0;
			for(Invoice invoice : invoiceList) {
				tableData[i][0] = invoice.getId() + "";
				tableData[i][1] = NumberFormat.getInstance().format(invoice.getAmount()) + " ₺";
				tableData[i][2] = new SimpleDateFormat("dd.MM.yyyy").format(invoice.getDate()); 
				++i;
			}
			
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).setModel(new DefaultTableModel(tableData, invoiceTableColumns));
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(0).setPreferredWidth(5);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			((JTable)lastPaymentsScroll.getViewport().getComponent(0)).getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
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
	

	@Override
	public void update() {
		JobDAO.getInstance().refresh();
		searchJobSearchBox.setObjectList(JobDAO.getInstance().list());
		clearPanel();
		
	}
	

}
