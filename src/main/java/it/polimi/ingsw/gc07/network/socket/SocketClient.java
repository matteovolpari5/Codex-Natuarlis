package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.updates.*;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SocketClient  {
    private final String nickname;
    private final Socket mySocket;
    private final GameView gameView;
    private final ObjectInputStream input;
    private VirtualSocketServer myServer;



    public SocketClient(String nickname, Socket mySocket) throws IOException {
        System.out.println("SocketClient-costruttore>>>>>>>>>");
        InputStream temp_input;
        OutputStream temp_output;

        this.nickname = nickname;
        System.out.println("SocketClient> nickname ok");

        this.mySocket = mySocket;
        System.out.println("SocketClient> mySocket ok");

        this.gameView = new GameView(nickname);
        System.out.println("SocketClient> gameView ok");

        temp_output = this.mySocket.getOutputStream();
        System.out.println("SocketClient> temp_output ok");
        ObjectOutputStream output = new ObjectOutputStream(temp_output);
        System.out.println("SocketClient> ObjectOutputStream output ok");
        //output.flush();

        temp_input = this.mySocket.getInputStream();
        System.out.println("SocketClient> temp_input ok");
        this.input = new ObjectInputStream(temp_input);
        System.out.println("SocketClient> ObjectInputStream input ok");

        this.myServer = new VirtualSocketServer(output);
        System.out.println("SocketClient> myServer output ok");

        System.out.println("SocketClient> invocazione run()");
        this.run();
        System.out.println("SocketClient> fine costruttore, dopo run()");
    }

    private void run(){
        new Thread(() -> {
            try{
                manageReceivedMessage();
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }).start();
    }

    //TODO oppure se non deve condividere il metodo allora valutare se ricevere nel costruttore il tipo di interfaccia, run() esegue alla fine connectToGamesManager()
    //TODO e in ClientMain avere solo "new SocketClient(nickname, sc);" con aggiunta del tipo di interfaccia; connectToGamesManger() diventa quindi private
    public void connectToGamesManager(boolean connectionType, boolean interfaceType) {
        System.out.println("SocketClient-connectToGamesManager>>>>>>>>>");
        try {
            myServer.setAndExecuteCommand(new AddPlayerToPendingCommand(nickname, connectionType, interfaceType));
            System.out.println("SocketClient> eseguo AddPlayerToPendingCommand");
        } catch (RemoteException e) {
            // TODO
            throw new RuntimeException(e);
        }
        //TODO controllo esito command?
        System.out.println("SocketClient> passo a runCliJoinGame()");
        this.runCliJoinGame();
    }
    private void manageReceivedMessage() {
        System.out.println("Client_Thread-manageReceiveMessage>>>>>>>>>");
        Update update;
        while (true){ //TODO dalla documentazione non trovo un modo di utilizzare il risultato di readObject() come condizione del while, chiedere se così va bene
            try {
                System.out.println("Client_Thread> ascolto");
                update = (Update) input.readObject();
                System.out.println("Client_Thread> leggo un update");
                update.execute(gameView);
                System.out.println("Client_Thread> eseguo l'update");
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            //TODO se all'iterazione successiva non è stato scritto alcun nuovo oggetto, alla riga 52 cosa succede?
        }
    }


    public void runCliJoinGame() {
        System.out.println("SocketClient-runCliJoinGame>>>>>>>>>");
        boolean joiningGame = true;
        Scanner scan = new Scanner(System.in);
        String tokenColorString;
        TokenColor tokenColor;
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
                    System.out.println("Insert game id: ");
                    int gameId = scan.nextInt();
                    scan.nextLine();
                    try {
                        myServer.setAndExecuteCommand(new JoinExistingGameCommand(nickname, tokenColor, gameId));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
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
                    try {
                        myServer.setAndExecuteCommand(new JoinNewGameCommand(nickname, tokenColor, playersNumber));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    joiningGame = false;
                    break;
                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
        }
        // game joined
        runCliGame();
    }

    private void runCliGame() {
        Scanner scan = new Scanner(System.in);
        String content;
        String cardTypeString;
        CardType cardType;
        int wayInput;
        boolean way;
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
                    content = scan.nextLine();
                    try {
                        myServer.setAndExecuteCommand(new AddChatPrivateMessageCommand(content, nickname, receiver));
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
                        myServer.setAndExecuteCommand(new AddChatPublicMessageCommand(content, nickname));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "e":
                    try {
                        myServer.setAndExecuteCommand(new DisconnectPlayerCommand(nickname));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
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
                        continue; //TODO perchè non break?
                    }
                    try {
                        myServer.setAndExecuteCommand(new DrawDeckCardCommand(nickname, cardType));
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
                        continue; //TODO perchè non break?
                    }
                    System.out.println("Select the position of the card to draw: ");
                    System.out.print("> ");
                    int pos = scan.nextInt();
                    scan.nextLine();
                    //TODO possiamo introdurre un controllo per evitare una chiamata
                    // inutile se la posizione eccede il range possibile
                    try {
                        myServer.setAndExecuteCommand(new DrawFaceUpCardCommand(nickname, cardType, pos));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "y":
                    // String nickname, int pos, int x, int y, boolean way
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
                        continue; //TODO perchè non break?
                    }
                    // create and execute command
                    try {
                        myServer.setAndExecuteCommand(new PlaceCardCommand(nickname, cardPos, x, y, way));
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
                        continue; //TODO perchè non break?
                    }
                    try {
                        myServer.setAndExecuteCommand(new PlaceStarterCardCommand(nickname, way));
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
}
