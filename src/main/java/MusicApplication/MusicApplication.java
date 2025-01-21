package MusicApplication;
//09:54 fc
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


    public static Boolean updateTrack(String initialTitle) {

        Scanner console = new Scanner(System.in);
        boolean choose = false;
        boolean update_TR_loop = true;

        // Queste variabili ci servono all'occorrenza
        String title = initialTitle.trim();
        boolean updateTR; // Indica se stiamo "aggiornando" una traccia esistente
        boolean loop;     // Flag per i cicli interni
        boolean loop2;    // Flag per i cicli interni

        while (update_TR_loop) {
            // Di default immaginiamo di creare una nuova traccia
            String ans_TR = "2";

            // Se non abbiamo un titolo iniziale, lo chiediamo
            if (title.isEmpty()) {
                updateTR = false; // Non abbiamo ancora traccia
                loop = true;
                while (loop) {
                    System.out.println("Please provide the TITLE of the Track:");
                    String inputTitle = console.nextLine().trim();
                    if (!inputTitle.isEmpty()) {
                        title = inputTitle;
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please enter a valid Title for the Track.");
                    }
                }
            } else {
                // Se "initialTitle" è già valorizzato, supponiamo che si voglia aggiornare
                updateTR = true;
            }

            // Verifichiamo se la traccia esiste già
            String track_id = "";
            try {
                Track existingTrack = trackService.getTrackByTitle(title);
                track_id = existingTrack.getId();
            } catch (IllegalArgumentException e) {
                track_id = "";
            }

            // Se la traccia esiste, chiediamo se aggiornarla o creare una nuova
            if (!track_id.isEmpty()) {
                loop = true;
                while (loop) {
                    // Mostriamo info
                    Track track = trackService.getSingleTrackbyId(track_id);
                    System.out.println("\nThe Track \"" + title + "\" already exists.");
                    System.out.println("Genre: " + track.getGenre());
                    System.out.println("Year: " + track.getYear());
                    System.out.println("Album: " + track.getAlbum());
                    System.out.print("Artist list: ");
                    for (String artistId : track.getArtistIds()) {
                        Artist art = artistService.getArtistDetailsById(artistId);
                        System.out.print(art.getName() + ", ");
                    }
                    System.out.println("\nDo you want to (1) Update this track or (2) Create a new one with a different title?");
                    String choice = console.nextLine().trim();
                    switch (choice) {
                        case "1":
                            // Aggiorneremo la traccia esistente
                            ans_TR = "1";
                            loop = false;
                            break;
                        case "2":
                            // Creeremo una nuova traccia -> chiediamo nuovo titolo
                            ans_TR = "2";
                            loop2 = true;
                            while (loop2) {
                                System.out.println("Please provide the NEW TITLE of the Track (current is \"" + title + "\"):");
                                String new_title = console.nextLine().trim();
                                if (!new_title.isEmpty() && !new_title.equalsIgnoreCase(title)) {
                                    title = new_title;
                                    loop2 = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please enter a different, valid title.");
                                }
                            }
                            loop = false;
                            break;
                        default:
                            System.out.println("INPUT ERROR - Please enter a valid option (1 or 2).");
                    }
                }
            }

            // --------------------------------------------------------
            // SE L'UTENTE HA SCELTO "2" => CREIAMO UNA NUOVA TRACCIA
            // --------------------------------------------------------
            if (ans_TR.equals("2")) {
                // Chiediamo i dettagli della nuova traccia
                String album;
                String genre;
                int year = 1900;

                // Album
                System.out.println("\nIs \"" + title + "\" part of an album? (If yes, provide the album title, otherwise leave blank)");
                album = console.nextLine().trim();
                if (album.isEmpty()) {
                    album = "SINGLE";
                }

                // Genre
                while (true) {
                    System.out.println("Please provide the GENRE of \"" + title + "\" (e.g. ROCK, POP, JAZZ...):");
                    genre = console.nextLine().trim().toUpperCase();
                    if (MusicGenres.ALLOWED_GENRES.contains(genre)) {
                        break;
                    } else {
                        System.out.println("INPUT ERROR - The genre \"" + genre + "\" is not allowed.\nAllowed: " + MusicGenres.ALLOWED_GENRES);
                    }
                }

                // Year
                while (true) {
                    try {
                        System.out.println("Please provide the YEAR of \"" + title + "\":");
                        year = Integer.parseInt(console.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        // year rimane 0
                    }
                    int current = Year.now().getValue();
                    if (year >= 1900 && year <= current) {
                        break;
                    } else {
                        System.out.println("INPUT ERROR - Please enter a valid year between 1900 and " + current);
                    }
                }

                // Raccogliamo i NOMI degli artisti
                List<String> artistNameList = new ArrayList<>();
                // Forziamo almeno 1 artista
                while (true) {
                    System.out.println("\nProvide at least one ARTIST for \"" + title + "\":");
                    String firstArtist = console.nextLine().trim();
                    if (!firstArtist.isEmpty()) {
                        artistNameList.add(firstArtist);
                        break;
                    } else {
                        System.out.println("INPUT ERROR - At least 1 artist is required.");
                    }
                }

                // Artisti addizionali
                boolean addMore = true;
                while (addMore) {
                    System.out.println("Add another artist? (leave blank to finish). Current list: " + artistNameList);
                    String another = console.nextLine().trim();
                    if (!another.isEmpty()) {
                        artistNameList.add(another);
                    } else {
                        addMore = false;
                    }
                }

                // ORA CONVERTIAMO I NOMI IN ID
                List<String> artistIdList = new ArrayList<>();
                for (int i = 0; i < artistNameList.size(); i++) {
                    String artistName = artistNameList.get(i);
                    String aId;
                    try {
                        aId = artistService.getArtistByName(artistName).getId();
                    } catch (IllegalArgumentException e) {
                        aId = "";
                    }

                    if (aId.isEmpty()) {
                        // Artista non esiste
                        System.out.println("\nWARNING: The artist \"" + artistName + "\" does not exist in the database.");
                        boolean loopAsk = true;
                        while (loopAsk) {
                            System.out.println("Do you want to add this artist now? (Y/N)");
                            String ans = console.nextLine().trim().toUpperCase();
                            switch (ans) {
                                case "Y":
                                case "YES":
                                    // Creiamo l'artista (interattivamente)
                                    updateArtist(artistName);
                                    // Ora dovrebbe esistere, quindi recuperiamo nuovamente l'ID
                                    aId = artistService.getArtistByName(artistName).getId();
                                    loopAsk = false;
                                    break;
                                case "N":
                                case "NO":
                                    // Rimuoviamo l'artista dalla lista
                                    artistNameList.remove(i);
                                    i--;
                                    loopAsk = false;
                                    break;
                                default:
                                    System.out.println("INPUT ERROR - Please type YES or NO.");
                            }
                        }
                    }
                    if (!aId.isEmpty()) {
                        artistIdList.add(aId);
                    }
                }

                // Infine, CREIAMO la traccia con la lista di ID
                trackService.createTrack("", year, genre, album, title, artistIdList);

                // Fine blocco "create track"
                update_TR_loop = false;
                choose = true;

            } else {
                // --------------------------------------------------------
                // SE L'UTENTE HA SCELTO "1" => AGGIORNIAMO UNA TRACCIA ESISTENTE
                // --------------------------------------------------------
                // Chiediamo il titolo della traccia da aggiornare
                // (anche se in teoria ce l'abbiamo già in `title`, se vuoi puoi confermare o farlo cambiare)
                boolean askTitle = true;
                while (askTitle) {
                    System.out.println("\nPlease provide again the TITLE of the Track to UPDATE (currently \"" + title + "\"): ");
                    String new_title = console.nextLine().trim();
                    if (!new_title.isEmpty() && !new_title.equalsIgnoreCase(title)) {
                        title = new_title;
                    }
                    askTitle = false; // usciamo comunque
                }

                // Recuperiamo i dati della traccia esistente
                String track_id2 = trackService.getTrackByTitle(title).getId();
                int oldYear = trackService.getYearByTrackId(track_id2);
                String oldGenre = trackService.getGenreByTrackId(track_id2);
                String oldAlbum = trackService.getAlbumTrackId(track_id2);

                // Chiediamo all'utente cosa modificare
                boolean editing = true;
                String genre = oldGenre;
                int year = oldYear;
                String album = oldAlbum;
                List<String> artistIdList = new ArrayList<>();

                while (editing) {
                    System.out.println("\nWhat do you want to edit?\n" +
                            "1 - Title: " + title + "\n" +
                            "2 - Genre: " + genre + "\n" +
                            "3 - Year: " + year + "\n" +
                            "4 - Album: " + album + "\n" +
                            "5 - Artist list\n" +
                            "X - Done editing");
                    String choice = console.nextLine().trim();

                    switch (choice) {
                        case "1":
                            System.out.println("Please provide a NEW title for the track (old: \"" + title + "\"). Leave empty to skip:");
                            String newTitle = console.nextLine().trim();
                            if (!newTitle.isEmpty() && !newTitle.equalsIgnoreCase(title)) {
                                title = newTitle;
                            }
                            break;

                        case "2":
                            while (true) {
                                System.out.println("Please provide a NEW GENRE (old: " + oldGenre + "). Leave empty to skip:");
                                String newGenre = console.nextLine().trim().toUpperCase();
                                if (newGenre.isEmpty()) {
                                    // skip
                                    break;
                                }
                                if (MusicGenres.ALLOWED_GENRES.contains(newGenre)) {
                                    genre = newGenre;
                                    break;
                                } else {
                                    System.out.println("Invalid genre. Allowed: " + MusicGenres.ALLOWED_GENRES);
                                }
                            }
                            break;

                        case "3":
                            while (true) {
                                System.out.println("Please provide a NEW YEAR (old: " + oldYear + "). Leave empty to skip:");
                                String sYear = console.nextLine().trim();
                                if (sYear.isEmpty()) {
                                    break; // skip
                                }
                                try {
                                    int parsed = Integer.parseInt(sYear);
                                    int current = Year.now().getValue();
                                    if (parsed >= 1900 && parsed <= current) {
                                        year = parsed;
                                        break;
                                    } else {
                                        System.out.println("Year must be between 1900 and " + current);
                                    }
                                } catch (NumberFormatException ex) {
                                    System.out.println("Not a valid number.");
                                }
                            }
                            break;

                        case "4":
                            System.out.println("Please provide a NEW ALBUM (empty => SINGLE, leave blank to skip):");
                            String sAlbum = console.nextLine().trim();
                            if (sAlbum.isEmpty()) {
                                sAlbum = "SINGLE";
                            }
                            if (!sAlbum.equals(oldAlbum)) {
                                album = sAlbum;
                            }
                            break;

                        case "5":
                            // Raccogliamo di nuovo i NOMI degli artisti da associare
                            List<String> artistNameList = new ArrayList<>();
                            System.out.println("Enter at least one ARTIST name (leave blank to finish).");
                            boolean collecting = true;
                            while (collecting) {
                                String anArtist = console.nextLine().trim();
                                if (!anArtist.isEmpty()) {
                                    artistNameList.add(anArtist);
                                } else {
                                    collecting = false;
                                }
                            }
                            // Ora convertiamo i nomi in ID
                            List<String> newArtistIdList = new ArrayList<>();
                            for (String artistName : artistNameList) {
                                String aId;
                                try {
                                    aId = artistService.getArtistByName(artistName).getId();
                                } catch (IllegalArgumentException e) {
                                    aId = "";
                                }
                                if (aId.isEmpty()) {
                                    System.out.println("WARNING: The artist \"" + artistName + "\" does not exist.");
                                    boolean askAgain = true;
                                    while (askAgain) {
                                        System.out.println("Do you want to add this artist now? (Y/N)");
                                        String c = console.nextLine().trim().toUpperCase();
                                        switch (c) {
                                            case "Y", "YES":
                                                // creiamo l'artista
                                                updateArtist(artistName);
                                                aId = artistService.getArtistByName(artistName).getId();
                                                askAgain = false;
                                                break;
                                            case "N", "NO":
                                                // skip
                                                askAgain = false;
                                                break;
                                            default:
                                                System.out.println("INPUT ERROR - type Y or N");
                                        }
                                    }
                                }
                                if (!aId.isEmpty()) {
                                    newArtistIdList.add(aId);
                                }
                            }
                            // Adesso avremo la lista di ID completa
                            artistIdList = newArtistIdList;
                            break;

                        case "X":
                        case "x":
                            editing = false;
                            break;

                        default:
                            System.out.println("Invalid choice. Choose 1..5 or X.");
                    }
                }

                // Ora aggiorniamo la traccia
                // Se l'utente non ha toccato la lista artisti, recuperiamo quella esistente
                if (artistIdList.isEmpty()) {
                    // usiamo gli ID attuali
                    artistIdList = trackService.getSingleTrackbyId(track_id2).getArtistIds();
                }

                Track updTrack = new Track(track_id2, year, genre, album, title, artistIdList);
                trackService.updateTrack(updTrack);

                update_TR_loop = false;
                choose = true;
            }

        } // end while (update_TR_loop)

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
