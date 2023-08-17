import java.util.ArrayDeque;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.util.Deque;
import java.util.HashSet;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
/**
 * Provides an implementation of the WordLadderGame interface. 
 *
 */
public class Doublets implements WordLadderGame {
   
   // A word ladder with no words. Used as the return value for the ladder methods
   // below when no ladder exists.
   List<String> EMPTY_LADDER = new ArrayList<>();
   // The word list used to validate words.
   // Must be instantiated and populated in the constructor.
   TreeSet<String> lexicon;
   /**
    * Instantiates a new instance of Doublets with the lexicon populated with
    * the strings in the provided InputStream. The InputStream can be formatted
    * in different ways as long as the first string on each line is a word to be
    * stored in the lexicon.
    */
   public Doublets(InputStream in) {
      try {
         lexicon = new TreeSet<String>();
         Scanner s =
            new Scanner(new BufferedReader(new InputStreamReader(in)));
         while (s.hasNext()) {
            String str = s.next();
            lexicon.add(str.toLowerCase());
            s.nextLine();
         }
         in.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }
   ///////////////////////////////////////////////////////////////////////////////
   // Fill in implementations of all the WordLadderGame interface methods here. //
   ///////////////////////////////////////////////////////////////////////////////
   /**
    * Returns the Hamming distance between two strings, str1 and str2. The
    * Hamming distance between two strings of equal length is defined as the
    * number of positions at which the corresponding symbols are different. The
    * Hamming distance is undefined if the strings have different length, and
    * this method returns -1 in that case. See the following link for
    * reference: https://en.wikipedia.org/wiki/Hamming_distance
    *
    * @param  str1 the first string
    * @param  str2 the second string
    * @return      the Hamming distance between str1 and str2 if they are the
    *                  same length, -1 otherwise
    */
   public int getHammingDistance(String str1, String str2) {
      if (str1.length() != str2.length()) 
      {
         return -1;
      }
      str1 = str1.toLowerCase();
      str2 = str2.toLowerCase();
      int dist = 0;
      for (int i = 0; i < str1.length(); i++) 
      {
         if (str1.charAt(i) != str2.charAt(i)) 
         {
            dist++;
         }
      }
      return dist;
   }
  /**
   * Returns a minimum-length word ladder from start to end. If multiple
   * minimum-length word ladders exist, no guarantee is made regarding which
   * one is returned. If no word ladder exists, this method returns an empty
   * list.
   *
   * Breadth-first search must be used in all implementing classes.
   *
   * @param  start  the starting word
   * @param  end    the ending word
   * @return        a minimum length word ladder from start to end
   */
   public List<String> getMinLadder(String start, String end) {
      start = start.toLowerCase();
      end = end.toLowerCase();
      ArrayList<String> backward = new ArrayList<String>();
      List<String> minLadder = new ArrayList<String>();
      if (start.equals(end)) {
         minLadder.add(start);
         return minLadder;
      }
      if (getHammingDistance(start, end) == - 1) {
         return EMPTY_LADDER;
      }
      if(isWord(start) && isWord(end)) {
         backward = breadthFirstSearch(start, end);
      }
      if (backward.isEmpty()) {
         return EMPTY_LADDER;
      }
      for (int i = backward.size() - 1; i >= 0; i--) {
         minLadder.add(backward.get(i));
      }
      return minLadder;
   }
   private ArrayList<String> breadthFirstSearch(String start, String end) {
      Deque<Node> queue = new ArrayDeque<Node>();
      HashSet<String> visited = new HashSet<String>();
      ArrayList<String> backward = new ArrayList<String>();
      visited.add(start);
      queue.addLast(new Node(start, null));
      Node endNode = new Node(end, null);
      outerloop:
      while (!queue.isEmpty()) {
         Node n = queue.removeFirst();
         String w = n.word;
         List<String> neighbors = getNeighbors(w);
         for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
               visited.add(neighbor);
               queue.addLast(new Node(neighbor, n));
               if (neighbor.equals(end)) {
                  endNode.predecessor = n;
                  break outerloop;
               }
            }
         }
      }
      if (endNode.predecessor == null)
      {
         return backward;
      }
      Node m = endNode;
      while (m != null) {
         backward.add(m.word);
         m = m.predecessor;
      }
      return backward;
   }
   /**
    * Returns all the words that have a Hamming distance of one relative to the
    * given word.
    *
    * @param  word the given word
    * @return      the neighbors of the given word
    */
   public List<String> getNeighbors(String word) {
      List<String> neighbor = new ArrayList<String>();
      Iterator<String> itr = lexicon.iterator();
      while (itr.hasNext()) {
         String w2 = itr.next();
         if (getHammingDistance(word, w2) == 1) {
            neighbor.add(w2);
         }
      }
      return neighbor;
   }
   /**
    * Returns the total number of words in the current lexicon.
    *
    * @return number of words in the lexicon
    */
   public int getWordCount() 
   {
      return lexicon.size();
   }
   /**
    * Checks to see if the given string is a word.
    *
    * @param  str the string to check
    * @return     true if str is a word, false otherwise
    */
   public boolean isWord(String str) 
   {
      str = str.toLowerCase();
      if (lexicon.contains(str)) 
      {
         return true;
      }
      return false;
   }
   /**
    * Checks to see if the given sequence of strings is a valid word ladder.
    *
    * @param  sequence the given sequence of strings
    * @return          true if the given sequence is a valid word ladder,
    *                       false otherwise
    */
   public boolean isWordLadder(List<String> sequence) 
   {
      String w1 = "";
      String w2 = "";
      if (sequence.isEmpty()) 
      {
         return false;
      }
      for (int i = 0; i < sequence.size() - 1; i ++) 
      {
         w1 = sequence.get(i);
         w2 = sequence.get(i + 1);
         if (!isWord(w1) || !isWord(w2)) 
         {
            return false;
         } 
         if (getHammingDistance(w1, w2) != 1) 
         {
            return false;
         }
      }
      return true;
   }
   private class Node 
   {
      String word;
      Node predecessor;
   
      public Node(String s, Node pred) 
      {
         word = s;
         predecessor = pred;
      }
   }
}