/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength = 3;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            if((sizeToWords.get(word.length()))==null)
            {
                ArrayList<String> words = new ArrayList<String>();
                words.add(word);
                sizeToWords.put(word.length(), words);
            }
            else {
                //ArrayList<String> array = sizeToWords.get(word.length());
                //array.add(word);
                //sizeToWords.put(word.length(), array);
                sizeToWords.get(word.length()).add(word);
            }

            if(lettersToWord.get(sortLetters(word))!=null)
                lettersToWord.get(sortLetters(word)).add(word);
            else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                lettersToWord.put(sortLetters(word), list);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {

        if(!wordSet.contains(word))
            return false;
        if(word.contains(base))
            return false;
        return true;
    }

    public String sortLetters(String word){
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);
        return sorted;

    }

    public List<String> getAnagrams(String targetWord) {
       ArrayList<String> result = new ArrayList<String>();
        String sorted = sortLetters(targetWord);
        for (int i = 0; i != wordList.size(); i++)
        {
            if(sortLetters(wordList.get(i)).equals(sorted) && wordList.get(i).length() == targetWord.length())
            {
                result.add(wordList.get(i));
            }
        }
        return result;

    }


    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabet = new char[26];
        int index = 0;
        for (char c = 'a'; c <= 'z'; c++)
            alphabet[index++] = c;

        for(int i = 0; i <26; i++)
        {
            String sorted = sortLetters(word+alphabet[i]);


                if(lettersToWord.containsKey(sorted) )
                {
                    result.addAll(lettersToWord.get(sorted));
                }


        }

        return result;
    }

   /* public String pickGoodStarterWord() {

        int start = random.nextInt(wordList.size());
        boolean found = false;

        while(found == false)
        {
        if(start >=wordList.size())
            start = 0;
        String word = sortLetters(wordList.get(start));
        ArrayList<String> anagrams = lettersToWord.get(word);
            start++;
            if(anagrams.size() >= MIN_NUM_ANAGRAMS)
            found = true;

         }
        if(wordLength <MAX_WORD_LENGTH)
           wordLength++;
        return wordList.get(start-1);
    }*/

    public String pickGoodStarterWord() {
        ArrayList<String> words = sizeToWords.get(wordLength);
        int start = random.nextInt(words.size());

        while(start<words.size())
        {
            if(start >=words.size())
                start = 0;
            String word = words.get(start);

            if(getAnagramsWithOneMoreLetter(word).size() >= MIN_NUM_ANAGRAMS)
            {
             if(wordLength < MAX_WORD_LENGTH)
                 wordLength++;
                return word;
            }
            start++;
        }

        return words.get(start-1);

    }
    }
