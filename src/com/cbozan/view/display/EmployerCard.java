package com.cbozan.view.display;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.cbozan.dao.EmployerDAO;
import com.cbozan.entity.Employer;
import com.cbozan.exception.EntityException;
import com.cbozan.view.component.RecordTextField;
import com.cbozan.view.component.TextArea;
import com.cbozan.view.helper.Observer;

public class EmployerCard extends JPanel implements ActionListener{

	private static final long serialVersionUID = -1216173735073342728L;

	public static final int rowWidth = 240;
	public static final int rowHeight = 24;
	private final int BW = 40;
	private final int BS = 5;
	private final String INIT_TEXT = "SEARCH EMPLOYER";
	//private int VSS = 5;
	private int VMS = 10;
	private int VBS = 15;
	
	private JLabel fnameLabel, lnameLabel, telLabel, descriptionLabel;
	private RecordTextField fnameTextField, lnameTextField, telTextField;
	private TextArea descriptionTextArea;
	private JButton updateButton;
	private Employer selectedEmployer;

	public EmployerCard() {
		super();
		setLayout(null);
		
		selectedEmployer =  null;
		
		fnameLabel = new JLabel("Name");
		fnameLabel.setBounds(0, 0, rowWidth, rowHeight);
		addHeight(fnameLabel.getHeight());
		this.add(fnameLabel);
		
		fnameTextField = new RecordTextField(RecordTextField.REQUIRED_TEXT);
		fnameTextField.setText(INIT_TEXT);
		fnameTextField.setEditable(false);
		fnameTextField.setBounds(fnameLabel.getX(), fnameLabel.getY() + fnameLabel.getHeight(), rowWidth - BW - BS, rowHeight);
		addHeight(fnameTextField.getHeight());
		this.add(fnameTextField);
		this.add(addEditButton(fnameTextField, lnameTextField));
		
		
		lnameLabel = new JLabel("Surname");
		lnameLabel.setBounds(fnameTextField.getX(), fnameTextField.getY() + fnameTextField.getHeight() + VMS, rowWidth, rowHeight);
		addHeight(lnameLabel.getHeight() + VMS);
		this.add(lnameLabel);
		
		lnameTextField = new RecordTextField(RecordTextField.REQUIRED_TEXT);
		lnameTextField.setText(INIT_TEXT);
		lnameTextField.setEditable(false);
		lnameTextField.setBounds(lnameLabel.getX(), lnameLabel.getY() + lnameLabel.getHeight(), rowWidth - BW - BS, rowHeight);
		addHeight(lnameTextField.getHeight());
		this.add(lnameTextField);
		this.add(addEditButton(lnameTextField, telTextField));
		
		telLabel = new JLabel("Phone number");
		telLabel.setBounds(lnameTextField.getX(), lnameTextField.getY() + lnameTextField.getHeight() + VMS, rowWidth, rowHeight);
		addHeight(telLabel.getHeight() + VMS);
		this.add(telLabel);
		
		telTextField = new RecordTextField(RecordTextField.PHONE_NUMBER_TEXT + RecordTextField.NON_REQUIRED_TEXT);
		telTextField.setText(INIT_TEXT);
		telTextField.setEditable(false);
		telTextField.setBounds(telLabel.getX(), telLabel.getY() + telLabel.getHeight(), rowWidth - BW - BS, rowHeight);
		addHeight(telTextField.getHeight());
		this.add(telTextField);
		this.add(addEditButton(telTextField, null));
		
		
		
		descriptionLabel = new JLabel("Description");
		descriptionLabel.setBounds(telTextField.getX(), telTextField.getY() + telTextField.getHeight() + VMS, rowWidth, rowHeight);
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
		setSelectedEmployer(selectedEmployer);
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


	public String getFnameTextFieldContent() {
		return fnameTextField.getText();
	}


	public void setFnameTextFieldContent(String fnameTextFieldContent) {
		this.fnameTextField.setText(fnameTextFieldContent);
		try {
			if(getSelectedEmployer() != null)
				getSelectedEmployer().setFname(fnameTextFieldContent);
		} catch (EntityException e) {
			e.printStackTrace();
		}
	}


	public String getLnameTextFieldContent() {
		return lnameTextField.getText();
	}


	public void setLnameTextFieldContent(String lnameTextFieldContent) {
		this.lnameTextField.setText(lnameTextFieldContent);
		try {
			if(getSelectedEmployer() != null)
				getSelectedEmployer().setLname(lnameTextFieldContent);
		} catch (EntityException e) {
			e.printStackTrace();
		}
	}


	public String getTelTextFieldContent() {
		return telTextField.getText();
	}


	public void setTelTextFieldContent(String telTextFieldContent) {
		this.telTextField.setText(telTextFieldContent);
		if(getSelectedEmployer() != null)
			getSelectedEmployer().setTel(Arrays.asList(telTextFieldContent));
	}


	public String getDescriptionTextAreaContent() {
		return descriptionTextArea.getText();
	}


	public void setDescriptionTextAreaContent(String descriptionTextAreaContent) {
		this.descriptionTextArea.setText(descriptionTextAreaContent);
		getSelectedEmployer().setDescription(descriptionTextAreaContent);
	}


	public Employer getSelectedEmployer() {
		return selectedEmployer;
	}

	public void setSelectedEmployer(Employer selectedEmployer) {
		this.selectedEmployer = selectedEmployer;
		
		if(selectedEmployer == null) {
			fnameTextField.setText(INIT_TEXT);
			lnameTextField.setText(INIT_TEXT);
			telTextField.setText(INIT_TEXT);
			descriptionTextArea.setText(INIT_TEXT);
		} else {
			
			setFnameTextFieldContent(selectedEmployer.getFname());
			setLnameTextFieldContent(selectedEmployer.getLname());
			if(selectedEmployer.getTel() == null || selectedEmployer.getTel().size() == 0)
				setTelTextFieldContent("");
			else
				setTelTextFieldContent(selectedEmployer.getTel().get(0));
			setDescriptionTextAreaContent(selectedEmployer.getDescription());
			
		}
		
		for(int i = 2; i < this.getComponentCount(); i += 3) {
			if(getComponent(i - 1) instanceof RecordTextField)
				((RecordTextField)this.getComponent(i - 1)).setEditable(false);
			else if(getComponent(i - 1) instanceof TextArea)
				((TextArea)getComponent(i - 1)).setEditable(false);
			
			((JButton)this.getComponent(i)).setIcon(new ImageIcon("src\\icon\\text_edit.png"));
		}
		
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getSource() == updateButton && selectedEmployer != null) {
			setFnameTextFieldContent(getFnameTextFieldContent());
			setLnameTextFieldContent(getLnameTextFieldContent());
			setTelTextFieldContent(getTelTextFieldContent());
			setDescriptionTextAreaContent(getDescriptionTextAreaContent());
			
			if(true == EmployerDAO.getInstance().update(selectedEmployer)) {
				JOptionPane.showMessageDialog(this, "Update successful", "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
				
				Component component = getParent();
				while(component != null && component.getClass() != EmployerDisplay.class) {
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
