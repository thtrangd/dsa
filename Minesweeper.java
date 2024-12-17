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
    int numRows = 9;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;

    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JButton playAgainButton = new JButton("Play Again");
    JButton backHomeButton = new JButton("Back Home");

    int mineCount = 10;
    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    Random random = new Random();

    int tilesClicked = 0;
    boolean gameOver = false;

    public Minesweeper() {
        frame.setSize(boardWidth, boardHeight + tileSize * 2);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        textLabel.setForeground(new Color(139, 131, 134));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper: " + mineCount);
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
        frame.setVisible(true);
        setMines();
    }

    void initializeBoard() {
        boardPanel.removeAll();
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Monospaced", Font.PLAIN, 30));
                tile.setForeground(Color.GRAY); // Đặt màu xám riêng cho bông hoa ✿
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) return;

                        MineTile tile = (MineTile) e.getSource();
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.getText().isEmpty()) {
                                if (mineList.contains(tile)) {
                                    revealMines();
                                } else {
                                    checkMine(tile.r, tile.c);
                                }
                            }
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.getText().isEmpty() && tile.isEnabled()) {
                                tile.setText("✿");
                                
                            } else if (tile.getText().equals("✿")) {
                                tile.setText("");
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
            tile.setForeground(new Color(255, 105, 180)); // Màu hot pink cho bom
        }
        gameOver = true;
        textLabel.setText("Ka-boom (~_~メ)");
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
            textLabel.setText("No more boommm ٩(♡ε♡ )۶");
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
        frame.dispose(); // Close the Minesweeper frame
        App.showHomeScreen(); // Return to the home screen
    }
}
