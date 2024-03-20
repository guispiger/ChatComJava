/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;

import java.util.logging.Logger;
import java.util.logging.*;

/**
 *
 * @author guispiger
 */
public class ServicoMensagem implements Runnable {

    private final String apelido;
    private final String mensagem;
    private final static Logger logger = Logger.getLogger(ServicoMensagem.class.getName());

    public ServicoMensagem(String apelido, String mensagem) {
        this.apelido = apelido;
        this.mensagem = mensagem;
    }

    @Override
    public void run() {
        Servidor.p2.forEach((participante) -> {
            logger.log(Level.INFO, "{0} - {1}", new Object[]{apelido.toUpperCase(), mensagem});
            participante.getWriter().println(apelido.toUpperCase() + " - " + mensagem);
        });
    }

}
