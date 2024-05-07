package it.polimi.ingsw.gc07;

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
import it.polimi.ingsw.gc07.model.conditions.CornerCoverageCondition;
import it.polimi.ingsw.gc07.model.conditions.ItemsCondition;
import it.polimi.ingsw.gc07.model.conditions.LayoutCondition;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.enumerations.CardType;
import it.polimi.ingsw.gc07.enumerations.GameObject;
import it.polimi.ingsw.gc07.enumerations.GameResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class used to parse decks' JSON files.
 */
public abstract class DecksBuilder {

    /**
     * Method to extract a boolean value from a string.
     * @param booleanString string representing the boolean value
     * @return boolean value
     */
    private static boolean extractBoolean(String booleanString) {
        return booleanString.equals("true");
    }

    /**
     * Method to extract a resource from a string.
     * @param resource string representing a resource
     * @return resource
     */
    private static GameResource extractResource(String resource) {
        return switch (resource) {
            case "plant" -> GameResource.PLANT;
            case "animal" -> GameResource.ANIMAL;
            case "fungi" -> GameResource.FUNGI;
            case "insect" -> GameResource.INSECT;
            case "null" -> null;
            default -> throw new RuntimeException();
        };
    }

    /**
     * Method to extract an item from a string.
     * @param item string representing an item
     * @return item
     */
    private static GameItem extractItem(String item) {
        return switch (item) {
            case "plant" -> GameResource.PLANT;
            case "animal" -> GameResource.ANIMAL;
            case "fungi" -> GameResource.FUNGI;
            case "insect" -> GameResource.INSECT;
            case "quill" -> GameObject.QUILL;
            case "inkwell" -> GameObject.INKWELL;
            case "manuscript" -> GameObject.MANUSCRIPT;
            case "null" -> null;
            default -> throw new RuntimeException();
        };
    }

    /**
     * Method to extract front corners.
     * @param cardJsonObject card JSON object
     * @return front corners boolean array
     */
    private static boolean[] extractFrontCorners(JsonObject cardJsonObject) {
        boolean[] frontCorners = new boolean[4];

        String frontcorner0 = cardJsonObject.get("frontcorner0").getAsString();
        String frontcorner1 = cardJsonObject.get("frontcorner1").getAsString();
        String frontcorner2 = cardJsonObject.get("frontcorner2").getAsString();
        String frontcorner3 = cardJsonObject.get("frontcorner3").getAsString();

        frontCorners[0] = extractBoolean(frontcorner0);
        frontCorners[1] = extractBoolean(frontcorner1);
        frontCorners[2] = extractBoolean(frontcorner2);
        frontCorners[3] = extractBoolean(frontcorner3);
        return frontCorners;
    }

    /**
     * Method to extract front corners content.
     * @param cardJsonObject card JSON object
     * @return front corners content array
     */
    private static GameItem[] extractFrontCornersContent(JsonObject cardJsonObject){
        GameItem[] frontCornersContent = new GameItem[4];
        // frontCornersContent
        String frontcontent0String = cardJsonObject.get("frontcontent0").getAsString();
        String frontcontent1String = cardJsonObject.get("frontcontent1").getAsString();
        String frontcontent2String = cardJsonObject.get("frontcontent2").getAsString();
        String frontcontent3String = cardJsonObject.get("frontcontent3").getAsString();

        frontCornersContent[0] = extractItem(frontcontent0String);
        frontCornersContent[1] = extractItem(frontcontent1String);
        frontCornersContent[2] = extractItem(frontcontent2String);
        frontCornersContent[3] = extractItem(frontcontent3String);

        return frontCornersContent;
    }

    /**
     * Method to extract back corners.
     * @param cardJsonObject card JSON object
     * @return back corners boolean array
     */
    private static boolean[] extractBackCorners(JsonObject cardJsonObject) {
        boolean[] backCorners = new boolean[4];

        String backcorner0 = cardJsonObject.get("backcorner0").getAsString();
        String backcorner1 = cardJsonObject.get("backcorner1").getAsString();
        String backcorner2 = cardJsonObject.get("backcorner2").getAsString();
        String backcorner3 = cardJsonObject.get("backcorner3").getAsString();

        backCorners[0] = extractBoolean(backcorner0);
        backCorners[1] = extractBoolean(backcorner1);
        backCorners[2] = extractBoolean(backcorner2);
        backCorners[3] = extractBoolean(backcorner3);

        return backCorners;
    }

    /**
     * Method to extract back corners content.
     * @param cardJsonObject card JSON object
     * @return back corners content array
     */
    private static GameItem[] extractBackCornersContent(JsonObject cardJsonObject) {
        GameItem[] backCornersContent = new GameItem[4];

        String backcontent0String = cardJsonObject.get("backcontent0").getAsString();
        String backcontent1String = cardJsonObject.get("backcontent1").getAsString();
        String backcontent2String = cardJsonObject.get("backcontent2").getAsString();
        String backcontent3String = cardJsonObject.get("backcontent3").getAsString();

        backCornersContent[0] = extractItem(backcontent0String);
        backCornersContent[1] = extractItem(backcontent1String);
        backCornersContent[2] = extractItem(backcontent2String);
        backCornersContent[3] = extractItem(backcontent3String);

        return backCornersContent;
    }

    /**
     * Method to extract permanent resources.
     * @param cardJsonObject card JSON object
     * @return list of permanent resources
     */
    private static List<GameResource> extractPermanentResources(JsonObject cardJsonObject) {
        List<GameResource> permanentResources = new ArrayList<>();

        JsonArray permanentResourcesArray = cardJsonObject.get("permanentresource").getAsJsonArray();
        for(JsonElement r: permanentResourcesArray){
            // for every resource
            JsonObject resourceJsonObject = r.getAsJsonObject();
            String resourceString = resourceJsonObject.get("resource").getAsString();
            GameResource gameResource = extractResource(resourceString);
            permanentResources.add(gameResource);
        }
        return permanentResources;
    }

    /**
     * Method to extract a layout condition.
     * @param conditionObject condition JSON object
     * @return matrix representing a layout
     */
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
            cardsColor[row][col] = extractResource(colorString);
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

    /**
     * Method to extract an item condition.
     * @param conditionObject condition JSON object
     * @return game items list
     */
    private static List<GameItem> extractItemsCondition(JsonObject conditionObject) {
        List<GameItem> neededItems = new ArrayList<>();
        JsonArray neededItemsArray = conditionObject.get("neededItems").getAsJsonArray();
        for(JsonElement itemElement: neededItemsArray){
            // get json object
            JsonObject itemObject = itemElement.getAsJsonObject();
            String itemString = itemObject.get("resource").getAsString();
            if(extractItem(itemString) != null) {
                neededItems.add(extractItem(itemString));
            }
        }
        return neededItems;
    }

    /**
     * Method that builds a starter card deck from the JSON file starterCardsDeck.json
     * @return starter card deck
     */
    public static Deck<PlaceableCard> buildStarterCardsDeck() {
        Stack<PlaceableCard> deckContent = new Stack<>();
        File input = new File("src/main/resources/it/polimi/ingsw/gc07/starterCardsDeck.json");
        JsonElement fileElement;
        JsonObject fileObject;
        try{
            fileElement = JsonParser.parseReader(new FileReader(input));
        }catch(FileNotFoundException e){
            throw new RuntimeException();
        }
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
     * Method that builds an objective card deck from the JSON file objectiveCardsDeck.json
     * @return objective card deck
     */
    public static PlayingDeck<ObjectiveCard> buildObjectiveCardsDeck() {
        Stack<ObjectiveCard> deckContent = new Stack<>();
        File input = new File("src/main/resources/it/polimi/ingsw/gc07/objectiveCardsDeck.json");
        JsonElement fileElement;
        try{
            fileElement = JsonParser.parseReader(new FileReader(input));
        }catch(FileNotFoundException e){
            throw new RuntimeException();
        }
        JsonObject fileObject = fileElement.getAsJsonObject();
        // get the JsonArray of all the cards
        JsonArray jsonArrayObjectiveCards = fileObject.get("cards").getAsJsonArray();
        for(JsonElement c: jsonArrayObjectiveCards){
            // for every card, extract attributes
            JsonObject cardJsonObject = c.getAsJsonObject();

            int id = cardJsonObject.get("id").getAsInt();
            int objectiveScore = cardJsonObject.get("objectivescore").getAsInt();

            // extract and create the condition
            Condition scoringCondition;
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
            ObjectiveCard card = new ObjectiveCard(id, scoringCondition, objectiveScore);
            deckContent.add(card);
        }
        return new PlayingDeck<>(CardType.OBJECTIVE_CARD, deckContent);
    }

    /**
     * Method that build a resource cards deck from the JSON file resourceCardsDeck.json
     * @return resource cards deck
     */
    public static DrawableDeck<DrawableCard> buildResourceCardsDeck() {
        Stack<DrawableCard> deckContent = new Stack<>();
        File input = new File("src/main/resources/it/polimi/ingsw/gc07/resourceCardsDeck.json");
        JsonElement fileElement;
        try {
            fileElement = JsonParser.parseReader(new FileReader(input));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        return new DrawableDeck<DrawableCard>(CardType.RESOURCE_CARD, deckContent);
    }

    /**
     * Method that build a gold cards deck from the JSON file goldCardsDeck.json
     * @return resource cards deck
     */
    public static DrawableDeck<GoldCard> buildGoldCardsDeck() {
        Stack<GoldCard> deckContent = new Stack<>();
        File input = new File("src/main/resources/it/polimi/ingsw/gc07/goldCardsDeck.json");
        JsonElement fileElement;
        try{
            fileElement = JsonParser.parseReader(new FileReader(input));
        }
        catch(FileNotFoundException e){
            throw new RuntimeException();
        }
        JsonObject fileObject = fileElement.getAsJsonObject();
        // get the JsonArray of all the cards
        JsonArray jsonArrayGoldCards = fileObject.get("cards").getAsJsonArray();
        for (JsonElement c : jsonArrayGoldCards) {
            // for every card, extract attributes
            JsonObject cardJsonObject = c.getAsJsonObject();

            int id = cardJsonObject.get("id").getAsInt();
            int placementScore = cardJsonObject.get("placementscore").getAsInt();
            boolean[] frontCorners = DecksBuilder.extractFrontCorners(cardJsonObject);
            GameItem[] frontCornersContent = DecksBuilder.extractFrontCornersContent(cardJsonObject);
            boolean[] backCorners = DecksBuilder.extractBackCorners(cardJsonObject);
            GameItem[] backCornersContent = DecksBuilder.extractBackCornersContent(cardJsonObject);
            List<GameResource> permanentResources = DecksBuilder.extractPermanentResources(cardJsonObject);

            // extract and create the scoring condition
            Condition scoringCondition = null;
            JsonObject scoringConditionObject = cardJsonObject.get("scoringcondition").getAsJsonObject();
            String conditionType = scoringConditionObject.get("conditiontype").getAsString();
            if(conditionType.equals("CORNER_COVERAGE_CONDITION")){
                scoringCondition = new CornerCoverageCondition();
            }
            else if(conditionType.equals("ITEMS_CONDITION")){
                // extract items condition
                List<GameItem> neededItems = DecksBuilder.extractItemsCondition(scoringConditionObject);
                scoringCondition = new ItemsCondition(neededItems);
            }
            // else, the gold card has no scoringCondition

            // extract and create the placement condition
            // always ITEM_CONDITION
            Condition placementCondition;
            JsonObject placementConditionObject = cardJsonObject.get("placementcondition").getAsJsonObject();
            // extract items condition
            List<GameItem> neededItems = DecksBuilder.extractItemsCondition(placementConditionObject);
            placementCondition = new ItemsCondition(neededItems);

            // create resource card and add it to deck content
            GoldCard card = new GoldCard(id, frontCorners, frontCornersContent, backCorners, backCornersContent,
                    placementScore, permanentResources, placementCondition, scoringCondition);
            deckContent.add(card);
        }
        return new DrawableDeck<GoldCard>(CardType.GOLD_CARD, deckContent);
    }
}
