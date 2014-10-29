package nl.ncim.bknopper.tspeademo.domain;

import static org.junit.Assert.*;
import nl.ncim.bknopper.tspeademo.domain.City;

import org.junit.Test;

public class CityTest {

	@Test
	public void testCalculateDistance() {
		City amsterdam = new City("Amsterdam", 52.370216, 4.895168);
		City paris = new City("Paris", 48.856613, 2.352222);		
		assertEquals(amsterdam.calculateDistance(paris), paris.calculateDistance(amsterdam));
	}
}
