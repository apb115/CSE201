import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class UserInterface  {
	static JTextArea appData;
	static JTextArea tableHeader;
	static String userName;
	static String pw;

	public static void main(String[] args) {
		// Read in the app data from a file and add
		// the list to our user interface:
		ArrayList<App> appList = App.readAppsFromFile();
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		new UserInterface(appList, buttonList);
	}

	// =========== FRAME ===========
	JFrame window = new JFrame("Appic");

	// Builds the table for app list to be displayed:
	public UserInterface(ArrayList<App> appList, ArrayList<JButton> buttonList) {
		constructDisplay(appList, buttonList);
		window.setBounds(50, 50, 1400, 700);
		window.setAlwaysOnTop(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setVisible(true);
	}

	// Builds the layout of the home page:
	private void constructDisplay(ArrayList<App> appList, ArrayList<JButton> buttonList) {
		Container container = window.getContentPane();
		Container functions = new Container();
		container.setLayout(new BorderLayout(10, 10));
		functions.setLayout(new BorderLayout(10, 10));

		// "Appic" title to appear in top left of home screen:
		JLabel appicLabel = new JLabel("Appic ");
		appicLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 50));

		// =========== Menu and Help Button Panel ===========
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));

		JMenuBar mb = new JMenuBar();
		mb.setMaximumSize(new Dimension(400, 150));

		// Items to appear in menu bar: 
		JMenuItem menuItem = new JMenuItem("Menu");
		JMenuItem helpItem = new JMenuItem("Help");
		JMenuItem loginItem = new JMenuItem("Login");
		JMenuItem createAccountItem = new JMenuItem("Create Account");

		// Ability for menu bar items to be clicked:
		helpItem.addActionListener(new MenuActionListener());
		loginItem.addActionListener(new MenuActionListener());
		createAccountItem.addActionListener(new MenuActionListener());

		// Make menu bar items change color when hovered over: 
		menuItem.addMouseListener(new MouseAdapter() {
			Color c = menuItem.getBackground();
			public void mouseEntered(MouseEvent evt) {
				menuItem.setBackground(Color.CYAN);
			}

			public void mouseExited(MouseEvent evt) {
				menuItem.setBackground(c);
			}
		});

		helpItem.addMouseListener(new MouseAdapter() {
			Color c = helpItem.getBackground();
			public void mouseEntered(MouseEvent evt) {
				helpItem.setBackground(Color.CYAN);
			}

			public void mouseExited(MouseEvent evt) {
				helpItem.setBackground(c);
			}
		});

		loginItem.addMouseListener(new MouseAdapter() {
			Color c = loginItem.getBackground();
			public void mouseEntered(MouseEvent evt) {
				loginItem.setBackground(Color.CYAN);
			}

			public void mouseExited(MouseEvent evt) {
				loginItem.setBackground(c);
			}
		});

		createAccountItem.addMouseListener(new MouseAdapter() {
			Color c = createAccountItem.getBackground();
			public void mouseEntered(MouseEvent evt) {
				createAccountItem.setBackground(Color.CYAN);
			}

			public void mouseExited(MouseEvent evt) {
				createAccountItem.setBackground(c);
			}
		});

		// Organize items on menu bar: 
		mb.add(menuItem, BorderLayout.WEST);
		mb.add(helpItem, BorderLayout.CENTER);
		mb.add(loginItem, BorderLayout.EAST);
		mb.add(createAccountItem, BorderLayout.EAST);


		// =========== Search Bar Panel ===========
		JPanel searchPanel = new JPanel();
		JLabel searchLabel = new JLabel("Search: ");
		JTextField search = new JTextField(45);

		// Upon entering text and clicking 'Enter', program searches for matches
		search.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				search(appList, search.getText(), buttonList);
			}});

		// GENRE - When check box is selected, the search will filter
		// apps by the genre entered in the search box:
		JCheckBox genre = new JCheckBox("Genre");
		genre.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) { //check box has been selected
					filter(search.getText(), appList, true, buttonList);
				} else {
					// When box is de-selected, all apps are displayed again:
					deFilter(appList, buttonList);
				};
			}
		});

		// PRICE - When check box is selected, the search will
		// filter apps by the price entered in the search box:
		JCheckBox price = new JCheckBox("Price");
		price.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					filter(search.getText(), appList, false, buttonList);
				} else {
					// When box is de-selected, all apps are displayed again:
					deFilter(appList, buttonList);
				};
			}
		});

		// =========== Application Display Panel ===========
		JPanel appText = new JPanel();
		tableHeader = new JTextArea(5, 150);
		appData = new JTextArea(29, 150);
		JScrollPane appScrollPane = new JScrollPane(appData);
		appText.add(appScrollPane, BorderLayout.NORTH);

		JTextArea topApps = new JTextArea(5, 50);
		JScrollPane topAppsScroll = new JScrollPane(topApps);
		searchPanel.add(topAppsScroll, BorderLayout.NORTH);
		topApps.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

		// Get top rated apps:
		ArrayList<App> top10List = new ArrayList<>();
		top10List = App.getTop10(appList);

		topApps.append("Top 10 Apps:\n");
		for (int i = 0; i < 5; i++) {
			topApps.append(String.format("%-20s %-20s%n", 
					(i + 1 + ". ") + top10List.get(i).getName(), (i + 6 + ". ") + top10List.get(i + 5).getName()));
		}
		topApps.setEditable(false);

		// =========== Adding all elements ===========
		menuPanel.add(mb, BorderLayout.NORTH);

		searchPanel.add(appicLabel, BorderLayout.NORTH);
		searchPanel.add(searchLabel, BorderLayout.WEST);
		searchPanel.add(search, BorderLayout.WEST);
		searchPanel.add(search, BorderLayout.WEST);
		searchPanel.add(genre, BorderLayout.CENTER);
		searchPanel.add(price, BorderLayout.CENTER);
		searchPanel.add(topApps, BorderLayout.EAST);

		functions.add(menuPanel, BorderLayout.NORTH);
		functions.add(searchPanel, BorderLayout.CENTER);
		functions.add(appText, BorderLayout.SOUTH);

		container.add(functions, BorderLayout.NORTH);
		App.populateGUI(appData, appList, buttonList);

	}
	
	/*
	 * Clears out all elements from JTextArea
	 */
	public static void clear(ArrayList<JButton> buttonList) {
		appData.setText("");
		for (int i = 0; i<buttonList.size(); i++) {
			appData.remove(buttonList.get(i));
		}
	}

	/*
	 * shows Apps matching filter category and query
	 */
	public static void filter(String param, ArrayList<App> appList, boolean filterCat, ArrayList<JButton> buttonList) {
		ArrayList<App> vals = new ArrayList<App>();
		if (filterCat) {
			for (int i = 0; i<appList.size(); i++) {
				if (appList.get(i).getGenre().contains(param)) {vals.add(appList.get(i));}
			}
		} else {
			for (int i = 0; i<appList.size(); i++) {
				if (appList.get(i).getPrice() <= Double.parseDouble(param)) {vals.add(appList.get(i));}
			}
		}
		clear(buttonList);
		App.populateGUI(appData, vals, buttonList);
	}
	
	/*
	 * Reverts UI back to original list
	 */
	public static void deFilter(ArrayList<App> appList, ArrayList<JButton> buttonList) {
		clear(buttonList);
		App.populateGUI(appData, appList, buttonList);
	}
	
	/*
	 * Searches Apps for matching name or similar name characters
	 */
	public static void search(ArrayList<App> appList, String name, ArrayList<JButton> buttonList) {
		ArrayList<App> results = new ArrayList<App>();
		clear(buttonList);
		App.setTitles(appData);
		App.setTitles(tableHeader);
		boolean tester = false;
		for (int i = 0; i<appList.size(); i++) {
			if (appList.get(i).getName().equals(name)) {
				results.add(appList.get(i));
				App.populateGUI(appData, results, buttonList);
				tester = true;
				break;
			}
		}
		
		if (!tester) {
			for (int i = 0; i<appList.size(); i++) {
				if (appList.get(i).getName().contains(name)) {results.add(appList.get(i));}
				if (i == appList.size()-1) { App.populateGUI(appData, results, buttonList); break; }
			}
		}
		
		// Reverts UI back to original form
		if (name.equals("")) {clear(buttonList); App.populateGUI(appData, appList, buttonList);}
	}

	class MenuActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String item = e.getActionCommand();

			if (item.equals("Help")) {
				JOptionPane.showMessageDialog(window, 
						"How to use: \n     Select an app to view a more detailed description of its features, current"
								+ " version, rating, genre, and price\n" + "To add an app to your personal library:\n"
								+ "     Tap the '+' icon in the top right corner of the library.\n"
								+ "To rate an app: \n     Press on the app and enter the appropriate number of stars.",
								"How to Use", JOptionPane.QUESTION_MESSAGE);

			} else if (item.equals("Login")) {
				// Prompt user to enter username and store it:
				userName = JOptionPane.showInputDialog(window, 
						"Enter Username: ", "Login", JOptionPane.DEFAULT_OPTION);
				// Prompt user to enter password and store it:
				pw = JOptionPane.showInputDialog(window, "Enter Password: ", "Login", JOptionPane.DEFAULT_OPTION);

				// Check if user information is valid, if not - display an error message:
				if (!Account.readAccounts(userName, pw, false)) {
					JOptionPane.showMessageDialog(window, "Either Username and/or Password input was invalid. \nPlease try logging in again", 
							"Invalid", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(window, "Login successful!", "Success", JOptionPane.PLAIN_MESSAGE);
				}

			} else if (item.equals("Create Account")) {
				Account a = new Account();
				a.setIsAdmin(false);

				// Prompt user to enter a username and store it:
				userName = JOptionPane.showInputDialog(window, 
						"Enter New Username: ", 
						"Create Account", JOptionPane.DEFAULT_OPTION);
				boolean goodUn = a.setUserName(userName);

				// Only proceed if username is valid:
				if(goodUn) {
					a.setUserName(userName);
					// Prompt user to enter a password and store it:
					pw = JOptionPane.showInputDialog(window, "Enter New Password: ", "Create Account", JOptionPane.DEFAULT_OPTION);

					boolean goodPw = a.setPw(pw);
					if (!goodPw) {
						JOptionPane.showMessageDialog(window, "Invalid password\nMust be at least 8 characters long\n"
								+ "Must contain at least one uppercase character\nMust contain at least one number\n", 
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						a.setPw(pw);
						// Check if user already exists - if not, display confirmation:
						if (Account.readAccounts(userName, pw, true)) {
							JOptionPane.showMessageDialog(window, "Username already associated with account. \nTry Creating Account again", 
									"Error", JOptionPane.ERROR_MESSAGE);
						} else {
							Account.writeAccounts(userName, pw);
							JOptionPane.showMessageDialog(window, "Account created successfully!", 
									"Success", JOptionPane.PLAIN_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(window, "Invalid username - must be at least 5 characters long.", 
							"Error", JOptionPane.ERROR_MESSAGE);
				}

			}

		}

	}

}
