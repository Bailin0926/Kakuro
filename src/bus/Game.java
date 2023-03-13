package bus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Game implements Serializable {

	private static final long serialVersionUID = 5589690927433721066L;
	private ArrayList<ArrayList<Cell>> gameGrid = null;
	private ArrayList<ArrayList<Cell>> questionGrid = null;
	private ArrayList<ArrayList<Cell>> answerGrid = null;
	private GameTimer timmer;
	private EnumGameDifficulty difficulty;
	private boolean isFromAnswer;
	private boolean isPaused;
	private boolean isFinish;

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
		if (this.isFinish) {
			this.setPaused(true);
		}
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused() {
		if (!this.isPaused()) {
			this.getTimmer().stopTimer();
		} else {
			this.getTimmer().startTimer();
		}
		this.isPaused = !this.isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
		if (this.isPaused) {
			this.getTimmer().stopTimer();
		} else {
			this.getTimmer().startTimer();
		}
	}

	public boolean isFromAnswer() {
		return isFromAnswer;
	}

	public void setFromAnswer(boolean fromAnswer) {
		this.isFromAnswer = fromAnswer;
	}

	public ArrayList<ArrayList<Cell>> getQuestionGrid() {
		return questionGrid;
	}

	public void setQuestionGrid(ArrayList<ArrayList<Cell>> questionGrid) {
		this.questionGrid = questionGrid;
	}

	public EnumGameDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(EnumGameDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	public GameTimer getTimmer() {
		return timmer;
	}

	public void setTimmer(GameTimer timmer) {
		this.timmer = timmer;
	}

	public ArrayList<ArrayList<Cell>> getGameGrid() {
		return gameGrid;
	}

	public void setGameGrid(ArrayList<ArrayList<Cell>> gameGrid) {
		this.gameGrid = gameGrid;
	}

	public ArrayList<ArrayList<Cell>> getAnswerGrid() {
		return answerGrid;
	}

	public void setAnswerGrid(ArrayList<ArrayList<Cell>> answerGrid) {
		this.answerGrid = answerGrid;
	}

	public Game(int height, int width, EnumGameDifficulty difficulty) {
		GameGenerator gg = new GameGenerator();
		if (gg.creatGame(height, width, difficulty)) {
			this.setGameGrid(gg.getGameGrid());
			this.setQuestionGrid(this.deepCopy(gg.getGameGrid()));
			this.setAnswerGrid(gg.getAnswerGrid());
			this.setTimmer(new GameTimer());
			this.setFromAnswer(false);
			this.setPaused(false);
			this.setFinish(false);
			this.setDifficulty(difficulty);
		}
	}

	public int getRowSize() {
		return gameGrid.size();
	}

	public int getColumnSize() {
		if (gameGrid.size() > 0) {
			return gameGrid.get(0).size();
		}
		return 0;
	}

	public Double getScore() {
		if (this.isFromAnswer()) {
			return (double) -9999;
		}
		Double score;
		score = (double) (this.getRowSize() * this.getColumnSize() * 10);
		if (this.getDifficulty() == EnumGameDifficulty.Easy) {
			score = score * 0.8;
		} else if (this.getDifficulty() == EnumGameDifficulty.Normal) {
			score = score * 1.0;
		} else if (this.getDifficulty() == EnumGameDifficulty.Hard) {
			score = score * 1.2;
		}

		score -= getTimmer().getTime() / 20000;
		return score;
	}

	public void addNumber(Cell cell, int row, int col) {
		gameGrid.get(row).set(col, cell);
	}

	public void showSolution() {
		this.setGameGrid(this.getAnswerGrid());
		this.setFromAnswer(true);
		this.setFinish(true);
		this.setPaused(true);
	}

	public void clearAll() {
		this.setGameGrid(deepCopy(this.getQuestionGrid()));
		this.setPaused(false);
	}

	public void changeNumberState() {
		for (int i = 1; i < this.getRowSize(); i++) {
			for (int j = 1; j < this.getColumnSize(); j++) {
				if (gameGrid.get(i).get(j).getType() == EnumCellTypes.Answer) {
					((CellAnswer) gameGrid.get(i).get(j)).setStateUnfinished();
				}
			}
		}
		for (int i = 1; i < this.getRowSize(); i++) {
			for (int j = 1; j < this.getColumnSize(); j++) {
				if (gameGrid.get(i).get(j).getType() == EnumCellTypes.Answer) {
					if (gameGrid.get(i).get(j - 1).getType() == EnumCellTypes.Question) {
						CellQuestion bq = (CellQuestion) gameGrid.get(i).get(j - 1);
						changeRowState(bq.getRow(), new ArrayList<Integer>(), i, j);
					}
					if (gameGrid.get(i - 1).get(j).getType() == EnumCellTypes.Question) {
						CellQuestion bq = (CellQuestion) gameGrid.get(i - 1).get(j);
						changeColState(bq.getCol(), new ArrayList<Integer>(), i, j);
					}
				}
			}
		}
		boolean finish = true;
		for (int i = 1; i < this.getRowSize(); i++) {
			for (int j = 1; j < this.getColumnSize(); j++) {
				if (gameGrid.get(i).get(j).getType() == EnumCellTypes.Answer
						&& ((CellAnswer) gameGrid.get(i).get(j)).getState() != EnumCellStates.Finish) {
					finish = false;
				}
			}
		}
		this.setFinish(finish);
	}

	private EnumCellStates changeRowState(int total, ArrayList<Integer> usedNum, int row, int col) {
		if (col >= this.getColumnSize() || gameGrid.get(row).get(col).getType() == EnumCellTypes.Question) {
			int usedTotal = 0;
			boolean hasZero = false;
			for (int i = 0; i < usedNum.size(); i++) {
				if (usedNum.get(i) == 0) {
					hasZero = true;
				} else {
					usedTotal += usedNum.get(i);
				}
			}
			usedNum.removeIf(val -> val == 0);
			HashSet<Integer> hashUsedNum = new HashSet<Integer>(usedNum);
			if (usedTotal > total || (usedTotal == total && hasZero) || (usedTotal < total && !hasZero)
					|| hashUsedNum.size() != usedNum.size()) {
				return EnumCellStates.Error;
			} else if (usedTotal == total && !hasZero) {
				return EnumCellStates.Finish;
			} else {
				return EnumCellStates.Unfinished;
			}
		}
		CellAnswer num = (CellAnswer) gameGrid.get(row).get(col);
		usedNum.add(num.getNumber());
		return ((CellAnswer) gameGrid.get(row).get(col)).setState(changeRowState(total, usedNum, row, col + 1));
	}

	private EnumCellStates changeColState(int total, ArrayList<Integer> usedNum, int row, int col) {
		if (row >= this.getRowSize() || gameGrid.get(row).get(col).getType() == EnumCellTypes.Question) {
			int usedTotal = 0;
			boolean hasZero = false;
			for (int i = 0; i < usedNum.size(); i++) {
				if (usedNum.get(i) == 0) {
					hasZero = true;
				} else {
					usedTotal += usedNum.get(i);
				}
			}
			usedNum.removeIf(val -> val == 0);
			HashSet<Integer> hashUsedNum = new HashSet<Integer>(usedNum);
			if (usedTotal > total || (usedTotal == total && hasZero) || (usedTotal < total && !hasZero)
					|| hashUsedNum.size() != usedNum.size()) {
				return EnumCellStates.Error;
			} else if (usedTotal == total && !hasZero) {
				return EnumCellStates.Finish;
			} else {
				return EnumCellStates.Unfinished;
			}
		}
		CellAnswer num = (CellAnswer) gameGrid.get(row).get(col);
		usedNum.add(num.getNumber());
		return ((CellAnswer) gameGrid.get(row).get(col)).setState(changeColState(total, usedNum, row + 1, col));
	}

	@SuppressWarnings("unchecked")
	private ArrayList<ArrayList<Cell>> deepCopy(ArrayList<ArrayList<Cell>> list) {
		ArrayList<ArrayList<Cell>> rList = new ArrayList<ArrayList<Cell>>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(list);
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();

			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			rList = (ArrayList<ArrayList<Cell>>) new ObjectInputStream(bais).readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}

	@Override
	public String toString() {
		return gameGrid.toString();
	}

}
