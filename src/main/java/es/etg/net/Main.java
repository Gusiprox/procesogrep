package es.etg.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static final String FILTRO = "host*";
    public static final String DIRECTORIO = "//etc";

    public static final String MSG_CORRECTO = "La lista filtrada es: ";
    public static final String MSG_ERROR = "Se ha producido un error al ejecutar el comando";
    
    public static final String SALTO_DE_LINEA = "\n";

    public static void main(String[] args) {

        final String[] COMANDO_LS = {"ls", DIRECTORIO};
        String salidaLs[];

        final String[] COMANDO_GREP = {"grep", FILTRO};
        String salidaGrep[];

        salidaLs = ejecCommand(COMANDO_LS, null);

        salidaGrep = ejecCommand(COMANDO_GREP, salidaLs);

        System.out.println(MSG_CORRECTO + Arrays.toString(salidaGrep));

    }

    private static String[] ejecCommand(String[] comando, String[] writeInProcess){
        
        String[] ERROR_VALUE = {""};
        
        try {
            Process process = Runtime.getRuntime().exec(comando);
            String output[];
            String errOutput[];

            if (writeInProcess != null) write(process.getOutputStream(), writeInProcess);

            errOutput = read(process.getErrorStream());
            output = read(process.getInputStream());

            int exitVal = process.waitFor();
            if (exitVal == 0) {

                return output;

            } else {

                System.err.println(MSG_ERROR);
                System.err.println(Arrays.toString(errOutput));
                return ERROR_VALUE;
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error" + e);
            return ERROR_VALUE;

        }
    }

    private static String[] read(InputStream is) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            List<String> output = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            //Revisar si esto funciona bien
            return output.toArray(String[]::new);

        }
    }

    private static void write(OutputStream os, String[] mensaje) throws IOException{

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {

            for (String line : mensaje) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();

        }
    }
}
