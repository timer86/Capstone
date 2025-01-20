// fc 21/01/2025 00:11 am

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

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.*;
import java.util.InputMismatchException;

public class MusicApplication {
    public static void main(String[] args) {
        String ans="";
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
                System.out.println("\n\n Please choose what you would like to do\n 1) - Update the Music Application\n 2) - Consult Music Application \n E) for Exit");
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
        String ans_UM = "";
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


        String ans_TR = "";
        String album = "";
        String t_title = "";
        String genre = "";
        int yyyy = 1900;
        boolean choose = false;
        boolean loop = true;
        boolean updateTR = false;

        List<String> artistlist = new ArrayList<String>();
        String single_artist = "";

        System.out.println(" ");
        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }
        System.out.print("  Welcome to the Music Application - UPDATE TRACK Section ");
        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }
        System.out.println(" ");
        boolean update_TR_loop = true;
        while (update_TR_loop) {
            ans_TR = "2";/*By Default we set ans_TR = 2 because is a variable used to mark Create a New Track*/

            /*TITLE - NOT ALLOWED EMPTY*/
            /*ASK TITLE if not received*/
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

            TrackDAO trackdao = new inMemoryTrackDAO();
            ArtistDAO artistdao = new inMemoryArtistDAO();
            TrackService ts = new TrackService(trackdao, artistdao);
            ArtistService as = new ArtistService(trackdao,artistdao);
            String track_id = "";
            try {
                track_id = ts.getTrackByTitle(title).getId();//getTrackByTitle return a Track, adding .getId() I retrieve only the id
            }
            catch(IllegalArgumentException e){
                track_id = "";
            }
            if (track_id != null && !track_id.isEmpty()) {
                loop = true;
                while (loop) {
                    Track track = ts.getSingleTrackbyId(track_id);

                    System.out.println("The Song " + title + " already exist\n");
                    System.out.println("Genre: " + track.getGenre());
                    System.out.println("Year: " + track.getYear());
                    System.out.println("Album: " + track.getAlbum());
                    System.out.print("Artist list: ");
                    List<String> artistidlist = new ArrayList<String>();
                    artistidlist = track.getArtistIds();

                    for (int i = 0; i < artistidlist.size(); i++) {
                        Artist artist = as.getArtistDetailsById(artistidlist.get(i));
                        System.out.print(artist.getName()+", ");
                    }
                    Scanner input_TR = new Scanner(System.in);
                    System.out.println("Do you want to update or create a new track?\n 1 - Update\n 2 - Create");
                    ans_TR = input_TR.nextLine().trim();
                    switch(ans_TR){
                        case "1":
                            loop = false;
                            break;
                        case "2":
                            boolean loop2 = true;
                            while (loop2) {
                                System.out.println("Please provide the TITLE of the Track " + title);
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


            /* CREATE A NEW TRUCK */
            if (ans_TR.equals("2")){

                /*ALBUM - IF EMPTY IS A SINGLE TRACK*/
                Scanner input_Album = new Scanner(System.in);
                System.out.println("Is " + title + " part of a music album?\n if YES - provide the ALBUM TITLE\n if NO - let empty");
                album = input_Album.nextLine().trim();
                System.out.println(" ");

                if (album.isEmpty()) {
                    album = "SINGLE";
                }


                /*GENRE - ALLOWED ONLY GENRE IN MUSICGENRES LIST*/
                loop = true;
                while (loop) {
                    Scanner input_Genre = new Scanner(System.in);
                    System.out.println("Please provide the GENRE of the Track " + title);
                    genre = input_Genre.nextLine().trim().toUpperCase();
                    if (MusicGenres.ALLOWED_GENRES.contains(genre)) {
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

                /*YEAR - ALLOWED ONLY 1900 - today*/
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

                /*ARTIST LIST - EMPTY to finish*/
                loop = true;
                while (loop) {
                    Scanner input_Artist = new Scanner(System.in);
                    System.out.println(" ");
                    System.out.println("Please provide the ARTIST of the Track " + title);
                    single_artist = input_Artist.nextLine().trim();
                    /* 1st Artist cannot be empty*/
                    if (!single_artist.isEmpty()) {
                        artistlist.add(single_artist);
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please provide at least 1 Artist name");
                    }
                }

                loop = true;
                while (loop) {
                    Scanner input_Additional_Artist = new Scanner(System.in);
                    System.out.println("please provide additional ARTIST of the Track " + title + "\n Artist List:");
                    for (String s : artistlist) {
                        System.out.print(s + ", ");
                    }
                    System.out.println("Let empty if there is no more Artist to add");
                    single_artist = input_Additional_Artist.nextLine().trim();
                    if (!single_artist.isEmpty()) {
                        artistlist.add(single_artist);
                    } else {
                        loop = false;
                    }
                }



                /* VALIDATE ARTIST NAME IN LIST if EXIST or CREATE*/
                for (int i = 0; i < artistlist.size(); i++) {
                    single_artist = artistlist.get(i);
                    String artist_id = "";
                    try{
                        artist_id = as.getArtistByName(single_artist).getId();
                    }
                    catch(IllegalArgumentException e){
                        artist_id = "";
                    }

                    if (artist_id == null || artist_id.isEmpty()){
                        System.out.println(" ");
                        System.out.println("WARNING the Artist " + single_artist + " is not in the database");
                        loop = true;
                        while (loop) {
                            Scanner input_Artist_list = new Scanner(System.in);
                            System.out.println("Do you want to add this artist now?");
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
                                    System.out.println("INPUT ERROR - Please enter only YES/NO");
                                    break;
                            }
                        }
                    }

                }

                ts.createTrack(track_id,yyyy,genre,album,title,artistlist);


            }
            else {//(ans_TR.equals("1")
                loop = true;
                while (loop) {
                    Scanner input_Tytle = new Scanner(System.in);
                    System.out.println("Please provide the TITLE of the Track tu UPDATE");
                    String new_title = input_Tytle.nextLine().trim();

                    if (!new_title.isEmpty() && !new_title.equals(title)) {
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please enter a valid Title");
                    }
                }

                loop = true;
                while (loop) {

                    track_id = ts.getTrackByTitle(title).getId();
                    yyyy = ts.getYearByTrackId(track_id);
                    genre = ts.getGenreByTrackId(track_id);
                    album = ts.getAlbumTrackId(track_id);

                    System.out.println("What do you want to edit?");
                    System.out.println("1 - Title: " + title);
                    System.out.println("2 - Genre: " + genre);
                    System.out.println("3 - Year: " + yyyy);
                    System.out.println("4 - Album: " + album);
                    System.out.print("5 - Artist list: ");
                    for (int i = 0; i < ts.getArtistsByTrackId(track_id).size(); i++) {
                        String artist_id = ts.getArtistsByTrackId(track_id).get(i).getId();
                        System.out.print(artistdao.getArtistById(artist_id).getName() + ", ");
                    }
                    Scanner input_TR = new Scanner(System.in);
                    ans_TR = input_TR.nextLine().trim();

                    switch (ans_TR) {
                        case "1":
                            /*TITLE*/
                            boolean loop2 = true;
                            while (loop2) {
                                System.out.println("Please provide a new TITLE of the Track" + title);
                                String new_title = input_TR.nextLine().trim();
                                if (!new_title.isEmpty() && !new_title.equals(title)) {
                                    title = new_title;
                                    loop2 = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please enter a NEW valid Title");
                                }
                            }
                            loop = false;
                            break;
                        case "2":
                            /*GENRE - ALLOWED ONLY GENRE IN MUSICGENRES LIST*/
                            loop2 = true;
                            while (loop2) {
                                Scanner input_Genre = new Scanner(System.in);
                                System.out.println("Please provide the NEW GENRE of the Track " + title);
                                genre = input_Genre.nextLine().trim().toUpperCase();
                                if (MusicGenres.ALLOWED_GENRES.contains(genre)) {
                                    loop2 = false;
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
                            loop = false;
                            break;
                        case "3":
                            /*YEAR - ALLOWED ONLY 1900 - today*/
                            loop2 = true;
                            while (loop2) {
                                int new_yyyy = 1900;
                                try {
                                    System.out.print("Please provide the new YEAR");
                                    Scanner input_Year = new Scanner(System.in);
                                    new_yyyy = input_Year.nextInt();
                                } catch (InputMismatchException ignored) {
                                    new_yyyy = 0;
                                }

                                int year = Year.now().getValue();
                                if ((new_yyyy >= 1990) && (new_yyyy <= year) && !(new_yyyy == yyyy)) {
                                    loop2 = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please enter a NEW valid Year from 1900 to " + year);
                                }
                            }
                            loop = false;
                            break;
                        case "4":
                            /*ALBUM - IF EMPTY IS A SINGLE TRACK*/
                            loop2 = true;
                            while (loop2) {
                                Scanner input_Album = new Scanner(System.in);
                                System.out.println("Provide the new ALBUM TITLE");
                                String new_album = input_Album.nextLine();
                                if (new_album.isEmpty()) {
                                    new_album = "SINGLE";
                                }
                                if (!(new_album.equals(album))) {
                                    loop2 = false;
                                }
                                else{
                                    System.out.println("INPUT ERROR - Please enter a valid NEW ALBUM TITLE");
                                }
                            }
                            loop = false;
                            break;
                        case "5":
                            /*ARTIST LIST - EMPTY to finish*/
                            loop2 = true;
                            while (loop2) {
                                Scanner input_Artist = new Scanner(System.in);
                                System.out.println("Please provide the ARTIST of the Track " + title);
                                single_artist = input_Artist.next();
                                /* 1st Artist cannot be empty*/
                                if (!single_artist.isEmpty()) {
                                    artistlist.add(single_artist);
                                    loop2 = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please provide at least 1 Artist name");
                                }
                            }

                            loop2 = true;

                            while (loop2) {
                                System.out.println("\nplease provide additional ARTIST of the Track " + title);
                                for (int i = 0; i < artistlist.size(); i++) {
                                    System.out.println(artistlist.get(i));
                                }
                                System.out.println(" ");
                                System.out.println("Let empty if there is no more Artist to add");
                                System.out.println(" ");
                                Scanner input_Artist_list = new Scanner(System.in);
                                single_artist = input_Artist_list.nextLine().trim();
                                if (!single_artist.isEmpty()) {
                                    artistlist.add(single_artist);
                                } else {
                                    loop2 = false;
                                }
                            }


                            Artist artist = new Artist("", "", "", List.of());
                            /* VALIDATE ARTIST NAME IN LIST if EXIST or CREATE*/
                            for (int i = 0; i < artistlist.size(); i++) {
                                single_artist = artistlist.get(i);
                                String id = artist.getIdByName(single_artist);
                                if (id.isEmpty()) {
                                    System.out.println("WARNING the Artist " + single_artist + " is not in the database");
                                    loop2 = true;
                                    while (loop2) {
                                        Scanner input = new Scanner(System.in);
                                        System.out.println("Do you want to add this artist now?");
                                        String ans = input.nextLine().toUpperCase().trim();
                                        switch (ans) {
                                            case "Y", "YES":
                                                loop = updateArtist(single_artist);
                                            case "N", "NO":
                                                artistlist.remove(i);
                                                loop = false;
                                            default:
                                                System.out.println("INPUT ERROR - Please enter only YES/NO");
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
                Track upd_track = new Track(track_id, yyyy, genre, album, title, artistlist);
                ts.updateTrack(upd_track);
            }

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
            }
            else{
                update_TR_loop = false;
                choose = true;
            }
        }

        return choose;
    }




    public static Boolean updateArtist (String name){

        Scanner input_AR = new Scanner(System.in);

        String ans_AR = "";
        String a_name = "";
        String genre = "";
        boolean choose = false;
        boolean loop = false;
        boolean updateAR = false;

        List<String> tracklist = new ArrayList<String>();
        String single_track = "";


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
            ans_AR = "2";/*By Default we set ans_AR = 2 because is a variable used to mark Create a New Artist*/
            /*ASK ARTIST NAME if not received*/
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


            TrackDAO trackdao = new inMemoryTrackDAO();
            ArtistDAO artistdao = new inMemoryArtistDAO();
            TrackService ts = new TrackService(trackdao, artistdao);
            ArtistService as = new ArtistService(trackdao, artistdao);
            String artist_id = "";
            try{
                artist_id = as.getArtistByName(name).getId();//getArtistByName return an Artist, adding .getId() I retrieve only the id
            }  catch(IllegalArgumentException e){
                artist_id = "";
            }


            /*GENRE - ALLOWED ONLY GENRE IN MUSICGENRES LIST*/
            loop = true;
            while (loop) {
                Scanner input_Genre = new Scanner(System.in);
                System.out.println("Please provide the GENRE of the Artist " + name);
                genre = input_Genre.next().trim().toUpperCase();
                if (MusicGenres.ALLOWED_GENRES.contains(genre)) {
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



            /*TRACK LIST - EMPTY to finish*/
            /*ARTIST LIST - EMPTY to finish*/
            loop = true;
            while (loop) {
                Scanner input_Track = new Scanner(System.in);
                System.out.println(" ");
                System.out.println("Please provide the TRACK of the Artist " + name);
                single_track = input_Track.nextLine();
                /* 1st Artist cannot be empty*/
                if (!single_track.isEmpty()) {
                    tracklist.add(single_track);
                    loop = false;
                } else {
                    System.out.println("INPUT ERROR - Please provide at least 1 Track name");
                }
            }

            loop = true;
            while (loop) {
                Scanner input_Additional_Track = new Scanner(System.in);
                System.out.println("please provide additional TRACK of the Artist " + name + "\n Track List:");
                for (String t : tracklist) {
                    System.out.print(t + ", ");
                }
                System.out.println("Let empty if there is no more Track to add");
                single_track = input_Additional_Track.nextLine();
                if (!single_track.isEmpty()) {
                    tracklist.add(single_track);
                } else {
                    loop = false;
                }
            }



            /* VALIDATE ARTIST NAME IN LIST if EXIST or CREATE*/
            for (int i = 0; i < tracklist.size(); i++) {
                single_track = tracklist.get(i);
                String track_id = "";
                try{
                    track_id = ts.getTrackByTitle(single_track).getId();
                }
                catch(IllegalArgumentException e){
                    track_id = "";
                }

                if (track_id == null || track_id.isEmpty()){
                    System.out.println(" ");
                    System.out.println("WARNING the Track " + single_track + " is not in the database");
                    loop = true;
                    while (loop) {
                        Scanner input_Track_list = new Scanner(System.in);
                        System.out.println("Do you want to add this artist now?");
                        String ans = input_Track_list.next().toUpperCase().trim();
                        switch (ans) {
                            case "Y","YES":
                                if (updateTrack(single_track)) {
                                    loop = false;
                                }
                                else {
                                    System.out.println("ERROR CREATING NEW TRACK");
                                }
                                break;
                            case "N","NO":
                                tracklist.remove(i);
                                loop = false;
                                break;
                            default:
                                System.out.println("INPUT ERROR - Please enter only YES/NO");
                                break;
                        }
                    }
                }

            }

            Artist upd_artist = new Artist(artist_id,name,genre, tracklist);
            as.updateArtist(upd_artist);

            if (!updateAR){
                loop=true;
                while (loop) {
                    Scanner input = new Scanner(System.in);
                    System.out.println("Jobs Done\n 1 - Update new ARTIST \n E - for Exit\n 0 - Previous menu");
                    String ans = input.nextLine().trim();
                    switch (ans){
                        case "1":
                            loop = false;
                            break;
                        case "E":
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
            }
            else {
                update_AR_loop = false;
                choose = true;
            }
        }
        return choose;
    }


    public static boolean consultMusicMenu() {
        String choose = Menu();
        String Separato;
        Separato = "-".repeat(50);
        boolean loop = true;
        if (choose.equals("Artist Reporting Tool")){
            Artist_Framework();
        } else if (choose.equals("Track Reporting Tool")){
            Track_Framework();
        } else if (choose.equals("Exit")){
            System.out.println(Separato);
            System.out.println("Exiting.. going on Main Menu \n \n \n");
            System.out.println(Separato);
            String[] arguments = {};
             main(arguments);
        }

        return loop;
    }


    public static String Menu(){
        String Separato;
        String choose = "";
        Separato = "-".repeat(50);
        Scanner input_CM = new Scanner(System.in);
        String ans_CM = "";
        System.out.println(Separato);
        System.out.print("  Welcome to the Music Application - CONSULT Section \n ");
        System.out.println(Separato);
        System.out.println("Select: \n 1) For Artist Reporting Tool \n 2) For Track Reporting Tool \n E) for Exit");
        String ans = input_CM.nextLine().trim();
        boolean jump = false;
        switch (ans){
            case "1":
                jump = true;
                choose = "Artist Reporting Tool";
                break;
            case "2":
                jump = true;
                choose = "Track Reporting Tool";
                break;
            case "E", "e":
                jump = true;
                choose = "Exit";
                break;
            default:
                System.out.println("INPUT ERROR - Please enter a valid option");
                choose = "nothing";
                break;

        }

        return choose;
    }


    public static void Artist_Framework() {
        ArtistDAO artistDAO = new inMemoryArtistDAO();
        TrackDAO trackDAO = new inMemoryTrackDAO();
        ArtistService artistService = new ArtistService(trackDAO, artistDAO);
        Scanner input = new Scanner(System.in);

        String separato = "-".repeat(50);
        System.out.println(separato);
        System.out.println("  Artist Reporting Tool  ");
        System.out.println(separato);
        System.out.println("Select:");
        System.out.println(" 1) Get All Artists");
        System.out.println(" 2) Get Tracks By Artist ID");
        System.out.println(" 3) Get Tracks By Artist Name");
        System.out.println(" 4) Get Artists By Genre");
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
                        List<Artist> artistsByGenre = artistService.getArtistsByGenre(genre);
                        System.out.println("Artists in genre " + genre + ":");
                        for (Artist artist : artistsByGenre) {
                            System.out.println("ID: " + artist.getId() + ", Name: " + artist.getName());
                        }
                    }
                }
                break;

            case "E", "e":
                System.out.println("Returning to the main menu...");
                break;

            default:
                System.out.println("INPUT ERROR - Please enter a valid option.");
                break;
        }
    }


    public static void Track_Framework() {
        TrackDAO trackDAO = new inMemoryTrackDAO();
        ArtistDAO artistDAO = new inMemoryArtistDAO();
        TrackService trackService = new TrackService(trackDAO, artistDAO);
        Scanner input = new Scanner(System.in);

        String separato = "-".repeat(50);
        System.out.println(separato);
        System.out.println("  Track Reporting Tool  ");
        System.out.println(separato);
        System.out.println("Select:");
        System.out.println(" 1) Get All Tracks");
        System.out.println(" 2) Get Tracks By Artist ID");
        System.out.println(" 3) Get Tracks By Artist Name");
        System.out.println(" 4) Get Tracks By Title");
        System.out.println(" 5) Get Tracks By Genre");
        System.out.println(" 6) Get Tracks By Year");
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
                        System.out.println("Title: " + track.getTitle() + ", Album: " + track.getAlbum() + ", Year: " + track.getYear());
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
                    Artist artist = new ArtistService(trackDAO, artistDAO).getArtistByName(artistName);
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

            case "E", "e":
                System.out.println("Returning to the main menu...");
                break;

            default:
                System.out.println("INPUT ERROR - Please enter a valid option.");
                break;
        }
    }


}

