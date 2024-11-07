import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;

public class welcome extends Frame implements ActionListener {
    private Image backgroundImage;
    private Button homeButton;

    public welcome() {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("background.jpg")); // Use the correct path to your image
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Setup the frame
        setTitle("Welcome Page");
        setSize(500, 500);
        setResizable(false);
        setVisible(true);
        setLayout(null); // Use null layout for absolute positioning

        // Create Home Button
        homeButton = new Button("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 16));
        homeButton.setBackground(Color.CYAN);
        homeButton.setForeground(Color.BLACK);
        homeButton.setBounds(250, 20, 100, 30); // Position the button at the top

        // Add action listener to the button
        homeButton.addActionListener(this);

        // Add button to the frame

        add(homeButton);

        // Add Window Listener to close the application properly
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose(); // Close the window
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw welcome message
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Welcome!", 200, 200); // Centered position
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == homeButton) {
            new Main(); // Redirect to the Main.java page
            dispose(); // Close the welcome page
        }
    }

    public static void main(String[] args) {
        new welcome();
    }
}
