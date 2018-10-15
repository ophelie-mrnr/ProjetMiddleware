package client.view;

import javax.swing.JButton;
import javax.swing.JTextArea;

import shared.Item;

public class BidButton extends JButton {

	private static final long serialVersionUID = 5268533470010984872L;
	private Item related;
	private JTextArea source;
	
	public BidButton(String cmd, Item i, JTextArea jta) {
		super(cmd);
		this.related = i;
		this.source = jta;
	}
	
	public Item getItem(){
		return this.related;
	}
	
	public String getContent(){
		return this.source.getText();
	}

	
	
}
