package com.example.socket.service.impl;

import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.stereotype.Service;

import com.example.socket.service.ISocketService;
import com.example.socket.service.handler.ClientHandler;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SocketServiceImpl implements ISocketService {

    private ServerSocket serverSocket;
    private int port  = 3020;

    @Override
    @PostConstruct
    public void startServer() {
        new Thread(() -> {
            try {
                log.info("Inicio de un nuevo hilo");
                serverSocket = new ServerSocket(port);
                while (true) {
                    Socket clienSocket = serverSocket.accept();
                    new Thread(new ClientHandler(clienSocket)).start();
                    log.info("CLIENTE ACEPTADO {}" , clienSocket.getInetAddress());
                   // new Thread(n)
                }

            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }).start();
    }

    @Override
    public void stopsServer() {

    }

}
