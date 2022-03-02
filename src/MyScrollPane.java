import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MyScrollPane extends JScrollPane{

	
	
	public MyScrollPane(Component a, int vertical, int horizontal) {
		
		super(a,ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
			      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
//		setComponentZOrder(getVerticalScrollBar(), 0);
//		    setComponentZOrder(getViewport(), 0);
		    getVerticalScrollBar().setOpaque(false);

		   setLayout(new ScrollPaneLayout() {
		      @Override
		      public void layoutContainer(Container parent) {
		        JScrollPane scrollPane = (JScrollPane)parent;

		        Rectangle availR = scrollPane.getBounds();
		        availR.x = availR.y = 0;

		        Insets insets = parent.getInsets();
		        availR.x = insets.left;
		        availR.y = insets.top;
		        availR.width  -= insets.left + insets.right;
		        availR.height -= insets.top  + insets.bottom;

		        Rectangle vsbR = new Rectangle();
		        vsbR.width  = 12;
		        vsbR.height = availR.height;
		        vsbR.x = availR.x + availR.width - vsbR.width;
		        vsbR.y = availR.y;

		        if(viewport != null) {
		          viewport.setBounds(availR);
		        }
		        if(vsb != null) {
		          vsb.setVisible(true);
		          vsb.setBounds(vsbR);
		        }
		      }
		    });
		    getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		      private final Dimension d = new Dimension();
		      @Override protected JButton createDecreaseButton(int orientation) {
		        return new JButton() {
		          @Override public Dimension getPreferredSize() {
		            return d;
		          }
		        };
		      }
		      @Override protected JButton createIncreaseButton(int orientation) {
		        return new JButton() {
		          @Override public Dimension getPreferredSize() {
		            return d;
		          }
		        };
		      }
		      @Override
		      protected void paintTrack(Graphics g, JComponent c, Rectangle r) {}
		      @Override
		      protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
		        Graphics2D g2 = (Graphics2D)g.create();
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                            RenderingHints.VALUE_ANTIALIAS_ON);
		        Color color = null;
		        JScrollBar sb = (JScrollBar)c;
		        if(!sb.isEnabled() || r.width>r.height) {
		          return;
		        }else if(isDragging) {
		          color = new Color(200,200,100,220);
		        }else if(isThumbRollover()) {
		          color = new Color(255,255,100,220);
		        }else {
		          color = new Color(220,220,200,220);
		        }
		        g2.setPaint(color);
		        g2.fillRoundRect(r.x,r.y,r.width,r.height,10,10);
		        g2.setPaint(Color.WHITE);
		        g2.drawRoundRect(r.x,r.y,r.width,r.height,10,10);
		        g2.dispose();
		      }
		      @Override
		      protected void setThumbBounds(int x, int y, int width, int height) {
		        super.setThumbBounds(x, y, width, height);
		        scrollbar.repaint();
		      }
		    });
	}
	
	public MyScrollPane(Component a) {
		this(a, 0, 1);
	}

}
