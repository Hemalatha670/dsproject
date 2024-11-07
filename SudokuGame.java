import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

public class SudokuGame extends Frame implements ActionListener {
    private final int GRID_SIZE = 9;
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private int[][] board; // Store the current state of the Sudoku board
    private HashMap<String, Integer> playerScores;
    private Button homeButton; // Home button

    public SudokuGame(HashMap<String, Integer> playerScores, int playerMode) {
        this.playerScores = playerScores;

        setTitle("Sudoku Game ");
        setSize(600, 650); // Increased size for the home button
        setLayout(new BorderLayout());

        initializeBoard();
        createButtons();

        homeButton = new Button("Home");
        homeButton.addActionListener(e -> {
            dispose(); // Close the current game window
            new Main(); // Open the main menu
        });

        add(homeButton, BorderLayout.SOUTH); // Add home button to the bottom of the window
        setVisible(true);
    }

    private void initializeBoard() {
        board = new int[GRID_SIZE][GRID_SIZE];

        // Sample hardcoded Sudoku puzzle (0 means empty cell)
        int[][] samplePuzzle = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        for (int r = 0; r < GRID_SIZE; r++) {
            System.arraycopy(samplePuzzle[r], 0, board[r], 0, GRID_SIZE);
        }
    }

    private void createButtons() {
        Panel gridPanel = new Panel(new GridLayout(GRID_SIZE, GRID_SIZE)); // Use a separate panel for the grid
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                buttons[r][c] = new Button(board[r][c] == 0 ? "" : String.valueOf(board[r][c]));
                buttons[r][c].setFont(new Font("Arial", Font.PLAIN, 24));
                buttons[r][c].setBackground(getCellColor(r, c));
                buttons[r][c].addActionListener(this);
                buttons[r][c].setEnabled(board[r][c] == 0); // Only allow input in empty cells
                gridPanel.add(buttons[r][c]); // Add buttons to the grid panel
            }
        }
        add(gridPanel, BorderLayout.CENTER); // Add the grid panel to the center
    }

    private Color getCellColor(int row, int col) {
        return (row / 3 + col / 3) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Button clickedButton = (Button) e.getSource(); // Get the clicked button
        String input = JOptionPane.showInputDialog(this, "Enter a number (1-9):"); // Ask player for input

        if (input != null && input.matches("[1-9]")) { // Check if the input is valid
            int number = Integer.parseInt(input); // Convert input to a number

            if (isValidMove(clickedButton, number)) { // Check if the move is valid
                clickedButton.setLabel(input); // Update the button label
                clickedButton.setEnabled(false); // Disable the button
                updateBoard(clickedButton, number); // Update the game board

                if (isCompleted()) { // Check if the game is completed
                    showContinueDialog(); // Show dialog to continue or go home
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid move!", "Error", JOptionPane.ERROR_MESSAGE); // Invalid move message
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number between 1 and 9.", "Error", JOptionPane.ERROR_MESSAGE); // Invalid input message
        }
    }


    private boolean isValidMove(Button button, int value) {
        // Check row and column
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[findRow(button)][i] == value || board[i][findCol(button)] == value) {
                return false; // Number already in the same row or column
            }
        }

        // Check 3x3 subgrid
        int startRow = (findRow(button) / 3) * 3;
        int startCol = (findCol(button) / 3) * 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (board[r][c] == value) {
                    return false; // Number already in the same subgrid
                }
            }
        }

        return true; // Move is valid
    }

    private int findRow(Button button) {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (buttons[r][c] == button) {
                    return r;
                }
            }
        }
        return -1; // Not found
    }

    private int findCol(Button button) {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (buttons[r][c] == button) {
                    return c;
                }
            }
        }
        return -1; // Not found
    }

    private void updateBoard(Button button, int value) {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (buttons[r][c] == button) {
                    board[r][c] = value; // Update board state
                    return;
                }
            }
        }
    }

    private boolean isCompleted() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (board[r][c] == 0) {
                    return false; // If any cell is still empty
                }
            }
        }
        return true; // All cells filled
    }

    private void showContinueDialog() {
        int option = JOptionPane.showOptionDialog(this, "Congratulations! You completed the Sudoku. Do you want to continue or go to the home page?", "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Continue", "Home"}, "Continue");

        if (option == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.out.println("Returning to home...");
            dispose(); // Close the game window
            new Main(); // Open the main menu
        }
    }

    private void resetGame() {
        initializeBoard();
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                buttons[r][c].setLabel(board[r][c] == 0 ? "" : String.valueOf(board[r][c]));
                buttons[r][c].setEnabled(board[r][c] == 0); // Enable empty cells
            }
        }
    }

    public static void main(String[] args) {
        HashMap<String, Integer> playerScores = new HashMap<>();
        new SudokuGame(playerScores, 1); // Change to 2 for 2 Player mode
    }
}
