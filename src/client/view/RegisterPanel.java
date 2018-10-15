package client.view;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterPanel extends JPanel {
	
	private static final long serialVersionUID = -4854758538004111313L;
	private ActionListener controller;
	private JTextField registerField;
	private JButton registerButton;
	
	public RegisterPanel(ActionListener controller) {
		this.controller = controller;
		this.add(new JLabel("Pseudo :"));
		this.registerField = new JTextField();
		this.registerField.setColumns(15);
		this.add(registerField);
		this.registerButton = new JButton("Connexion");
		this.add(registerButton );
		this.registerButton.addActionListener(this.controller);
	}
	
	public String getFieldContent(){
		return this.registerField.getText();
	}
	
}
