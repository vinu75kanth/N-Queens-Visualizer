import java.util.ArrayList;
import java.util.List;

public class NQueensSolver {
    private int size;
    private List<int[]> solutions;
    private NQueensVisualizer visualizer;

    public NQueensSolver(int size, NQueensVisualizer visualizer) {
        this.size = size;
        this.solutions = new ArrayList<>();
        this.visualizer = visualizer;
    }

    public List<int[]> solve() {
        int[] board = new int[size];
        for (int i = 0; i < size; i++) {
            board[i] = -1;  // Initialize with -1 to indicate no queen placed
        }
        placeQueen(board, 0);
        return solutions;
    }

    private void placeQueen(int[] board, int row) {
        if (row == size) {
            solutions.add(board.clone());
            visualizer.updateBoard(board, row - 1);
            return;
        }
        for (int col = 0; col < size; col++) {
            if (isSafe(board, row, col)) {
                board[row] = col;
                visualizer.updateBoard(board, row);
                placeQueen(board, row + 1);
                board[row] = -1;  // Backtracking step
                visualizer.updateBoard(board, row);
            }
        }
    }

    private boolean isSafe(int[] board, int row, int col) {
        for (int i = 0; i < row; i++) {
            int queenCol = board[i];
            if (queenCol == col || queenCol - i == col - row || queenCol + i == col + row) {
                return false;
            }
        }
        return true;
    }
}
