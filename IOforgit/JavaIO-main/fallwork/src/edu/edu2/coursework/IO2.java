package edu.sdmesa.cisc191;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;

/**
 * Lead Author(s): Nikkia Miklos
 * @author Nikkia Miklos
 * @version 2024.11.9.001
 * Version/date: 2024.11.9.001
 * 
 * Responsibilities of class:
 * Provides methods for reading, writing, and appending to flies, retrieves the current date and time and Handles input and output.
 * 
 * @param content the content to write or append to the file
 * @param apiUrl the URL of the API 
 */


public class IO {
	/**
     * Reads the contents of a file and returns it as a string.
     *
     * @param fileName the name of the file to read from
     * @return the contents of the file as a single string, or an empty string if the file is not found
     */
    public static String readTestResults(String fileName) {
        File file = new File(fileName);
        Scanner scanner = null;
        StringBuilder content = new StringBuilder();

        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            return "";
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return content.toString().trim();
    }
    /**
     * Writes the specified content to a file, over writing any existing content.
     *
     * @param fileName the name of the file to write to
     * @param content the content to write to the file
     */
    public static void startTestResults(String fileName, String content) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println(content);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot write file. File will not be written.");
        }
    }
    /**
     * Adds specified content to the end of a file or Creates file if it does not exist.
     *
     * @param fileName the name of the file to append to
     * @param content the content to append to the file
     */
    public static void appendTestResult(String fileName, String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(content);
        } catch (IOException e) {
            System.out.println("Cannot write file. File will not be written.");
        }
    }
    /**
     * Reads the current date and time from an API to make datetime string.
     *
     * @param apiUrl the URL of the API to retrieve the date and time from
     * @return the datetime string in "YYYY-MM-DDThh:mm:ss" format 
     */
    public static String readDateTime(String apiUrl) {
        StringBuilder content = new StringBuilder();

        try (Scanner scanner = new Scanner(URI.create(apiUrl).toURL().openStream())) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
        } catch (IOException e) {
            return "";  // Return empty string on failure
        }

        // Looks for the dateTime field in the JSON response
        int startIndex = content.indexOf("\"dateTime\":\"") + 12;
        int endIndex = content.indexOf("\"", startIndex);

        // Checks for valid indexes before parsing the substring
        if (startIndex > 11 && endIndex > startIndex) {
            String dateTimeString = content.substring(startIndex, endIndex);

            // Check length of 26 characters (YYYY-MM-DDThh:mm:ss.SSSSSS)
            if (dateTimeString.length() == 26) {
                return dateTimeString;
            }

            // Handle case if there's additional info
            if (dateTimeString.length() > 26) {
                return dateTimeString.substring(0, 26);  // Trimming to match the expected length
            }

            return dateTimeString.substring(0, 19);
        }

        return "";  // Return empty if format is wrong or indexes are invalid
    }
}
