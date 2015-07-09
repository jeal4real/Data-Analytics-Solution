import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.SortedMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.ArrayList;

public class TweetAnalytics {

    private File file;
    private FileReader fileReader;
    private FileWriter fileWriter;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private SortedMap<String, Integer> wordFrequency;
    private ArrayList<Double> medianList;

    public void analyseTweet(String inputDir, String outputDir) {

        int tweetSN = 0; //tweet serial number
        int distinctWordCounter, distinctWordSum = 0;
        String word, tweet = "";
        String[] tweetWords;
        medianList = new ArrayList<>();
        wordFrequency = new TreeMap<>(); //This helps sort result

        //Validate parameters
        if ((inputDir == null || inputDir.isEmpty()) || (outputDir == null || outputDir.isEmpty())) {
            System.out.println("Invalid parameters!!! Enter correct inputDir and outputDir");
            return;
        }
		
        file = new File(inputDir + "tweets.txt");

        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException fnfExc) {
            System.out.println("Tweet source file is not found. Confirm dir: " + file.getAbsolutePath());
            return;
        }
		
        bufferedReader = new BufferedReader(fileReader);

        do {
			
            try {
                tweet = bufferedReader.readLine();
            } catch (IOException ioExc) {
                System.out.println("An error encountered while reading tweet file: " + file.getAbsolutePath());
                break;
            }

            if (tweet != null && !tweet.equals("")) {
                tweetWords = tweet.split(" ");
                distinctWordCounter = new HashSet<String>(Arrays.asList(tweetWords)).size();
                distinctWordSum += distinctWordCounter;

                tweetSN++;
                medianList.add((double) (distinctWordSum) / tweetSN);

                for (int x = 0; x < tweetWords.length; x++) {
                    word = tweetWords[x];

                    if (!wordFrequency.containsKey(word)) {
                        wordFrequency.put(word, 1);
                    } else {
                        wordFrequency.put(word, wordFrequency.get(word) + 1);
                    }
                }
            }
        } while (tweet != null);
		//Done reading file, close the resources used
        closeInStreams();

        writeFrequencyFile(outputDir);
        writeMedianFile(outputDir);

    }
    
    private void writeFrequencyFile(String outputDir){
        
        file = new File(outputDir + "ft1.txt");
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException ioExc) {
            System.out.println("An error encountered while creating output file: " + file.getAbsolutePath());
        }

        bufferedWriter = new BufferedWriter(fileWriter);
        for (String str : wordFrequency.keySet()) {
            try {
                bufferedWriter.write(String.format("%-25s %d", str, wordFrequency.get(str)));
                bufferedWriter.newLine();
            } catch (IOException ioExc) {
                System.out.println("An error encountered while writing to output file: " + file.getAbsolutePath());
            }
        }
        try {
            bufferedWriter.flush();
        } catch (IOException ioExc) {
        }
        closeOutStreams();
    }
    
    private void writeMedianFile(String outputDir){
        
        file = new File(outputDir + "ft2.txt");
        
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException ioExc) {
            System.out.println("An error encountered while creating output file: " + file.getAbsolutePath());
        }

        bufferedWriter = new BufferedWriter(fileWriter);
        for (int x = 0; x < medianList.size(); x++) {
            try {
                bufferedWriter.write(medianList.get(x).toString());
                bufferedWriter.newLine();
            } catch (IOException ioExc) {
                System.out.println("An error encountered while writing to output file: " + file.getAbsolutePath());
            }
        }
        try {
            bufferedWriter.flush();
        } catch (IOException ioExc) {
        }
        closeOutStreams();
    }

    public void closeInStreams() {

        try {
            bufferedReader.close();
        } catch (IOException ioExc) {
        }
        try {
            fileReader.close();
        } catch (IOException ioExc) {
        }
    }

    public void closeOutStreams() {

        try {
            bufferedWriter.close();
        } catch (IOException ioExc) {
        }
        try {
            fileWriter.close();
        } catch (IOException ioExc) {
        }
    }

    public static void main(String[] args) {
        TweetAnalytics solnObj = new TweetAnalytics();
        solnObj.analyseTweet("..\\tweet_input\\", "..\\tweet_output\\");
    }
}
