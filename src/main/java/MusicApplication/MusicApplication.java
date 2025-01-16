package MusicApplication;
import java.io.*;
import java.util.Scanner;

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
        System.out.println("");

        boolean loop = true;
        while(loop){
            System.out.println("Please choose what you would like to do\n 1) Update the Music Application\n 2) Consult Music Application");
            ans = input.next();
            if(!(ans.trim().equals("1") || ans.trim().equals("2"))){
                System.out.println("INPUT ERROR - Please enter a valid option");
            }
            else{
                loop = false;
            }
        }



    }


}
