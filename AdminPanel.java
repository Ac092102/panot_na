import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class AdminPanel extends JFrame {
    JTable appointmentTable;
    DefaultTableModel model;

    final String DB_URL = "jdbc:mysql://localhost:3306/school_appointments";
    final String USER = "root";
    final String PASS = "";

    public AdminPanel() {
        setTitle("Admin Panel - Appointment Management");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel gradientPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(183, 148, 244), 0, getHeight(), new Color(112, 202, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new BorderLayout(20, 20));
        gradientPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        model = new DefaultTableModel(new String[]{"ID", "Name", "Date", "Time", "Reason", "Status"}, 0);
        appointmentTable = new JTable(model);
        styleTable(appointmentTable);

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        JButton approveBtn = createButton("Approve", new Color(76, 175, 80));
        JButton declineBtn = createButton("Decline", new Color(244, 67, 54));
        buttonsPanel.add(approveBtn);
        buttonsPanel.add(declineBtn);

        gradientPanel.add(scrollPane, BorderLayout.CENTER);
        gradientPanel.add(buttonsPanel, BorderLayout.SOUTH);
        add(gradientPanel);

        approveBtn.addActionListener(e -> updateStatus("approved"));
        declineBtn.addActionListener(e -> updateStatus("declined"));

        loadAppointments();
        setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(color.darker());
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(color, 2));
        btn.setPreferredSize(new Dimension(120, 40));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(new Color(220, 220, 220));
        table.setBackground(new Color(255, 255, 255, 230));
        table.setForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(120, 90, 200));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
    }

    private void loadAppointments() {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM appointments WHERE status='pending'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("student_name"),
                    rs.getDate("appointment_date"),
                    rs.getTime("appointment_time"),
                    rs.getString("reason"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStatus(String status) {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow != -1) {
            int appointmentId = (int) model.getValueAt(selectedRow, 0);
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "UPDATE appointments SET status=? WHERE id=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, status);
                pstmt.setInt(2, appointmentId);
                pstmt.executeUpdate();
                loadAppointments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an appointment to update.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminPanel::new);
    }
}
