/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guispiger
 */
public class Participante implements Runnable {

    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final ExecutorService fofoqueiro;
    private static final Logger logger = Logger.getLogger(Participante.class.getName());

    public Participante(Socket socket, ExecutorService fofoqueiro) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.fofoqueiro = fofoqueiro;
    }

    @Override
    public void run() {
        try {
            String apelido = reader.readLine();
            logger.log(Level.INFO, "Novo participante conectado: {0}", apelido);

            String mensagem;
            while ((mensagem = reader.readLine()) != null && !mensagem.equals("##sair##")) {
                logger.log(Level.FINE, "preparando mensagem {0}", mensagem);

                ServicoMensagem sm = new ServicoMensagem(apelido, mensagem);
                fofoqueiro.execute(sm);

                logger.fine("mensagem enviada");
            }

            Servidor.p2.remove(this);
            logger.log(Level.INFO, "Participante desconectado: {0}", apelido);

            reader.close();
            writer.close();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro na comunica\u00e7\u00e3o com o participante: {0}", e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Erro ao fechar socket: {0}", ex.getMessage());
            }
        }
    }

    public PrintWriter getWriter() {
        return writer;
    }

}
