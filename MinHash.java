package minhash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
 
public class MinHash<T>
{
    private int hash[];
    private int numHash;
     
    public MinHash(int numHash)
    {
        this.numHash = numHash;
        hash = new int[numHash];
        Random r = new Random(11);
        for (int i = 0; i < numHash; i++)
        {
            int a = (int) r.nextInt();
            int b = (int) r.nextInt();
            int c = (int) r.nextInt();
            int x = hash(a * b * c, a, b, c);
            hash[i] = x;
        }
    }
 
    public double similarity(Set<T> set1, Set<T> set2)
    {
        int numSets = 2;
        Map<T, boolean[]> bitMap = buildBitMap(set1, set2);
        int[][] minHashValues = initializeHashBuckets(numSets, numHash);
        computeMinHashForSet(set1, 0, minHashValues, bitMap);
        computeMinHashForSet(set2, 1, minHashValues, bitMap);
        return computeSimilarityFromSignatures(minHashValues, numHash);
    }
 
    private int[][] initializeHashBuckets(int numSets,
            int numHashFunctions)
    {
        int[][] minHashValues = new int[numSets][numHashFunctions];
        for (int i = 0; i < numSets; i++)
        {
            for (int j = 0; j < numHashFunctions; j++)
            {
                minHashValues[i][j] = Integer.MAX_VALUE;
            }
        }
        return minHashValues;
    }
 
    private double computeSimilarityFromSignatures(
            int[][] minHashValues, int numHashFunctions)
    {
        int identicalMinHashes = 0;
        for (int i = 0; i < numHashFunctions; i++)
        {
            if (minHashValues[0][i] == minHashValues[1][i])
            {
                identicalMinHashes++;
            }
        }
        return (1.0 * identicalMinHashes) / numHashFunctions;
    }
 
    private int hash(int x, int a, int b, int c)
    {
        int hashValue = (int) ((a * (x >> 4) + b * x + c) & 131071);
        return Math.abs(hashValue);
    }
 
    private void computeMinHashForSet(Set<T> set, int setIndex,
            int[][] minHashValues, Map<T, boolean[]> bitArray)
    {
        int index = 0;
        for (T element : bitArray.keySet())
        {
            for (int i = 0; i < numHash; i++)
            {
                if (set.contains(element))
                {
                    int hindex = hash[index];
                    if (hindex < minHashValues[setIndex][index])
                    {
                        minHashValues[setIndex][i] = hindex;
                    }
                }
            }
            index++;
        }
    }
 
    public Map<T, boolean[]> buildBitMap(Set<T> set1, Set<T> set2)
    {
        Map<T, boolean[]> bitArray = new HashMap<T, boolean[]>();
        for (T t : set1)
        {
            bitArray.put(t, new boolean[] { true, false });
        }
        for (T t : set2)
        {
            if (bitArray.containsKey(t))
            {
                bitArray.put(t, new boolean[] { true, true });
            }
            else if (!bitArray.containsKey(t))
            {
                bitArray.put(t, new boolean[] { false, true });
            }
        }
        return bitArray;
    }

    public Set<String> createShinglesChar(String text, int charGramLength)
    {
        Set<String> setShingles = new HashSet<String>();
        String processedString = text.toLowerCase();

        for (int i = 0; i < processedString.length() - charGramLength + 1; i++) 
        {
            String shingle = processedString.substring(i, i + charGramLength);
            setShingles.add(shingle);
        }

        return setShingles;        
    }

    public static Set<String> createShinglesWords(String text, int wordGramLength)
    {
        Set<String> setShingles = new HashSet<String>();
        String processedString = text.toLowerCase();

        List<String> words = new ArrayList<String>();

        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher(processedString);
        while (matcher.find()) 
        {
            words.add(matcher.group());
        }    

        for (int i = 0; i < words.size() - wordGramLength + 1; i++)
        {
            String shingle = new String();

            int j = 0;
            for(j=0; j<wordGramLength-1; j++)
            {
                shingle = shingle + words.get(i+j) + " ";
            }
            shingle = shingle + words.get(i+j);

            setShingles.add(shingle);
        }

        return setShingles;
    }

    public int wordCount(String field)
    {
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher(field);
        int count = 0; 
        while (matcher.find()) 
        {
            count = count+1;
        }
        return count;
    }
}