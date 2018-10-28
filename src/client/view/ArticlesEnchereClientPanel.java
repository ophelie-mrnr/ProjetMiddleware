package client.view;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import shared.IClient;
import shared.Item;

public class ArticlesEnchereClientPanel  extends JPanel{


	private IClient client;
	private ActionListener controller;
	private List<Item> items;
	
	
	public ArticlesEnchereClientPanel(IClient client, ActionListener controller) throws RemoteException {
		
		super();
		this.client = client;
		this.controller = controller;

		// récupérer les articles qu'il a mi en vente pour qu'il ai une vision de ses ventess en cours 
		items = client.getItems();
		
		this.setPreferredSize(new Dimension(800, 600));
		
		//System.out.println("Mes articles mis aux encheres");
		
		for (Item i : items) {
			JPanel itemPanel = new JPanel();
			itemPanel.setLayout(new GridBagLayout());

			JLabel name = new JLabel(i.getName());
			JTextArea descLabel = new JTextArea(i.getDescription());
			JLabel price = new JLabel(String.valueOf(i.getPrice()) + " mornilles");
			
			
		}
	}

	

}
