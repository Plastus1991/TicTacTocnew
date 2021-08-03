package com.kodilla.tictactoe;

public class MiniMax {

    private static final char player = 'X';
    private static final char comp = 'O';
    private static final int MAX_DEPTH = 6;

    private static int evaluateBoard(char[][] cells) {
        int rowSum = 0;
        int bWidth = 3;
        int Xwin = 3;
        int Owin = 3;

        for (int row = 0; row < bWidth; row++) {
            for (int col = 0; col < bWidth; col++) {
                rowSum += cells[row][col];
            }
            if (rowSum == Xwin) {
                return 10;
            } else if (rowSum == Owin) {
                return -10;
            }
            rowSum = 0;
        }

        rowSum = 0;
        for (int col = 0; col < bWidth; col++) {
            for (int row = 0; row < bWidth; row++) {
                rowSum += cells[row][col];
            }
            if (rowSum == Xwin) {
                return 10;
            } else if (rowSum == Owin) {
                return -10;
            }
            rowSum = 0;
        }

        rowSum = 0;
        for (int i = 0; i < bWidth; i++) {
            rowSum += cells[i][i];
        }
        if (rowSum == Xwin) {
            return 10;
        } else if (rowSum == Owin) {
            return -10;
        }

        rowSum = 0;
        int indexMax = bWidth - 1;
        for (int i = 0; i <= indexMax; i++) {
            rowSum += cells[i][indexMax - i];
        }
        if (rowSum == Xwin) {
            return 10;
        } else if (rowSum == Owin) {
            return -10;
        }

        return 0;
    }

    public static int miniMax(char[][] cells, int depth, boolean isMax) {
        int boardVal = evaluateBoard(cells);

        // Terminal node (win/lose/draw) or max depth reached.
        if (Math.abs(boardVal) == 10 || depth == 0
                || isFull(cells)) {
            return boardVal;
        }

        if (isMax) {
            int highestVal = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (!isFilled(cells[row][col])) {
                        cells[row][col] = comp;
                        highestVal = Math.max(highestVal, miniMax(cells,
                                depth - 1, false));
                        cells[row][col] = ' ';
                    }
                }
            }
            return highestVal;
        } else {
            int lowestVal = Integer.MAX_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (!isFilled(cells[row][col])) {
                        cells[row][col] = player;
                        lowestVal = Math.min(lowestVal, miniMax(cells,
                                depth - 1, true));
                        cells[row][col] = ' ';
                    }
                }
            }
            return lowestVal;
        }
    }

    public static int[] getBestMove(char[][] cells) {
        int[] bestMove = new int[]{-1, -1};
        int bestValue = Integer.MIN_VALUE;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (!isFilled(cells[row][col])) {
                    cells[row][col] = comp;
                    int moveValue = miniMax(cells, MAX_DEPTH, false);
                    cells[row][col] = ' ';
                    if (moveValue > bestValue) {
                        bestMove[0] = row;
                        bestMove[1] = col;
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }

    private static boolean isFilled(char cell) {
        return cell == player || cell == comp;
    }

    public static boolean isFull(char [][]cells) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (!isFilled(cells[i][j]))
                    return false;

        return true;
    }
}
