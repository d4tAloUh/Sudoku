import java.io.*;

public class Sudoku {
    private static int boardSize = 9;

    public static void main(String[] args) {
        String line = "";
        String file = "Sudoku.txt";
        String fileLast = "SudokuSolved.txt";
        char[] chars;
        char[][] board = new char[9][9];
        int i = 0;
        int j = 0;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                chars = line.toCharArray();
                for (char aChar : chars) {
                    if (i < 9) {
                        if (aChar == '*' || ('0' <= aChar && aChar <= '9')) {
                            board[i][j++] = aChar;
                        }
                    }
                }
                i++;
                j = 0;

            }
//          Print starting board
            printBoard(board);
//          Run the algorithm
            solve(board);
//          Print Solved board
            printBoard(board);
            bufferedReader.close();
        } catch (FileNotFoundException exception) {
            System.out.println("unable to open the file" + file);
        } catch (IOException exception2) {
            System.out.println("error reading file");
        }
        try {
//            Write Solved board into file;
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileLast, true));
            for (int k = 0; k < boardSize; k++) {
                for (int l = 0; l < boardSize; l++) {
                    bufferedWriter.write(board[k][l] + " ");
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException excp){
            System.out.println("Problem with writing file");
        }
    }

    private static void printBoard(char[][] board) {
        System.out.println("Printing board : ");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static boolean solve(char[][] board) {
        //Прохід по рядкам
        for (int i = 0; i < boardSize; i++) {
            // Прохід по стовпцям
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == '*') {
                    // спроба заповнити дошку значеннями
                    for (int k = 1; k <= 9; k++) {
                        board[i][j] = (char) (k + 48);
//                        printBoard(board);
                        // перевірка підстановки та наступна ітерація
                        if (isValid(board, i, j) && solve(board)) {
                            return true;
                        }
                        board[i][j] = '*';
                    }
                    // Бектрекінг, якщо значення не підійшло для подальшого розв'язку, то
                    // дерево рекурсії згортається до останньої правильної підстановки
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(char[][] board, int row, int column) {
        return squareRestriction(board, row, column)
                && rowRestiction(board, row)
                && columnRestiction(board, column);

    }

    private static boolean squareRestriction(char[][] board, int row, int column) {
        int[] boolVector = new int[boardSize];
        int squareRow = row / 3 * 3;
        int squareRowEnd = squareRow + 3;
        int squareColumn = column / 3 * 3;
        int squareColumnEnd = squareColumn + 3;
        for (int i = squareRow; i < squareRowEnd; i++) {
            for (int j = squareColumn; j < squareColumnEnd; j++) {
                if (!checkRestriction(board, i, j, boolVector))
                    return false;
            }
        }
        return true;
    }

    private static boolean rowRestiction(char[][] board, int row) {
        int[] boolVector = new int[boardSize];
        for (int i = 0; i < boardSize; i++) {
            if (!checkRestriction(board, row, i, boolVector))
                return false;
        }
        return true;
    }

    private static boolean columnRestiction(char[][] board, int column) {
        int[] boolVector = new int[boardSize];
        for (int i = 0; i < boardSize; i++) {
            if (!checkRestriction(board, i, column, boolVector))
                return false;
        }
        return true;
    }

    private static boolean checkRestriction(char[][] board, int row, int column, int[] boolVector) {
        //Якщо на дошці в клітинці (row, column) є значення, якого немає в булевому векторі
        // то змінити в булевому векторі клітіинку зі значеенням клітинки дошки на 1
        if (board[row][column] != '*') {
            if (boolVector[(int) board[row][column] - 1 - 48] != 1)
                boolVector[(int) board[row][column] - 1 - 48] = 1;
            else return false;
        }
        return true;
    }
}
