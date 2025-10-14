package es.etg.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main {
    public static final String MSG_ERROR = "Se ha producido un error al ejecutar el comando";
    public static final String[] COMANDOS = {"grep", "PSP"};

    public static final String[] MENSAJE_A_GREP = { 
        "Me gusta PSP y java",
        "PSP se programa en java",
        "es un módulo de DAM",
        "y se programa de forma concurrente en PSP",
        "PSP es programación"
    };

    public static final String SALTO_DE_LINEA = "\n";


    public static void main(String[] args) {

		try {
			Process process = Runtime.getRuntime().exec(COMANDOS);
			String output;

            try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                    
                for (String line : MENSAJE_A_GREP) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.flush();
                
            }

            readStream(process.getErrorStream());
			output = readStream(process.getInputStream());

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println(output);
				System.exit(0);
			} else {
				System.out.println(MSG_ERROR);
				System.exit(1);
			}

		} catch (IOException | InterruptedException e) {
		    System.out.println("Error"+ e);
		    System.exit(34);
		}
	}

    private static String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
            return output.toString();
        }
    }
}