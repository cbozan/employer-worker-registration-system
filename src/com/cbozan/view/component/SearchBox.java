package com.cbozan.view.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.cbozan.entity.Worker;

public class SearchBox extends JTextField implements CaretListener, FocusListener{
	
	public final Color DEFAULT_EXITED_COLOR = new Color(162, 208, 215);
	public final Color DEFAULT_ENTERED_COLOR = new Color(73, 171, 134);
	
	private JPanel resultPanel;
	private List<Object> objectList;
	private List<Object> searchResultList;
	private Dimension resultBoxSize;
	private Color exitedColor, enteredColor;
	private boolean isEntered = false;
	private Object selectedObject;
	
	
	@SuppressWarnings("unchecked")
	public SearchBox(List<?> objectList, Dimension resultBoxSize) {
		this.objectList =  (List<Object>) objectList;
		this.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.resultBoxSize = resultBoxSize;
		
		resultPanel = new JPanel();
		resultPanel.setVisible(true);
		resultPanel.setLayout(null);
		
		searchResultList = new ArrayList<Object>();
		
		addCaretListener(this);
		addFocusListener(this);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SearchBox.this.setEditable(true);
			}
		});
		exitedColor = DEFAULT_EXITED_COLOR;
		enteredColor = DEFAULT_ENTERED_COLOR;
		
		
		DocumentFilter filter = new DocumentFilter() {
			public void insertString(DocumentFilter.FilterBypass fb, int offset,
		            String text, AttributeSet attr) throws BadLocationException {

		        fb.insertString(offset, text.toUpperCase(), attr);
		    }

		    public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
		            String text, AttributeSet attrs) throws BadLocationException {

		        fb.replace(offset, length, text.toUpperCase(), attrs);
		    }
		};
		
		((AbstractDocument)this.getDocument()).setDocumentFilter(filter);
		
		
	}
	
	
	public void setResultBoxSize(Dimension resultBoxSize) {
		this.resultBoxSize = resultBoxSize;
	}
	
	
	public Dimension getResultBoxSize() {
		return resultBoxSize;
	}
	
	public void setPanel(JPanel resultPanel) {
		this.resultPanel = resultPanel;
		
	}
	
	public JPanel getPanel() {
		return resultPanel;
	}
	
	@SuppressWarnings("unchecked")
	public void setObjectList(List<?> objectList) {
		this.objectList = (List<Object>) objectList;
	}
	
	public List<Object> getObjectList(){
		return objectList;
	}
	
	public void setSearchResultList(List<Object> searchResultList) {
		this.searchResultList = searchResultList;
	}
	
	public List<Object> getSearchResultList(){
		return searchResultList;
	}
	

	public void setExitedColor(Color color) {
		this.exitedColor = color;
	}
	
	public Color getEnteredColor() {
		return enteredColor;
	}
	
	public void setEnteredColor(Color color) {
		this.enteredColor = color;
	}
	
	public Color getExitedColor() {
		return exitedColor;
	}
	
	public Object getSelectedObject() {
		return selectedObject;
	}
	
	
	@Override
	public void caretUpdate(CaretEvent e) {
		if(getText().trim().length() > 0) {
			resultViewPanel();
		} else {
			getPanel().setVisible(false);
		}
		
	}
	
	public void resultViewPanel() {
		
		String searchText = getText().trim().toUpperCase();
		getPanel().removeAll();
		getSearchResultList().clear();
		
		for(Object obj : getObjectList()) {
			if(obj.toString().contains(searchText)) {
				getSearchResultList().add(obj);
				if(getSearchResultList().size() >= 10) {
					break;
				}
				
			}
			
		}
		
		int i = 0;
		for(Object searchResultObj : getSearchResultList()) {
			
			JTextField tf = new JTextField(searchResultObj.toString());
			tf.setForeground(new Color(105, 35, 64));
			tf.setName(i + "");
			tf.setHorizontalAlignment(SwingConstants.CENTER);
			tf.setEditable(false);
			tf.setBackground(new Color(162, 208, 215));
			tf.setBorder(new LineBorder(new Color(202, 228, 232)));
			tf.setBounds(0, i * (getResultBoxSize().height + 1) + i, getResultBoxSize().width, getResultBoxSize().height);
			tf.addMouseListener(new MouseAdapter() {
				public void mouseExited(MouseEvent e) {
					tf.setBackground(getExitedColor());
					isEntered = false;
				}
				
				public void mouseEntered(MouseEvent e) {
					tf.setBackground(getEnteredColor());
					isEntered = true;
				}
				
				public void mouseClicked(MouseEvent e) {
					mouseAction(e, searchResultList.get(Integer.parseInt(tf.getName())), Integer.parseInt(tf.getName()));
					//selectedWorkerDefaultListModel.addElement(searchResultList.get(Integer.parseInt(tf.getName())));
					//workerList.remove(searchResultList.get(Integer.parseInt(tf.getName())));
					getPanel().setVisible(false);
					//selectedInfoCountLabel.setText(selectedWorkerDefaultListModel.size() + " kişi");
				}
			});
			
			getPanel().add(tf);
			
			++i;
		}
		
		getPanel().setSize(getPanel().getWidth(), getSearchResultList().size() * (getResultBoxSize().height + 2));
		getPanel().setVisible(true);
		getPanel().revalidate();
		getPanel().repaint();
		
		
	}
	
	public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
		selectedObject = searchResultObject;
	}

	@Override
	public void focusGained(FocusEvent e) {}

	@Override
	public void focusLost(FocusEvent e) {
		if(!isEntered) {
			getPanel().setVisible(false);
		}
	}

}
