import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("Welcome");
        setSize(350, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(false); // Set to true for frameless look

        // Gradient panel
        JPanel gradientPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(183, 148, 244); // Light purple
                Color color2 = new Color(112, 202, 255); // Light blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(new BoxLayout(gradientPanel, BoxLayout.Y_AXIS));
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(60, 30, 30, 30));

        // Logo or icon
        JLabel logo = new JLabel("◯", SwingConstants.CENTER); // Placeholder logo
        logo.setFont(new Font("SansSerif", Font.BOLD, 64));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        gradientPanel.add(logo);

        JLabel appName = new JLabel("Welcome", SwingConstants.CENTER);
        appName.setFont(new Font("SansSerif", Font.BOLD, 22));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);
        gradientPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gradientPanel.add(appName);

        // Buttons
        JButton loginBtn = createButton("Login", true);
        JButton signupBtn = createButton("Sign Up", false);

        gradientPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        gradientPanel.add(loginBtn);
        gradientPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gradientPanel.add(signupBtn);

        // Guest option
        JLabel guestLabel = new JLabel("Continue as a guest", SwingConstants.CENTER);
        guestLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        guestLabel.setForeground(Color.WHITE);
        guestLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gradientPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        gradientPanel.add(guestLabel);

        // Button Actions
        loginBtn.addActionListener(e -> {
            new LoginWindow();
            dispose();
        });

        signupBtn.addActionListener(e -> {
            new SignUpWindow(); // Make sure this class exists
            dispose();
        });

        guestLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new AppointmentScheduler(); // Or guest view
                dispose();
            }
        });

        add(gradientPanel);
        setVisible(true);
    }

    private JButton createButton(String text, boolean filled) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(240, 40));
        button.setMaximumSize(new Dimension(240, 40));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        if (filled) {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(120, 90, 200));
        } else {
            button.setBackground(new Color(0, 0, 0, 0));
            button.setForeground(Color.WHITE);
        }
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomeScreen::new);
    }
}
