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
        System.out.println("Welcome to the Music Application");
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }
        System.out.println(" ");


        boolean main_loop = true;
        while (main_loop) {
            boolean loop = true;
            while(loop){
                System.out.println("Please choose what you would like to do\n 1 - Update the Music Application\n 2 - Consult Music Application \n E) for Exit");
                ans = input.next();
                switch(ans){
                    case "1":
                        loop = false;
                        main_loop = updateMusicMenu();
                    case "2":
                        loop = false;
                        main_loop = consultMusicMenu();
                    case "e","E":
                        loop = false;
                        main_loop = false;
                    default:
                        System.out.println("INPUT ERROR - Please enter a valid option");

                }
            }
        }


    }
    public static Boolean updateMusicMenu (){
        Scanner input_UM = new Scanner(System.in);
        String ans_UM = "";
        Boolean choose = false;
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }
        System.out.println("  Welcome to the Music Application - UPDATE Section  ");
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }
        System.out.println(" ");
        boolean update_MM_loop = true;
        while(update_MM_loop){
            System.out.println("Please choose what you would like to update\\n 1) Update one Track\\n 2) Update an Artist\n 0) Previous Menu\n E) for Exit");
            ans_UM = input_UM.next();
            switch(ans_UM){
                case "1":
                    update_MM_loop = updateTrack();
                    choose = update_MM_loop;
                case "2":
                    update_MM_loop = updateArtist("");
                    choose = update_MM_loop;
                case "0":
                    update_MM_loop = false;
                    choose = true;
                    break;
                case "e","E":
                    update_MM_loop = false;
                    return false;
                default:
                    System.out.println("INPUT ERROR - Please enter a valid option");
                    update_MM_loop = true;
            }

        }
        return choose;
    }

    public static Boolean updateTrack (String title) {
        Scanner input_TR = new Scanner(System.in);

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


        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }
        System.out.println("  Welcome to the Music Application - UPDATE TRACK Section ");
        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }

        boolean update_TR_loop = true;
        while (update_TR_loop) {
            ans_TR = "2";/*By Default we set ans_TR = 2 because is a variable used to mark Create a New Track*/

            /*TITLE - NOT ALLOWED EMPTY*/
            /*ASK TITLE if not received*/
            if (!title.isEmpty()) {
                loop = true;
                updateTR = false;
                while (loop) {
                    t_title = input_TR.next("Please provide the TITLE of the Track").trim();
                    if (!t_title.isEmpty()) {
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please enter a valid Name of Artist");
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
            String track_id = ts.getTrackByTitle(title).getId();//getTrackByTitle return a Track, adding .getId() I retrieve only the id
            ArtistService as = new ArtistService(trackdao,artistdao);


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
                    ans_TR = input_TR.next("Do you want to update or create a new track?\n 1 - Update\n 2 - Create");
                    switch(ans_TR){
                        case "1":
                            loop = false;
                        case "2":
                            boolean loop2 = true;
                            while (loop2) {
                                String new_title = input_TR.next("Please provide a new TITLE of the Track" + title).trim();
                                if (!new_title.isEmpty() && !new_title.equals(title)) {
                                    title = new_title;
                                    loop2 = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please enter a valid Title");
                                }
                            }
                            loop = false;
                        default:
                            System.out.println("INPUT ERROR - Please enter a valid option");
                    }
                }
            }


            /* CREATE A NEW TRUCK */
            if (ans_TR.equals("2")){

                /*ALBUM - IF EMPTY IS A SINGLE TRACK*/
                album = input_TR.next("Is " + title + " part of a music album?\n if YES - provide the ALBUM TITLE\n if NO - let empty");
                if (album.isEmpty()) {
                    album = "SINGLE";
                }

                /*GENRE - ALLOWED ONLY GENRE IN MUSICGENRES LIST*/
                loop = true;
                while (loop) {
                    genre = (input_TR.next("Please provide the GENRE of the Track").trim());
                    if (MusicGenres.ALLOWED_GENRES.contains(genre)) {
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please enter a valid Genre");
                        for (int i = 0; i < MusicGenres.ALLOWED_GENRES.size(); i++) {
                            System.out.println(MusicGenres.ALLOWED_GENRES.get(i));
                        }
                    }
                }

                /*YEAR - ALLOWED ONLY 1900 - today*/
                loop = true;
                while (loop) {
                    try {
                        System.out.print("Please provide the YEAR of the Track");
                        yyyy = input_TR.nextInt();
                    } catch (InputMismatchException ignored) {
                        yyyy = 0;
                    }

                    int year = Year.now().getValue();
                    if (yyyy >= 1900 && yyyy <= year) {
                        loop = false;
                    } else {
                        System.out.println("INPUT ERROR - Please enter a valid Year from 1900 to " + year);
                    }
                }

                /*ARTIST LIST - EMPTY to finish*/
                loop = true;
                while (loop) {
                    single_artist = input_TR.next("Please provide the ARTIST of the Track " + title);
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
                    System.out.println("please provide additional ARTIST of the Track " + title + "\n Artist List:");
                    for (int i = 0; i < artistlist.size(); i++) {
                        System.out.println(artistlist.get(i));
                    }
                    System.out.println("Let empty if there is no more Artist to add");
                    single_artist = input_TR.next();
                    if (!single_artist.isEmpty()) {
                        artistlist.add(single_artist);
                    } else {
                        loop = false;
                    }
                }



                /* VALIDATE ARTIST NAME IN LIST if EXIST or CREATE*/
                for (int i = 0; i < artistlist.size(); i++) {
                    single_artist = artistlist.get(i);
                    String artist_id = as.getArtistByName(single_artist).getId();
                    if (artist_id == null || artist_id.isEmpty()){
                        System.out.println("WARNING the Artist " + single_artist + " is not in the database");
                        loop = true;
                        while (loop) {
                            String ans = input_TR.next("Do you want to add this artist now?").toUpperCase().trim();
                            switch (ans) {
                                case "Y","YES":
                                    if (updateArtist(single_artist)) {
                                        loop = false;
                                    }
                                    else {
                                        System.out.println("ERROR CREATING NEW ARTIST");
                                    }

                                case "N","NO":
                                    artistlist.remove(i);
                                    loop = false;
                                default:
                                    System.out.println("INPUT ERROR - Please enter only YES/NO");
                            }
                        }
                    }

                }

                ts.createTrack(track_id,yyyy,genre,album,title,artistlist);


            }
            else {//(ans_TR.equals("1")
                loop = true;
                while (loop) {
                    String new_title = input_TR.next("Please provide the TITLE of the Track tu UPDATE").trim();
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
                    System.out.println("1 - Genre: " + genre);
                    System.out.println("2 - Year: " + yyyy);
                    System.out.println("3 - Album: " + album);
                    System.out.print("4 - Artist list: ");
                    for (int i = 0; i < ts.getArtistsByTrackId(track_id).size(); i++) {
                        String artist_id = ts.getArtistsByTrackId(track_id).get(i).getId();
                        System.out.print(artistdao.getArtistById(artist_id).getName() + ", ");
                    }

                    ans_TR = input_TR.next();

                    switch (ans_TR) {
                        case "1":
                            /*TITLE*/
                            boolean loop2 = true;
                            while (loop2) {
                                String new_title = input_TR.next("Please provide a new TITLE of the Track" + title).trim();
                                if (!new_title.isEmpty() && !new_title.equals(title)) {
                                    title = new_title;
                                    loop2 = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please enter a NEW valid Title");
                                }
                            }
                            loop = false;

                        case "2":
                            /*GENRE - ALLOWED ONLY GENRE IN MUSICGENRES LIST*/
                            loop2 = true;
                            while (loop2) {
                                String new_genre = input_TR.next("Provide the new Genre");
                                if (MusicGenres.ALLOWED_GENRES.contains(new_genre) && !genre.equals(new_genre)) {
                                    genre = new_genre;
                                    loop2 = false;
                                } else {
                                    System.out.println("INPUT ERROR - Please enter a NEW valid Genre");
                                    for (int i = 0; i < MusicGenres.ALLOWED_GENRES.size(); i++) {
                                        System.out.println(MusicGenres.ALLOWED_GENRES.get(i));
                                    }
                                }
                            }
                            loop = false;

                        case "3":
                            /*YEAR - ALLOWED ONLY 1900 - today*/
                            loop2 = true;
                            while (loop2) {
                                int new_yyyy = 1900;
                                try {
                                    System.out.print("Please provide the new YEAR");
                                    new_yyyy = input_TR.nextInt();
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

                        case "4":
                            /*ALBUM - IF EMPTY IS A SINGLE TRACK*/
                            loop2 = true;
                            while (loop2) {
                                String new_album = input_TR.next("Provide the new ALBUM TITLE");
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

                        case "5":
                            /*ARTIST LIST - EMPTY to finish*/
                            loop2 = true;
                            while (loop2) {
                                single_artist = input_TR.next("Please provide the ARTIST of the Track " + title);
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
                                System.out.println("please provide additional ARTIST of the Track " + title);
                                for (int i = 0; i < artistlist.size(); i++) {
                                    System.out.println(artistlist.get(i));
                                }
                                System.out.println("Let empty if there is no more Artist to add");
                                single_artist = input_TR.next();
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
                                    Boolean loop2 = true;
                                    while (loop2) {
                                        String ans = input_TR.next("Do you want to add this artist now?").toUpperCase().trim();
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
                        default:
                            System.out.println("INPUT ERROR - Please enter a valid option");
                    }

                }
                Track upd_track = new Track(track_id, yyyy, genre, album, title, artistlist);
                ts.updateTrack(upd_track);
            }

            if (updateTR == false){
                loop=true;
                while (loop) {
                    String ans = input_TR.next("Jobs Done\n 1 - Update new Track \n E - for Exit\n 0 - Previous menu");
                    switch (ans_TR){
                        case "1":
                            loop = false;
                        case "E":
                            loop = false;
                            update_TR_loop = false;
                            choose = false;
                        case "0":
                            loop = false;
                            update_TR_loop = false;
                            choose = true;
                        default:
                            System.out.println("INPUT ERROR - Please enter a valid option");
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
        System.out.println("  Welcome to the Music Application - UPDATE ARTIST Section ");
        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }
        boolean update_AR_loop = true;
        while (update_AR_loop) {
            ans_AR = "2";/*By Default we set ans_AR = 2 because is a variable used to mark Create a New Artist*/
            /*ASK ARTIST NAME if not received*/
            if (!name.isEmpty()) {
                loop = true;
                updateAR = false;
                while (loop) {
                    a_name = input_AR.next("Please provide a NAME of Artist").trim();
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
            String artist_id = as.getArtistByName(a_name).getId();//getArtistByName return an Artist, adding .getId() I retrieve only the id


            /*GENRE - ALLOWED ONLY GENRE IN MUSICGENRES LIST*/
            loop = true;
            while (loop) {
                genre = (input_AR.next("Please provide the GENRE of the Artist").trim());
                if (MusicGenres.ALLOWED_GENRES.contains(genre)) {
                    loop = false;
                } else {
                    System.out.println("INPUT ERROR - Please enter a valid Genre");
                    for (int i = 0; i < MusicGenres.ALLOWED_GENRES.size(); i++) {
                        System.out.println(MusicGenres.ALLOWED_GENRES.get(i));
                    }
                }
            }


            /*TRACK LIST - EMPTY to finish*/
            loop = true;
            while (loop) {
                single_track = input_AR.next("Please provide the TRACK of the Artist " + name);
                /* 1st Artist cannot be empty*/
                if (!single_track.isEmpty()) {
                    tracklist.add(single_track);
                    loop = false;
                } else {
                    System.out.println("INPUT ERROR - Please provide at least 1 Artist name");
                }
            }

            loop = true;
            while (loop) {
                System.out.println("please provide additional TRACK of the Artist " + name + "\n Track List:");
                for (int i = 0; i < tracklist.size(); i++) {
                    System.out.println(tracklist.get(i));
                }
                System.out.println("Let empty if there is no more Artist to add");
                single_track = input_AR.next();
                if (!single_track.isEmpty()) {
                    tracklist.add(single_track);
                } else {
                    loop = false;
                }
            }

            /* VALIDATE TRACK NAME IN LIST if EXIST or CREATE*/
            for (int i = 0; i < tracklist.size(); i++) {
                single_track = tracklist.get(i);
                String track_id = as.getArtistByName(single_track).getId();
                if (track_id == null || track_id.isEmpty()){
                    System.out.println("WARNING the Track " + single_track + " is not in the database");
                    loop = true;
                    while (loop) {
                        String ans = input_AR.next("Do you want to add this track now?").toUpperCase().trim();
                        switch (ans) {
                            case "Y","YES":
                                if (updateTrack(single_track)) {
                                    loop = false;
                                }
                                else {
                                    System.out.println("ERROR CREATING NEW TRACK");
                                }

                            case "N","NO":
                                tracklist.remove(i);
                                loop = false;
                            default:
                                System.out.println("INPUT ERROR - Please enter only YES/NO");
                        }
                    }
                }

            }

            Artist upd_artist = new Artist(artist_id,name,genre, tracklist);
            as.updateArtist(upd_artist);

            if (updateAR == false){
                loop=true;
                while (loop) {
                    String ans = input_AR.next("Jobs Done\n 1 - Update new ARTIST \n E - for Exit\n 0 - Previous menu");
                    switch (ans_AR){
                        case "1":
                            loop = false;
                        case "E":
                            loop = false;
                            update_AR_loop = false;
                            choose = false;
                        case "0":
                            loop = false;
                            update_AR_loop = false;
                            choose = true;
                        default:
                            System.out.println("INPUT ERROR - Please enter a valid option");
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

    public static Boolean consultMusicMenu(){


        Scanner input_CM = new Scanner(System.in);
        String ans_CM = "";
        Boolean choose = false;
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }
        System.out.println("  Welcome to the Music Application - CONSULT Section  ");
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }



        return choose;
    }


}

