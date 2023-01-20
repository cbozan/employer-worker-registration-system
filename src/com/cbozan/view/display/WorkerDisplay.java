package com.cbozan.view.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.cbozan.dao.JobDAO;
import com.cbozan.dao.PaymentDAO;
import com.cbozan.dao.WorkDAO;
import com.cbozan.dao.WorkerDAO;
import com.cbozan.dao.WorktypeDAO;
import com.cbozan.entity.Job;
import com.cbozan.entity.Payment;
import com.cbozan.entity.Work;
import com.cbozan.entity.Worker;
import com.cbozan.entity.Worktype;
import com.cbozan.view.component.SearchBox;
import com.cbozan.view.helper.Observer;

public class WorkerDisplay extends JPanel implements Observer{

	private static final long serialVersionUID = -6961272671905633906L;
	
	private final int LLX = 60;
	private final int RLX = 360;
	private final int LLY = 50;
	private final int RLY = 24;
	private final int LLW;
	private final int RLW = 480;
	private final int RLH = 24;
	private final int BS = 20;
	private final int MS = 15;
	private final int WTH = 210;
	private final int FW = 210;
	
	
	private final String[] workTableColumns = {"ID", "İŞ", "İŞÇİ", "ÇALIŞMA ŞEKLİ", "AÇIKLAMA", "TARİH"}; 
	private final String[] workerPaymentTableColumns = {"ID", "İŞ", "İŞÇİ", "ÖDEME ŞEKLİ", "MİKTAR", "TARİH"};
	
	private JLabel workerImageLabel, workerSearchBoxImageLabel;
	private WorkerCard workerCard;
	private SearchBox workerSearchBox;
	private Worker selectedWorker;
	private JLabel workScrollPaneLabel, workerPaymentScrollPaneLabel, workerPaymentCountLabel, workerPaymentTotalLabel;
	private JScrollPane workScrollPane, workerPaymentScrollPane;
	private JLabel workCountLabel, workTableJobFilterLabel, workTableWorktypeFilterLabel, workTableDateFilterLabel;
	private JLabel workerPaymentJobFilterLabel, workerPaymentDateFilterLabel;
	private SearchBox workTableJobFilterSearchBox, workTableWorktypeFilterSearchBox, workerPaymentJobFilterSearchBox;
	private JTextField workTableDateFilterSearchBox, workerPaymentDateFilterSearchBox;
	private Job selectedWorkTableJob, selectedWorkerPaymentJob;
	private Worktype selectedWorktype;
	private JButton removeWorkTableJobFilterButton, removeWorkTableWorktypeFilterButton, removeWorkTableDateFilterButton;
	private JButton removeWorkerPaymentJobFilterButton, removeWorkerPaymentDateFilterButton;
	private String selectedWorkTableDateStrings, selectedWorkerPaymentDateStrings;
	
	
	
	public WorkerDisplay() {
		
		super();
		setLayout(null);
		
		LLW = WorkerCard.rowWidth;
		
		selectedWorker = null;
		selectedWorkTableJob = null;
		selectedWorktype = null;
		selectedWorkTableDateStrings = null;
		
		workerImageLabel = new JLabel(new ImageIcon("src\\icon\\view_worker.png"));
		workerImageLabel.setBounds(LLX + 24, LLY, 128, 128);
		this.add(workerImageLabel);
		
		
		workerCard = new WorkerCard();
		workerCard.setBounds(LLX, workerImageLabel.getY() + workerImageLabel.getHeight() + BS, LLW, workerCard.getHeight());
		this.add(workerCard);
		
		workerSearchBoxImageLabel = new JLabel(new ImageIcon("src\\icon\\search_worker.png"));
		workerSearchBoxImageLabel.setBounds(RLX  + (RLW - 64) / 2, RLY, 64, 64);
		this.add(workerSearchBoxImageLabel);
		
		workerSearchBox = new SearchBox(WorkerDAO.getInstance().list(), new Dimension(RLW, RLH)) {
			private static final long serialVersionUID = 1L;
			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedWorker = (Worker) searchResultObject;
				workerCard.setSelectedWorker((Worker) selectedWorker.clone());
				
				workerSearchBox.setText(selectedWorker.toString());
				workerSearchBox.setEditable(false);
				
				updateWorkTableData();
				updateWorkerPaymentTableData();
				
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		workerSearchBox.setBounds(RLX, workerSearchBoxImageLabel.getY() + workerSearchBoxImageLabel.getHeight(), RLW, RLH);
		this.add(workerSearchBox);
		
		workerSearchBox.getPanel().setBounds(workerSearchBox.getX(), workerSearchBox.getY() + workerSearchBox.getHeight(), RLW, 0);
		this.add(workerSearchBox.getPanel());
		
		
		workScrollPaneLabel = new JLabel("GÜNLÜK ÇALIŞMA TABLOSU");
		workScrollPaneLabel.setOpaque(true);
		workScrollPaneLabel.setBackground(new Color(189, 224, 254));
		workScrollPaneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		workScrollPaneLabel.setBounds(workerSearchBox.getX(), workerSearchBox.getY() + workerSearchBox.getHeight() + 30, RLW, RLH);
		this.add(workScrollPaneLabel);
		
		workScrollPane = new JScrollPane(new JTable(new String[][] {}, workTableColumns) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
			{
				setRowHeight(30);
				setShowVerticalLines(false);
				setShowHorizontalLines(false);
				getColumnModel().getColumn(0).setPreferredWidth(5);
				setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			}
		});
		workScrollPane.setBounds(workScrollPaneLabel.getX(), workScrollPaneLabel.getY() + workScrollPaneLabel.getHeight(), RLW, WTH);
		this.add(workScrollPane);
		
		workCountLabel = new JLabel("0 Kayıt");
		workCountLabel.setBounds(workScrollPane.getX(), workScrollPane.getY() + workScrollPane.getHeight(), RLW, RLH - 8);
		workCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(workCountLabel);
		
		workerPaymentScrollPaneLabel = new JLabel("YAPILAN ÖDEMELER TABLOSU");
		workerPaymentScrollPaneLabel.setOpaque(true);
		workerPaymentScrollPaneLabel.setBackground(workScrollPaneLabel.getBackground());
		workerPaymentScrollPaneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		workerPaymentScrollPaneLabel.setBounds(workCountLabel.getX(), workCountLabel.getY() + workCountLabel.getHeight() + 30, RLW, RLH);
		this.add(workerPaymentScrollPaneLabel);
		
		workerPaymentScrollPane = new JScrollPane(new JTable(new String[][] {}, workerPaymentTableColumns) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
			{
				setRowHeight(30);
				setShowVerticalLines(false);
				setShowHorizontalLines(false);
				getColumnModel().getColumn(0).setPreferredWidth(5);
				setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			}
		});
		workerPaymentScrollPane.setBounds(workerPaymentScrollPaneLabel.getX(), workerPaymentScrollPaneLabel.getY() + workerPaymentScrollPaneLabel.getHeight(), RLW, 120);
		this.add(workerPaymentScrollPane);
		
		
		workerPaymentCountLabel = new JLabel("0 Kayıt");
		workerPaymentCountLabel.setBounds(workerPaymentScrollPane.getX() + RLW / 2, workerPaymentScrollPane.getY() + workerPaymentScrollPane.getHeight(), RLW / 2, RLH - 8);
		workerPaymentCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(workerPaymentCountLabel);
		
		workerPaymentTotalLabel = new JLabel("Toplam 0 ₺");
		workerPaymentTotalLabel.setBounds(workerPaymentScrollPane.getX(), workerPaymentScrollPane.getY() + workerPaymentScrollPane.getHeight(), RLW / 2, RLH - 8);
		workerPaymentTotalLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.add(workerPaymentTotalLabel);
		
		workTableJobFilterLabel = new JLabel("İŞE GÖRE FİLTRELE");
		workTableJobFilterLabel.setForeground(Color.GRAY);
		workTableJobFilterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		workTableJobFilterLabel.setBounds(workScrollPane.getX() + workScrollPane.getWidth() + 5, workScrollPane.getY(), FW, RLH);
		this.add(workTableJobFilterLabel);
		
		workTableJobFilterSearchBox = new SearchBox(JobDAO.getInstance().list(), new Dimension(FW, RLH)) {
			private static final long serialVersionUID = 1L;
			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedWorkTableJob = (Job) searchResultObject;
				workTableJobFilterSearchBox.setText(selectedWorkTableJob.toString());
				workTableJobFilterSearchBox.setEditable(false);
				workTableJobFilterLabel.setForeground(new Color(37, 217, 138));
				removeWorkTableJobFilterButton.setVisible(true);
				updateWorkTableData();
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		workTableJobFilterSearchBox.setBounds(workTableJobFilterLabel.getX(), workTableJobFilterLabel.getY() + workTableJobFilterLabel.getHeight(), FW, RLH);
		this.add(workTableJobFilterSearchBox);
		
		workTableJobFilterSearchBox.getPanel().setBounds(workTableJobFilterSearchBox.getX(), workTableJobFilterSearchBox.getY() + workTableJobFilterSearchBox.getHeight(), workTableJobFilterSearchBox.getWidth(), 0);
		this.add(workTableJobFilterSearchBox.getPanel());
		
		removeWorkTableJobFilterButton = new JButton("FİLTREYİ KALDIR");
		removeWorkTableJobFilterButton.setBounds(workTableJobFilterSearchBox.getX(), workTableJobFilterSearchBox.getY() + workTableJobFilterSearchBox.getHeight(), workTableJobFilterSearchBox.getWidth(), 16);
		removeWorkTableJobFilterButton.setBorderPainted(false);
		removeWorkTableJobFilterButton.setFont(new Font(removeWorkTableJobFilterButton.getFont().getName(), Font.BOLD, 9));
		removeWorkTableJobFilterButton.setForeground(Color.WHITE);
		removeWorkTableJobFilterButton.setFocusable(false);
		removeWorkTableJobFilterButton.setBackground(new Color(159, 11, 40));
		removeWorkTableJobFilterButton.setVisible(false);
		removeWorkTableJobFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeWorkTableJobFilterButton.setVisible(false);
				workTableJobFilterSearchBox.setText("");
				workTableJobFilterLabel.setForeground(Color.GRAY);
				selectedWorkTableJob = null;
				updateWorkTableData();
				
			}
		});
		this.add(removeWorkTableJobFilterButton);
		
		workTableWorktypeFilterLabel = new JLabel("ÇALIŞMA ŞEKLİNE GÖRE FİLTRELE");
		workTableWorktypeFilterLabel.setForeground(Color.GRAY);
		workTableWorktypeFilterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		workTableWorktypeFilterLabel.setBounds(workTableJobFilterLabel.getX(), workTableJobFilterSearchBox.getY() + workTableJobFilterSearchBox.getHeight() + (int)(MS * (1.5f)), FW, RLH);
		this.add(workTableWorktypeFilterLabel);
		
		
		workTableWorktypeFilterSearchBox = new SearchBox(WorktypeDAO.getInstance().list(), new Dimension(FW, RLH)) {
			private static final long serialVersionUID = 1L;
			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedWorktype = (Worktype) searchResultObject;
				workTableWorktypeFilterSearchBox.setText(selectedWorktype.toString());
				workTableWorktypeFilterSearchBox.setEditable(false);
				workTableWorktypeFilterLabel.setForeground(new Color(37, 217, 138));
				removeWorkTableWorktypeFilterButton.setVisible(true);
				updateWorkTableData();
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		workTableWorktypeFilterSearchBox.setBounds(workTableWorktypeFilterLabel.getX(), workTableWorktypeFilterLabel.getY() + workTableWorktypeFilterLabel.getHeight(), FW, RLH);
		this.add(workTableWorktypeFilterSearchBox);
		
		workTableWorktypeFilterSearchBox.getPanel().setBounds(workTableWorktypeFilterSearchBox.getX(), workTableWorktypeFilterSearchBox.getY() + workTableWorktypeFilterSearchBox.getHeight(), workTableWorktypeFilterSearchBox.getWidth(), 0);
		this.add(workTableWorktypeFilterSearchBox.getPanel());
		
		removeWorkTableWorktypeFilterButton = new JButton("FİLTREYİ KALDIR");
		removeWorkTableWorktypeFilterButton.setBounds(workTableWorktypeFilterSearchBox.getX(), workTableWorktypeFilterSearchBox.getY() + workTableWorktypeFilterSearchBox.getHeight(), workTableWorktypeFilterSearchBox.getWidth(), 16);
		removeWorkTableWorktypeFilterButton.setBorderPainted(false);
		removeWorkTableWorktypeFilterButton.setFont(new Font(removeWorkTableWorktypeFilterButton.getFont().getName(), Font.BOLD, 9));
		removeWorkTableWorktypeFilterButton.setForeground(Color.WHITE);
		removeWorkTableWorktypeFilterButton.setFocusable(false);
		removeWorkTableWorktypeFilterButton.setBackground(new Color(159, 11, 40));
		removeWorkTableWorktypeFilterButton.setVisible(false);
		removeWorkTableWorktypeFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeWorkTableWorktypeFilterButton.setVisible(false);
				workTableWorktypeFilterSearchBox.setText("");
				workTableWorktypeFilterLabel.setForeground(Color.GRAY);
				selectedWorktype = null;
				updateWorkTableData();
				
			}
		});
		this.add(removeWorkTableWorktypeFilterButton);
		
		
		
		
		workTableDateFilterLabel = new JLabel("TARİHE GÖRE FİLTRELE");
		workTableDateFilterLabel.setForeground(Color.GRAY);
		workTableDateFilterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		workTableDateFilterLabel.setBounds(workTableWorktypeFilterLabel.getX(), workTableWorktypeFilterSearchBox.getY() + workTableWorktypeFilterSearchBox.getHeight() + (int)(MS * (1.5f)), FW, RLH);
		this.add(workTableDateFilterLabel);
		
		
		workTableDateFilterSearchBox = new JTextField();
		workTableDateFilterSearchBox.setHorizontalAlignment(SwingConstants.CENTER);
		workTableDateFilterSearchBox.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		workTableDateFilterSearchBox.setBounds(workTableDateFilterLabel.getX(), workTableDateFilterLabel.getY() + workTableDateFilterLabel.getHeight(), FW, RLH);
		workTableDateFilterSearchBox.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				workTableDateFilterSearchBox.setEditable(true);
				workTableDateFilterSearchBox.requestFocus();
			}
			public void keyPressed(KeyEvent e) {
				if((e.getKeyChar() >= '0' && e.getKeyChar() <= '9')) {
					
					if(workTableDateFilterSearchBox.getText().length() >= 21) {
						workTableDateFilterSearchBox.setEditable(false);
					} else {
						
						switch(workTableDateFilterSearchBox.getText().length()) {
							case 2:case 5:case 13:case 16:
								workTableDateFilterSearchBox.setText(workTableDateFilterSearchBox.getText() + "/");
								break;
							case 10:
								workTableDateFilterSearchBox.setText(workTableDateFilterSearchBox.getText() + "-");
								break;
						}
						
					}
				} else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE);
				  else {
					workTableDateFilterSearchBox.setEditable(false);
				}
			}
		});
		
		workTableDateFilterSearchBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(workTableDateFilterSearchBox.getText().length() == 10 || workTableDateFilterSearchBox.getText().length() == 21) {
					selectedWorkTableDateStrings = workTableDateFilterSearchBox.getText();
					workTableDateFilterSearchBox.setEditable(false);
					workTableDateFilterLabel.setForeground(new Color(37, 217, 138));
					removeWorkTableDateFilterButton.setVisible(true);
					updateWorkTableData();
				}
			}
		});
		this.add(workTableDateFilterSearchBox);

		
		

		
		removeWorkTableDateFilterButton = new JButton("FİLTREYİ KALDIR");
		removeWorkTableDateFilterButton.setBounds(workTableDateFilterSearchBox.getX(), workTableDateFilterSearchBox.getY() + workTableDateFilterSearchBox.getHeight(), workTableDateFilterSearchBox.getWidth(), 16);
		removeWorkTableDateFilterButton.setBorderPainted(false);
		removeWorkTableDateFilterButton.setFont(new Font(removeWorkTableDateFilterButton.getFont().getName(), Font.BOLD, 9));
		removeWorkTableDateFilterButton.setForeground(Color.WHITE);
		removeWorkTableDateFilterButton.setFocusable(false);
		removeWorkTableDateFilterButton.setBackground(new Color(159, 11, 40));
		removeWorkTableDateFilterButton.setVisible(false);
		removeWorkTableDateFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeWorkTableDateFilterButton.setVisible(false);
				workTableDateFilterSearchBox.setText("");
				workTableDateFilterLabel.setForeground(Color.GRAY);
				selectedWorkTableDateStrings = null;
				updateWorkTableData();
			}
		});
		this.add(removeWorkTableDateFilterButton);
		
		
		
		workerPaymentJobFilterLabel = new JLabel("İŞE GÖRE FİLTRELE");
		workerPaymentJobFilterLabel.setForeground(Color.GRAY);
		workerPaymentJobFilterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		workerPaymentJobFilterLabel.setBounds(workerPaymentScrollPane.getX() + workerPaymentScrollPane.getWidth() + 5, workerPaymentScrollPaneLabel.getY(), FW, RLH);
		this.add(workerPaymentJobFilterLabel);
		
		workerPaymentJobFilterSearchBox = new SearchBox(JobDAO.getInstance().list(), new Dimension(FW, RLH)) {
			private static final long serialVersionUID = 1L;
			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedWorkerPaymentJob = (Job) searchResultObject;
				workerPaymentJobFilterSearchBox.setText(selectedWorkerPaymentJob.toString());
				workerPaymentJobFilterSearchBox.setEditable(false);
				workerPaymentJobFilterLabel.setForeground(new Color(37, 217, 138));
				removeWorkerPaymentJobFilterButton.setVisible(true);
				updateWorkerPaymentTableData();
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		workerPaymentJobFilterSearchBox.setBounds(workerPaymentJobFilterLabel.getX(), workerPaymentJobFilterLabel.getY() + workerPaymentJobFilterLabel.getHeight(), FW, RLH);
		this.add(workerPaymentJobFilterSearchBox);
		
		workerPaymentJobFilterSearchBox.getPanel().setBounds(workerPaymentJobFilterSearchBox.getX(), workerPaymentJobFilterSearchBox.getY() + workerPaymentJobFilterSearchBox.getHeight(), workerPaymentJobFilterSearchBox.getWidth(), 0);
		this.add(workerPaymentJobFilterSearchBox.getPanel());
		
		removeWorkerPaymentJobFilterButton = new JButton("FİLTREYİ KALDIR");
		removeWorkerPaymentJobFilterButton.setBounds(workerPaymentJobFilterSearchBox.getX(), workerPaymentJobFilterSearchBox.getY() + workerPaymentJobFilterSearchBox.getHeight(), workerPaymentJobFilterSearchBox.getWidth(), 16);
		removeWorkerPaymentJobFilterButton.setBorderPainted(false);
		removeWorkerPaymentJobFilterButton.setFont(new Font(removeWorkTableJobFilterButton.getFont().getName(), Font.BOLD, 9));
		removeWorkerPaymentJobFilterButton.setForeground(Color.WHITE);
		removeWorkerPaymentJobFilterButton.setFocusable(false);
		removeWorkerPaymentJobFilterButton.setBackground(new Color(159, 11, 40));
		removeWorkerPaymentJobFilterButton.setVisible(false);
		removeWorkerPaymentJobFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeWorkerPaymentJobFilterButton.setVisible(false);
				workerPaymentJobFilterSearchBox.setText("");
				workerPaymentJobFilterLabel.setForeground(Color.GRAY);
				selectedWorkerPaymentJob = null;
				updateWorkTableData();
				
			}
		});
		this.add(removeWorkerPaymentJobFilterButton);
		
		
		
		workerPaymentDateFilterLabel = new JLabel("TARİHE GÖRE FİLTRELE");
		workerPaymentDateFilterLabel.setForeground(Color.GRAY);
		workerPaymentDateFilterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		workerPaymentDateFilterLabel.setBounds(workerPaymentJobFilterLabel.getX(), workerPaymentJobFilterSearchBox.getY() + workerPaymentJobFilterSearchBox.getHeight() + (int)(MS * (1.5f)), FW, RLH);
		this.add(workerPaymentDateFilterLabel);
		
		
		workerPaymentDateFilterSearchBox = new JTextField();
		workerPaymentDateFilterSearchBox.setHorizontalAlignment(SwingConstants.CENTER);
		workerPaymentDateFilterSearchBox.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		workerPaymentDateFilterSearchBox.setBounds(workerPaymentDateFilterLabel.getX(), workerPaymentDateFilterLabel.getY() + workerPaymentDateFilterLabel.getHeight(), FW, RLH);
		workerPaymentDateFilterSearchBox.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				workerPaymentDateFilterSearchBox.setEditable(true);
				workerPaymentDateFilterSearchBox.requestFocus();
			}
			public void keyPressed(KeyEvent e) {
				if((e.getKeyChar() >= '0' && e.getKeyChar() <= '9')) {
					
					if(workerPaymentDateFilterSearchBox.getText().length() >= 21) {
						workerPaymentDateFilterSearchBox.setEditable(false);
					} else {
						
						switch(workerPaymentDateFilterSearchBox.getText().length()) {
							case 2:case 5:case 13:case 16:
								workerPaymentDateFilterSearchBox.setText(workerPaymentDateFilterSearchBox.getText() + "/");
								break;
							case 10:
								workerPaymentDateFilterSearchBox.setText(workerPaymentDateFilterSearchBox.getText() + "-");
								break;
						}
						
					}
				} else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE);
				  else {
					  workerPaymentDateFilterSearchBox.setEditable(false);
				}
			}
		});
		
		workerPaymentDateFilterSearchBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(workerPaymentDateFilterSearchBox.getText().length() == 10 || workerPaymentDateFilterSearchBox.getText().length() == 21) {
					selectedWorkerPaymentDateStrings = workerPaymentDateFilterSearchBox.getText();
					workerPaymentDateFilterSearchBox.setEditable(false);
					workerPaymentDateFilterLabel.setForeground(new Color(37, 217, 138));
					removeWorkerPaymentDateFilterButton.setVisible(true);
					updateWorkerPaymentTableData();
				}
			}
		});
		this.add(workerPaymentDateFilterSearchBox);

		
		

		
		removeWorkerPaymentDateFilterButton = new JButton("FİLTREYİ KALDIR");
		removeWorkerPaymentDateFilterButton.setBounds(workerPaymentDateFilterSearchBox.getX(), workerPaymentDateFilterSearchBox.getY() + workerPaymentDateFilterSearchBox.getHeight(), workerPaymentDateFilterSearchBox.getWidth(), 16);
		removeWorkerPaymentDateFilterButton.setBorderPainted(false);
		removeWorkerPaymentDateFilterButton.setFont(new Font(removeWorkerPaymentDateFilterButton.getFont().getName(), Font.BOLD, 9));
		removeWorkerPaymentDateFilterButton.setForeground(Color.WHITE);
		removeWorkerPaymentDateFilterButton.setFocusable(false);
		removeWorkerPaymentDateFilterButton.setBackground(new Color(159, 11, 40));
		removeWorkerPaymentDateFilterButton.setVisible(false);
		removeWorkerPaymentDateFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeWorkerPaymentDateFilterButton.setVisible(false);
				workerPaymentDateFilterSearchBox.setText("");
				workerPaymentDateFilterLabel.setForeground(Color.GRAY);
				selectedWorkerPaymentDateStrings = null;
				updateWorkerPaymentTableData();
			}
		});
		this.add(removeWorkerPaymentDateFilterButton);
		
		clearPanel();
		
	}
	
	public void clearPanel() {
		workerSearchBox.setText("");
		selectedWorker = null;
		workerCard.setSelectedWorker(null);
		workerCard.reload();
		
	}

	@Override
	public void update() {
		WorkDAO.getInstance().refresh();
		PaymentDAO.getInstance().refresh();
		WorkerDAO.getInstance().refresh();
		workerSearchBox.setObjectList(WorkerDAO.getInstance().list());
		selectedWorker = workerCard.getSelectedWorker();
		workerSearchBox.setText(selectedWorker == null ? "" : selectedWorker.toString());
		updateWorkTableData();
		updateWorkerPaymentTableData();
		workerCard.reload();
	}
	
	private void updateWorkTableData() {
		
		if(selectedWorker != null) {
			
			List<Work> workList;
			
			if(selectedWorkTableDateStrings != null) {
				workList = WorkDAO.getInstance().list(selectedWorker, selectedWorkTableDateStrings);
			} else {
				workList = WorkDAO.getInstance().list(selectedWorker);
			}
			
			filterJob(workList);
			filterWorktype(workList);
			
			String[][] tableData = new String[workList.size()][workTableColumns.length];
			
			int i = 0;
			for(Work w : workList) {
				tableData[i][0] = w.getId() + "";
				tableData[i][1] = w.getJob().toString();
				tableData[i][2] = w.getWorker().toString();
				tableData[i][3] = w.getWorktype().toString();
				tableData[i][4] = w.getDescription();
				tableData[i][5] = new SimpleDateFormat("dd.MM.yyyy").format(w.getDate());
				++i;
			}
			
			((JTable)workScrollPane.getViewport().getComponent(0)).setModel(new DefaultTableModel(tableData, workTableColumns));
			((JTable)workScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(0).setPreferredWidth(5);
			((JTable)workScrollPane.getViewport().getComponent(0)).setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
			((JTable)workScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			((JTable)workScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
			
			workCountLabel.setText(workList.size() + " Kayıt");
			
		}
	}
	
	public void updateWorkerPaymentTableData() {
		
		if(selectedWorker != null) {
			
			List<Payment> paymentList;
			
			if(selectedWorkerPaymentDateStrings != null) {
				paymentList = PaymentDAO.getInstance().list(selectedWorker, selectedWorkerPaymentDateStrings);
			} else {
				paymentList = PaymentDAO.getInstance().list(selectedWorker);
			}
			
			filterPaymentJob(paymentList);
			
			String[][] tableData = new String[paymentList.size()][workerPaymentTableColumns.length];
			BigDecimal totalAmount = new BigDecimal("0.00");
			int i = 0;
			for(Payment p : paymentList) {
				tableData[i][0] = p.getId() + "";
				tableData[i][1] = p.getJob().toString();
				tableData[i][2] = p.getWorker().toString();
				tableData[i][3] = p.getPaytype().toString();
				tableData[i][4] = p.getAmount() + " ₺";
				tableData[i][5] = new SimpleDateFormat("dd.MM.yyyy").format(p.getDate());
				totalAmount = totalAmount.add(p.getAmount());
				++i;
			}
			
			((JTable)workerPaymentScrollPane.getViewport().getComponent(0)).setModel(new DefaultTableModel(tableData, workerPaymentTableColumns));
			((JTable)workerPaymentScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(0).setPreferredWidth(5);
			((JTable)workerPaymentScrollPane.getViewport().getComponent(0)).setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
			((JTable)workerPaymentScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			((JTable)workerPaymentScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			((JTable)workerPaymentScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
			((JTable)workerPaymentScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
			
			workerPaymentTotalLabel.setText("Toplam " + totalAmount + " ₺");
			workerPaymentCountLabel.setText(paymentList.size() + " Kayıt");
			
		}
		
	}
	
	public void filterPaymentJob(List<Payment> paymentList) {
		if(selectedWorkerPaymentJob == null)
			return;
		
		Iterator<Payment> payment = paymentList.iterator();
		Payment paymentItem;
		while(payment.hasNext()) {
			paymentItem = payment.next();
			if(paymentItem.getJob().getId() != selectedWorkerPaymentJob.getId()) {
				payment.remove();
			}
		}
	}
	
	public void filterJob(List<Work> workList) {
		if(selectedWorkTableJob == null) {
			return;
		}
		
		Iterator<Work> work = workList.iterator();
		Work workItem;
		while(work.hasNext()) {
			workItem = work.next();
			if(workItem.getJob().getId() != selectedWorkTableJob.getId()) {
				work.remove();
			}
		}
		
	}
	
	public void filterWorktype(List<Work> workList) {
		if(selectedWorktype == null) {
			return;
		}
		
		Iterator<Work> work = workList.iterator();
		Work workItem;
		while(work.hasNext()) {
			workItem = work.next();
			if(workItem.getWorktype().getId() != selectedWorktype.getId()) {
				work.remove();
			}
		}
	}
	
}
