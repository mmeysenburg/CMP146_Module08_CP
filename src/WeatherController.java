import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

/**
 * Controller for the IST 146 Weather App project.
 */
public class WeatherController {
    /** Reference to the model. */
    private WeatherModelTemplate model;
    /** Reference to the view. */
    private WeatherView view;
    /** Currently selected state. */
    private String state;
    /** Currently selected county. */
    private String county;
    /** Currently selected month. */
    private String month;
    /** Currently selected year. */
    private int year;

    /**
     * Construct the application's controller.
     *
     * @param model WeatherModel object that encapsulates the state, county,
     *              and FIPS information
     * @param view The console-based view object representing the user
     *             interface
     */
    public WeatherController(WeatherModelTemplate model, WeatherView view) {
        this.model = model;
        this.view = view;
        // use current month and year as the default
        month = WeatherModel.months[Calendar.getInstance().get(Calendar.MONTH)];
        year = Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * Run the application.
     */
    public void run() {
        // get state and county arrays
        String[] states = model.getStates();
        String[] counties = model.getCounties(states[0]);

        // set default state and county to first element of
        // each array
        state = states[0];
        county = counties[0];

        // display menu and act appropriately
        int choice = view.mainMenu(state, county, month, year);
        while (choice != 6) {
            switch (choice) {
                case 1:
                    // change the state
                    int idx = view.stateCountyMenu("state", states);
                    state = states[idx];
                    // default to 1st county in the state
                    counties = model.getCounties(state);
                    county = counties[0];
                    break;
                case 2:
                    // change the county
                    idx = view.stateCountyMenu("county", counties);
                    county = counties[idx];
                    break;
                case 3:
                    // set the month for the query
                    month = WeatherModel.months[view.monthMenu()];
                    break;
                case 4:
                    // set the year for the query
                    year = view.yearMenu();
                    break;
                case 5:
                    // get the index for the month
                    for (idx = 0; idx < WeatherModel.months.length; idx++) {
                        if (month.equalsIgnoreCase(WeatherModel.months[idx])) {
                            break;
                        }
                    }
                    idx++; // LocalDate expects 1-based vs. 0-based month indices
                    LocalDate date = LocalDate.now().withDayOfMonth(1).withMonth(idx).withYear(year);

                    // get list of high temperatures from the model
                    List<Double> temps = model.fetchData(state, county, date);

                    // convert to an array
                    double[] tempArray = new double[temps.size()];
                    for (int i = 0; i < tempArray.length; i++) {
                        tempArray[i] = temps.get(i);
                    }

                    // produce the output plot image
                    view.makePlot(state, county, month, year, tempArray);
                    break;
            } // switch

            // get next menu selection
            view.showParameters(state, county, month, year);
            choice = view.mainMenu(state, county, month, year);
        } // while
    }
}
