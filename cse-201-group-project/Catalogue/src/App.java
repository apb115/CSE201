import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.*;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * The App class provides the ability to create App objects that
 * include the following properties: name, version, genre, price,
 * description, rating, availability on the Apple app store, and
 * availability on the Android app store.  App data can be read in
 * from a text file and converted to an ArrayList of App objects. 
 */

public class App {
	// ----------------------------------------------------------------------------- Properties
	String name;
	String version;
	String genre;
	double price;
	String desc;
	int rating;
	boolean apple;
	boolean android;

	// ----------------------------------------------------------------------------- Constructors
	public App(String name, String version, String genre, double price, String desc, int rating, boolean apple, boolean android) {
		this.name = name;
		this.version = version;
		this.genre = genre;
		this.price = price;
		this.desc = desc;
		this.rating = rating;
		this.apple = apple;
		this.android = android;
	}

	public App() {}

	// ----------------------------------------------------------------------------- Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isApple() {
		return apple;
	}

	public void setApple(boolean apple) {
		this.apple = apple;
	}

	public boolean isAndroid() {
		return android;
	}

	public void setAndroid(boolean android) {
		this.android = android;
	}

	// ----------------------------------------------------------------------------- Methods
	/* 
	 * Read in App data from a text file, create app objects from each line,
	 * and then add Apps to an ArrayList.
	 */
	public static ArrayList<App> readAppsFromFile() {
		ArrayList<App> newApp = new ArrayList<App>(); 
		Scanner in = null;
		try {
			File read = new File("MOCK_DATA.txt");
			in = new Scanner(read);
			while (in.hasNextLine()) {
				String newLine = in.nextLine();
				String[] appLine = newLine.split(", ");
				App intoApp = new App(appLine[0], appLine[1], appLine[2], Double.parseDouble(appLine[3]), 
						appLine[4], Integer.parseInt(appLine[5]), Boolean.parseBoolean(appLine[6]), 
						Boolean.parseBoolean(appLine[7]));
				newApp.add(intoApp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception f) {
				f.printStackTrace();
			}
		}
		return newApp;
	}

	/*
	 * Retrieve the 10 highest-rated apps from an ArrayList
	 * of apps and add them to their own ArrayList.
	 */
	public static ArrayList<App> getTop10(ArrayList<App> appList) {
		ArrayList<App> top10List = new ArrayList<App>();
		int max = -1;

		// Loop through all apps:
		for (int i = 0; i < appList.size(); i++) {
			// If 10 apps are already in list, but we found an app that
			// has a higher rating, remove the lowest ranked app and 
			// add in the higher rated app:
			if (top10List.size() >= 10 && appList.get(i).getRating() >= max) {
				removeLowRankApp(top10List);
				top10List.add(appList.get(i));
				max = appList.get(i).getRating();
			}
			else if (appList.get(i).getRating() >= max) {
				top10List.add(appList.get(i));
				max = appList.get(i).getRating();
			}
		}
		return top10List;
	}

	/*
	 * From an ArrayList of apps, remove the app with the
	 * lowest rating.
	 */
	private static void removeLowRankApp(ArrayList<App> appList) {
		int min = 6;
		int minInd = -1;

		for (int i = 0; i < appList.size(); i++) {
			if (appList.get(i).getRating() < min) {
				min = appList.get(i).getRating();
				minInd = i;
			}
		}
		appList.remove(minInd);
	}

	/*
	 * toString method for App objects.
	 */
	public static String toString(App test) {
		String place = String.format("%-20s %-20s %-45s %-20.2f %-35s"
				+ "%-10d", test.getName(), test.getVersion(), test.getGenre(),
				test.getPrice(), test.getDesc(), test.getRating());
		if (test.isApple() && !test.isAndroid()) place += "    Apple Store";
		else if (test.isAndroid() && !test.isApple()) place += "    Android Store";
		else place += "    Both stores";
		return place;
	}

	/*
	 * Creates the row of header titles for App table.
	 */
	public static boolean setTitles(JTextArea appData) {
		appData.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
		String name1 = "NAME:";
		appData.append(String.format("%75s", name1));
		for (int i = 0; i < 60; i++) {
			appData.append("\n");
		}
		return true;
	}
	
	/*
	 * Populates GUI with list of click-able buttons
	 */
	public static boolean populateGUI(JTextArea appData, ArrayList<App> appList, ArrayList<JButton> buttonList) {
		UserInterface.clear(buttonList);
		buttonList.clear();
		appData.setSize(1000, 1040);
		setTitles(appData);

		// Add apps to table:
		JButton appButton;
		int x = 250, y = 40;
		for (int i = 0; i < appList.size(); i++) {
			String app = appList.get(i).getName();
			appButton = new JButton(app);
			appButton.setBounds(x, y, 190, 30);
			buttonList.add(appButton);
			appData.add(buttonList.get(i));
			int placer = i;
			appButton.addActionListener(new ActionListener() { 
				  public void actionPerformed(ActionEvent e) {selectionButtonPressed(appList.get(placer));} 
				} );
			if ((i+1) % 4 == 0 && (i+1) != 0) {y += 40; x = 250;}
			else {x += 250;}	
			if (i == appList.size() -1) return true;
		} 
		return false;
	}
	
	/*
	 * Returns true if an App object does not contain any data
	 */
	public boolean isEmpty() {
		return this == null;
	}
	
	/*
	 * Helper method for filter & search functionality, 
	 * 	checks App categories with a String
	 */
	public boolean contains(String param) {
		if (this.getName().contains(param)) return true;
		if (this.getGenre().contains(param)) return true;
		if (this.getVersion().contains(param)) return true;
		if (this.getDesc().contains(param)) return true;
		return false;
	}
	
	/*
	 * Displays App information upon button click
	 */
	public static boolean selectionButtonPressed(App app) {
		JTextArea appInfo = new JTextArea();
		appInfo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		
		JFrame newFrame = new JFrame(app.getName());
		newFrame.setBounds(50, 50, 700, 300);
		newFrame.setAlwaysOnTop(true);
		newFrame.setVisible(true);
		newFrame.add(appInfo);
		
		JButton appButton = new JButton("Install?");
		appButton.setBounds(0, 0, 190, 30);
		appInfo.add(appButton);
		appButton.addActionListener(new ActionListener() { 
				 public void actionPerformed(ActionEvent e) {appButton.setText("Installed");} 
		} );
		String display1 = String.format("Name: %s%nVersion: %s%nGenre: %s%nPrice: %s%nDescription: "
				+ "%s%nRating: %s%nApple? %s%nAndroid? %s", app.getName(), app.getVersion(), app.getGenre(), Double.toString(app.getPrice()),
				app.getDesc(), Integer.toString(app.getRating()), Boolean.toString(app.isApple()), Boolean.toString(app.isAndroid()));
		appInfo.append(display1);
		return true;
	}
}
