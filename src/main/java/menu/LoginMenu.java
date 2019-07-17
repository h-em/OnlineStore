package menu;

import model.Store;
import model.User;
import services.LoginService;

import java.io.IOException;
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

    protected void executeOption(Integer option) throws IOException {

        LoginService loginService = new LoginService(user);
        switch (option) {
            case 1:
                loginService.login(option);
                break;
            case 2:
                loginService.displayAvailableProducts();
                break;
            case 3:
                loginService.displayShopingCart();
                break;
            case 4:
                loginService.buyProduct();
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
