package services;

import menu.BuyMenu;
import model.Product;
import model.Store;
import model.User;
import utils.ApplicationConst;
import utils.TxtFileReader;

import java.util.ArrayList;
import java.util.logging.Logger;

public class LoginService {
    private User user;
    private Store store;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());

    public LoginService(User user) {
        this.user = user;
    }


    public User loginUser(String userId, String password) {
        User user = null;
        TxtFileReader txtFileReader = new TxtFileReader(ApplicationConst.FILE_USERS_PATH);
        ArrayList<String> lines = txtFileReader.read();

        for (String line : lines) {
            String[] tokens = line.split(" ");
            if (tokens.length != 2) {
                continue;
            }
            if (tokens[0].equals(userId) && tokens[1].equals(password)) {
                user = new User(userId, password);
                break;
            }
        }
        return user;
    }
}
