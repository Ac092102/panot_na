import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class AppointmentScheduler extends JFrame {

    JTextField nameField, dateField, timeField;
    JTextArea reasonArea;
    JButton submitButton;

    final String DB_URL = "jdbc:mysql://localhost:3306/school_appointments";
    final String USER = "root"; 
    final String PASS = "";     

    public AppointmentScheduler() {
        setTitle("School Appointment Scheduler");
        setSize(700, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color gradientStart = new Color(0, 153, 255); 
                Color gradientEnd = new Color(102, 0, 204);   
                GradientPaint gp = new GradientPaint(0, 0, gradientStart, 0, getHeight(), gradientEnd);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.92f));
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setPreferredSize(new Dimension(500, 550));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        backgroundPanel.add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("📅 Appointment Scheduling Form");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel nameLabel = new JLabel("👤 Student Name:");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        mainPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        nameField = new JTextField(25);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mainPanel.add(nameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel dateLabel = new JLabel("📅 Date (DD/MM/YYYY):");
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        mainPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        dateField = new JTextField();
        dateField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mainPanel.add(dateField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel timeLabel = new JLabel("⏰ Time (HH:MM:SS):");
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        mainPanel.add(timeLabel, gbc);
        gbc.gridx = 1;
        timeField = new JTextField();
        timeField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mainPanel.add(timeField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel reasonLabel = new JLabel("📝 Reason:");
        reasonLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        mainPanel.add(reasonLabel, gbc);
        gbc.gridx = 1;
        reasonArea = new JTextArea(10, 30);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(reasonArea);
        scrollPane.setPreferredSize(new Dimension(350, 180));
        mainPanel.add(scrollPane, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        submitButton = new JButton("Schedule Appointment");
        submitButton.setBackground(new Color(255, 87, 34));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        submitButton.setFocusPainted(false);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mainPanel.add(submitButton, gbc);

        submitButton.addActionListener(e -> saveAppointment());

        setVisible(true);
    }

    private void saveAppointment() {
        String name = nameField.getText().trim();
        String dateStr = dateField.getText().trim();  // DD/MM/YYYY
        String timeStr = timeField.getText().trim();  // HH:MM:SS
        String reason = reasonArea.getText().trim();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date parsedDate = sdf.parse(dateStr);
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

            java.sql.Time sqlTime = java.sql.Time.valueOf(timeStr);

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "INSERT INTO appointments (student_name, appointment_date, appointment_time, reason, status) VALUES (?, ?, ?, ?, 'pending')";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setDate(2, sqlDate);
                pstmt.setTime(3, sqlTime);
                pstmt.setString(4, reason);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Appointment Scheduled Successfully!");
                    clearFields();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        dateField.setText("");
        timeField.setText("");
        reasonArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppointmentScheduler::new);
    }
}
