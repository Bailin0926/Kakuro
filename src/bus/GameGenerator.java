package bus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameGenerator {
	private ArrayList<ArrayList<Cell>> gameGrid;
	private ArrayList<ArrayList<Cell>> answerGrid;
	private int width;
	private int height;

	public ArrayList<ArrayList<Cell>> getGameGrid() {
		return gameGrid;
	}

	public ArrayList<ArrayList<Cell>> getAnswerGrid() {
		return answerGrid;
	}

	public boolean creatGame(int height, int width, EnumGameDifficulty difficulty) {
		int max = 3;
		for (int i = 0; i < 10; i++) {
			this.gameGrid = this.creatNewGrid(EnumCellTypes.Question, height, width);
			this.height = height;
			this.width = width;

			if (height < 3 || width < 3) {
				return false;
			}
			if (difficulty == EnumGameDifficulty.Easy) {
				max = 1;
			}
			if (difficulty == EnumGameDifficulty.Normal) {
				max = 2;
			}
			if (difficulty == EnumGameDifficulty.Hard) {
				max = 3;
			}
			
			fillGrid(1, 1, max);
			creatQuestion();
			if (isQualified(answerGrid)) {
				return true;
			}
		}
		return false;

	}

	private ArrayList<ArrayList<Cell>> creatNewGrid(EnumCellTypes type, int height, int width) {
		ArrayList<ArrayList<Cell>> listOfCell = new ArrayList<ArrayList<Cell>>();
		for (int i = 0; i < height; i++) {
			ArrayList<Cell> colOfBox = new ArrayList<Cell>();
			for (int j = 0; j < width; j++) {
				if (type == EnumCellTypes.Question) {
					colOfBox.add(new CellQuestion());
				} else {
					colOfBox.add(new CellAnswer());
				}
			}
			listOfCell.add(colOfBox);
		}
		return listOfCell;
	}

	private ArrayList<Integer> listForPick(int row, int col) {
		ArrayList<Integer> listForPick = new ArrayList<Integer>();
		listForPick.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		for (int i = 1; i <= 9; i++) {
			int newVal = row - i;
			if (newVal <= 0 || gameGrid.get(newVal).get(col).getType() == EnumCellTypes.Question) {
				break;
			}
			listForPick.removeIf(val -> val == ((CellAnswer) gameGrid.get(newVal).get(col)).getNumber());
		}
		for (int i = 1; i <= 9; i++) {
			int newVal = col - i;
			if (newVal <= 0 || gameGrid.get(row).get(newVal).getType() == EnumCellTypes.Question) {
				break;
			}
			listForPick.removeIf(val -> val == ((CellAnswer) gameGrid.get(row).get(newVal)).getNumber());
		}
		for (int i = 1; i <= 9; i++) {
			int newVal = row + i;
			if (newVal >= height || gameGrid.get(newVal).get(col).getType() == EnumCellTypes.Question) {
				break;
			}
			listForPick.removeIf(val -> val == ((CellAnswer) gameGrid.get(newVal).get(col)).getNumber());
		}

		for (int i = 1; i <= 9; i++) {
			int newVal = col + i;
			if (newVal >= width || gameGrid.get(row).get(newVal).getType() == EnumCellTypes.Question) {
				break;
			}
			listForPick.removeIf(val -> val == ((CellAnswer) gameGrid.get(row).get(newVal)).getNumber());
		}
		return listForPick;
	}

	private int randomNum(int min, int max) {
		int range = max - min + 1;
		return (int) (Math.random() * range) + min;
	}

	private ArrayList<?> randomList(ArrayList<?> list) {
		for (int i = 0; i < list.size(); i++) {
			Collections.swap(list, randomNum(0, list.size() - 1), randomNum(0, list.size() - 1));
		}
		return list;
	}

	private ArrayList<ArrayList<Integer>> findSpaceTR(int row, int col, int max) {
		max = max > 9 ? 9 : max;
		if (row >= height || row < 0 || col >= width || col < 0) {
			return new ArrayList<ArrayList<Integer>>();
		}
		ArrayList<ArrayList<Integer>> listOfSpace = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tempList;
		int nRow;
		int nCol;
		int minY;
		minY = max;
		for (int x = 0; x <= max; x++) {
			nCol = col + x;
			for (int y = 0; y <= max && nCol < this.width; y++) {
				nRow = row - y;
				if (nRow <= 0 || isCloseToAnswer(nRow, nCol)) {
					minY = minY < y - 1 ? minY : y - 1;
					if (minY < 1) {
						break;
					}
				}
				if (x >= 1 && y >= 1 && y <= minY) {
					tempList = new ArrayList<Integer>();
					tempList.add(row);
					tempList.add(col);
					tempList.add(x);
					tempList.add(0 - y);
					listOfSpace.add(tempList);
				}
			}
		}
		return listOfSpace;
	}

	private ArrayList<ArrayList<Integer>> findSpaceBR(int row, int col, int max) {
		max = max > 9 ? 9 : max;
		if (row >= height || row < 0 || col >= width || col < 0) {
			return new ArrayList<ArrayList<Integer>>();
		}
		ArrayList<ArrayList<Integer>> listOfSpace = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tempList;
		int nRow;
		int nCol;
		int minY;
		minY = max;
		for (int x = 0; x <= max; x++) {
			nCol = col + x;
			for (int y = 0; y <= max && nCol < this.width; y++) {
				nRow = row + y;
				if (nRow >= this.height || isCloseToAnswer(nRow, nCol)) {
					minY = minY < y - 1 ? minY : y - 1;
					if (minY < 1) {
						break;
					}
				}
				if (x >= 1 && y >= 1 && y <= minY) {
					tempList = new ArrayList<Integer>();
					tempList.add(row);
					tempList.add(col);
					tempList.add(x);
					tempList.add(y);
					listOfSpace.add(tempList);
				}
			}
		}
		return listOfSpace;
	}

	private ArrayList<ArrayList<Integer>> findSpaceBL(int row, int col, int max) {
		max = max > 9 ? 9 : max;
		if (row >= height || row < 0 || col >= width || col < 0) {
			return new ArrayList<ArrayList<Integer>>();
		}
		ArrayList<ArrayList<Integer>> listOfSpace = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tempList;
		int nRow;
		int nCol;
		int minY;
		minY = max;
		for (int x = 0; x <= max; x++) {
			nCol = col - x;
			for (int y = 0; y <= max && nCol > 0; y++) {
				nRow = row + y;
				if (nRow >= this.height || isCloseToAnswer(nRow, nCol)) {
					minY = minY < y - 1 ? minY : y - 1;
					if (minY < 1) {
						break;
					}
				}
				if (x >= 1 && y >= 1 && y <= minY) {
					tempList = new ArrayList<Integer>();
					tempList.add(row);
					tempList.add(col);
					tempList.add(0 - x);
					tempList.add(y);
					listOfSpace.add(tempList);
				}
			}
		}
		return listOfSpace;
	}

	private ArrayList<ArrayList<Integer>> findSpaceTL(int row, int col, int max) {
		max = max > 9 ? 9 : max;
		if (row >= height || row < 0 || col >= width || col < 0) {
			return new ArrayList<ArrayList<Integer>>();
		}
		ArrayList<ArrayList<Integer>> listOfSpace = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tempList;
		int nRow;
		int nCol;
		int minY;
		minY = max;
		for (int x = 0; x <= max; x++) {
			nCol = col - x;
			for (int y = 0; y <= max && nCol > 0; y++) {
				nRow = row - y;
				if (nRow <= 0 || isCloseToAnswer(nRow, nCol)) {
					minY = minY < y - 1 ? minY : y - 1;
					if (minY < 1) {
						break;
					}
				}
				if (x >= 1 && y >= 1 && y <= minY) {
					tempList = new ArrayList<Integer>();
					tempList.add(row);
					tempList.add(col);
					tempList.add(0 - x);
					tempList.add(0 - y);
					listOfSpace.add(tempList);
				}
			}
		}
		return listOfSpace;
	}

	@SuppressWarnings("unchecked")
	private boolean fillSpace(int row, int col, int x, int y, int nX, int nY) {

		gameGrid.get(row).set(col, new CellAnswer());

		ArrayList<Integer> listForPick = (ArrayList<Integer>) randomList(listForPick(row, col));
		int nRow;
		int nCol;
		int index = 0;
		nRow = row - nY;
		nCol = col - nX;
		if (y == nY) {
			if (x == nX) {
				if (index >= listForPick.size()) {
					gameGrid.get(row).set(col, new CellQuestion());
					return false;
				} else {
					((CellAnswer) gameGrid.get(row).get(col)).setNumber(listForPick.get(index));
					return true;
				}
			}
			nX = x > 0 ? nX + 1 : nX - 1;
			nY = 0;
		} else {
			nY = y > 0 ? nY + 1 : nY - 1;
		}
		nRow = nRow + nY;
		nCol = nCol + nX;

		while (true) {
			if (index >= listForPick.size()) {
				gameGrid.get(row).set(col, new CellQuestion());
				return false;
			} else {
				((CellAnswer) gameGrid.get(row).get(col)).setNumber(listForPick.get(index));
			}
			if (fillSpace(nRow, nCol, x, y, nX, nY)) {
				return true;
			} else {
				index++;
			}

		}
	}

	private ArrayList<ArrayList<Integer>> findCorner(int row, int col, int x, int y) {
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> corner1 = new ArrayList<Integer>();
		ArrayList<Integer> corner2 = new ArrayList<Integer>();
		ArrayList<Integer> corner3 = new ArrayList<Integer>();
		ArrayList<Integer> corner4 = new ArrayList<Integer>();

		corner1.addAll(Arrays.asList(row, col));
		list.add(corner1);
		corner2.addAll(Arrays.asList(row + y, col));
		list.add(corner2);
		corner3.addAll(Arrays.asList(row, col + x));
		list.add(corner3);
		corner4.addAll(Arrays.asList(row + y, col + x));
		list.add(corner4);

		return list;
	}

	@SuppressWarnings("unchecked")
	private void fillGrid(int row, int col, int max) {
		ArrayList<ArrayList<Cell>> copyGameGrid = deepCopy(gameGrid);

		ArrayList<ArrayList<Integer>> spaceTR = findSpaceTR(row, col, max);
		ArrayList<ArrayList<Integer>> spaceTRT = findSpaceTR(row - 1, col, max);
		ArrayList<ArrayList<Integer>> spaceTRR = findSpaceTR(row, col + 1, max);

		ArrayList<ArrayList<Integer>> spaceTL = findSpaceTL(row, col, max);
		ArrayList<ArrayList<Integer>> spaceTLT = findSpaceTL(row - 1, col, max);
		ArrayList<ArrayList<Integer>> spaceTLL = findSpaceTL(row, col - 1, max);

		ArrayList<ArrayList<Integer>> spaceBR = findSpaceBR(row, col, max);
		ArrayList<ArrayList<Integer>> spaceBRB = findSpaceBR(row + 1, col, max);
		ArrayList<ArrayList<Integer>> spaceBRR = findSpaceBR(row, col + 1, max);

		ArrayList<ArrayList<Integer>> spaceBL = findSpaceBL(row, col, max);
		ArrayList<ArrayList<Integer>> spaceBLB = findSpaceBL(row + 1, col, max);
		ArrayList<ArrayList<Integer>> spaceBLL = findSpaceBL(row, col - 1, max);

		ArrayList<ArrayList<ArrayList<Integer>>> spaceList = new ArrayList<ArrayList<ArrayList<Integer>>>();
		if (spaceTR.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceTR));
		}
		if (spaceTL.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceTL));
		}
		if (spaceBR.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceBR));
		}
		if (spaceBL.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceBL));
		}

		if (spaceTRT.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceTRT));
		}
		if (spaceTRR.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceTRR));
		}
		if (spaceTLT.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceTLT));
		}
		if (spaceTLL.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceTLL));
		}
		if (spaceBRB.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceBRB));
		}
		if (spaceBRR.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceBRR));
		}
		if (spaceBLB.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceBLB));
		}
		if (spaceBLL.size() > 0) {
			spaceList.add((ArrayList<ArrayList<Integer>>) randomList(spaceBLL));
		}

		setAnswerBase(false, row, col);
		if (spaceList.size() == 0) {
			return;
		}
		spaceList = (ArrayList<ArrayList<ArrayList<Integer>>>) randomList(spaceList);

		int i = 0;
		for (int j = 0; j < spaceList.get(i).size(); j++) {
			if (fillSpace(spaceList.get(i).get(j).get(0), spaceList.get(i).get(j).get(1),
					spaceList.get(i).get(j).get(2), spaceList.get(i).get(j).get(3), 0, 0)) {
				ArrayList<ArrayList<Integer>> cornerList = (ArrayList<ArrayList<Integer>>) randomList(
						findCorner(spaceList.get(i).get(j).get(0), spaceList.get(i).get(j).get(1),
								spaceList.get(i).get(j).get(2), spaceList.get(i).get(j).get(3)));
				for (int k = 0; k < 4; k++) {
					setAnswerBase(true, cornerList.get(k).get(0), cornerList.get(k).get(1));
					fillGrid(cornerList.get(k).get(0), cornerList.get(k).get(1), max);
				}
				break;
			} else {
				gameGrid = copyGameGrid;
			}

		}

	}

	private boolean isCloseToAnswer(int x, int y) {
		if (gameGrid.get(x).get(y).getType() == EnumCellTypes.AnswerBase) {
			return false;
		}
		if ((y + 1 < width && gameGrid.get(x).get(y + 1).getType() == EnumCellTypes.Answer)
				|| (y - 1 >= 0 && gameGrid.get(x).get(y - 1).getType() == EnumCellTypes.Answer)
				|| (x + 1 < height && gameGrid.get(x + 1).get(y).getType() == EnumCellTypes.Answer)
				|| (x - 1 >= 0 && gameGrid.get(x - 1).get(y).getType() == EnumCellTypes.Answer)) {
			return true;
		}
		return false;
	}

	private void setAnswerBase(boolean set, int x, int y) {
		if (gameGrid.get(x).get(y).getType() != EnumCellTypes.Question) {
			gameGrid.get(x).get(y).setType(set ? EnumCellTypes.AnswerBase : EnumCellTypes.Answer);
		}
	}

	private void creatQuestion() {
		int total = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (gameGrid.get(i).get(j).getType() == EnumCellTypes.Question) {
					total = 0;
					for (int k = 1; k <= 9; k++) {
						if (i + k < height && gameGrid.get(i + k).get(j).getType() != EnumCellTypes.Question) {
							total += ((CellAnswer) gameGrid.get(i + k).get(j)).getNumber();
						} else {
							break;
						}
					}
					((CellQuestion) gameGrid.get(i).get(j)).setCol(total);
					total = 0;
					for (int k = 1; k <= 9; k++) {
						if (j + k < width && gameGrid.get(i).get(j + k).getType() != EnumCellTypes.Question) {
							total += ((CellAnswer) gameGrid.get(i).get(j + k)).getNumber();
						} else {
							break;
						}
					}
					((CellQuestion) gameGrid.get(i).get(j)).setRow(total);
				}
			}
		}

		answerGrid = deepCopy(gameGrid);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (gameGrid.get(i).get(j).getType() != EnumCellTypes.Question) {
					((CellAnswer) gameGrid.get(i).get(j)).setNumber(0);
					((CellAnswer) answerGrid.get(i).get(j)).setState(EnumCellStates.Finish);
				}
			}
		}
	}

	private boolean isQualified(ArrayList<ArrayList<Cell>> list) {

		boolean listOk = true;
		boolean lineOk = false;
		int total = 0;

		for (int i = 1; i < height; i++) {
			lineOk = false;
			for (int j = 1; j < width; j++) {
				if (list.get(i).get(j).getType() != EnumCellTypes.Question) {
					if (((CellAnswer) list.get(i).get(j)).getNumber() > 9
							|| ((CellAnswer) list.get(i).get(j)).getNumber() < 1) {
						listOk = false;
						break;
					}
					lineOk = true;
				} else {
					for (int k = 1; k <= 9; k++) {
						if (i + k < height && list.get(i + k).get(j).getType() != EnumCellTypes.Question) {
							total++;
						} else {
							break;
						}
					}
					if (total == 1) {
						listOk = false;
						break;
					}
					total = 0;
					for (int k = 1; k <= 9; k++) {
						if (j + k < width && list.get(i).get(j + k).getType() != EnumCellTypes.Question) {
							total++;
						} else {
							break;
						}
					}
					if (total == 1) {
						listOk = false;
						break;
					}
				}
			}
			if (!lineOk || !listOk) {
				listOk = false;
				break;
			}
		}

		for (int j = 1; j < width; j++) {
			lineOk = false;
			for (int i = 1; i < height; i++) {
				if (list.get(i).get(j).getType() != EnumCellTypes.Question) {
					lineOk = true;
					break;
				}
			}
			if (!lineOk || !listOk) {
				listOk = false;
				break;
			}
		}

		return listOk;
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

}
