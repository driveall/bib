package bibparser;

import java.io.*;
import java.util.*;

public class BibParser {

    public static void main(String[] args) throws IOException {
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
                flag = s.contains(author);
            }
            if(s.startsWith("title") && flag){
                s = s.substring(s.indexOf('=')+1);
                s = s.trim();
                while(s.startsWith("\"") || s.startsWith("{"))
                    s = s.substring(1);
                while(s.endsWith("\"") || s.endsWith("}") || s.endsWith(","))
                    s = s.substring(0, s.length()-1);
                s = s.trim();
                titles.add(s);
                flag = false;
            }
        }
        reader.close();
        Map<String, Integer> words = new HashMap<>();
        //filling words map
        for(String title : titles){
            while(true){
                String word;
                if(title.indexOf(' ') != -1){
                    word = title.substring(0, title.indexOf(' '));
                    title = title.substring(title.indexOf(' ')+1);
                } else word = title;
                if (words.get(word) == null) {
                    words.put(word, 1);
                }else{
                    Integer oldWordCounter = words.get(word);
                    oldWordCounter++;
                    words.remove(word);
                    words.put(word, oldWordCounter);
                }
                if (word.equals(title)) break;
            }
        }
        List<String> result = new LinkedList<>();
        //forming result words list
        Integer[] wordsUsedTimes = words.values().toArray(new Integer[0]);
        Arrays.sort(wordsUsedTimes);
        List<Integer> usages = new LinkedList<>();
        for (int i = wordsUsedTimes.length - 1; i >= wordsUsedTimes.length / 4 * 3; i--) {
            usages.add(wordsUsedTimes[i]);
        }
        for (String word : words.keySet()) {
            Integer times = words.get(word);
            if (usages.contains(times)) {
                result.add(word);
                usages.remove(times);
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
