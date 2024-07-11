import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NQueensVisualizer extends JFrame {
    private int size;
    private List<int[]> solutions;
    private int[] currentBoard;
    private ChessBoardPanel chessBoardPanel;
    private Image queenIcon;
    private int currentSolutionIndex = 0;

    public NQueensVisualizer() {
        setTitle("N-Queens Visualizer");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        loadQueenIcon();
        addControls();
    }

    private void loadQueenIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/queen.png")); // Ensure this path is correct
        queenIcon = icon.getImage();
    }

    private void addControls() {
        JPanel controlPanel = new JPanel();
        JLabel label = new JLabel("Enter the value of N: ");
        JTextField textField = new JTextField(5);
        JButton solveButton = new JButton("Solve");
        JButton nextSolutionButton = new JButton("Next Solution");
        nextSolutionButton.setEnabled(false);

        solveButton.addActionListener(e -> {
            try {
                size = Integer.parseInt(textField.getText());
                if (size < 1) {
                    throw new NumberFormatException();
                }
                solutions = null;
                currentBoard = new int[size];
                for (int i = 0; i < size; i++) {
                    currentBoard[i] = -1;
                }
                chessBoardPanel = new ChessBoardPanel();
                add(chessBoardPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
                NQueensSolver solver = new NQueensSolver(size, this);
                new Thread(() -> {
                    solutions = solver.solve();
                    SwingUtilities.invokeLater(() -> {
                        if (!solutions.isEmpty()) {
                            currentSolutionIndex = 0;
                            currentBoard = solutions.get(currentSolutionIndex);
                            nextSolutionButton.setEnabled(true);
                            chessBoardPanel.repaint();
                        }
                    });
                }).start();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive integer.");
            }
        });

        nextSolutionButton.addActionListener(e -> {
            if (solutions != null && !solutions.isEmpty()) {
                currentSolutionIndex = (currentSolutionIndex + 1) % solutions.size();
                currentBoard = solutions.get(currentSolutionIndex);
                chessBoardPanel.repaint();
            }
        });

        controlPanel.add(label);
        controlPanel.add(textField);
        controlPanel.add(solveButton);
        controlPanel.add(nextSolutionButton);
        add(controlPanel, BorderLayout.NORTH);
    }

    public void updateBoard(int[] board, int row) {
        currentBoard = board.clone();
        chessBoardPanel.repaint();
        try {
            Thread.sleep(200); // Slow down the visualization to see the steps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ChessBoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentBoard == null) {
                return;
            }
            int cellSize = getWidth() / size;
            Font font = new Font("Arial", Font.BOLD, cellSize / 5);

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    if ((row + col) % 2 == 0) {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(Color.GRAY);
                    }
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);

                    // Draw row and column numbers
                    g.setColor(Color.BLACK);
                    g.setFont(font);
                    if (col == 0) {
                        g.drawString(Integer.toString(row), 5, (row + 1) * cellSize - 5);
                    }
                    if (row == 0) {
                        g.drawString(Integer.toString(col), col * cellSize + 5, cellSize - 5);
                    }

                    if (currentBoard[row] == col) {
                        g.drawImage(queenIcon, col * cellSize, row * cellSize, cellSize, cellSize, this);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NQueensVisualizer visualizer = new NQueensVisualizer();
            visualizer.setVisible(true);
        });
    }
}
