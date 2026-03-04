package hardware;

public class Utils {

    public static int charToIndex(char c, String alphabet) {
        c = Character.toUpperCase(c);
        return alphabet.indexOf(c);
    }


    // Convert index to letter
    public static char indexToChar(int index, String alphabet) {
        index = normalize(index, alphabet.length());
        return alphabet.charAt(index);
    }

    public static int normalize(int index, int size) {
        int res = index % size;
        if (res < 0) {
            res += size;
        }
        return res;
    }

    public static  Character MapNumToABC(int num) {
        return switch (num) {
            case 0 -> 'A';
            case 1 -> 'B';
            case 2 -> 'C';
            case 3 -> 'D';
            case 4 -> 'E';
            case 5 -> 'F';
            case 6 -> 'G';
            case 7 -> 'H';
            case 8 -> 'I';
            case 9 -> 'J';
            case 10 -> 'K';
            case 11 -> 'L';
            case 12 -> 'M';
            case 13 -> 'N';
            case 14 -> 'O';
            case 15 -> 'P';
            case 16 -> 'Q';
            case 17 -> 'R';
            case 18 -> 'S';
            case 19 -> 'T';
            case 20 -> 'U';
            case 21 -> 'V';
            case 22 -> 'W';
            case 23 -> 'X';
            case 24 -> 'Y';
            case 25 -> 'Z';
            default -> null;
        };
    }


}
