package menu;

import model.User;
import services.BuyService;
import services.LoginService;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.logging.Logger;

public class BuyMenu extends AbstractMenu {
    private User user;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());


    public BuyMenu(User user) {
        this.user = user;
    }

    public void displayOption() {
        System.out.println(" 1->Display Available Products");
        System.out.println(" 2->Display Shopping Cart");
        System.out.println(" 3->Buy Product");
        System.out.println(" 0->Logout");
    }

    public void executeOption(Integer option) throws IOException {
        LoginService loginService = new LoginService();
        BuyService buyService = new BuyService();
        switch (option) {
            case 1:
                loginService.displayAvailableProducts();
                break;
            case 2:
                buyService.displayShopingCart();
                break;
            case 3:
                loginService.buyProduct();
                break;
            case 0:
                loger.info("User " + user.getName() + " is successfully logged out!\n");
                break;
            default:
                loger.warning("Invalide option!");
                break;
        }
    }
}
