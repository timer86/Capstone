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
                System.out.println("Please choose what you would like to do\n 1) Update the Music Application\n 2) Consult Music Application \n E) for Exit");
                ans = input.next();
                switch(ans){
                    case "1":
                        loop = false;
                        main_loop = updateMusicMenu();
                    case "2":
                        loop = false;

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
                case "2":
                    update_MM_loop = updateArtist();
                    ;
                case "0":
                    update_MM_loop = false;
                    return true;
                    break;
                case "e","E":
                    update_MM_loop = false;
                    return false;
                default:
                    System.out.println("INPUT ERROR - Please enter a valid option");
                    update_MM_loop = true;
            }

        }


    }

    public static Boolean updateTrack () {
        Scanner input_TR = new Scanner(System.in);

        String ans_TR = "";
        String album = "";
        String title = "";
        String genre = "";
        int yyyy = 1900;

        List<String> artistlist = new ArrayList<String>();
        String single_artist = "";


        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }
        System.out.println("  Welcome to the Music Application - UPDATE TRACK Section  ");
        for (int i = 1; i <= 10; i++) {
            System.out.print("*");
        }
        boolean update_TR_loop = true;
        while (update_TR_loop) {

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



            /* VALIDATE ARTIST NAME IN LIST if EXIST or CREATE*/
            for (int i = 0; i < artistlist.size(); i++) {
                single_artist = artistlist.get(i);
                String id = Artist.getIdByName(single_artist);


            }

        }

    }
}
