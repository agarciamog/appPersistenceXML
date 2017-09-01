package io.albertogarcia.workshop;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        if(!Files.exists(Paths.get("appSettings.xml"))) {
            try (InputStream in = Main.class.getResourceAsStream("defaultAppSettings.xml")) {
                Properties defaultProps = new Properties();
                defaultProps.loadFromXML(in);
                Properties userProps = new Properties(defaultProps);

                showMessages(userProps);

                if(userProps.getProperty("firstLaunch").equalsIgnoreCase("Y")) {
                    userProps.setProperty("welcome", "Welcome back!");
                    userProps.setProperty("bye", "Don't be afraid to comeback!");
                    userProps.setProperty("firstLaunch", "N");
                    writeSettings(userProps);
                }
            } catch (IOException e) {
                System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
            }
        } else {
            Properties userProps = loadSettings();
            showMessages(userProps);
        }

    }

    private static void showMessages(Properties props) {
        System.out.println(props.getProperty("welcome"));
        System.out.println(props.getProperty("bye"));
    }

    private static void writeSettings(Properties props) {
        try (OutputStream out = Files.newOutputStream(Paths.get("appSettings.xml"))) {
            props.storeToXML(out, "User Settings");
        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    private static Properties loadSettings() {
        Properties userProps = new Properties();

        try (InputStream in = Files.newInputStream(Paths.get("appSettings.xml"))) {
            userProps.loadFromXML(in);
        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        return userProps;
    }
}
