package le.gui.components;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

/**
 * LMenu class is a replacement for JMenuBar class. This class let you create
 * full menu bar from 2d String array, as following:
 * <ol>
 * <li>Every row in the array represents menu, the first element is its name and the next elements are the menu items.</li>
 * <li>To create a shortcut, you add at the end of the String one or
 * more of the shortcut characters and the letter (More details below).</li>
 * <li>To create a JCheckBoxMenuItem, add "[check-box]" before the name of the item.</li>
 * <li>To create a separator line, write "---" as the name of the item.<br/>
 * Also, null item names will became separators.</li>
 * </ol>
 * <table>
	 * <tr><td>Character</td><td>Key</td></tr>
	 * <tr><td>#</td>        <td>Control</td></tr>
	 * <tr><td>@</td>        <td>Shift</td></tr>
	 * <tr><td>$</td>        <td>Alt</td></tr>
 * </table>
 * For example, to create "Save As..." item with control shift 's' shortcut,
 * write this String: {@code "Save As...#$s"}
 * 
 */
public class LMenu extends JMenuBar {
	private static final long serialVersionUID = 1L;
	public ActionListener AL;
	public JMenuItem[][] menuArr;

	public LMenu(String[][] menuNames, ActionListener AL) {
		this.AL = AL;
		translateToMenuItems(menuNames);
		addToMenuBar();
	}
	private void translateToMenuItems(String[][] menuNames) {
		menuArr = new JMenuItem[menuNames.length][0];
		for (int i = 0; i < menuNames.length; i++) {
			if (menuNames[i][0] != null) {
				menuArr[i] = new JMenuItem[menuNames[i].length];
				menuArr[i][0] = new JMenu(menuNames[i][0]);
			}
		}
		for (int i = 0; i < menuNames.length; i++) {
			for (int j = 1; j < menuNames[i].length; j++) {
				if (menuNames[i][j] != null && !menuNames[i][j].equals("---")) {
					menuArr[i][j] = build(menuNames[i][j]);
				}
			}
		}
	}
	private JMenuItem build(String menuItem) {
		JMenuItem MI;
		if (menuItem.contains("@") || menuItem.contains("#") || menuItem.contains("$")) {
			int hotkeyLength = 1;
			String keyCombo = "";
			if (menuItem.contains("@")) {
				keyCombo += "shift ";
				hotkeyLength++;
			}
			if (menuItem.contains("#")) {
				keyCombo += "control ";
				hotkeyLength++;
			}
			if (menuItem.contains("$")) {
				keyCombo += "alt ";
				hotkeyLength++;
			}
			MI = new JMenuItem(menuItem.substring(0, menuItem.length() - hotkeyLength));
			keyCombo += (menuItem.charAt(menuItem.length() - 1) + "").toUpperCase();
			KeyStroke hotkey = KeyStroke.getKeyStroke(keyCombo);
			MI.setAccelerator(hotkey);
		} else if (menuItem.startsWith("[check-box]")) {
			MI = new JCheckBoxMenuItem(menuItem.substring(11));
		}else {
			MI = new JMenuItem(menuItem);
		}
		MI.addActionListener(AL);
		return MI;
	}
	private void addToMenuBar() {
		for (int i = 0; i < menuArr.length; i++) {
			if (menuArr[i][0] != null) {
				this.add(menuArr[i][0]);
			} else {
				break;
			}
		}
		for (int i = 0; i < menuArr.length; i++) {
			for (int j = 1; j < menuArr[i].length; j++) {
				if (menuArr[i][j] != null) {
					if (menuArr[i][j].getText().equals("---")) {
						menuArr[i][0].add(new JSeparator());
					} else {
						menuArr[i][0].add(menuArr[i][j]);
					}
				} else {
					break;
				}
			}
		}
	}
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (menuArr != null) {
			for (JMenuItem[] jMenuItems : menuArr) {
				for (JMenuItem jMenuItem : jMenuItems) {
					jMenuItem.setBackground(bg);
				}
			}
		}
	}
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (menuArr != null) {
			for (JMenuItem[] jMenuItems : menuArr) {
				for (JMenuItem jMenuItem : jMenuItems) {
					jMenuItem.setForeground(fg);
				}
			}
		}
	}
}