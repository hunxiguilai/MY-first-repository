package 斗地主;

import java.util.Objects;
import java.util.Scanner;

public class Constants {
    public static String[] toFigure = {"3", "4", "5", "6", "7", "8", "9", "0", "J", "Q", "K", "A", " ", "2", "Y", "Z"};
    public static int VALUECOUNT = 17;
    public static int PLAYERCOUNT = 3;
    public static int CARDSCOUNT = 54;
    public static int CURRENTPLAYER = 0;
    public static int ERROR = -1;

    public static boolean makeChoice(String tip) {
        System.out.println(tip);
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        return Objects.equals(input, "Y") || Objects.equals(input, "y");
    }
}

