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
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.Year;

public class MusicApplication {

    // *********** 1) CREIAMO UNA SOLA ISTANZA "IN MEMORIA" CHE VALE PER TUTTO IL PROGRAMMA ***********
    private static final ArtistDAO artistDAO = new inMemoryArtistDAO();
    private static final TrackDAO trackDAO = new inMemoryTrackDAO();
    private static final ArtistService artistService = new ArtistService(trackDAO, artistDAO);
    private static final TrackService trackService = new TrackService(trackDAO, artistDAO);

    public static void main(String[] args) {
        String ans;
        Scanner input = new Scanner(System.in);

        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }
        System.out.print(" Welcome to the Music Application ");
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }
        System.out.println(" ");


        boolean main_loop = true;
        while (main_loop) {
            boolean loop = true;
            while(loop){
                System.out.println("\n\n Please choose what you would like to do");
                System.out.println(" 1) - Update the Music Application");
                System.out.println(" 2) - Consult Music Application ");
                System.out.println(" E) for Exit");
                ans = input.nextLine().trim();
                System.out.println(" ");
                switch(ans){
                    case "1":
                        loop = false;
                        main_loop = updateMusicMenu();
                        break;
                    case "2":
                        loop = false;
                        main_loop = consultMusicMenu();
                        break;
                    case "e","E":
                        loop = false;
                        main_loop = false;
                        break;
                    default:
                        System.out.println("INPUT ERROR - Please enter a valid option");
                        break;
                }
            }
        }
    }

    public static Boolean updateMusicMenu (){
        Scanner input_UM = new Scanner(System.in);
        String ans_UM;
        boolean choose = false;
        System.out.println(" ");
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }
        System.out.print("  Welcome to the Music Application - UPDATE Section  ");
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }
        System.out.println(" ");
        boolean update_MM_loop = true;
        while(update_MM_loop){
            System.out.println("Please choose what you would like to update \n 1) Update Track \n 2) Update Artist \n 0) Previous Menu\n E) for Exit");
            ans_UM = input_UM.nextLine().trim();
            switch(ans_UM){
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
                case "E","e":
                    update_MM_loop = false;
                    choose = false;
                    break;
                default:
                    System.out.println("INPUT ERROR - Please enter a valid option\n");
                    break;
            }
        }
        return choose;
    }

    public static Boolean updateTrack (String title) {

        String ans_TR;
        String album = "";
        String t_title = "";
        String genre = "";
        int yyyy = 1900;
        boolean choose = false;
        boolean loop;
        boolean loop2;
        boolean updateTR;

        List<String> artistlist = new ArrayList<>();
        String single_artist;

        System.out.println(" ");
        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }
        System.out.print("  Welcome to the Music Application - UPDATE TRACK Section ");
        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }
        System.out.println(" ");

        // *********** 2) USIAMO SEMPRE LA STESSA ISTANZA trackService / artistService ***********
        //    (nessun new inMemory... all'interno!)

        boolean update_TR_loop = true;
        while (update_TR_loop) {
            ans_TR = "2"; // By default, intendiamo "create a new Track"

            /*TITLE - NOT ALLOWED EMPTY*/
            if (title.isEmpty()) {
                loop = true;
                updateTR = false;
                while (loop) {
                    Scanner input_Title = new Scanner(System.in);
                    System.out.println("Please provide the TITLE of the Track");
                    t_title = input_Title.nextLine().trim();
                    System.out.println(" ");
                    if (!t_title.isEmpty()) {
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please enter a valid Name of Artist\n");
                    }
                }
                title = t_title;
            }
            else {
                updateTR = true;
            }

            String track_id;
            try {
                // Se esiste, ci prendiamo l'ID
                track_id = trackService.getTrackByTitle(title).getId();
            }
            catch(IllegalArgumentException e){
                // Se non esiste, ID vuoto
                track_id = "";
            }

            if (!track_id.isEmpty()) {
                // Track esistente
                loop = true;
                while (loop) {
                    Track track = trackService.getSingleTrackbyId(track_id);

                    System.out.println("The Song " + title + " already exist\n");
                    System.out.println("Genre: " + track.getGenre());
                    System.out.println("Year: " + track.getYear());
                    System.out.println("Album: " + track.getAlbum());
                    System.out.print("Artist list: ");

                    List<String> artistidlist = track.getArtistIds();
                    for (String aId : artistidlist) {
                        Artist artist = artistService.getArtistDetailsById(aId);
                        System.out.print(artist.getName()+", ");
                    }
                    System.out.println("\nDo you want to update or create a new track?\n 1 - Update\n 2 - Create");
                    Scanner input_TR = new Scanner(System.in);
                    ans_TR = input_TR.nextLine().trim();
                    switch(ans_TR){
                        case "1":
                            loop = false;
                            break;
                        case "2":
                            loop2 = true;
                            while (loop2) {
                                System.out.println("Please provide the TITLE of the new Track instead of " + title);
                                String new_title = input_TR.nextLine().trim();
                                if (!new_title.isEmpty() && !new_title.equals(title)) {
                                    title = new_title;
                                    loop2 = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please enter a valid Title");
                                }
                            }
                            loop = false;
                            break;
                        default:
                            System.out.println("INPUT ERROR - Please enter a valid option");
                            break;
                    }
                }
            }

            // ------ CREATE A NEW TRACK ------
            if (ans_TR.equals("2")){

                // album
                Scanner input_Album = new Scanner(System.in);
                System.out.println("Is " + title + " part of a music album?\n if YES - provide the ALBUM TITLE\n if NO - let empty");
                album = input_Album.nextLine().trim();
                System.out.println(" ");
                if (album.isEmpty()) {
                    album = "SINGLE";
                }

                // genere
                loop = true;
                while (loop) {
                    Scanner input_Genre = new Scanner(System.in);
                    System.out.println("Please provide the GENRE of the Track " + title);
                    genre = input_Genre.nextLine().trim().toUpperCase();
                    if (MusicGenres.ALLOWED_GENRES.contains(genre.toUpperCase())) {
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR");
                        System.out.println("Genre " + genre + " is not allowed\nPlease enter a valid Genre from the list:\n");
                        for (int i = 0; i < MusicGenres.ALLOWED_GENRES.size(); i++) {
                            if (i % 5 ==0 && i>5) {
                                System.out.println(" ");
                            }
                            System.out.print(MusicGenres.ALLOWED_GENRES.get(i) + ", ");
                        }
                        System.out.println(" ");
                    }
                    System.out.println(" ");
                }

                // anno
                loop = true;
                while (loop) {
                    Scanner input_Year = new Scanner(System.in);
                    try {
                        System.out.print("Please provide the YEAR of the Track\n");
                        yyyy = input_Year.nextInt();
                    } catch (InputMismatchException ignored) {
                        System.out.print("year is " + yyyy);
                        yyyy = 0;
                    }

                    int year = Year.now().getValue();
                    if (yyyy >= 1900 && yyyy <= year) {
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please enter a valid Year from 1900 to " + year + "\n");
                    }
                }

                // artisti (almeno uno)
                loop = true;
                while (loop) {
                    Scanner input_Artist = new Scanner(System.in);
                    System.out.println(" ");
                    System.out.println("Please provide the ARTIST of the Track " + title);
                    single_artist = input_Artist.nextLine().trim();
                    if (!single_artist.isEmpty()) {
                        artistlist.add(single_artist);
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please provide at least 1 Artist name");
                    }
                }

                // eventuali artisti aggiuntivi
                loop = true;
                while (loop) {
                    Scanner input_Additional_Artist = new Scanner(System.in);
                    System.out.println("please provide additional ARTIST of the Track " + title + "\n Artist List:");
                    for (String s : artistlist) {
                        System.out.print(s + ", ");
                    }
                    System.out.println("\n(Leave empty if there are no more artists to add)");
                    single_artist = input_Additional_Artist.nextLine().trim();
                    if (!single_artist.isEmpty()) {
                        artistlist.add(single_artist);
                    } else {
                        loop = false;
                    }
                }

                // validiamo se l'artista esiste, se non esiste chiediamo se crearlo
                for (int i = 0; i < artistlist.size(); i++) {
                    single_artist = artistlist.get(i);
                    String artist_id;
                    try{
                        artist_id = artistService.getArtistByName(single_artist).getId();
                    }
                    catch(IllegalArgumentException e){
                        artist_id = "";
                    }

                    if (artist_id.isEmpty()){
                        System.out.println(" ");
                        System.out.println("WARNING the Artist " + single_artist + " is not in the database");
                        loop = true;
                        while (loop) {
                            Scanner input_Artist_list = new Scanner(System.in);
                            System.out.println("Do you want to add this artist now? (Y/N)");
                            String ans = input_Artist_list.nextLine().toUpperCase().trim();
                            switch (ans) {
                                case "Y","YES":
                                    if (updateArtist(single_artist)) {
                                        loop = false;
                                    }
                                    else {
                                        System.out.println("ERROR CREATING NEW ARTIST");
                                    }
                                    break;
                                case "N","NO":
                                    artistlist.remove(i);
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("INPUT ERROR - Please enter only YES or NO");
                                    break;
                            }
                        }
                    }
                }

                // a questo punto creiamo la track
                trackService.createTrack("", yyyy, genre, album, title, artistlist);

            } else {
                // ------ UPDATE AN EXISTING TRACK ------
                loop = true;
                while (loop) {
                    Scanner input_Title = new Scanner(System.in);
                    System.out.println("Please provide the TITLE of the Track to UPDATE");
                    String new_title = input_Title.nextLine().trim();

                    if (!new_title.isEmpty() && !new_title.equals(title)) {
                        title = new_title;
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please enter a valid Title");
                    }
                }

                //String track_id = trackService.getTrackByTitle(title).getId();
                yyyy = trackService.getYearByTrackId(track_id);
                genre = trackService.getGenreByTrackId(track_id);
                album = trackService.getAlbumTrackId(track_id);

                loop = true;
                while (loop) {
                    System.out.println("What do you want to edit?");
                    System.out.println("1 - Title: " + title);
                    System.out.println("2 - Genre: " + genre);
                    System.out.println("3 - Year: " + yyyy);
                    System.out.println("4 - Album: " + album);
                    System.out.print("5 - Artist list: ");
                    trackService.getArtistsByTrackId(track_id).forEach(a -> System.out.print(a.getName() + ", "));
                    System.out.println();

                    Scanner input_TR = new Scanner(System.in);
                    ans_TR = input_TR.nextLine().trim();

                    switch (ans_TR) {
                        case "1":
                            boolean loop2a = true;
                            while (loop2a) {
                                System.out.println("Please provide a new TITLE of the Track (old = " + title + ")");
                                String new_title = input_TR.nextLine().trim();
                                if (!new_title.isEmpty() && !new_title.equals(title)) {
                                    title = new_title;
                                    loop2a = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please enter a NEW valid Title");
                                }
                            }
                            loop = false;
                            break;

                        case "2":
                            boolean loop2b = true;
                            while (loop2b) {
                                Scanner input_Genre = new Scanner(System.in);
                                System.out.println("Please provide the NEW GENRE of the Track " + title);
                                genre = input_Genre.nextLine().trim().toUpperCase();
                                if (MusicGenres.ALLOWED_GENRES.contains(genre)) {
                                    loop2b = false;
                                } else {
                                    System.out.println("INPUT ERROR - Genre not allowed\n");
                                }
                            }
                            loop = false;
                            break;

                        case "3":
                            boolean loop2c = true;
                            while (loop2c) {
                                int new_yyyy;
                                try {
                                    System.out.print("Please provide the new YEAR: ");
                                    new_yyyy = Integer.parseInt(input_TR.nextLine());
                                } catch (NumberFormatException ignored) {
                                    new_yyyy = 0;
                                }

                                int year = Year.now().getValue();
                                if (new_yyyy >= 1900 && new_yyyy <= year && new_yyyy != yyyy) {
                                    yyyy = new_yyyy;
                                    loop2c = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please enter a NEW valid Year (1900 - " + year + ")");
                                }
                            }
                            loop = false;
                            break;

                        case "4":
                            boolean loop2d = true;
                            while (loop2d) {
                                Scanner input_Album = new Scanner(System.in);
                                System.out.println("Provide the new ALBUM TITLE (empty => SINGLE)");
                                String new_album = input_Album.nextLine().trim();
                                if (new_album.isEmpty()) {
                                    new_album = "SINGLE";
                                }
                                if (!new_album.equals(album)) {
                                    album = new_album;
                                    loop2d = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please enter a valid NEW ALBUM TITLE (different from current)");
                                }
                            }
                            loop = false;
                            break;

                        case "5":
                            boolean loop2e = true;
                            while (loop2e) {
                                Scanner input_Artist = new Scanner(System.in);
                                System.out.println("Please provide an ARTIST of the Track " + title + " (at least 1)");
                                single_artist = input_Artist.nextLine().trim();
                                if (!single_artist.isEmpty()) {
                                    artistlist.add(single_artist);
                                    loop2e = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please provide at least 1 Artist name");
                                }
                            }

                            boolean moreArtists = true;
                            while (moreArtists) {
                                System.out.println("\nAdd more ARTISTS for " + title + " (Leave empty if none)");
                                Scanner input_Artist_list = new Scanner(System.in);
                                single_artist = input_Artist_list.nextLine().trim();
                                if (!single_artist.isEmpty()) {
                                    artistlist.add(single_artist);
                                } else {
                                    moreArtists = false;
                                }
                            }

                            // Creiamo una lista di artisti + validazione
                            for (int i = 0; i < artistlist.size(); i++) {
                                single_artist = artistlist.get(i);
                                String idCandidate;
                                try {
                                    idCandidate = artistService.getArtistByName(single_artist).getId();
                                } catch (IllegalArgumentException e) {
                                    idCandidate = "";
                                }
                                if (idCandidate.isEmpty()) {
                                    // L'artista non esiste
                                    System.out.println("WARNING: " + single_artist + " is not in the database.");
                                    boolean doneLoop = false;
                                    while (!doneLoop) {
                                        System.out.println("Do you want to add this artist now? (Y/N)");
                                        String ans = input_TR.nextLine().trim().toUpperCase();
                                        switch (ans) {
                                            case "Y":
                                            case "YES":
                                                // creiamo l'artista
                                                updateArtist(single_artist);
                                                doneLoop = true;
                                                break;
                                            case "N":
                                            case "NO":
                                                artistlist.remove(i);
                                                i--;
                                                doneLoop = true;
                                                break;
                                            default:
                                                System.out.println("Please answer Y or N");
                                        }
                                    }
                                }
                            }

                            loop = false;
                            break;

                        default:
                            System.out.println("INPUT ERROR - Please enter a valid option");
                            break;
                    }
                }

                // Aggiorniamo la track con i nuovi dati
                Track upd_track = new Track(track_id, yyyy, genre, album, title, artistlist);
                trackService.updateTrack(upd_track);
            }

            // Se *prima* era "updateTR = false", significa che l'utente aveva un titolo vuoto
            // Ritorniamo al menu?
            if (!updateTR){
                loop=true;
                while (loop) {
                    System.out.println("Jobs Done \n 1) Update new Track \n E) for Exit \n 0) Previous menu\n");
                    Scanner input_TR = new Scanner(System.in);
                    ans_TR = input_TR.next();
                    switch (ans_TR){
                        case "1":
                            loop = false;
                            break;
                        case "E":
                            loop = false;
                            update_TR_loop = false;
                            choose = false;
                            break;
                        case "0":
                            loop = false;
                            update_TR_loop = false;
                            choose = true;
                            break;
                        default:
                            System.out.println("INPUT ERROR - Please enter a valid option\n");
                            break;
                    }
                }
            } else {
                update_TR_loop = false;
                choose = true;
            }
        }

        return choose;
    }



    public static Boolean updateArtist (String name){
        Scanner input_AR = new Scanner(System.in);

        String a_name = "";
        String a_genre = "";
        boolean choose = false;
        boolean loop;
        boolean updateAR = false;
        List<String> tracklist = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }
        System.out.print("  Welcome to the Music Application - UPDATE ARTIST Section ");
        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }
        System.out.println();

        boolean update_AR_loop = true;
        while (update_AR_loop) {
            // ans_AR = "2"; // Non necessariamente ci serve

            if (name.isEmpty()) {
                loop = true;
                updateAR = false;
                while (loop) {
                    System.out.println("Please provide a NAME of Artist");
                    a_name = input_AR.nextLine().trim();
                    System.out.println("");
                    if (!a_name.isEmpty()) {
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please enter a valid Name of Artist");
                    }
                }
                name = a_name;
            }
            else {
                updateAR = true;
            }

            // USIAMO SEMPRE LE STESSE ISTANZE
            String artist_id;
            try{
                artist_id = artistService.getArtistByName(name).getId();
            }  catch(IllegalArgumentException e){
                artist_id = "";
            }

            // GENRE
            loop = true;
            while (loop) {
                Scanner input_Genre = new Scanner(System.in);
                System.out.println("Please provide the GENRE of the Artist " + name + " (es. ROCK, POP, ECC..)");
                a_genre = input_Genre.nextLine().trim().toUpperCase();
                if (MusicGenres.ALLOWED_GENRES.contains(a_genre)) {
                    loop = false;
                } else {
                    System.out.println("INPUT ERROR - Genre " + a_genre + " is not allowed. Allowed: " + MusicGenres.ALLOWED_GENRES);
                }
                System.out.println(" ");
            }

            // TRACK LIST: l'utente può aggiungere brani in questa sezione, ma non è obbligatorio
            loop = true;
            while (loop) {
                System.out.println("Please provide a TRACK name for Artist " + name + " or leave empty to end:");
                System.out.println("Current track list: " + tracklist);
                String single_track = input_AR.nextLine().trim();
                if (!single_track.isEmpty()) {
                    tracklist.add(single_track);
                } else {
                    loop = false;
                }
            }

            // per ogni brano, se non esiste, chiediamo se vogliamo crearlo
            for (int i = 0; i < tracklist.size(); i++) {
                String single_track = tracklist.get(i);
                String track_id;
                try{
                    track_id = trackService.getTrackByTitle(single_track).getId();
                }
                catch(IllegalArgumentException e){
                    track_id = "";
                }

                if (track_id.isEmpty()){
                    System.out.println("WARNING: Track " + single_track + " is not in the database");
                    boolean done = false;
                    while (!done) {
                        System.out.println("Do you want to add this track now? (Y/N)");
                        String ans = input_AR.nextLine().trim().toUpperCase();
                        switch (ans) {
                            case "Y","YES":
                                if (updateTrack(single_track)) {
                                    done = true;
                                } else {
                                    System.out.println("ERROR CREATING NEW TRACK");
                                    done = true; // esci per evitare loop infinito
                                }
                                break;
                            case "N","NO":
                                tracklist.remove(i);
                                i--;
                                done = true;
                                break;
                            default:
                                System.out.println("INPUT ERROR - Please enter only YES/NO");
                                break;
                        }
                    }
                }
            }

            // Adesso verifichiamo se l'artista esiste: se no -> create, se sì -> update
            Artist existingArtist = null;
            if (!artist_id.isEmpty()) {
                try {
                    existingArtist = artistService.getArtistDetailsById(artist_id);
                } catch (Exception ex) {
                    existingArtist = null;
                }
            }

            if (existingArtist == null) {
                // Non esiste, quindi creo
                artistService.createArtist(artist_id, name, a_genre, tracklist);
            } else {
                // Esiste, quindi aggiorno
                Artist upd_artist = new Artist(artist_id, name, a_genre, tracklist);
                artistService.updateArtist(upd_artist);
            }

            if (!updateAR){
                loop = true;
                while (loop) {
                    Scanner input = new Scanner(System.in);
                    System.out.println("Jobs Done\n 1 - Update another new ARTIST \n E - for Exit\n 0 - Previous menu");
                    String ans = input.nextLine().trim();
                    switch (ans){
                        case "1":
                            loop = false;
                            break;
                        case "E":
                        case "e":
                            loop = false;
                            update_AR_loop = false;
                            choose = false;
                            break;
                        case "0":
                            loop = false;
                            update_AR_loop = false;
                            choose = true;
                            break;
                        default:
                            System.out.println("INPUT ERROR - Please enter a valid option");
                            break;
                    }
                }
            } else {
                update_AR_loop = false;
                choose = true;
            }
        }
        return choose;
    }


    public static boolean consultMusicMenu() {
        String choose = Menu();
        String separator = "-".repeat(50);
        boolean loop = true;
        if (choose.equals("Artist Reporting Tool")){
            Artist_Framework();
        } else if (choose.equals("Track Reporting Tool")){
            Track_Framework();
        } else if (choose.equals("Exit")){
            System.out.println(separator);
            System.out.println("Exiting.. going on Main Menu \n \n \n");
            System.out.println(separator);
            // Torno al main
            String[] arguments = {};
            main(arguments);
        }

        return loop;
    }

    public static String Menu(){
        String separator = "-".repeat(50);
        Scanner input_CM = new Scanner(System.in);
        System.out.println(separator);
        System.out.print("  Welcome to the Music Application - CONSULT Section \n ");
        System.out.println(separator);
        System.out.println("Select: \n 1) For Artist Reporting Tool \n 2) For Track Reporting Tool \n E) for Exit");
        String ans = input_CM.nextLine().trim();
        String choose = "";
        switch (ans){
            case "1":
                choose = "Artist Reporting Tool";
                break;
            case "2":
                choose = "Track Reporting Tool";
                break;
            case "E":
            case "e":
                choose = "Exit";
                break;
            default:
                System.out.println("INPUT ERROR - Please enter a valid option");
                choose = "nothing";
                break;
        }
        return choose;
    }

    // *********** 3) USIAMO I SERVICE E DAO STATICI NEI METODI SOTTO ***********
    public static void Artist_Framework() {
        Scanner input = new Scanner(System.in);
        String separator = "-".repeat(50);

        System.out.println(separator);
        System.out.println("  Artist Reporting Tool  ");
        System.out.println(separator);
        System.out.println("Select:");
        System.out.println(" 1) Get All Artists");
        System.out.println(" 2) Get Tracks By Artist ID");
        System.out.println(" 3) Get Tracks By Artist Name");
        System.out.println(" 4) Get Artists By Genre");
        System.out.println(" 0) Get to Main Menu");
        System.out.println(" E) Exit");
        System.out.print("Your choice: ");

        String ans = input.nextLine().trim();

        switch (ans) {
            case "1":
                List<Artist> artists = artistService.getAllArtists();
                if (artists.isEmpty()) {
                    System.out.println("No artists found.");
                } else {
                    System.out.println("All Artists:");
                    for (Artist artist : artists) {
                        System.out.println("ID: " + artist.getId() + ", Name: " + artist.getName() + ", Genre: " + artist.getGenre());
                    }
                }
                break;

            case "2":
                System.out.print("Enter Artist ID: ");
                String artistId = input.nextLine().trim();
                try {
                    List<Track> tracksById = artistService.getTracksByArtistId(artistId);
                    System.out.println("Tracks for Artist ID " + artistId + ":");
                    for (Track track : tracksById) {
                        System.out.println("Title: " + track.getTitle() + ", Album: " + track.getAlbum());
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "3":
                System.out.print("Enter Artist Name: ");
                String artistName = input.nextLine().trim();
                try {
                    Artist artistByName = artistService.getArtistByName(artistName);
                    List<Track> tracksByName = artistService.getTracksByArtistId(artistByName.getId());
                    System.out.println("Tracks for Artist " + artistName + ":");
                    for (Track track : tracksByName) {
                        System.out.println("Title: " + track.getTitle() + ", Album: " + track.getAlbum());
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "4":
                boolean validGenre = false;
                while (!validGenre) {
                    System.out.print("Enter Genre: ");
                    String genre = input.nextLine().trim().toUpperCase();
                    if (!MusicGenres.ALLOWED_GENRES.contains(genre)) {
                        System.out.println("Invalid genre. Allowed genres: " + MusicGenres.ALLOWED_GENRES);
                        System.out.println("Do you want to try again? (Y/N)");
                        String retry = input.nextLine().trim().toUpperCase();
                        if (retry.equals("N")) {
                            return;
                        }
                    } else {
                        validGenre = true;
                        try {
                            List<Artist> artistsByGenre = artistService.getArtistsByGenre(genre);
                            System.out.println("Artists in genre " + genre + ":");
                            for (Artist artist : artistsByGenre) {
                                System.out.println("ID: " + artist.getId() + ", Name: " + artist.getName());
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                break;
            case "0":
                Menu();
            case "E":
            case "e":
                System.out.println("Returning to the main menu...");
                break;

            default:
                System.out.println("INPUT ERROR - Please enter a valid option.");
                break;
        }
    }

    public static void Track_Framework() {
        Scanner input = new Scanner(System.in);
        String separator = "-".repeat(50);

        System.out.println(separator);
        System.out.println("  Track Reporting Tool  ");
        System.out.println(separator);
        System.out.println("Select:");
        System.out.println(" 1) Get All Tracks");
        System.out.println(" 2) Get Tracks By Artist ID");
        System.out.println(" 3) Get Tracks By Artist Name");
        System.out.println(" 4) Get Tracks By Title");
        System.out.println(" 5) Get Tracks By Genre");
        System.out.println(" 6) Get Tracks By Year");
        System.out.println(" 0) Get to Main Menu");
        System.out.println(" E) Exit");
        System.out.print("Your choice: ");

        String ans = input.nextLine().trim();

        switch (ans) {
            case "1":
                List<Track> tracks = trackService.getAllTracks();
                if (tracks.isEmpty()) {
                    System.out.println("No tracks found.");
                } else {
                    System.out.println("All Tracks:");
                    for (Track track : tracks) {
                        System.out.println("Title: " + track.getTitle()
                                + ", Album: " + track.getAlbum()
                                + ", Year: " + track.getYear());
                    }
                }
                break;

            case "2":
                System.out.print("Enter Artist ID: ");
                String artistId = input.nextLine().trim();
                try {
                    List<Track> tracksById = trackService.getTracksByArtistID(artistId);
                    for (Track track : tracksById) {
                        System.out.println("Title: " + track.getTitle() + ", Album: " + track.getAlbum());
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "3":
                System.out.print("Enter Artist Name: ");
                String artistName = input.nextLine().trim();
                try {
                    Artist artist = artistService.getArtistByName(artistName);
                    List<Track> tracksByName = trackService.getTracksByArtistID(artist.getId());
                    for (Track track : tracksByName) {
                        System.out.println("Title: " + track.getTitle() + ", Album: " + track.getAlbum());
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "4":
                System.out.print("Enter Track Title: ");
                String title = input.nextLine().trim();
                try {
                    List<Track> trackByTitle = trackService.getTrackById(title);
                    for (Track track : trackByTitle) {
                        System.out.println("Title: " + track.getTitle() + ", Album: " + track.getAlbum());
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "5":
                boolean validGenre = false;
                while (!validGenre) {
                    System.out.print("Enter Genre: ");
                    String genre = input.nextLine().trim().toUpperCase();
                    if (!MusicGenres.ALLOWED_GENRES.contains(genre)) {
                        System.out.println("Invalid genre. Allowed genres: " + MusicGenres.ALLOWED_GENRES);
                        System.out.println("Do you want to try again? (Y/N)");
                        String retry = input.nextLine().trim().toUpperCase();
                        if (retry.equals("N")) {
                            return;
                        }
                    } else {
                        validGenre = true;
                        List<Track> tracksByGenre = trackService.getTracksByGenre(genre);
                        for (Track track : tracksByGenre) {
                            System.out.println("Title: " + track.getTitle() + ", Album: " + track.getAlbum());
                        }
                    }
                }
                break;

            case "6":
                System.out.print("Enter Year: ");
                try {
                    int year = Integer.parseInt(input.nextLine().trim());
                    List<Track> tracksByYear = trackService.getTracksByYear(year);
                    for (Track track : tracksByYear) {
                        System.out.println("Title: " + track.getTitle() + ", Album: " + track.getAlbum());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid year format.");
                }
                break;
            case "0":
                Menu();
            case "E":
            case "e":
                System.out.println("Returning to the main menu...");
                break;

            default:
                System.out.println("INPUT ERROR - Please enter a valid option.");
                break;
        }
    }

}
