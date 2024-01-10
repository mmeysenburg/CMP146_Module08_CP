import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Console-based user interface for this application.
 */
public class WeatherView {

    /**
     * Display the main menu, and return the user's selection.
     *
     * @param state Currently selected state
     * @param county Currently selected county
     * @param month Currently selected month
     * @param year Currently selected year
     * @return User's input, in [1, 6].
     */
    public int mainMenu(String state, String county, String month, int year) {
        System.out.println("Please choose an option: ");
        System.out.println("\t1) Select state (currently " + state + ")");
        System.out.println("\t2) Select county (currently " + county + ")");
        System.out.println("\t3) Select month (currently " + month + ")");
        System.out.println("\t4) Select year (currently " + year + ")");
        System.out.println("\t5) Fetch weather data");
        System.out.println("\t6) Exit the program");

        Scanner stdin = new Scanner(System.in);
        System.out.println();
        System.out.print("Choice (1 - 6): ");
        int choice = -1;
        try {
            choice = Integer.parseInt(stdin.nextLine());
        } catch (NumberFormatException nfe) {

        }
        while(choice < 1 || choice > 6) {
            System.out.println("Please enter an integer in [1, 6].");
            System.out.print("Choice (1 - 6): ");
            choice = -1;
            try {
                choice = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException nfe) {

            }
        }

        return choice;
    }

    /**
     * Display list of states or counties, and get user's selection.
     *
     * @param stateOrCounty The word "state" or "county"
     * @param entities Array of states or counties to choose from
     * @return Index of the selected state or county.
     */
    public int stateCountyMenu(String stateOrCounty, String[] entities) {
        System.out.printf("Please choose a %s\n: ", stateOrCounty);
        int idx = 0;
        for(String entity : entities) {
            System.out.printf("\t%3d: %s\n", idx++, entity);
        }
        Scanner stdin = new Scanner(System.in);
        System.out.println();
        System.out.printf("Choice (0 - %d): ", entities.length - 1);
        int choice = -1;
        try {
            choice = Integer.parseInt(stdin.nextLine());
        } catch (NumberFormatException nfe) {

        }
        while(choice < 0 || choice >= entities.length) {
            System.out.printf("Please enter an integer in [0, %d].\n", entities.length - 1);
            System.out.printf("Choice (0 - %d): ", entities.length - 1);
            choice = -1;
            try {
                choice = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException nfe) {

            }
        }

        return choice;
    }

    /**
     * Get a year for the query.
     *
     * @return Year chosen by the user.
     */
    public int yearMenu() {
        System.out.printf("Please enter a year (yyyy): ");
        Scanner stdin = new Scanner(System.in);
        int year = -1;
        try {
            year = Integer.parseInt(stdin.nextLine());
        } catch (NumberFormatException nfe) {

        }
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        while (year < 1900 || year > currentYear) {
            System.out.printf("Please enter a year in [1900, %d].\n", currentYear);
            year = -1;
            try {
                year = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException nfe) {

            }
        }

        return year;
    }

    /**
     * Get the month to be used for the query.
     *
     * @return Index of the month chosen, in [0, 11].
     */
    public int monthMenu() {
        System.out.println("Please choose a month:");
        for (int i = 0; i < WeatherModel.months.length; i++) {
            System.out.printf("\t%d: %s\n", i, WeatherModel.months[i]);
        }

        Scanner stdin = new Scanner(System.in);
        System.out.print("Choice (0 - 11): ");
        int month = -1;
        try {
            month = Integer.parseInt(stdin.nextLine());
        } catch (NumberFormatException nfe) {

        }
        while (month < 0 || month > 11) {
            System.out.println("Please enter an integer in [0, 11].");
            System.out.print("Choice (0 - 11): ");
            month = -1;
            try {
                month = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException nfe) {

            }
        }

        return month;
    }

    /**
     * Display the currently selected parameters.
     *
     * @param state Currently selected state.
     * @param county Currently selected county.
     * @param month Currently selected month.
     * @param year Currently selected year.
     */
    public void showParameters(String state, String county, String month, int year) {
        System.out.printf("*** Current parameters: %s, %s, %s, %d\n",
                state, county, month, year);
    }

    /**
     * Produce a PNG line plot of the temperature data, using the external GNUPlot application.
     *
     * @param state Currently selected state.
     * @param county Currently selected county.
     * @param month Currently selected month.
     * @param year Currently selected year.
     * @param temps Array of doubles holding the temperatures to plot, in degrees F.
     */
    public void makePlot(String state, String county, String month, int year, double[] temps) {
        try {
            // root file name is based on the state/county/month/year parameters
            String fileRoot = state.replace(' ', '_') + "-" +
                    county.replace(' ', '_') + "-" +
                    month + year;

            // output data file
            FileWriter fw = new FileWriter(fileRoot + ".dat", false);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < temps.length; i++) {
                pw.println((i + 1) + "\t" + temps[i]);
            }
            pw.close();

            // output gnuplot command script
            fw = new FileWriter(fileRoot + ".p", false);
            pw = new PrintWriter(fw);
            pw.println("set terminal png size 1000, 1000");
            pw.println("set output \"" + fileRoot + ".png" + "\"");
            pw.println("set autoscale");
            pw.println("set title \"" + state + ", " + county + ", " +
                    month + ", " + year + "\"");
            pw.println("set xlabel \"Day\"");
            pw.println("set ylabel \"Temperature\"");
            pw.println("set xr [1:31]");
            pw.println("set yr [-30:115]");
            pw.println("plot \"" + fileRoot + ".dat" + "\" with lines");
            pw.close();

            // use gnuplot to make the plot
            String command = "gnuplot " + fileRoot + ".p";
            Process process = Runtime.getRuntime().exec(command);
            process.getErrorStream().transferTo(System.err);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
