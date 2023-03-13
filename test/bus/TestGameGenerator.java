package bus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestGameGenerator {
	static GameGenerator g;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		g = new GameGenerator();
	}

	@Test
	void testCreatGame() {
		assertEquals(false, g.creatGame(1, 8, EnumGameDifficulty.Easy));
		assertEquals(false, g.creatGame(12, 0, EnumGameDifficulty.Easy));
		assertEquals(true, g.creatGame(12, 8, EnumGameDifficulty.Easy));
		assertEquals(true, g.creatGame(12, 8, null));
	}
	
	@Test
	void testGrid() {
		g.creatGame(10, 10, EnumGameDifficulty.Easy);
		assertNotSame(g.getAnswerGrid(),g.getGameGrid());
	}

}
