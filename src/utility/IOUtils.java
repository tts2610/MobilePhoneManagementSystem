package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class IOUtils {

    public static void main(String[] args) {

    }

    /**
     * Write object to binary file. Object must implement "Serializable"
     * interface.
     *
     * @param path: relative or absolute path of file to write
     * @param obj: serializable object
     */
    public static void writeObject(String path, Serializable obj) {
        try {
            // Dam bao file phai ton tai
            File file = new File(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
        }
    }

    /**
     * Read object from binary file. Object must implement "Serializable"
     * interface.
     *
     * @param path: relative or absolute path of file to write
     * @return serializable object
     */
    public static Serializable readObject(String path) {
        Serializable obj = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
            obj = (Serializable) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
        }

        return obj;
    }

    /**
     * Write STRING object to unicode file.
     *
     * @param path: relative or absolute path of file to write
     * @param str: string object
     */
    public static void writeUnicode(String path, String str) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(str);
            fileWriter.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Read STRING object from unicode file.
     *
     * @param path: relative or absolute path of file to write
     * @return string object
     */
    public static String readUnicode(String path) {
        StringBuilder result = new StringBuilder(4096);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String tmp = null;
            while ((tmp = bufferedReader.readLine()) != null) {
                result.append(tmp + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result.toString();
    }
}
