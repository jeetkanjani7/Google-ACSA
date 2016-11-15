package com.google.engedu.ghost;

/**
 * Created by apicard on 9/16/15.
 */
public interface GhostDictionary {
    public final static int MIN_WORD_LENGTH = 4;
    boolean isWord(String word);
    String getAnyWordStartingWith(String prefix,Boolean firstTurn);
    String getGoodWordStartingWith(String prefix,Boolean firstTurn);
    public String startingFragment();
}
