import java.net.URL;

/**
 * Application entry point for the IST 146 Weather App Project.
 *
 */
public class Main {

    /**
     * Application entry point
     *
     * @param args Command-line arguments; ignored by this application.
     */
    public static void main(String[] args) {
        WeatherModelTemplate model = new WeatherModel();
        WeatherView view = new WeatherView();
        WeatherController controller = new WeatherController(model, view);
        controller.run();
    }
}
