package sample;

import javafx.application.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.InputStreamReader;

public class Controller {

    public class UniqueWord implements Comparable<UniqueWord>
    {
        public String word;
        public int count;

        public UniqueWord(String word, int count)
        {
            this.word = word;
            this.count = count;
        }

        public int compareTo(UniqueWord compareFruit) {

            int compareQuantity = ((UniqueWord) compareFruit).count;

            return compareQuantity - this.count;
        }
    }

    static Controller classRef;

    static HashMap<String, Integer> allUniqueWords;
    public Void offButtonClicked(){
        System.exit(0);
        return null;
    }
    public Void onButtonClicked(){

        try {
            classRef = new Controller();

            URL url = new URL ("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
            InputStreamReader streamReader = new InputStreamReader(url.openStream());
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            //Convert to string
            String fullFile = bufferedReader.lines().parallel().collect(Collectors.joining("\n"));
            bufferedReader.close();

            //Remove Footers and Formatting
            int startIndex = fullFile.indexOf("<h1");
            int endIndex = fullFile.indexOf("<div style='display:block;margin-top:4em'>*** END OF THE PROJECT GUTENBERG EBOOK THE RAVEN ***</div>");
            String minusFooters = fullFile.substring(startIndex, endIndex);

            //Remove Tags
            int contentStart = minusFooters.indexOf(">", 0) + 1;
            int contentEnd = minusFooters.indexOf("<", contentStart);
            String tagless = "";
            while (contentStart >= 0 && contentEnd >= 0)
            {
                tagless = tagless.concat(minusFooters.substring(contentStart, contentEnd));

                contentStart = minusFooters.indexOf(">", contentEnd) + 1;
                contentEnd = minusFooters.indexOf("<", contentStart);
            }

            //Remove hyphens
            contentStart = 0;
            contentEnd = tagless.indexOf("&mdash;", 0);
            String mdashless = "";
            while (contentEnd >= 0)
            {
                mdashless += tagless.substring(contentStart, contentEnd) + "-";

                contentStart = contentEnd + 7;
                contentEnd = tagless.indexOf("&mdash;", contentStart);
            }

            mdashless = mdashless.toLowerCase();

            allUniqueWords = new HashMap<String, Integer>();
            char[] allChars = mdashless.toCharArray();
            boolean wordStarted = false;
            String word = "";
            for (int i = 0; i < allChars.length; i++)
            {
                if(!wordStarted)
                {
                    if(allChars[i] >= 97 && allChars[i] <= 122)
                    {
                        word += allChars[i];
                        wordStarted = true;
                    }
                }
                else
                {
                    if(allChars[i] >= 97 && allChars[i] <= 122 || allChars[i] == 39)
                    {
                        word += allChars[i];
                    }
                    else
                    {
                        if(allUniqueWords.containsKey(word)){
                            allUniqueWords.put(word, allUniqueWords.get(word) + 1);
                        }
                        else{
                            allUniqueWords.put(word, 1);
                        }
                        word = "";
                        wordStarted = false;
                    }
                }
            }

            UniqueWord[] topUniqueWords = new UniqueWord[allUniqueWords.size()];
            Iterator hmIterator = allUniqueWords.entrySet().iterator();
            for(int i = 0; i < topUniqueWords.length; i++)
            {
                Map.Entry mapElement = (Map.Entry)hmIterator.next();
                String keyWord = (String)mapElement.getKey();
                int valueWord = (int)mapElement.getValue();
                topUniqueWords[i] = classRef.new UniqueWord(keyWord, valueWord);
            }
            Arrays.sort(topUniqueWords);

            for(int i = 0; i < 20; i++)
            {
                System.out.println(topUniqueWords[i].word + " : " + topUniqueWords[i].count);
            }
        }
        catch(MalformedURLException e) {
            System.out.println("Incorrect URL: " + e.getMessage());
        }
        catch(IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
        return null;
    }
    public static int GetWordCount(String word)
    {
        return allUniqueWords.get(word);
    }
}
