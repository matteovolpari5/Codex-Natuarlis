package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.network.Client;
import it.polimi.ingsw.gc07.network.PingSender;
import it.polimi.ingsw.gc07.updates.*;
import it.polimi.ingsw.gc07.view.Ui;
import it.polimi.ingsw.gc07.view.gui.Gui;
import it.polimi.ingsw.gc07.view.tui.Tui;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClient implements Client, PingSender {
    private final String nickname;
    private final Socket mySocket;
    private final GameView gameView;
    private final ObjectInputStream input;
    private VirtualSocketServer myServer;
    private boolean clientAlive;
    private Ui ui;

    public SocketClient(String nickname, Socket mySocket, String status, boolean interfaceType) throws IOException {
        System.out.println("SC> costruttore");
        InputStream temp_input;
        OutputStream temp_output;

        this.nickname = nickname;
        this.mySocket = mySocket;
        this.gameView = new GameView(nickname);
        this.ui = null;

        temp_output = this.mySocket.getOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(temp_output);
        output.flush();

        temp_input = this.mySocket.getInputStream();
        this.input = new ObjectInputStream(temp_input);

        this.myServer = new VirtualSocketServer(output, nickname, status, interfaceType);
        this.clientAlive = true;

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
        if(interfaceType) {
            // Gui
            this.ui = new Gui();

        }else {
            // Tui
            this.ui = new Tui(nickname, this);
        }
        // this.gameView.addViewListener(ui);
        // TODO per ora rimosso
        try {
            myServer.setAndExecuteCommand(new AddPlayerToPendingCommand(nickname, connectionType, interfaceType));
        } catch (RemoteException e) {
            // TODO
            throw new RuntimeException(e);
        }
        //TODO controllo esito command?
        this.runCliJoinGame();
    }

    public void runCliJoinGame() {
        assert(ui != null);
        ui.runCliJoinGame();
        String result;
        try {
            result = (String) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println(result);
        if(result.equals("Game joined.")){
            System.out.println("entro");
            new Thread(() -> {
                try{
                    manageReceivedUpdate();
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            }).start();
            // game joined
            new Thread(this::startGamePing).start();
            runCliGame();
        }else{
            if(result.equals("Display successful.")){
                Update update;
                try {
                    update = (Update) input.readObject();
                    update.execute(gameView);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            runCliJoinGame();
        }
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

    public void runCliGame() {
        assert(ui != null);
        ui.runCliGame();
    }

    @Override
    public void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) {
        try {
            myServer.setAndExecuteCommand(gamesManagerCommand);
        } catch (RemoteException e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void setAndExecuteCommand(GameControllerCommand gameControllerCommand) {
        try {
            myServer.setAndExecuteCommand(gameControllerCommand);
        } catch (RemoteException e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public synchronized void setClientAlive(boolean isAlive) {
        this.clientAlive = isAlive;
    }

    @Override
    public synchronized boolean isClientAlive() {
        return clientAlive;
    }

    @Override
    public void startGamePing() {
        System.out.println("SC-T2> startGamePing");
        while(true) {
            synchronized (this) {
                if (!clientAlive) {
                    break;
                }
            }
            try {
                myServer.setAndExecuteCommand(new SendPingCommand(nickname));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
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
