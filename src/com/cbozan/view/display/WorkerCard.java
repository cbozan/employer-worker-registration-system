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

import com.cbozan.dao.WorkerDAO;
import com.cbozan.entity.Worker;
import com.cbozan.exception.EntityException;
import com.cbozan.view.component.RecordTextField;
import com.cbozan.view.component.TextArea;
import com.cbozan.view.helper.Observer;

public class WorkerCard extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = -8639991015785772064L;
	public static final int rowWidth = 240;
	public static final int rowHeight = 24;
	private final int BW = 40;
	private final int BS = 5;
	private final String INIT_TEXT = "SEARCH WORKER";
	//private int VSS = 5;
	private int VMS = 10;
	private int VBS = 15;
	
	
	private JLabel fnameLabel, lnameLabel, telLabel, ibanLabel, descriptionLabel;
	private RecordTextField fnameTextField, lnameTextField, telTextField, ibanTextField;
	private TextArea descriptionTextArea;
	private JButton updateButton;
	private Worker selectedWorker;
	
	
	
	public WorkerCard() {
		super();
		setLayout(null);
		
		// Edit butons will be optimized
		// because they aren't accessible
		
		selectedWorker = null;
		
		fnameLabel = new JLabel("İsim");
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
		
		
		lnameLabel = new JLabel("Soyisim");
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
		
		telLabel = new JLabel("Telefon Numarası");
		telLabel.setBounds(lnameTextField.getX(), lnameTextField.getY() + lnameTextField.getHeight() + VMS, rowWidth, rowHeight);
		addHeight(telLabel.getHeight() + VMS);
		this.add(telLabel);
		
		telTextField = new RecordTextField(RecordTextField.PHONE_NUMBER_TEXT + RecordTextField.NON_REQUIRED_TEXT);
		telTextField.setText(INIT_TEXT);
		telTextField.setEditable(false);
		telTextField.setBounds(telLabel.getX(), telLabel.getY() + telLabel.getHeight(), rowWidth - BW - BS, rowHeight);
		addHeight(telTextField.getHeight());
		this.add(telTextField);
		this.add(addEditButton(telTextField, ibanTextField));
		
		ibanLabel = new JLabel("İban Numarası");
		ibanLabel.setBounds(telTextField.getX(), telTextField.getY() + telTextField.getHeight() + VMS, rowWidth, rowHeight);
		addHeight(ibanLabel.getHeight() + VMS);
		this.add(ibanLabel);
		
		ibanTextField = new RecordTextField(RecordTextField.IBAN_NUMBER_TEXT + RecordTextField.NON_REQUIRED_TEXT);
		ibanTextField.setText(INIT_TEXT);
		ibanTextField.setEditable(false);
		ibanTextField.setBounds(ibanLabel.getX(), ibanLabel.getY() + ibanLabel.getHeight(), rowWidth - BW - BS, rowHeight);
		addHeight(ibanTextField.getHeight());
		this.add(ibanTextField);
		this.add(addEditButton(ibanTextField, descriptionTextArea));
		
		descriptionLabel = new JLabel("Açıklama");
		descriptionLabel.setBounds(ibanTextField.getX(), ibanTextField.getY() + ibanTextField.getHeight() + VMS, rowWidth, rowHeight);
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
		
		updateButton = new JButton("Güncelle");
		updateButton.setFocusable(false);
		updateButton.addActionListener(this);
		updateButton.setBounds(descriptionTextArea.getX(), descriptionTextArea.getY() + descriptionTextArea.getHeight() + VBS, rowWidth - BW - BS, rowHeight);
		addHeight(updateButton.getHeight() + VBS);
		this.add(updateButton);
		
		
	}
	

	public void reload() {
		setSelectedWorker(selectedWorker);
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
			getSelectedWorker().setFname(fnameTextFieldContent);
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
			getSelectedWorker().setLname(lnameTextFieldContent);
		} catch (EntityException e) {
			e.printStackTrace();
		}
	}


	public String getTelTextFieldContent() {
		return telTextField.getText();
	}


	public void setTelTextFieldContent(String telTextFieldContent) {
		this.telTextField.setText(telTextFieldContent);
		getSelectedWorker().setTel(Arrays.asList(telTextFieldContent));
	}


	public String getIbanTextFieldContent() {
		return ibanTextField.getText();
	}


	public void setIbanTextFieldContent(String ibanTextFieldContent) {
		this.ibanTextField.setText(ibanTextFieldContent);
		getSelectedWorker().setIban(ibanTextFieldContent.trim().toUpperCase());
	}


	public String getDescriptionTextAreaContent() {
		return descriptionTextArea.getText();
	}


	public void setDescriptionTextAreaContent(String descriptionTextAreaContent) {
		this.descriptionTextArea.setText(descriptionTextAreaContent);
		getSelectedWorker().setDescription(descriptionTextAreaContent);
	}


	public Worker getSelectedWorker() {
		return selectedWorker;
	}

	public void setSelectedWorker(Worker selectedWorker) {
		this.selectedWorker = selectedWorker;
		
		if(selectedWorker == null) {
			fnameTextField.setText(INIT_TEXT);
			lnameTextField.setText(INIT_TEXT);
			ibanTextField.setText(INIT_TEXT);
			telTextField.setText(INIT_TEXT);
			descriptionTextArea.setText(INIT_TEXT);
		} else {
			
			setFnameTextFieldContent(selectedWorker.getFname());
			setLnameTextFieldContent(selectedWorker.getLname());
			if(selectedWorker.getTel() == null || selectedWorker.getTel().size() == 0)
				setTelTextFieldContent("");
			else
				setTelTextFieldContent(selectedWorker.getTel().get(0));
			setIbanTextFieldContent(selectedWorker.getIban());
			setDescriptionTextAreaContent(selectedWorker.getDescription());
			
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
		
		System.out.println(((Object)getParent()).getClass() == WorkerDisplay.class);
		
		if(e.getSource() == updateButton && selectedWorker != null) {
			setFnameTextFieldContent(getFnameTextFieldContent());
			setLnameTextFieldContent(getLnameTextFieldContent());
			setTelTextFieldContent(getTelTextFieldContent());
			setIbanTextFieldContent(getIbanTextFieldContent());
			setDescriptionTextAreaContent(getDescriptionTextAreaContent());
			
			if(true == WorkerDAO.getInstance().update(selectedWorker)) {
				JOptionPane.showMessageDialog(this, "Güncelleme başarılı", "BAŞARILI", JOptionPane.INFORMATION_MESSAGE);
				
				Component component = getParent();
				while(component != null && component.getClass() != WorkerDisplay.class) {
					component = component.getParent();
				}
				
				if(component != null) {
					((Observer)component).update();
				}
				
			} else {
				JOptionPane.showMessageDialog(this, "Güncelleme başarısız", "BAŞARISIZ", JOptionPane.ERROR_MESSAGE);
			}
			
			
			
		}
		
	}
	
}