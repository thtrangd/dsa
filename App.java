import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class App {
    public static void main(String[] args) {
        showHomeScreen(); // Hiển thị màn hình chính
    }

    public static void showHomeScreen() {
        // Tạo JFrame chính
        JFrame homeFrame = new JFrame("Floral Maze");
        homeFrame.setSize(630, 700);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setLocationRelativeTo(null);
        homeFrame.setResizable(false);

        // Tạo JPanel chính
        JPanel homePanel = new JPanel();
        homePanel.setBackground(new Color(255, 240, 245)); // Nền hồng pastel
        homePanel.setLayout(new BorderLayout());

        // Tiêu đề
        JLabel welcomeLabel = new JLabel("Enter the Floral Maze!");
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        welcomeLabel.setForeground(new Color(139, 131, 134)); // Màu xám
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        homePanel.add(welcomeLabel, BorderLayout.CENTER);

        // Tạo nút Play Game với nền trắng và khung bo tròn
        JButton playButton = new JButton("<html> Play<br>(≧◡≦)</html>");
        playButton.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Font giống Back Home
        playButton.setForeground(new Color(139, 131, 134)); // Màu chữ giống Back Home
        playButton.setPreferredSize(new Dimension(120, 40)); // Kích thước giống Back Home
        playButton.setFocusable(false); // Loại bỏ viền focus khi click
        playButton.setBackground(Color.WHITE); // Màu nền trắng
        playButton.setBorder(BorderFactory.createLineBorder(new Color(139, 131, 134), 1, true)); // Viền bo tròn (1px)
        playButton.setOpaque(true); // Đảm bảo nền được hiển thị

        // Sự kiện khi nhấn Play
        playButton.addActionListener((ActionEvent e) -> {
            homeFrame.dispose(); // Đóng màn hình chính
            new Minesweeper();   // Chạy Minesweeper
        });

        // Panel chứa nút Play
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 240, 245));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        buttonPanel.add(playButton);

        homePanel.add(buttonPanel, BorderLayout.SOUTH);
        homeFrame.add(homePanel);

        // Hiển thị giao diện
        homeFrame.setVisible(true);
    }
}
