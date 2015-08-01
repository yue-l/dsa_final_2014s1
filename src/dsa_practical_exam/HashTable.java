package dsa_practical_exam;

import java.util.ArrayList;
import java.io.*;

/**
 * Data Structures and Algorithm 2014 - Final Exam Question 1: Comparing Hash
 * Functions
 *
 * This class implements a hash table for storing a collection of Strings You
 * need to complete the three hash functions, and the locate method
 *
 * You are ONLY ALLOWED to update the methods below the "Insert your code here"
 * comments
 *
 * @Author: Yue Li
 * @Student ID: 1251124
 */
public class HashTable {

    // The array containing elements in the hash table
    private String[] data;

    // When an element is removed from a position, the position should be set to
    // "reserved"
    // for the contains method later
    // We use a boolean array to indicate which positions are "reserved"
    // The array should have the same length as the data array
    // A position is set to true in this array means that this position is
    // "reserved".
    private boolean[] isReserved;

    // The number of elements currently stored in the hash table
    private int size;

    // The current capacity of the hash table
    private int capacity;

    // The load factor is the maximum size:capacity that is allowed before
    // the capacity of the hash table is expanded.
    private double maximumLoadFactor;

    // The numProbes variable keeps track of the total number of probing made
    // during the test
    private int numProbes;

    // The hOption variable keeps track of the option made on which hash
    // function to choose
    private int hOption;

    // The constructor specifying the initial capacity of the hash table
    public HashTable(int capa) {
        capacity = capa;
        data = new String[capacity];
        isReserved = new boolean[capacity];
        maximumLoadFactor = 0.75; // The maximum load factor is set to 0.75
        numProbes = 0; // Initially no probing has been made
        hOption = 0;
    }

    // A constructor with the extra hOpt parameter indicating
    // the choice made on the hash function
    // Valid values of hOpt are 0,1,2,3
    // 0 indicates the default hash function (the hashCode method by Java)
    // 1,2,3 indicate the first, second and third hash function
    public HashTable(int capa, int hOpt) {
        this(capa);
        hOption = hOpt;
    }

    /**
     * This methods implements the first hash function, which assigns the string
     * s with the ASCII value of its first letter
     */
    protected int hashfunc1(String s) {
        // /////////////////////////
        // Insert your code here
        if (s.length() == 0) {
            return 0;
        } else {
            return s.charAt(0);
        }
        // /////////////////////////
    }

    /**
     * This method implements the second hash function, which assigns the string
     * s with the sum of its letters
     */
    protected int hashfunc2(String s) {
        // /////////////////////////
        // Insert your code here
        int result = 0;
        for (char temp : s.toCharArray()) {
            result += temp;
        }
        return result;
        // /////////////////////////
    }

    /**
     * This method implements the third hash function, which applies
     * exponentiation (with base 2) on the index of each letter then sum up the
     * resulting values
     */
    protected int hashfunc3(String s) {
        // /////////////////////////
        // Insert your code here
        int result = 0;
        for (int index = 0; index < s.length(); index++) {
            result += Math.pow(2, index) * s.charAt(index);
        }
        return result;
        // /////////////////////////

    }

    /**
     * This method tries to locate the given String s in the hash table. If s is
     * found in the hash table, it should return the index of the position of s;
     * If s is not found, it should return the index of the position where s
     * should be put in.
     *
     * This method also updates numProbes variable so that it keeps the number
     * of probes made.
     */
    protected int locate(String s) {

        // hash stores the hash code of s
        int hash;

        // The switch statement applies the chosen hash function to the string s
        // The resulting hash code is then taken mod the current number of
        // positions to get
        // the position in the table.
        switch (hOption) {
            case 1:
                hash = Math.abs(hashfunc1(s) % data.length);
                break;
            case 2:
                hash = Math.abs(hashfunc2(s) % data.length);
                break;
            case 3:
                hash = Math.abs(hashfunc3(s) % data.length);
                break;
            default:
                hash = Math.abs(s.hashCode() % data.length); // applying the Java
                // default hash function for Strings
                break;
        }
        // Insert your code here
        //1st implementation
        int reservedSlot = -1;
        boolean foundReserved = false;
        while (data[hash] != null) {
            if (isReserved[hash]) {
                if (!foundReserved) {
                    reservedSlot = hash;
                    foundReserved = true;
                }
            } else if (s.equals(data[hash])) {
                return hash;
            }
            hash = (1 + hash) % data.length;
            numProbes++;
        }
        if (!foundReserved) {
            return hash;
        } else {
            return reservedSlot;
        }
    }

    /**
     * This method puts a String s in the hash table
     *
     */
    public void put(String s) {
        if (maximumLoadFactor * data.length <= (1 + size)) {
            expandCapacity();
        }

        int hash = locate(s);
        if (data[hash] == null || isReserved[hash]) {
            data[hash] = s;
            size++;
        }
    }

    // This is a utility method for expanding the capacity of the hash table (by
    // twice)
    private void expandCapacity() {

        String[] temp = new String[capacity];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = data[i];
        }

        capacity = capacity * 2;
        data = new String[capacity];
        isReserved = new boolean[capacity];

        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != null) {
                put(temp[i]);
            }
        }

    }

    // This method return true if and only if s is found in the hash table
    public boolean contains(String s) {
        if (data[locate(s)] != null) {
            return data[locate(s)].equals(s);
        }
        return false;
    }

    // This method removes the given String s (if it is in the hash table)
    public String remove(String s) {
        int hash = locate(s);
        if (data[hash] == null || isReserved[hash]) {
            return null;
        }
        size--;
        String old = data[hash];
        isReserved[hash] = true;
        data[hash] = null;
        return old;
    }

    /**
     * Returns the elements in the hash table as an ArrayList
     */
    public ArrayList<String> elementSet() {
        ArrayList<String> set = new ArrayList<String>();
        for (int i = 0; i < capacity; i++) {
            if (data[i] != null) {
                set.add(data[i]);
            }
        }
        return set;

    }

    /**
     * Display the contents of the current hash table
     *
     */
    public void showContent() {
        System.out.println("===========Hash Table Content===========");
        System.out.println("Number of elements: " + size);
        for (int i = 0; i < capacity; i++) {
            if (data[i] != null) {
                System.out.println(i + ": " + data[i]);
            } else if (isReserved[i]) {
                System.out.println(i + ": RESERVED");
            } else {
                System.out.println(i + ": EMPTY");
            }
        }
        System.out.println("========================================");
    }

    /**
     * Check and print if s is contained in the hash table and print its
     * location if it is found
     */
    public void check(String s) {
        if (contains(s)) {
            System.out.println(s + " is in the table at index: " + locate(s));
        } else {
            System.out.println(s + " is not in the table");
        }
    }

    /**
     * returns the number of probe made so far
     */
    public int probes() {
        return numProbes;
    }

    /**
     * The main method implements a test on adding/removing Strings when using
     * all four hash functions The number of probes are printed out as well
     */
    public static void main(String[] args) {

        HashTable table;

        // The variable i in the for loop below is the choice made on the hash
        // function
        for (int i = 0; i < 4; i++) {

            System.out.println("============================================");
            System.out.println("Create a hash table using hash function: " + i);
            table = new HashTable(16, i); // Set the initial capacity to be 16
            try {
                //yue changed the filereader
                BufferedReader fileRead = new BufferedReader(new FileReader(
                        "src/dsa_practical_exam/add.txt"));
                String s;
                while ((s = fileRead.readLine()) != null) {
                    table.put(s);
                }

                fileRead = new BufferedReader(new FileReader("src/dsa_practical_exam/remove.txt"));
                while ((s = fileRead.readLine()) != null) {
                    table.remove(s);
                }

                fileRead = new BufferedReader(new FileReader("src/dsa_practical_exam/add2.txt"));
                while ((s = fileRead.readLine()) != null) {
                    table.put(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//			table = new HashTable(16, i); // Set the initial capacity to be 16
//			try {
//				// //yue changed the filereader
//				BufferedReader fileRead = new BufferedReader(new FileReader(
//						"add.txt"));
//				String s;
//				while ((s = fileRead.readLine()) != null)
//					table.put(s);
//				
//				fileRead = new BufferedReader(new FileReader("remove.txt"));
//				while ((s = fileRead.readLine()) != null)
//					table.remove(s);
//				
//				fileRead = new BufferedReader(new FileReader("add2.txt"));
//				while ((s = fileRead.readLine()) != null)
//					table.put(s);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
            // Print out the number of probes made
            System.out.println("Number of probes made: " + table.probes());

            table.check("Li");
            table.check("Adams");
            table.check("Santos");

        }

        // Expected outcome:
        /*
         * ============================================ 
         * Create a hash table using hash function: 0 
         * Number of probes made: 44
         * Li is in the table at index: 157 
         * Adams is in the table at index: 164 
         * Santos is not in the table 
         * ============================================ 
         * Create a hash table using hash function: 1
         * Number of probes made: 4849 
         * Li is in the table at index: 112 
         * Adams is in the table at index: 65 
         * Santos is not in the table 
         * ============================================ 
         * Create a hash table using hash function: 2
         * Number of probes made: 69 
         * Li is in the table at index: 182 
         * Adams is in the table at index: 230 
         * Santos is not in the table 
         * ============================================ 
         * Create a hash table using hash function: 3
         * Number of probes made: 37 
         * Li is in the table at index: 30 
         * Adams is in the table at index: 37 
         * Santos is not in the table
         */
    }

}
