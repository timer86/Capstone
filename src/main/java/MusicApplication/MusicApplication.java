package MusicApplication;
import Track.MusicGenres;
import Artist.Artist;
import Track.Track;

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
                        main_loop = consultMusicMenu()
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
                    update_MM_loop = updateArtist();
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

    public static Boolean updateTrack () {
        Scanner input_TR = new Scanner(System.in);

        String ans_TR = "";
        String album = "";
        String title = "";
        String genre = "";
        int yyyy = 1900;
        boolean choose = false;

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
            boolean loop = true;
            while (loop) {
                title = input_TR.next("Please provide the TITLE of the Track");
                if (!title.isEmpty()) {
                    loop = false;
                } else {
                    System.out.println("INPUT ERROR - Please enter a valid Title");
                }
            }

            Track track = new Track("","","","","");
            String track_id = track.getIdByTitle(title);

            if(!track_id.isEmpty()){
                boolean loop = true;
                while (loop) {
                    System.out.println("The Song " + title + " already exist\n");
                    System.out.println("Genre: " + track.getGenre(track_id));
                    System.out.println("Year: " + track.getYear(track_id));
                    System.out.println("Album: " + track.getAlbum(track_id));
                    System.out.print("Artist list: ");
                    List<String> artistidlist = new ArrayList<String>();
                    artistidlist = track.getArtistIds(track_id);
                    for (int i = 0; i < artistidlist.size(); i++) {
                        Artist artist = new Artist("","","");
                        System.out.println(artist.getNameById(artistidlist(i))+", ");
                    }
                    ans_TR = input_TR.next("Do you want to update or create a new track?\n 1 - Update\n 2 - Create");
                    switch(ans_TR){
                        case "1","2":
                            loop = false;
                        default:
                            System.out.println("INPUT ERROR - Please enter a valid option");
                    }
                }
            }


            /* CREATE A NEW TRUCK */
            if (ans_TR=="2"){


                /*ALBUM - IF EMPTY IS A SINGLE TRACK*/
                album = input_TR.next("Is " + title + " part of a music album?\n if YES - provide the ALBUM TITLE\n if NO - let empty");
                if (album.isEmpty()) {
                    album = "SINGLE";
                }

                /*GENRE - ALLOWED ONLY GENRE IN MUSICGENRES LIST*/
                loop = true;
                while (loop) {
                    genre = input_TR.next("Please provide the GENRE of the Track");
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
                    if (yyyy >= 1990 && yyyy <= year) {
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
                    System.out.println("please provide additional ARTIST of the Track " + title);
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


                Artist artist = new Artist("","","");
                /* VALIDATE ARTIST NAME IN LIST if EXIST or CREATE*/
                for (int i = 0; i < artistlist.size(); i++) {
                    single_artist = artistlist.get(i);
                    String id = artist.getIdByName(single_artist);
                    if (id.isEmpty()) {
                        System.out.println("WARNING the Artist " + single_artist + " is not in the database");
                        loop = true;
                        while (loop) {
                            String ans = input_TR.next("Do you want to add this artist now?").toUpperCase().trim();
                            switch (ans) {
                                case "Y","YES":
                                    creare funzione crea artista;
                                    break;
                                case "N","NO":
                                    artistlist.remove(i);
                                    loop = false;
                                default:
                                    System.out.println("INPUT ERROR - Please enter only YES/NO");
                            }
                        }
                    }

                }
            }
            else{
                System.out.println("What do you want to edit?");
                System.out.println("1 - Genre: " + track.getGenre(track_id));
                System.out.println("2 -  Year: " + track.getYear(track_id));
                System.out.println("3 -  Album: " + track.getAlbum(track_id));
                System.out.print("4 -  Artist list: ");
                for (int i = 0; i < track.getArtistIds(track_id).size(); i++) {
                    Artist artist = new Artist("","","");
                    System.out.print(artist.getNameById(track.getArtistIds(i)+", ");
                }
                ans_TR=input_TR.next();
                switch (ans_TR){
                    case "1":
                        /*GENRE - ALLOWED ONLY GENRE IN MUSICGENRES LIST*/
                        loop = true;
                        while (loop) {
                            genre = input_TR.next("Provide the new Genre");
                            if (MusicGenres.ALLOWED_GENRES.contains(genre)) {
                                loop = false;
                            } else {
                                System.out.println("INPUT ERROR - Please enter a valid Genre");
                                for (int i = 0; i < MusicGenres.ALLOWED_GENRES.size(); i++) {
                                    System.out.println(MusicGenres.ALLOWED_GENRES.get(i));
                                }
                            }
                        }
                        track.setGenre(genre);
                    case "2":
                        /*YEAR - ALLOWED ONLY 1900 - today*/
                        loop = true;
                        while (loop) {
                            try {
                                System.out.print("Please provide the new YEAR");
                                yyyy = input_TR.nextInt();
                            } catch (InputMismatchException ignored) {
                                yyyy = 0;
                            }

                            int year = Year.now().getValue();
                            if (yyyy >= 1990 && yyyy <= year) {
                                loop = false;
                            } else {
                                System.out.println("INPUT ERROR - Please enter a valid Year from 1900 to " + year);
                            }
                        }
                        track.setYear(yyyy);
                    case "3":
                        /*ALBUM - IF EMPTY IS A SINGLE TRACK*/
                        album = input_TR.next("Provide the new ALBUM TITLE");
                        if (album.isEmpty()) {
                            album = "SINGLE";
                        }
                        track.setAlbum(album);
                    case "4":
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
                            System.out.println("please provide additional ARTIST of the Track " + title);
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


                        Artist artist = new Artist("","","");
                        /* VALIDATE ARTIST NAME IN LIST if EXIST or CREATE*/
                        for (int i = 0; i < artistlist.size(); i++) {
                            single_artist = artistlist.get(i);
                            String id = artist.getIdByName(single_artist);
                            if (id.isEmpty()) {
                                System.out.println("WARNING the Artist " + single_artist + " is not in the database");
                                loop = true;
                                while (loop) {
                                    String ans = input_TR.next("Do you want to add this artist now?").toUpperCase().trim();
                                    switch (ans) {
                                        case "Y","YES":
                                            creare funzione crea artista;
                                            break;
                                        case "N","NO":
                                            artistlist.remove(i);
                                            loop = false;
                                        default:
                                            System.out.println("INPUT ERROR - Please enter only YES/NO");
                                    }
                                }
                            }

                        }
                    default:
                        System.out.println("INPUT ERROR - Please enter a valid option");
                        break;
                }

            }


            Boolean loop=true;
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

        return choose;
    }

    public static Boolean updateArtist (){

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
        System.out.println(" ");




        return choose;
    }


}

