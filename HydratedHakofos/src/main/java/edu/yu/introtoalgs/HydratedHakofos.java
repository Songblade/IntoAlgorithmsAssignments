package edu.yu.introtoalgs;

/** Defines the API for the HydratedHakofos assignment: see the requirements
 * document for more information.
 *
 * Students MAY NOT change the constructor signature.  My test code will only
 * invoke the API defined below.
 *
 * @author Avraham Leff
 */

public class HydratedHakofos {
    public HydratedHakofos() {
        // fill in to taste
    }

    /** Determines whether or not a table exists such that hakofos, beginning
     * from this table, can successfully traverse the entire circuit without
     * getting dehydrated ("negative water level").  If multiple tables are
     * valid, return the lowest-numbered table (numbered 1..n).  Otherwise,
     * return the sentinel value of "-1".
     *
     * @param waterAvailable an array, indexed from 0..n-1, such that the ith
     * element represents the amount of water available at the ith table.
     * @param waterRequired an array, indexed from 0..n-1, such that the ith
     * element represents the amount of water required to travel from the ith
     * table to the next table.
     * @return a number from 1..n or -1 as appropriate.
     *
     * NOTE: if the client supplies arrays of differing lengths, or if the arrays
     * are null, or empty, or if the client supplies non-positive values in
     * either of these arrays, the result is undefined.  In other words: you
     * don't have to check for these conditions (unless you want to prevent
     * errors during development).
     */
    public int doIt(int[] waterAvailable, int[] waterRequired) {

        // I think I will do my error checker first
        errorChecker(waterAvailable, waterRequired);

        // I am doing a for-loop through the array
        // When I find a table with a positive value, I throw it into a function that sees if
            // the value stays positive until it gets back to the starting location
        // If it doesn't, then we return the table where our value went negative
        // If it does, we return -1
        // We are using the indexing for all of this, we add 1 when returning a correct answer
        // If we return a value that is > the current i in the for loop, we change i to equal that
        // The i will then be ++, but since we know this is a bad table because it made us negative
            // that isn't a problem, it can't be the right answer anyway
        // If the value is < the current value in the for loop, we know we looped over, and the
            // problem must be unsolvable
        // If it works, it will return -1, which will be checked first, so it will return true, and
            // it doesn't matter that it was repeating a second time
        // the efficiency's worst case scenario is 2n
        // Okay, let's build it

        for (int table = 0; table < waterAvailable.length; table++) {
            // I can't use for-each, because I will change the index
            if (waterAvailable[table] - waterRequired[table] >= 0) {
                // if this table is a valid starting location
                int newLocation = checkIfValidTable(waterAvailable, waterRequired, table);
                if (newLocation == -1) { // if the table is good
                    return table + 1;
                    // we return table + 1, not table, because they start their tables at 1, not 0
                } else if (newLocation < table) {
                    // this means that we wrapped around but still couldn't get a good table
                    // that means there are no good tables to be had
                    return -1;
                } // if we are still here, then the table was bad, but we aren't done searching
                table = newLocation; // so we mess with the for-loop by skipping to the next location
                // we know that this table is bad, but the for loop is about to table++
            }
            // if there isn't enough water, this is a bad table, let's go to the next one
        }
        // if we reach here, it means that we never wrap around, because the last table is negative
            // and past the last positive point
        // this means that the problem is unsolvable and the congregation needs to give out more water
        return -1;
    } // doIt

    /**
     * This method checks if any given table can properly hydrate you through the whole trip
     * @param waterAvailable water given by each table
     * @param waterRequired water taken up by each trip after the table
     * @param startingTable that we are starting our hakofah at
     * @return -1 if this table is a valid starting location, the index of the trip that
     * dehydrates you from this table otherwise
     */
    private int checkIfValidTable(int[] waterAvailable, int[] waterRequired, int startingTable) {
        int currentTable = startingTable;
        int waterTotal = 0;
        do {
            // I am a little unsure on how to increase the tables
            // we start by visiting the table and drinking, then we check if we can make it to the
                // next table, and if the next table is the last one, we return
            waterTotal += waterAvailable[currentTable] - waterRequired[currentTable];
            if (waterTotal < 0) { // we check if we can make it to the next table with that water
                return currentTable;
            }
            // since we made it to the table, we record it, and check if we are done
            currentTable = (currentTable + 1) % waterAvailable.length;
            // the modulus means it wraps around
        } while (currentTable != startingTable);
        // this is a do-while, because the table always starts as the starting table
        // we want to know if it is at the starting table at the end
        return -1; // this is a good thing, it means the table worked
        // I have to return a negative number, because 0 or a positive number could be the name
            // of a table
    }

    private void errorChecker(int[] waterAvailable, int[] waterRequired) {
        if (waterAvailable == null || waterRequired == null) {
            throw new IllegalArgumentException("Arrays are null");
        }
        if (waterAvailable.length == 0) {
            throw new IllegalArgumentException("Arrays are empty");
        }
        if (waterAvailable.length != waterRequired.length) {
            throw new IllegalArgumentException("Arrays have different lengths");
        }
    }

}
