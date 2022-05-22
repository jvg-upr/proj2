// Hand.java - John K. Estell - 8 May 2003
// last modified: 23 Febraury 2004
// Implementation of a abstract hand of playing cards.
// Uses the Card class.  Requires subclass for specifying
// the specifics of what constitutes the evaluation of a hand
// for the game being implemented.

import java.util.*;
import java.util.ArrayList;

/**
 * Represents the basic functionality of a hand of cards.
 * Extensions of this class will provide the
 * definition of what constitutes a hand for that game and how hands are compared
 * to one another by overriding the <code>compareTo</code> method.
 * @author John K. Estell
 * @version 1.0
 */
public class Hand implements HandInterface {

   protected java.util.List hand = new ArrayList();


  /**
   * Adds a card to this hand.
   * @param card card to be added to the current hand.
   */
   public void addCard( Card card ) {
      hand.add( card );
      this.sort();
   }

  /**
   * Searches for the first instance of a set (3 or 4 Cards of the same rank) in the hand.
   * @return  returns Card [] of Cards found in deck or <code>-null </code> if not found.
   */
   public Card [] findSet( ) {
       HashMap<Character, ArrayList<Card>> sets = new HashMap<Character, ArrayList<Card>>();
       for(Object obj : hand) {
           Card card = (Card) obj;
           sets.compute(card.getRank(), (k, v) -> {
               if(v == null) v = new ArrayList<Card>();
               v.add(card);
               return v;
           });
       }

       for(Map.Entry<Character, ArrayList<Card>> entry : sets.entrySet()) {
           if(entry.getValue().size() >= 3) {
               return entry.getValue().toArray(new Card[entry.getValue().size()]);
           }
       }

       return null;
   }

  /**
   * Obtains the card stored at the specified location in the hand.  Does not
   * remove the card from the hand.
   * @param index position of card to be accessed.
   * @return the card of interest, or the null reference if the index is out of
   * bounds.
   */
   public Card getCard( int index ) {
      return (Card) hand.get( index );
   }


  /**
   * Removes the specified card from the current hand.
   * @param card the card to be removed.
   * @return the card removed from the hand, or null if the card
   * was not present in the hand.
   */
   public Card removeCard( Card card ) {
      int index = hand.indexOf( card );
      if ( index < 0 )
         return null;
      else
         return (Card) hand.remove( index );
   }


  /**
   * Removes the card at the specified index from the hand.
   * @param index poisition of the card to be removed.
   * @return the card removed from the hand, or the null reference if
   * the index is out of bounds.
   */
   public Card removeCard( int index ) {
      return (Card) hand.remove( index );
   }


  /**
   * Removes all the cards from the hand, leaving an empty hand.
   */
   public void discardHand() {
      hand.clear();
   }


  /**
   * The number of cards held in the hand.
   * @return number of cards currently held in the hand.
   */
   public int getNumberOfCards() {
      return hand.size();
   }


  /**
   * Sorts the card in the hand.
   * Sort is performed according to the order specified in the {@link Card} class.
   */
   public void sort() {
      Collections.sort( hand );
   }


  /**
   * Checks to see if the hand is empty.
   * @return <code>true</code> is the hand is empty.
   */
   public boolean isEmpty() {
      return hand.isEmpty();
   }


  /**
   * Determines whether or not the hand contains the specified card.
   * @param card the card being searched for in the hand.
   * @return <code>true</code> if the card is present in the hand.
   */
   public boolean containsCard( Card card ) {
      return false;
   }


  /**
   * Searches for the first instance of the specified card in the hand.
   * @param card card being searched for.
   * @return position index of card if found, or <code>-1</code> if not found.
   */
   public int findCard( Card card ) {
      return hand.indexOf(card);
   }


  /**
   *  Compares two hands.
   *  @param otherHandObject the hand being compared.
   *  @return < 0 if this hand is less than the other hand, 0 if the two hands are
   *  the same, or > 0 if this hand is greater then the other hand.
   */
   public int compareTo( Object otherHandObject ) {
      Hand otherHand = (Hand) otherHandObject;
      return evaluateHand() - otherHand.evaluateHand();
   }


    /**
     * Evaluates a hand according to the rules of the dumb card game.
     * Each card is worth its displayed pip value (ace = 1, two = 2, etc.)
     * in points with face cards worth ten points.  The value of a hand
     * is equal to the summation of the points of all the cards held in
     * the hand.
     */
    public int evaluateHand() {
        int value = 0;

        for ( int i = 0; i < getNumberOfCards(); i++ ) {
            Card c = getCard( i );
            int cardValue = Card.getRankIndex(c.getRank()) - Card.getRankIndex('a') + 1;
            if ( cardValue > 10 )
               cardValue = 10;
            value += cardValue;
        }

        return value;
    }


   /**
    * Returns a description of the hand.
    * @return a list of cards held in the hand.
    */
    public String toString() {
        return hand.toString();
    }


   /**
    * Replaces the specified card with another card.  Only the first
    * instance of the targeted card is replaced.  No action occurs if
    * the targeted card is not present in the hand.
    * @return <code>true</code> if the replacement occurs.
    */
    public boolean replaceCard( Card oldCard, Card replacementCard ) {
        int location = findCard( oldCard );
        if ( location < 0 )
           return false;
        hand.set( location, replacementCard );
        return true;
    }

    public Card[] findRun() {

        return null;
    }

    // Given a deck and a pile this hand will act as a player and make a valid
    // rummy play. Possibly modifies deck or discard pile.
    public Card[] play(Deck deck, Pile<Card> discard) {
        // make sure we don't blow up
        if(deck.isEmpty() || this.isEmpty()) return null;

        // I want to cry, it's so bad
        Card added;
        if(!discard.isEmpty() && (new Random()).nextBoolean()) {
            added = discard.peek();
            discard.pop();
        } else {
            added = deck.dealCard();
        }

        // WHY DO WE HAVE IO? well too bad it's 1 AM and idc
        System.out.println("\tAdded: " + added);

        this.addCard(added);
        Card[] set = this.findSet();

        // ah yes, everything can be null. Thanks java! \s
        if(set == null) {
            set = new Card[]{this.removeCard((new Random()).nextInt(hand.size()))};
            return set;
        }

        // these for loops would be so much nicer with streams but I don't have
        // time
        for(Card card : set) {
            this.removeCard(card);
        }

        return set;
    }
}
