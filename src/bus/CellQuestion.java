package bus;

public class CellQuestion extends Cell {

	private static final long serialVersionUID = -4295952190489671568L;
	private int row;
	private int col;

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setRow(int row) {
		if (row >= 0) {
			this.row = row;
		} else {
			this.row = 0;
		}
	}

	public void setCol(int col) {
		if (col >= 0) {
			this.col = col;
		} else {
			this.col = 0;
		}
	}

	public CellQuestion() {
		super(EnumCellTypes.Question);
		this.row = 0;
		this.col = 0;
	}

	public CellQuestion(int row, int col) {
		super(EnumCellTypes.Question);
		this.setRow(row);
		this.setCol(col);
	}

	@Override
	public String toString() {
		return col + "\\" + row;
	}

}
