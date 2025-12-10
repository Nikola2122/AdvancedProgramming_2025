package AUD2;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum Type {
    HEARTS,
    DIAMONDS,
    SPADES,
    CLUBS
}

class PlayingCard{
    private Type type;
    private int number;
    private boolean isDealt;

    public PlayingCard(Type type, int number, boolean isDealt) {
        this.type = type;
        this.number = number;
        this.isDealt = isDealt;
    }

    @Override
    public String toString() {
        return String.format("Card type: %s, number: %d", type.toString(), number);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDealt(boolean dealt) {
        isDealt = dealt;
    }

    public boolean isDealt() {
        return isDealt;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Type getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }
}

class Deck{
    private ArrayList<PlayingCard> cards;
    private int cardsDealt;
    public Deck() {
        cardsDealt = 0;
        cards = Arrays.stream(Type.values()).flatMap(Type ->
                IntStream.range(0,13).mapToObj(i->new PlayingCard(Type,i+2, false))
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<PlayingCard> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return cards.stream().map(PlayingCard::toString).reduce("",(finalS,str)->finalS.concat(str).concat("\n"));
    }

    public boolean hasCards(){
        return cardsDealt!=52;
    }

    public ArrayList<PlayingCard> shuffle(){
        Collections.shuffle(cards);
        this.cards.stream().forEach(i->i.setDealt(false));
        cardsDealt = 0;
        return cards;
    }

    public PlayingCard dealCard(){
        if(!hasCards()) return null;

        int card = (int)(Math.random() * 52);
        if(!cards.get(card).isDealt()){
            cards.get(card).setDealt(true);
            cardsDealt++;
            return cards.get(card);
        }
        return dealCard();
    }

}

class MultipleDecks{
    ArrayList<Deck> decks;
    int numberOfDecks;

    public MultipleDecks(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
        decks = new ArrayList<>();
        IntStream.range(0,numberOfDecks).forEach(i->decks.add(new Deck()));
    }

    @Override
    public String toString() {
        return decks.stream().map(Deck::toString).collect(Collectors.joining("\n"));
    }
}
public class PlayingCardGame {
    public static void main(String[] args) {
        Deck deck = new Deck();
        PlayingCard card;
        while((card = deck.dealCard()) != null){
            System.out.println(card);
        }
        System.out.println("NEW LINE");
        deck.shuffle();
        while((card = deck.dealCard()) != null){
            System.out.println(card);
        }
    }
}
