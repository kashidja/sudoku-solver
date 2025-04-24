import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int GRID_SIZE=9;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        boolean exit = false;

        while (!exit) {

            System.out.println("Welcome to Sudoku Solver!");
            System.out.println("1. Solve the current puzzle");
            System.out.println("2. Get a hint");
            System.out.println("3. Generate a new puzzle");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (solveBoard(board)) {
                        System.out.println("Solved successfully!");
                    } else {
                        System.out.println("Unsolvable board.");
                    }
                    printBoard(board);
                    break;
                case 2:
                    System.out.print("Enter row (0-8): ");
                    int row = scanner.nextInt();
                    System.out.print("Enter column (0-8): ");
                    int col = scanner.nextInt();
                    int hint = getHint(board, row, col);
                    if (hint != -1) {
                        System.out.println("Hint for cell (" + row + ", " + col + "): " + hint);
                    } else {
                        System.out.println("No valid number for this cell.");
                    }
                    break;
                case 3:
                    System.out.print("Choose difficulty (easy, medium, hard): ");
                    String difficulty = scanner.next().toLowerCase();
                    try {
                        board = generateSudokuWithDifficulty(difficulty);
                        System.out.println("Generated new puzzle:");
                        printBoard(board);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void printBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("------+-------+------");
            }
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col % 3 == 0 && col != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[row][col] == 0 ? ". " : board[row][col] + " ");
            }
            System.out.println();
        }
    }

    //helper methods
    private static boolean isNumberInRow(int[][] board, int number, int row){
        for(int i=0;i<GRID_SIZE;i++){
            if(board[row][i]==number){
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInCol(int[][] board, int number, int col){
        for(int i=0;i<GRID_SIZE;i++){
            if(board[i][col]==number){
                return true;
            }
        }
        return false;
    }

    //checks if number is in 3x3 box
    private static boolean isNumberInBox(int[][] board, int number,int row, int col){
    int localBoxRow=row-row%3;
    int localBoxCol=col-col%3;
    for(int i=localBoxRow;i< localBoxRow+3; i++){
         for(int j=localBoxCol;j<localBoxCol+3;j++){
             if(board[i][j]==number){
                 return true;
             }
         }
    }
    return false;
    }

    private static boolean isValidPlacement (int[][]board,int number,int row,int col){
        return !isNumberInRow(board,number,row) &&
                !isNumberInCol(board,number,col) &&
                !isNumberInBox(board,number,row,col);
    }

     private static boolean solveBoard(int[][]board){
        for(int row=0; row<GRID_SIZE;row++){
             for(int col=0;col<GRID_SIZE;col++){
                 if(board[row][col]==0){
                     for(int nrToTry=1;nrToTry<=GRID_SIZE;nrToTry++){
                         if(isValidPlacement(board,nrToTry,row,col)){
                             board[row][col]=nrToTry;

                             if(solveBoard(board)){
                                 return true;
                             }else{
                                 board[row][col]=0;
                             }
                         }
                     }
                     return false;
                 }
             }
        }
        return true;
     }


    private static boolean isValidBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int number = board[row][col];
                if (number != 0) {
                    board[row][col] = 0; // temporarily remove the number
                    if (!isValidPlacement(board, number, row, col)) {
                        return false;
                    }
                    board[row][col] = number; //restore the number
                }
            }
        }
        return true;
    }

    private static int[][] generateSudoku(int emptyCells) {
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        solveBoard(board); // generate a fully solved board

        Random random = new Random();
        while (emptyCells > 0) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);
            if (board[row][col] != 0) {
                board[row][col] = 0; // remove a number
                emptyCells--;
            }
        }
        return board;
    }

    private static int[][] generateSudokuWithDifficulty(String difficulty) {
        int emptyCells;
        switch (difficulty.toLowerCase()) {
            case "easy":
                emptyCells = 20;
                break;
            case "medium":
                emptyCells = 30;
                break;
            case "hard":
                emptyCells = 50;
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level. Choose easy, medium, or hard.");
        }
        return generateSudoku(emptyCells);
    }

    private static int getHint(int[][] board, int row, int col) {
        if (board[row][col] != 0) {
            return board[row][col]; // Already filled
        }
        for (int number = 1; number <= GRID_SIZE; number++) {
            if (isValidPlacement(board, number, row, col)) {
                return number; // Return the first valid number
            }
        }
        return -1; // No valid number
    }
}