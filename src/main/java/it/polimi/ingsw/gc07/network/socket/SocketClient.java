package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.enumerations.CardType;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.updates.*;
import it.polimi.ingsw.gc07.view.gui.Gui;
import it.polimi.ingsw.gc07.view.tui.Tui;

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
    private boolean viewAlive;



    public SocketClient(String nickname, Socket mySocket, String status, boolean interfaceType) throws IOException {
        System.out.println("SC> costruttore");
        InputStream temp_input;
        OutputStream temp_output;

        this.nickname = nickname;
        this.mySocket = mySocket;
        this.gameView = new GameView(nickname);

        temp_output = this.mySocket.getOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(temp_output);
        output.flush();

        temp_input = this.mySocket.getInputStream();
        this.input = new ObjectInputStream(temp_input);

        this.myServer = new VirtualSocketServer(output, nickname, status, interfaceType);
        this.viewAlive = true;

        manageSetUp(status, interfaceType);

    }

    private void manageSetUp(String status, boolean interfaceType){
        System.out.println(status);
        if(status.equals("new")){
            connectToGamesManagerServer(false, interfaceType);
        } else if(status.equals("reconnected")){
            new Thread(this::manageReceivedUpdate).start();
            new Thread(this::startGamePing).start();
            runCliGame();
        }
    }


    private void connectToGamesManagerServer(boolean connectionType, boolean interfaceType) {
        System.out.println("SC> connectToGMS");
        /*if(interfaceType) {
            // Gui
            this.gameView.addViewListener(new Gui());
        }else {
            // Tui
            this.gameView.addViewListener(new Tui());
        }*/
        try {
            myServer.setAndExecuteCommand(new AddPlayerToPendingCommand(nickname, connectionType, interfaceType));
        } catch (RemoteException e) {
            // TODO
            throw new RuntimeException(e);
        }
        //TODO controllo esito command?
        this.runCliJoinGame();
    }



    private void manageReceivedUpdate() {
        System.out.println("SC-T> manageReceivedUpdate");
        Update update;
        while (true){ //TODO dalla documentazione non trovo un modo di utilizzare il risultato di readObject() come condizione del while, chiedere se così va bene
            try {
                System.out.println("SC-T> ascolto");
                update = (Update) input.readObject();
                System.out.println("SC-T> ho letto un update, lo eseguo");
                update.execute(gameView);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void runCliJoinGame() {
        System.out.println("SC> runCliJoinGame");
        boolean gameJoined = false;
        Scanner scan = new Scanner(System.in);
        String tokenColorString;
        TokenColor tokenColor;
        while(!gameJoined) {
            System.out.println("Insert a character to perform an action:");
            System.out.println("- q to join an existing game"); // JoinExistingGameCommand
            System.out.println("- w to join an new game"); // JoinNewGameCommand
            System.out.println("- e to see existing games"); // DisplayGamesCommand
            System.out.print("> ");
            String command = scan.nextLine();
            switch(command){
                case "q":
                    // join existing game
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
                    } catch (RemoteException  e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "e":
                    // display existing games
                    try {
                        myServer.setAndExecuteCommand(new DisplayGamesCommand(nickname));
                    } catch (RemoteException e) {
                        // TODO
                        throw new RuntimeException(e);
                    }
                    break;

                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
            String result;
            try {
                result = (String) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(result.equals("Game joined.")){
                System.out.println("Game joined.");
                gameJoined = true;
                new Thread(() -> {
                    try{
                        manageReceivedUpdate();
                    } catch (Exception e){
                        throw new RuntimeException(e);
                    }
                }).start();
            }else if(result.equals("Display successful.")){
                Update update;
                try {
                    update = (Update) input.readObject();
                    update.execute(gameView);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // game joined
        new Thread(this::startGamePing).start();
        runCliGame();
    }

    private void runCliGame() {
        System.out.println("SC> runCliGame");
        Scanner scan = new Scanner(System.in);
        String content;
        String cardTypeString;
        CardType cardType;
        int wayInput;
        boolean way;
        while(viewAlive) {
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
            switch(command){
                case "q":
                    System.out.println("Insert the receiver nickname: ");
                    System.out.print("> ");
                    String receiver = scan.nextLine();
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    try {
                        myServer.setAndExecuteCommand(new AddChatPrivateMessageControllerCommand(content, nickname, receiver));
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
                        myServer.setAndExecuteCommand(new AddChatPublicMessageControllerCommand(content, nickname));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "e":
                    try {
                        myServer.setAndExecuteCommand(new DisconnectPlayerControllerCommand(nickname));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    System.out.println("\nYou successfully disconnected !");
                    viewAlive = false;
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
                    try {
                        myServer.setAndExecuteCommand(new DrawDeckCardControllerCommand(nickname, cardType));
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
                        myServer.setAndExecuteCommand(new DrawFaceUpCardControllerCommand(nickname, cardType, pos));
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
                        continue;
                    }
                    // create and execute command
                    try {
                        myServer.setAndExecuteCommand(new PlaceCardControllerCommand(nickname, cardPos, x, y, way));
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
                        myServer.setAndExecuteCommand(new PlaceStarterCardControllerCommand(nickname, way));
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

    public void startGamePing() {
        System.out.println("SC-T2> startGamePing");
        boolean runThread = true;
        while(runThread) {
            synchronized (this) {
                if (viewAlive) {
                    try {
                        myServer.setAndExecuteCommand(new SendPingControllerCommand(nickname));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    runThread = false;
                }
            }
            try {
                Thread.sleep(1000); // wait one second between two ping
            } catch (InterruptedException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
