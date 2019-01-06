package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.example.Card.CardSuit;

public class SimplePlayer implements PlayerStrategy {
    List<Card> spades;//store cards of spades
    List<Card> hearts;//store cards of hearts
    List<Card> diamonds;//store cards of diamonds
    List<Card> clubs;//store cards of clubs
    List<Card> deadwoods;//store cards of deadwoods
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
        System.out.println("Receive:");
        hearts = new ArrayList<Card>();
        spades = new ArrayList<Card>();
        clubs = new ArrayList<Card>();
        diamonds = new ArrayList<Card>();
        deadwoods = new ArrayList<Card>();
        melds = new ArrayList<Meld>();

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
        for (int i = 0; i < hearts.size(); i++) {
            System.out.print(hearts.get(i).getRank() + "(HEARTS) ");
        }
        for (int i = 0; i < spades.size(); i++) {
            System.out.print(spades.get(i).getRank() + "(SPADES) ");
        }
        for (int i = 0; i < clubs.size(); i++) {
            System.out.print(clubs.get(i).getRank() + "(CLUBS) ");
        }
        for (int i = 0; i < diamonds.size(); i++) {
            System.out.print(diamonds.get(i).getRank() + "(DIAMONDS) ");
        }
        System.out.println();

        //find all melds and split hand to melds and deadwoods
        if (hearts.size() > 2) {
            int continous = 1;
            for (int i = 1; i < hearts.size(); i++) {
                if (hearts.get(i).getRankValue() - hearts.get(i - 1).getRankValue() == 1) {
                    continous += 1;
                    if (i == hearts.size() - 1) {
                        if (continous > 2) {
                            Card[] temp = new Card[continous];
                            for (int j = 0; j < continous; j++) {
                                temp[j] = hearts.get(i - continous + 1 + j);
                            }
                            RunMeld rm = Meld.buildRunMeld(temp);
                            melds.add(rm);
                        } else {
                            for (int j = 0; j < continous; j++) {
                                deadwoods.add(hearts.get(i - continous + 1 + j));
                            }
                        }
                    }
                } else {
                    if (continous > 2) {
                        Card[] temp = new Card[continous];
                        for (int j = 0; j < continous; j++) {
                            temp[j] = hearts.get(i - continous + j);
                        }
                        RunMeld rm = Meld.buildRunMeld(temp);
                        melds.add(rm);
                    } else {
                        for (int j = 0; j < continous; j++) {
                            deadwoods.add(hearts.get(i - continous + j));
                        }
                    }
                    if (i == hearts.size() - 1) {
                        deadwoods.add(hearts.get(i));
                    }
                    continous = 1;
                }
            }
        } else {
            deadwoods.addAll(hearts);
        }
        if (spades.size() > 2) {
            int continuous = 1;
            for (int i = 1; i < spades.size(); i++) {
                if (spades.get(i).getRankValue() - spades.get(i - 1).getRankValue() == 1) {
                    continuous += 1;
                    if (i == spades.size() - 1) {
                        if (continuous > 2) {
                            Card[] temp = new Card[continuous];
                            for (int j = 0; j < continuous; j++) {
                                temp[j] = spades.get(i - continuous + 1 + j);
                            }
                            RunMeld rm = Meld.buildRunMeld(temp);
                            melds.add(rm);
                        } else {
                            for (int j = 0; j < continuous; j++) {
                                deadwoods.add(spades.get(i - continuous + 1 + j));
                            }
                        }
                    }
                } else {
                    if (continuous > 2) {
                        Card[] temp = new Card[continuous];
                        for (int j = 0; j < continuous; j++) {
                            temp[j] = spades.get(i - continuous + j);
                        }
                        RunMeld rm = Meld.buildRunMeld(temp);
                        melds.add(rm);
                    } else {
                        for (int j = 0; j < continuous; j++) {
                            deadwoods.add(spades.get(i - continuous + j));
                        }
                    }
                    if (i == spades.size() - 1) {
                        deadwoods.add(spades.get(i));
                    }
                    continuous = 1;
                }
            }
        } else {
            deadwoods.addAll(spades);
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
                                deadwoods.add(clubs.get(i - continous + 1 + j));
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
                            deadwoods.add(clubs.get(i - continous + j));
                        }
                    }
                    if (i == clubs.size() - 1) {
                        deadwoods.add(clubs.get(i));
                    }
                    continous = 1;
                }
            }
        } else {
            deadwoods.addAll(clubs);
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
                                deadwoods.add(diamonds.get(i - continous + 1 + j));
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
                            deadwoods.add(diamonds.get(i - continous + j));
                        }
                    }
                    if (i == diamonds.size() - 1) {
                        deadwoods.add(diamonds.get(i));
                    }
                    continous = 1;
                }
            }
        } else {
            deadwoods.addAll(diamonds);
        }
    }

    /**
     * Called by the game engine to prompt the player on whether they want to take the top card
     * from the discard pile or from the deck.
     * <p>
     * In the Master AI, it checks whether the top card of the discard pile can make a new meld or not,
     * but there is no such function in SimplePlayer.
     * <p>
     * This is the major difference between master and SimplePlayer.
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
        System.out.println("Got " + drawnCard.getRank() + "(" + drawnCard.getSuit() + ")");
        boolean append = false;
        for (int i = 0; i < melds.size(); i++) {
            if (melds.get(i).canAppendCard(drawnCard)) {
                melds.get(i).appendCard(drawnCard);
                append = true;
                for (int j = 0; j < deadwoods.size(); j++) {
                    if (melds.get(i).canAppendCard(deadwoods.get(j))) {
                        melds.get(i).appendCard(deadwoods.remove(j));
                    }
                }
            }
        }
        //the card simply add to hand
        if (!append) {
            for (int m = 0; m < deadwoods.size(); m++) {
                if (drawnCard.getSuit() == deadwoods.get(m).getSuit()) {
                    deadwoods.add(m, drawnCard);
                    break;
                } else if (m == deadwoods.size() - 1) {
                    deadwoods.add(drawnCard);
                    break;
                }
            }
        }
        int max = 0;
        for (int j = 1; j < deadwoods.size(); j++) {
            if (deadwoods.get(max).getRankValue() < deadwoods.get(j).getRankValue()) {
                max = j;
            }
        }
        System.out.println("Discard " + deadwoods.get(max).getRank() + "(" + deadwoods.get(max).getSuit() + ")");
        return deadwoods.remove(max);
    }

    @Override
    public boolean knock() {
        //check if the player can knock or not
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
        deadwoods.clear();
        hearts = null;
        spades = null;
        clubs = null;
        diamonds = null;
        melds = null;
        deadwoods = null;
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
        cards.addAll(deadwoods);
        return cards;
    }

    public List<Card> getDeadWoods() {
        return this.deadwoods;
    }
}
