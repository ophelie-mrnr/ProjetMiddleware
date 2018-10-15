package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import shared.Item;
import shared.SellableItem;

public class DBManager {

	private static final String dbPath = "db.json";

	private BufferedReader jsonReader;
	private BufferedWriter jsonWritter;
	private JsonObject root;
	private Gson gson;

	public DBManager(boolean recreate, boolean mock) {
		if (recreate) {
			Path file = Paths.get(dbPath);
			try {
				if (Files.exists(file)){
					Files.delete(file);
				}
				this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
				jsonWritter.write("{\n\"items\": []\n}");
				jsonWritter.flush();
				this.jsonReader = new BufferedReader(new FileReader(dbPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				this.jsonReader = new BufferedReader(new FileReader(dbPath));
			} catch (FileNotFoundException e1) {
				Path file = Paths.get(dbPath);
				try {
					this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
					jsonWritter.write("{\n\"items\": []\n}");
					jsonWritter.flush();
					this.jsonReader = new BufferedReader(new FileReader(dbPath));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.gson = new Gson();
		JsonParser parser = new JsonParser();
		this.root = parser.parse(this.jsonReader).getAsJsonObject();
		if (mock) {
			initDBMock();
		}
	}

	public void addItem(Item i){
		this.root.get("items").getAsJsonArray().add(gson.toJsonTree(i));
		try {
			Path file = Paths.get(dbPath);
			this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
			jsonWritter.write(root.toString());
			jsonWritter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Item> listItems() {
		List<Item> items = new ArrayList<Item>();
		if (root!= null ) {
		JsonElement registeredItems = root.get("items");
			if (registeredItems.isJsonArray()){
				for (JsonElement item : registeredItems.getAsJsonArray()){
					Item i = gson.fromJson(item, SellableItem.class);
					items.add(i);
				}
			}
		}
		
		return items;
	}

	public void updateItem(Item i) {
		// TODO Find item, remove it, replace it with new.
		JsonElement registeredItems = root.get("items");
		if (registeredItems.isJsonArray()){
			for (JsonElement item : registeredItems.getAsJsonArray()){
				if (item.getAsJsonObject().get("name").getAsString().equals(i.getName())) {
					registeredItems.getAsJsonArray().remove(item);
					break;
				}
			}
		}
		this.root.get("items").getAsJsonArray().add(gson.toJsonTree(i));
		try {
			Path file = Paths.get(dbPath);
			this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
			jsonWritter.write(root.toString());
			jsonWritter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initDBMock() {
		Item obj1 = new SellableItem("Botruc", "Petite créature d'une vingtaine de centimètres ayant un aspect végétal et deux longs doigts pointus à chaque main. - Peut crocheter des serrures -", 400, "nDragonneau", 5);	
		Item obj2 = new SellableItem("Cerbère nain", "Chien géant à trois tête servant de gardien - Cet exemplaire est de petite taille -", 250, "nDragonneau", 4);
		Item obj3 = new SellableItem("Demiguise", "Créature pouvant se rendre invisible lorsqu'elle est menacée. - Ses poils servent à tisser des toiles d'invisibilité -" , 900, "nDragonneau", 3);
		Item obj4 = new SellableItem("Démonzémerveille", "Créature apparaissant sous forme de boule et se transformant, quand on la lance, en oiseau de proie bleu et vert. - A un attrait particulier pour le cerveau humain -", 1000, "nDragonneau", 2);
		Item obj5 = new SellableItem("Éruptif", "Sorte de Rhinocéros géant vivant en Afrique. Le fluide contenu dans sa corne peut être injecté dans tout type de materiau, provoquant l'explosion de celui-ci. - Sa peau épaisse le rend insensible à la plupart des sorts -", 600, "nDragonneau", 2);
		Item obj6 = new SellableItem("Plume d'Hippogriffe", "L'hippogriffe est une créature volante mi-aigle, mi- cheval. Il est très dangereux tant qu'il n'est pas dressé. - Cette plume a été récoltée dans les alentours de Poudlard et mesure 50 cm -", 150, "nDragonneau", 3);
		Item obj7 = new SellableItem("Niffleur", "Animal à la fourrure noire et au long museau semblable à un ornithorynque. Ils sont attirés par tout ce qui brille. - Formidable voleur -", 250, "nDragonneau", 4);
		Item obj8 = new SellableItem("OEuf d'Occamy", "Les Occamy sont une sorte d'oiseau-serpent. Ils ont la particularité d'être choranaptyxique : leur taille varient en fonction de l'espace dont ils disposent. - La coquille des oeufs d'Occamy est en argent pur -", 700, "nDragonneau", 6);
		Item obj9 = new SellableItem("Oiseau-Tonnerre", "Vivant en Arizona, ces oiseau provoquent des tempêtes lorsqu'ils se sentent menacés. - Leur plume peuvent être utilisées pour fabriquer des baguettes magiques", 1250, "nDragonneau", 5);
		Item obj10 = new SellableItem("OEuf congelé de Serpencendre", "Les serpencendres naissent dans des feux magiques laissés sans surveillance. Ils se cachent dans des recoins de la maison pour y pondre leurs oeufs qui, s'ils réussissent à grandir sans être repérés et chassés, enflamment la maison." , 2000, "nDragonneau", 1);
		 
		this.addItem(obj1);
		this.addItem(obj2);
		this.addItem(obj3);
		this.addItem(obj4);
		this.addItem(obj5);
		this.addItem(obj6);
		this.addItem(obj7);
		this.addItem(obj8);
		this.addItem(obj9);
		this.addItem(obj10);
	}

}
