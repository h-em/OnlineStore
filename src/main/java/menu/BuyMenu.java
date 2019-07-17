package menu;

import model.Store;
import model.User;
import services.BuyService;

import java.io.IOException;
import java.util.logging.Logger;

public class BuyMenu extends AbstractMenu {
    private User user;
    private Store store;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());


    public BuyMenu(User user,Store store) {
        this.user = user;
        this.store = store;
    }

    public void displayOption() {
        System.out.println(" 1->Display Available Products");
        System.out.println(" 2->Display Shopping Cart");
        System.out.println(" 3->Buy Product");
        System.out.println(" 0->Logout");
    }

    public void executeOption(Integer option)  throws IOException{
        BuyService buyService = new BuyService(store,user);
        switch (option) {
            case 1:
                buyService.displayAvailableProducts();
                break;
            case 2:
                buyService.displayShopingCart();
                break;
            case 3:
                buyService.buyProduct();
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
