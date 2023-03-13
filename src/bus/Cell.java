package bus;

import java.io.Serializable;

public abstract class Cell implements Serializable{

	private static final long serialVersionUID = -6249710237057334418L;
	private EnumCellTypes type;
	
	public EnumCellTypes getType() {
		return type;
	}

	public void setType(EnumCellTypes type) {
		this.type = type;
	}

	public Cell(EnumCellTypes type) {
		this.setType(type);
	}
	
}
