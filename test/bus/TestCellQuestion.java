package bus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestCellQuestion {
	static CellQuestion c;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		c = new CellQuestion();
	}
	
	@Test
	void testNew() {
		c = new CellQuestion();
		assertEquals(0, c.getRow());
		assertEquals(0, c.getCol());
		c = new CellQuestion(2,25);
		assertEquals(2, c.getRow());
		assertEquals(25, c.getCol());
		c = new CellQuestion(5,-1);
		assertEquals(5, c.getRow());
		assertEquals(0, c.getCol());
		
	}

	@Test
	void testType() {
		assertEquals(EnumCellTypes.Question, c.getType());
	}
	
	@Test
	void testRow() {
		c.setRow(0);
		assertEquals(0, c.getRow());
		c.setRow(10);
		assertEquals(10, c.getRow());
		c.setRow(-2);
		assertEquals(0, c.getRow());
	}
	
	@Test
	void testCol() {
		c.setCol(0);
		assertEquals(0, c.getCol());
		c.setCol(10);
		assertEquals(10, c.getCol());
		c.setCol(-2);
		assertEquals(0, c.getCol());
	}

}
