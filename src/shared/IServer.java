package shared;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import server.DBManager;

public interface IServer extends Remote, Serializable {

	/**
	 * Register a freshly connected client.
	 * @param client the new client
	 * @throws RemoteException
	 */
	void registerClient(IClient client) throws RemoteException;

	/**
	 * Log out a client.
	 * @param client the client to log out.
	 * @throws RemoteException
	 */
	void logout(IClient client) throws RemoteException;
	
	/**
	 * Record a new bid from a a client for an item.
	 * @param item the item.
	 * @param newPrice the bid amount.
	 * @param buyer the client.
	 * @throws RemoteException
	 */
	void bid(Item item, double newPrice, String buyer)  throws RemoteException;
	
	/**
	 * Record a new Item.
	 * @param item the item
	 * @throws RemoteException
	 */
	void submit(Item item)  throws RemoteException;
	
	/**
	 * List server's items
	 * @return the items.
	 * @throws RemoteException
	 */
	List<Item> getItems()  throws RemoteException;
	
	/**
	 * List server's clients
	 * @return the clients.
	 * @throws RemoteException
	 */
	List<IClient> getClients() throws RemoteException;
	
	/**
	 * Get the server's db
	 * @return the db.
	 * @throws RemoteException
	 */
	DBManager getDB() throws RemoteException;
	
}
