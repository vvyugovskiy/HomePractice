package utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    /**
     * Checked and Unchecked exceptions
     * checked before it compiled
     */
//    private static FileInputStream fileInputStream;

    static {
        // try with resource -> try/catch block can take a parameter, AutoClosable

        try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
            // file Reader as byte code
            // FileInputStream here is our configFile as Byte code
            properties = new Properties();
            // properties class can read byte code so we need the file as byte code
            properties.load(fileInputStream);

        } catch (Exception e) {
            System.out.println("The config file did not load");
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

//    public static void main (String [] args){
//        System.out.println(ConfigReader.getProperty("browser"));
    /**
     *
     */
//        System.out.println(ConfigReader.getProperty("hello"));
//    }
}
