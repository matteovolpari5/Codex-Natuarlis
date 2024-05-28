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
        System.out.println("Insert a character to perform an action:");
        System.out.println("- q to join an existing game"); // JoinExistingGameCommand
        System.out.println("- w to create an new game"); // JoinNewGameCommand
        System.out.println("- e to see existing games"); // DisplayGamesCommand
        System.out.print("> ");
        String command = scan.nextLine();

        String tokenColorString;
        TokenColor tokenColor;
        boolean correctInput;
        switch(command){
            case "q":
                // join existing game
                do {
                    System.out.println("Insert token color (green, red, yellow or blue): ");
                    System.out.print("> ");
                    tokenColorString = scan.nextLine();
                    tokenColor = parseTokenColor(tokenColorString);
                    if(tokenColor == null) {
                        correctInput = false;
                        System.out.println("No such token color");
                    }else {
                        correctInput = true;
                    }
                }while(!correctInput);

                int gameId;
                do {
                    System.out.println("Insert game id: ");
                    System.out.print("> ");
                    gameId = -1;
                    correctInput = true;
                    try {
                        gameId = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        correctInput = false;
                        System.out.println("No such game id, insert a number.");
                    }
                    if(correctInput && gameId < 0) {
                        System.out.println("No such game id.");
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
                    System.out.println("Insert token color (green, red, yellow or blue): ");
                    System.out.print("> ");
                    tokenColorString = scan.nextLine();
                    tokenColor = parseTokenColor(tokenColorString);
                    correctInput = true;
                    if(tokenColor == null) {
                        correctInput = false;
                        System.out.println("No such token color");
                    }
                }while(!correctInput);

                int playersNumber = -1;
                do {
                    correctInput = true;
                    System.out.println("Insert the number of players for the game: ");
                    System.out.print("> ");
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
                System.out.println("The provided character doesn't refer to any action");
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
            System.out.println("Insert a character to perform an action:");
            System.out.println("- q to write a private message");
            System.out.println("- w to write a public message");
            System.out.println("- e to disconnect from the game");
            System.out.println("- r to draw a card from a deck");
            System.out.println("- t to draw a face up card");
            System.out.println("- y to place a card");
            System.out.println("- u to set initial cards");
            System.out.println("- i to see another player's game field");
            System.out.println("- o to see the whole chat");
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
                    // check not empty
                    if(receiver == null || receiver.isEmpty()) {
                        System.out.println("Insert a non empty receiver");
                        break;
                    }
                    // check sender not equals receiver
                    if(receiver.equals(nickname)) {
                        System.out.println("You can't send a private message to yourself.");
                        break;
                    }
                    // check existing receiver
                    if(!client.getGameView().checkPlayerPresent(receiver)) {
                        System.out.println("Provided receiver doesn't exist.");
                        break;
                    }
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    // check not empty content
                    if(content == null || content.isEmpty()) {
                        System.out.println("Content can't be empty");
                        break;
                    }

                    client.setAndExecuteCommand(new AddChatPrivateMessageCommand(content, nickname, receiver));
                    break;

                case "w":
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    // check not empty content
                    if(content == null || content.isEmpty()) {
                        System.out.println("Content can't be empty");
                        break;
                    }

                    client.setAndExecuteCommand(new AddChatPublicMessageCommand(content, nickname));
                    break;

                case "e":
                    client.setAndExecuteCommand(new DisconnectPlayerCommand(nickname));
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
                        break;
                    }
                    // check game state
                    if(!client.getGameView().getGameState().equals(GameState.PLAYING)) {
                        System.out.println("Wrong game state.");
                        break;
                    }
                    // check current player
                    if(!client.getGameView().isCurrentPlayer(nickname)) {
                        System.out.println("This is not your turn, try later.");
                        break;
                    }
                    client.setAndExecuteCommand(new DrawDeckCardCommand(nickname, cardType));
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
                        break;
                    }
                    System.out.println("Select the position of the card to draw: ");
                    System.out.print("> ");
                    int pos;
                    try {
                        pos = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    // check valid position
                    if(pos < 0 || pos > client.getGameView().getNumFaceUpCards(cardType)) {
                        System.out.println("Wrong cards position.");
                        break;
                    }
                    // check game state
                    if(!client.getGameView().getGameState().equals(GameState.PLAYING)) {
                        System.out.println("Wrong game state.");
                        break;
                    }
                    // check current player
                    if(!client.getGameView().isCurrentPlayer(nickname)) {
                        System.out.println("This is not your turn, try later.");
                        break;
                    }
                    client.setAndExecuteCommand(new DrawFaceUpCardCommand(nickname, cardType, pos));
                    break;

                case "y":
                    // position in hand
                    System.out.println("Select the position of the card you want to place: ");
                    System.out.print("> ");
                    int cardPos;
                    try {
                        cardPos = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    if(cardPos < 0 || cardPos >= client.getGameView().getCurrHandSize()) {
                        System.out.println("Wrong card hand position.");
                        break;
                    }
                    System.out.println("Insert a position of the game field where you want to place the card.");
                    // position in game field - x
                    System.out.println("Insert x: ");
                    System.out.print("> ");
                    int x;
                    try {
                        x = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    if(x < 0 || x >= client.getGameView().getGameFieldDim()) {
                        System.out.println("GameField position out of bound.");
                        break;
                    }
                    // position in game field - y
                    System.out.println("Insert y: ");
                    System.out.print("> ");
                    int y;
                    try {
                        y = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    if(y < 0 || y >= client.getGameView().getGameFieldDim()) {
                        System.out.println("GameField position out of bound.");
                        break;
                    }
                    // way in game field
                    System.out.println("Select 0 to place the card face up, 1 to place the card face down: ");
                    System.out.print("> ");
                    try {
                        wayInput = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        System.out.println("The provided value for way is not correct");
                        break;
                    }
                    // check game state
                    if(!client.getGameView().getGameState().equals(GameState.PLAYING)) {
                        System.out.println("Wrong game state.");
                        break;
                    }
                    // check current player
                    if(!client.getGameView().isCurrentPlayer(nickname)) {
                        System.out.println("This is not your turn, try later.");
                        break;
                    }

                    // create and execute command
                    client.setAndExecuteCommand(new PlaceCardCommand(nickname, cardPos, x, y, way));
                    break;

                case "u":
                    System.out.println("Select 0 to place the starter card face up, 1 to place the starter card face down: ");
                    System.out.print("> ");
                    try {
                        wayInput = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    boolean startCardWay;
                    if(wayInput == 1) {
                        startCardWay = true;
                    }else if(wayInput == 0) {
                        startCardWay = false;
                    }else {
                        System.out.println("The provided value is not correct");
                        break;
                    }

                    System.out.println("Select 0 to choose the first secret objective, 1 to choose the second one: ");
                    System.out.print("> ");
                    boolean objectiveCard;
                    int objectiveCardInt;
                    try {
                        objectiveCardInt = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    if(objectiveCardInt == 0) {
                        objectiveCard = false;
                    }else if(objectiveCardInt == 1) {
                        objectiveCard = true;
                    }else {
                        System.out.println("The provided value is not correct");
                        break;
                    }

                    if(!client.getGameView().getGameState().equals(GameState.SETTING_INITIAL_CARDS)) {
                        System.out.println("Wrong game state.");
                        break;
                    }

                    client.setAndExecuteCommand(new SetInitialCardsCommand(nickname, startCardWay, objectiveCard));
                    break;

                case "i":
                    System.out.println("Insert other player's nickname");
                    System.out.print("> ");
                    String nickname = scan.nextLine();
                    // check existing player
                    if(nickname == null || nickname.isEmpty() || !client.getGameView().checkPlayerPresent(nickname)) {
                        System.out.println("Provided nickname doesn't exist in the game.");
                        break;
                    }
                    System.out.println();
                    System.out.println("--------------------------------------------------------");
                    System.out.println("                     " + nickname + "'s GAME FIELD");
                    System.out.println("--------------------------------------------------------");
                    printGameField(nickname);
                    System.out.println("\n\n");
                    break;

                case "o":
                    System.out.println();
                    System.out.println("--------------------------------------------------------");
                    System.out.println("                            CHAT                        ");
                    System.out.println("--------------------------------------------------------");
                    ChatTui.printChat(client.getGameView().getOwnerMessages());
                    System.out.println();
                    break;

                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
        }

        askForReconnection();
    }

    /**
     * Method used to ask for reconnection to a player.
     */
    public void askForReconnection() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n\nDo you want to reconnect (1 = yes, other = no)?");
        System.out.print("> ");
        int reconnect = 0;
        try {
            reconnect = scan.nextInt();
            scan.nextLine();
        }catch(InputMismatchException e) {
            System.exit(0);
        }
        if(reconnect == 1) {
            System.out.println("\n\n");
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
        receiveGameFieldUpdate(nickname, gameField.getCardsContent(), gameField.getCardsFace(), gameField.getCardsOrder());
    }

    /**
     * Method used to notify the player he has received a new chat chatMessage.
     * @param chatMessage chat message
     */
    @Override
    public void receiveMessageUpdate(ChatMessage chatMessage) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                      GAME CHAT                         ");
        System.out.println("--------------------------------------------------------");
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
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                   PLAYER GAME FIELD                    ");
        System.out.println("--------------------------------------------------------");
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
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                     STARTER CARD                       ");
        System.out.println("--------------------------------------------------------");
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
        if(!(client.getGameView().getGameState().equals(GameState.GAME_STARTING) && (hand.size() < 3 || personalObjectives == null || personalObjectives.size() != 1))) {
            System.out.println();
            System.out.println("--------------------------------------------------------");
            System.out.println("                  FRONT PLAYER HAND                     ");
            System.out.println("--------------------------------------------------------");
            PlayerTui.printPlayerHand(hand, personalObjectives,false);
            System.out.println();
            System.out.println("--------------------------------------------------------");
            System.out.println("                   BACK PLAYER HAND                     ");
            System.out.println("--------------------------------------------------------");
            PlayerTui.printPlayerHand(hand, personalObjectives,true);
        }
    }

    /**
     * Method used to show the client an updated score.
     * @param playerScores players' scores
     * @param playerTokenColors players' token colors
     */
    @Override
    public void receiveScoreUpdate(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                      SCORE BOARD                       ");
        System.out.println("--------------------------------------------------------");
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
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                     GAME MODEL UPDATE                  ");
        System.out.println("--------------------------------------------------------");
        System.out.println("Game state: " + gameState);
        if(currPlayer == null) {
            currPlayer = "none";
        }
        System.out.println("Current player: " + currPlayer);

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
            System.out.println();
            System.out.println("--------------------------------------------------------");
            System.out.println("                      FRONT DECK                        ");
            System.out.println("--------------------------------------------------------");
            DeckTui.printDeck(commonObjective, faceUpGoldCard, faceUpResourceCard, topGoldDeck, topResourceDeck);
            System.out.println("--------------------------------------------------------");
            System.out.println("                       BACK DECK                        ");
            System.out.println("--------------------------------------------------------");
            DeckTui.printBackDeck(commonObjective, faceUpGoldCard, faceUpResourceCard, topGoldDeck, topResourceDeck);
        }
    }

    /**
     * Method used to inform the player that the current round is the penultimate round.
     */
    @Override
    public void receivePenultimateRoundUpdate() {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                     PENULTIMATE ROUND                  ");
        System.out.println("--------------------------------------------------------");
    }

    /**
     * Method used to inform the player that the current round is the additional round.
     */
    @Override
    public void receiveAdditionalRoundUpdate() {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                     ADDITIONAL ROUND                   ");
        System.out.println("--------------------------------------------------------");
    }

    /**
     * Method used to receive the last command result.
     * @param commandResult command result
     */
    @Override
    public void receiveCommandResultUpdate(CommandResult commandResult) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                      COMMAND RESULT                    ");
        System.out.println("--------------------------------------------------------");
        System.out.println(commandResult.getResultMessage());
    }

    /**
     * Method used to display existing and free games.
     * @param existingGamesPlayerNumber players number for each game
     * @param existingGamesTokenColor taken token colors for each game
     */
    @Override
    public void receiveExistingGamesUpdate(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {
        for(Integer id: existingGamesPlayerNumber.keySet()) {
            System.out.print("Id: " + id + " - " + "Number of players: " + existingGamesPlayerNumber.get(id));
        }
        for(Integer id: existingGamesTokenColor.keySet()) {
            System.out.print(" - " + "Taken token colors: ");
            for(TokenColor t: existingGamesTokenColor.get(id)) {
                System.out.print(t + " ");
            }
            System.out.println();
        }
        runJoinGameInterface();
    }

    /**
     * Method used to display winners.
     * @param winners winners' nicknames
     */
    @Override
    public void receiveWinnersUpdate(List<String> winners) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                         WINNERS                        ");
        System.out.println("--------------------------------------------------------");
        for(String winner: winners) {
            System.out.println(winner);
        }
        System.out.println();
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
        // TODO vogliamo stampare
    }

    /**
     * Method used to show a connection update.
     * @param nickname   nickname
     * @param connection true if the player is connected, false otherwise
     */
    @Override
    public void receiveConnectionUpdate(String nickname, boolean connection) {
        // TODO vogliamo stampare
    }

    /**
     * Method used to show a stall update.
     * @param nickname nickname
     * @param stall    true if the player is stalled, false otherwise
     */
    @Override
    public void receiveStallUpdate(String nickname, boolean stall) {
        // TODO vogliamo stampare
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
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                         SECRET OBJECTIVES                        ");
        System.out.println("--------------------------------------------------------");
        if(!nickname.equals(client.getGameView().getOwnerNickname())) {
            return;
        }
        for(ObjectiveCard objectiveCard: secretObjectives) {
            PlayerTui.printOnlyObjectiveCard(objectiveCard);
        }
    }
}