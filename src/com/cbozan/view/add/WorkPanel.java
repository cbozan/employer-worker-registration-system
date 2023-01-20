package com.cbozan.view.add;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.cbozan.dao.JobDAO;
import com.cbozan.dao.WorkDAO;
import com.cbozan.dao.WorkerDAO;
import com.cbozan.dao.WorkgroupDAO;
import com.cbozan.dao.WorktypeDAO;
import com.cbozan.entity.Job;
import com.cbozan.entity.Work;
import com.cbozan.entity.Worker;
import com.cbozan.entity.Workgroup;
import com.cbozan.entity.Worktype;
import com.cbozan.exception.EntityException;
import com.cbozan.view.component.SearchBox;
import com.cbozan.view.component.TextArea;
import com.cbozan.view.helper.Observer;


public class WorkPanel extends JPanel implements Observer, ActionListener{
	
	
	private static final long serialVersionUID = 5937069706644528838L;
	private final List<Observer> observers;

	/*
	 * Left label x position
	 */
	private final int LLX = 100;
	
	/*
	 * Right label x position
	 */
	private final int RLX = 550;
	
	/*
	 * Left Label y positon
	 */
	private final int LLY = 250;
	
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
	private final int RLW = 430;
	
	/*
	 * Label height
	 */
	private final int LH = 25;
	
	/*
	 * Label combobox vertical space
	 */
	private final int LCVS = 5;
	
	private JLabel imageLabel, searchWorkerImageLabel;
	private JLabel jobLabel, worktypeLabel, descriptionLabel, searchWorkerLabel;
	private SearchBox searchWorkerSearchBox, searchJobSearchBox;
	//private JPanel searchResultPanel;
	private JComboBox<Worktype> worktypeComboBox;
	private TextArea descriptionTextArea;
	private JButton saveButton, removeSelectedButton;
	
	private Job selectedJob;
	private DefaultListModel<Worker> selectedWorkerDefaultListModel;
	private JList<Worker> selectedWorkerList;
	private JLabel selectedWorkerListLabel, selectedInfoTextLabel, selectedInfoCountLabel;
	
	public WorkPanel() {
		super();
		setLayout(null);

		observers = new ArrayList<>();
		subscribe(this);
		
		
		selectedWorkerDefaultListModel = new DefaultListModel<>();
		
		imageLabel = new JLabel(new ImageIcon("src\\icon\\add_work.png"));
		imageLabel.setBounds(LLX, 30, 128, 128);
		add(imageLabel);
		
		
		jobLabel = new JLabel("Job selection");
		jobLabel.setBounds(LLX, LLY, LLW, LH);
		add(jobLabel);
		
		searchJobSearchBox = new SearchBox(JobDAO.getInstance().list(), new Dimension(LLW, LH)) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedJob = (Job) searchResultObject;
				searchJobSearchBox.setText(searchResultObject.toString());
				searchJobSearchBox.setEditable(false);
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		searchJobSearchBox.setBounds(LLX, jobLabel.getY() + jobLabel.getHeight() + LCVS, LLW, LH);
		add(searchJobSearchBox);
		
		searchJobSearchBox.getPanel().setBounds(LLX, searchJobSearchBox.getY() + searchJobSearchBox.getHeight(), searchJobSearchBox.getWidth(), 0);
		this.add(searchJobSearchBox.getPanel());
		
		worktypeLabel = new JLabel("Work type");
		worktypeLabel.setBounds(LLX, searchJobSearchBox.getY() + searchJobSearchBox.getHeight() + LCVS * 3, LLW, LH);
		add(worktypeLabel);
		
		worktypeComboBox = new JComboBox<Worktype>();
		worktypeComboBox.setBounds(LLX, worktypeLabel.getY() + worktypeLabel.getHeight() + LCVS, LLW, LH);
		add(worktypeComboBox);
		
		descriptionLabel = new JLabel("Description");
		descriptionLabel.setBounds(LLX, worktypeComboBox.getY() + worktypeComboBox.getHeight() + LCVS * 3, LLW, LH);
		add(descriptionLabel);
		
		
		
		descriptionTextArea = new TextArea();
		descriptionTextArea.setBounds(LLX, descriptionLabel.getY() + descriptionLabel.getHeight() + LCVS, LLW, LH * 3);
		add(descriptionTextArea);
		
		
		searchWorkerImageLabel = new JLabel(new ImageIcon("src\\icon\\search_worker.png"));
		searchWorkerImageLabel.setBounds(RLX - 32 + RLW / 2, RLY, 64, 64);
		add(searchWorkerImageLabel);
		
		searchWorkerLabel = new JLabel("Search worker");
		searchWorkerLabel.setBounds(RLX, searchWorkerImageLabel.getY() + 64 + 10, RLW, LH);
		add(searchWorkerLabel);
		
		
		searchWorkerSearchBox = new SearchBox(new ArrayList<>(), new Dimension(RLW, LH)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedWorkerDefaultListModel.addElement((Worker) searchResultObject);
				getObjectList().remove(searchResultObject);
				this.setText("");
				selectedInfoCountLabel.setText(selectedWorkerDefaultListModel.size() + " person");
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		searchWorkerSearchBox.setBounds(RLX, searchWorkerLabel.getY() + LH + LCVS, RLW, LH);
		add(searchWorkerSearchBox);
		
		searchWorkerSearchBox.getPanel().setBounds(RLX, searchWorkerSearchBox.getY() + searchWorkerSearchBox.getHeight(), searchWorkerSearchBox.getWidth(), 0);
		this.add(searchWorkerSearchBox.getPanel());
		
		selectedWorkerListLabel = new JLabel("Added workers");
		selectedWorkerListLabel.setBounds(RLX, LLY, RLW - 70, LH);
		add(selectedWorkerListLabel);
		
		selectedWorkerList = new JList<Worker>(selectedWorkerDefaultListModel);
		selectedWorkerList.setCellRenderer(new DefaultListCellRenderer() {
		
			private static final long serialVersionUID = 1L;

			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
                listCellRendererComponent.setBorder(null);
                return listCellRendererComponent;
            }
		});
		selectedWorkerList.setForeground(Color.GRAY);
		selectedWorkerList.setSelectionForeground(new Color(0, 180, 0));
		selectedWorkerList.setFixedCellHeight(30);
		selectedWorkerList.setBounds(RLX, selectedWorkerListLabel.getY() + selectedWorkerListLabel.getHeight(), RLW, 185);
		add(selectedWorkerList);
		
		removeSelectedButton = new JButton("DELETE");
		removeSelectedButton.setFocusPainted(false);
		removeSelectedButton.setBorder(searchWorkerSearchBox.getBorder());
		removeSelectedButton.setBackground(Color.red);
		removeSelectedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedWorkerList.getSelectedValue() != null) {
					Object obj = selectedWorkerList.getSelectedValue();
					selectedWorkerDefaultListModel.removeElement(obj);
					searchWorkerSearchBox.getObjectList().add(obj);
					selectedInfoCountLabel.setText(selectedWorkerDefaultListModel.size() + " kişi");
				}
			}
		});
		removeSelectedButton.setForeground(Color.white);
		removeSelectedButton.setBounds(searchWorkerSearchBox.getX() + searchWorkerSearchBox.getWidth() - 69, selectedWorkerList.getY() + selectedWorkerList.getHeight()+ 5, 68, LH + 5);
		add(removeSelectedButton);
		
		selectedInfoTextLabel = new JLabel("Selected : ");
		//selectedInfoTextLabel.setForeground(new Color(210, 89, 85));
		selectedInfoTextLabel.setBounds(RLX, removeSelectedButton.getY(), 60, LH + 5);
		add(selectedInfoTextLabel);
		
		selectedInfoCountLabel = new JLabel(selectedWorkerDefaultListModel.size() + " person");
		selectedInfoCountLabel.setForeground(new Color(0, 180, 0));
		selectedInfoCountLabel.setBounds(RLX + selectedInfoTextLabel.getWidth(), selectedInfoTextLabel.getY(), RLW - 60 - 68, LH + 5);
		add(selectedInfoCountLabel);
		
		saveButton = new JButton("SAVE");
		saveButton.setBounds(removeSelectedButton.getX() - 100, removeSelectedButton.getY() + 80, 168, 30);
		saveButton.addActionListener(this);
		add(saveButton);
		
		update();
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getSource() == saveButton) {
			
			Job job;
			List<Worker> workers = new ArrayList<>();
			String workersText = "";
			Worktype worktype;
			String description;
			
			job = selectedJob;
			
			for(int i = 0; i < selectedWorkerDefaultListModel.size(); i++) {
				workersText += (i + 1) + " - " + selectedWorkerDefaultListModel.get(i) + "\n";
				workers.add(selectedWorkerDefaultListModel.get(i));
			}
			
			worktype = (Worktype) worktypeComboBox.getSelectedItem();
			description = ((JTextArea)descriptionTextArea.getViewport().getComponent(0)).getText().trim().toUpperCase();
			
			if(job == null || workers.size() < 1 || worktype == null) {
				
				String message = "fill/select in the required fields";
				JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
				
			} else {
				
				JTextArea jobTextArea, workersTextArea, worktypeTextArea, descriptionTextArea, workerCountTextArea;
				
				jobTextArea = new JTextArea(job.toString());
				jobTextArea.setEditable(false);
				
				workersTextArea = new JTextArea(workersText);
				workersTextArea.setEditable(false);
				
				worktypeTextArea = new JTextArea(worktype.toString());
				worktypeTextArea.setEditable(false);
				
				workerCountTextArea = new JTextArea(" " + workers.size() + " person");
				workerCountTextArea.setEditable(false);
				
				descriptionTextArea = new JTextArea(description);
				descriptionTextArea.setEditable(false);
				
				Object[] pane = {
					new JLabel("Work"),
					jobTextArea,
					new JLabel("Work type"),
					worktypeTextArea,
					new JLabel("Workers"),
					new JScrollPane(workersTextArea) {
						private static final long serialVersionUID = 1L;
						public Dimension getPreferredSize() {
							return new Dimension(240, workers.size() * 30 > 200 ? 200 : workers.size() * 30);
						}
					},
					new JLabel("Worker count"),
					workerCountTextArea,
					new JLabel("Description"),
					new JScrollPane(descriptionTextArea) {
						private static final long serialVersionUID = 1L;
						public Dimension getPreferredSize() {
							return new Dimension(240, 80);
						}
					}
				};
				
				int result = JOptionPane.showOptionDialog(this, pane, "Confirmation", 1, 1, 
						new ImageIcon("src\\icon\\accounting_icon_1_32.png"), new Object[] {"SAVE", "CANCEL"}, "CANCEL");
				if(result == 0) {
					
					Workgroup.WorkgroupBuilder workgroupBuilder = new Workgroup.WorkgroupBuilder();
					Workgroup workgroup = null;
					
					workgroupBuilder.setId(Integer.MAX_VALUE);
					workgroupBuilder.setJob(job);
					workgroupBuilder.setWorktype(worktype);
					workgroupBuilder.setWorkCount(workers.size());
					workgroupBuilder.setDescription(description);
					
					
					try {
						workgroup = workgroupBuilder.build();
					} catch (EntityException e2) {
						System.out.println(e2.getMessage());
					}
					
					List<Worker> failedWorkerList = new ArrayList<Worker>();
					
					if(WorkgroupDAO.getInstance().create(workgroup)) {
						Work.WorkBuilder builder = new Work.WorkBuilder();
						Work work = null;
						builder.setId(Integer.MAX_VALUE);
						builder.setJob(job);
						builder.setWorktype(worktype);
						builder.setWorkgroup(WorkgroupDAO.getInstance().getLastAdded());
						builder.setDescription(description);
						
						for(Worker worker : workers) {
							
							builder.setWorker(worker);
							
							try {
								work = builder.build();
							} catch (EntityException e1) {
								System.out.println(e1.getMessage());
							}
							
							if(!WorkDAO.getInstance().create(work))
								failedWorkerList.add(worker);

						}
						
						if(failedWorkerList.size() == 0) { 
							JOptionPane.showMessageDialog(this, "Registraion Successful");
							notifyAllObservers();
						} else {
							String message = "";
							for(Worker worker2 : failedWorkerList)
								message += worker2.toString() + "\n";
							
							JScrollPane scroll = new JScrollPane(new JTextArea(message)) {
								
								private static final long serialVersionUID = 1L;

								@Override
								public Dimension getPreferredSize() {
									return new Dimension(240, (failedWorkerList.size() + 2) * 30 > 200 ? 200 : failedWorkerList.size() * 30);
								}
							};
							
							JOptionPane.showMessageDialog(this, new Object[] {new JLabel("Not saved : "), scroll}, "Database error", JOptionPane.ERROR_MESSAGE);
						}
						
					} else {
						
						JOptionPane.showMessageDialog(this, "Work group and workers not saved", "Database error", JOptionPane.ERROR_MESSAGE);
						
					}
					
					
				}
				
			}
				
		}
		
	}

	private void clearPanel() {
		
		((JTextArea)descriptionTextArea.getViewport().getComponent(0)).setText("");
		searchWorkerSearchBox.setText("");
		searchJobSearchBox.setText("");
		searchWorkerSearchBox.getPanel().setVisible(false);
		selectedWorkerDefaultListModel.clear();
		selectedInfoCountLabel.setText("0 person");
		
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
		clearPanel();
		
		searchWorkerSearchBox.setObjectList(WorkerDAO.getInstance().list());
		searchJobSearchBox.setObjectList(JobDAO.getInstance().list());
		worktypeComboBox.setModel(new DefaultComboBoxModel<>(WorktypeDAO.getInstance().list().toArray(new Worktype[0])));
		
	}
	
}




