import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public final class Game {

    private int activePlayer;
    private final Players p;
    private static Player currentPlayer, nextPlayer;

    private boolean reverseGame = false;
    private boolean gameOver = false;

    private Card topCard;

    Scanner scan = new Scanner(System.in);
    Random r = new Random();


    // ------ CONSTRUCTOR ------ //

    public Game(int players, int activePlayer) {
        this.p = new Players(players);
        topCard = p.setTopCard();

        setActivePlayer(activePlayer);
        Player mainPlayer = p.getHands().get(this.activePlayer-1);

        int player = 0;
        setCurrentPlayer(player);

        while(!gameOver) {
            if(currentPlayer.equals(mainPlayer)) {
                chooseCard();
            }
            else {
                botTurn(player+1, topCard);
            }

            player = p.getHands().indexOf(nextPlayer);
            setCurrentPlayer(player);
        }
    }


    // ------ SETTERS ------ //

    public void setTopCard(Card c) {
        topCard = c;
    }

    public void setActivePlayer(int player) {
        activePlayer = player;
    }

    public void setCurrentPlayer(int player) {
        currentPlayer = p.getHands().get(player);
        setNextPlayer(player);
    }

    public void setNextPlayer(int player) {
        if(reverseGame) {
            int x;

            if(player == 0) {
                x = p.getHands().size() - 1;
            }
            else {
                x = player - 1;
            }

            nextPlayer = p.getHands().get(x);
        }
        else {
            if(player == p.getHands().size()-1) {
                nextPlayer = p.getHands().getFirst();
            }
            else {
                nextPlayer = p.getHands().get(player+1);
            }
        }
    }


    // ------ BOOLEAN FUNCTIONS ------ //

    public boolean isGameOver() {
        int n = p.getHands().indexOf(currentPlayer);

        if(p.getHands().get(n).getHand().isEmpty()) {
            System.out.println("\n"+currentPlayer.getName().toUpperCase()+" wins!");
            return true;
        }

        return false;
    }

    public boolean isCardValid(ArrayList<Integer> n, int x) {
        Card c = p.selectHand(activePlayer).get(n.get(x-1));

        if(c.getValue().equals("draw four") || c.getValue().equals("choose color")) {
            return true;
        }
        else if(!(c.getColor().equals(topCard.getColor())) && !(c.getValue().equals(topCard.getValue()))) {
            System.out.println("Please select the same color or value as the top card.");
            System.out.println("The top card is: "+topCard.getColor()+" "+topCard.getValue()+"\n");
        }
        return ((x <= n.size()) && (x > 0) && ((c.getColor().equals(topCard.getColor())) || (c.getValue().equals(topCard.getValue()))));
    }


    // ------ MAIN GAME FUNCTIONS ------ //

    public void chooseCard() {
        System.out.println("\nThe top card is: "+topCard.getColor().toUpperCase()+" "+topCard.getValue().toUpperCase()+".\n");
        ArrayList<Integer> n = p.getPlayer(1);

        for(int i = 1; i <= n.size(); i++) {
            System.out.print("("+i+")\t");

            if(p.selectHand(activePlayer).containsKey(n.get(i-1)) && p.selectHand(activePlayer) != null) {
                if(p.selectHand(1).get(n.get(i-1)).getColor() == null || p.selectHand(activePlayer).get(n.get(i-1)).getColor().isEmpty()) {
                    System.out.println(p.selectHand(1).get(n.get(i-1)).getValue().toUpperCase());
                }
                else {
                    System.out.println(p.selectHand(1).get(n.get(i-1)).getColor().toUpperCase()+" "+p.selectHand(1).get(n.get(i-1)).getValue().toUpperCase());
                }
            }
        }
        System.out.println("("+(n.size()+1)+")\tBuy card");

        int x;
        x = scan.nextInt();

        if(x == n.size()+1) {
            p.buyCard(1);
            System.out.println("\n"+currentPlayer.getName().toUpperCase()+" buys a card.");
        }
        else if((x < 1) || (x > n.size()+1)) {
            System.out.print("\nPlease, select a number from 1-"+(n.size()+1)+".\n");
        }
        else {
            while(!(isCardValid(n, x))) {
                x = scan.nextInt();
            }

            playCard(n, activePlayer, x);
        }
    }

    public void botTurn(int player, Card topCard) {
        HashMap<Integer, Card> hand = p.selectHand(player);
        ArrayList<Integer> keys = p.getPlayer(player);
        HashMap<Integer, Card> validCards = new HashMap<>();
        int n;

        for(int i = 0; i < hand.size(); i++) {
            n = keys.get(i);

            if(hand.containsKey(n) && hand.get(n) != null) {
                if(hand.get(n).getColor() == null || hand.get(n).getColor().isEmpty()) {
                    if(hand.get(n).getValue().equals("draw four") || hand.get(n).getValue().equals("choose color")) {
                        validCards.put(n, hand.get(n));
                    }
                }
                else if(hand.get(n).getColor().equals(topCard.getColor()) || hand.get(n).getValue().equals(topCard.getValue())) {
                    validCards.put(n, hand.get(n));
                }
            }
        }

        if(validCards.isEmpty()) {
            p.buyCard(player);
            System.out.println("\n"+currentPlayer.getName().toUpperCase()+" buys a card.");
        }
        else {
            ArrayList<Integer> validKeys = new ArrayList<>(validCards.keySet());
            int randomCard = 1;
            int rand;

            if(validCards.size() == 1) {
                rand = 1;
            }
            else {
                rand = r.nextInt(1,validCards.size());
            }

            for(int k : keys) {
                if(validKeys.get(rand-1) == k) {
                    randomCard = keys.indexOf(k) + 1;
                }
            }

            playCard(keys, player, randomCard);
        }
    }

    public void playCard(ArrayList<Integer> n, int player, int x) {
        Card c = p.selectHand(player).get(n.get(x-1));

        p.removeCard(player-1, x);
        setTopCard(c);

        switch(c.getValue()) {
            case "block" -> blockPlayer();
            case "reverse" -> reverse();
            case "choose color" -> chooseColor();
            case "draw four" -> drawFour();
            case "draw two" -> drawTwo();
            default -> {
            }
        }

        System.out.println("\n"+currentPlayer.getName().toUpperCase()+" plays "+printCard(c).toUpperCase()+".");
        System.out.println(currentPlayer.getName().toUpperCase()+" has "+cardsLeft(p.getHands().indexOf(currentPlayer))+" cards left.");
        gameOver = isGameOver();
    }


    // ------ WILD CARD FUNCTIONS ------ //

    public void blockPlayer() {
        System.out.println("\n"+currentPlayer.getName().toUpperCase()+" blocks "+nextPlayer.getName().toUpperCase()+".");
        setNextPlayer(p.getHands().indexOf(nextPlayer));
    }

    public void chooseColor() {
        int opt = 0;

        if(currentPlayer == p.getHands().get(activePlayer-1)) {
            while(opt < 1 || opt > 4) {
                System.out.println("Select a color:\n");
                System.out.println("(1)\tRed");
                System.out.println("(2)\tBlue");
                System.out.println("(3)\tYellow");
                System.out.println("(4)\tGreen");

                opt = scan.nextInt();
            }
        }
        else {
            opt = r.nextInt(1,4);
        }

        String c;

        switch(opt) {
            case 1 -> c = "red";
            case 2 -> c = "blue";
            case 3 -> c = "yellow";
            case 4 -> c = "green";
            default -> c = null;
        }

        topCard.setColor(c);
    }

    public void drawFour() {
        chooseColor();
        p.buyFour(nextPlayer);
        blockPlayer();
    }

    public void drawTwo() {
        p.buyTwo(nextPlayer);
        blockPlayer();
    }

    public void reverse() {
        reverseGame = !reverseGame;
        setNextPlayer(p.getHands().indexOf(currentPlayer));
        System.out.println("\nGame reversed!");
    }


    // ------ DISPLAY FUNCTIONS ------ //

    public int cardsLeft(int player) {
        ArrayList<Integer> keys = p.getPlayer(player+1);
        int cards = 0;
        for(int i = 0; i < keys.size(); i++) {
            cards++;
        }
        return cards;
    }

    public String printCard(Card c) {
        if(c.getColor() != null && !c.getColor().isEmpty()) {
            return c.getColor()+" "+c.getValue();
        }

        return c.getValue();
    }

}
