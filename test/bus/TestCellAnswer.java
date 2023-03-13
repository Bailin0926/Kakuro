package bus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class TestCellAnswer {
	static CellAnswer c;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		c = new CellAnswer();	
	}
	
	@Test
	void testNew() {
		c = new CellAnswer();
		assertEquals(EnumCellStates.Unfinished, c.getState());
		assertEquals(0, c.getNumber());
		c = new CellAnswer(5);
		assertEquals(EnumCellStates.Unfinished, c.getState());
		assertEquals(5, c.getNumber());
		c = new CellAnswer(20);
		assertEquals(EnumCellStates.Unfinished, c.getState());
		assertEquals(0, c.getNumber());
	}
	
	@Test
	void testType() {
		assertEquals(EnumCellTypes.Answer, c.getType());
	}
	
	@Test
	void testState() {
		c.setStateUnfinished();
		assertEquals(EnumCellStates.Error, c.setState(EnumCellStates.Error));
		assertEquals(EnumCellStates.Error, c.getState());
		
		c.setStateUnfinished();
		assertEquals(EnumCellStates.Unfinished, c.getState());
		assertEquals(EnumCellStates.Finish, c.setState(EnumCellStates.Finish));
		assertEquals(EnumCellStates.Finish, c.getState());
		
		assertEquals(EnumCellStates.Error, c.setState(EnumCellStates.Error));
		assertEquals(EnumCellStates.Error, c.getState());
		
		assertEquals(EnumCellStates.Finish, c.setState(EnumCellStates.Finish));
		assertEquals(EnumCellStates.Error, c.getState());
		
		c.setStateUnfinished();
		assertEquals(EnumCellStates.Unfinished, c.getState());
	}
	
	@Test
	void testNumber() {
		c.setNumber(5);
		assertEquals(5, c.getNumber());
		c.setNumber(9);
		assertEquals(9, c.getNumber());
		c.setNumber(0);
		assertEquals(0, c.getNumber());
		c.setNumber(-1);
		assertEquals(0, c.getNumber());
		c.setNumber(10);
		assertEquals(0, c.getNumber());
	}

}
