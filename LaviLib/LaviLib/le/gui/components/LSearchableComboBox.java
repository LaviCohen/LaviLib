package le.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import le.gui.ColorTheme;
import le.gui.dialogs.LDialogs;
import le.languages.AbstractTranslator;

/**
 * LSearchableComboBox is a component which provides combo box which the user can type in its field and search in the list
 * in addition to select from it manually, like the original JComboBox. LsearchableComboBox also provides auto-completion for typing,
 * to help the user find his need faster.
 * */
public class LSearchableComboBox<T> extends JPanel {

	private static final long serialVersionUID = 1L;

	private Styler[] list;
	private int index;
	private JTextField field = new JTextField();
	private JButton openMenu = new JButton("\\/");
	private String itemUndefinedErrorText = "Item undefined";
	private boolean returnToLastIfUndefind = true;
	private int downs = 0;
	private boolean isAutoCompleteNow = false;
	private ColorTheme theme;
	private JDialog popupWindow;

	private JScrollPane popupWindowScrollPane;
	public static final StylingManager DEFAULT_STYLIN_MANAGER = new StylingManager() {
		
		@Override
		public Styler getStylerFor(Object source) {
			Styler styler = new Styler(source, source.toString(), "Arial", Color.BLACK);
			return styler;
		}
	};
	
	public static class Styler{
		public Object source;
		//What user will see
		String display;
		String font;
		Color textColor;
		public Styler(Object source, String display, String font, Color textColor) {
			super();
			this.source = source;
			this.display = display;
			this.font = font;
			this.textColor = textColor;
		}
		public Font getFont(int size) {
			return new Font(font, Font.PLAIN, size);
		}
	}
	private class StyledList extends JPanel implements LayoutManager2{
		public LinkedList<Styler> list = new LinkedList<Styler>();
		public LinkedList<JComponent> panels = new LinkedList<JComponent>();
		JPanel listPanel = new JPanel();
		public JPanel selectedPanel;
		private static final long serialVersionUID = 1L;
		private ListSelectionListener listSelectionListener;
		
		public StyledList(Styler[] stylersList){
			listPanel.setLayout(this);
			for (int i = 0; i < stylersList.length; i++) {
				list.add(stylersList[i]);
			}
			for (Styler s: stylersList) {
				JPanel p = new JPanel();
				p.setLayout(new BorderLayout());
				p.setBackground(Color.WHITE);
				JLabel l = new JLabel(s.display);
				l.setFont(s.getFont(15));
				l.setForeground(s.textColor);
				l.setOpaque(false);
				panels.add(p);
				p.add(l);
				p.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						setSelection(p);
					}
				});
				listPanel.add(p);
			}
			listPanel.setBackground(Color.WHITE);
			this.setLayout(new BorderLayout());
			this.add(listPanel);
		}
		public void addListSelectionListener(ListSelectionListener listener) {
			this.listSelectionListener = listener;
		}
		public void setSelection(JPanel p) {
			if (selectedPanel != null) {
				selectedPanel.setOpaque(false);
				selectedPanel.setBackground(Color.WHITE);
			}
			p.setOpaque(true);
			p.setBackground(new Color(30, 30, 255, 100));
			selectedPanel = p;
			this.revalidate();
			this.repaint();
			if (listSelectionListener != null) {
				listSelectionListener.valueChanged(new ListSelectionEvent(this, 0, 0, false));
			}
		}
		@SuppressWarnings({ "unused", "unchecked" })
		public T getSelectedValue() {
			if (selectedPanel == null) {
				return null;
			}
			return (T) list.get(this.getSelectedIndex()).source;
		}
		public int getSelectedIndex() {
			return getComponentIndex(selectedPanel);
		}
		public int getComponentIndex(Component comp) {
			Component[] comps = listPanel.getComponents();
			for (int i = 0; i < comps.length; i++) {
				if (comps[i] == comp) {
					return i;
				}
			}
			return -1;
		}
		@SuppressWarnings("unused")
		public int locationToIndex(Point p) {
			return this.getComponentIndex(listPanel.getComponentAt(p));
		}
		public void setSelectedIndex(int index) {
			if (index == -1) {
				selectedPanel = null;
				return;
			}
			setSelection((JPanel) listPanel.getComponent(index));
		}
		
		int lastTotalHeight = 0;
		@Override
		public void addLayoutComponent(String name, Component comp) {
			
		}
		public void layoutContainer(Container parent) {
			int totalHeight = 0;
			for (int i = 0; i < panels.size(); i++) {
				panels.get(i).setBounds(0, totalHeight, parent.getWidth(), 15);
				totalHeight += panels.get(i).getSize().height;
			}
			lastTotalHeight = totalHeight;
		}
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			if (this.list.size() > 0) {
				return new Dimension(150, lastTotalHeight);		
			}
			return new Dimension(parent.getWidth(), parent.getHeight());
		}
		@Override
		public void removeLayoutComponent(Component comp) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void addLayoutComponent(Component comp, Object constraints) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public float getLayoutAlignmentX(Container target) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public float getLayoutAlignmentY(Container target) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void invalidateLayout(Container target) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public Dimension maximumLayoutSize(Container target) {
			// TODO Auto-generated method stub
			return null;
		}

	}
	public static interface StylingManager{
		public Styler getStylerFor(Object source);
	}
	public LSearchableComboBox(T[] list) {
		this(list, 0);
	}
	public LSearchableComboBox(T[] list, int index) {
		this(list, index, DEFAULT_STYLIN_MANAGER, ColorTheme.DEFAULT_COLOR_THEME);
	}
	public LSearchableComboBox(T[] list, int index, StylingManager manager, ColorTheme theme) {
		this.theme = theme;
		this.theme.affect(this.openMenu);
		this.list = (Styler[]) Array.newInstance(Styler.class, list.length);
		for (int i = 0; i < list.length; i++) {
			this.list[i] = manager.getStylerFor(list[i]);
		}
		this.index = index;
		this.field.setText(this.list[index].display);
		this.theme.affect(this.field);
		this.setLayout(new BorderLayout());
		this.add(field, BorderLayout.CENTER);
		this.add(openMenu, AbstractTranslator.getTranslator().getAfterTextBorder());
		openMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (popupWindow != null) {
					openMenu.setText("\\/");
					popupWindow.dispose();
				}else {
					openMenu.setText("/\\");
					openSelect();
				}
			}
		});
		this.field.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (isAutoCompleteNow) {
					return;
				}
				downs = 0;
				autoComplete(field.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
		this.field.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				LSearchableComboBox.this.field.setSelectionStart(0);
				LSearchableComboBox.this.field.setSelectionEnd(0);
				if (!isListItem(LSearchableComboBox.this.field.getText())) {
					LDialogs.showMessageDialog(LSearchableComboBox.this,
							itemUndefinedErrorText, "Error", LDialogs.ERROR_MESSAGE);
					if (returnToLastIfUndefind) {
						LSearchableComboBox.this.field.setText(list[index].toString());
					}
				}else {
					updateFieldFont();
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		field.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					LSearchableComboBox.this.downs = 1;
					autoComplete(LSearchableComboBox.this.field.getText());
				}else if (e.getKeyCode() == KeyEvent.VK_UP) {
					LSearchableComboBox.this.downs = -1;
					autoComplete(LSearchableComboBox.this.field.getText());
				}
			}
		});
		updateFieldFont();
	}
	public void autoComplete(String search) {
		String found = null;
		for (int i = 0; i < list.length && found == null; i++) {
			if (list[i].display.toLowerCase().startsWith(search.toLowerCase())) {
				found = list[getIndexFor(i + downs)].display;
			}
		}
		if (found == null || found.equals(search)) {
			return;
		}
		final String finalFound = found;
		int writtenChars = field.getText().length();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				isAutoCompleteNow = true;
				field.setText(finalFound);
				if (downs == 0) {
					field.setSelectionStart(writtenChars);
					field.setSelectionEnd(finalFound.length());	
				}else {
					field.setSelectionStart(0);
					field.setSelectionEnd(finalFound.length());
				}
				downs = 0;
				int index = indexOf(finalFound);
				Font f = list[index].getFont(15);
				
				field.setFont(f);
				isAutoCompleteNow = false;
			}
		});
	}
	public void setSelectedItem(String itemString) {
		if (isSourceListItem(itemString)) {
			this.index = indexOfSource(itemString);
			this.field.setText(list[index].display);
			updateFieldFont();
			this.field.revalidate();
			this.field.repaint();
		}
	}
	private void updateFieldFont() {
		this.index = this.indexOf(this.field.getText());
		field.setFont(this.list[indexOf(field.getText())].getFont(15));
	}
	private int getIndexFor(int i) {
		int a = i % list.length;
		if (a < 0) {
			return list.length + a;
		}
		return a;
	}
	private static Dialog getParentDialog(Component component) {
		if (component == null) {
			return null;
		}
		if (component instanceof Dialog) {
			return (Dialog)component;
		}
		return getParentDialog(component.getParent());
	}
	private void createPopupWindow() {
		popupWindow = new JDialog(getParentDialog(this), true);
		popupWindow.setUndecorated(true);
		popupWindow.setLayout(new BorderLayout());
		StyledList showList = new StyledList(list);
		showList.setSelectedIndex(index);
		showList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				LSearchableComboBox.this.index = showList.getSelectedIndex();
				LSearchableComboBox.this.field.setText(
						LSearchableComboBox.this.list[LSearchableComboBox.this.index].display);
				updateFieldFont();
				popupWindow.dispose();
				popupWindow = null;
				openMenu.setText("\\/");
			}
		});
		popupWindowScrollPane = new JScrollPane(showList);
		showList.doLayout();
		popupWindow.add(popupWindowScrollPane);
		popupWindow.setSize(300, 300);
	}
	public void openSelect() {
		int partHeight = 15;
		int scrolling = partHeight * index;
		scrolling -= (scrolling > 45? 45: scrolling);
		createPopupWindow();
		popupWindowScrollPane.getViewport().setViewPosition(new Point(0, scrolling));
		popupWindow.setLocation(getLocationOnScreen().x, getLocationOnScreen().y + getHeight());
		popupWindow.setVisible(true);
	}
	private boolean isSourceListItem(Object source) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].source.equals(source)) {
				return true;
			}
		}
		return false;
	}
	public int indexOfSource(Object source) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].source.equals(source)) {
				return i;
			}
		}
		return -1;
	}
	private boolean isListItem(String itemString) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].display.equals(itemString)) {
				return true;
			}
		}
		return false;
	}
	public int indexOf(String itemString) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].display.equals(itemString)) {
				return i;
			}
		}
		return -1;
	}
	@SuppressWarnings("unchecked")
	public T getSelectedItem() {
		return (T) list[index].source;
	}
	public int getSelectedIndex() {
		return index;
	}
	public void update() {
		theme.affect(openMenu);
		revalidate();
		repaint();
	}
	public String getItemUndefinedErrorText() {
		return itemUndefinedErrorText;
	}
	public void setItemUndefinedErrorText(String itemUndefinedErrorText) {
		this.itemUndefinedErrorText = itemUndefinedErrorText;
	}
	public boolean isReturnToLastIfUndefind() {
		return returnToLastIfUndefind;
	}
	public void setReturnToLastIfUndefind(boolean returnToLastIfUndefind) {
		this.returnToLastIfUndefind = returnToLastIfUndefind;
	}
}