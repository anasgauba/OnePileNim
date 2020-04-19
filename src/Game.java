import java.util.Arrays;
import java.util.Scanner;

/**
 * Main game class for One-Pile Nim Game (aka Subtraction Game) with command
 * line user interface. A game between human and smart computer player.
 * @version 2019-01-23
 * @author Anas Farooq Gauba
 */
public class Game {

    /**
     * Determines if the user played an invalid move. Look in the moves array
     * and see if the number that user pressed is allowed, if it is, then its
     * a valid move, otherwise, invalid.
     * @param length of the moves array.
     * @param humanPlay user's play on their turn.
     * @param moves set of allowed moves.
     * @return valid or invalid move.
     */
    private boolean isInvalidMove(int length, int humanPlay, int[] moves) {
        boolean invalid = true;
        for (int i = 0; i < length; i++) {
            if (moves[i] == humanPlay) {
                invalid = false;
                break;
            }
        }
        return invalid;
    }

    /**
     * Main loop for the One-Pile Nim game against human and computer player.
     * @param args Command line args ignored here.
     */
    public static void main(String[] args) {
        Game game = new Game();
        Scanner scan = new Scanner(System.in);

        int N;
        int length;
        int[] moves;

        System.out.println("Welcome to One-Pile Nim!");
        System.out.println("Please pick number of marbles:");

        N = scan.nextInt();

        System.out.println("Please give us the length of the array");

        length = scan.nextInt();
        moves = new int[length];

        System.out.println("Please give us the elements of moves array:");
        for (int i = 0; i < length; i++) {
            moves[i] = scan.nextInt();
        }
        System.out.println();
        System.out.println("N = " + N + ", moves = " + Arrays.toString(moves));
        System.out.println();
        System.out.println("Do you want to go first? Y/N");

        String response = scan.next();

        int humanPlay, goodMove = 0;
        Computer computerPlayer = new Computer(N,moves);

        if (response.contains("N") || response.contains("n")) {
            while (scan.hasNextLine()) {
                computerPlayer.setTurn(true);
                computerPlayer.play(N, moves);
                goodMove = computerPlayer.getGoodMove();
                N = N - goodMove;
                System.out.println("N remaining: " + N);
                if (N == 0) {
                    System.out.println();
                    System.out.println("Computer Wins!!!");
                    break;
                }
                System.out.println("User, please pick a number from valid set of moves");
                humanPlay = scan.nextInt();

                // error handling, human shouldn't make an invalid move.
                while (game.isInvalidMove(length,humanPlay,moves)) {
                    System.out.println("INVALID MOVE! Make a valid move!");
                    humanPlay = scan.nextInt();
                }

                N = N - humanPlay;
                System.out.println("N remaining: " + N);
                if (N == 0) {
                    System.out.println();
                    System.out.println("Congratulations! You Won.");
                    break;
                }
            }
        }
        else if (response.contains("Y") || response.contains("y")) {
            System.out.println();
            System.out.println("Alright!!! Lets make a move!");
            while (scan.hasNextLine()) {
                System.out.println("User, please pick a number from valid set of moves");
                humanPlay = scan.nextInt();

                // error handling, human shouldn't make an invalid move.
                while (game.isInvalidMove(length,humanPlay,moves)) {
                    System.out.println("INVALID MOVE! Make a valid move!");
                    humanPlay = scan.nextInt();
                }

                N = N - humanPlay;
                System.out.println("N remaining: " + N);
                if (N == 0) {
                    System.out.println();
                    System.out.println("Congratulations! You Won.");
                    break;
                }

                computerPlayer.setTurn(true);
                computerPlayer.play(N, moves);
                goodMove = computerPlayer.getGoodMove();
                N = N - goodMove;
                System.out.println("N remaining: " + N);
                if (N == 0) {
                    System.out.println();
                    System.out.println("Computer Wins!!!");
                    break;
                }
            }
        }
    }
}
