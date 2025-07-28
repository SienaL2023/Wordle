import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;



public class Wordle extends JFrame{
    // creating fields (everything in the  is a field)
    private JPanel guessesPanel;
    private JTextField inputField;
    private JLabel messageLabel; // label is any text that can show up on gui
    private String secretWord;
    private static final int MAX_TRIES = 6; // constants r all caps
    private int tries = 0;
    private List<String> wordList;

    public Wordle(){
        List<String> wordList = loadDictionary("C://Users//xiaop//OneDrive//Wordle//src/dictionary.txt");
        System.out.println("loaded words: " + wordList.size());
        
        // if dictionary is empty
        if(wordList.isEmpty()){
            JOptionPane.showMessageDialog(this, "Dictionary file is empty :(");
            System.exit(1);
        }

        // if dictionary isn't empty
        secretWord = wordList.get(new Random().nextInt(wordList.size()));
        // System.out.println("Secret Word: " + secretWord);
        
        
        // GUI Creation
        setTitle("Wordle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // gui closed -> program ends
        setSize(350,500);
        

        guessesPanel = new JPanel();
        guessesPanel.setLayout(new BoxLayout(guessesPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(guessesPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputField = new JTextField(10);
        JButton guessButton = new JButton("Guess");
        guessButton.addActionListener(e -> handleGuess()); // e = event
        inputPanel.add(inputField);
        inputPanel.add(guessButton);

        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // center centers things

        add(inputPanel, BorderLayout.SOUTH);
        add(messageLabel, BorderLayout.NORTH);

        setVisible(true);
    }

    // helper method to read dictionary.txt
    private List<String> loadDictionary(String filename){
        List<String> words = new ArrayList<>();
        
        // try and catch, try something, if it crashes it'll go straight to the catch
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = br.readLine()) != null){
                line = line.trim().toLowerCase();
                if(line.length() == 5){
                    words.add(line);
                }
            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
        return words;
    }

    // logic of wordle
    // capture user's input
    // compare it with secret word, letter by letter

    // example: juice (answer)
    // elect
    void handleGuess(){
        String guess = inputField.getText().toLowerCase();
        
        // check word length
        if(guess.length() != 5){
            messageLabel.setText("Please enter a 5-letter word!!");
            return;
        }

        // if(!wordList.contains(guess)){
        //     messageLabel.setText("Not a word in our tiny list :(");
        //     return;
        // }

        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        boolean[] secretUsed = new boolean[5]; // determine which letters r right or wrong
        JLabel [] labels = new JLabel[5];

        // first pass: correct position
        for(int i = 0; i < 5; i++){
            // makes the letters
            JLabel letterLabel = new JLabel(String.valueOf(guess.charAt(i)).toUpperCase(), SwingConstants.CENTER);
            
            // making box
            letterLabel.setOpaque(true);
            letterLabel.setPreferredSize(new Dimension(40,40));
            letterLabel.setFont(new Font("Arial", Font.BOLD, 20));
            letterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            if(guess.charAt(i) == secretWord.charAt(i)){
                // make box green
                // display that letter
                letterLabel.setBackground(Color.GREEN);
                secretUsed[i] = true;
            }

            else{
                letterLabel.setBackground(Color.LIGHT_GRAY);
            }
            // adding the exact letter to the label to show on screen
            labels[i] = letterLabel; 
            rowPanel.add(letterLabel);
        }

        // second pass: correct letter wrong placement (yellow)
        for(int i = 0; i < 5; i++){
            // check gray letters
            if(labels[i].getBackground() == Color.LIGHT_GRAY){
                boolean found = false;
                for(int j = 0; j < 5; j++){
                    if(!secretUsed[j] && secretWord.charAt(j) == guess.charAt(i)){
                        found = true;
                        secretUsed[j] = true;
                        break;
                    }
                }
                if(found){
                    labels[i].setBackground(Color.YELLOW);
                }

            }
        }

        guessesPanel.add(rowPanel);
        guessesPanel.revalidate();
        guessesPanel.repaint();

        tries++;

        // check winning or loosing condition
        if(guess.equals(secretWord)){
            messageLabel.setText("Congrats! You guessed the word!");
            inputField.setEnabled(false);
        }
        else if(tries >= MAX_TRIES){
            messageLabel.setText("Out of tries. the word was: " + secretWord);
            inputField.setEnabled(false);
        }
        else{
            messageLabel.setText(" ");
        }

        inputField.setText("");
    }

}
