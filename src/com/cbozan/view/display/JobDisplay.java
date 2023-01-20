package com.cbozan.view.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.cbozan.dao.JobDAO;
import com.cbozan.dao.WorkgroupDAO;
import com.cbozan.entity.Job;
import com.cbozan.entity.Workgroup;
import com.cbozan.view.component.SearchBox;
import com.cbozan.view.helper.Observer;

public class JobDisplay extends JPanel implements Observer{

private static final long serialVersionUID = -7816939112811838345L;
	
	private final int LLX = 60;
	private final int RLX = 360;
	private final int LLY = 50;
	private final int RLY = 24;
	private final int LLW;
	private final int RLW = 480;
	private final int RLH = 24;
	private final int BS = 20;
	private final int MS = 15;
	private final int WTH = 180;
	private final int FW = 210;
	
	private final String[] jobWorkTableColumns = {"ID", "İŞ BAŞLIĞI", "ÇALIŞMA ŞEKLİ", "KİŞİ SAYISI", "AÇIKLAMA", "TARİH"};
	
	private JLabel jobImageLabel, jobSearchBoxImageLabel;
	private JLabel jobWorkScrollPaneLabel, jobWorkCountLabel, jobWorkTotalCountLabel;
	private JScrollPane jobWorkScrollPane;
	private SearchBox jobSearchBox;
	private JobCard jobCard;
	private Job selectedJob;
	
	public JobDisplay(){
		
		super();
		setLayout(null);
		
		LLW = jobCard.rowWidth;
		selectedJob = null;
		
		jobImageLabel = new JLabel(new ImageIcon("src\\icon\\new_job.png"));
		jobImageLabel.setBounds(LLX + 24, LLY, 128, 128);
		this.add(jobImageLabel);
		
		jobCard = new JobCard();
		jobCard.setBounds(LLX, jobImageLabel.getY() + jobImageLabel.getHeight() + BS, LLW, jobCard.getHeight());
		this.add(jobCard);
		
		jobSearchBoxImageLabel = new JLabel(new ImageIcon("src\\icon\\search_worker.png"));
		jobSearchBoxImageLabel.setBounds(RLX + (RLW - 64) / 2, RLY, 64, 64);
		this.add(jobSearchBoxImageLabel);
		
		jobSearchBox = new SearchBox(JobDAO.getInstance().list(), new Dimension(RLW, RLH)) {
			private static final long serialVersionUID = -3041644007866138549L;
			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedJob = (Job) searchResultObject;
				jobCard.setSelectedJob(selectedJob.clone());
				
				jobSearchBox.setText(selectedJob.toString());
				jobSearchBox.setEditable(false);
				
				updateWorkgroupTableData();
				updateJobPaymentTableData();
				
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		
		
		jobSearchBox.setBounds(RLX, jobSearchBoxImageLabel.getY() + jobSearchBoxImageLabel.getHeight(), RLW, RLH);
		this.add(jobSearchBox);
		
		jobSearchBox.getPanel().setBounds(jobSearchBox.getX(), jobSearchBox.getY() + jobSearchBox.getHeight(), RLW, 0);
		this.add(jobSearchBox.getPanel());
		
		
		jobWorkScrollPaneLabel = new JLabel("İŞ GÜNLÜK KAYIT TABLOSU");
		jobWorkScrollPaneLabel.setOpaque(true);
		jobWorkScrollPaneLabel.setBackground(new Color(189, 224, 254));
		jobWorkScrollPaneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		jobWorkScrollPaneLabel.setBounds(jobSearchBox.getX(), jobSearchBox.getY() + jobSearchBox.getHeight() + 30, RLW, RLH);
		this.add(jobWorkScrollPaneLabel);
		
		jobWorkScrollPane = new JScrollPane(new JTable(new String[][] {}, jobWorkTableColumns) {
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
		jobWorkScrollPane.setBounds(jobWorkScrollPaneLabel.getX(), jobWorkScrollPaneLabel.getY() + jobWorkScrollPaneLabel.getHeight(), RLW, WTH);
		this.add(jobWorkScrollPane);
		
		
		jobWorkTotalCountLabel = new JLabel("Toplam 0 yevmiye");
		jobWorkTotalCountLabel.setBounds(jobWorkScrollPane.getX(), jobWorkScrollPane.getY() + jobWorkScrollPane.getHeight(), RLW / 2 - 1, RLH - 8);
		jobWorkTotalCountLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.add(jobWorkTotalCountLabel);
		
		
		jobWorkCountLabel = new JLabel("0 Kayıt");
		jobWorkCountLabel.setBounds(jobWorkTotalCountLabel.getX() + jobWorkTotalCountLabel.getWidth() + 1, jobWorkTotalCountLabel.getY(), RLW / 2, RLH - 8);
		jobWorkCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(jobWorkCountLabel);
		
	}
	
	public void updateWorkgroupTableData() {
		
		if(selectedJob != null) {
			
			List<Workgroup> workgroupList = WorkgroupDAO.getInstance().list(selectedJob);
			
			String[][] tableData = new String[workgroupList.size()][jobWorkTableColumns.length];
			
			int i = 0;
			int totalCount = 0;
			for(Workgroup w : workgroupList) {
				
				tableData[i][0] = w.getId() + "";
				tableData[i][1] = w.getJob().toString();
				tableData[i][2] = w.getWorktype().toString();
				tableData[i][3] = w.getWorkCount() + "";
				tableData[i][4] = w.getDescription();
				tableData[i][5] = new SimpleDateFormat("dd.MM.yyyy").format(w.getDate());
				
				totalCount += w.getWorkCount();
				++i;	
			}
			
			((JTable)jobWorkScrollPane.getViewport().getComponent(0)).setModel(new DefaultTableModel(tableData, jobWorkTableColumns));
			((JTable)jobWorkScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(0).setPreferredWidth(5);
			((JTable)jobWorkScrollPane.getViewport().getComponent(0)).setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			((JTable)jobWorkScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			((JTable)jobWorkScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			((JTable)jobWorkScrollPane.getViewport().getComponent(0)).getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
			
			//jobWorkCount 
			jobWorkTotalCountLabel.setText("Toplam " + totalCount + " yevmiye");
			jobWorkCountLabel.setText(workgroupList.size() + " Kayıt");
		}
		
	}
	
	public void updateJobPaymentTableData() {
		
	}
	
	
	
	@Override
	public void update() {
		
	}

	
}
