package client.view;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import shared.IClient;

public class ClientFrame extends JFrame {

	private static final long serialVersionUID = 6994145468596380654L;
	private IClient client;
	private BidsPanel bidsPanel;
	private OwnedPanel ownedPanel;
	private JTabbedPane tabPanel;
	private RegisterPanel registerPanel;
	private ActionListener controller;
	
	public ClientFrame(IClient client, ActionListener controller) throws RemoteException {
		super();
		this.client = client;
		this.controller = controller;
		registerPanel = new RegisterPanel(controller);
		
		this.bidsPanel = new BidsPanel(client, controller);
		JScrollPane bidsScroll = new JScrollPane(bidsPanel);
		this.ownedPanel = new OwnedPanel(client, controller);
		JScrollPane ownedScroll = new JScrollPane(ownedPanel);
		this.tabPanel = new JTabbedPane();
		this.tabPanel.addTab("Soummettre un article", new SubmitPanel(client, controller));
		this.tabPanel.addTab("Mes achats", ownedScroll);
		this.tabPanel.addTab("Enchères", bidsScroll);
		this.tabPanel.setSelectedIndex(2);
		
		
		this.setTitle("Gringott - Service d'enchère pour sorciers");
		this.setSize(800,600);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.add(tabPanel);
		this.add(registerPanel);
		this.setContentPane(registerPanel);
	}

	public Container getTabPanel() {
		return this.tabPanel;
	}
	
	public SubmitPanel getSubmitPanel() {
		return (SubmitPanel) this.tabPanel.getComponentAt(0);
	}

	public RegisterPanel getRegisterPanel() {
		return this.registerPanel;
	}

	public void rebuild() throws RemoteException {
		this.tabPanel.remove(1);
		this.tabPanel.remove(1);
		this.bidsPanel = new BidsPanel(this.client, this.controller);
		JScrollPane bidsScroll = new JScrollPane(bidsPanel);
		this.ownedPanel = new OwnedPanel(this.client, this.controller);
		JScrollPane ownedScroll = new JScrollPane(ownedPanel);
		this.tabPanel.add("Mes achats", ownedScroll);
		this.tabPanel.add("Enchères", bidsScroll);
		this.tabPanel.setSelectedIndex(2);
	}
	
	@Override
	public void dispose() {
		try {
			if (this.client.getPseudo() != null) {
				this.client.getServer().logout(client);
				this.client.setPseudo(null);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.dispose();
		System.exit(NORMAL);
	}
		
}
