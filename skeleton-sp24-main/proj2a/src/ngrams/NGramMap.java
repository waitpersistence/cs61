package ngrams;

import edu.princeton.cs.algs4.In;
import jdk.jfr.Frequency;

import java.util.*;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;
import static utils.Utils.SHORT_WORDS_FILE;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    // TODO: Add any necessary static/instance variables.
    /*每个单词只对应一个 TimeSeries 对象，而
     这个 TimeSeries 对象内部是用 TreeMap<Integer, Double> 来管理每个年份的数据。
     因此，同一个单词在不同年份的数据会被存储在同一个 TimeSeries 对象中，而不会覆盖其他年份的数据.
    */
    public HashMap<String,TimeSeries> wordToTimeSeries ;//先找word timeseires代表的是一系列时间
    private HashMap<Integer,Long> countdata;;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {

        // TODO: Fill in this constructor. See the "NGramMap Tips" section of the spec for help.
        wordToTimeSeries = new HashMap<>();
        countdata = new HashMap<>();
        In in = new In(wordsFilename);//读文件
        int i = 0;
        while (!in.isEmpty()) {
            i += 1;
            String nextLine = in.readLine();
            String[] splitLine = nextLine.split("\t");
            if(wordToTimeSeries.containsKey(splitLine[0])){
            TimeSeries ts = wordToTimeSeries.get(splitLine[0]);
            ts.put(Integer.valueOf(splitLine[1]),Double.valueOf(splitLine[2]));
            wordToTimeSeries.put(splitLine[0],ts);
            }else{
                TimeSeries ts = new TimeSeries();
                ts.put(Integer.valueOf(splitLine[1]),Double.valueOf(splitLine[2]));
                wordToTimeSeries.put(splitLine[0],ts);
            }
            //System.out.print("Line " + i + " is: ");
            //System.out.println(nextLine);
            //System.out.print("After splitting on tab characters, the first word is: ");
            //String[] splitLine = nextLine.split("\t");
            //System.out.println(splitLine[0]);
        }
        in = new In(countsFilename);
        i = 0;
        while (!in.isEmpty()) {
            i += 1;
            String nextLine = in.readLine();
            String[] splitLine = nextLine.split(",");
            countdata.put(Integer.valueOf(splitLine[0]),Long.valueOf(splitLine[1]));
        }


    }


    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries cs = new TimeSeries();
        if(wordToTimeSeries.containsKey(word)){
        TimeSeries as = wordToTimeSeries.get(word);
        //深拷贝
        for(Integer year: as.keySet()){
            if(startYear<=year&&year<=endYear){
            cs.put(year,as.get(year));
        }
        }
        }


        return cs;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    // TODO: Fill in this method.
    public TimeSeries countHistory(String word) {
        TimeSeries cs = new TimeSeries();
        if(wordToTimeSeries.containsKey(word)){
            TimeSeries as = wordToTimeSeries.get(word);
            //深拷贝
            for(Integer year: as.keySet()){
                cs.put(year,as.get(year));
            }
        }
        return cs;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        // TODO: Fill in this method.
        TimeSeries cs = new TimeSeries();
        for(Integer year : countdata.keySet()){
            cs.put(year,Double.valueOf(countdata.get(year)));
        }
        return cs;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries cs = countHistory(word,startYear,endYear);
        TimeSeries cs1 = new TimeSeries();
        //计算出frequency
        if(wordToTimeSeries.containsKey(word)) {
            for(Integer year:cs.keySet()){
                if(year>=startYear&&year<=endYear){
                    Double frequency = cs.get(year) / totalCountHistory().get(year);
                    cs1.put(year,frequency);
                }
            }
        }
        return cs1;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        // TODO: Fill in this method.
        TimeSeries cs = countHistory(word);
        TimeSeries cs1 = new TimeSeries();
        //计算出frequency
        if(wordToTimeSeries.containsKey(word)) {
            for(Integer year:cs.keySet()){
                    Double frequency = cs.get(year) / totalCountHistory().get(year);
                    cs1.put(year,frequency);
                }
            }
        return cs1;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries cs = new TimeSeries();
        for(String word : words){
            cs = cs.plus(weightHistory(word,startYear,endYear));
        }
        return cs;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        // TODO: Fill in this method.
        TimeSeries cs = new TimeSeries();
        for(String word : words){
            cs = cs.plus(weightHistory(word));
        }
        return cs;

    }

    // TODO: Add any private helper methods.
    // TODO: Remove all TODO comments before submitting.
}
