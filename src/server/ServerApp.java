package server;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shared.IClient;
import shared.IServer;
import shared.Item;

public class ServerApp extends UnicastRemoteObject implements IServer {

	private static final long serialVersionUID = -8168686161180269490L;

	DBManager dbManager;
	List<IClient> clients;
	List<Item> items;

	public ServerApp() throws RemoteException, FileNotFoundException {
		this.dbManager = new DBManager(true, true);
		this.clients = new ArrayList<IClient>();
		this.items = this.dbManager.listItems();
	}

	@Override
	public void registerClient(IClient client) throws RemoteException {
		System.out.println("New client registered : " + client.getPseudo());
		this.clients.add(client);
		for (Item i : items) {
			client.addNewItem(i);
		}
	}
	
	@Override
	public void logout(IClient client) throws RemoteException {
		System.out.println(client.getPseudo() + " logged out.");
		for (IClient c : clients) {
			if (c.getPseudo().equals(client.getPseudo())) {
				this.clients.remove(client);
			}
			break;
		}
		System.out.println(clients.size() > 0 ? "Still connected : " + clients : "No clients connected now.");
	}

	@Override
	public void bid(Item item, double newPrice, String buyer) throws RemoteException {
		double price = item.getPrice() + newPrice;
		System.out.println("New bid from " + buyer + " recorded for " + item.getName() + " at " + price);
		
		for (Item i : items) {
			if (i.getName().equals(item.getName())){
				i.setPrice(price);
				i.setLeader(buyer);
				dbManager.updateItem(i);
			}
		}
		
		for (IClient c : clients) {
			c.update(item, price, buyer);
		}
	}

	@Override
	public void submit(Item item) throws RemoteException {
		System.out.println("New item registered : " + item);
		this.items.add(item);
		dbManager.addItem(item);
		for (IClient c : clients) {
			c.addNewItem(item);
		}
	}
	
	@Override
	public List<Item> getItems() {
		return this.items;
	}
	
	@Override
	public List<IClient> getClients() throws RemoteException {
		return this.clients;
	}
	
	@Override
	public DBManager getDB() {
		return this.dbManager;
	}
	
	public static void main(String[] args) {
		try {
			int port = 8090;
			LocateRegistry.createRegistry(port);
			IServer s = new ServerApp();
			Naming.bind("//localhost:" + port + "/enchere", s);

			System.out.println("Adresse : localhost:" + port + "/enchere");

			while (true) {
				for (Item i : s.getItems()) {
					Date localDate = new Date(System.currentTimeMillis());
					if (i.getTime().compareTo(localDate) < 0 && !i.isSold()) {
						for (IClient c : s.getClients()) {
							i.setSold(true);
							s.getDB().updateItem(i);
							c.endSelling(i);
						}
					}
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
