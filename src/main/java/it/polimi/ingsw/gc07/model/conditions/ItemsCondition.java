package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.ConditionType;
import it.polimi.ingsw.gc07.model.GameItem;

import java.util.List;
import java.util.ArrayList;

/**
 * Condition regarding the presence of some given items on the game field.
 */
public class ItemsCondition extends Condition {
    /**
     * List of GameItems that need to be found on the game field in order satisfy the condition.
     */
    private final List<GameItem> neededItems;

    /**
     * Constructor for the class ItemsCondition.
     * @param conditionType type of the condition, must be ITEM_CONDITION
     * @param neededItems List of items that need to be on the game field in
     *                    order satisfy the condition
     */
    public ItemsCondition(ConditionType conditionType, List<GameItem> neededItems) {
        super(conditionType);
        this.neededItems = new ArrayList<>(neededItems);
    }

    /**
     * Getter method returning the List of items required by the condition.
     * @return List of Items of the condition.
     */
    public List<GameItem> getNeededItems() {
        return new ArrayList<>(this.neededItems);
    }

    /**
     * Method returning the number of times an item condition is met.
     * Counts how many times the list neededItems is found in the gameField.
     * @param gameField game field on which the condition has to be verified
     * @return number of times the list of items is found
     */
    public int numTimesMet(GameField gameField) throws NullPointerException {
        // TODO implementare
        // creo una lista di elementi che trovo
        // scorro tutte le posizioni:
        // - controllo se è presente una carta
        // - aggiungo le risorse permanenti
        // - guardo se è fronte o retro
        // - per ogni angolo se ha angoli scoperti (sia la carta, sia se sono coperti da altre carte),
        //   se li ha, cosa contengono
        //   possono contenere un gameitem che aggiungo alla lista
        // alla fine conto quante volte la lista di needed items sta nella lista di item

        // check valid game field
        if(gameField == null){
            throw new NullPointerException();
        }
        int dim = gameField.getDim();

        // create list of items found on the game field
        List<GameItem> foundItems = new ArrayList<>();
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                // for every possible position on the game field
                if(gameField.isCardPresent(i,j)){
                    try{
                        PlaceableCard placedCard = gameField.getPlacedCard(i,j);
                        if (!gameField.getCardWay(i,j)) {
                            // card placed face up (no permanent resources)
                            boolean[] frontCorners = placedCard.getFrontCorners(); // TODO: se null ? per la costruzione non può esserlo!
                            GameItem[] frontCornersContent = placedCard.getFrontCornersContent(); // TODO: se null ? per la costruzione non può esserlo!
                            // all placeable cards can have temporary resources on the front
                            // TODO
                            // controllo se ci sono carte nei quattro angoli
                            // se c'è controllo se è sopra o sotto, se sopra basta
                            // se è sotto, controllo se la carta ha un angolo e
                            // se sì, se ha una risorsa nell'angolo
                        }
                        else {
                            // card placed face down
                            // add permanent resources to foundItems
                            foundItems.addAll(gameField.getPlacedCard(i,j).getPermanentResources());
                            // only starter cards can have temporary resources on the back
                            boolean[] backCorners = placedCard.getBackCorners(); // TODO: se null ? per la costruzione non può esserlo!
                            GameItem[] backCornersContent = placedCard.getBackCornersContent(); // TODO: se null ? per la costruzione non può esserlo!
                            if(backCorners != null){
                                // starter card
                                // TODO
                                // controllo se c'è una carta in (1,1) poi (1,-1), (-1,-1) e (-1,1)
                                // se c'è una carta, sicuramente copre la starter card, quindi copre
                                // un'eventuale risorsa nell'angolo
                                // se non c'è una carta, vedo se c'è l'angolo e se c'è, se c'è una
                                // risorsa nell'angolo
                            }
                        }
                    } catch (CardNotPresentException e) {
                        // I have checked before!
                        e.printStackTrace();
                    }
                }
            }
        }


        return 0; // TODO cambiare
    }
}
