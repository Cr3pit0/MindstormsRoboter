package robot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static BufferedWriter writer;
    static DateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss:SSS");

    static {
        String file = "";
        try {
            file = new File(Logger.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                    .getParentFile().getPath() + File.separator + "robot.log";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        try {
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(Level level, String msg) {
        String message = format.format(new Date(System.currentTimeMillis())) + " - " + level.name() + " - " + msg + "\n";
        System.out.println(msg);
        try {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
