package org.venus.linearhash;

import junit.framework.TestCase;

/**
 * @Author venus
 * @Date 2024/8/5
 * @Version 1.0
 */
public class RoundTest extends TestCase {


    public void testApp() {
        assertTrue( true );
    }

    private void console(int init, int current, int round) {
        System.out.printf(
                "Init bucket number: %d, current bucket number: %d, round: %d.\n",
                init, current, round
        );
    }

//    public void testCalculateSplitRounds() {
//        Round round = Round.getInstance();
//        Integer level = round.getRound();
//        System.out.println("Init level: " + level);
//
//        round.calculateSplitRounds(2);
//        console(1, 2, round.getRound());
//
//        round.calculateSplitRounds(7);
//        console(4, 7, round.getRound());
//
//        round.calculateSplitRounds(33, 4);
//        console(4, 33, round.getRound());
//    }



}
