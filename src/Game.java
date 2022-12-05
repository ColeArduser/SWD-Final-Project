import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Game implements Runnable { //TODO: make class abstract and children runnable maybe?
    ArrayList<String> wordsToChooseFrom = new ArrayList<>();
    private String gamemode;
    private File filePath = null;
    private int fileIndex;
    private String selectedWord;
    private final ArrayList<Character> letters = new ArrayList<>();
    final static int[] alphabetScores = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
    private ArrayList<String> validWords = new ArrayList<>();
    private final ArrayList<String> allWords = new ArrayList<>();
    private int[] letterFreq = new int[26];
    private boolean progressFlag = false;
    private boolean endFlag = false;
    private boolean startFlag = false;
    private int numConnectedClients = 0;
    private int countDownTime;
    private boolean preGameLobbyFlag = false;
    private long lobbyStartTime;
    private int matchTime;

    public int getMatchTime() {
        return matchTime;
    }

    public void setPreGameLobbyFlag(boolean preGameLobbyFlag) {
        this.preGameLobbyFlag = preGameLobbyFlag;
    }

    public long getLobbyStartTime() {
        return lobbyStartTime;
    }

    public void setLobbyStartTime(long lobbyStartTime) {
        this.lobbyStartTime = lobbyStartTime;
    }

    public void setCountDownTime(int countDownTime) {
        this.countDownTime = countDownTime;
    }

    public int getCountDownTime() {
        return countDownTime;
    }

    public boolean isInProgress() {
        return progressFlag;
    }
    public boolean isFinished(){
        return endFlag;
    }
    public boolean hasStarted(){
        return startFlag;
    }

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public ArrayList<Character> getLetters() {
        return letters;
    }

    public int getNumConnectedClients() {
        return numConnectedClients;
    }

    public void changeProgressFlag() {
        progressFlag = !progressFlag;
    }

    public void changeEndFlag() {
        endFlag = !endFlag;
    }

    public void changeStartFlag() {
        startFlag = !startFlag;
    }

    public void clientDisconnected() {
        numConnectedClients--;
    }

    public Game(int matchTime) {
        this.matchTime = matchTime;
    }

    public Game(int matchTime, File filePath, int fileIndex) {
        this.matchTime = matchTime;
        this.filePath = filePath;
        this.fileIndex = fileIndex;
    }

    public void clientConnected() {
        numConnectedClients++;
    }

    @Override
    public void run() {
        if (filePath == null) {
            normalGame();
        } else {
            TAGame();
        }
        try {
            pregameLobby();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        startGame();
    }

    private void normalGame() {
        readAllWords();
        selectWord();
        generateLetters();
        addAdditionalLetters();
        Collections.sort(letters);
        findValidWords();
    }

    private void TAGame() {
        readLetterFile();
        readAllWords();
        selectedWord = wordsToChooseFrom.get(fileIndex);
        generateLetters();
        //Collections.sort(letters);
        findValidWords();
    }

    private void readLetterFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                wordsToChooseFrom.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAllWords() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("words"));
            String line = reader.readLine();
            while (line != null) {
                if (line.length() > 2 && line.length() < 9) {
                    allWords.add(line);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectWord() {
        for (String word : allWords) {
            if (word.length() > 5) {
                wordsToChooseFrom.add(word);
            }
        }
        int selectedIndex = (int) (Math.random() * wordsToChooseFrom.size());

        selectedWord = wordsToChooseFrom.get(selectedIndex);
    }

    private void generateLetters() {
        for (char letter : selectedWord.toCharArray()) {
            letters.add(letter);
        }
    }

    private void addAdditionalLetters() {
        int numAddedLetters = (int) (Math.random() * 4);
        for (int i = 0; i < numAddedLetters; i++) {
            letters.add((char) ((Math.random() * 26) + 97));
        }
    }

    private void findValidWords() {
        validWords = new ArrayList<>();
        letterFreq = findLetterFreq(String.valueOf(letters).replaceAll("[,\\s\\[\\]]", ""));
        for (String word : allWords) {
            int[] tempFreq = findLetterFreq(word);
            if (findMatch(tempFreq)) {
                validWords.add(word);
            }
        }
    }

    private boolean findMatch(int[] tempFreq) {
        for (int i = 0; i < 26; i++) {
            if (letterFreq[i] == 0 && tempFreq[i] > 0) {
                return false;
            } else if (letterFreq[i] < tempFreq[i]) {
                return false;
            }
        }
        return true;
    }

    private int[] findLetterFreq(String word) {
        int[] freq = new int[26];
        for (char letter : word.toCharArray()) {
            freq[letter - 'a']++;
        }
        return freq;
    }

    public int guess(String guess) {
        if (validWords.contains(guess.toLowerCase())) {
            int scoreSum = 0;
            for (Character c: guess.toCharArray()) {
                scoreSum += alphabetScores[Character.toUpperCase(c) - 65];
            }
            scoreSum += guess.length();
            return scoreSum;
        }
        return 0;
    }

    public abstract void pregameLobby() throws InterruptedException;

    public abstract void startGame();

    public String getTournamentName() {
        return "";
    }


    //TODO: rounds can be specified as special rule set in subclasses

}
