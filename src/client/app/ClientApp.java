package client.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import client.view.BidButton;
import client.view.ClientFrame;
import shared.IClient;
import shared.IServer;
import shared.Item;

public class ClientApp extends UnicastRemoteObject implements IClient, ActionListener {

	private static final long serialVersionUID = 1373624286313090112L;
	private ClientFrame view;
	private String pseudo;
	private List<Item> items;
	private IServer server;

	public ClientApp(String url) throws MalformedURLException, RemoteException, NotBoundException {
		this.items = new ArrayList<Item>();
		this.view = new ClientFrame(this, this);
		this.view.setVisible(true);
		this.server = (IServer) Naming.lookup("//" + url);
	}
	
	public void updateView() throws RemoteException {
		this.view.rebuild();
		this.view.repaint();
		this.view.revalidate();
	}

	@Override
	public void addNewItem(Item item) throws RemoteException {
		boolean contains = false;
		for (Item i : items){
			if (i.getName().equals(item.getName())){
				contains = true;
			}
		}
		if (!contains){
			System.out.println("Nouvel item ajout� : " + item.getName());
			this.items.add(item);
		}
		this.updateView();
	}

	@Override
	public void update(Item item, double newPrice, String buyer) throws RemoteException {
		for (Item i : items){
			if (i.getName().equals(item.getName()) && !i.isSold()){
				System.out.println("Mise � jour de l'item : " + i.getName());
				i.setPrice(newPrice);
				i.setLeader(buyer);
				this.updateView();
			}
		}
	}

	@Override
	public void endSelling(Item item) throws RemoteException {
		for (Item i : items){
			if (i.getName().equals(item.getName())){
				System.out.println("Fin de la vente : " + i.getName());
				i.setSold(true);
				this.updateView();
			}
		}
	}

	@Override
	public String getPseudo() throws RemoteException {
		return this.pseudo;
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Connexion":
				try {
					
				//if( this.pseudo == null || this.pseudo == ""){
				//	JOptionPane.showMessageDialog(view, "Veuillez entrer un pseudo.", "Message d'erreur", JOptionPane.INFORMATION_MESSAGE);
				//}
				
				//else{
					this.pseudo = this.view.getRegisterPanel().getFieldContent();
					this.server.registerClient(this);
					this.view.setContentPane(view.getTabPanel());
					this.updateView();
				//}
			}  
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			break;
			
		case "Soumettre":
			try {
				Item item = this.view.getSubmitPanel().getFieldsContent();
				this.server.submit(item);
				this.view.getSubmitPanel().clear();
			} catch (NumberFormatException e1) {
				System.out.println("Merci de mettre des nombres.");
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			break;
		case "Encherir":
			try {
				BidButton source = (BidButton) e.getSource();
				if (Double.parseDouble(source.getContent()) >= source.getItem().getPrice()*0.2) {
					this.server.bid(source.getItem(), Double.parseDouble(source.getContent()), this.getPseudo());
				} else {
					System.out.println("Vous devez encherir d'au moins 20% du prix courant.");
				}
			} catch (NumberFormatException e1) {
				System.out.println("Merci de mettre un nombre.");
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			break;
		case "Deconnexion":
			this.view.setContentPane(view.getRegisterPanel());
			try {
				server.logout(this);
				this.pseudo = null;
				this.updateView();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			break;
		default:
			System.out.println(e.getActionCommand() + " button has been clicked but i don't care.");
		}
	}

	@Override
	public IServer getServer() {
		return this.server;
	}
	
	@Override
	public List<Item> getItems() throws RemoteException {
		return this.items;
	}

	@Override
	public void setPseudo(String pseudo) throws RemoteException {
		this.pseudo = pseudo;
	}

	public static void main(String[] args) {
		try {
			String serverURL = "localhost:8090/enchere";
			ClientApp c = new ClientApp(serverURL);
			System.out.println("Connexion au serveur " + serverURL + " reussi.");
		} catch (RemoteException e) {
			System.out.println("Connexion au serveur impossible.");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("Erreur dans l'adresse du serveur.");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("Serveur inconnu.");
			e.printStackTrace();
		}
	}
	
}
