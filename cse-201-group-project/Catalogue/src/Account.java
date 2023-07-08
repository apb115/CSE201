import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.security.auth.login.AccountExpiredException;

public class Account {
    // ----------------------------------------------------------------------------- Properties
    String userName;
    String pw;
    boolean isAdmin;
    private App newApp;

    // ----------------------------------------------------------------------------- Constructor
    Account() {
    	this.userName = "";
    	this.pw = "";
    	this.isAdmin = false;
    }
    
    Account(String userName, String pw, boolean isAdmin) {
    	this.userName = userName;
    	this.pw = pw; 
    	this.isAdmin = isAdmin;
    }
    
    // ----------------------------------------------------------------------------- Methods
    public void makeAdmin(String userName, String pw, boolean isAdmin) {
        isAdmin = true;
    }
    
    public void addApp(String name, String version, String genre, double price, String desc, int rating, boolean apple, boolean android, boolean isAdmin) {

		if (isAdmin) newApp = new App(name, version, genre, price, desc, rating, apple, android);
        else System.out.println("Cannot add app. Access denied"); // printing to console for now
	}
    
    public static boolean readAccounts(String userName, String pw, boolean isAdmin) {
    	Scanner readIn = null;
    	try {
    		File inFile = new File("Accounts.txt");
    		readIn = new Scanner(inFile);
    		while(readIn.hasNextLine()) {
    			String line = readIn.nextLine();
    			String[] acctLine = line.split("~");
    			if (userName.equals(acctLine[0]) && pw.equals(acctLine[1]) && isAdmin == Boolean.parseBoolean(acctLine[2]))
    				return true;
    		}
    	} catch(FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			readIn.close();
    		} catch(Exception f) {
    			f.printStackTrace();
    		}
    	}
    	return false;
    }
    
    public static void writeAccounts(String userName, String password) {
    	BufferedWriter writer = null;
    	try { 
	    	writer = new BufferedWriter(new FileWriter("Accounts.txt", true));
	    	
	    	Account account = new Account();
	    	account.setUserName(userName);
	    	account.setPw(password);
	    	account.setIsAdmin(false); // default to false
    
    		String acc = account.toString();
    	    writer.append(acc);
    	    writer.append("\n");
	    } catch (IOException e) {
	    	System.out.println("An error occurred.");
	    	e.printStackTrace();
	    } finally {
	    	try {
	    		writer.close();
	    	} catch (Exception f) {
	    		f.printStackTrace();
	    	}
	    }
    }
    
    @Override
    public String toString() {
    	return String.format("%s~%s~%b", this.userName, this.pw, this.isAdmin);
    }

    // ----------------------------------------------------------------------------- Getters and Setters
    public String getUserName() {
        return this.userName;
    }

    public boolean setUserName(String userName) {
    	if(userName.length() < 5)
    		return false;
    	else
    		this.userName = userName;
    		return true;
    }

    public boolean isIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPw() {
        return this.pw;
    }

    public boolean setPw(String pw) {
        boolean containsUpper = false;
        boolean containsNumber = false;

        if (pw.length() < 8) {
        	return false;
        }
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isUpperCase(pw.charAt(i))) containsUpper = true;
            else if (Character.isDigit(pw.charAt(i))) containsNumber = true;
        }

        // print statements should be output on gui. Printing to console for now.
        if (containsUpper && containsNumber) {
        	this.pw = pw;
        	return true;
        } else {
        	return false;
        }
    }
}
