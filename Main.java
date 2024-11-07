import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class Main extends Frame implements ActionListener {
    private Button pongButton, sudokuButton;
    private HashMap<String, Integer> playerScores;

    public Main() {
        // Initialize the main menu and scores
        pongButton = new Button("Play Pong");
        sudokuButton = new Button("Play Sudoku");
        playerScores = new HashMap<>(); // Store player scores

        // Set button styles
        setButtonStyle(pongButton);
        setButtonStyle(sudokuButton);

        pongButton.addActionListener(this);
        sudokuButton.addActionListener(this);

        // Layout setup
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50)); // Center and add spacing
        add(pongButton);
        add(sudokuButton);

        // Frame settings
        setTitle("Game Selection");
        setSize(500, 500);
        setResizable(true);
        setVisible(true);
        setBackground(Color.LIGHT_GRAY); // Set background color
    }

    private void setButtonStyle(Button button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(Color.CYAN);
        button.setForeground(Color.black);
        button.setPreferredSize(new Dimension(150, 50));

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pongButton) {
            new PlayerModeFrame("Pong", playerScores); // Open Player Mode Selection for Pong
        } else if (e.getSource() == sudokuButton) {
            new SudokuGame(playerScores, 1); // Open Player Mode Selection for Sudoku
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
