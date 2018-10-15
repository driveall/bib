package bibparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BibParser {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("Enter author name:");
        String author = new Scanner(System.in).nextLine().toLowerCase();
        File f = new File("big.bib");
        List<String> titles = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String s;
        boolean flag = false;
        //parsing file
        while((s = reader.readLine()) != null){
            s = s.trim().toLowerCase();
            if(s.startsWith("author")){
                s = s.substring(6);
                s = s.trim();
                if(s.contains(author))
                    flag = true;
                else
                    flag = false;
            }
            if(s.startsWith("title") && flag){
                s = s.substring(s.indexOf('=')+1);
                s = s.trim();
                while(s.startsWith("\"") || s.startsWith("{"))
                    s = s.substring(1);
                while(s.endsWith("\"") || s.endsWith("}") || s.endsWith(","))
                    s = s.substring(0, s.length()-1);
                titles.add(s);
            }
        }
        reader.close();
        Map<String, Integer> words = new HashMap<>();
        //filling words map
        for(String title : titles){
            while(true){
                if(title.indexOf(' ') != -1){
                    String word = title.substring(0, title.indexOf(' '));
                    if(words.get(word) == null){
                        words.put(word, 1);
                    }else{
                        Integer oldWordCounter = words.get(word);
                        oldWordCounter++;
                        words.remove(word);
                        words.put(word, oldWordCounter);
                    }
                    title = title.substring(title.indexOf(' ')+1);
                }else{
                    if(words.get(title) == null){
                        words.put(title, 1);
                    }else{
                        Integer oldWordCounter = words.get(title);
                        oldWordCounter++;
                        words.remove(title);
                        words.put(title, oldWordCounter);
                    }
                    break;
                }
            }
        }
        List<String> result = new LinkedList<>();
        //forming result
        Integer[] wordsUsedTimes = words.values().toArray(new Integer[words.values().size()]);
        Arrays.sort(wordsUsedTimes);
        Integer barrier = wordsUsedTimes[wordsUsedTimes.length/4*3];
        for(String w : words.keySet()){
            if(words.get(w) >= barrier){
                result.add(w);
            }
        }
        //filling titles file
        File formedTitles = new File("titles.txt");
        formedTitles.createNewFile();
        PrintWriter writer = new PrintWriter(formedTitles);
        for(String title : titles){
            writer.println(title);
        }
        writer.close();
        //filling words file
        File formedWords = new File("words.txt");
        formedWords.createNewFile();
        writer = new PrintWriter(formedWords);
        for(String word : result){
            writer.println(word);
        }
        writer.close();
    }
    
}
