package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    public ArrayList<String> words;
    private int idx = 0;
    private Random random = new Random();
    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(line.trim());
        }
    }
    @Override
    public String startingFragment(){
        //on empty prefix, first turn of computer
        int i ;
        do{
            i = random.nextInt(words.size());
            int len = words.get(i).length();
            if(len>=8 && len%2==0)
                return words.get(i);
        }while (true);
    }
    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }
    private void binarySearch(int start,int end,String prefix){
        if(start>end) return ;
        int mid = (start+end)/2;
        String word = words.get(mid);
        String pre = word.substring(0, Math.min(prefix.length(), word.length()));
        int res = pre.compareToIgnoreCase(prefix);
        if(res>=0){
            idx = mid;
            binarySearch(start, mid - 1, prefix);
        }else{
            binarySearch(mid+1,end,prefix)  ;
        }
    }

    private boolean empty(String str){
        return str==null || str.isEmpty();
    }
    private boolean empty(ArrayList<String> arrayList){
        return arrayList==null || arrayList.isEmpty();
    }
    @Override
    public String getAnyWordStartingWith(String prefix,Boolean firstTurn) {

        //implemented getGoodWordStartingWith here only

        //on empty prefix, first turn of computer
        if(empty(prefix)){
            int i ;
            do{
                i = random.nextInt(words.size());
                int len = words.get(i).length();
                if(len>=6 && len%2==0)
                    return words.get(i);
            }while (true);
        }


        idx = words.size();
        binarySearch(0,words.size()-1,prefix);
        //even Length String
        ArrayList<String> even = new ArrayList<>();
        //odd Length String
        ArrayList<String> odd = new ArrayList<>();
        for(int i=idx;i<words.size();i++){
            String s= words.get(i);
            if(prefix.length()+2>=s.length() && s.length()>=4) continue;
            String pre = s.substring(0,prefix.length());
            int res = pre.compareToIgnoreCase(prefix);
            if(res<0) continue;
            if(res>0) break;
            if(s.length()%2==0)
                even.add(s);
            else
                odd.add(s);
        }
        if( empty(even) && empty(odd) ){
            return  null;
        } else if( !empty(even) && empty(odd) ){
            int i = random.nextInt(even.size());
            return even.get(i);
        }else if( empty(even) && !empty(odd) ){
            int i = random.nextInt(odd.size());
            return odd.get(i);
        }else{
            if(firstTurn){
                int i = random.nextInt(even.size());
                return even.get(i);
            }else{
                int i = random.nextInt(odd.size());
                return odd.get(i);
            }
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix,Boolean firstTurn) {
        return null;
    }
}
