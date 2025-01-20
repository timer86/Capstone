//FC Update 20/01/2025 14:37

package MusicApplication;

import DAO.ArtistDAO;
import DAO.TrackDAO;
import DAO.inMemoryArtistDAO;
import DAO.inMemoryTrackDAO;
import Track.MusicGenres;
import Artist.Artist;
import Track.Track;
import Service.ArtistService;
import Service.TrackService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.*;

public class MusicApplication {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("********** Welcome to the Music Application **********");

        boolean main_loop = true;
        while (main_loop) {
            System.out.println("\nPlease choose what you would like to do:");
            System.out.println("1 - Update the Music Application");
            System.out.println("2 - Consult Music Application");
            System.out.println("E - Exit");

            String ans = input.next().trim();

            switch (ans) {
                case "1":
                    main_loop = updateMusicMenu();
                    break;
                case "2":
                    main_loop = consultMusicMenu();
                    break;
                case "e", "E":
                    main_loop = false;
                    break;
                default:
                    System.out.println("INPUT ERROR - Please enter a valid option");
                    break;
            }
        }

        System.out.println("Thank you for using the Music Application. Goodbye!");
    }

    public static Boolean updateMusicMenu() {
        Scanner input_UM = new Scanner(System.in);
        boolean choose = false;

        System.out.println("********** Welcome to the Music Application - UPDATE Section **********");

        boolean update_MM_loop = true;
        while (update_MM_loop) {
            System.out.println("\nPlease choose what you would like to update:");
            System.out.println("1 - Update one Track");
            System.out.println("2 - Update an Artist");
            System.out.println("0 - Previous Menu");
            System.out.println("E - Exit");

            String ans_UM = input_UM.next().trim();

            switch (ans_UM) {
                case "1":
                    update_MM_loop = updateTrack("");
                    choose = update_MM_loop;
                    break;
                case "2":
                    update_MM_loop = updateArtist("");
                    choose = update_MM_loop;
                    break;
                case "0":
                    update_MM_loop = false;
                    choose = true;
                    break;
                case "E", "e":
                    update_MM_loop = false;
                    return false;
                default:
                    System.out.println("INPUT ERROR - Please enter a valid option");
                    break;
            }
        }
        return choose;
    }

    public static Boolean updateTrack(String title) {
        Scanner input_TR = new Scanner(System.in);

        boolean choose = false;
        boolean loop = true;

        System.out.println("********** Welcome to the Music Application - UPDATE TRACK Section **********");

        while (loop) {
            System.out.print("Please provide the TITLE of the Track: ");
            title = input_TR.nextLine().trim();

            if (!title.isEmpty()) {
                loop = false;
            } else {
                System.out.println("INPUT ERROR - Please enter a valid title");
            }
        }

        TrackDAO trackDAO = new inMemoryTrackDAO();
        ArtistDAO artistDAO = new inMemoryArtistDAO();
        TrackService trackService = new TrackService(trackDAO, artistDAO);

        Track track = trackService.getTrackByTitle(title);

        if (track != null) {
            System.out.println("The Song " + title + " already exists");
            System.out.println("Genre: " + track.getGenre());
            System.out.println("Year: " + track.getYear());
            System.out.println("Album: " + track.getAlbum());
        } else {
            System.out.println("Track not found. Creating a new one.");
            // Prompt for details and create new track
            System.out.print("Enter Genre: ");
            String genre = input_TR.nextLine();
            System.out.print("Enter Album: ");
            String album = input_TR.nextLine();
            System.out.print("Enter Year: ");

            int year;
            while (true) {
                try {
                    year = Integer.parseInt(input_TR.nextLine().trim());
                    if (year >= 1900 && year <= Year.now().getValue()) {
                        break;
                    } else {
                        System.out.println("INPUT ERROR - Year must be between 1900 and " + Year.now().getValue());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("INPUT ERROR - Please enter a valid year.");
                }
            }

            System.out.println("Track successfully created!");
        }

        return choose;
    }

    public static Boolean updateArtist(String name) {
        Scanner input_AR = new Scanner(System.in);
        System.out.println("********** Welcome to the Music Application - UPDATE ARTIST Section **********");

        boolean loop = true;
        while (loop) {
            System.out.print("Please provide the NAME of the Artist: ");
            name = input_AR.nextLine().trim();

            if (!name.isEmpty()) {
                loop = false;
            } else {
                System.out.println("INPUT ERROR - Please enter a valid artist name");
            }
        }

        ArtistDAO artistDAO = new inMemoryArtistDAO();
        ArtistService artistService = new ArtistService(new inMemoryTrackDAO(), artistDAO);

        Artist artist = artistService.getArtistByName(name);

        if (artist != null) {
            System.out.println("The Artist " + name + " already exists with Genre: " + artist.getGenre());
        } else {
            System.out.println("Artist not found. Creating a new one.");
            System.out.print("Enter Genre: ");
            String genre = input_AR.nextLine();

            artistService.createArtist(null, name, genre, new ArrayList<>());
            System.out.println("Artist successfully created!");
        }

        return true;
    }

    public static Boolean consultMusicMenu() {
        System.out.println("********** Welcome to the Music Application - CONSULT Section **********");

        // Placeholder for consult functionalities
        System.out.println("Consultation feature is under development.");

        return true;
    }
}
