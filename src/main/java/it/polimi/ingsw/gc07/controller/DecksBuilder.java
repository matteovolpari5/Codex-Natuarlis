package it.polimi.ingsw.gc07.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.conditions.Condition;
import it.polimi.ingsw.gc07.model.conditions.ItemsCondition;
import it.polimi.ingsw.gc07.model.conditions.LayoutCondition;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.GameObject;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DecksBuilder {

    private static boolean[] extractFrontCorners(JsonObject cardJsonObject) {
        boolean[] frontCorners = new boolean[4];

        String frontcorner0 = cardJsonObject.get("frontcorner0").getAsString();
        String frontcorner1 = cardJsonObject.get("frontcorner1").getAsString();
        String frontcorner2 = cardJsonObject.get("frontcorner2").getAsString();
        String frontcorner3 = cardJsonObject.get("frontcorner3").getAsString();

        if(frontcorner0.equals("true")){
            frontCorners[0] = true;
        }
        else{
            frontCorners[0] = false;
        }
        if(frontcorner1.equals("true")){
            frontCorners[1] = true;
        }
        else{
            frontCorners[1] = false;

        }
        if(frontcorner2.equals("true")){
            frontCorners[2] = true;
        }
        else{
            frontCorners[2] = false;
        }
        if(frontcorner3.equals("true")){
            frontCorners[3] = true;
        }
        else{
            frontCorners[3] = false;
        }
        return frontCorners;
    }

    private static GameItem[] extractFrontCornersContent(JsonObject cardJsonObject){
        GameItem[] frontCornersContent = new GameItem[4];
        // frontCornersContent
        String frontcontent0String = cardJsonObject.get("frontcontent0").getAsString();
        String frontcontent1String = cardJsonObject.get("frontcontent1").getAsString();
        String frontcontent2String = cardJsonObject.get("frontcontent2").getAsString();
        String frontcontent3String = cardJsonObject.get("frontcontent3").getAsString();

        switch(frontcontent0String){
            case "plant":
                frontCornersContent[0] = GameResource.PLANT;
                break;
            case "animal":
                frontCornersContent[0] = GameResource.ANIMAL;
                break;
            case "fungi":
                frontCornersContent[0] = GameResource.FUNGI;
                break;
            case "insect":
                frontCornersContent[0] = GameResource.INSECT;
                break;
            case "quill":
                frontCornersContent[0] = GameObject.QUILL;
                break;
            case "inkwell":
                frontCornersContent[0] = GameObject.INKWELL;
                break;
            case "manuscript":
                frontCornersContent[0] = GameObject.MANUSCRIPT;
                break;
            case "null":
                frontCornersContent[0] = null;
                break;

        }
        switch(frontcontent1String){
            case "plant":
                frontCornersContent[1] = GameResource.PLANT;
                break;
            case "animal":
                frontCornersContent[1] = GameResource.ANIMAL;
                break;
            case "fungi":
                frontCornersContent[1] = GameResource.FUNGI;
                break;
            case "insect":
                frontCornersContent[1] = GameResource.INSECT;
                break;
            case "quill":
                frontCornersContent[1] = GameObject.QUILL;
                break;
            case "inkwell":
                frontCornersContent[1] = GameObject.INKWELL;
                break;
            case "manuscript":
                frontCornersContent[1] = GameObject.MANUSCRIPT;
                break;
            case "null":
                frontCornersContent[1] = null;
                break;
        }
        switch(frontcontent2String){
            case "plant":
                frontCornersContent[2] = GameResource.PLANT;
                break;
            case "animal":
                frontCornersContent[2] = GameResource.ANIMAL;
                break;
            case "fungi":
                frontCornersContent[2] = GameResource.FUNGI;
                break;
            case "insect":
                frontCornersContent[2] = GameResource.INSECT;
                break;
            case "quill":
                frontCornersContent[2] = GameObject.QUILL;
                break;
            case "inkwell":
                frontCornersContent[2] = GameObject.INKWELL;
                break;
            case "manuscript":
                frontCornersContent[2] = GameObject.MANUSCRIPT;
                break;
            case "null":
                frontCornersContent[2] = null;
                break;
        }
        switch(frontcontent3String){
            case "plant":
                frontCornersContent[3] = GameResource.PLANT;
                break;
            case "animal":
                frontCornersContent[3] = GameResource.ANIMAL;
                break;
            case "fungi":
                frontCornersContent[3] = GameResource.FUNGI;
                break;
            case "insect":
                frontCornersContent[3] = GameResource.INSECT;
                break;
            case "quill":
                frontCornersContent[3] = GameObject.QUILL;
                break;
            case "inkwell":
                frontCornersContent[3] = GameObject.INKWELL;
                break;
            case "manuscript":
                frontCornersContent[3] = GameObject.MANUSCRIPT;
                break;
            case "null":
                frontCornersContent[3] = null;
                break;
        }
        return frontCornersContent;
    }

    private static boolean[] extractBackCorners(JsonObject cardJsonObject) {
        boolean[] backCorners = new boolean[4];

        String backcorner0 = cardJsonObject.get("backcorner0").getAsString();
        String backcorner1 = cardJsonObject.get("backcorner1").getAsString();
        String backcorner2 = cardJsonObject.get("backcorner2").getAsString();
        String backcorner3 = cardJsonObject.get("backcorner3").getAsString();
        if(backcorner0.equals("true")){
            backCorners[0] = true;
        }
        else{
            backCorners[0] = false;
        }
        if(backcorner1.equals("true")){
            backCorners[1] = true;
        }
        else{
            backCorners[1] = false;

        }
        if(backcorner2.equals("true")){
            backCorners[2] = true;
        }
        else{
            backCorners[2] = false;
        }
        if(backcorner3.equals("true")){
            backCorners[3] = true;
        }
        else{
            backCorners[3] = false;
        }
        return backCorners;
    }

    private static GameItem[] extractBackCornersContent(JsonObject cardJsonObject) {
        GameItem[] backCornersContent = new GameItem[4];

        String backcontent0String = cardJsonObject.get("backcontent0").getAsString();
        String backcontent1String = cardJsonObject.get("backcontent1").getAsString();
        String backcontent2String = cardJsonObject.get("backcontent2").getAsString();
        String backcontent3String = cardJsonObject.get("backcontent3").getAsString();
        switch(backcontent0String){
            case "plant":
                backCornersContent[0] = GameResource.PLANT;
                break;
            case "animal":
                backCornersContent[0] = GameResource.ANIMAL;
                break;
            case "fungi":
                backCornersContent[0] = GameResource.FUNGI;
                break;
            case "insect":
                backCornersContent[0] = GameResource.INSECT;
                break;
            case "quill":
                backCornersContent[0] = GameObject.QUILL;
                break;
            case "inkwell":
                backCornersContent[0] = GameObject.INKWELL;
                break;
            case "manuscript":
                backCornersContent[0] = GameObject.MANUSCRIPT;
                break;
            case "null":
                backCornersContent[0] = null;
                break;
        }
        switch(backcontent1String){
            case "plant":
                backCornersContent[1] = GameResource.PLANT;
                break;
            case "animal":
                backCornersContent[1] = GameResource.ANIMAL;
                break;
            case "fungi":
                backCornersContent[1] = GameResource.FUNGI;
                break;
            case "insect":
                backCornersContent[1] = GameResource.INSECT;
                break;
            case "quill":
                backCornersContent[1] = GameObject.QUILL;
                break;
            case "inkwell":
                backCornersContent[1] = GameObject.INKWELL;
                break;
            case "manuscript":
                backCornersContent[1] = GameObject.MANUSCRIPT;
                break;
            case "null":
                backCornersContent[1] = null;
                break;
        }
        switch(backcontent2String){
            case "plant":
                backCornersContent[2] = GameResource.PLANT;
                break;
            case "animal":
                backCornersContent[2] = GameResource.ANIMAL;
                break;
            case "fungi":
                backCornersContent[2] = GameResource.FUNGI;
                break;
            case "insect":
                backCornersContent[2] = GameResource.INSECT;
                break;
            case "quill":
                backCornersContent[2] = GameObject.QUILL;
                break;
            case "inkwell":
                backCornersContent[2] = GameObject.INKWELL;
                break;
            case "manuscript":
                backCornersContent[2] = GameObject.MANUSCRIPT;
                break;
            case "null":
                backCornersContent[2] = null;
                break;
        }
        switch(backcontent3String){
            case "plant":
                backCornersContent[3] = GameResource.PLANT;
                break;
            case "animal":
                backCornersContent[3] = GameResource.ANIMAL;
                break;
            case "fungi":
                backCornersContent[3] = GameResource.FUNGI;
                break;
            case "insect":
                backCornersContent[3] = GameResource.INSECT;
                break;
            case "quill":
                backCornersContent[3] = GameObject.QUILL;
                break;
            case "inkwell":
                backCornersContent[3] = GameObject.INKWELL;
                break;
            case "manuscript":
                backCornersContent[3] = GameObject.MANUSCRIPT;
                break;
            case "null":
                backCornersContent[3] = null;
                break;
        }
        return backCornersContent;
    }

    private static List<GameResource> extractPermanentResources(JsonObject cardJsonObject) {
        List<GameResource> permanentResources = new ArrayList<>();

        JsonArray permanentResourcesArray = cardJsonObject.get("permanentresources").getAsJsonArray();
        for(JsonElement r: permanentResourcesArray){
            // for every resource
            JsonObject resourceJsonObject = r.getAsJsonObject();
            String resourceString = resourceJsonObject.get("resource").getAsString();
            GameResource gameResource = null;
            switch(resourceString) {
                case "plant":
                    gameResource = GameResource.PLANT;
                    break;
                case "animal":
                    gameResource = GameResource.ANIMAL;
                    break;
                case "fungi":
                    gameResource = GameResource.FUNGI;
                    break;
                case "insect":
                    gameResource = GameResource.INSECT;
                    break;
            }
            permanentResources.add(gameResource);
        }
        return permanentResources;
    }

    private static GameResource[][] extractLayoutCondition(JsonObject conditionObject){
        GameResource[][] cardsColor = new GameResource[4][3];
        int row = 0;
        int col = 0;
        JsonArray cardsColorArray = conditionObject.get("cardscolor").getAsJsonArray();
        for(JsonElement color: cardsColorArray){
            // for every element of the array
            JsonObject colorObject = color.getAsJsonObject();
            // extract the color
            String colorString = colorObject.get("cardscolor").getAsString();
            switch(colorString){
                case "fungi":
                    cardsColor[row][col] = GameResource.FUNGI;
                    break;
                case "plant":
                    cardsColor[row][col] = GameResource.PLANT;
                    break;
                case "insect":
                    cardsColor[row][col] = GameResource.INSECT;
                    break;
                case "animal":
                    cardsColor[row][col] = GameResource.ANIMAL;
                    break;
                case "null":
                    cardsColor[row][col] = null;
                    break;
            }
            if(col < 2){
                // not last column, go to next column, same row
                col ++;
            }
            else{
                // last column, go to next row, first column
                row ++;
                col = 0;
            }
        }
        return cardsColor;
    }

    private static List<GameItem> extractItemsCondition(JsonObject conditionObject) {
        List<GameItem> neededItems = new ArrayList<>();
        JsonArray neededItemsArray = conditionObject.get("neededItems").getAsJsonArray();
        for(JsonElement itemElement: neededItemsArray){
            // get json object
            JsonObject itemObject = itemElement.getAsJsonObject();
            String itemString = itemObject.get("resource").getAsString();
            switch(itemString){
                case "plant":
                    neededItems.add(GameResource.PLANT);
                    break;
                case "animal":
                    neededItems.add(GameResource.ANIMAL);
                    break;
                case "fungi":
                    neededItems.add(GameResource.FUNGI);
                    break;
                case "insect":
                    neededItems.add(GameResource.INSECT);
                    break;
                case "quill":
                    neededItems.add(GameObject.QUILL);
                    break;
                case "inkwell":
                    neededItems.add(GameObject.INKWELL);
                    break;
                case "manuscript":
                    neededItems.add(GameObject.MANUSCRIPT);
                    break;
            }
        }
        return neededItems;
    }

    /**
     * Method that builds a starter card deck from the json file starterCardsDeck.json
     * @return starter card deck
     * @throws FileNotFoundException
     */
    public static Deck<PlaceableCard> buildStarterCardsDeck() throws FileNotFoundException{
        Stack<PlaceableCard> deckContent = new Stack<>();
        File input = new File("src/main/resources/it/polimi/ingsw/gc07/starterCardsDeck.json");
        JsonElement fileElement;
        JsonObject fileObject;
        fileElement = JsonParser.parseReader(new FileReader(input));
        fileObject = fileElement.getAsJsonObject();
        // get the JsonArray of all the cards
        JsonArray jsonArrayStarterCards = fileObject.get("cards").getAsJsonArray();
        for(JsonElement c: jsonArrayStarterCards){
            // for every card, extract attributes
            JsonObject cardJsonObject = c.getAsJsonObject();

            // the card type is CardType.STARTER_CARD for all cards in the deck
            CardType type = CardType.STARTER_CARD;

            int id = cardJsonObject.get("id").getAsInt();
            boolean[] backCorners = DecksBuilder.extractBackCorners(cardJsonObject);
            GameItem[] backCornersContent = DecksBuilder.extractBackCornersContent(cardJsonObject);
            boolean[] frontCorners =  DecksBuilder.extractFrontCorners(cardJsonObject);
            GameItem[] frontCornersContent =  DecksBuilder.extractFrontCornersContent(cardJsonObject);
            List<GameResource> permanentResources = DecksBuilder.extractPermanentResources(cardJsonObject);

            PlaceableCard card = new PlaceableCard(id, type, frontCorners, frontCornersContent, backCorners, backCornersContent, permanentResources);
            deckContent.add(card);
        }
        return new Deck<>(CardType.STARTER_CARD, deckContent);
    }

    /**
     * Method that builds an objective card deck from the json file objectiveCardsDeck.json
     * @return objective card deck
     * @throws FileNotFoundException
     */
    public static PlayingDeck<ObjectiveCard> buildObjectiveCardsDeck() throws FileNotFoundException{
        Stack<ObjectiveCard> deckContent = new Stack<>();
        File input = new File("src/main/resources/it/polimi/ingsw/gc07/objectiveCardsDeck.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();
        // get the JsonArray of all the cards
        JsonArray jsonArrayObjectiveCards = fileObject.get("cards").getAsJsonArray();
        for(JsonElement c: jsonArrayObjectiveCards){
            // for every card, extract attributes
            JsonObject cardJsonObject = c.getAsJsonObject();

            int id = cardJsonObject.get("id").getAsInt();
            CardType type = CardType.OBJECTIVE_CARD;
            int objectiveScore = cardJsonObject.get("objectivescore").getAsInt();

            // extract and create the condition
            Condition scoringCondition = null;
            JsonObject conditionObject = cardJsonObject.get("scoringcondition").getAsJsonObject();
            String conditionType = conditionObject.get("conditiontype").getAsString();
            if(conditionType.equals("LAYOUT_CONDITION")){
                // extract layout condition
                GameResource[][] cardsColor = DecksBuilder.extractLayoutCondition(conditionObject);
                scoringCondition = new LayoutCondition(cardsColor);
            }
            else {
                // extract items condition
                List<GameItem> neededItems = DecksBuilder.extractItemsCondition(conditionObject);
                scoringCondition = new ItemsCondition(neededItems);
            }

            // create Objective card and add it to deck content
            ObjectiveCard card = new ObjectiveCard(id, type, scoringCondition, objectiveScore);
            deckContent.add(card);
        }
        return new PlayingDeck<>(CardType.OBJECTIVE_CARD, deckContent);
    }

    /**
     * Method that build a resource cards deck from the json file resourceCardsDeck.json
     * @return resource cards deck
     * @throws FileNotFoundException
     */
    public static ResourceCardsDeck buildResourceCardsDeck() throws FileNotFoundException {
        Stack<DrawableCard> deckContent = new Stack<>();
        File input = new File("src/main/resources/it/polimi/ingsw/gc07/resourceCardsDeck.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();
        // get the JsonArray of all the cards
        JsonArray jsonArrayResourceCards = fileObject.get("cards").getAsJsonArray();
        for (JsonElement c : jsonArrayResourceCards) {
            // for every card, extract attributes
            JsonObject cardJsonObject = c.getAsJsonObject();

            int id = cardJsonObject.get("id").getAsInt();
            CardType type = CardType.RESOURCE_CARD;
            int placementScore = cardJsonObject.get("placementscore").getAsInt();
            boolean[] frontCorners = DecksBuilder.extractFrontCorners(cardJsonObject);
            GameItem[] frontCornersContent = DecksBuilder.extractFrontCornersContent(cardJsonObject);
            boolean[] backCorners = DecksBuilder.extractBackCorners(cardJsonObject);
            GameItem[] backCornersContent = DecksBuilder.extractBackCornersContent(cardJsonObject);
            List<GameResource> permanentResources = DecksBuilder.extractPermanentResources(cardJsonObject);

            // create resource card and add it to deck content
            DrawableCard card = new DrawableCard(id, type, frontCorners, frontCornersContent, backCorners, backCornersContent, placementScore, permanentResources);
            deckContent.add(card);
        }
        return new ResourceCardsDeck(CardType.RESOURCE_CARD, deckContent);
    }

    /**
     * Method that build a gold cards deck from the json file goldCardsDeck.json
     * @return resource cards deck
     * @throws FileNotFoundException
     */
    public static GoldCardsDeck buildGoldCardsDeck() throws FileNotFoundException {
        Stack<GoldCard> deckContent = new Stack<>();
        File input = new File("src/main/resources/it/polimi/ingsw/gc07/goldCardsDeck.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();
        // get the JsonArray of all the cards
        JsonArray jsonArrayStarterCards = fileObject.get("cards").getAsJsonArray();

    }
}
