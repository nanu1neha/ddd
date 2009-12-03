package se.citerus.dddsample.tracking.core.domain.model.cargo;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import org.junit.Test;
import static se.citerus.dddsample.tracking.core.application.util.DateTestUtil.toDate;
import static se.citerus.dddsample.tracking.core.domain.model.location.SampleLocations.*;
import se.citerus.dddsample.tracking.core.domain.model.shared.HandlingActivity;
import static se.citerus.dddsample.tracking.core.domain.model.shared.HandlingActivity.loadedOnto;
import static se.citerus.dddsample.tracking.core.domain.model.shared.HandlingActivity.unloadedOff;
import static se.citerus.dddsample.tracking.core.domain.model.voyage.SampleVoyages.NEW_YORK_TO_DALLAS;
import se.citerus.dddsample.tracking.core.domain.model.voyage.Voyage;

public class LegTest {

  final Voyage voyage = NEW_YORK_TO_DALLAS;

  @Test(expected = NullPointerException.class)
  public void testConstructor() throws Exception {
    Leg.deriveLeg(null, null, null);
  }

  @Test
  public void legThatFollowsPartOfAVoyage() {
    Leg chicagoToDallas = Leg.deriveLeg(voyage, CHICAGO, DALLAS);

    assertEquals(chicagoToDallas.loadTime(), toDate("2008-10-24", "21:25"));
    assertEquals(chicagoToDallas.loadLocation(), CHICAGO);
    assertEquals(chicagoToDallas.unloadTime(), toDate("2008-10-25", "19:30"));
    assertEquals(chicagoToDallas.unloadLocation(), DALLAS);
  }

  @Test
  public void legThatFollowsAnEntireVoyage() {
    Leg newYorkToDallas = Leg.deriveLeg(voyage, NEWYORK, DALLAS);

    assertEquals(newYorkToDallas.loadTime(), toDate("2008-10-24", "07:00"));
    assertEquals(newYorkToDallas.loadLocation(), NEWYORK);
    assertEquals(newYorkToDallas.unloadTime(), toDate("2008-10-25", "19:30"));
    assertEquals(newYorkToDallas.unloadLocation(), DALLAS);
  }

  @Test(expected = IllegalArgumentException.class)
  public void locationsInWrongOrder() {
    Leg.deriveLeg(voyage, DALLAS, CHICAGO);
  }

  @Test(expected = IllegalArgumentException.class)
  public void endLocationNotOnVoyage() {
    Leg.deriveLeg(voyage, CHICAGO, HELSINKI);
  }

  @Test(expected = IllegalArgumentException.class)
  public void startLocationNotOnVoyage() {
    Leg.deriveLeg(voyage, HONGKONG, DALLAS);
  }

  @Test
  public void matchActivity() {
    Leg newYorkToDallas = Leg.deriveLeg(voyage, NEWYORK, DALLAS);

    assertTrue(newYorkToDallas.matchesActivity(loadedOnto(voyage).in(NEWYORK)));
    assertTrue(newYorkToDallas.matchesActivity(unloadedOff(voyage).in(DALLAS)));
    assertFalse(newYorkToDallas.matchesActivity(loadedOnto(voyage).in(DALLAS)));
    assertFalse(newYorkToDallas.matchesActivity(unloadedOff(voyage).in(NEWYORK)));
  }

  @Test
  public void deriveActivities() {
    Leg newYorkToDallas = Leg.deriveLeg(voyage, NEWYORK, DALLAS);
    
    assertThat(newYorkToDallas.deriveLoadActivity(), is(HandlingActivity.loadedOnto(voyage).in(NEWYORK)));
    assertThat(newYorkToDallas.deriveUnloadActivity(), is(HandlingActivity.unloadedOff(voyage).in(DALLAS)));
  }
}
