package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.controller.Game;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.model.enumerations.GameObject;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemsConditionTest {

    @Test
    public void constructorAndGetter() {
        List<GameItem> myList = new ArrayList<>();
        myList.add(GameObject.QUILL);
        myList.add(GameObject.INKWELL);
        myList.add(GameResource.ANIMAL);
        myList.add(GameResource.FUNGI);
        ItemsCondition condition = new ItemsCondition(myList);
        List<GameItem> checkList = condition.getNeededItems();
        assertNotNull(checkList);
        assertEquals(myList, checkList);
    }

}