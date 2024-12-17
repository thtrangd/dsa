import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class App {
    public static void main(String[] args) {
        JFrame homeFrame = new JFrame("Minesweeper Home");
        homeFrame.setSize(630, 700); // Cùng kích thước giao diện game
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setLocationRelativeTo(null);
        homeFrame.setResizable(false);

        JPanel homePanel = new JPanel();
        homePanel.setForeground(new Color(255, 182, 193)); // Màu hồng nhạt (Light Pink)
        homePanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Minesweeper!");
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        welcomeLabel.setForeground(new Color(139, 131, 134)); // Màu xám nhạt
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        homePanel.add(welcomeLabel, BorderLayout.CENTER);

        JButton playButton = new JButton("Play Game");
        playButton.setFont(new Font("Monospaced", Font.PLAIN, 20));
        playButton.setFocusable(false);
        playButton.setBackground(new Color(255, 240, 245)); // Màu hồng pastel
        playButton.setForeground(new Color(139, 131, 134)); // Màu chữ xám nhạt
        playButton.addActionListener((ActionEvent e) -> {
            homeFrame.dispose(); // Đóng cửa sổ hiện tại
            new Minesweeper();  // Chạy game
        });

        homePanel.add(playButton, BorderLayout.SOUTH);
        homeFrame.add(homePanel);
        homeFrame.setVisible(true);
    }

    public static void showHomeScreen() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showHomeScreen'");
    }
}
