// author: Jantony Velazquez Gauthier
// Implements a very simple Rummy-like game with 2 dumb AI's that play against
// each other.
public class CLI {
    public static void main(String[] args) {
        Deck deck = new Deck();

        // streams would be cool
        for(char s : Card.suit) {
            for(char r : Card.rank){
                deck.addCard(new Card(s, r));
            }
        }
        deck.shuffle();

        Pile<Card> discard = new Pile();

        // would love a draining stream here
        Hand p1 = new Hand();
        for(int i = 0; i < 9; i++) {
            p1.addCard(deck.dealCard());
        }
        Hand p2 = new Hand();
        for(int i = 0; i < 9; i++) {
            p2.addCard(deck.dealCard());
        }

        System.out.println("Initial Player 1: " + p1);
        System.out.println("Initial Player 2: " + p2);

        // I yearn from streams, if they're anywhere as useful as iterators in
        // functional languages. The fors physically hurt me ;(
        for(boolean turn = true; !(deck.isEmpty() || p1.isEmpty() || p2.isEmpty()); turn = !turn) {
            if(turn) {
                System.out.println("Player 1");
                Card[] discarded = p1.play(deck, discard);
                System.out.print("\tDiscarded: ");
                for(Card card : discarded) {
                    System.out.print(card + " ");
                    discard.push(card);
                }
                System.out.println();
                System.out.println("\tHand Now:" + p1);
            } else {
                System.out.println("Player 2");
                Card[] discarded = p2.play(deck, discard);
                System.out.print("\tDiscarded: ");
                for(Card card : discarded) {
                    System.out.print(card + " ");
                    discard.push(card);
                }
                System.out.println();
                System.out.println("\tHand Now:" + p2);
            }
        }

        int p1_val = p1.evaluateHand();
        int p2_val = p2.evaluateHand();
        if(p1_val == p2_val) {
            System.out.println("It's a tie");
        } else if(p1_val < p2_val) {
            System.out.println("Player 1 Wins!");
        } else {
            System.out.println("Player 2 Wins!");
        }
    }
}
