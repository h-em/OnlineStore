import menu.LoginMenu;
import model.Store;

public class Main {
    public static void main(String[] args) {

        Store store = new Store("H-em Store", "bd. Mihai eminescu, 6");
        store.buildProductList();       // populate the Map of products for the store
        // this is similar with the 'buildAccounts'
        // functionality from the course
        LoginMenu menu = new LoginMenu(store);
        menu.displayMenu();
    }
}
