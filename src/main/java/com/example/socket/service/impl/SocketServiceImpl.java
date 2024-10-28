package com.example.socket.service.impl;

import java.io.IOException;
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
    private int port  = 8484;
    private volatile boolean listening = true;

    @Override
    @PostConstruct
    public void startServer() {
        new Thread(() -> {
            try {
                log.info("Inicio de un nuevo hilo");
                serverSocket = new ServerSocket(port);
                while (true ) {

                    try { 
                        Socket clienSocket = serverSocket.accept();
                        log.info("CLIENTE ACEPTADO {}" , clienSocket.getInetAddress());
                        new Thread(new ClientHandler(clienSocket)).start();
                       // new Thread(n)
                    } catch(Exception e) {
                        if (!listening) {
                            log.info("Servidor de sockets cerrado, no se aceptarán más conexiones.");
                            break;  // Salir del bucle si el servidor se cierra
                        }
                        log.error("Error al aceptar la conexión: {}", e.getMessage());
                    }
             
                }

            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }).start();
    }

    @Override
    @PostConstruct
    public void stopsServer() {
        listening = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                log.info("Servidor de sockets cerrado correctamente.");
            }
        } catch (IOException e) {
            log.info("Error al cerrar el servidor de sockets", e.getMessage());
        }
    }

}
