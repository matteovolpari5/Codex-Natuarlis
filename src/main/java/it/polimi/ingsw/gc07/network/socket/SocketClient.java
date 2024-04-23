package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SocketClient implements VirtualView {
    private final String nickname;
    private final Socket mySocket;
    private final ObjectInputStream input;
    private VirtualSocketServerGamesManager myServer;
    private VirtualServerGame gameServer; //TODO dovrebbe essere VirtualSocketServerGame e non VirtualServerGame, tuttavia nell'interfaccia utilizza il secondo,
                                         //TODO dato che VirtualView è usata anche in RMI crea errori, per non causare problemi in RMI qui viene posta a VirtualServerGame
                                         //TODO tenere solo uno dei due

    public SocketClient(Socket mySocket, String nickname) throws IOException {
        this.nickname = nickname;
        this.mySocket = mySocket;
        this.input = new ObjectInputStream(mySocket.getInputStream());
        ObjectOutputStream output = new ObjectOutputStream(mySocket.getOutputStream());
        this.myServer = new VirtualSocketServerGamesManager(output);
    }

    public void run(){  //TODO dovrebbe essere private
        new Thread(() -> {
            try{
                runVirtualServer();
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }).start();
        runCliJoinGame();
    }

    private void runVirtualServer() throws IOException, ClassNotFoundException {
        //gestisce i messaggi ottenuti dal server
        GenericMessage message;
        while (true){
            message = (GenenricMessage)input.readObject();
            //TODO (...)
        }
    }

    public void runCliJoinGame() {
        boolean joiningGame = true;
        Scanner scan = new Scanner(System.in);
        while(joiningGame) {
            System.out.println("Insert a character to perform an action:");
            System.out.println("- q to join an existing game"); // JoinExistingGameCommand
            System.out.println("- w to join an new game"); // JoinNewGameCommand
            System.out.print("> ");
            String command = scan.nextLine();
            switch(command){
                case "q":
                    // join existing game

                    //TODO display existing games
                    // penso così:
                    // 1) inserisce un comando e manda la richiesta di entrare in un gioco esistente
                    // 2) il server riceve questa richiesta e chiama un metodo della virtual view che mostra i giochi esistenti
                    // 3) il client fa queste cose sotto
                    // 4) il server deve controllare di nuovo se il gioco è disponibile

                    System.out.println("Insert token color (green, red, yellow or blue): ");
                    System.out.print("> ");
                    String tokenColorString = scan.nextLine();
                    TokenColor tokenColor;
                    switch(tokenColorString) {
                        case "green":
                            tokenColor = TokenColor.GREEN;
                            break;
                        case "red":
                            tokenColor = TokenColor.RED;
                            break;
                        case "yellow":
                            tokenColor = TokenColor.YELLOW;
                            break;
                        case "blue":
                            tokenColor = TokenColor.BLUE;
                            break;
                        default:
                            System.out.println("No such token color");
                            continue;
                    }
                    System.out.println("Insert game id: ");
                    int gameId = scan.nextInt();
                    scan.nextLine();
                    GamesManagerCommand gamesManagerCommand = new JoinExistingGameCommand(nickname, tokenColor, gameId);
                    myServer.setAndExecuteCommand(gamesManagerCommand); //TODO eccezione
                    joiningGame = false;
                    break;


                case "w":
                    // join new game
                    System.out.println("Insert token color (green, red, yellow or blue): ");
                    System.out.print("> ");
                    tokenColorString = scan.nextLine();
                    switch(tokenColorString) {
                        case "green":
                            tokenColor = TokenColor.GREEN;
                            break;
                        case "red":
                            tokenColor = TokenColor.RED;
                            break;
                        case "yellow":
                            tokenColor = TokenColor.YELLOW;
                            break;
                        case "blue":
                            tokenColor = TokenColor.BLUE;
                            break;
                        default:
                            System.out.println("No such token color");
                            continue;
                    }
                    System.out.println("Insert the number of players for the game: ");
                    int playersNumber = scan.nextInt();
                    scan.nextLine();
                    // TODO potremmo fare già qua il controllo su players number per efficienza
                    gamesManagerCommand = new JoinNewGameCommand(nickname, tokenColor, playersNumber);
                    myServer.setAndExecuteCommand(gamesManagerCommand); //TODO eccezione
                        myServer.setAndExecuteCommand(new JoinNewGameCommand(nickname, tokenColor, playersNumber));
                    joiningGame = false;
                    break;
                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
        }
        // game joined
        //TODO valutare: connectToGameServer();
        runCliGame();
    }

    private void runCliGame() {
        Scanner scan = new Scanner(System.in);
        while(true) {
            System.out.println("Insert a character to perform an action:");
            System.out.println("- q to write a private message"); // AddChatPrivateMessage
            System.out.println("- w to write a public message"); // AddChatPublicMessage
            System.out.println("- e to disconnect from the game"); // DisconnectPlayerCommand
            System.out.println("- r to draw a card from a deck"); // DrawDeckCardCommand
            System.out.println("- t to draw a face up card"); // DrawFaceUpCardCommand
            System.out.println("- y to place a card"); // PlaceCardCommand
            System.out.println("- u to place the starter card"); // PlaceStarterCardCommand
            System.out.print("> ");
            String command = scan.nextLine();
            switch(command){
                case "q":
                    System.out.println("Insert the receiver nickname: ");
                    System.out.print("> ");
                    String receiver = scan.nextLine();
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    String content = scan.nextLine();
                    try {
                        serverGame.setAndExecuteCommand(new AddChatPrivateMessageCommand(content, nickname, receiver));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "w":
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    try {
                        serverGame.setAndExecuteCommand(new AddChatPublicMessageCommand(content, nickname));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "e":
                    try {
                        serverGame.setAndExecuteCommand(new DisconnectPlayerCommand(nickname));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "r":
                    System.out.println("Select a card type ('g' for gold or 'r' for resource): ");
                    System.out.print("> ");
                    String cardTypeString = scan.nextLine();
                    CardType cardType;
                    if(cardTypeString.equals("r")) {
                        cardType = CardType.RESOURCE_CARD;
                    }else if(cardTypeString.equals("g")) {
                        cardType = CardType.GOLD_CARD;
                    }else {
                        System.out.println("No such card type");
                        continue;
                    }
                    try {
                        serverGame.setAndExecuteCommand(new DrawDeckCardCommand(nickname, cardType));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
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
                    try {
                        serverGame.setAndExecuteCommand(new DrawFaceUpCardCommand(nickname, cardType, pos));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
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
                    int wayInput = scan.nextInt();
                    scan.nextLine();
                    boolean way;
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        System.out.println("The provided value for way is not correct");
                        continue;
                    }
                    // create and execute command
                    try {
                        serverGame.setAndExecuteCommand(new PlaceCardCommand(nickname, cardPos, x, y, way));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
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
                    try {
                        serverGame.setAndExecuteCommand(new PlaceStarterCardCommand(nickname, way));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
        }
    }

    @Override
    public void setServerGame(VirtualServerGame gameServer) throws RemoteException {
        this.gameServer = gameServer;
        this.gamesManagerServer = null; //TODO in questo modo quando entra in una partita non può più comunicare con gamesManager
    }
    @Override
    public String getNickname() throws RemoteException {
        return null;
    }
}
