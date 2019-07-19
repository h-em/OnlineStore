package menu;

import model.Store;
import model.User;
import services.BuyService;
import services.LoginService;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class LoginMenu extends AbstractMenu{

    private Store store;
    private User user;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());

    public LoginMenu(Store store){
        this.store = store;
    }

    public void displayOption() {
        System.out.println(" 1->Login");
        System.out.println(" 2->Display Available Products");
        System.out.println(" 3->Display Shopping Cart");
        System.out.println(" 4->Buy Product");
        System.out.println(" 0->Exit");
    }

    public void executeOption(Integer option) throws IOException {
        LoginService loginService = new LoginService(user);
        BuyService buyService = new BuyService(store,user);
        switch (option) {
            case 1:
                Scanner scanner = new Scanner(System.in);
                System.out.println("User: ");
                String userId = scanner.nextLine();
                System.out.println("Password: ");
                String password = scanner.nextLine();

                User user = loginService.loginUser(userId, password);
                if (user == null) {
                    loger.warning("UserId\\Password incorect!");
                } else {
                    loger.info("Hello, " + userId + "!");
                    BuyMenu buyMenu = new BuyMenu(user,store);
                    while (option != 0) {
                        buyMenu.displayOption();
                        System.out.println("Your option: ");
                        option = scanner.nextInt();
                        try {
                            buyMenu.executeOption(option);
                        } catch (IOException e) {
                            continue;
                        }
                    }
                }
                break;
            case 2:
                buyService.displayAvailableProducts();
                break;
            case 3:
                System.out.println("You have to login first, before start buying products!");
                break;
            case 4:
                System.out.println("You have to login first, before start buying products!");
                break;
            case 0:
                loger.info("Exiting...");
                break;
            default:
                loger.warning("Invalide option!");
                break;
        }
    }

}
