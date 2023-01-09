package testing;

import edu.yu.introtoalgs.LinelandNavigation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinelandHappyTest {

    // here, I will have all my lineland tests
    // more will be added as I think of them

    private LinelandNavigation runway;

    private LinelandHappyTest() {
        runway = new LinelandNavigation(25, 5);
    }

    // the following tests work, and don't return 0

    // test for the trivial case, where there are no mines
    @Test
    public void trivialTest() {
        assertEquals(5, runway.solveIt());
    }

    // like above, but g % m != 0
    @Test
    public void unevenGoalTest() {
        runway = new LinelandNavigation(26, 5);
        assertEquals(6, runway.solveIt());
    }

    // test where there are mines, but we jump over them automatically
    @Test
    public void ignoredMinesTest() {
        runway.addMinedLineSegment(1, 3);
        runway.addMinedLineSegment(9, 9);
        assertEquals(5, runway.solveIt());
    }

    // test where there are mines in the path, but we can go around them
    @Test
    public void dodgeMinesTest() {
        runway.addMinedLineSegment(10, 10);
        runway.addMinedLineSegment(11, 11);
        assertEquals(7, runway.solveIt());
    } // (0)>5<3>8>13>18>23>28

    // like above, but the mine length > 1
    @Test
    public void dodgeBigMinesTest() {
        runway.addMinedLineSegment(10, 11);
        assertEquals(7, runway.solveIt());
    }

    // test works when there is a mine on the goal, if we can get beyond it
    @Test
    public void dodgeGoalMinesTest() {
        runway.addMinedLineSegment(25, 26);
        assertEquals(7, runway.solveIt());
    }

    // and one where there are mines past the goal, but before goal+movement
    @Test
    public void minesABitPastGoalTest() {
        runway.addMinedLineSegment(26, 28);
        assertEquals(5, runway.solveIt());
    }

    // and one where there are mines past the goal + movement
    @Test
    public void minesPastGoalTest() {
        runway.addMinedLineSegment(40, 500);
        assertEquals(5, runway.solveIt());
    }

    // and one where they start past the goal, and the same mine continues past the m
    @Test
    public void minesPastGoalAndBeyond() {
        runway.addMinedLineSegment(26, 400);
        assertEquals(5, runway.solveIt());
    }

    // add a new test for a really big example
    @Test
    public void giantProblemTest() {
        runway = new LinelandNavigation(1_000_000, 5);
        assertEquals(200_000, runway.solveIt());
    }

    @Test
    public void giantProblemWithMineTest() {
        runway = new LinelandNavigation(1_000_000, 500_000);
        runway.addMinedLineSegment(1_000_000, 1_000_000);
        assertEquals(4, runway.solveIt());
    }

    // I want a test for a big example that also includes mines, but I'm not sure how to do it and
        // still calculate correctly

    // the following tests return 0

    // test when there is a mine covering an entire section of jumping
    @Test
    public void tooBigMineTest() {
        runway.addMinedLineSegment(10, 14);
        assertEquals(0, runway.solveIt());
    }

    // test when a mine covers an entire section of jumping at and beyond the goal
    @Test
    public void tooBigGoalMineTest() {
        runway.addMinedLineSegment(25, 29);
        assertEquals(0, runway.solveIt());
    }

    // test when we would be able to go around the mines, except we can't jump before 0
    @Test
    public void tooEarlyMineTest() {
        runway.addMinedLineSegment(5, 5);
        assertEquals(0, runway.solveIt());
    }

    // test when a mine doesn't cover an entire section, but 2 mines together mean the problem is
        // still impossible
    @Test
    public void comboMineTest() {
        runway.addMinedLineSegment(10, 13);
        runway.addMinedLineSegment(19, 19);
        assertEquals(0, runway.solveIt());
    }

}
