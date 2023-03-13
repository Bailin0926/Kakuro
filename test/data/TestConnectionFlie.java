package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestConnectionFlie {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	void testTheme() {
		assertEquals(null, ConnectionFile.getTheme("aaaaaaa"));
	}
	
	@Test
	void testMusic() {
		assertEquals(null, ConnectionFile.getMusic("aaaaaaa").listFiles());
	}

}
