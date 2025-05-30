import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SignUpWindow extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JComboBox<String> roleBox;
    JButton signUpButton;

    final String DB_URL = "jdbc:mysql://localhost:3306/school_appointments";
    final String USER = "root";
    final String PASS = "";

    public SignUpWindow() {
        setTitle("Sign Up");
        setSize(400, 550);
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
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 40, 40));

        JLabel title = new JLabel("Create Your Account", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        gradientPanel.add(title);

        gradientPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        usernameField = new JTextField("Username");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        usernameField.setForeground(Color.GRAY);
        usernameField.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        addPlaceholderListener(usernameField, "Username");
        gradientPanel.add(usernameField);
        gradientPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        passwordField = new JPasswordField("Password");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char) 0);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        addPasswordPlaceholderListener(passwordField, "Password");
        gradientPanel.add(passwordField);
        gradientPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        roleBox = new JComboBox<>(new String[]{"student", "admin"});
        roleBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        roleBox.setFont(new Font("SansSerif", Font.PLAIN, 15));
        gradientPanel.add(roleBox);
        gradientPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        signUpButton = new JButton("Sign Up");
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        signUpButton.setBackground(Color.WHITE);
        signUpButton.setForeground(new Color(120, 90, 200));
        signUpButton.setFocusPainted(false);
        signUpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpButton.setMaximumSize(new Dimension(240, 40));
        signUpButton.setBorder(BorderFactory.createLineBorder(new Color(120, 90, 200), 2));
        gradientPanel.add(signUpButton);

        signUpButton.addActionListener(e -> registerUser());

        gradientPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel loginLabel = new JLabel("Already have an account? Login");
        loginLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginWindow();
            }
        });
        gradientPanel.add(loginLabel);

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

    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || username.equals("Username") || password.equals("Password")) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String checkSql = "SELECT * FROM users WHERE username=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, username);
            pstmt.setString(2, password); 
            pstmt.setString(3, role);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                dispose();
                new LoginWindow();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpWindow::new);
    }
}
