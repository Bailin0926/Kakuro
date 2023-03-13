package bus;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

import data.ConnectionFile;

public class GameTheme implements Serializable{

	private static final long serialVersionUID = -2390915335423813534L;
	private String name;
	private Color questionText;
	private Color questionForeground;
	private Color questionBackground;

	private Color answerTextFinish;
	private Color answerTextUnfinished;
	private Color answerTextError;
	private Color answerTextSelect;
	private Color answerBackground;

	public GameTheme() {

	}

	public GameTheme(GameTheme gameTheme) {
		this.name = gameTheme.getName();
		this.questionText = gameTheme.getQuestionText();
		this.questionForeground = gameTheme.getQuestionForeground();
		this.questionBackground = gameTheme.getQuestionBackground();
		this.answerTextFinish = gameTheme.getAnswerTextFinish();
		this.answerTextUnfinished = gameTheme.getAnswerTextUnfinished();
		this.answerTextError = gameTheme.getAnswerTextError();
		this.answerTextSelect = gameTheme.getAnswerTextSelect();
		this.answerBackground = gameTheme.getAnswerBackground();
	}

	public static GameTheme themeDefault() {
		GameTheme gameTheme = new GameTheme();
		gameTheme.setName("Default");
		gameTheme.setQuestionText(new Color(50, 50, 50));
		gameTheme.setQuestionForeground(new Color(200, 200, 200));
		gameTheme.setQuestionBackground(new Color(235, 235, 235));
		gameTheme.setAnswerTextFinish(new Color(0, 150, 0));
		gameTheme.setAnswerTextUnfinished(new Color(0, 0, 0));
		gameTheme.setAnswerTextError(new Color(150, 0, 0));
		gameTheme.setAnswerTextSelect(new Color(242, 242, 242));
		gameTheme.setAnswerBackground(new Color(250, 250, 250));
		return gameTheme;
	}

	public static ArrayList<String> getThemesList() {
		return ConnectionFile.getThemesList();
	}

	public static GameTheme setTheme(String name) {
		Properties props = ConnectionFile.getTheme(name);
		if (props == null) {
			return themeDefault();
		}
		GameTheme gameTheme = new GameTheme();
		gameTheme.setName(name);
		gameTheme.setQuestionText(Color.decode(props.getProperty("questionText")));
		gameTheme.setQuestionForeground(Color.decode(props.getProperty("questionForeground")));
		gameTheme.setQuestionBackground(Color.decode(props.getProperty("questionBackground")));
		gameTheme.setAnswerTextFinish(Color.decode(props.getProperty("answerTextFinish")));
		gameTheme.setAnswerTextUnfinished(Color.decode(props.getProperty("answerTextUnfinished")));
		gameTheme.setAnswerTextError(Color.decode(props.getProperty("answerTextError")));
		gameTheme.setAnswerTextSelect(Color.decode(props.getProperty("answerTextSelect")));
		gameTheme.setAnswerBackground(Color.decode(props.getProperty("answerBackground")));
		return gameTheme;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getQuestionText() {
		return questionText;
	}

	public Color getQuestionForeground() {
		return questionForeground;
	}

	public Color getQuestionBackground() {
		return questionBackground;
	}

	public Color getAnswerTextFinish() {
		return answerTextFinish;
	}

	public Color getAnswerTextUnfinished() {
		return answerTextUnfinished;
	}

	public Color getAnswerTextError() {
		return answerTextError;
	}

	public Color getAnswerBackground() {
		return answerBackground;
	}

	public Color getAnswerTextSelect() {
		return answerTextSelect;
	}

	public void setAnswerTextSelect(Color numberTextSelect) {
		this.answerTextSelect = numberTextSelect;
	}

	public void setQuestionText(Color questionText) {
		this.questionText = questionText;
	}

	public void setQuestionForeground(Color questionForeground) {
		this.questionForeground = questionForeground;
	}

	public void setQuestionBackground(Color questionBackground) {
		this.questionBackground = questionBackground;
	}

	public void setAnswerTextFinish(Color numberTextFinish) {
		this.answerTextFinish = numberTextFinish;
	}

	public void setAnswerTextUnfinished(Color numberTextUnfinished) {
		this.answerTextUnfinished = numberTextUnfinished;
	}

	public void setAnswerTextError(Color numberTextError) {
		this.answerTextError = numberTextError;
	}

	public void setAnswerBackground(Color numberBackground) {
		this.answerBackground = numberBackground;
	}

}
