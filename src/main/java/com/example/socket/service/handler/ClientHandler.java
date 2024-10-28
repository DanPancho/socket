package com.example.socket.service.handler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketTimeoutException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ClientHandler implements Runnable {

    private Socket clientSocketPeticion;

    @Override
    public void run() {
        try (
                InputStream inputStream = clientSocketPeticion.getInputStream();
                OutputStream outputStream = clientSocketPeticion.getOutputStream();
                DataInputStream din = new DataInputStream(inputStream);) {
            clientSocketPeticion.setSoTimeout(5000);
            clientSocketPeticion.setKeepAlive(true);
            // byte[] data = readData(din);
            byte[] buffer = new byte[1024];
            int bytesRead;
            log.info("ANTES WHILE");
            while ((bytesRead = din.read(buffer)) != -1) {
                String message = new String(buffer, 0, bytesRead);
                log.info("Mensaje recibido: {}", message);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            try {
                clientSocketPeticion.close();
            } catch (IOException e) {
                log.info("Error al cerrar el socket del cliente", e);
            }
        }
    }

    private byte[] readData(DataInputStream din) throws IOException {

        byte[] lengthData = new byte[4]; // Cambia el tamaño según tu protocolo de longitud
        int count = din.read(lengthData);
        if (count == -1)
            return new byte[0];

        BigInteger messageLength = new BigInteger(1, lengthData);

        byte[] data = new byte[messageLength.intValue()];
        try {
            count = din.read(data);

        } catch (SocketTimeoutException e) {

            return new byte[0];
        }
        return data;
    }

}
