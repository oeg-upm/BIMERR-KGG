import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesFile {

    public static void main(String[] args){

        String cwd = System.getProperty("user.dir");
        String properties_file = cwd + "/utils/config.properties";

        try (OutputStream output = new FileOutputStream(properties_file)) {

            Properties prop = new Properties();

            prop.setProperty("user", "dba");
            prop.setProperty("pass", "dba");

            prop.store(output, null);
            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}
