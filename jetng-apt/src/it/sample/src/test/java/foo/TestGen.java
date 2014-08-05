package foo;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestGen {
	@Test
	public void testGenerate() {
		List<String> l = Arrays.asList("hello", "world");
		assertEquals("Hello world" + System.getProperty("line.separator") + 2
				+ System.getProperty("line.separator"), new Bar().generate(l));
	}
}