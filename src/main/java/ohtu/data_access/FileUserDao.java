package ohtu.data_access;

import ohtu.domain.User;

import javax.jws.soap.SOAPBinding;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by emivo on 10.4.2016.
 */
public class FileUserDao implements UserDao {
    private String filepath;

    public FileUserDao() {
        this.filepath = "passwords.txt";
    }

    public FileUserDao(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public List<User> listAll() {
        ArrayList<User> userList = new ArrayList<User>();
        try {
            reading(userList);
        } catch (Exception e) {
            System.err.println("Couldn't read from file");
            return null;
        }
        return userList;
    }

    private void reading(ArrayList<User> userList) throws FileNotFoundException {
        Scanner reader = new Scanner(new File(filepath));
        reader.useDelimiter(System.getProperty("line.separator"));
        while (reader.hasNext()) {
            String line = reader.next();
            String[] parts = line.split("\t");
            userList.add(new User(parts[0], parts[1]));
        }
        reader.close();
    }

    @Override
    public User findByName(String name) {
        Iterator<User> userIterator = listAll().iterator();
        while (userIterator.hasNext()) {
            User user = userIterator.next();
            if (user.getUsername().equals(name)) return user;
        }
        return null;
    }

    @Override
    public void add(User user) {
        try {
            FileWriter writer = new FileWriter(filepath, true);
            writer.write(user.getUsername() + "\t" + user.getPassword() + "\n");
            writer.close();
        } catch (Exception e) {
            System.err.println("Couldn't write in file");
        }
    }
}
