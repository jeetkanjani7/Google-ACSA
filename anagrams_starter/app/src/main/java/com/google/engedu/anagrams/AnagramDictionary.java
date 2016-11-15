package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static ArrayList<String> wordList = new ArrayList<>();
    private static HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
    private static HashSet<String> hashSet = new HashSet<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        Log.i("AnagramDictionary()","Load Dictionary");
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            String temp = word;
            //add word to hashSet
            hashSet.add(word);
            //prepare hashMap
            ArrayList<String> arrayList = new ArrayList<>();
            if (hashMap.containsKey(sortLetters(temp)))
                arrayList = hashMap.get(sortLetters(temp));
            arrayList.add(word);
            hashMap.put(sortLetters(word), arrayList);
        }
        Log.i("AnagramDictionary()","Dictionary : "+hashSet);
    }
    //check word is good or not
    public boolean isGoodWord(String word, String base) {

        Log.i("isGoodWord()", "word : " + word + " base : " + base);
        //check for empty word
        if (word == null || word.isEmpty()) {
            Log.i("isGoodWord()", "Empty word ");
            return false;
        }
        //check for empty base
        if (base == null || base.isEmpty()) {
            Log.i("isGoodWord()", "Empty base ");
            return false;
        }
        //check word in dictionary
        if (!hashSet.contains(word)) {
            Log.i("isGoodWord()", "Invalid word ");
            return false;
        }
        //check extra character padding
        if (word.length() != base.length() + 1) {
            if (word.length() <= base.length()) {
                Log.i("isGoodWord()", "word does not contain extra character ");
                return false;
            }
            Log.i("isGoodWord()", "word contains two or more extra character ");
            return false;
        }
        //check for substring as suffix or prefix
        String prefix = word.substring(0, word.length() - 1);
        String suffix = word.substring(1, word.length());
        if (prefix.equalsIgnoreCase(base) || suffix.equalsIgnoreCase(base)) {
            Log.i("isGoodWord()", "word contains substring as base word");
            return false;
        }
        Log.i("isGoodWord()", "word is good word");
        return true;
    }
    //To sort String
    private String sortLetters(String string) {
        //Log.i("sortLetters()", "Given String " + string);
        //convert string to character array
        char[] chars = string.toCharArray();
        //sort character array
        Arrays.sort(chars);
        //convert character array to string back
        String sorted = new String(chars);
        //Log.i("sortLetters()", "Sorted String " + sorted);
        return sorted;
    }
    //Get anagram of target word
    public ArrayList<String> getAnagrams(String targetWord) {

        String sortedString = sortLetters(targetWord);
       // Log.i("getAnagrams()", "Given String :" + targetWord+"Sorted String :" + sortedString);

        ArrayList<String> anagramList = hashMap.get(sortedString);
        if(anagramList==null || anagramList.isEmpty()){
  //          Log.i("getAnagrams()", "Empty Anagram List  ");
            return null;
        }
//        Log.i("getAnagrams()", "anagramList : "+anagramList.toString());

        //return all anagram of targetWord from dictionary
        return anagramList;
    }
    //get anagram with one word
    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {

        Log.i("GAWOneMoreLetter()", "Given String :" + word);

        ArrayList<String> arrayList = new ArrayList<>();
        //check existence of word
        if (!hashSet.contains(word)) {
            Log.i("GAWOneMoreLetter()", "Invalid word :" + word);
            return arrayList;
        }
        ArrayList<String> listOfOneExtraWord = new ArrayList<>();
        //create all word with one padding one character at beginning of given word
        for (char c = 'a'; c <= 'z'; c++) {
            String temp = Character.toString(c) + word;
            listOfOneExtraWord.add(temp);
        }
        //create all word with one padding one character at ending of given word
        for (char c = 'a'; c <= 'z'; c++) {
            String temp = word + Character.toString(c);
            listOfOneExtraWord.add(temp);
        }
        Log.i("GAWOneMoreLetter()", "listOfOneExtraWord :" + listOfOneExtraWord.toString());
        //get all anagram word from hashMap
        ArrayList<String> noAnagramWordList = new ArrayList<>();
        ArrayList<String> anagramWordList = new ArrayList<>();
        for (String temp : listOfOneExtraWord) {
            ArrayList<String> anagrams = getAnagrams(temp);
            if(anagrams == null || anagrams.isEmpty()){
                noAnagramWordList.add(temp);
                continue;
            }
            anagramWordList.add(temp);
            for (String currAnagram : anagrams)
                if (isGoodWord(currAnagram, word))
                    arrayList.add(currAnagram);
        }
        if(noAnagramWordList!=null && noAnagramWordList.isEmpty())
            Log.i("GAWOneMoreLetter()","No Anagram for "+noAnagramWordList);
        if(anagramWordList!=null && anagramWordList.isEmpty())
            Log.i("GAWOneMoreLetter()","Anagram for "+anagramWordList);

        Log.i("GAWOneMoreLetter()", "Anagrams : "+arrayList.toString());
        return arrayList;
    }

    public String pickGoodStarterWord() {
        return "stop";
    }
}