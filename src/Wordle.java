import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.BorderLayout;



public class Wordle extends JFrame{
    // creating fields (everything in the  is a field)
    private JPanel guessesPanel;
    private JTextField inputField;
    private JLabel messageLabel;
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
        String secretWord = wordList.get(new Random().nextInt(wordList.size()));
        System.out.println("Secret Word: " + secretWord);
        
        
        // GUI Creation
        setTitle("Wordle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // gui closed -> program ends
        setSize(350,500);
        

        guessesPanel = new JPanel();
        guessesPanel.setLayout(new BoxLayout(guessesPanel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel();
        inputField = new JTextField(10);
        JButton guessButton = new JButton("Guess");
        // TODO: listen actioner
        inputPanel.add(inputField);
        inputPanel.add(guessButton);

        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

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
}
