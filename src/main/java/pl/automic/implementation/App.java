package pl.automic.implementation;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.uc4.api.SearchResultItem;

import pl.automic.Automic;
import pl.automic.communication.requests.SearchObject;

public class App {
	Automic automic;
	File config;
	File filter;
	
	public App(String[] args) {
		this.config = new File(readArg(0, args, "config.json"));
		this.filter = new File(readArg(1, args, "filter.json"));
		
		try {
			this.automic = new Automic(config);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		App app = new App(args);
		
		app.search().forEachRemaining(e -> System.out.println(e.getName()));
		app.exit();
	}

	private void exit() {
		try {
			this.automic.exit();
		} catch (IOException e) {
			
		}
	}
	
	private Iterator<SearchResultItem> search() {
		try {
			SearchObject so = new SearchObject(filter);

			// TODO Temp select all object types
			so.selectAllObjectTypes();
			automic.send(so);
			
			return so.resultIterator();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String readArg(int pos, String[] args, String def) {
		return args.length > pos ? args[pos] : def;
	}
}
