package bus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestGame {
	static Game g;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@Test
	void testNew() {
		g = new Game(10, 11, EnumGameDifficulty.Easy);
		assertNotSame(g.getGameGrid(), g.getQuestionGrid());
		assertEquals(false, g.isFinish());
		assertEquals(false, g.isFromAnswer());
		assertEquals(false, g.isPaused());
		assertEquals(EnumGameDifficulty.Easy, g.getDifficulty());
		assertEquals(11, g.getColumnSize());
		assertEquals(10, g.getRowSize());
	}

	
	@Test
	void testPause() {
		g = new Game(10, 11, EnumGameDifficulty.Easy);
		g.setPaused(true);
		assertEquals(true, g.isPaused());
		g.setPaused(false);
		assertEquals(false, g.isPaused());
		g.setPaused();
		assertEquals(true, g.isPaused());
	}

	@Test
	void testFinish() {
		g = new Game(10, 11, EnumGameDifficulty.Easy);
		g.setFinish(true);
		assertEquals(true, g.isPaused());
	}

	@Test
	void testScore() {
		g = new Game(10, 11, EnumGameDifficulty.Easy);
		assertEquals(880, g.getScore());
		g = new Game(10, 11, EnumGameDifficulty.Normal);
		assertEquals(1100, g.getScore());
		g = new Game(10, 10, EnumGameDifficulty.Hard);
		assertEquals(1200, g.getScore());

	}
	
	@Test
	void testSolution() {
		g = new Game(10, 11, EnumGameDifficulty.Easy);
		g.showSolution();
		assertEquals(true, g.isFinish());
		assertEquals(true, g.isFromAnswer());
		assertEquals(true, g.isPaused());
		assertSame(g.getGameGrid(), g.getAnswerGrid());
	}
	
	@Test
	void testClear() {
		g = new Game(10, 11, EnumGameDifficulty.Easy);
		g.setPaused(true);
		g.clearAll();
		assertNotSame(g.getGameGrid(), g.getQuestionGrid());
		assertEquals(false, g.isPaused());
	}
}
