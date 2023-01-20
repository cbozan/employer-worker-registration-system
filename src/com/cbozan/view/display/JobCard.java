package com.cbozan.view.display;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.cbozan.dao.EmployerDAO;
import com.cbozan.dao.JobDAO;
import com.cbozan.dao.PriceDAO;
import com.cbozan.entity.Employer;
import com.cbozan.entity.Job;
import com.cbozan.entity.Price;
import com.cbozan.exception.EntityException;
import com.cbozan.view.component.RecordTextField;
import com.cbozan.view.component.SearchBox;
import com.cbozan.view.component.TextArea;
import com.cbozan.view.helper.Observer;



public class JobCard extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	public static final int rowWidth = 240;
	public static final int rowHeight = 24;
	private final int BW = 40;
	private final int BS = 5;
	private final String INIT_TEXT = "SEARCH JOB";
	//private int VSS = 5;
	private int VMS = 10;
	private int VBS = 15;
	
	
	private JLabel titleLabel, employerLabel, priceLabel, descriptionLabel;
	private RecordTextField titleTextField;
	private SearchBox employerSearchBox, priceSearchBox;
	private TextArea descriptionTextArea;
	private JButton updateButton;
	private Job selectedJob;
	
	
	public JobCard() {
		super();
		setLayout(null);
		
		titleLabel = new JLabel("Job Title");
		titleLabel.setBounds(0, 0, rowWidth, rowHeight);
		addHeight(titleLabel.getHeight());
		this.add(titleLabel);
		
		titleTextField = new RecordTextField(RecordTextField.REQUIRED_TEXT);
		titleTextField.setText(INIT_TEXT);
		titleTextField.setHorizontalAlignment(SwingConstants.CENTER);
		titleTextField.setEditable(false);
		titleTextField.setBounds(titleLabel.getX(), titleLabel.getY() + titleLabel.getHeight(), rowWidth - BW - BS, rowHeight);
		addHeight(titleTextField.getHeight());
		this.add(titleTextField);
		this.add(addEditButton(titleTextField, null));
		
		
		
		employerLabel = new JLabel("Employer");
		employerLabel.setBounds(titleTextField.getX(), titleTextField.getY() + titleTextField.getHeight() + VMS, rowWidth, rowHeight);
		addHeight(employerLabel.getHeight() + VMS);
		this.add(employerLabel);
		
		employerSearchBox = new SearchBox(EmployerDAO.getInstance().list(), new Dimension(rowWidth - BW - BS, rowHeight)) {
			private static final long serialVersionUID = 1L;
			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				if(selectedJob != null) {
					try {
						selectedJob.setEmployer((Employer)searchResultObject);
						if(employerSearchBox.getSelectedObject() != null)
							employerSearchBox.setText(employerSearchBox.getSelectedObject().toString());
					} catch (EntityException e1) {
						e1.printStackTrace();
					}
				}
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
			
		};
		employerSearchBox.setText(INIT_TEXT);
		employerSearchBox.setEditable(false);
		employerSearchBox.setBounds(employerLabel.getX(), employerLabel.getY() + employerLabel.getHeight(), rowWidth - BW - BS, rowHeight);
		addHeight(employerSearchBox.getHeight());
		this.add(employerSearchBox);
		this.add(addEditButton(employerSearchBox, null));
		
		employerSearchBox.getPanel().setBounds(employerSearchBox.getX(), employerSearchBox.getY() + employerSearchBox.getHeight(), rowWidth - BW - BS, 0);
		this.add(employerSearchBox.getPanel());
		
		
		priceLabel = new JLabel("Price");
		priceLabel.setBounds(employerSearchBox.getX(), employerSearchBox.getY() + employerSearchBox.getHeight() + VMS, rowWidth, rowHeight);
		addHeight(priceLabel.getHeight() + VMS);
		this.add(priceLabel);
		
		priceSearchBox = new SearchBox(PriceDAO.getInstance().list(), new Dimension(rowWidth - BW - BS, rowHeight)) {
			private static final long serialVersionUID = 1L;
			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				if(selectedJob != null) {
					try {
						selectedJob.setPrice((Price)searchResultObject);
						if(priceSearchBox.getSelectedObject() != null)
							priceSearchBox.setText(priceSearchBox.getSelectedObject().toString());
					} catch (EntityException e1) {
						e1.printStackTrace();
					}
				}
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
			
		};
		priceSearchBox.setText(INIT_TEXT);
		priceSearchBox.setEditable(false);
		priceSearchBox.setBounds(priceLabel.getX(), priceLabel.getY() + priceLabel.getHeight(), rowWidth - BW - BS, rowHeight);
		addHeight(priceSearchBox.getHeight());
		this.add(priceSearchBox);
		this.add(addEditButton(priceSearchBox, null));
		
		priceSearchBox.getPanel().setBounds(priceSearchBox.getX(), priceSearchBox.getY() + priceSearchBox.getHeight(), rowWidth - BW - BS, 0);
		this.add(priceSearchBox.getPanel());
		
		descriptionLabel = new JLabel("Description");
		descriptionLabel.setBounds(priceSearchBox.getX(), priceSearchBox.getY() + priceSearchBox.getHeight() + VMS, rowWidth, rowHeight);
		addHeight(descriptionLabel.getHeight() + VMS);
		this.add(descriptionLabel);
		
		descriptionTextArea = new TextArea();
		descriptionTextArea.setText(INIT_TEXT);
		descriptionTextArea.setEditable(false);
		descriptionTextArea.setBounds(descriptionLabel.getX(), descriptionLabel.getY() + descriptionLabel.getHeight(), rowWidth - BW - BS, rowHeight * 3);
		addHeight(descriptionTextArea.getHeight());
		this.add(descriptionTextArea);
		this.add(new JButton() {
			private static final long serialVersionUID = 1L;
			{
				setContentAreaFilled(false);
				setFocusable(false);
				setBorderPainted(false);
				setIcon(new ImageIcon("src\\icon\\text_edit.png"));
				setBounds(descriptionTextArea.getX() + descriptionTextArea.getWidth() + BS, descriptionTextArea.getY() + (descriptionTextArea.getHeight() - rowHeight) / 2 , BW, rowHeight);
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if(descriptionTextArea.isEditable()) {
							((JButton)e.getSource()).setIcon(new ImageIcon("src\\icon\\text_edit.png"));
							descriptionTextArea.setEditable(false);
							requestFocusInWindow();
						} else if(!descriptionTextArea.getText().equals(INIT_TEXT)) {
								descriptionTextArea.setEditable(true);
								((JButton)e.getSource()).setIcon(new ImageIcon("src\\icon\\check.png"));
							
						}
					}
				});
			}
			});
		
		
		
		updateButton = new JButton("Update");
		updateButton.setFocusable(false);
		updateButton.addActionListener(this);
		updateButton.setBounds(descriptionTextArea.getX(), descriptionTextArea.getY() + descriptionTextArea.getHeight() + VBS, rowWidth - BW - BS, rowHeight);
		addHeight(updateButton.getHeight() + VBS);
		this.add(updateButton);
		
	}

	
	public void reload() {
		setSelectedJob(selectedJob);
	}
	
	public void addHeight(int height) {
		setSize(getWidth(), getHeight() + height);
		
	}

	
	public JButton addEditButton(JTextComponent textField, Component nextFocus) {
		JButton editButton = new JButton(new ImageIcon("src\\icon\\text_edit.png"));
		editButton.setContentAreaFilled(false);
		editButton.setFocusable(false);
		editButton.setBorderPainted(false);
		editButton.setBounds(textField.getX() + textField.getWidth() + BS, textField.getY(), BW, rowHeight);
		
		editButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(textField.isEditable()) {
					editButton.setIcon(new ImageIcon("src\\icon\\text_edit.png"));
					textField.setEditable(false);
					if(nextFocus != null) {
						nextFocus.requestFocus();
					} else {
						requestFocusInWindow();
					}
					
				} else if(!textField.getText().equals(INIT_TEXT)) {
						textField.setEditable(true);
						editButton.setIcon(new ImageIcon("src\\icon\\check.png"));
				}
				super.mouseClicked(e);
			}
		});
		
		return editButton;
	}
	
	
	
	
	public String getTitleTextFieldContent() {
		return titleTextField.getText();
	}


	public void setTitleTextFieldContent(String title) {
		this.titleTextField.setText(title);
		
		try {
			if(getSelectedJob() != null)
				getSelectedJob().setTitle(title);
		} catch (EntityException e) {
			e.printStackTrace();
		}
	}


	public Employer getEmployerSearchBoxObject() {
		return (Employer) employerSearchBox.getSelectedObject();
	}


	public Price getPriceSearchBoxObject() {
		return (Price) priceSearchBox.getSelectedObject();
	}

	public String getDescriptionTextAreaContent() {
		return descriptionTextArea.getText();
	}


	public void setDescriptionTextAreaContent(String description) {
		this.descriptionTextArea.setText(description);
		
		if(getSelectedJob() != null)
			getSelectedJob().setDescription(description);
	}


	public Job getSelectedJob() {
		return selectedJob;
	}


	public void setSelectedJob(Job selectedJob) {
		this.selectedJob = selectedJob;
		
		if(selectedJob == null) {
			titleTextField.setText(INIT_TEXT);
			employerSearchBox.setText(INIT_TEXT);
			priceSearchBox.setText(INIT_TEXT);
			descriptionTextArea.setText(INIT_TEXT);
		} else {
			
			setTitleTextFieldContent(selectedJob.getTitle());
			setDescriptionTextAreaContent(selectedJob.getDescription());
			
		}
		
		for(int i = 0; i < this.getComponentCount(); i++) {
			if(getComponent(i) instanceof RecordTextField)
				((RecordTextField)this.getComponent(i)).setEditable(false);
			else if(getComponent(i) instanceof TextArea)
				((TextArea)getComponent(i)).setEditable(false);
			else if(getComponent(i) instanceof JButton && (this.getComponent(i) != updateButton))
				((JButton)this.getComponent(i)).setIcon(new ImageIcon("src\\icon\\text_edit.png"));
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == updateButton && selectedJob != null) {
			
			setTitleTextFieldContent(getTitleTextFieldContent());
			setDescriptionTextAreaContent(getDescriptionTextAreaContent());
			
			
			if(true == JobDAO.getInstance().update(selectedJob)) {
				JOptionPane.showMessageDialog(this, "Update successful", "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
				
				Component component = getParent();
				while(component != null && component.getClass() != JobDisplay.class) {
					component = component.getParent();
				}
				
				if(component != null) {
					((Observer)component).update();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Update failed", "UNSUCCESSFUL", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
}
