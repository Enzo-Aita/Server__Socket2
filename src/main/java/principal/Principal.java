package principal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class Principal {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("Digite a porta do servidor: ");
            int porta = sc.nextInt();

          
            Map<String, Map<String, Boolean>> voos = new HashMap<>();

         
            Map<String, Boolean> assentosA1 = new HashMap<>();
            assentosA1.put("1", true);
            assentosA1.put("2", true);
            assentosA1.put("3", true);
            voos.put("A1", assentosA1);

            Map<String, Boolean> assentosB2 = new HashMap<>();
            assentosB2.put("1", true);
            assentosB2.put("2", true);
            voos.put("B2", assentosB2);

            ServerSocket servidor = new ServerSocket(porta);
            System.out.println("Servidor aguardando conexão na porta " + porta + "...");

            Socket socket = servidor.accept();
            System.out.println("Cliente conectado: " + socket.getInetAddress().getHostAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String mensagem;
            while ((mensagem = in.readLine()) != null) {
                System.out.println("Recebido: " + mensagem);

                String[] partes = mensagem.split(";");
                if (partes.length != 3) {
                    out.println("Formato inválido");
                    continue;
                }

                String comando = partes[0];
                String voo = partes[1];
                String assento = partes[2];

                int resposta;

                if (!voos.containsKey(voo)) {
                    resposta = 3; 
                } else if (!voos.get(voo).containsKey(assento)) {
                    resposta = 2;
                } else {
                    if (comando.equalsIgnoreCase("C")) {
                        resposta = voos.get(voo).get(assento) ? 0 : 1;
                    } else if (comando.equalsIgnoreCase("M")) {
                        if (voos.get(voo).get(assento)) {
                            voos.get(voo).put(assento, false);
                            resposta = 4;
                        } else {
                            resposta = 1; 
                        }
                    } else {
                        resposta = -1; 
                    }
                }

                out.println(resposta);
            }

            servidor.close();
        } catch (IOException e) {
            System.err.println("Problemas de IO: " + e.getMessage());
        }
    }
}
