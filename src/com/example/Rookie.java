package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Rookie implements PlayerStrategy {
    List<Card> hand;
    List<Card> deadwoods;
    List<Meld> melds;
    List<Card> opponentHand;
    List<Meld> opponentMelds;
    boolean drewDiscard;
    Card opponentDiscarded;
    Card previousDiscardTop;

    /**
     * Called by the game engine for each player at the beginning of each round to receive and
     * process their initial hand dealt.
     *
     * @param hand The initial hand dealt to the player
     */
    @Override
    public void receiveInitialHand(List<Card> hand) {
        this.hand = hand;
        deadwoods = new ArrayList<Card>();
        melds = new ArrayList<Meld>();
        Collections.sort(hand);
        System.out.println("Receive: ");

        for (int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i).getRank() + "(" + hand.get(i).getSuit() + ") ");
        }
        System.out.println("\n");
        int same = 1;
        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(i).getRank() == hand.get(i - 1).getRank()) {
                same += 1;
                if (i == hand.size() - 1) {
                    if (same > 2) {
                        Card[] temp = new Card[same];
                        for (int j = 0; j < same; j++) {
                            temp[j] = hand.get(i - same + 1 + j);
                        }
                        SetMeld rm = Meld.buildSetMeld(temp);
                        melds.add(rm);
                        same = 1;
                    } else {
                        deadwoods.add(hand.get(i - 1));
                        deadwoods.add(hand.get(i));
                    }
                }
            } else {
                if (same > 2) {
                    Card[] temp = new Card[same];
                    for (int j = 0; j < same; j++) {
                        temp[j] = hand.get(i - same + j);
                    }
                    SetMeld rm = Meld.buildSetMeld(temp);
                    melds.add(rm);
                    same = 1;
                } else {
                    for (int j = 0; j < same; j++) {
                        deadwoods.add(hand.get(i - same + j));
                    }
                    same = 1;
                }
                if (i == hand.size() - 1) {
                    deadwoods.add(hand.get(i));
                }
            }
        }
    }

    /**
     * Called by the game engine to prompt the player on whether they want to take the top card
     * from the discard pile or from the deck.
     *
     * @param card The card on the top of the discard pile
     * @return whether the user takes the card on the discard pile
     */
    @Override
    public boolean willTakeTopDiscard(Card card) {
        for (int i = 0; i < melds.size(); i++) {
            if (melds.get(i).canAppendCard(card)) {
                return true;
            }
        }
        for (int i = 1; i < deadwoods.size(); i++) {
            if (deadwoods.get(i).getRank() == deadwoods.get(i - 1).getRank()) {
                if (deadwoods.get(i).getRank() == card.getRank()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Called by the game engine to prompt the player to take their turn given a
     * dealt card (and returning their card they've chosen to discard).
     *
     * @param drawnCard The card the player was dealt
     * @return The card the player has chosen to discard
     */
    @Override
    public Card drawAndDiscard(Card drawnCard) {
        System.out.println("Got " + drawnCard.getRank() + "(" + drawnCard.getSuit() + ")");
        boolean append = false;
        for (int i = 0; i < melds.size(); i++) {
            if (melds.get(i).canAppendCard(drawnCard)) {
                melds.get(i).appendCard(drawnCard);
                append = true;
            }
        }
        if (!append) {
            for (int i = 1; i < deadwoods.size(); i++) {
                if (deadwoods.get(i).getRank() == deadwoods.get(i - 1).getRank()) {
                    if (deadwoods.get(i).getRank() == drawnCard.getRank()) {
                        Card[] temp = new Card[3];
                        temp[2] = deadwoods.remove(i);
                        temp[0] = deadwoods.remove(i - 1);
                        temp[1] = drawnCard;
                        SetMeld sm = Meld.buildSetMeld(temp);
                        melds.add(sm);
                        append = true;
                    }
                }
            }
        }
        if (!append) {
            deadwoods.add(drawnCard);
            Collections.sort(deadwoods);
        }
        int minLengthIndex = 0;
        for (int j = 1; j < deadwoods.size(); j++) {
            if (deadwoods.get(j).getRank() != deadwoods.get(j - 1).getRank()) {
                if (j != deadwoods.size() - 1) {
                    if (deadwoods.get(j).getRank() != deadwoods.get(j + 1).getRank()) {
                        minLengthIndex = j;
                    }
                } else {
                    minLengthIndex = j;
                }
            }
        }
        if (deadwoods.size() == 0) {
            for (int i = 0; i < melds.size(); i++) {
                for (int j = 0; j < melds.get(i).getCards().length; j++) {
                    if (melds.get(i).canRemoveCard(melds.get(i).getCards()[j])) {
                        Card temp = melds.get(i).getCards()[j];
                        melds.get(i).removeCard(temp);
                        System.out.println("Discard " + temp.getRank() + "(" + temp.getSuit() + ")");
                        return temp;
                    }
                }
            }
        }
        System.out.println("Discard " + deadwoods.get(minLengthIndex).getRank() + "(" + deadwoods.get(minLengthIndex).getSuit() + ")");
        return deadwoods.remove(minLengthIndex);
    }

    @Override
    public boolean knock() {
        int count = 0;
        for (int i = 0; i < deadwoods.size(); i++) {
            count += deadwoods.get(i).getPointValue();
        }
        if (count <= 10) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void opponentEndTurnFeedback(boolean drewDiscard, Card previousDiscardTop, Card opponentDiscarded) {
        this.drewDiscard = drewDiscard;
        this.previousDiscardTop = previousDiscardTop;
        this.opponentDiscarded = opponentDiscarded;
    }

    @Override
    public void opponentEndRoundFeedback(List<Card> opponentHand, List<Meld> opponentMelds) {
        this.opponentHand = opponentHand;
        this.opponentMelds = opponentMelds;
    }

    @Override
    public List<Meld> getMelds() {
        return melds;
    }

    @Override
    public void reset() {
        hand.clear();
        melds.clear();
        deadwoods.clear();
        hand = null;
        melds = null;
        deadwoods = null;
        opponentHand = null;
        opponentMelds = null;
        drewDiscard = false;
        previousDiscardTop = null;
        opponentDiscarded = null;
    }

    public List<Card> getHand() {
        List<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < melds.size(); i++) {
            cards.addAll(Arrays.asList(melds.get(i).getCards()));
        }
        cards.addAll(deadwoods);
        return cards;
    }

    public List<Card> getDeadWoods() {
        return this.deadwoods;
    }

}
