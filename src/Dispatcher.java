import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Dispatcher {
    private static int ROW_COUNT = 3;
    private static int COL_COUNT = 3;
    private static String CELL_STATE_EMPTY = " ";
    private static String CELL_STATE_X = "X";
    private static String CELL_STATE_O = "O";
    private static String GAME_STATE_X_WON = "Гравець переміг!";
    private static String GAME_STATE_0_WON = "Компютер переміг! Спробуй ще!";
    private static String GAME_STATE_DRAW = "Нічия!";
    private static String GAME_STATE_GAME_NOT_FINISHED = "Гра не закінчена!";
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    public static void main(String[] args) {
        String[][] board = createBoard();
        startGameRound();
    }

    public static void startGameRound() {
        System.out.println("Початок гри 'Хрестики-Нулики'");
        String[][] board = createBoard();
        startGameLoop(board);
        // create board
        // startGameLoop
    }
    public static void startGameLoop(String[][] board){
        int moveOrder = chooseMoveOrder();
        if(moveOrder == 0){
            boolean playerTurn = true;
            do {
                if(playerTurn == true){
                    makePlayerTurn(board);
                    printBoard(board);
                    System.out.println();
                } else{
                    makeBotTurn(board);
                    printBoard(board);
                    System.out.println();
                }
                playerTurn = !playerTurn;
                String gameState = checkGameState(board);
                if (!Objects.equals(gameState, GAME_STATE_GAME_NOT_FINISHED)) {
                    System.out.println(gameState);
                    return;
                }
            } while (true);
        } else{
            boolean botTurn = true;
            do {
                if(botTurn == true){
                    makeBotTurn(board);
                    printBoard(board);
                    System.out.println();
                } else{
                    makePlayerTurn(board);
                    printBoard(board);
                    System.out.println();
                }
                botTurn = !botTurn;
                String gameState = checkGameState(board);
                if (!Objects.equals(gameState, GAME_STATE_GAME_NOT_FINISHED)) {
                    System.out.println(gameState);
                    return;
                }
            } while (true);
        }
    }
    public static int chooseMoveOrder(){
        System.out.println("Кидаємо монетку для вибору порядку ходів. Оберіть орел(1) або решка(2).");
        int playerMoveOrderInput = Integer.parseInt(scanner.nextLine());
        int randomMoveOrder = random.nextInt(2);
        if (playerMoveOrderInput == (randomMoveOrder + 1)){
            System.out.println("Гравець ходить першим.");
            return 0;
        }else{
            System.out.println("Компютер ходить першим.");
            System.out.println("Компютер ходить першим.");
            return 1;
        }
    }
     public static void makeBotTurn(String[][] board){
        int[] coordinates = getRandomEmptyCellCoordinates(board);
        board[coordinates[0]][coordinates[1]] = CELL_STATE_O;
         System.out.println("Бот походив (" + coordinates[0] + "-" + coordinates[1] + ")");
    }
    public static int[] getRandomEmptyCellCoordinates(String[][] board){
        do{
            int row = random.nextInt(ROW_COUNT);
            int col = random.nextInt(COL_COUNT);
            if (Objects.equals(board[row][col], CELL_STATE_EMPTY)) {
                return new int[]{row, col};
            }
        }while(true);
    }
    public static void makePlayerTurn(String[][] board){
        int[] coordinates = inputCellCoordinates(board);
        board[coordinates[0]][coordinates[1]] = CELL_STATE_X;
    }
    public static int[] inputCellCoordinates(String[][] board){
        System.out.println("Введіть координати через пробіл (0-2)");
        do{
            String[] input = scanner.nextLine().split(" ");
            int row = Integer.parseInt(input[0]);
            int col = Integer.parseInt(input[1]);
            if((row < 0) || (row >= ROW_COUNT) || (col >= COL_COUNT)){
                System.out.println("Некоректне значення!! Введіть два значення від 0 до 2");
            } else if(!Objects.equals(board[row][col], CELL_STATE_EMPTY)) {
                System.out.println("Данна клітинка вже зайнята!! Введіть інше значення!!");
            } else {
                return new int[]{row,col};
            }
        }while(true);
    }
    public static String checkGameState(String[][] board){
        ArrayList<Integer> sum = new ArrayList<>();
        for (int row = 0; row < ROW_COUNT; row++) {
            int rowSum = 0;
            for (int col = 0; col < COL_COUNT; col++) {
                rowSum += getCellNumValue(board[row][col]);
            }
            sum.add(rowSum);
        }
        for (int col = 0; col < COL_COUNT; col++) {
            int colSum = 0;
            for (int row = 0; row < ROW_COUNT; row++) {
                colSum += getCellNumValue(board[row][col]);
            }
            sum.add(colSum);
        }
        int leftDiagonalSum = 0;
        for (int i = 0; i < ROW_COUNT; i++) {
            leftDiagonalSum += getCellNumValue(board[i][i]);
        }
        sum.add(leftDiagonalSum);
        int rightDiagonalSum = 0;
        for (int i = 0; i < ROW_COUNT; i++) {
            rightDiagonalSum += getCellNumValue(board[i][(ROW_COUNT - 1) - i]);
        }
        sum.add(rightDiagonalSum);
        if(sum.contains(3)){
            return GAME_STATE_X_WON;
        } else if (sum.contains(-3)) {
            return GAME_STATE_0_WON;
        } else if (areAllCellsTaken(board)) {
            return GAME_STATE_DRAW;
        } else{
            return GAME_STATE_GAME_NOT_FINISHED;
        }
    }
    public static boolean areAllCellsTaken(String[][] board) {
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                if (Objects.equals(board[row][col], CELL_STATE_EMPTY)) {
                    return false;
                }
            }
        }
        return true;
    }
    private static int getCellNumValue(String cellState) {
        if(cellState == CELL_STATE_X){
            return 1;
        } else if (cellState == CELL_STATE_O) {
            return -1;
        } else{
            return 0;
        }
    }
    public static String[][] createBoard(){
        String[][] board = new String[ROW_COUNT][COL_COUNT];
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                board[row][col] = CELL_STATE_EMPTY;
            }
        }
        return board;
    }
    public static void printBoard(String[][] board){
        for (int row = 0; row < ROW_COUNT; row++) {
            String line = "| ";
            for (int col = 0; col < COL_COUNT; col++) {
                line += board[row][col] + " ";
            }
            line += "|";
            System.out.println(line);
        }
    }
}