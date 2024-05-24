package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.enumerations.CardType;

import java.util.List;
import java.util.ArrayList;

/**
 * Condition regarding the presence of some given items on the game field.
 */
public class ItemsCondition implements Condition {
    /**
     * List of GameItems that need to be found on the game field in order satisfy the condition.
     */
    private final List<GameItem> neededItems;

    /**
     * Constructor for the class ItemsCondition.
     * @param neededItems List of items that needs to be on the game field in order satisfy the condition
     */
    public ItemsCondition(List<GameItem> neededItems) {
        this.neededItems = new ArrayList<>(neededItems);
    }

    /**
     * Method returning the number of times an item condition is met.
     * Counts how many times the list neededItems is found in the gameField.
     * It checks for every position containing a card if it has permanent
     * or temporary resources. Then computes the result.
     * @param gameField game field on which the condition has to be verified
     * @return number of times the list of items is found
     */
    @Override
    public int numTimesMet(GameField gameField) {
        // check valid game field
        assert(gameField != null): "No GameField passed as parameter";
        int dim = GameField.getDim();

        // create list of items found on the game field
        List<GameItem> foundItems = new ArrayList<>();
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                // for every possible position on the game field
                if(gameField.isCardPresent(i,j)){
                    PlaceableCard placedCard = gameField.getPlacedCard(i,j);
                    if (!gameField.getCardWay(i,j)) {
                        // card placed face up (no permanent resources)
                        boolean[] frontCorners = placedCard.getFrontCorners();
                        assert(frontCorners != null) : "frontCorners can't be null";
                        GameItem[] frontCornersContent = placedCard.getFrontCornersContent();
                        assert(frontCornersContent != null) : "frontCornersContent can't be null";
                        // all placeable cards can have temporary resources on the front

                        // check position (i-1,j+1)
                        if((i == 0 || j == dim-1) ||
                                (i>0 && j<dim-1 && !gameField.isCardPresent(i-1,j+1)) ||
                                (i>0 && j<dim-1 && gameField.isCardPresent(i-1,j+1) && gameField.getCardsOrder()[i][j]>gameField.getCardsOrder()[i-1][j+1])) {
                            // uncovered top right corner
                            if(frontCorners[1] && frontCornersContent[1] != null)
                                foundItems.add(frontCornersContent[1]);
                        }
                        // check position (i+1,j+1)
                        if((i == dim-1 || j == dim-1) ||
                                (i<dim-1 && j<dim-1 && !gameField.isCardPresent(i+1,j+1)) ||
                                (i<dim-1 && j<dim-1 && gameField.isCardPresent(i+1,j+1) && gameField.getCardsOrder()[i][j]>gameField.getCardsOrder()[i+1][j+1])) {
                            // uncovered bottom right corner
                            if(frontCorners[2] && frontCornersContent[2] != null)
                                foundItems.add(frontCornersContent[2]);
                        }
                        // check position (i+1,j-1)
                        if((i == dim-1 || j == 0) ||
                                (i<dim-1 && j>0 && !gameField.isCardPresent(i+1,j-1)) ||
                                (i<dim-1 && j>0 && gameField.isCardPresent(i+1,j-1) && gameField.getCardsOrder()[i][j]>gameField.getCardsOrder()[i+1][j-1])) {
                            // uncovered bottom left corner
                            if(frontCorners[3] && frontCornersContent[3] != null)
                                foundItems.add(frontCornersContent[3]);
                        }
                        // check position (i-1,j-1)
                        if((i == 0 || j == 0) ||
                                (i>0 && j>0 && !gameField.isCardPresent(i-1,j-1)) ||
                                (i>0 && j>0 && gameField.isCardPresent(i-1,j-1) && gameField.getCardsOrder()[i][j]>gameField.getCardsOrder()[i-1][j-1])) {
                            // uncovered top left corner
                            if(frontCorners[0] && frontCornersContent[0] != null)
                                foundItems.add(frontCornersContent[0]);
                        }
                    }
                    else {
                        // card placed face down
                        // add permanent resources to foundItems
                        foundItems.addAll(gameField.getPlacedCard(i,j).getPermanentResources());

                        // only starter cards can have temporary resources on the back
                        if(placedCard.getType().equals(CardType.STARTER_CARD)) {
                            boolean[] backCorners = placedCard.getBackCorners();
                            assert(backCorners != null): "backCorners can't be null";
                            GameItem[] backCornersContent = placedCard.getBackCornersContent();
                            assert(backCornersContent != null): "backCorners can't be null";

                            // checks if a card is present in the 4 corners
                            // if yes, it covers the starter card
                            // if not, checks if the starter card has a resource in that corner

                            // check position (39,41)
                            if(!gameField.isCardPresent(39,41)){
                                // uncovered top right corner
                                if(backCorners[1] && backCornersContent[1] != null) {
                                    foundItems.add(backCornersContent[1]);
                                }
                            }
                            // check position (41,41)
                            if(!gameField.isCardPresent(41,41)){
                                // uncovered bottom right corner
                                if(backCorners[2] && backCornersContent[2] != null) {
                                    foundItems.add(backCornersContent[2]);
                                }
                            }
                            // check position (41,39)
                            if(!gameField.isCardPresent(41,39)){
                                // uncovered bottom left corner
                                if(backCorners[3] && backCornersContent[3] != null) {
                                    foundItems.add(backCornersContent[3]);
                                }
                            }
                            // check position (39,39)
                            if(!gameField.isCardPresent(39,39)){
                                // uncovered top left corner
                                if(backCorners[0] && backCornersContent[0] != null) {
                                    foundItems.add(backCornersContent[0]);
                                }
                            }
                        }
                    }
                }
            }
        }

        // check how many times neededItems is in foundItems
        int numTimes = 0;
        boolean flag = true;
        while(flag) {
            for(GameItem g: neededItems) {
                if(foundItems.contains(g)){
                    foundItems.remove(g);
                }
                else{
                    flag = false;
                }
            }
            if(flag)
                numTimes++;
        }

        return numTimes;
    }

    /**
     * getter method for attribute neededItems
     * @return neededItems
     */
    public List<GameItem> getNeededItems() {
        return neededItems;
    }
}
