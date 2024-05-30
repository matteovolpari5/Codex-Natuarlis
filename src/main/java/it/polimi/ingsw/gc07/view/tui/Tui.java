package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CardType;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.main.ClientMain;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view.GameFieldView;
import it.polimi.ingsw.gc07.network.Client;
import it.polimi.ingsw.gc07.utils.SafePrinter;
import it.polimi.ingsw.gc07.view.Ui;

import java.util.*;

public class Tui implements Ui, ChatTui, DeckTui, GameFieldTui, PlayerTui, BoardTui {
    private String nickname;
    private Client client;
    private final static int minPlayersNumber = 2;
    private final static int maxPlayersNumber = 4;

    /**
     * Constructor of Tui.
     * @param nickname nickname
     * @param client client
     */
    public Tui(String nickname, Client client) {
        this.nickname = nickname;
        this.client = client;
    }

    /**
     * Method used to set nickname.
     * @param nickname nickname
     */
    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Method used to set client.
     * @param client client
     */
    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Method used to run the starting interface.
     */
    @Override
    public void runJoinGameInterface() {
        if(!client.isClientAlive()) {
            askForReconnection(); //TODO se inserisce 1 esegue il client main e crea un nuovo client. Questo che fine fa?
        }

        Timer timeout = new Timer();
        new Thread(()->{
            timeout.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, 60*1000); //timer of 1 minute
        }).start();

        Scanner scan = new Scanner(System.in);

        SafePrinter.println("Insert a character to perform an action:");
        SafePrinter.println("- q to join an existing game"); // JoinExistingGameCommand
        SafePrinter.println("- w to create an new game"); // JoinNewGameCommand
        SafePrinter.println("- e to see existing games"); // DisplayGamesCommand
        SafePrinter.print("> ");
        String command = scan.nextLine();

        String tokenColorString;
        TokenColor tokenColor;
        boolean correctInput;
        switch(command){
            case "q":
                // join existing game
                do {
                    SafePrinter.println("Insert token color (green, red, yellow or blue): ");
                    SafePrinter.print("> ");
                    tokenColorString = scan.nextLine();
                    tokenColor = parseTokenColor(tokenColorString);
                    if(tokenColor == null) {
                        correctInput = false;
                        SafePrinter.println("No such token color");
                    }else {
                        correctInput = true;
                    }
                }while(!correctInput);

                int gameId;
                do {
                    SafePrinter.println("Insert game id: ");
                    SafePrinter.print("> ");
                    gameId = -1;
                    correctInput = true;
                    try {
                        gameId = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        correctInput = false;
                        SafePrinter.println("No such game id, insert a number.");
                    }
                    if(correctInput && gameId < 0) {
                        SafePrinter.println("No such game id.");
                        correctInput = false;
                    }
                }while(!correctInput);

                // cancel timer
                synchronized (this){
                    timeout.cancel();
                    timeout.purge();
                }
                client.setAndExecuteCommand(new JoinExistingGameCommand(nickname, tokenColor, gameId));
                break;

            case "w":
                // join new game
                do {
                    SafePrinter.println("Insert token color (green, red, yellow or blue): ");
                    SafePrinter.print("> ");
                    tokenColorString = scan.nextLine();
                    tokenColor = parseTokenColor(tokenColorString);
                    correctInput = true;
                    if(tokenColor == null) {
                        correctInput = false;
                        SafePrinter.println("No such token color");
                    }
                }while(!correctInput);

                int playersNumber = -1;
                do {
                    correctInput = true;
                    SafePrinter.println("Insert the number of players for the game: ");
                    SafePrinter.print("> ");
                    try {
                        playersNumber = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        correctInput = false;
                        scan.nextLine();
                    }
                    if(playersNumber < minPlayersNumber || playersNumber > maxPlayersNumber) {
                        correctInput = false;
                    }
                }while(!correctInput);

                // cancel timer
                synchronized (this){
                    timeout.cancel();
                    timeout.purge();
                }
                client.setAndExecuteCommand(new JoinNewGameCommand(nickname, tokenColor, playersNumber));
                break;

            case "e":
                // display existing games

                // cancel timer
                synchronized (this){
                    timeout.cancel();
                    timeout.purge();
                }

                client.setAndExecuteCommand(new DisplayGamesCommand(nickname));
                break;

            default:
                SafePrinter.println("The provided character doesn't refer to any action");
                runJoinGameInterface();
        }
    }

    /**
     * Method used to run the game interface.
     */
    @Override
    public void runGameInterface() {
        Scanner scan = new Scanner(System.in);
        while(client.isClientAlive()) {
            SafePrinter.println("Insert a character to perform an action:");
            SafePrinter.println("- q to write a private message");
            SafePrinter.println("- w to write a public message");
            SafePrinter.println("- e to disconnect from the game");
            SafePrinter.println("- r to draw a card from a deck");
            SafePrinter.println("- t to draw a face up card");
            SafePrinter.println("- y to place a card");
            SafePrinter.println("- u to set initial cards");
            SafePrinter.println("- i to see another player's game field");
            SafePrinter.println("- o to see the whole chat");
            SafePrinter.print("> ");
            String command = scan.nextLine();

            String content;
            String cardTypeString;
            CardType cardType;
            int wayInput;
            boolean way;
            switch(command) {
                case "q":
                    SafePrinter.println("Insert the receiver nickname: ");
                    SafePrinter.print("> ");
                    String receiver = scan.nextLine();
                    // check not empty
                    if(receiver == null || receiver.isEmpty()) {
                        SafePrinter.println("Insert a non empty receiver");
                        break;
                    }
                    // check sender not equals receiver
                    if(receiver.equals(nickname)) {
                        SafePrinter.println("You can't send a private message to yourself.");
                        break;
                    }
                    // check existing receiver
                    if(!client.getGameView().checkPlayerPresent(receiver)) {
                        SafePrinter.println("Provided receiver doesn't exist.");
                        break;
                    }
                    SafePrinter.println("Insert the message content:");
                    SafePrinter.print("> ");
                    content = scan.nextLine();
                    // check not empty content
                    if(content == null || content.isEmpty()) {
                        SafePrinter.println("Content can't be empty");
                        break;
                    }

                    client.setAndExecuteCommand(new AddChatPrivateMessageCommand(content, nickname, receiver));
                    break;

                case "w":
                    SafePrinter.println("Insert the message content:");
                    SafePrinter.print("> ");
                    content = scan.nextLine();
                    // check not empty content
                    if(content == null || content.isEmpty()) {
                        SafePrinter.println("Content can't be empty");
                        break;
                    }

                    client.setAndExecuteCommand(new AddChatPublicMessageCommand(content, nickname));
                    break;

                case "e":
                    client.setAndExecuteCommand(new DisconnectPlayerCommand(nickname));
                    client.setClientAlive(false);
                    break;

                case "r":
                    SafePrinter.println("Select a card type ('g' for gold or 'r' for resource): ");
                    SafePrinter.print("> ");
                    cardTypeString = scan.nextLine();
                    if(cardTypeString.equals("r")) {
                        cardType = CardType.RESOURCE_CARD;
                    }else if(cardTypeString.equals("g")) {
                        cardType = CardType.GOLD_CARD;
                    }else {
                        SafePrinter.println("No such card type");
                        break;
                    }
                    // check game state
                    if(!client.getGameView().getGameState().equals(GameState.PLAYING)) {
                        SafePrinter.println("Wrong game state.");
                        break;
                    }
                    // check current player
                    if(!client.getGameView().isCurrentPlayer(nickname)) {
                        SafePrinter.println("This is not your turn, try later.");
                        break;
                    }
                    client.setAndExecuteCommand(new DrawDeckCardCommand(nickname, cardType));
                    break;

                case "t":
                    SafePrinter.println("Select a card type ('g' for gold or 'r' for resource): ");
                    SafePrinter.print("> ");
                    cardTypeString = scan.nextLine();
                    if(cardTypeString.equals("r")) {
                        cardType = CardType.RESOURCE_CARD;
                    }else if(cardTypeString.equals("g")) {
                        cardType = CardType.GOLD_CARD;
                    }else {
                        SafePrinter.println("No such card type");
                        break;
                    }
                    SafePrinter.println("Select the position of the card to draw: ");
                    SafePrinter.print("> ");
                    int pos;
                    try {
                        pos = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        SafePrinter.println("Insert a number.");
                        break;
                    }
                    // check valid position
                    if(pos < 0 || pos > client.getGameView().getNumFaceUpCards(cardType)) {
                        SafePrinter.println("Wrong cards position.");
                        break;
                    }
                    // check game state
                    if(!client.getGameView().getGameState().equals(GameState.PLAYING)) {
                        SafePrinter.println("Wrong game state.");
                        break;
                    }
                    // check current player
                    if(!client.getGameView().isCurrentPlayer(nickname)) {
                        SafePrinter.println("This is not your turn, try later.");
                        break;
                    }
                    client.setAndExecuteCommand(new DrawFaceUpCardCommand(nickname, cardType, pos));
                    break;

                case "y":
                    // position in hand
                    SafePrinter.println("Select the position of the card you want to place: ");
                    SafePrinter.print("> ");
                    int cardPos;
                    try {
                        cardPos = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        SafePrinter.println("Insert a number.");
                        break;
                    }
                    if(cardPos < 0 || cardPos >= client.getGameView().getCurrHandSize()) {
                        SafePrinter.println("Wrong card hand position.");
                        break;
                    }
                    SafePrinter.println("Insert a position of the game field where you want to place the card.");
                    // position in game field - x
                    SafePrinter.println("Insert x: ");
                    SafePrinter.print("> ");
                    int x;
                    try {
                        x = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        SafePrinter.println("Insert a number.");
                        break;
                    }
                    if(x < 0 || x >= client.getGameView().getGameFieldDim()) {
                        SafePrinter.println("GameField position out of bound.");
                        break;
                    }
                    // position in game field - y
                    SafePrinter.println("Insert y: ");
                    SafePrinter.print("> ");
                    int y;
                    try {
                        y = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        SafePrinter.println("Insert a number.");
                        break;
                    }
                    if(y < 0 || y >= client.getGameView().getGameFieldDim()) {
                        SafePrinter.println("GameField position out of bound.");
                        break;
                    }
                    // way in game field
                    SafePrinter.println("Select 0 to place the card face up, 1 to place the card face down: ");
                    SafePrinter.print("> ");
                    try {
                        wayInput = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        SafePrinter.println("Insert a number.");
                        break;
                    }
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        SafePrinter.println("The provided value for way is not correct");
                        break;
                    }
                    // check game state
                    if(!client.getGameView().getGameState().equals(GameState.PLAYING)) {
                        SafePrinter.println("Wrong game state.");
                        break;
                    }
                    // check current player
                    if(!client.getGameView().isCurrentPlayer(nickname)) {
                        SafePrinter.println("This is not your turn, try later.");
                        break;
                    }

                    // create and execute command
                    client.setAndExecuteCommand(new PlaceCardCommand(nickname, cardPos, x, y, way));
                    break;

                case "u":
                    SafePrinter.println("Select 0 to place the starter card face up, 1 to place the starter card face down: ");
                    SafePrinter.print("> ");
                    try {
                        wayInput = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        SafePrinter.println("Insert a number.");
                        break;
                    }
                    boolean startCardWay;
                    if(wayInput == 1) {
                        startCardWay = true;
                    }else if(wayInput == 0) {
                        startCardWay = false;
                    }else {
                        SafePrinter.println("The provided value is not correct");
                        break;
                    }

                    SafePrinter.println("Select 0 to choose the first secret objective, 1 to choose the second one: ");
                    SafePrinter.print("> ");
                    boolean objectiveCard;
                    int objectiveCardInt;
                    try {
                        objectiveCardInt = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        SafePrinter.println("Insert a number.");
                        break;
                    }
                    if(objectiveCardInt == 0) {
                        objectiveCard = false;
                    }else if(objectiveCardInt == 1) {
                        objectiveCard = true;
                    }else {
                        SafePrinter.println("The provided value is not correct");
                        break;
                    }

                    if(!client.getGameView().getGameState().equals(GameState.SETTING_INITIAL_CARDS)) {
                        SafePrinter.println("Wrong game state.");
                        break;
                    }

                    client.setAndExecuteCommand(new SetInitialCardsCommand(nickname, startCardWay, objectiveCard));
                    break;

                case "i":
                    SafePrinter.println("Insert other player's nickname");
                    SafePrinter.print("> ");
                    String nickname = scan.nextLine();
                    // check existing player
                    if(nickname == null || nickname.isEmpty() || !client.getGameView().checkPlayerPresent(nickname)) {
                        SafePrinter.println("Provided nickname doesn't exist in the game.");
                        break;
                    }
                    SafePrinter.println("");
                    SafePrinter.println("--------------------------------------------------------");
                    SafePrinter.println("                     " + nickname + "'s GAME FIELD");
                    SafePrinter.println("--------------------------------------------------------");
                    printGameField(nickname);
                    SafePrinter.println("\n\n");
                    break;

                case "o":
                    SafePrinter.println("");
                    SafePrinter.println("--------------------------------------------------------");
                    SafePrinter.println("                            CHAT                        ");
                    SafePrinter.println("--------------------------------------------------------");
                    ChatTui.printChat(client.getGameView().getOwnerMessages());
                    SafePrinter.println("");
                    break;

                default:
                    SafePrinter.println("The provided character doesn't refer to any action");
            }
        }

        askForReconnection();
    }

    /**
     * Method used to ask for reconnection to a player.
     */
    public void askForReconnection() {
        Scanner scan = new Scanner(System.in);
        SafePrinter.println("\n\nDo you want to reconnect (1 = yes, other = no)?");
        SafePrinter.print("> ");
        int reconnect = 0;
        try {
            reconnect = scan.nextInt();
            scan.nextLine();
        }catch(InputMismatchException e) {
            System.exit(0);
        }
        if(reconnect == 1) {
            SafePrinter.println("\n\n");
            String[] args = new String[2];
            args[0] = "1234";
            args[1] = "65000";
            ClientMain.main(args);
        }else {
            System.exit(0);
        }
    }

    /**
     * Method used to parse a string token color.
     * @param tokenColorString token color string
     * @return token color value
     */
    private TokenColor parseTokenColor(String tokenColorString) {
        if(tokenColorString == null || tokenColorString.isEmpty())
            return null;
        return switch (tokenColorString) {
            case "green" -> TokenColor.GREEN;
            case "red" -> TokenColor.RED;
            case "yellow" -> TokenColor.YELLOW;
            case "blue" -> TokenColor.BLUE;
            default -> null;
        };
    }

    /**
     * Method used to print a game field.
     * @param nickname nickname of the player
     */
    private void printGameField(String nickname) {
        GameFieldView gameField = client.getGameView().getGameField(nickname);
        GameFieldTui.printGameField(gameField.getCardsContent(), gameField.getCardsFace(), gameField.getCardsOrder());
    }

    /**
     * Method used to notify the player he has received a new chat chatMessage.
     * @param chatMessage chat message
     */
    @Override
    public void receiveMessageUpdate(ChatMessage chatMessage) {
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                      GAME CHAT                         ");
        SafePrinter.println("--------------------------------------------------------");
        ChatTui.printMessage(chatMessage);
    }

    /**
     * Method used to receive a game field update, return all game field structures.
     * @param nickname nickname
     * @param cardsContent cards content
     * @param cardsFace cards face
     * @param cardsOrder cards order
     */
    @Override
    public void receiveGameFieldUpdate(String nickname, PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {
        if(!nickname.equals(client.getGameView().getOwnerNickname())) {
            return;
        }
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                   PLAYER GAME FIELD                    ");
        SafePrinter.println("--------------------------------------------------------");
        GameFieldTui.printGameField(cardsContent, cardsFace, cardsOrder);
    }

    /**
     * Method used to show the player of his starter card.
     * @param nickname nickname
     * @param starterCard starter card
     */
    @Override
    public void receiveStarterCardUpdate(String nickname, PlaceableCard starterCard) {
        if(!nickname.equals(client.getGameView().getOwnerNickname())) {
            return;
        }
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                     STARTER CARD                       ");
        SafePrinter.println("--------------------------------------------------------");
        PlayerTui.printStarterCard(starterCard, true);
        PlayerTui.printStarterCard(starterCard, false);
    }

    /**
     * Method used to show the player his new card hand.
     * @param nickname nickname
     * @param hand card hand
     * @param personalObjectives personal objectives
     */
    @Override
    public void receiveCardHandUpdate(String nickname, List<DrawableCard> hand, List<ObjectiveCard> personalObjectives) {
        if(!nickname.equals(client.getGameView().getOwnerNickname())) {
            return;
        }
        if(client.getGameView().getGameState().equals(GameState.GAME_STARTING) && hand.size() < 3) {
            // will receive another update
            return;
        }
        if(personalObjectives == null || personalObjectives.size() != 1 || personalObjectives.getFirst() == null) {
            return;
        }

        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                  FRONT PLAYER HAND                     ");
        SafePrinter.println("--------------------------------------------------------");
        PlayerTui.printPlayerHand(hand, personalObjectives,false);
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                   BACK PLAYER HAND                     ");
        SafePrinter.println("--------------------------------------------------------");
        PlayerTui.printPlayerHand(hand, personalObjectives,true);
    }

    /**
     * Method used to show the client an updated score.
     * @param playerScores players' scores
     * @param playerTokenColors players' token colors
     */
    @Override
    public void receiveScoreUpdate(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors) {
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                      SCORE BOARD                       ");
        SafePrinter.println("--------------------------------------------------------");
        BoardTui.printScoreTrackBoard(playerScores, playerTokenColors);
    }

    /**
     * Method used to display the deck view, i.e. the cards the player can draw or see.
     * @param topResourceDeck top resource deck
     * @param topGoldDeck top gold deck
     * @param faceUpResourceCard face up resource card
     * @param faceUpGoldCard face up gold card
     * @param commonObjective common objective
     */
    @Override
    public void receiveDecksUpdate(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {
        // don't want to print deck update every time a card changes
    }

    /**
     * Method used to receive general game info.
     * @param gameState game state
     * @param currPlayer current player
     */
    @Override
    public void receiveGeneralModelUpdate(GameState gameState, String currPlayer) {
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                     GAME MODEL UPDATE                  ");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("Game state: " + gameState);
        if(currPlayer == null) {
            currPlayer = "none";
        }
        SafePrinter.println("Current player: " + currPlayer);

        // if new current player, print deck update
        GameState state = client.getGameView().getGameState();
        String currPlayerNickname = client.getGameView().getCurrentPlayerNickname();
        String ownerNickname = client.getGameView().getOwnerNickname();
        DrawableCard topResourceDeck = client.getGameView().getTopResourceDeck();
        GoldCard topGoldDeck = client.getGameView().getTopGoldDeck();
        List<DrawableCard> faceUpResourceCard = client.getGameView().getFaceUpResourceCard();
        List<GoldCard> faceUpGoldCard = client.getGameView().getFaceUpGoldCard();
        List<ObjectiveCard> commonObjective = client.getGameView().getCommonObjective();
        if(state.equals(GameState.PLAYING) && currPlayerNickname != null && currPlayerNickname.equals(ownerNickname)) {
            SafePrinter.println("");
            SafePrinter.println("--------------------------------------------------------");
            SafePrinter.println("                      FRONT DECK                        ");
            SafePrinter.println("--------------------------------------------------------");
            DeckTui.printDeck(commonObjective, faceUpGoldCard, faceUpResourceCard, topGoldDeck, topResourceDeck);
            SafePrinter.println("--------------------------------------------------------");
            SafePrinter.println("                       BACK DECK                        ");
            SafePrinter.println("--------------------------------------------------------");
            DeckTui.printBackDeck(commonObjective, faceUpGoldCard, faceUpResourceCard, topGoldDeck, topResourceDeck);
        }
    }

    /**
     * Method used to inform the player that the current round is the penultimate round.
     */
    @Override
    public void receivePenultimateRoundUpdate() {
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                     PENULTIMATE ROUND                  ");
        SafePrinter.println("--------------------------------------------------------");
    }

    /**
     * Method used to inform the player that the current round is the additional round.
     */
    @Override
    public void receiveAdditionalRoundUpdate() {
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                     ADDITIONAL ROUND                   ");
        SafePrinter.println("--------------------------------------------------------");
    }

    /**
     * Method used to receive the last command result.
     * @param commandResult command result
     */
    @Override
    public void receiveCommandResultUpdate(CommandResult commandResult) {
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                      COMMAND RESULT                    ");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println(commandResult.getResultMessage());
    }

    /**
     * Method used to display existing and free games.
     * @param existingGamesPlayerNumber players number for each game
     * @param existingGamesTokenColor taken token colors for each game
     */
    @Override
    public void receiveExistingGamesUpdate(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {
        for(Integer id: existingGamesPlayerNumber.keySet()) {
            SafePrinter.print("Id: " + id + " - " + "Number of players: " + existingGamesPlayerNumber.get(id));
        }
        for(Integer id: existingGamesTokenColor.keySet()) {
            SafePrinter.print(" - " + "Taken token colors: ");
            for(TokenColor t: existingGamesTokenColor.get(id)) {
                SafePrinter.print(t + " ");
            }
            SafePrinter.println("");
        }
        runJoinGameInterface();
    }

    /**
     * Method used to display winners.
     * @param winners winners' nicknames
     */
    @Override
    public void receiveWinnersUpdate(List<String> winners) {
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                         WINNERS                        ");
        SafePrinter.println("--------------------------------------------------------");
        for(String winner: winners) {
            SafePrinter.println(winner);
        }
        SafePrinter.println("");
        client.setClientAlive(false);
    }

    /**
     * Method used to notify the player he has received a full chat update.
     * @param chatMessages full chat update
     */
    @Override
    public void receiveFullChatUpdate(List<ChatMessage> chatMessages) {
        // do not print whole chat
    }

    /**
     * Method used to show the game id.
     * @param gameId game id
     */
    @Override
    public void receiveGameIdUpdate(int gameId) {
        // don't display
    }

    /**
     * Method used to show a connection update.
     * @param nickname   nickname
     * @param connection true if the player is connected, false otherwise
     */
    @Override
    public void receiveConnectionUpdate(String nickname, boolean connection) {
        if(!connection) {
            SafePrinter.println("");
            SafePrinter.println("--------------------------------------------------------");
            SafePrinter.println("                         CONNECTION UPDATE                        ");
            SafePrinter.println("--------------------------------------------------------");
            SafePrinter.println("Player " + nickname + " lost connection.");
        }
    }

    /**
     * Method used to show a stall update.
     * @param nickname nickname
     * @param stall    true if the player is stalled, false otherwise
     */
    @Override
    public void receiveStallUpdate(String nickname, boolean stall) {
        if(stall) {
            SafePrinter.println("");
            SafePrinter.println("--------------------------------------------------------");
            SafePrinter.println("                         STALL UPDATE                        ");
            SafePrinter.println("--------------------------------------------------------");
            SafePrinter.println("Player " + nickname + "is stalled.");
        }
    }

    /**
     * Method used to display game players.
     * @param nicknames        map containing players and their token colors
     * @param connectionValues map containing players and their connection values
     * @param stallValues      map containing players and their stall values
     */
    @Override
    public void receivePlayersUpdate(Map<String, TokenColor> nicknames, Map<String, Boolean> connectionValues, Map<String, Boolean> stallValues) {
        // don't display
    }

    /**
     * Method used to show secrete objectives.
     * @param nickname nickname
     * @param secretObjectives secreteObjectives
     */
    @Override
    public void receiveSecretObjectives(String nickname, List<ObjectiveCard> secretObjectives) {
        SafePrinter.println("");
        SafePrinter.println("--------------------------------------------------------");
        SafePrinter.println("                         SECRET OBJECTIVES                        ");
        SafePrinter.println("--------------------------------------------------------");
        if(!nickname.equals(client.getGameView().getOwnerNickname())) {
            return;
        }
        for(ObjectiveCard objectiveCard: secretObjectives) {
            PlayerTui.printOnlyObjectiveCard(objectiveCard);
        }
    }
}

