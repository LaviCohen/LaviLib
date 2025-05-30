package le.gui.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LFileChooserPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public interface SelectionListener{
		public void fileSelected(File f);
	}
	
	JLabel descriptionLabel = new JLabel();
	JTextField pathField = new JTextField();
	JButton browse = new JButton("Browse");
	
	SelectionListener selectionListener = null;
	
	public LFileChooserPanel(String description, int fileSelectionMode) {
		super(new BorderLayout(5, 0));
		
		if (description != null) {
			descriptionLabel.setText(description);
		}
		this.add(descriptionLabel, BorderLayout.WEST);
		this.add(pathField);
		browse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(pathField.getText());
				jfc.setFileSelectionMode(fileSelectionMode);
				int option = jfc.showOpenDialog(LFileChooserPanel.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					pathField.setText(jfc.getSelectedFile().getAbsolutePath());
					if (selectionListener != null) {
						selectionListener.fileSelected(getSelectedFile());
					}
				}
			}
		});
		this.add(browse, BorderLayout.EAST);
	}
	
	public File getSelectedFile() {
		return new File(pathField.getText());
	}

	public SelectionListener getSelectionListener() {
		return selectionListener;
	}

	public void setSelectionListener(SelectionListener selectionListener) {
		this.selectionListener = selectionListener;
	}
}