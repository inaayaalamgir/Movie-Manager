
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieV3 {

    //movie class
    static class Movie {
        String title;
        int rating;

        Movie(String title, int rating) {
            this.title = title;
            this.rating = rating;
        }

        public String toString() {
            String stars = "*".repeat(rating);
            return title + " (" + stars + ")";
        }
    }

    private static ArrayList<Movie> movies = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "movies.txt";

    public static void main(String[] args) {
        loadMovies();

        boolean running = true;

        while (running) {
            System.out.println("\n=== Movie Manager ===");
            System.out.println("1. Add Movie");
            System.out.println("2. View Movies");
            System.out.println("3. Edit Movie");
            System.out.println("4. Delete Movie");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> addMovie();
                case 2 -> listMovies();
                case 3 -> editMovie();
                case 4 -> deleteMovie();
                case 5 -> {
                    saveMovies();
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    // Add a new movie
    private static void addMovie() {
        System.out.print("Enter movie title: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Movie title cannot be empty.");
            return;
        }

        System.out.print("Enter movie rating (1-5): ");
        int rating = getRatingInput();

        movies.add(new Movie(title, rating));
        System.out.println("\"" + title + "\" has been added with a rating of " + rating + " stars.");
    }

    // Display movies
    private static void listMovies() {
        if (movies.isEmpty()) {
            System.out.println("No movies added yet.");
        } else {
            System.out.println("\nYour Movies:");
            for (int i = 0; i < movies.size(); i++) {
                System.out.println((i + 1) + ". " + movies.get(i));
            }
        }
    }

    // Edit a movie's rating
    private static void editMovie() {
        if (movies.isEmpty()) {
            System.out.println("No movies to edit.");
            return;
        }

        listMovies();
        System.out.print("Enter the number of the movie you want to edit: ");
        int index = getIntInput() - 1;

        if (index < 0 || index >= movies.size()) {
            System.out.println("Invalid movie number.");
            return;
        }

        Movie movie = movies.get(index);
        System.out.print("Enter new rating for \"" + movie.title + "\" (1-5): ");
        int newRating = getRatingInput();

        movie.rating = newRating;
        System.out.println("Updated \"" + movie.title + "\" to " + newRating + " stars.");
    }

    // Delete a movie
    private static void deleteMovie() {
        if (movies.isEmpty()) {
            System.out.println("No movies to delete.");
            return;
        }

        listMovies();
        System.out.print("Enter the number of the movie you want to delete: ");
        int index = getIntInput() - 1;

        if (index < 0 || index >= movies.size()) {
            System.out.println("Invalid movie number.");
            return;
        }

        Movie removed = movies.remove(index);
        System.out.println("\"" + removed.title + "\" has been deleted.");
    }

    // get valid input
    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    // Get rating between 1–5
    private static int getRatingInput() {
        while (true) {
            int rating = getIntInput();
            if (rating >= 1 && rating <= 5) {
                return rating;
            }
            System.out.print("Rating must be between 1 and 5. Try again: ");
        }
    }

    // Save movie to file
    private static void saveMovies() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Movie m : movies) {
                writer.println(m.title + "|" + m.rating);
            }
            System.out.println("Movies saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving movies: " + e.getMessage());
        }
    }

    // Load movies from file
    private static void loadMovies() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String title = parts[0];
                    int rating = Integer.parseInt(parts[1]);
                    movies.add(new Movie(title, rating));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading movies: " + e.getMessage());
        }
    }
}
