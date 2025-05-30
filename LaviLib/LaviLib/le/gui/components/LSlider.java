package le.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import le.languages.AbstractTranslator;

/**
 * LSlider is a component which provides a label before the slider to the slider's subject, and also a text field next to the slider,
 * to let the user insert accurate values faster.
 * */
public class LSlider extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JSlider slider;
	private JTextField field;
	private JLabel subject;
	private double valueFactor = 1;
	
	private int roundFactor = 1000;
	
	public LSlider(String subject, double minValue, double maxValue, double defaultValue, double valueFactor) {
		this(subject, (int) (minValue / valueFactor), (int) (maxValue / valueFactor),
				(int) (defaultValue / valueFactor));
		this.valueFactor = valueFactor;
	}
	public LSlider(String subject, int minValue, int maxValue, int defaultValue) {
		super(new BorderLayout());
		this.subject = new JLabel(subject);
		this.add(this.getSubject(), AbstractTranslator.getTranslator().getBeforeTextBorder());
		this.slider = new JSlider(minValue, maxValue, defaultValue);
		this.getSlider().setComponentOrientation(AbstractTranslator.getTranslator().getComponentOrientation());
		this.add(getSlider());
		this.field = new JTextField();
		this.add(field, AbstractTranslator.getTranslator().getAfterTextBorder());
		this.getSlider().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				double newVal = round(LSlider.this.getValue());
				if (newVal == (int)newVal) {
					field.setText((int)newVal + "");					
				} else {
					field.setText(newVal + "");
				}
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						LSlider.this.doLayout();
					}
				});
			}
		});
		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				getSlider().setValue((int) (Double.valueOf(field.getText()) / LSlider.this.valueFactor));
				LSlider.this.doLayout();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				LSlider.this.field.setText(defaultValue * valueFactor + "");
				LSlider.this.doLayout();
			}
		});
	}
	private double round(double value) {
		return ((int)(value * roundFactor)) / (double)roundFactor;
	}
	public double getValue() {
		return getSlider().getValue() * valueFactor;
	}
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (this.getSlider() != null) {
			this.getSubject().setBackground(bg);
			this.getSlider().setBackground(bg);
			this.field.setBackground(bg.brighter());
		}
	}
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (this.getSlider() != null) {
			this.getSubject().setForeground(fg);
			this.getSlider().setForeground(fg);
			this.field.setForeground(fg);
		}
	}
	public JLabel getSubject() {
		return subject;
	}
	public JSlider getSlider() {
		return slider;
	}
	public int getRoundFactor() {
		return roundFactor;
	}
	public void setRoundFactor(int roundFactor) {
		this.roundFactor = roundFactor;
	}
}