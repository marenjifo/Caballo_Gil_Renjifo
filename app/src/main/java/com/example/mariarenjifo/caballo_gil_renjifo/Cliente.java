package com.example.mariarenjifo.caballo_gil_renjifo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Cliente extends Thread {

    MulticastSocket socket;
    int PUERTO;
    InetAddress address;
    InetAddress ipG;
    GetData datos;
    MainActivity activity;

    public Cliente(MainActivity activity) {
        this.activity = activity;
        this.datos = activity;

    }

    @Override
    public void run() {
        comnunicar();

        while (true) {
            recibir();
        }
    }

    public void comnunicar() {
        try {
            PUERTO = 5000;
            address = InetAddress.getByName("192.168.0.105");
            ipG = InetAddress.getByName("228.5.6.8");


            socket = new MulticastSocket(PUERTO);
socket.joinGroup(ipG);

            // socket.connect(address, PUERTO);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviar(final String mensaje) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket datagramPacket = new DatagramPacket(mensaje.getBytes(), mensaje.length(), address, PUERTO);

                try {
                    socket.send(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public void recibir() {
        byte[] informacion = new byte[100];
        DatagramPacket datagramPacket = new DatagramPacket(informacion, informacion.length);

        try {
            System.out.println("Esperando datos...........................................................");
            socket.receive(datagramPacket);
            System.out.println("Dato recivido...........................................................");
            String mensaje = new String(datagramPacket.getData()).trim();

            datos.getRecibido(mensaje);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface GetData {
        public void getRecibido(String mensaje);
    }
}