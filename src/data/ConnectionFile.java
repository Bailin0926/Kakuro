package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.ImageIcon;

import bus.Game;
import bus.GameSetting;
import bus.ResultList;

public class ConnectionFile {
	private static String themesPath = System.getProperty("user.dir") + "\\data\\themes\\";
	private static String gamePath = System.getProperty("user.dir") + "\\data\\game\\game.dat";
	private static String settingPath = System.getProperty("user.dir") + "\\data\\setting\\setting.dat";
	private static String iconPath = System.getProperty("user.dir") + "\\data\\image\\icon.png";
	private static String musicPath = System.getProperty("user.dir") + "\\data\\music\\";
	private static String resultPath = System.getProperty("user.dir") + "\\data\\game\\result.dat";
	
	public static ArrayList<String> getThemesList() {
		ArrayList<String> themesList = new ArrayList<String>();
		File themes = new File(themesPath);
		File[] tempList = themes.listFiles();
		if (tempList == null) {
			return themesList;
		}
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				themesList.add(tempList[i].getName().split("\\.")[0]);
			}
		}
		return themesList;
	}

	public static Properties getTheme(String name) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(themesPath + name + ".properties"));
		} catch (Exception e) {
			return null;
		}
		return props;
	}
	public static ArrayList<String> getMusicList() {
		ArrayList<String> themesList = new ArrayList<String>();
		File themes = new File(musicPath);
		File[] tempList = themes.listFiles();
		if (tempList == null) {
			return themesList;
		}
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				themesList.add(tempList[i].getName().split("\\.")[0]);
			}
		}
		return themesList;
	}
	
	public static File getMusic(String name) {
		return new File(musicPath + name +".wav");
	}
	
	public static void saveResult(ResultList result) {
		File file = new File(resultPath);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(result);
			objOut.flush();
			objOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ResultList loadResult() {
		ResultList temp = null;
		File file = new File(resultPath);
		
		if (!file.exists()) {
			return null;
		}
		
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = (ResultList) objIn.readObject();
			objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	public static void saveSetting(GameSetting gameSetting) {
		File file = new File(settingPath);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(gameSetting);
			objOut.flush();
			objOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static GameSetting loadGameSetting() {
		GameSetting temp = null;
		File file = new File(settingPath);
		
		if (!file.exists()) {
			return null;
		}
		
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = (GameSetting) objIn.readObject();
			objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	public static void saveGame(Game game) {
		File file = new File(gamePath);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(game);
			objOut.flush();
			objOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Game loadGame() {
		Game temp = null;
		File file = new File(gamePath);
		
		if (!file.exists()) {
			return null;
		}
		
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = (Game) objIn.readObject();
			objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		file.delete();
		return temp;
	}

	public static ImageIcon loadIcon() {
		return new ImageIcon(iconPath);
	}
}
