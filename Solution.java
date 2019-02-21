import java.io.*;
import java.util.*;

public class Solution {
    /** @return boolean indicating if two Strings are isomorphic.
        Stores mapped characters in map and checked
    */
    static boolean mapStr(String c, String m){
        c = c.toLowerCase();
        m = m.toLowerCase();
        if(c.length() != m.length())
            return false;
        for(int i = 0; i < c.length(); i++){
            if(map[c.charAt(i)-97] != -1){//Has c[i] already been mapped?
                if(map[c.charAt(i)-97] != m.charAt(i)){//To the right value?
                    return false;
                }
            }
            else{
                if(checked[m.charAt(i) - 97]){//Does m[i] have another char it's mapped to?
                    return false;
                }
                checked[m.charAt(i) - 97] = true;//m[i] is now checked
                map[c.charAt(i)-97] = m.charAt(i);//c[i] is now mapped to m[i]
            }
        }
        return true;
    }
    /**@return boolean indicating if a given word in a message can be mapped given previous state of map and checked
    @precondition m[i] for i's 0 through wordNum - 1 are mapped
    Stores mapped characters in map and checked*/
    static boolean mapDict(List<String> dict, String[] m, int start, int wordNum){
        //So that the inital state can be altered and then referenced.
        int[] initialMap = map.clone(); 
        boolean[] initialChecked = checked.clone();
        for(int i = start; i < dict.size(); i++){
            //Resetting map and checked (static fields)
            map = initialMap.clone();
            checked = initialChecked.clone();    
            if(mapStr(m[wordNum], dict.get(i))){//Can we map m[wordNum] to the ith word in dict?
                if(wordNum == m.length - 1){//Is it the last word?
                    isDone = true;//We're done!
                    return false;
                }
                starts[wordNum] = i+1;//So we know where to start from if this branch doesn't work out
                Arrays.fill(starts, wordNum+1, starts.length, 0);//So previous values for start for different branches are erased
                return true;
            }
        }
        return false;
    }
    static int[] map = new int[26];//ASCII values that each char is mapped to
    static boolean[] checked = new boolean[26];//Are characters already mapped?
    static int[] starts;//Where to start
    static boolean isDone = false;//Is our loop done?
    public static void main(String[] args) throws IOException{
        List<String> dict = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader("dictionary.lst"));
        String word = br.readLine();
        while(word!=null){
            dict.add(word);
            word = br.readLine();
        }
        String[] m = new Scanner(System.in).nextLine().split(" ", 0);
        starts = new int[m.length];
        int starter = 0;//Which word do we start each loop with?
        Arrays.fill(map, -1);
        Arrays.fill(checked, false);
        //Keep track of previous maps
        int[][] prevMaps = new int[m.length][26];
        boolean[][] prevChecks = new boolean[m.length][26];
        //Default value is nothing mapped
        prevMaps[0] = map.clone();
        prevChecks[0] = checked.clone();
        while(!isDone){//Has the last word been mapped?
            //Default values at a level
            map = prevMaps[starter].clone();
            checked = prevChecks[starter].clone();
            int wordNum = starter; //Start at m[starter]
            boolean goBack = true; //Flag to see if we ever map
            while(mapDict(dict, m, starts[wordNum], wordNum)){//Is this path viable?
                wordNum++;//Check the next one
                //Reset our defaults
                prevMaps[wordNum] = map.clone();
                prevChecks[wordNum] = checked.clone();
                goBack = false; //Flag is false
            }
            starter = goBack?wordNum-1:wordNum;//Either start one back if there are no compatible maps or where you are otherwise.
        }
        for(String w: m){
            for(int i = 0; i < w.length(); i++)
                System.out.print((char)map[w.charAt(i)-97]);
            System.out.print(" ");
        }
    }
}
