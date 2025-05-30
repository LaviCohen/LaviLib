package le.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

public interface ColorTheme {
	String DONT_AFFECT = "Don't affect";
	ColorTheme DEFAULT_COLOR_THEME = new ColorTheme() {
		
		Color backgroundColor = new Color(233, 233, 233);
		Color textColor = Color.BLACK;
		
		
		@Override
		public Color getTextColor() {
			return textColor;
		}
		
		@Override
		public Color getBackgroundColor() {
			return backgroundColor;
		}
		@Override
		public Component affect(Component component) {
			if (component.getName() != null && component.getName().equals(DONT_AFFECT)) {
				return component;
			}
			if (component instanceof JTextComponent) {
				component.setBackground(getBackgroundColor().brighter());
				component.setForeground(getTextColor());
			}else if(component instanceof JButton){	
				component.setBackground(getBackgroundColor().darker());
				component.setForeground(getTextColor());
			}else{
				if (component instanceof JComponent) {
					((JComponent) component).setOpaque(true);
					if (!isAffectingButtons() || !(component instanceof JButton)) {
						component.setBackground(getBackgroundColor());
						component.setForeground(getTextColor());
					}
				}
			}
			if (component instanceof Container) {
				affectContainer((Container)component);
			}
			return component;
		}

		@Override
		public boolean isAffectingButtons() {
			return false;
		}
	};
	public boolean isAffectingButtons();
	public Color getBackgroundColor();
	public Color getTextColor();
	public Component affect(Component component);
	public default void affectContainer(Container container) {
		for (Component component : container.getComponents()) {
			affect(component);
		}
	}
}