package client.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import shared.IClient;
import shared.Item;
import shared.SellableItem;

public class SubmitPanel extends JPanel {

	private static final long serialVersionUID = -7555887340687619434L;
	private IClient client;
	private ActionListener controller;
	private JTextField txtItemName;
	private JTextField txtItemDescription;
	private JTextField txtItemPrice;
	private JTextField txtItemTime;
	private JButton btnItemSubmission;

	public SubmitPanel(IClient client, ActionListener controller) {
		super();
		this.client = client;
		this.controller = controller;
		this.txtItemName = new JTextField();
		this.txtItemDescription = new JTextField();
		this.txtItemPrice = new JTextField();
		this.txtItemTime = new JTextField();
		this.btnItemSubmission = new JButton("Soumettre");

		this.setLayout(new GridBagLayout());

		JLabel labelName = new JLabel("Nom : ");
		JLabel labelDescription = new JLabel("Description : ");
		JLabel labelPrice = new JLabel("Prix de base : ");
		JLabel labelTime = new JLabel("Durée de la vente (minutes) : ");

		labelName.setPreferredSize(new Dimension(250, 40));
		txtItemName.setPreferredSize(new Dimension(300, 40));
		labelDescription.setPreferredSize(new Dimension(250, 150));
		txtItemDescription.setPreferredSize(new Dimension(300, 150));
		labelPrice.setPreferredSize(new Dimension(250, 40));
		txtItemPrice.setPreferredSize(new Dimension(300, 40));
		labelTime.setPreferredSize(new Dimension(250, 40));
		txtItemTime.setPreferredSize(new Dimension(300, 40));

		GridBagConstraints gbSubmission = new GridBagConstraints();

		gbSubmission.gridx = 0;
		gbSubmission.gridy = 0;
		gbSubmission.gridwidth = 1;
		gbSubmission.gridheight = 4;
		gbSubmission.insets = new Insets(5, 5, 5, 50);
		gbSubmission.insets = new Insets(0, 0, 0, 0);

		// Name
		gbSubmission.gridx = 1;
		gbSubmission.gridheight = 1;
		this.add(labelName, gbSubmission);

		gbSubmission.gridx = 2;
		this.add(txtItemName, gbSubmission);

		// Description
		gbSubmission.gridx = 1;
		gbSubmission.gridy = 2;
		gbSubmission.gridwidth = 1;
		gbSubmission.gridheight = 1;
		this.add(labelDescription, gbSubmission);

		gbSubmission.gridx = 2;
		this.add(txtItemDescription, gbSubmission);

		// Price
		gbSubmission.gridx = 1;
		gbSubmission.gridy = 3;
		this.add(labelPrice, gbSubmission);

		gbSubmission.gridx = 2;
		gbSubmission.gridy = 3;
		this.add(txtItemPrice, gbSubmission);

		// Time
		gbSubmission.gridx = 1;
		gbSubmission.gridy = 4;
		this.add(labelTime, gbSubmission);

		gbSubmission.gridx = 2;
		gbSubmission.gridy = 4;
		this.add(txtItemTime, gbSubmission);

	
		// Button for submission
		gbSubmission.gridx = 2;
		gbSubmission.gridy = 6;
		btnItemSubmission.addActionListener(this.controller);
		this.add(btnItemSubmission, gbSubmission);
	}

	public Item getFieldsContent() throws NumberFormatException, RemoteException{
		Item content = new SellableItem(txtItemName.getText(), txtItemDescription.getText(), Double.parseDouble(txtItemPrice.getText()), client.getPseudo(), Long.parseLong(txtItemTime.getText()));
		return content;
	}

	public void clear() {
		txtItemName.setText("");
		txtItemDescription.setText("");
		txtItemPrice.setText("");
		txtItemTime.setText("");
	}

}
