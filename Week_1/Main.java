import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class Main {

  static HashMap<String, Integer> wordMap = new HashMap<String, Integer>();

  static HashMap<String, Integer> stopWord = new HashMap<String, Integer>();

  public static void addWord(String word) {
    word = word.toLowerCase();
    if(!stopWord.containsKey(word) && word.length() >= 2) {
      if(wordMap.containsKey(word)) {
        int count = wordMap.get(word);
        count++;
        wordMap.put(word, count);
      }
      else {
        wordMap.put(word, 1);
      }
    }
  }

  public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map) {

    List<Map.Entry<String, Integer>> LList = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

    Collections.sort(LList, new Comparator<Map.Entry<String, Integer>>() {
      public int compare(Map.Entry<String, Integer> v1,
                         Map.Entry<String, Integer> v2) 
      {
        return (v2.getValue()).compareTo(v1.getValue());
      }
    });

    HashMap<String, Integer> sortedList = new LinkedHashMap<String, Integer>();
    
    for(Map.Entry<String, Integer> mapVal : LList) {
      sortedList.put(mapVal.getKey(), mapVal.getValue());
    }

    return sortedList;
  }

  public static void main(String[] args) {
    System.out.println(args[0]);

    File content = new File(args[0]);
    if(content.exists()) {

      try(BufferedReader brStopWord = new BufferedReader(new FileReader("../stop_words.txt"))) {

        System.out.println("File exists!!");

        BufferedReader br = new BufferedReader(new FileReader(content));

        // populate stopWord map
        String line;
        while((line = brStopWord.readLine()) != null) {

          String[] wordList = line.split(",");
          for(int i = 0; i < wordList.length; i++) {
            stopWord.put(wordList[i], 1);
          }

        }

        // read through the book & populate map
        String bookLine;
        while((bookLine = br.readLine()) != null) {

          String replacedLine = bookLine.replaceAll("[^A-Za-z0-9]", ",");
          String[] bookWordList = replacedLine.split(",");
          for(int i = 0; i < bookWordList.length; i++) {
            addWord(bookWordList[i]);
          }

        }

        brStopWord.close();
        br.close();



        //select the top 25 words & print count
        Map<String, Integer> sortedWordMap = sortByValue(wordMap);

        List<String> first25Words = sortedWordMap
                                      .entrySet()
                                      .stream()
                                      .map(Map.Entry::getKey)
                                      .limit(25).collect(Collectors.toList());

        for(String word : first25Words) {
          System.out.println(word + "  -  " + wordMap.get(word));
        }


      } catch(Exception e) {
        e.printStackTrace();
      }

    }
    else {
      System.out.println("Couldn't find file.");
    }
  }
}