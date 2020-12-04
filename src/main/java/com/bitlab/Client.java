package com.bitlab;

import java.net.*;
import java.util.stream.Collectors;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Klasa pozwalająca na połączenie z serwerem, i wysłanie wiadomości
 * @author Marek Pałdyna
 * @version 1.0
 */
public class Client {
    private Socket client_socket;
    private PrintWriter out;
    private BufferedReader in;
    boolean isConnected = false;

    public Client(String ip, int port){
        connect(ip, port);
    }
    public Client(){

    }
    /**
     * Próbuje połączyć się z serwerem pod podanym id i na podanym porcie
     * Używa TCP
     * Jeśli dany klient był już połączony, zamyka stare połączenie i otwiera nowe
     * @param ip
     * @param port
     */
    private void connect(String ip, int port) {
        if (isConnected) {
            close();
        }
        try {
            client_socket = new Socket(ip, port);

            out = new PrintWriter(client_socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Wysłanie wiadomości do podłączonego serwera
     * @param msg - wiadomość do wysłania
     * @return - wiadomość odebrana od serwera
     * @throws IOException
     * @throws Exception
     */
    public String send(String msg) throws IOException, Exception {
        if (!isConnected) {
            throw new Exception("Client not Connected yet");
        } else {
            out.println(msg);
            String resposnse = in.lines().collect(Collectors.joining());;
            return resposnse;
        }
    }
    /**
     * zamyka socket - "połączenie"
     */
    public void close(){
        try {
            in.close();
            out.close();
            client_socket.close();
            isConnected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
