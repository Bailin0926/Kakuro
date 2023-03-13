package bus;

import java.io.Serializable;

public class GameSetting implements Serializable {
	private static final long serialVersionUID = 2813355737418786130L;
	
	private GameTheme gameTheme;
	private String gameMusicName;
	private EnumGridSize gridSize;
	

	public String getGameMusicName() {
		return gameMusicName;
	}

	public void setGameMusicName(String gameMusicName) {
		this.gameMusicName = gameMusicName;
	}

	public GameTheme getGameTheme() {
		return gameTheme;
	}

	public void setGameTheme(GameTheme gameTheme) {
		this.gameTheme = gameTheme;
	}
	public void setGameTheme(String gameTheme) {
		this.gameTheme = GameTheme.setTheme(gameTheme);
	}

	public EnumGridSize getGridSize() {
		return gridSize;
	}

	public int getGridSizeInt() {
		if(this.gridSize == EnumGridSize.Small) {
			return 45;
		}
		if(this.gridSize == EnumGridSize.Medium) {
			return 50;
		}
		if(this.gridSize == EnumGridSize.Large) {
			return 55;
		}
		return 50;
	}
	
	public void setGridSize(EnumGridSize gridSize) {
		this.gridSize = gridSize;
	}

	public GameSetting() {
		this.setGameTheme(GameTheme.themeDefault());
		this.setGameMusicName("");
		this.setGridSize(EnumGridSize.Medium);
	}
}
