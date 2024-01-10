import java.util.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test class for the WeatherModel class.
 */
public class WeatherModelTest {
  WeatherModelTemplate wmt;

  @Before
  public void setUp() throws Exception {
    wmt = new WeatherModel();
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testGetStates() {
    String[] states = {"Alabama", "Alaska", "Arizona", "Arkansas", "California",
            "Colorado", "Connecticut", "Delaware", "District of Columbia",
            "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana",
            "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
            "Massachusetts", "Michigan", "Minnesota", "Mississippi",
            "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire",
            "New Jersey", "New Mexico", "New York", "North Carolina",
            "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
            "Rhode Island", "South Carolina", "South Dakota", "Tennessee",
            "Texas", "Utah", "Vermont", "Virginia", "Washington",
            "West Virginia", "Wisconsin", "Wyoming"};

    String[] modelStates = wmt.getStates();

    assertEquals(states.length, modelStates.length);

    for(int i = 0; i < states.length; i++) {
      assertEquals(states[i], modelStates[i]);
    }
  }

  @Test
  public void testGetCounties() {
    String[] counties = {"Aleutians East Borough", "Aleutians West Census Area",
            "Anchorage Municipality", "Bethel Census Area", "Bristol Bay Borough",
            "Denali Borough", "Dillingham Census Area", "Fairbanks North Star Borough",
            "Haines Borough", "Hoonah-Angoon Census Area", "Juneau City and Borough",
            "Kenai Peninsula Borough", "Ketchikan Gateway Borough", "Kodiak Island Borough",
            "Kusilvak Census Area", "Lake and Peninsula Borough", "Matanuska-Susitna Borough",
            "Nome Census Area", "North Slope Borough", "Northwest Arctic Borough",
            "Petersburg Borough", "Prince of Wales-Hyder Census Area", "Sitka City and Borough",
            "Skagway Municipality", "Southeast Fairbanks Census Area", "Valdez-Cordova Census Area",
            "Wrangell City and Borough", "Yakutat City and Borough", "Yukon-Koyukuk Census Area"};

    String[] modelCounties = wmt.getCounties("Alaska");

    assertEquals(counties.length, modelCounties.length);

    for(int i = 0; i < counties.length; i++) {
      assertEquals(counties[i], modelCounties[i]);
    }
  }

  @Test
  public void testGetFIPS() {
    assertEquals("31151", wmt.getFIPS("Nebraska", "Saline County"));
    assertEquals("02020", wmt.getFIPS("Alaska", "Anchorage Municipality"));
    assertEquals("38101", wmt.getFIPS("North Dakota", "Ward County"));
  }

  /**
   * Test launcher.
   *
   * @param args Command-line arguments; ignored by 
   * this application
   */
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main("WeatherModelTest");
  }
}