import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class PongGame extends Frame implements Runnable, KeyListener {
    int width = 600, height = 400;
    int ballX = width / 2, ballY = height / 2;
    int ballDiameter = 20, ballXSpeed = 3, ballYSpeed = 3;
    int paddleHeight = 80, paddleWidth = 10;
    int player1Y = height / 2, player2Y = height / 2;
    int paddleSpeed = 10;
    boolean upPressed = false, downPressed = false, wPressed = false, sPressed = false;
    boolean gamePaused = false; // Game pauses after each point
    int playerMode;
    HashMap<String, Integer> playerScores;

    public PongGame(HashMap<String, Integer> playerScores, int playerMode) {
        this.playerScores = playerScores;
        this.playerMode = playerMode;

        setTitle("Pong Game - " + (playerMode == 1 ? "1 Player" : "2 Players"));
        setSize(width, height);
        setVisible(true);
        setResizable(false);
        addKeyListener(this);

        // Initialize scores if not already
        playerScores.putIfAbsent("Player1", 0);
        if (playerMode == 2) {
            playerScores.putIfAbsent("Player2", 0);
        } else {
            playerScores.putIfAbsent("AI", 0);
        }

        // Start game loop
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        while (true) {
            if (!gamePaused) {
                moveBall();
                movePaddles();
                checkCollisions();
                repaint();
            }
            try {
                Thread.sleep(10); // Control game speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        // Draw paddles
        g.setColor(Color.CYAN);
        g.fillRect(30, player1Y, paddleWidth, paddleHeight); // Player 1 paddle
        g.fillRect(width - 40, player2Y, paddleWidth, paddleHeight); // Player 2/AI paddle

        // Draw ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballX, ballY, ballDiameter, ballDiameter);

        // Draw scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Player 1: " + playerScores.get("Player1"), 50, 50);

        if (playerMode == 2) {
            g.drawString("Player 2: " + playerScores.get("Player2"), width - 150, 50);
        } else {
            g.drawString("AI: " + playerScores.get("AI"), width - 150, 50);
        }
    }

    private void moveBall() {
        ballX += ballXSpeed;
        ballY += ballYSpeed;

        // Ball collision with top and bottom walls
        if (ballY <= 0 || ballY >= height - ballDiameter) {
            ballYSpeed = -ballYSpeed;
        }

        // Ball out of bounds (score)
        if (ballX <= 0) {
            if (playerMode == 2) {
                playerScores.put("Player2", playerScores.get("Player2") + 1);
            } else {
                playerScores.put("AI", playerScores.get("AI") + 1);
            }
            showContinueDialog();
        } else if (ballX >= width - ballDiameter) {
            playerScores.put("Player1", playerScores.get("Player1") + 1);
            showContinueDialog();
        }
    }

    private void resetBall() {
        ballX = width / 2;
        ballY = height / 2;
        ballXSpeed = -ballXSpeed; // Change direction after each score
    }

    private void movePaddles() {
        // Player 1 movement
        if (wPressed && player1Y > 0) {
            player1Y -= paddleSpeed;
        }
        if (sPressed && player1Y < height - paddleHeight) {
            player1Y += paddleSpeed;
        }

        // Player 2 movement (Two-player mode)
        if (playerMode == 2) {
            if (upPressed && player2Y > 0) {
                player2Y -= paddleSpeed;
            }
            if (downPressed && player2Y < height - paddleHeight) {
                player2Y += paddleSpeed;
            }
        } else {
            // Simple AI for single-player mode
            if (ballY < player2Y && player2Y > 0) {
                player2Y -= paddleSpeed - 2;
            } else if (ballY > player2Y && player2Y < height - paddleHeight) {
                player2Y += paddleSpeed - 2;
            }
        }
    }

    private void checkCollisions() {
        // Ball collision with player 1 paddle
        if (ballX <= 40 && ballY + ballDiameter >= player1Y && ballY <= player1Y + paddleHeight) {
            ballXSpeed = -ballXSpeed;
        }

        // Ball collision with player 2 or AI paddle
        if (ballX >= width - 50 && ballY + ballDiameter >= player2Y && ballY <= player2Y + paddleHeight) {
            ballXSpeed = -ballXSpeed;
        }
    }

    private void showContinueDialog() {
        gamePaused = true; // Pause the game

        // Display a dialog asking to continue or return to the home screen
        int option = JOptionPane.showOptionDialog(this, "Do you want to continue or go to the home page?", "Match Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Continue", "Home"}, "Continue");

        if (option == JOptionPane.YES_OPTION) {
            // Continue the game, keep the scores
            resetBall();
            gamePaused = false; // Resume the game
        } else {
            // Return to home page
            System.out.println("Returning to home...");
            dispose(); // Close the game window
            new Main(); // Open the main menu
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            wPressed = true;
        }
        if (key == KeyEvent.VK_S) {
            sPressed = true;
        }
        if (key == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            wPressed = false;
        }
        if (key == KeyEvent.VK_S) {
            sPressed = false;
        }
        if (key == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        HashMap<String, Integer> playerScores = new HashMap<>();
        // Set mode to 1 for single-player or 2 for two-player
        new PongGame(playerScores, 2); // Change this number to switch between modes
    }
}
