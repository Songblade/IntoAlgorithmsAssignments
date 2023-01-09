package testing;

import edu.yu.introtoalgs.LinelandNavigation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LinelandErrorTest {

    // tests for potential errors

    // I am only instructed to throw if there are problems in the constructor, so I will only test there

    // tests that the values must be positive
    @Test
    public void mustHavePositiveValuesTest() {
        assertThrows(IllegalArgumentException.class, ()->new LinelandNavigation(-1, 5));
        assertThrows(IllegalArgumentException.class, ()->new LinelandNavigation(25, -1));
        assertThrows(IllegalArgumentException.class, ()->new LinelandNavigation(0, 5));
        assertThrows(IllegalArgumentException.class, ()->new LinelandNavigation(25, 0));
    }

    // tests that the values can't be more than max_forward
    @Test
    public void largeValuesTest() {
        assertThrows(IllegalArgumentException.class, ()->new LinelandNavigation(LinelandNavigation.MAX_FORWARD + 1, 5));
        assertThrows(IllegalArgumentException.class, ()->new LinelandNavigation(25, LinelandNavigation.MAX_FORWARD + 1));
    }

    // tests that the values CAN equal max_forward
    @Test
    public void maxValueWorksTest() {
        new LinelandNavigation(LinelandNavigation.MAX_FORWARD, 5);
        new LinelandNavigation(25, LinelandNavigation.MAX_FORWARD);
    }

    // tests that the m CAN be bigger than g
    @Test
    public void biggerMovementWorksTest() {
        new LinelandNavigation(5, 25);
    }

}
