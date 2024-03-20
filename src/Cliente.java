/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guispiger
 */
public class Cliente {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Passe o endereco IP do servidor e o apelido do usuário como argumento na linha de comando: ");
            System.err.println("java Cliente.java <enderecoIP> <apelido>");
            return;
        }
        try (Socket socket = new Socket(args[0], 50123)) {
            System.out.println("Entre com as linhas de texto e então Ctrl+C ou digite: ##sair## para sair");
            Scanner scanner = new Scanner(System.in);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            //Envia apelido do usuario
            out.println(args[1]);

            // Thread para receber mensagens do servidor
            Thread threadReceber = new Thread(() -> {
                while (in.hasNextLine()) {
                    System.out.println(in.nextLine());
                }
            });
            threadReceber.start();

            // Thread para enviar mensagens para o servidor
            Thread threadEnviar = new Thread(() -> {
                String mensagem;
                while (true) {
                    mensagem = scanner.nextLine();
                    out.println(mensagem);

                    if (mensagem.equals("##sair##")) {
                        break;
                    }
                }
            });
            threadEnviar.start();

            threadReceber.join();
            threadEnviar.join();

            //Fecha conexões
            scanner.close();
            in.close();
            out.close();

        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
