package com.google.engedu.ghost;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class TrieNode {

    private HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode root = this;
        for (int i=0;i<s.length();i++) {
            char c=s.charAt(i);
            if (root.children.containsKey(c)) {
                root=root.children.get(c);
            }else{
                TrieNode temp = new TrieNode();
                root.children.put(c,temp);
                root = temp;
            }
        }
        root.isWord=true;
    }

    public boolean isWord(String s) {
        TrieNode root = this;
        for (int i=0;i<s.length();i++) {
            char c=s.charAt(i);
            if(!root.children.containsKey(c)) {
                return false;
            }
            root=root.children.get(c);
        }
        return  root.isWord;
    }

    public String getAnyWordStartingWith(String prefix) {
        TrieNode root = this;
        for (int i=0;i<prefix.length();i++) {
            char c=prefix.charAt(i);
            if (root.children.containsKey(c)) {
                root=root.children.get(c);
            }else{
                return  null;
            }
        }
        String str=prefix;
        while (!root.isWord){
            List<Character> keysAsArray = new ArrayList<>(root.children.keySet());
            Character c = keysAsArray.get((new Random()).nextInt(keysAsArray.size()));
            str=str+Character.toString(c);
            root=root.children.get(c);
        }
        return str;
    }
    public String getGoodWord(TrieNode root,String curr,boolean turn) {
        if (root==null)
            return null;
        for(Character c: root.children.keySet()) {
            String temp = curr+Character.toString(c);
            if (root.isWord) {

                if(turn)
                    if(temp.length()%2==0){
                        Log.i("TrieNode","getGoodWord() : "+temp+" "+turn);
                        return temp;
                    }
                else
                    if(temp.length()%2!=0){
                        Log.i("TrieNode","getGoodWord() : "+temp+" "+turn);
                        return temp;
                    }
            }
            return getGoodWord(root.children.get(c), temp, turn);
        }
        return null;
    }

    public String getGoodWordStartingWith(String s,Boolean firstTurn) {

        TrieNode root = this;
        for (int i=0;i<s.length();i++) {
            char c=s.charAt(i);
            if (root.children.containsKey(c)) {
                root=root.children.get(c);
            }else{
                return null;
            }
        }
        Log.i("TrieNode", "Calling to getGoodWord() with prefix"+ s+" "+firstTurn);
        String  ans =  getGoodWord(root,s, firstTurn);
        Log.i("TrieNode","getGoodWordStartingWith()  : "+(ans==null?"":ans));
        return  ans;
    }
    public String startingFragment(){
        TrieNode root = this;
        String str="";
        for (int i=0;i<4;i++){
            List<Character> keysAsArray = new ArrayList<>(root.children.keySet());
            Character c = keysAsArray.get((new Random()).nextInt(keysAsArray.size()));
            str=str+Character.toString(c);
            root=root.children.get(c);
        }
        Log.i("TrieNode","starting Fragment : "+str);
        return str;
    }
}
