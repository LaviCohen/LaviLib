package lb.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LImageView extends JPanel{

	private static final long serialVersionUID = 1L;

	protected Image image;
	
	protected int resizePolicy;

	protected LSlider zoomSlider;

	protected JScrollPane jsp;
	
	public LImageView(Image image, int resizePolicy) {
		super(new BorderLayout());
		this.resizePolicy = resizePolicy;
		zoomSlider = new LSlider("Zoom:", 10, 400, 100);
		zoomSlider.getSlider().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				jsp.revalidate();
				repaint();
			}
		});
		JLabel view = new JLabel() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paint(Graphics g) {
				Image image = LImageView.this.image;
				if (image == null) {
					super.paint(g);
					return;
				}
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				Dimension imageDimension = getImageDimension();
				g2.drawImage(image, 0, 0, imageDimension.width, imageDimension.height, null);
				g2.dispose();
				g.drawImage(image, 0, 0, null);
				this.setPreferredSize(imageDimension);
			}
		};
		jsp = new JScrollPane(view);
		this.add(jsp);
		this.add(zoomSlider, BorderLayout.SOUTH);
	}
	
	protected Dimension getImageDimension() {
		double zoomRate = zoomSlider.getValue() / 100.0;
		return new Dimension((int) (image.getWidth(null) * zoomRate), 
							 (int) (image.getHeight(null) * zoomRate));
	}

	public void setImage(Image image) {
		this.image = image;
		enforcePolicy();
		repaint();
	}

	private void enforcePolicy() {
		
	}
}
