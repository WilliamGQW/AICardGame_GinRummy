package com.example;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Before;

import com.example.Card.CardRank;

public class GinrummyTest {
    Master player;
    ArrayList<Card> deck;
    ArrayList<Card> discard;

    @Before
    public void setUp() throws Exception {
        player = new Master();
        Set<Card> cards = Card.getAllCards();
        deck = new ArrayList<Card>(cards);
        discard = new ArrayList<Card>();
        Collections.sort(deck);
        List<Card> temp = new ArrayList<Card>();
        for (int i = 0; i < 10; i++) {
            temp.add(deck.remove(0));
        }
        player.receiveInitialHand(temp);
        discard.add(deck.remove(0));
    }

    //taking the top of discard pile properly
    @org.junit.Test
    public void testWillTakeTopDiscardTrue() {
        boolean result = player.willTakeTopDiscard(discard.get(0));
        assertEquals(result, true);
    }

    //taking the top of the discard pile inappropriately
    @org.junit.Test
    public void testWillTakeTopDiscardFlase() {
        discard.add(0, deck.get(5));
        boolean result = player.willTakeTopDiscard(discard.get(0));
        assertEquals(result, false);
    }

    //discard the fifth card
    @org.junit.Test
    public void testDrawAndDiscard() {
        discard.add(0, deck.get(5));
        Card discardCard = player.drawAndDiscard(discard.get(0));
        assertEquals(discardCard.getRank(), CardRank.FIVE);
    }

    @org.junit.Test
    public void testKnock() {
        boolean knock = player.knock();
        assertEquals(knock, true);
    }

}
