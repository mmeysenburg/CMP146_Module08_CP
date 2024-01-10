import java.time.LocalDate;
import java.util.List;

public interface WeatherModelTemplate {
    /** String versions of months. */
    public static final String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    /**
     * Get an array containing all of the state names, in alphabetical
     * order.
     *
     * @return String[] containing sorted state names.
     */
    public String[] getStates();

    /**
     * Get an array of all the counties in a given state, in alphabetical
     * order.
     *
     * @param state State to get counties for.
     *
     * @return String[] containing sorted county names.
     */
    public String[] getCounties(String state);

    /**
     * Given a state and county name, return the corresponding Federal Information Processing Standards (FIPS) code.
     *
     * @param state Name of state.
     *
     * @param county Name of county.
     *
     * @return Corresponding FIPS code.
     */
    public String getFIPS(String state, String county);

    /**
     * Query the NOAA CDO site for the specified temperatures. This method should
     * be called by the controller.
     *
     * The CDO site is queried via a call to the static NOAAReader.getTmaxData() method.
     *
     * @param state Name of the state to get data for.
     *
     * @param county Name of the county to get data for.
     *
     * @param startDate LocalDate somewhere within the month to fetch data for.
     *
     * @return List of doubles, containing the high temperatures, in degrees F,
     * for the specified state, county, and date.
     */
    public List<Double> fetchData(String state, String county, LocalDate startDate);
}