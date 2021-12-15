package org.quality.gates.sonar.api;

import org.junit.Test;
import org.quality.gates.sonar.api5x.SonarHttpRequester5x;
import org.quality.gates.sonar.api60.SonarHttpRequester60;
import org.quality.gates.sonar.api61.SonarHttpRequester61;
import org.quality.gates.sonar.api8x.SonarHttpRequester8x;

import static org.junit.Assert.assertTrue;

public class SonarHttpRequesterFactoryTest {

	@Test
	public void testGetSonarHttpRequesterInstanceLessThanMajorVersion5 () {
		SonarHttpRequester sonar = SonarHttpRequesterFactory.getSonarHttpRequesterInstance("4.0.2");
		assertTrue(sonar instanceof SonarHttpRequester5x);
	}

	@Test
	public void testGetSonarHttpRequesterInstanceEqualsMajorVersion5 () {
		SonarHttpRequester sonar = SonarHttpRequesterFactory.getSonarHttpRequesterInstance("5.0.2");
		assertTrue(sonar instanceof SonarHttpRequester5x);
	}

	@Test
	public void testGetSonarHttpRequesterInstanceEqualsMajorVersion6AndMinorVersion0 () {
		SonarHttpRequester sonar = SonarHttpRequesterFactory.getSonarHttpRequesterInstance("6.0.2");
		assertTrue(sonar instanceof SonarHttpRequester60);
	}

	@Test
	public void testGetSonarHttpRequesterInstanceEqualsMajorVersion6AndMinorVersion1 () {
		SonarHttpRequester sonar = SonarHttpRequesterFactory.getSonarHttpRequesterInstance("6.1.2");
		assertTrue(sonar instanceof SonarHttpRequester61);
	}

	@Test
	public void testGetSonarHttpRequesterInstanceEqualsVersionMajorVersion8 () {
		SonarHttpRequester sonar = SonarHttpRequesterFactory.getSonarHttpRequesterInstance("8.9.1");
		assertTrue(sonar instanceof SonarHttpRequester8x);
	}
}