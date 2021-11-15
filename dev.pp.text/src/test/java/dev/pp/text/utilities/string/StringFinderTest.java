package dev.pp.text.utilities.string;

import dev.pp.text.utilities.character.CharChecks;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringFinderTest {

    @Test
    public void testFindFirstIndex() {

        assertEquals ( -1, StringFinder.findFirstIndex ( "abc", CharChecks::isSpaceOrTab ) );
        assertEquals ( 0, StringFinder.findFirstIndex ( " abc", CharChecks::isSpaceOrTab ) );
        assertEquals ( 1, StringFinder.findFirstIndex ( "a bc", CharChecks::isSpaceOrTab ) );
        assertEquals ( 3, StringFinder.findFirstIndex ( "abc ", CharChecks::isSpaceOrTab ) );
    }
}