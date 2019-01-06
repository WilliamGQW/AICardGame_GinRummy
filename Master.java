package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.example.Card.CardSuit;

public class Master implements PlayerStrategy {
    List<Card> spades;//store cards of spades
    List<Card> hearts;//store cards of hearts
    List<Card> diamonds;//store cards of diamonds
    List<Card> clubs;//store cards of clubs
    List<Card> deadWoods;//store cards of deadWoods
    List<Meld> melds;//store all melds
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
        //show received cards' information
        // System.out.println("Receive:");
        hearts = new ArrayList<Card>();
        spades = new ArrayList<Card>();
        clubs = new ArrayList<Card>();
        diamonds = new ArrayList<Card>();
        deadWoods = new ArrayList<Card>();
        melds = new ArrayList<Meld>();

        //sort cards according to their suits
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getSuit() == CardSuit.HEARTS) {
                hearts.add(hand.get(i));
            } else if (hand.get(i).getSuit() == CardSuit.SPADES) {
                spades.add(hand.get(i));
            } else if (hand.get(i).getSuit() == CardSuit.CLUBS) {
                clubs.add(hand.get(i));
            } else if (hand.get(i).getSuit() == CardSuit.DIAMONDS) {
                diamonds.add(hand.get(i));
            }
            Collections.sort(hearts);
            Collections.sort(spades);
            Collections.sort(clubs);
            Collections.sort(diamonds);
        }

        // print cards in hand
//        for (int i = 0; i < hearts.size(); i++) {
//            System.out.print(hearts.get(i).getRank() + "(HEARTS) ");
//        }
//        for (int i = 0; i < spades.size(); i++) {
//            System.out.print(spades.get(i).getRank() + "(SPADES) ");
//        }
//        for (int i = 0; i < clubs.size(); i++) {
//            System.out.print(clubs.get(i).getRank() + "(CLUBS) ");
//        }
//        for (int i = 0; i < diamonds.size(); i++) {
//            System.out.print(diamonds.get(i).getRank() + "(DIAMONDS) ");
//        }
//        System.out.println();

        //find all melds and split hand to melds and deadWoods
        //repeat the procedure four times since there are four different suits
        if (hearts.size() > 2) {
            int continuous = 1;
            for (int i = 1; i < hearts.size(); i++) {
                if (hearts.get(i).getRankValue() - hearts.get(i - 1).getRankValue() == 1) {
                    //if two cards are continuous, the difference between them is 1
                    continuous += 1;
                    //build runMelds
                    if (i == hearts.size() - 1) {
                        if (continuous > 2) {
                            Card[] temp = new Card[continuous];
                            for (int j = 0; j < continuous; j++) {
                                temp[j] = hearts.get(i - continuous + 1 + j);
                            }
                            RunMeld rm = Meld.buildRunMeld(temp);
                            melds.add(rm);
                        } else {
                            //otherwise, throw them into deadWoods
                            for (int j = 0; j < continuous; j++) {
                                deadWoods.add(hearts.get(i - continuous + 1 + j));
                            }
                        }
                    }
                } else {
                    if (continuous > 2) {
                        Card[] temp = new Card[continuous];
                        for (int j = 0; j < continuous; j++) {
                            temp[j] = hearts.get(i - continuous + j);
                        }
                        RunMeld rm = Meld.buildRunMeld(temp);
                        melds.add(rm);
                    } else {
                        for (int j = 0; j < continuous; j++) {
                            deadWoods.add(hearts.get(i - continuous + j));
                        }
                    }
                    if (i == hearts.size() - 1) {
                        deadWoods.add(hearts.get(i));
                    }
                    continuous = 1;
                }
            }
        } else {
            deadWoods.addAll(hearts);
        }

        if (spades.size() > 2) {
            int continous = 1;
            for (int i = 1; i < spades.size(); i++) {
                if (spades.get(i).getRankValue() - spades.get(i - 1).getRankValue() == 1) {
                    continous += 1;
                    if (i == spades.size() - 1) {
                        if (continous > 2) {
                            Card[] temp = new Card[continous];
                            for (int j = 0; j < continous; j++) {
                                temp[j] = spades.get(i - continous + 1 + j);
                            }
                            RunMeld rm = Meld.buildRunMeld(temp);
                            melds.add(rm);
                        } else {
                            for (int j = 0; j < continous; j++) {
                                deadWoods.add(spades.get(i - continous + 1 + j));
                            }
                        }
                    }
                } else {
                    if (continous > 2) {
                        Card[] temp = new Card[continous];
                        for (int j = 0; j < continous; j++) {
                            temp[j] = spades.get(i - continous + j);
                        }
                        RunMeld rm = Meld.buildRunMeld(temp);
                        melds.add(rm);
                    } else {
                        for (int j = 0; j < continous; j++) {
                            deadWoods.add(spades.get(i - continous + j));
                        }
                    }
                    if (i == spades.size() - 1) {
                        deadWoods.add(spades.get(i));
                    }
                    continous = 1;
                }
            }
        } else {
            deadWoods.addAll(spades);
        }
        if (clubs.size() > 2) {
            int continous = 1;
            for (int i = 1; i < clubs.size(); i++) {
                if (clubs.get(i).getRankValue() - clubs.get(i - 1).getRankValue() == 1) {
                    continous += 1;
                    if (i == clubs.size() - 1) {
                        if (continous > 2) {
                            Card[] temp = new Card[continous];
                            for (int j = 0; j < continous; j++) {
                                temp[j] = clubs.get(i - continous + 1 + j);
                            }
                            RunMeld rm = Meld.buildRunMeld(temp);
                            melds.add(rm);
                        } else {
                            for (int j = 0; j < continous; j++) {
                                deadWoods.add(clubs.get(i - continous + 1 + j));
                            }
                        }
                    }
                } else {
                    if (continous > 2) {
                        Card[] temp = new Card[continous];
                        for (int j = 0; j < continous; j++) {
                            temp[j] = clubs.get(i - continous + j);
                        }
                        RunMeld rm = Meld.buildRunMeld(temp);
                        melds.add(rm);
                    } else {
                        for (int j = 0; j < continous; j++) {
                            deadWoods.add(clubs.get(i - continous + j));
                        }
                    }
                    if (i == clubs.size() - 1) {
                        deadWoods.add(clubs.get(i));
                    }
                    continous = 1;
                }
            }
        } else {
            deadWoods.addAll(clubs);
        }
        if (diamonds.size() > 2) {
            int continous = 1;
            for (int i = 1; i < diamonds.size(); i++) {
                if (diamonds.get(i).getRankValue() - diamonds.get(i - 1).getRankValue() == 1) {
                    continous += 1;
                    if (i == diamonds.size() - 1) {
                        if (continous > 2) {
                            Card[] temp = new Card[continous];
                            for (int j = 0; j < continous; j++) {
                                temp[j] = diamonds.get(i - continous + 1 + j);
                            }
                            RunMeld rm = Meld.buildRunMeld(temp);
                            melds.add(rm);
                        } else {
                            for (int j = 0; j < continous; j++) {
                                deadWoods.add(diamonds.get(i - continous + 1 + j));
                            }
                        }
                    }
                } else {
                    if (continous > 2) {
                        Card[] temp = new Card[continous];
                        for (int j = 0; j < continous; j++) {
                            temp[j] = diamonds.get(i - continous + j);
                        }
                        RunMeld rm = Meld.buildRunMeld(temp);
                        melds.add(rm);
                    } else {
                        for (int j = 0; j < continous; j++) {
                            deadWoods.add(diamonds.get(i - continous + j));
                        }
                    }
                    if (i == diamonds.size() - 1) {
                        deadWoods.add(diamonds.get(i));
                    }
                    continous = 1;
                }
            }
        } else {
            deadWoods.addAll(diamonds);
        }
    }


    /**
     * Called by the game engine to prompt the player on whether they want to take the top card
     * from the discard pile or from the deck.
     * <p>
     * In the Master AI, it checks whether the top card of the discard pile can make a new meld or not,
     * but there is no such function in SimplePlayer.
     *
     * @param card The card on the top of the discard pile
     * @return whether the user takes the card on the discard pile
     */
    @Override
    public boolean willTakeTopDiscard(Card card) {
        //check if the card can be append to existing meld or not
        for (int i = 0; i < melds.size(); i++) {
            if (melds.get(i).canAppendCard(card)) {
                return true;
            }
        }
        //check if the card can make a meld or not
        for (int i = 1; i < deadWoods.size(); i++) {
            if (deadWoods.get(i).getSuit() == deadWoods.get(i - 1).getSuit()) {
                int difference = deadWoods.get(i).getRankValue() - deadWoods.get(i - 1).getRankValue();
                // for example, in the deadWoods, there are FIVE HEARTS and SEVEN HEARTS
                // the difference between them is 2. The card on the top of the discard pile is SIX HEART
                // then the Master is supposed to take it.
                //there are also situations that: in the deadWoods, there are FIVE HEARTS, SIX HEARTS,
                //if the top of... is FOUR HEART or SEVEN HEART. Take it
                if (difference == 2) {
                    if (card.getRankValue() == difference / 2) {
                        return true;
                    }
                } else if (difference == 1) {
                    if (card.getRankValue() == deadWoods.get(i).getRankValue() + 1) {
                        return true;
                    } else if (card.getRankValue() == deadWoods.get(i - 1).getRankValue() - 1) {
                        return true;
                    }
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
        //check if the card can append to existing meld or not
        boolean append = false;
        for (int i = 0; i < melds.size(); i++) {
            if (melds.get(i).canAppendCard(drawnCard)) {
                melds.get(i).appendCard(drawnCard);
                append = true;
                for (int j = 0; j < deadWoods.size(); j++) {
                    if (melds.get(i).canAppendCard(deadWoods.get(j))) {
                        melds.get(i).appendCard(deadWoods.remove(j));
                    }
                }
            }
        }
        //check if the card can generate a new meld, if so, add the meld to list melds
        if (!append) {
            for (int i = 1; i < deadWoods.size(); i++) {
                if (drawnCard.getSuit() == deadWoods.get(i).getSuit()
                        && deadWoods.get(i).getSuit() == deadWoods.get(i - 1).getSuit()) {
                    int difference = deadWoods.get(i).getRankValue() - deadWoods.get(i - 1).getRankValue();
                    if (difference == 2) {
                        if (drawnCard.getRankValue() == (deadWoods.get(i).getRankValue() + deadWoods.get(i - 1).getRankValue()) / 2) {
                            Card[] temp = new Card[3];
                            temp[2] = deadWoods.remove(i);
                            temp[0] = deadWoods.remove(i - 1);
                            temp[1] = drawnCard;
                            RunMeld rm = Meld.buildRunMeld(temp);
                            //     System.out.println(rm);
                            if (i - 1 < deadWoods.size()) {
                                if (rm.canAppendCard(deadWoods.get(i - 1))) {
                                    rm.appendCard(deadWoods.remove(i - 1));
                                }
                            }
                            if (i - 1 > 0) {
                                if (rm.canAppendCard(deadWoods.get(i - 2))) {
                                    rm.appendCard(deadWoods.remove(i - 2));
                                }
                            }
                            melds.add(rm);
                            append = true;
                        }
                    } else if (difference == 1) {
                        if (drawnCard.getRankValue() == deadWoods.get(i).getRankValue() + 1) {
                            Card[] temp = new Card[3];
                            temp[1] = deadWoods.remove(i);
                            temp[0] = deadWoods.remove(i - 1);
                            temp[2] = drawnCard;
                            RunMeld rm = Meld.buildRunMeld(temp);
                            if (i < deadWoods.size()) {
                                if (rm.canAppendCard(deadWoods.get(i))) {
                                    rm.appendCard(deadWoods.remove(i));
                                }
                            }
                            if (i - 1 < deadWoods.size()) {
                                if (rm.canAppendCard(deadWoods.get(i - 1))) {
                                    rm.appendCard(deadWoods.remove(i - 1));
                                }
                            }
                            melds.add(rm);
                            append = true;
                        } else if (drawnCard.getRankValue() == deadWoods.get(i - 1).getRankValue() - 1) {
                            Card[] temp = new Card[3];
                            temp[0] = drawnCard;
                            temp[2] = deadWoods.remove(i);
                            temp[1] = deadWoods.remove(i - 1);
                            RunMeld rm = Meld.buildRunMeld(temp);
                            if (i - 1 > 0) {
                                if (rm.canAppendCard(deadWoods.get(i - 2))) {
                                    rm.appendCard(deadWoods.remove(i - 2));
                                }
                            }
                            if (i - 2 > 0) {
                                if (rm.canAppendCard(deadWoods.get(i - 3))) {
                                    rm.appendCard(deadWoods.remove(i - 3));
                                }
                            }
                            melds.add(rm);
                            append = true;
                        }
                    }
                }
            }
        }
        //the card simply add to hand
        if (!append) {
            for (int m = 0; m < deadWoods.size(); m++) {
                if (drawnCard.getSuit() == deadWoods.get(m).getSuit()) {
                    if (drawnCard.getRankValue() < deadWoods.get(m).getRankValue()) {
                        deadWoods.add(m, drawnCard);
                        break;
                    } else {
                        if (m == deadWoods.size() - 1) {
                            deadWoods.add(m + 1, drawnCard);
                            break;
                        } else if (m < deadWoods.size() - 1 && drawnCard.getSuit() != deadWoods.get(m + 1).getSuit()) {
                            deadWoods.add(m + 1, drawnCard);
                            break;
                        }
                    }
                } else if (m == deadWoods.size() - 1) {
                    deadWoods.add(drawnCard);
                    break;
                }
            }
        }
        //find a card to discard
        if (deadWoods.size() == 0) {
            for (int i = 0; i < melds.size(); i++) {
                for (int j = 0; j < melds.get(i).getCards().length; j++) {
                    if (melds.get(i).canRemoveCard(melds.get(i).getCards()[j])) {
                        Card temp = melds.get(i).getCards()[j];
                        melds.get(i).removeCard(temp);
                        //System.out.println("Discard " + temp.getRank() + "(" + temp.getSuit() + ")");
                        return temp;
                    }
                }
            }
        }

        //discard max to reduce points in deadWoods
        int max = 0;
        for (int j = 1; j < deadWoods.size(); j++) {
            if (deadWoods.get(max).getRankValue() < deadWoods.get(j).getRankValue()) {
                max = j;
            }
        }
//		System.out.println("Discard " + deadWoods.get(max).getRank() + "(" + deadWoods.get(max).getSuit() + ")");
        return deadWoods.remove(max);
    }

    @Override
    public boolean knock() {
        //check if the player can knock or not
        int count = 0;
        for (int i = 0; i < deadWoods.size(); i++) {
            count += deadWoods.get(i).getPointValue();
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
        //return all melds
        return melds;
    }

    @Override
    public void reset() {
        //reset all variables
        hearts.clear();
        spades.clear();
        clubs.clear();
        diamonds.clear();
        melds.clear();
        deadWoods.clear();
        hearts = null;
        spades = null;
        clubs = null;
        diamonds = null;
        melds = null;
        deadWoods = null;
        opponentHand = null;
        opponentMelds = null;
        drewDiscard = false;
        previousDiscardTop = null;
        opponentDiscarded = null;
    }

    public List<Card> getHand() {
        //return the current hand of the player
        List<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < melds.size(); i++) {
            cards.addAll(Arrays.asList(melds.get(i).getCards()));
        }
        cards.addAll(deadWoods);
        return cards;
    }

    public List<Card> getDeadWoods() {
        return this.deadWoods;
    }
}
