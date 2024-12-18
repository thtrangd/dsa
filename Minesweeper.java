import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {
    private class MineTile extends JButton {
        int r;
        int c;

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int tileSize = 70;
    int numRows;
    int numCols;
    int boardWidth;
    int boardHeight;

    JFrame frame = new JFrame("Floral Maze");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JButton playAgainButton = new JButton("Play Again");
    JButton backHomeButton = new JButton("Back Home");

    int mineCount;
    MineTile[][] board;
    ArrayList<MineTile> mineList;
    Random random = new Random();

    int tilesClicked = 0;
    boolean gameOver = false;

    // Constructor now accepts difficulty level
    public Minesweeper(int difficulty) {
        // Adjusting rows, columns, and mine count based on difficulty
        setDifficulty(difficulty);

        frame.setSize(boardWidth, boardHeight + tileSize * 2);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        textLabel.setForeground(new Color(139, 131, 134));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Floral Maze: " + mineCount);
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.CENTER);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols));
        boardPanel.setBackground(new Color(255, 240, 245));
        frame.add(boardPanel, BorderLayout.CENTER);

        playAgainButton.setFont(new Font("Monospaced", Font.PLAIN, 12));
        playAgainButton.setForeground(new Color(139, 131, 134));
        playAgainButton.setPreferredSize(new Dimension(120, 40));
        playAgainButton.setFocusable(false);
        playAgainButton.setEnabled(false);
        playAgainButton.addActionListener(e -> resetGame());

        backHomeButton.setFont(new Font("Monospaced", Font.PLAIN, 12));
        backHomeButton.setForeground(new Color(139, 131, 134));
        backHomeButton.setPreferredSize(new Dimension(120, 40));
        backHomeButton.setFocusable(false);
        backHomeButton.addActionListener(e -> goHome());

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(playAgainButton);
        buttonPanel.add(backHomeButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        initializeBoard();
        setMines();
        frame.setVisible(true);
    }

    // Method to set difficulty settings
    private void setDifficulty(int difficulty) {
        switch (difficulty) {
            case 10:
                numRows = 9;
                numCols = 9;
                mineCount = 10;
                break;
            case 20:
                numRows = 9;
                numCols = 12;
                mineCount = 20;
                break;
            case 30:
                numRows = 9;
                numCols = 14;
                mineCount = 30;
                break;
            default:
                numRows = 9;
                numCols = 9;
                mineCount = 10;
                break;
        }

        boardWidth = numCols * tileSize;
        boardHeight = numRows * tileSize;
    }

    void initializeBoard() {
        boardPanel.removeAll();
        board = new MineTile[numRows][numCols];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Monospaced", Font.PLAIN, 30));
                tile.setForeground(Color.GRAY); // Màu xám cho bông hoa ✿
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) return;

                        MineTile tile = (MineTile) e.getSource();
                        if (e.getButton() == MouseEvent.BUTTON1) {  // Left-click
                            if (tile.getText().isEmpty()) {
                                if (mineList.contains(tile)) {
                                    revealMines(); // Game over, hiện mìn
                                } else {
                                    checkMine(tile.r, tile.c);
                                }
                            }
                        } else if (e.getButton() == MouseEvent.BUTTON3) {  // Right-click
                            if (tile.getText().isEmpty() && tile.isEnabled()) {
                                tile.setText("✿");  // Đánh dấu hoa
                            } else if (tile.getText().equals("✿")) {
                                tile.setText("");  // Xóa dấu hoa
                            }
                        }
                    }
                });
                boardPanel.add(tile);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    void setMines() {
        mineList = new ArrayList<>();
        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft--;
            }
        }
    }

    void revealMines() {
        for (MineTile tile : mineList) {
            tile.setText("❀");
            tile.setForeground(new Color(255, 105, 180));  // Màu hot pink cho mìn
        }
        gameOver = true;
        textLabel.setText("Flower Trap Activated (~_~メ)");
        playAgainButton.setEnabled(true);
    }

    void checkMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) return;

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) return;

        tile.setEnabled(false);
        tilesClicked++;

        int minesFound = 0;
        minesFound += countMine(r - 1, c - 1);
        minesFound += countMine(r - 1, c);
        minesFound += countMine(r - 1, c + 1);
        minesFound += countMine(r, c - 1);
        minesFound += countMine(r, c + 1);
        minesFound += countMine(r + 1, c - 1);
        minesFound += countMine(r + 1, c);
        minesFound += countMine(r + 1, c + 1);

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        } else {
            // Tiếp tục kiểm tra các ô xung quanh nếu không có mìn
            checkMine(r - 1, c - 1);
            checkMine(r - 1, c);
            checkMine(r - 1, c + 1);
            checkMine(r, c - 1);
            checkMine(r, c + 1);
            checkMine(r + 1, c - 1);
            checkMine(r + 1, c);
            checkMine(r + 1, c + 1);
        }

        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            textLabel.setText("Flower Power Unleashed ٩(♡ε♡ )۶");
            playAgainButton.setEnabled(true);
        }
    }

    int countMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) return 0;
        return mineList.contains(board[r][c]) ? 1 : 0;
    }

    void resetGame() {
        tilesClicked = 0;
        gameOver = false;
        playAgainButton.setEnabled(false);
        textLabel.setText("Minesweeper: " + mineCount);
        initializeBoard();
        setMines();
    }

    void goHome() {
        frame.dispose();  // Đóng cửa sổ Minesweeper
        App.showHomeScreen();  // Quay lại màn hình chính
    }
}
