package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import edu.yu.introtoalgs.BTKeysAtSameLevel;

import java.util.ArrayList;
import java.util.List;

public class BTKeyTest {

    private final BTKeysAtSameLevel treeMaker;

    public BTKeyTest() {
        treeMaker = new BTKeysAtSameLevel();
    }

    private void checkCaseWorks(String strRep, List<List<Integer>> expected) {
        List<List<Integer>> result = treeMaker.compute(strRep);
        assertEquals(expected, result);
    }

    private void checkCaseFails(String strRep) {
        assertThrows(IllegalArgumentException.class, ()->treeMaker.compute(strRep));
    }

    // make sure that it works if the string is empty and so the tree is also empty
    @Test
    public void testEmpty() {
        List<List<Integer>> expected = new ArrayList<>();
        checkCaseWorks("", expected);
        checkCaseWorks(" ", expected);
        checkCaseWorks("\t", expected);
        checkCaseWorks("\t  ", expected);
        checkCaseWorks("\n", expected);
    }

    // I need to test that simply giving a number just returns a list with only that number
    @Test
    public void testRoot() {
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        checkCaseWorks("0", expected);
    }

    // I need to test that giving a number with one number in parentheses gives two lists with one each
    @Test
    public void testRootOneChild() {
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(1)));
        expected.add(new ArrayList<>(List.of(0)));
        checkCaseWorks("1(0)", expected);
    }

    // I need to test that giving a number with two numbers in two parentheses gives two lists with two in the second
    @Test
    public void testRootTwoChildren() {
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(1, 2)));
        checkCaseWorks("0(1)(2)", expected);
    }

    // Three in parentheses gives one in each of three
    @Test
    public void test3Gens() {
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(1)));
        expected.add(new ArrayList<>(List.of(2)));
        checkCaseWorks("0(1(2))", expected);
    }

    // We can have one at top and 2 two layers in
    @Test
    public void testCanHaveSiblingsFarIn() {
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(1)));
        expected.add(new ArrayList<>(List.of(2)));
        expected.add(new ArrayList<>(List.of(2, 3)));
        checkCaseWorks("0(1(2(2)(3)))", expected);
    }

    @Test
    public void testCanHave4Cousins() {
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(1, 2)));
        expected.add(new ArrayList<>(List.of(3, 4, 5, 6)));
        checkCaseWorks("0(1(3)(4))(2(5)(6))", expected);
    }

    // We can have an arbitrarily large number of children
    @Test
    public void testCanGoFarIn() {
        List<List<Integer>> expected = new ArrayList<>();
        StringBuilder strRepBuilder = new StringBuilder(1000001);
        strRepBuilder.append(0);
        expected.add(new ArrayList<>(List.of(0)));
        for (int i = 0; i < 1000000; i++) {
            strRepBuilder.append('(');
            strRepBuilder.append(i % 10);
            expected.add(new ArrayList<>(List.of(i % 10)));
        }
        strRepBuilder.append(")".repeat(1000000)); // to close all the parentheses
        checkCaseWorks(strRepBuilder.toString(), expected);
    }

    // One child can have children when others don't, testing for two different orders (should have same result)
    @Test
    public void testSiblingsWithChildren() {
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(1, 2)));
        expected.add(new ArrayList<>(List.of(3, 4)));
        checkCaseWorks("0(1(3)(4))(2)", expected);
        checkCaseWorks("0(1(3))(2(4))", expected);
        checkCaseWorks("0(1)(2(3)(4))", expected);
    }

    // tests builds prints children from left to right
    @Test
    public void testShowsChildrenLeftToRight() {
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(1, 2)));
        checkCaseWorks("0(1)(2)", expected);

        expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(2, 1)));
        checkCaseWorks("0(2)(1)", expected);

        expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(1, 2)));
        expected.add(new ArrayList<>(List.of(3, 4)));
        checkCaseWorks("0(1(3))(2(4))", expected);

        expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(1, 2)));
        expected.add(new ArrayList<>(List.of(4, 3)));
        checkCaseWorks("0(1(4))(2(3))", expected);

        expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(2, 1)));
        expected.add(new ArrayList<>(List.of(3, 4)));
        checkCaseWorks("0(2(3))(1(4))", expected);
    }

    // tests that it works when only contains one number repeated
    @Test
    public void testOneValueRepeated() {
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(List.of(0)));
        expected.add(new ArrayList<>(List.of(0, 0)));
        expected.add(new ArrayList<>(List.of(0, 0)));
        checkCaseWorks("0(0(0)(0))(0)", expected);
    }

    // I might want a more complicated example, but I'm not sure

    // Testing that if I give a null string, I get an IAE
    @Test
    public void testThrowsNull() {
        checkCaseFails(null);
    }

    // Same with a string that contains anything besides numbers and parentheses, whether at the end of a correct
        // thing or not
    @Test
    public void testThrowsLetter() {
        checkCaseFails("a");
        checkCaseFails("absalom");
        checkCaseFails("a0");
        checkCaseFails("0a");
        checkCaseFails("0(a)");
        checkCaseFails("0(1a)");
        checkCaseFails("0(1) a");
        checkCaseFails("-1");
        checkCaseFails("0(-1)");
        checkCaseFails("0[1]");
        checkCaseFails("0{1}");
        checkCaseFails("1+1");
    }

    // Same with open parentheses that don't close
    @Test
    public void testThrowsOpenParentheses() {
        checkCaseFails("0(");
        checkCaseFails("0(a");
        checkCaseFails("0(a(");
        checkCaseFails("0(a)(");
        checkCaseFails("0(a()");
        checkCaseFails("0(a(b)");
    }

    // Same with closed parentheses that don't open
    @Test
    public void testThrowsCloseParentheses() {
        checkCaseFails("0)");
        checkCaseFails("0)(1)");
    }

    // Same with parentheses around the root
    @Test
    public void testThrowsParenthesesOnRoot() {
        checkCaseFails("(0)");
        checkCaseFails("(0)(1)");
        checkCaseFails("(0(1))");
    }

    // make sure that it fails if you have 3 children
    @Test
    public void testThrows3Children() {
        checkCaseFails("0(1)(2)(3)");
        checkCaseFails("0(1(1)(2)(3))");
        checkCaseFails("0(2)(1(1)(2)(3))");
    }

    // make sure it fails if we have missing nodes
    @Test
    public void testThrowsMissingKeys() {
        checkCaseFails("0()(2)");
        checkCaseFails("0((1)(2))");
        checkCaseFails("0()(1(1)(2))");
    }

}
