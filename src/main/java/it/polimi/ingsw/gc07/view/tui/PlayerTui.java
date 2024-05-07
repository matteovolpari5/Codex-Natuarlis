package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.conditions.CornerCoverageCondition;
import it.polimi.ingsw.gc07.model.conditions.ItemsCondition;
import it.polimi.ingsw.gc07.model.conditions.LayoutCondition;
import it.polimi.ingsw.gc07.model.enumerations.GameObject;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import it.polimi.ingsw.gc07.model_view_listeners.PlayerViewListener;

import java.util.List;

public class PlayerTui implements PlayerViewListener {
    public final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    @Override
    public void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective) {
        printPlayerHand(hand, personalObjective);
    }

    private void printPlayerHand(List<DrawableCard> hand, ObjectiveCard personalObjective)
    {
        printFirstLastRow();
        printSecondRow( hand, personalObjective);
        printThirdRow(personalObjective);
        printFourthRow( hand, personalObjective);
        printFirstLastRow();
    }
    private void printFirstLastRow()
    {
        System.out.println(ANSI_YELLOW_BACKGROUND + "+---------+" +ANSI_BLACK_BACKGROUND + " +---------+ +---------+ +---------+");
    }
    private void printSecondRow(List<DrawableCard> hand, ObjectiveCard objectiveCard)
    {
        System.out.print(ANSI_YELLOW_BACKGROUND+"|     ");
        if(objectiveCard.getScoringCondition() instanceof LayoutCondition)
        {
            for (int i = 0; i < 3; i++) {
                if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i]==null)
                {
                    System.out.print(ANSI_BLACK_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.INSECT))
                {
                    System.out.print(ANSI_PURPLE_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.ANIMAL))
                {
                    System.out.print(ANSI_BLUE_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.PLANT))
                {
                    System.out.print(ANSI_GREEN_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.FUNGI))
                {
                    System.out.print(ANSI_RED_BACKGROUND+" ");
                }
            }
        }
        else
        {
            System.out.print(ANSI_BLACK_BACKGROUND+"   ");
        }
        System.out.print(ANSI_YELLOW_BACKGROUND+" |");
        System.out.print(ANSI_BLACK_BACKGROUND+" ");
        //print hand//
        DrawableCard card;
        for (int i=0;i<3;i++) {
            if(i<hand.size())
                card = hand.get(i);
            else
                card=null;
            if(card!=null) {
                if (card.getFrontCorners()[0]) {
                    if (card.getFrontCornersContent()[0] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
                }
                if(card.getPoints()>0)
                    System.out.print(ANSI_BLACK_BACKGROUND + card.getPoints());
                else
                    System.out.print(ANSI_BLACK_BACKGROUND + " ");
                if (card.getScoringCondition() == null) {
                    System.out.print(ANSI_BLACK_BACKGROUND + " ");
                } else if (card.getScoringCondition() instanceof CornerCoverageCondition) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "C");
                } else if (card.getScoringCondition() instanceof ItemsCondition) {
                    if (((ItemsCondition) card.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.QUILL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "Q");
                    } else if (((ItemsCondition) card.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "I");
                    } else if (((ItemsCondition) card.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "M");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + "  ");
                }
                System.out.print(ANSI_BLACK_BACKGROUND + "   ");
                if (card.getFrontCorners()[1]) {
                    if (card.getFrontCornersContent()[1] == null) {
                        System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + " |");
                }
            }
            else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|         |");
            }
            System.out.print(ANSI_BLACK_BACKGROUND+" ");
        }
        System.out.println();

    }
    private void printThirdRow(ObjectiveCard objectiveCard){
        System.out.print(ANSI_YELLOW_BACKGROUND+"|  "+ANSI_BLACK_BACKGROUND+objectiveCard.getPoints()+ANSI_YELLOW_BACKGROUND+"  ");
        if(objectiveCard.getScoringCondition() instanceof ItemsCondition)
        {
            for (int i = 0; i < 3; i++) {
                if(((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().size()>i)
                {
                    if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "M");
                    } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.QUILL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "Q");
                    } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "I");
                    } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + "P");
                    } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + "F");
                    } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + "A");
                    } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + "I");
                    } else {
                        System.out.print(ANSI_BLACK_BACKGROUND + " ");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + " ");
                }
            }
        }
        else if (objectiveCard.getScoringCondition() instanceof CornerCoverageCondition)
        {
            System.out.print(ANSI_YELLOW_BACKGROUND +" C ");
        }
        else if (objectiveCard.getScoringCondition() instanceof LayoutCondition)
        {
            for (int i = 0; i < 3; i++) {
                if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i]==null)
                {
                    System.out.print(ANSI_BLACK_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.INSECT))
                {
                    System.out.print(ANSI_PURPLE_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.ANIMAL))
                {
                    System.out.print(ANSI_BLUE_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.PLANT))
                {
                    System.out.print(ANSI_GREEN_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.FUNGI))
                {
                    System.out.print(ANSI_RED_BACKGROUND+" ");
                }
            }
        }
        System.out.print(ANSI_YELLOW_BACKGROUND+" |");
        System.out.print(ANSI_BLACK_BACKGROUND + " ");
        System.out.print(ANSI_BLACK_BACKGROUND + "|         | ");
        System.out.print(ANSI_BLACK_BACKGROUND + "|         | ");
        System.out.println(ANSI_BLACK_BACKGROUND + "|         | ");
    }

    private void printFourthRow(List<DrawableCard> hand, ObjectiveCard objectiveCard){
        System.out.print(ANSI_YELLOW_BACKGROUND+"|     ");
        if(objectiveCard.getScoringCondition() instanceof LayoutCondition)
        {
            for (int i = 0; i < 3; i++) {
                if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i]==null)
                {
                    System.out.print(ANSI_BLACK_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.INSECT))
                {
                    System.out.print(ANSI_PURPLE_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.ANIMAL))
                {
                    System.out.print(ANSI_BLUE_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.PLANT))
                {
                    System.out.print(ANSI_GREEN_BACKGROUND+" ");
                }
                else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.FUNGI))
                {
                    System.out.print(ANSI_RED_BACKGROUND+" ");
                }
            }
        }
        else
        {
            System.out.print(ANSI_BLACK_BACKGROUND+"   ");
        }
        System.out.print(ANSI_YELLOW_BACKGROUND+" |");
        System.out.print(ANSI_BLACK_BACKGROUND + " ");
        DrawableCard card;
        for (int i=0;i<3;i++) {
            if(i<hand.size())
                card = hand.get(i);
            else
                card=null;
            if(card!=null) {
                if (card.getFrontCorners()[3]) {
                    if (card.getFrontCornersContent()[3] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
                }
                System.out.print(ANSI_BLACK_BACKGROUND + "     ");
                if (card.getFrontCorners()[2]) {
                    if (card.getFrontCornersContent()[2] == null) {
                        System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + " |");
                }
            }
            else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|         |");
            }
            System.out.print(ANSI_BLACK_BACKGROUND +" ");
        }
        System.out.println();
    }
}
