import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class PlayerModeFrame extends Frame implements ActionListener {
    private Button onePlayer, twoPlayer, homeButton;
    private String game;
    private HashMap<String, Integer> playerScores;

    public PlayerModeFrame(String game, HashMap<String, Integer> playerScores) {
        this.game = game;
        this.playerScores = playerScores;

        // Initialize buttons
        onePlayer = new Button("1 Player");
        twoPlayer = new Button("2 Players");
        homeButton = new Button("Home");

        // Set button styles
        setButtonStyles(onePlayer);
        setButtonStyles(twoPlayer);
        setHomeButtonStyle(homeButton); // Different style for Home button

        // Add action listeners
        onePlayer.addActionListener(this);
        twoPlayer.addActionListener(this);
        homeButton.addActionListener(e -> goHome());

        // Layout setup
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3 buttons, 1 column
        buttonPanel.add(homeButton);
        buttonPanel.add(onePlayer);
        buttonPanel.add(twoPlayer);


        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.CENTER); // Center for buttons

        // Frame settings
        setTitle(game + " Mode Selection");
        setSize(400, 300);
        setResizable(false);
        setVisible(true);
    }

    private void setButtonStyles(Button button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.yellow);
        button.setForeground(Color.BLACK);

    }

    private void setHomeButtonStyle(Button button) {
        button.setFont(new Font("Arial", Font.BOLD, 20)); // Larger font for home button
        button.setBackground(Color.pink); // Distinct color for home button
        button.setForeground(Color.black); // White text color
        button.setPreferredSize(new Dimension(150, 60)); // Slightly larger size
    }

    private void goHome() {
        dispose(); // Close the current window
        new Main(); // Open the main menu
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == onePlayer) {
            new PongGame(playerScores, 1);

        } else if (e.getSource() == twoPlayer) {
                new PongGame(playerScores, 2);

        }
    }

    public static void main(String[] args) {
        HashMap<String, Integer> playerScores = new HashMap<>();
        new PlayerModeFrame("Pong", playerScores); // Example for Pong, change as needed
    }
}
