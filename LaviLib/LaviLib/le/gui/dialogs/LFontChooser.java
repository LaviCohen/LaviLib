package le.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import le.gui.ColorTheme;
import le.gui.components.LSearchableComboBox;
import le.gui.components.LSlider;
import le.gui.components.LSearchableComboBox.Styler;
import le.gui.components.LSearchableComboBox.StylingManager;
/**
 * JColorChooser provides a pane of controls designed to allow a user to manipulate and select a font.
 * The user can choose the family, size and style, and see a preview before selecting.
 * */
public class LFontChooser {
	private static final Font DEFAULT_FONT = Font.decode("Arial");
	public static class FontHolder{
		Font font;
		public FontHolder(Font font) {
			this.font = font;
		}
		public Font getFont() {
			return font;
		}

		public void setFont(Font font) {
			this.font = font;
		}
	}
	private static final String DEFAULT_PREVIEW_TEXT = "<html>Your text will look like this:<br/>"
			+ "abcdefghijklmnopqrstuvwxyz1234567890/*-+!@#$%^&*()_+?\\/\'\":`~;</html>";
	public static Font openChooseFontDialog(Window owner, String title, Font baseFont, String previewText) {
		return openChooseFontDialog(owner, title, baseFont, previewText, ColorTheme.DEFAULT_COLOR_THEME);
	}
	public static Font openChooseFontDialog(Component owner, String title, Font baseFont, String previewText, ColorTheme theme) {
		return openChooseFontDialog(LDialogs.getWindowParent(owner), title, baseFont, previewText);
	}
	public static Font openChooseFontDialog(Window owner, String title, Font baseFont, String previewText, ColorTheme theme) {
		if (baseFont == null) {
			baseFont = DEFAULT_FONT;
		}
		FontHolder fontHolder = new FontHolder(baseFont);
		JDialog dialog = new JDialog(owner, title, ModalityType.APPLICATION_MODAL);
		dialog.setBackground(theme.getBackgroundColor());
		dialog.setLayout(new BorderLayout());
		LSearchableComboBox<String> familyBox = new LSearchableComboBox<String>(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(), 0, 
				new StylingManager() {
					
					@Override
					public Styler getStylerFor(Object source) {
						return new Styler(source, source + "      abcdeABCDE", source.toString(), Color.BLACK);
					}
				}
			, theme);
		familyBox.setSelectedItem(baseFont.getFamily());
		JPanel leftPanel = new JPanel(new GridLayout(4, 0));
		JPanel familyPanel = new JPanel(new BorderLayout());
		familyPanel.add(theme.affect(new JLabel("Family:")), BorderLayout.WEST);
		familyPanel.add(familyBox);
		leftPanel.add(familyPanel);
		LSlider sizeSlider = new LSlider("Size:", 0, 100, baseFont.getSize());
		sizeSlider.setBackground(theme.getBackgroundColor());
		sizeSlider.getSubject().setForeground(theme.getTextColor());
		leftPanel.add(sizeSlider);
		JPanel stylePanel = new JPanel(new GridLayout());
		JCheckBox bold = new JCheckBox("bold", baseFont.getStyle() >= Font.BOLD);
		theme.affect(bold);
		stylePanel.add(bold);
		JCheckBox italic = new JCheckBox("italic", baseFont.getStyle() >= Font.ITALIC);
		theme.affect(italic);
		stylePanel.add(italic);
		leftPanel.add(stylePanel);
		JPanel buttons = new JPanel(new GridLayout());
		JButton previewButton = new JButton("Preview");
		theme.affect(previewButton);
		previewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int style = (bold.isSelected() ? Font.BOLD : 0) + (italic.isSelected() ? Font.ITALIC : 0);
				fontHolder.setFont(
						new Font(familyBox.getSelectedItem(), style, (int) sizeSlider.getValue()));
				openPreviwDialog(previewText == null ? DEFAULT_PREVIEW_TEXT : previewText, dialog, fontHolder.getFont(), theme);
			}
		});
		buttons.add(previewButton);
		JButton apply = new JButton("Apply");
		theme.affect(apply);
		apply.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int style = (bold.isSelected() ? Font.BOLD : 0) + (italic.isSelected() ? Font.ITALIC : 0);
				fontHolder.setFont(
						new Font(familyBox.getSelectedItem(), style, (int) sizeSlider.getValue()));
				dialog.setVisible(false);
			}
		});
		buttons.add(apply);
		leftPanel.add(buttons);
		dialog.add(leftPanel);
		dialog.pack();
		dialog.setVisible(true);
		dialog.dispose();
		return fontHolder.getFont();
	}
	

	private static void openPreviwDialog(String text, JDialog parent, Font font, ColorTheme theme) {
		JDialog dialog = new JDialog(parent, "Preview", true);
		dialog.setBackground(theme.getBackgroundColor());
		JLabel label = new JLabel(text);
		theme.affect(label);
		label.setFont(font);
		JScrollPane sp = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		theme.affect(sp);
		dialog.add(sp);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		if (dialog.getSize().width > 1000) {
			dialog.setSize(600, dialog.getSize().height + 100);
		}
		dialog.setVisible(true);
	}
}