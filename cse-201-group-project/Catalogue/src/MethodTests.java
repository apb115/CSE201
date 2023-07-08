import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTextArea;
import org.junit.jupiter.api.Test;

class MethodTests {
	
	@Test
	void readAppsFromFileTest() {
		ArrayList<App> test = App.readAppsFromFile();
		assertFalse(test.isEmpty()); // Confirms file correctly loaded in
		assertTrue(test.get(0).getName().equals("Voyatouch")); // Confirms first file input is correct
		assertTrue(test.get(test.size()-1).getGenre().equals("Drama")); // Confirms last file input is correct
		assertEquals(test.size(), 100);
	}
	
	@Test
	void toStringTest() {
		ArrayList<App> test = App.readAppsFromFile();
		String test2 = App.toString(test.get(0));
		assertFalse(test2.isEmpty()); // Confirms test2 has size
		assertFalse(test2.contains("4.8"));
		assertTrue(test2.contains("Documentary"));
		assertTrue(test2.contains("4.78"));
		assertTrue(test2.contains("Gazella granti"));
	}
	
	@Test
	void populateGUITest() {
		ArrayList<App> appList = App.readAppsFromFile();
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		JTextArea appData = new JTextArea(29, 106);
		boolean checker = App.populateGUI(appData, appList, buttonList);
		assertTrue(appData.getLineCount() > 0);
		assertTrue(buttonList.size() == 100);
		assertTrue(appData.contains(499, 499));
		assertEquals(checker, true);	
	}
	
	@Test
	void searchTest() {
		ArrayList<App> appList = App.readAppsFromFile();
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		UserInterface test = new UserInterface(appList, buttonList);
		test.search(appList, "Pannier", buttonList);
		assertFalse(test.appData.getText().equals(""));
		boolean checker = false;
		for (int i = 0; i < buttonList.size(); i++) {
			if (buttonList.get(i).getText().equals("Pannier")) {checker = true;}
		}
		assertTrue(checker);
	}
	
	@Test
	void filterTest() {
		ArrayList<App> appList = App.readAppsFromFile();
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		UserInterface test = new UserInterface(appList, buttonList);
		test.filter("Drama", appList, true, buttonList);
		assertFalse(test.appData.getText().equals(""));
		boolean checker = false, checker2 = false;
		for (int i = 0; i < buttonList.size(); i++) {
			if (buttonList.get(i).getText().equals("Flowdesk")) {checker = true;}
			if (buttonList.get(i).getText().equals("Voltsillam")) {checker2 = true;}
		}
		assertTrue(checker);
		assertTrue(checker2);
		
		test.deFilter(appList, buttonList);
		test.filter("1.99", appList, false, buttonList);
		boolean checker3 = false, checker4 = false;
		for (int i = 0; i < buttonList.size(); i++) {
			if (buttonList.get(i).getText().equals("Regrant")) {checker3 = true;}
			if (buttonList.get(i).getText().equals("Tampflex")) {checker4 = true;}
		}
		assertTrue(checker3);
		assertTrue(checker4);
	}
	
	@Test
	void top10Test() {
		ArrayList<App> appList = App.readAppsFromFile();
		ArrayList<App> top10List = App.getTop10(appList);
		
		for (int i = 0; i < top10List.size(); i++) {
			assertTrue(top10List.get(i).getRating() == 5);
		}
		
	}
	
	@Test
	void selectionButtonPressed() {
		ArrayList<App> appList = App.readAppsFromFile();
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		UserInterface test = new UserInterface(appList, buttonList);
		assertTrue(App.selectionButtonPressed(appList.get(0)));
	}
	
	@Test
	void setTitlesTest() {
		JTextArea appData = new JTextArea();
		ArrayList<App> appList = App.readAppsFromFile();
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		UserInterface test = new UserInterface(appList, buttonList);
		assertTrue((appList.get(0)).setTitles(appData));
	}
	
	@Test
	void clearTest() {
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		JTextArea appData = new JTextArea();
		JButton btn1 = new JButton();
		buttonList.add(btn1);
		appData.add(buttonList.get(0));
		appData.append("Test");
		assertFalse(appData.getText().equals(""));
		UserInterface.clear(buttonList);
	}
}
