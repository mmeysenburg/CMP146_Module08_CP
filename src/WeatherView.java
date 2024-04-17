import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
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


    private int mapToRange(double v, double minOld, double maxOld, double minNew, double maxNew) {
        double rv = ((v - minOld) / (maxOld - minOld)) * (maxNew - minNew) + minNew;
        return (int)rv;
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
        BufferedImage image = new BufferedImage(1100, 1100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // background
        g.setColor(Color.WHITE);
        g.fillRect(0,0, 1100, 1100);

        // plot border
        g.setColor(Color.BLACK);
        g.drawRect(49, 49, 1000, 1000);

        // plot title
        Font font = g.getFont();
        FontMetrics fontMetrics = g.getFontMetrics(font);
        String title = state + ", " + county + ", " + month + ", " + year;
        g.drawString(title, 550 - fontMetrics.stringWidth(title) / 2,
                fontMetrics.getHeight() + 5);

        // axis labels
        g.drawString("Day", 550, 1080);

        for(int day = 0; day <= 30; day += 5) {
            int x = mapToRange(day, 0,31, 49, 1049);
            String s = Integer.toString(day);
            int sWidth = fontMetrics.stringWidth(s);
            g.drawString(s, x - sWidth / 2, 1065);
            g.drawLine(x, 1049, x, 1039);
        }

        g.translate(20.0, 550);
        g.rotate(Math.toRadians(-90));
        g.drawString("Temperature", 0, 0);
        g.rotate(Math.toRadians(90));
        g.translate(-20, -550);
        for(int temp = -20; temp <= 120; temp += 10) {
            int y = mapToRange(temp, -20, 120, 1049, 49);
            String s = Integer.toString(temp);
            int sWidth = fontMetrics.stringWidth(s);
            int sHeight = fontMetrics.getHeight();
            g.drawString(s,45 - sWidth, y + sHeight / 4);
            g.drawLine(49, y, 59, y);
        }

        // data
        g.setColor(Color.RED);
        for(int i = 1; i < temps.length; i++) {
            int day1 = mapToRange(i, 0, 31, 49, 1049);
            int day2 = mapToRange(i + 1, 0, 31, 49, 1049);
            int temp1 = mapToRange(temps[i - 1], -20, 120, 1049, 49);
            int temp2 = mapToRange(temps[i], -20, 120, 1049, 49);
            g.drawLine(day1, temp1, day2, temp2);
        }

        try {
            System.out.println("The temperatures for " + title + ":");
            System.out.println(Arrays.toString(temps));
            String fileName = state + "_" + county + "_" + month + "_" + year + ".jpg";
            ImageIO.write(image, "jpg", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to write image.");
        }
    }
}
