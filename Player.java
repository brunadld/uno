import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private final ArrayList<Integer> keys;
    private final HashMap<Integer, Card> hand;
    private final String name;

    public Player(ArrayList<Integer> k, HashMap<Integer, Card> h, String s) {
        keys = k;
        hand = h;
        name = s;
    }

    public ArrayList<Integer> getPlayerKeys() {
        return keys;
    }

    public HashMap<Integer, Card> getHand() {
        return hand;
    }

    public void removeFromHand(int x) {
        hand.remove(keys.get(x-1));
        keys.remove(x-1);
    }

    public String getName() {
        return name;
    }
}
