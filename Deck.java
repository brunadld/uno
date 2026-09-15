import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Deck {

    private HashMap<Integer, Card> deck;

    private void fillDeck() {
        this.deck = new HashMap<>();
        int i = 1;

        File file = new File("cards.txt");
        try(Scanner s = new Scanner(file)){
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] array = line.split(",");

                deck.put(i, new Card(array[0], array[1]));
                i++;
            }
        } catch(FileNotFoundException e){
            System.out.println("File not found!");
        }
    }

    public Deck() {
        fillDeck();
    }

    public int getSize() {
        return this.deck.size();
    }

    public ArrayList<Integer> getKeys() {
        ArrayList<Integer> deckKeys = new ArrayList<>();

        for(int i = 1; i <= getSize(); i++) {
            deckKeys.add(i);
        }

        return deckKeys;
    }

    public Card getCard(int key) {
        return deck.get(key);
    }

}