import java.lang.reflect.Array;
import java.util.*;
/**
 * Computer class for computer player of One-Pile Nim (part 1b) as well as building a table
 * for playing against human player (part 1a), finding a period for part 2a.
 * @version date: 2019-01-23
 * @author Anas Farooq Gauba
 */
public class Computer {
    private Hashtable<Integer, Boolean> table;
    private boolean isTurn;
    private int goodMove;

    /**
     * @param N num of marbles.
     * @param moves set of moves.
     */
    public Computer(int N, int[] moves) {
        this.goodMove = 0;
        this.table = new Hashtable<>();
        this.isTurn = false;
        // Minimizes deep recursion to stack.
        for (int i = 0; i <= N; i++) {
            isWinning(i, moves);
        }
    }

    /**
     * get good move for computer.
     * @return good move.
     */
    public int getGoodMove() {
        return goodMove;
    }

    /**
     * @return turn of computer.
     */
    public boolean turn() {
        return isTurn;
    }

    /**
     * @param turn on/off.
     */
    public void setTurn(boolean turn) {
        this.isTurn = turn;
    }

    /**
     * Building a table of winning and losing move for the computer
     * player. Building a table based on the valid set of allowed moves
     * for the player and returning true or false. There are some edge cases
     * handled, the number of marbles shouldn't be less than set of moves.
     * @param N number of marbles.
     * @param moves allowed set of moves.
     * @return winning move (T) or losing move (F).
     */
    private boolean isWinning(int N, int[] moves) {
        // base case
        if (N == 0) {
            table.put(N, false);
            return false;
        }
        // memoize it to avoid duplicate recursion.
        else if (table.containsKey(N)) {
            return table.get(N);
        }
        else {
            for (int m : moves) {
                // break out of loop if N < m, negative marbles.
                if (N < m) {
                    break;
                }
                else if (!isWinning(N - m, moves)) {
                    goodMove = m;
                    table.put(N, true);
                    return true;
                }
            }
        }
        table.put(N, false);
        return false;
    }

    /**
     * Computer player to play a perfect game against the user if he goes first.
     * Check to see if it is computer's turn, then see what position he's in,
     * losing or winning. If winning position, then he puts the opponent in a
     * losing position (F). There is an edge case for the computer to access neg
     * marbles, so the value at table.get(N-m) shouldn't be null.
     * @param N number of marbles.
     * @param moves allowed set of moves.
     */
    public void play(int N, int[] moves) {
        System.out.println();
        Random rand = new Random();
        if (turn()) {
            if (isWinning(N, moves) || !isWinning(N, moves)) {
                // comp plays from allowed set of moves, finds a losing
                // position for the opponent from the table.
                for (int m : moves) {
                    if (table.get(N-m) != null) {
                        if (!table.get(N - m)) {
                            goodMove = m;
                            System.out.println("COMPUTER PLAYER'S MOVE: " + goodMove);
                            isTurn = false;
                            return;
                        }
                    }
                }
                // look for the case where computer can't find the losing
                // position from the opponent.
                // randomly choosing a move and hoping for the human
                // to make a wrong move.
                goodMove = moves[rand.nextInt(moves.length)];
                isTurn = false;
            }
        }
    }

    /**
     * Finds the period (repeating pattern) for the given set of moves.
     * It makes sure to account for offsets (initial values) before the
     * period begins. Has two arrays to compare, initialArr initially starts
     * at index 0 and shiftedArr initially starts at index+1. Both arrays
     * have blockSize of max(moves). Initially, the offset is -1 and period
     * is 0. The outer loop has initialArr (arr1) and inner loop has
     * shiftedArr (arr2), in each iteration of inner loop, the program
     * compares arr2 with arr1. If they are equal, the program calculates
     * the period, prints it to the console and gets out of the method.
     * @param moves allowed set of moves.
     */
    public void findPeriod(int[] moves) {
        int n = table.size();

        int blockSize = Arrays.stream(moves).max().getAsInt();

        int offset = -1;
        int period = 0;
        ArrayList<Boolean> initialArr = new ArrayList<>();
        ArrayList<Boolean> shiftedArr = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            offset++;
            initialArr = getArray(moves, i, blockSize+i);

            for (int j = i+1; j < n; j++) {
                shiftedArr = getArray(moves, j, blockSize+j);

                // parsing an arrayList to strings for easy comparisons.
                String s1 = initialArr.toString();
                String s2 = shiftedArr.toString();

                if (s1.equals(s2)) {
                    String repeatingPart = "";
                    // if equal, the period is the distance between i and j.
                    for (int m = i; m < j; m++) {
                        period++;
                        repeatingPart += table.get(m).toString().toUpperCase().substring(0,1) + ", ";
                    }
                    System.out.println("Initial values are: " + getPattern(offset));
                    System.out.println("Repeating pattern is: " + repeatingPart);
                    System.out.println("The offset is " + offset);
                    System.out.println("The period is " + period);
                    return;
                }
            }
        }
    }

    /**
     * Gets the initial pattern before the period begins.
     * @param offset offset.
     * @return initial segment before the period begins.
     */
    private String getPattern(int offset) {
        String pattern = "";

        for (int i = 0; i < offset; i++) {
            pattern += table.get(i).toString().toUpperCase().substring(0,1) + ", ";
        }
        if (offset == 0) {
            return "NONE! No offset!";
        }
        return pattern;
    }

    /**
     * Helper function for findPeriod.
     * Gets the array from creating index i till blockSize.
     * @param moves allowed set of moves.
     * @param i beginning index.
     * @param blockSize end index.
     * @return an array from i to blockSize.
     */
    private ArrayList<Boolean> getArray(int[] moves, int i, int blockSize) {
        ArrayList<Boolean> arr = new ArrayList<>();

        for (int j = i; j < blockSize; j++) {
            arr.add(isWinning(j, moves));
        }

        return arr;
    }

    /**
     * Prints the table to the console. Prints 1st 100 values.
     */
    public void printTable() {
        for (int i = 0; i < 101; i++) {
            System.out.println(i + ":" + table.get(i) + " ");
        }
        System.out.println();
    }

    /**
     * Main function to test the periodicity (repeating pattern) of the game.
     * Prints a table for visual representation.
     * @param args Command line args ignored here.
     */
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        int[] moves = {2,12,35,47,99};

        Computer nim = new Computer(26380, moves);
        System.out.println(nim.isWinning(26380, moves));
        nim.printTable();
        nim.findPeriod(moves);
        long time2 = System.currentTimeMillis();
        System.out.println(time2 - time + " millis, it takes.");
    }
}
