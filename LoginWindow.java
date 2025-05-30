import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginWindow extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    boolean isPasswordVisible = false;

    final String DB_URL = "jdbc:mysql://localhost:3306/school_appointments";
    final String USER = "root";
    final String PASS = "";

    public LoginWindow() {
        setTitle("Login");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel gradientPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(183, 148, 244);
                Color color2 = new Color(112, 202, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new BoxLayout(gradientPanel, BoxLayout.Y_AXIS));
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 40, 40));

        JLabel logo = new JLabel("🔐", SwingConstants.CENTER);
        logo.setFont(new Font("SansSerif", Font.PLAIN, 48));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        gradientPanel.add(logo);

        JLabel title = new JLabel("Welcome,\nGlad to see you!");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        gradientPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gradientPanel.add(title);

        gradientPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        usernameField = new JTextField("Email Address");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        usernameField.setForeground(Color.GRAY);
        usernameField.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        addPlaceholderListener(usernameField, "Email Address");
        gradientPanel.add(usernameField);

        gradientPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        passwordField = new JPasswordField("Password");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char) 0);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        addPasswordPlaceholderListener(passwordField, "Password");

        JLabel eyeIcon = new JLabel("👁️");
        eyeIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eyeIcon.setBorder(new EmptyBorder(0, 8, 0, 8));
        eyeIcon.setForeground(Color.DARK_GRAY);

        eyeIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    passwordField.setEchoChar((char) 0); // Show password
                } else {
                    passwordField.setEchoChar('•'); // Hide password
                }
            }
        });

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(eyeIcon, BorderLayout.EAST);
        gradientPanel.add(passwordPanel);

        gradientPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel forgotLabel = new JLabel("<HTML><U>Forgot Password?</U></HTML>");
        forgotLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        forgotLabel.setForeground(Color.WHITE);
        forgotLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        gradientPanel.add(forgotLabel);

        gradientPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(new Color(120, 90, 200));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setMaximumSize(new Dimension(240, 40));
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(120, 90, 200), 2));
        gradientPanel.add(loginButton);

        gradientPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel createLabel = new JLabel("Don't have an account? Sign Up Now");
        createLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        createLabel.setForeground(Color.WHITE);
        createLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gradientPanel.add(createLabel);

        loginButton.addActionListener(e -> authenticate());

        createLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new SignUpWindow();
                dispose();
            }
        });

        add(gradientPanel);
        setVisible(true);
    }

    private void addPlaceholderListener(JTextField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void addPasswordPlaceholderListener(JPasswordField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('•');
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (String.valueOf(field.getPassword()).isEmpty()) {
                    field.setEchoChar((char) 0);
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                dispose();
                if ("admin".equalsIgnoreCase(role)) {
                    new AdminPanel();
                } else {
                    new AppointmentScheduler();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }
}
