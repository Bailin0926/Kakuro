package bus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestGameTimer {
	static GameTimer t;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		t = new GameTimer();
	}

	@Test
	void testTiming() {
		assertEquals(false, t.isTiming());
		t.startStopTimer();
		assertEquals(true, t.isTiming());
		t.startStopTimer();
		assertEquals(false, t.isTiming());
		t.startTimer();
		assertEquals(true, t.isTiming());
		t.startTimer();
		assertEquals(true, t.isTiming());
		t.stopTimer();
		assertEquals(false, t.isTiming());
		t.stopTimer();
		assertEquals(false, t.isTiming());
	}
	
	@Test
	void testTime() {
		t = new GameTimer();
		assertEquals(0.0, t.getTime());
	}
	
	@Test
	void testTimeToString() {
		assertEquals("00:00", GameTimer.timeToString(0));
		assertEquals("00:00", GameTimer.timeToString(-1));
		assertEquals("0:01", GameTimer.timeToString(1000));
		assertEquals("0:01", GameTimer.timeToString(1500));
		assertEquals("1:00", GameTimer.timeToString(60000));
		assertEquals("1:15", GameTimer.timeToString(75000));
		assertEquals("100:00", GameTimer.timeToString(6000000));
	}

}
