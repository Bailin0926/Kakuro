package prod;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import bus.*;
import data.ConnectionFile;

public class WindowsGame extends JFrame {

	private static final long serialVersionUID = 412229668665471284L;
	private JPanel contentPane;
	private JPanel gameTopBarPane;
	private JMenuBar menuBar;
	private JPanel gamePane;
	private JDialog jd;
	private boolean isPausedLostFocus;

	private Game game = null;
	private GameSetting gameSetting = null;
	private GameMusic gameMusic = null;

	public WindowsGame windowsGame = this;

	public static void main(String[] args) {
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//				| UnsupportedLookAndFeelException e1) {
//			e1.printStackTrace();
//		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowsGame frame = new WindowsGame();
					frame.setTitle("Kakuro");
					frame.setIconImage(ConnectionFile.loadIcon().getImage());
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public WindowsGame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		gameSetting = ConnectionFile.loadGameSetting();
		if (gameSetting == null) {
			gameSetting = new GameSetting();
		}
		gameMusic = new GameMusic(gameSetting.getGameMusicName());
		game = ConnectionFile.loadGame();
		if (game != null) {
			int input = JOptionPane.showConfirmDialog(contentPane, "Do you want to load the previous game?",
					"Load game?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (input == 1) {
				jd = new WindowsNew(windowsGame, true);
			}
		} else {
			jd = new WindowsNew(windowsGame, true);
		}

		System.out.print(game.getAnswerGrid());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				if (!game.isFinish()) {
					int input = JOptionPane.showConfirmDialog(contentPane,
							"Do you want to save the game before exitting?", "Save game?", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (input == 0) {
						game.setPaused(true);
						ConnectionFile.saveGame(game);
					}

				}
			}
		});

		addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				gameMusic.play();
				if (isPausedLostFocus) {
					game.setPaused(false);

					loadPane();
				}

			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				gameMusic.pause();
				if (game.isPaused()) {
					isPausedLostFocus = false;
				} else {
					isPausedLostFocus = true;
					game.setPaused(true);

					loadPane();
				}
			}
		});
		game.setPaused(false);
		loadPane();

	}

	public void loadPane() {
		setSize(game.getColumnSize() * gameSetting.getGridSizeInt(),
				game.getRowSize() * gameSetting.getGridSizeInt() + 50);
		contentPane.removeAll();
		gameTopBarPane = creatGameTopBarPane();
		gamePane = creatGamePane();
		menuBar = creatMenuBar();
		setJMenuBar(menuBar);
		contentPane.add(gamePane, BorderLayout.CENTER);
		contentPane.add(gameTopBarPane, BorderLayout.PAGE_START);
		contentPane.setBackground(gameSetting.getGameTheme().getQuestionBackground());
		contentPane.revalidate();
		contentPane.repaint();

	}

	public JMenuBar creatMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu gameMenu = new JMenu("Game");
		JMenuItem gameNewItem = new JMenuItem("New Game...");
		gameNewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jd = new WindowsNew(windowsGame, false);
			}
		});
		JMenuItem gamePauseContinuetItem = new JMenuItem();
		gamePauseContinuetItem.setText((game.isPaused()) ? "Continue" : "Pause");
		gamePauseContinuetItem.setEnabled(!game.isFinish());
		gamePauseContinuetItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game.isPaused()) {
					game.setPaused(false);
					gameMusic.play();
				} else {
					game.setPaused(true);
					gameMusic.pause();
				}

				loadPane();
			}
		});
		JMenuItem gameClearItem = new JMenuItem("Clear All");
		gameClearItem.setEnabled(!game.isFinish());
		gameClearItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.clearAll();
				loadPane();
			}
		});
		JMenuItem gameSolutionItem = new JMenuItem("Show Solution");
		gameSolutionItem.setEnabled(!game.isFinish());
		gameSolutionItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.showSolution();
				loadPane();
			}
		});
		JMenuItem gameExitItem = new JMenuItem("Exit");
		gameExitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!game.isFinish()) {
					int input = JOptionPane.showConfirmDialog(gameExitItem,
							"Do you want to save the game before exitting?", "Save game?",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (input == 0) {
						game.setPaused(true);
						ConnectionFile.saveGame(game);
						System.exit(ABORT);
					} else if (input == 1) {
						System.exit(ABORT);
					}

				} else {
					System.exit(ABORT);
				}

			}
		});
		gameMenu.add(gameNewItem);
		gameMenu.add(gamePauseContinuetItem);
		gameMenu.add(gameClearItem);
		gameMenu.addSeparator();
		gameMenu.add(gameSolutionItem);
		gameMenu.addSeparator();
		gameMenu.add(gameExitItem);
		menuBar.add(gameMenu);

		JMenu settingMenu = new JMenu("Setting");
		JMenu settingMusicMenu = new JMenu("Music");
		ArrayList<String> musicList = gameMusic.getMusicList();
		JMenuItem settingStopMusicItem = new JRadioButtonMenuItem("Stop Music");
		if (gameMusic.getName().equals("")) {
			settingStopMusicItem.setSelected(true);
		}
		settingStopMusicItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameMusic.stop();
				gameSetting.setGameMusicName("");
				ConnectionFile.saveSetting(gameSetting);
				loadPane();
			}
		});
		settingMusicMenu.add(settingStopMusicItem);
		settingMusicMenu.addSeparator();
		for (int i = 0; i < musicList.size(); i++) {
			JMenuItem settingMusicItem = new JRadioButtonMenuItem(musicList.get(i));
			settingMusicItem.setName(musicList.get(i));
			if (gameMusic.getName().equals(settingMusicItem.getName())) {
				settingMusicItem.setSelected(true);
			} else {
				settingMusicItem.setSelected(false);
			}
			settingMusicItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameMusic.setMusic(settingMusicItem.getName());
					gameSetting.setGameMusicName(settingMusicItem.getName());
					ConnectionFile.saveSetting(gameSetting);
					loadPane();
				}
			});
			settingMusicMenu.add(settingMusicItem);
		}
		settingMenu.add(settingMusicMenu);

		JMenu settingThemesMenu = new JMenu("Themes");
		ArrayList<String> themesList = gameSetting.getGameTheme().getThemesList();
		for (int i = 0; i < themesList.size(); i++) {
			JMenuItem settingThemeItem = new JRadioButtonMenuItem(themesList.get(i));
			settingThemeItem.setName(themesList.get(i));
			if (gameSetting.getGameTheme().getName().equals(settingThemeItem.getName())) {
				settingThemeItem.setSelected(true);
			} else {
				settingThemeItem.setSelected(false);
			}

			settingThemeItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameSetting.setGameTheme(settingThemeItem.getName());
					ConnectionFile.saveSetting(gameSetting);
					loadPane();
				}
			});
			settingThemesMenu.add(settingThemeItem);
		}
		settingMenu.add(settingThemesMenu);

		JMenu settingSizeMenu = new JMenu("Size");
		EnumGridSize sizeList[] = EnumGridSize.values();
		for (int i = 0; i < sizeList.length; i++) {
			JMenuItem settingSizeItem = new JRadioButtonMenuItem(sizeList[i].toString());
			settingSizeItem.setName(sizeList[i].toString());
			if (settingSizeItem.getName() == gameSetting.getGridSize().name()) {
				settingSizeItem.setSelected(true);

			} else {
				settingSizeItem.setSelected(false);
			}
			settingSizeItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameSetting.setGridSize(EnumGridSize.valueOf(settingSizeItem.getName()));
					ConnectionFile.saveSetting(gameSetting);
					loadPane();
				}
			});
			settingSizeMenu.add(settingSizeItem);
		}
		settingMenu.add(settingSizeMenu);
		menuBar.add(settingMenu);

		JMenu moreMenu = new JMenu("More");

		JMenuItem settingLeaderboardItem = new JMenuItem("Leaderboard...");
		settingLeaderboardItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WindowsResult wr = new WindowsResult(null, null);
			}
		});

		JMenuItem settingHowItem = new JMenuItem("Game Rules");
		settingHowItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(settingHowItem,
						"<html><body><p style='width: 250px;'>Place the numbers 1 to 9 into the puzzle grid so that each continuous horizontal or vertical run of empty squares adds up to the value to the left of it or above it respectively. This value is shown either to the right or below a diagonal line. Also, the same number cannot be repeated within any run.</p></body></html>",
						"Game Rules", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		JMenuItem settingAboutItem = new JMenuItem("About Kakuro");
		settingAboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(settingAboutItem,
						"Version: 1.0, Developed by: Bailin Zhang, Stephanie Otero, Fang Hou, Mufei Li",
						"About Kakuro", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		moreMenu.add(settingLeaderboardItem);
		moreMenu.addSeparator();
		moreMenu.add(settingHowItem);
		moreMenu.add(settingAboutItem);
		menuBar.add(moreMenu);

		return menuBar;
	}

	public JPanel creatGameTopBarPane() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(gameSetting.getGameTheme().getQuestionBackground());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 1;

		JLabel scoreLabel = new JLabel(getScoreStr());
		scoreLabel.setForeground(gameSetting.getGameTheme().getQuestionText());
		scoreLabel.setFont(new Font("", Font.PLAIN, 18));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		scoreLabel.setHorizontalAlignment(JTextField.LEFT);
		panel.add(scoreLabel, gbc);

		JLabel timerLabel = new JLabel(getTimerStr());
		timerLabel.setForeground(gameSetting.getGameTheme().getQuestionText());
		timerLabel.setFont(new Font("", Font.PLAIN, 18));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		timerLabel.setHorizontalAlignment(JTextField.RIGHT);
		panel.add(timerLabel, gbc);

		Timer SystemTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timerLabel.setText(getTimerStr());
				scoreLabel.setText(getScoreStr());
			}
		});
		SystemTimer.start();
		return panel;
	}

	public String getTimerStr() {
		if (game.isPaused() && !game.isFromAnswer()) {
			if (game.isFinish()) {
				return "Finished";
			}
			return "Paused";
		} else {
			return GameTimer.timeToString(game.getTimmer().getTime());
		}
	}

	public String getScoreStr() {
		if (game.isFromAnswer()) {
			return "Solution";
		} else {
			return Integer.toString(game.getScore().intValue());
		}
	}

	public JPanel creatGamePane() {
		JPanel panel = new JPanel(new GridLayout(game.getRowSize(), game.getColumnSize(), 0, 0));
		panel.setBackground(gameSetting.getGameTheme().getQuestionBackground());
		ArrayList<ArrayList<JPanel>> panelList = panelList();
		for (int r = 0; r < game.getRowSize(); r++) {
			for (int c = 0; c < game.getColumnSize(); c++) {
				panel.add(panelList.get(r).get(c));
			}
		}
		return panel;
	}

	public void updateGameState(String num, String location) {
		int number = Integer.parseInt(num);
		String[] temp = location.split("-");
		int row = Integer.parseInt(temp[0]);
		int col = Integer.parseInt(temp[1]);

		game.addNumber(new CellAnswer(number), row, col);
		game.changeNumberState();
		if (game.isFinish()) {
			String input = (String) JOptionPane.showInputDialog(contentPane, "Please enter your name:", "Game Finish!",
					JOptionPane.INFORMATION_MESSAGE, null, null, "Anonymous");
			if (input != null) {
				WindowsResult wr = new WindowsResult(windowsGame, input);
			}

		}
		loadPane();

	}

	public void windowsExit() {

	}

	public ArrayList<ArrayList<JPanel>> panelList() {
		ArrayList<ArrayList<JPanel>> panelList = new ArrayList<ArrayList<JPanel>>();
		for (int r = 0; r < game.getRowSize(); r++) {
			ArrayList<JPanel> oneRow = new ArrayList<JPanel>();
			for (int c = 0; c < game.getColumnSize(); c++) {
				JPanel onePanel = new JPanel(new GridLayout());
				if (game.getGameGrid().get(r).get(c).getType() == EnumCellTypes.Question) {
					CellQuestion boxQuestion = (CellQuestion) game.getGameGrid().get(r).get(c);
					if (boxQuestion.getCol() != 0 || boxQuestion.getRow() != 0) {
						onePanel = new JPanel(new GridBagLayout()) {
							protected void paintComponent(Graphics g) {
								super.paintComponent(g);
								g.setColor(gameSetting.getGameTheme().getQuestionForeground());
								g.drawLine(0, 0, 100, 100);
								g.drawLine(1, 0, 101, 100);
							}
						};
						GridBagConstraints gbc = new GridBagConstraints();
						gbc.gridwidth = 1;
						gbc.gridheight = 1;
						gbc.weightx = 0.2;
						gbc.weighty = 0.2;

						JLabel labelR = new JLabel(
								boxQuestion.getRow() != 0 ? String.valueOf(boxQuestion.getRow()) : " ");
						labelR.setFont(new Font("", Font.PLAIN, 14));
						labelR.setForeground(gameSetting.getGameTheme().getQuestionText());
						gbc.gridx = 2;
						gbc.gridy = 0;
						if (game.isPaused() && !game.isFinish()) {
							labelR.setText(" ");
						}
						onePanel.add(labelR, gbc);

						JLabel labelC = new JLabel(
								boxQuestion.getCol() != 0 ? String.valueOf(boxQuestion.getCol()) : " ");
						labelC.setFont(new Font("", Font.PLAIN, 14));
						labelC.setForeground(gameSetting.getGameTheme().getQuestionText());
						gbc.gridx = 0;
						gbc.gridy = 2;
						if (game.isPaused() && !game.isFinish()) {
							labelC.setText(" ");
						}
						onePanel.add(labelC, gbc);

					}
				} else {
					JTextField oneField = new JTextField();

					CellAnswer boxNumber = (CellAnswer) game.getGameGrid().get(r).get(c);
					int number = boxNumber.getNumber();
					if (boxNumber.getState() == EnumCellStates.Finish) {
						oneField.setForeground(gameSetting.getGameTheme().getAnswerTextFinish());
					} else if (boxNumber.getState() == EnumCellStates.Unfinished) {
						oneField.setForeground(gameSetting.getGameTheme().getAnswerTextUnfinished());
					} else if (boxNumber.getState() == EnumCellStates.Error) {
						oneField.setForeground(gameSetting.getGameTheme().getAnswerTextError());
					}
					oneField.setBackground(gameSetting.getGameTheme().getAnswerBackground());
					oneField.setText(number != 0 ? String.valueOf(number) : "");
					oneField.setHorizontalAlignment(JTextField.CENTER);
					oneField.setBorder(null);
					oneField.setFont(new Font("", Font.PLAIN, 20));
					oneField.setName(r + "-" + c);

					if (game.isPaused() || game.isFinish()) {
						oneField.setFocusable(false);
					}
					if (game.isPaused() && !game.isFinish()) {
						oneField.setText(" ");
					}

					oneField.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent e) {
							JTextField textField = (JTextField) e.getSource();
							char c = e.getKeyChar();
							if ((c > '0') && (c <= '9')) {
								textField.setText(Character.toString(c));
								updateGameState(Character.toString(c), textField.getName());
							} else {
								textField.setText("");
								updateGameState("0", textField.getName());
							}
						}
					});

					oneField.addFocusListener(new FocusListener() {
						@Override
						public void focusLost(FocusEvent e) {
							JTextField textField = (JTextField) e.getSource();
							textField.setBackground(gameSetting.getGameTheme().getAnswerBackground());
						}

						@Override
						public void focusGained(FocusEvent e) {
							JTextField textField = (JTextField) e.getSource();
							textField.setBackground(gameSetting.getGameTheme().getAnswerTextSelect());
							textField.selectAll();
						}
					});
					onePanel.add(oneField);
				}
				onePanel.setBorder(BorderFactory.createLineBorder(gameSetting.getGameTheme().getQuestionForeground()));
				onePanel.setBackground(gameSetting.getGameTheme().getQuestionBackground());
				oneRow.add(onePanel);
			}
			panelList.add(oneRow);
		}
		return panelList;
	}

}
