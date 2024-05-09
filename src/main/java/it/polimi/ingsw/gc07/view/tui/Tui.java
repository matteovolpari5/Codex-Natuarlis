package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.enumerations.CardType;
import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.Client;
import it.polimi.ingsw.gc07.view.Ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Tui implements Ui, ChatTui, DeckTui, GameFieldTui, PlayerTui, ScoreTrackBoardTui {
    private final String nickname;
    private final Client client;

    public Tui(String nickname, Client client) {
        this.nickname = nickname;
        this.client = client;
    }

    @Override
    public void runCliJoinGame() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert a character to perform an action:");
        System.out.println("- q to join an existing game"); // JoinExistingGameCommand
        System.out.println("- w to join an new game"); // JoinNewGameCommand
        System.out.println("- e to see existing games"); // DisplayGamesCommand
        System.out.print("> ");
        String command = scan.nextLine();

        String tokenColorString;
        TokenColor tokenColor;
        switch(command){
            case "q":
                // join existing game
                System.out.println("Insert token color (green, red, yellow or blue): ");
                System.out.print("> ");
                tokenColorString = scan.nextLine();
                tokenColor = parseTokenColor(tokenColorString);
                if(tokenColor == null) {
                    System.out.println("No such token color");
                }
                System.out.println("Insert game id: ");
                int gameId = -1;
                try {
                    gameId = scan.nextInt();
                    scan.nextLine();
                }catch(InputMismatchException e) {
                    scan.nextLine();
                    System.out.println("No such game id, insert a number");
                }
                if(gameId < 0) {
                    System.out.println("No such game id");
                }
                client.setAndExecuteCommand(new JoinExistingGameCommand(nickname, tokenColor, gameId));
                break;

            case "w":
                // join new game
                System.out.println("Insert token color (green, red, yellow or blue): ");
                System.out.print("> ");
                tokenColorString = scan.nextLine();
                tokenColor = parseTokenColor(tokenColorString);
                if(tokenColor == null) {
                    System.out.println("No such token color");
                }
                System.out.println("Insert the number of players for the game: ");
                int playersNumber = scan.nextInt();
                scan.nextLine();
                // TODO potremmo fare già qua il controllo su players number per efficienza
                client.setAndExecuteCommand(new JoinNewGameCommand(nickname, tokenColor, playersNumber));
                break;

            case "e":
                // display existing games
                GamesManagerCommand mycommand = new DisplayGamesCommand(nickname);
                client.setAndExecuteCommand(mycommand);
                break;

            default:
                System.out.println("The provided character doesn't refer to any action");
        }
    }

    @Override
    public void runCliGame() {
        Scanner scan = new Scanner(System.in);
        while(client.isClientAlive()) {
            System.out.println("Insert a character to perform an action:");
            System.out.println("- q to write a private message"); // AddChatPrivateMessage
            System.out.println("- w to write a public message"); // AddChatPublicMessage
            System.out.println("- e to disconnect from the game"); // DisconnectPlayerControllerCommand
            System.out.println("- r to draw a card from a deck"); // DrawDeckCardControllerCommand
            System.out.println("- t to draw a face up card"); // DrawFaceUpCardControllerCommand
            System.out.println("- y to place a card"); // PlaceCardControllerCommand
            System.out.println("- u to place the starter card"); // PlaceStarterCardControllerCommand
            System.out.print("> ");
            String command = scan.nextLine();

            String content;
            String cardTypeString;
            CardType cardType;
            int wayInput;
            boolean way;
            switch(command) {
                case "q":
                    System.out.println("Insert the receiver nickname: ");
                    System.out.print("> ");
                    String receiver = scan.nextLine();
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    client.setAndExecuteCommand(new AddChatPrivateMessageControllerCommand(content, nickname, receiver));
                    break;
                case "w":
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    client.setAndExecuteCommand(new AddChatPublicMessageControllerCommand(content, nickname));
                    break;
                case "e":
                    client.setAndExecuteCommand(new DisconnectPlayerControllerCommand(nickname));
                    System.out.println("\nYou successfully disconnected !");
                    client.setClientAlive(false);
                    break;
                case "r":
                    System.out.println("Select a card type ('g' for gold or 'r' for resource): ");
                    System.out.print("> ");
                    cardTypeString = scan.nextLine();
                    if(cardTypeString.equals("r")) {
                        cardType = CardType.RESOURCE_CARD;
                    }else if(cardTypeString.equals("g")) {
                        cardType = CardType.GOLD_CARD;
                    }else {
                        System.out.println("No such card type");
                        continue;
                    }
                    client.setAndExecuteCommand(new DrawDeckCardControllerCommand(nickname, cardType));
                    break;
                case "t":
                    System.out.println("Select a card type ('g' for gold or 'r' for resource): ");
                    System.out.print("> ");
                    cardTypeString = scan.nextLine();
                    if(cardTypeString.equals("r")) {
                        cardType = CardType.RESOURCE_CARD;
                    }else if(cardTypeString.equals("g")) {
                        cardType = CardType.GOLD_CARD;
                    }else {
                        System.out.println("No such card type");
                        continue;
                    }
                    System.out.println("Select the position of the card to draw: ");
                    System.out.print("> ");
                    int pos = scan.nextInt();
                    scan.nextLine();
                    //TODO possiamo introdurre un controllo per evitare una chiamata
                    // inutile se la posizione eccede il range possibile
                    client.setAndExecuteCommand(new DrawFaceUpCardControllerCommand(nickname, cardType, pos));
                    break;
                case "y":
                    // String nickname, int pos, int x, int y, boolean way) {
                    // pos
                    System.out.println("Select the position of the card you want to place: ");
                    System.out.print("> ");
                    int cardPos = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Insert a position of the game field where you want to place the card.");
                    // x
                    System.out.println("Insert x: ");
                    System.out.print("> ");
                    int x = scan.nextInt();
                    scan.nextLine();
                    // y
                    System.out.println("Insert y: ");
                    System.out.print("> ");
                    int y = scan.nextInt();
                    scan.nextLine();
                    // way
                    System.out.println("Select 0 to place the card face up, 1 to place the card face down: ");
                    System.out.print("> ");
                    wayInput = scan.nextInt();
                    scan.nextLine();
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        System.out.println("The provided value for way is not correct");
                        continue;
                    }
                    // create and execute command
                    client.setAndExecuteCommand(new PlaceCardControllerCommand(nickname, cardPos, x, y, way));
                    break;
                case "u":
                    System.out.println("Select 0 to place the starter card face up, 1 to place the starter card face down: ");
                    System.out.print("> ");
                    wayInput = scan.nextInt();
                    scan.nextLine();
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        System.out.println("The provided value is not correct");
                        continue;
                    }
                    client.setAndExecuteCommand(new PlaceStarterCardControllerCommand(nickname, way));
                    break;
                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
        }
        System.exit(0);
    }

    private TokenColor parseTokenColor(String tokenColorString) {
        return switch (tokenColorString) {
            case "green" -> TokenColor.GREEN;
            case "red" -> TokenColor.RED;
            case "yellow" -> TokenColor.YELLOW;
            case "blue" -> TokenColor.BLUE;
            default -> null;
        };
    }

    @Override
    public void receiveMessageUpdate(ChatMessage chatMessage) {
        ChatTui.printMessage(chatMessage);
    }

    @Override
    public void receiveGameFieldUpdate(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {
        GameFieldTui.printGameField(cardsContent, cardsFace, cardsOrder);
    }

    @Override
    public void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective) {
        PlayerTui.printPlayerHand(hand, personalObjective);
    }

    @Override
    public void receiveScoreUpdate(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors) {
        ScoreTrackBoardTui.printScoreTrackBoard(playerScores, playerTokenColors);
    }

    @Override
    public void receiveDecksUpdate(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {
        DeckTui.printDeck(commonObjective, faceUpGoldCard, faceUpResourceCard, topGoldDeck, topResourceDeck);
    }
}