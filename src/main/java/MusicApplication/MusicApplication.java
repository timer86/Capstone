/* AG 20/01/2025 15:10*/

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
        System.out.print(" Welcome to the Music Application ");
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }
        System.out.println(" ");


        boolean main_loop = true;
        while (main_loop) {
            boolean loop = true;
            while(loop){
                System.out.println("Please choose what you would like to do\n 1) - Update the Music Application\n 2) - Consult Music Application \n E) for Exit");
                ans = input.next().trim();
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
            ans_UM = input_UM.next();
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
                    t_title = input_Title.next().trim();
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
                    ans_TR = input_TR.next().trim();
                    switch(ans_TR){
                        case "1":
                            loop = false;
                            break;
                        case "2":
                            boolean loop2 = true;
                            while (loop2) {
                                System.out.println("Please provide the TITLE of the Track " + title);
                                String new_title = input_TR.next().trim();
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
                    single_artist = input_Artist.nextLine();
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
                    single_artist = input_Additional_Artist.nextLine();
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
                            String ans = input_Artist_list.next().toUpperCase().trim();
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
                    String new_title = input_Tytle.next("Please provide the TITLE of the Track tu UPDATE").trim();
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
                    ans_TR = input_TR.nextLine();

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
                            break;
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
                            break;
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
                            break;
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
                            break;
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
                                System.out.println("\nplease provide additional ARTIST of the Track " + title);
                                for (int i = 0; i < artistlist.size(); i++) {
                                    System.out.println(artistlist.get(i));
                                }
                                System.out.println(" ");
                                System.out.println("Let empty if there is no more Artist to add");
                                System.out.println(" ");
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
                                    loop2 = true;
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
            loop = true;
            while (loop) {
                Scanner input_Track = new Scanner(System.in);
                System.out.println("Please provide the TRACK of the Artist " + name);
                single_track = input_Track.nextLine();
                System.out.println("");
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
                Scanner input_Track_list = new Scanner(System.in);
                System.out.println("please provide additional TRACK of the Artist " + name + "\n Track List:");
                for (String s : tracklist) {
                    System.out.println(s);
                }
                System.out.println("Let empty if there is no more Artist to add");
                single_track = input_Track_list.next();
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
                        Scanner input = new Scanner(System.in);
                        System.out.println("Do you want to add this track now?\n Y - N");
                        String ans = input.nextLine().toUpperCase().trim();
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
                    String ans = input.next("Jobs Done\n 1 - Update new ARTIST \n E - for Exit\n 0 - Previous menu");
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


    public static Boolean consultMusicMenu(){


        Scanner input_CM = new Scanner(System.in);
        String ans_CM = "";
        Boolean choose = false;
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }
        System.out.print("  Welcome to the Music Application - CONSULT Section  ");
        for(int i=1 ; i<=10 ; i++){
            System.out.print("*");
        }



        return choose;
    }


}

