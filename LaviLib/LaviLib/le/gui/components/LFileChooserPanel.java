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

	JLabel descriptionLabel = new JLabel();
	JTextField pathField = new JTextField();
	JButton browse = new JButton("Browse");
	
	public LFileChooserPanel(String description) {
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
				int option = jfc.showOpenDialog(LFileChooserPanel.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					pathField.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		this.add(browse, BorderLayout.EAST);
	}
	
	public File getSelectedFile() {
		return new File(pathField.getText());
	}
}