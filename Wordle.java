import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Wordle {
    private JFrame frame;
    private JPanel gridPanel;
    private JTextField inputField;
    private JButton guessButton;
    private JLabel messageLabel;
    private String wordToGuess;
    private int maxAttempts = 6;
    private int currentAttempt = 0;

    private final String[][] words = {
        {"HONOR", "Respect for others and personal integrity."},
        {"TRUTH", "Being honest and factual in actions and words."},
        {"UNITY", "Working together as one for a common goal."},
        {"PEACE", "Harmony and absence of conflict."},
        {"GRACE", "Simple elegance or courteous goodwill."},
        {"MORAL", "Standards of good behavior and ethics."},
        {"FAITH", "Complete trust or confidence in something."},
        {"HOPES", "A feeling of expectation and desire for a positive outcome."},
        {"LOVES", "Caring and compassion for others selflessly."},
        {"CARES", "Displaying concern and attention towards others."},
        {"SHARE", "Giving part of what you have to others."},
        {"AIDER", "Assisting someone in need or distress."},
        {"SMILE", "A simple act that spreads positivity."}
    };

    public Wordle() {
        // Select a random word from the array
        Random random = new Random();
        int randomIndex = random.nextInt(words.length);
        wordToGuess = words[randomIndex][0];
        String wordDefinition = words[randomIndex][1];

        // Set up the GUI
        frame = new JFrame("Wordle Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        // Grid panel for guesses
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(maxAttempts, 5, 5, 5)); // 5 columns for 5 letters
        for (int i = 0; i < maxAttempts * 5; i++) {
            JLabel cell = new JLabel("", SwingConstants.CENTER);
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cell.setOpaque(true);
            cell.setBackground(Color.WHITE);
            cell.setFont(new Font("Arial", Font.BOLD, 20));
            gridPanel.add(cell);
        }

        // Input field and button
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(10);
        guessButton = new JButton("Guess");
        inputPanel.add(inputField);
        inputPanel.add(guessButton);

        // Message label
        messageLabel = new JLabel("Guess the 5-letter value word!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add components to frame
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.add(messageLabel, BorderLayout.NORTH);

        // Add button action listener
        guessButton.addActionListener(new GuessActionListener(wordDefinition));

        frame.setVisible(true);
    }

    private class GuessActionListener implements ActionListener {
        private final String wordDefinition;

        public GuessActionListener(String wordDefinition) {
            this.wordDefinition = wordDefinition;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String guess = inputField.getText().toUpperCase().trim();
            inputField.setText("");

            if (guess.length() != 5) {
                messageLabel.setText("Word must be exactly 5 letters!");
                return;
            }

            if (currentAttempt >= maxAttempts) {
                messageLabel.setText("Game Over! The word was: " + wordToGuess);
                JOptionPane.showMessageDialog(frame, "Definition: " + wordDefinition);
                inputField.setEnabled(false);
                guessButton.setEnabled(false);
                return;
            }

            char[] wordArray = wordToGuess.toCharArray();
            boolean[] matched = new boolean[5]; // Tracks matched letters for yellow logic

            // First pass: Check for correct (green)
            for (int i = 0; i < 5; i++) {
                JLabel cell = (JLabel) gridPanel.getComponent(currentAttempt * 5 + i);
                cell.setText(String.valueOf(guess.charAt(i)));
                if (wordToGuess.charAt(i) == guess.charAt(i)) {
                    cell.setBackground(new Color(83, 141, 78)); // Green
                    matched[i] = true;
                } else {
                    cell.setBackground(Color.LIGHT_GRAY); // Default to gray
                }
            }

            // Second pass: Check for misplaced (yellow)
            for (int i = 0; i < 5; i++) {
                if (wordToGuess.charAt(i) != guess.charAt(i)) { // Skip greens
                    for (int j = 0; j < 5; j++) {
                        if (guess.charAt(i) == wordArray[j] && !matched[j]) {
                            JLabel cell = (JLabel) gridPanel.getComponent(currentAttempt * 5 + i);
                            cell.setBackground(new Color(181, 159, 59)); // Yellow
                            matched[j] = true; // Mark this letter as used
                            break;
                        }
                    }
                }
            }

            currentAttempt++;

            // Check win/loss
            if (guess.equals(wordToGuess)) {
                messageLabel.setText("Congratulations! You guessed the word!");
                JOptionPane.showMessageDialog(frame, "Definition: " + wordDefinition);
                inputField.setEnabled(false);
                guessButton.setEnabled(false);
            } else if (currentAttempt == maxAttempts) {
                messageLabel.setText("Game Over! The word was: " + wordToGuess);
                JOptionPane.showMessageDialog(frame, "Definition: " + wordDefinition);
                inputField.setEnabled(false);
                guessButton.setEnabled(false);
            } else {
                messageLabel.setText("Try again!");
            }
        }
    }

    public static void main(String[] args) {
        new Wordle();
    }
}
