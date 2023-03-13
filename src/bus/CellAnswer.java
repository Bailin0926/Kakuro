package bus;

public class CellAnswer extends Cell {

	private static final long serialVersionUID = -6415569424882515318L;
	private int number;
	private EnumCellStates state;

	public EnumCellStates getState() {
		return state;
	}

	public EnumCellStates setState(EnumCellStates state) {
		if (state != EnumCellStates.Unfinished && this.state != EnumCellStates.Error) {
			this.state = state;
		}
		return state;
	}

	public void setStateUnfinished() {
		this.state = EnumCellStates.Unfinished;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		if (number <= 9 && number >= 0) {
			this.number = number;
		} else {
			this.number = 0;
		}
	}

	public CellAnswer() {
		super(EnumCellTypes.Answer);
		number = 0;
		state = EnumCellStates.Unfinished;
	}

	public CellAnswer(int num) {
		super(EnumCellTypes.Answer);
		this.setNumber(num);
		state = EnumCellStates.Unfinished;
	}

	@Override
	public String toString() {
		return number + "/" + state;
	}

}
