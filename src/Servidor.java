/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;

import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guispiger
 */
public class Servidor {

    public static ArrayBlockingQueue<Participante> p2 = new ArrayBlockingQueue<>(10);
    private static final Logger logger = Logger.getLogger(Servidor.class.getName());

    public static void main(String[] args) {
        logger.setLevel(Level.INFO);

        try (ServerSocket listener = new ServerSocket(50123)) {
            logger.info("Servidor iniciado");
            ExecutorService fofoqueiro = Executors.newFixedThreadPool(10);

            while (true) {
                Participante p = new Participante(listener.accept(), fofoqueiro);

                p2.add(p);
                fofoqueiro.execute(p);
            }

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
