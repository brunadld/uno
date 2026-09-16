import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Players {

    private static ArrayList<Integer> deckKeys;
    private static final Random r = new Random();

    private final ArrayList<Player> hands = new ArrayList<>();
    private final String[] wildCards = {"block", "reverse", "draw four", "draw two", "choose color"};

    private final Deck d = new Deck();


    // ------ CONSTRUCTOR ------ //

    public Players(int players) {

        // ------ FIX THE NUMBER OF PLAYERS IF NOT BETWEEN 2 AND 4 ------ //
        
        if(players > 4) {
            players = 4;
        }
        else if(players < 2) {
            players = 2;
        }


        // ------ DISTRIBUTE PLAYER CARDS ------ //

        deckKeys = d.getKeys();
        ArrayList<Integer> tempKeys;
        HashMap<Integer, Card> tempHand;
        String[] names = {"Player 1", "Player 2", "Player 3", "Player 4"};

        for(int i = 0; i < players; i++) {
            tempKeys = getKeys(deckKeys);
            deckKeys = updateDeckKeys(tempKeys);
            tempHand = setCards(d, tempKeys);
            hands.add(new Player(tempKeys, tempHand, names[i]));
        }

    }


    // ------ CARD DISTRIBUTION ------ //

    public static ArrayList<Integer> getKeys(ArrayList<Integer> deckKeys) {
        ArrayList<Integer> userKeys = new ArrayList<>();
        int n, x;

        for(int i = 0; i < 7; i++) {
            n = r.nextInt(deckKeys.size());
            x = deckKeys.get(n);

            userKeys.add(x);
        }

        return userKeys;
    }

    public static ArrayList<Integer> updateDeckKeys(ArrayList<Integer> u) {
        for(int i : u) {
            if(deckKeys.contains(i)) {
                deckKeys.remove(Integer.valueOf(i));
            }
        }
        return deckKeys;
    }

    public static HashMap<Integer, Card> setCards(Deck d, ArrayList<Integer> k) {
        HashMap<Integer, Card> h = new HashMap<>();
        Card c;
        for(int i : k) {
            c = d.getCard(i);
            h.put(i, c);
        }

        return h;
    }

    public Card setTopCard() {
        int index = r.nextInt(deckKeys.size());
        int cardId = deckKeys.get(index);
        boolean valid = false;

        String v = d.getCard(cardId).getValue();
        while(!valid) {
            valid = true;
            for(String str : wildCards) {
                if(v.equals(str)) {
                    valid = false;
                    index = r.nextInt(deckKeys.size());
                    cardId = deckKeys.get(index);
                }
            }
        }

        deckKeys.remove(index);
        return d.getCard(cardId);
    }

    public void refreshDeck(Card t) {
        if(deckKeys.isEmpty()) {
            d.getKeys();
            ArrayList<Integer> tempKeys;

            for(Player p : hands) {
                tempKeys = p.getPlayerKeys();
                updateDeckKeys(tempKeys);
            }

            int topCard = d.findElementKey(t);
            deckKeys.remove((Integer) topCard);
        }
    }


    // ------ GETTERS ------ //

    public ArrayList<Integer> getPlayer(int user) {
        return hands.get(user-1).getPlayerKeys();
    }

    public HashMap<Integer, Card> selectHand(int player) {
        return hands.get(player-1).getHand();
    }

    public ArrayList<Player> getHands() {
        return hands;
    }


    // ------ GAME FUNCTIONS ------ //

    public void buyCard(int player) {
        ArrayList<Integer> k = getPlayer(player);
        HashMap<Integer, Card> h = selectHand(player);

        int x = r.nextInt(deckKeys.size());
        int n = deckKeys.remove(x); 

        h.put(n, d.getCard(n));
        k.add(n);
}

    public void removeCard(int player, int x) {
        hands.get(player).removeFromHand(x);
    }

    public void buyFour(Player player) {
        ArrayList<Integer> k = player.getPlayerKeys();
        HashMap<Integer, Card> h = player.getHand();

        int x, n;
        for(int i = 0; i < 4; i++) {
            x = r.nextInt(deckKeys.size());
            n = deckKeys.remove(x);

            h.put(n, d.getCard(n));
            k.add(n);
        }
    }

    public void buyTwo(Player player) {
        ArrayList<Integer> k = player.getPlayerKeys();
        HashMap<Integer, Card> h = player.getHand();

        int x, n;
        for(int i = 0; i < 2; i++) {
            x = r.nextInt(deckKeys.size());
            n = deckKeys.remove(x);

            h.put(n, d.getCard(n));
            k.add(n);
        }
    }

}
