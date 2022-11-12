import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sise.sqe.Product;
import sise.sqe.ShoppingList;
import sise.sqe.Supermarket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ShoppingListTest {
    static Supermarket supermarketMock = null;
    static ShoppingList shoppingList;
    static ShoppingList shoppingListSpy;

    @BeforeAll
    public static void init(){
        supermarketMock = Mockito.mock(Supermarket.class);
        shoppingList = new ShoppingList(supermarketMock);
        shoppingListSpy = spy(shoppingList);
    }

    //------------------------
    // addProduct
    //------------------------

//    @Test
//    public void add_product_success(){
//        String productID = "123";
//        when(supermarketMock.getPrice(productID)).thenReturn(5.0);
//        double priceBeforeAddingProduct = shoppingList.getMarketPrice();
//        shoppingList.addProduct(new Product("123", "bamba", 1));
//        double priceAfterAddingProduct = shoppingList.getMarketPrice();
//        assertEquals(priceBeforeAddingProduct + 5, priceAfterAddingProduct);
//    }

    //------------------------
    // getMarketPrice
    //------------------------

    //------------------------
    // getDiscount
    //------------------------

    //------------------------
    // priceWithDelivery
    //------------------------

    //------------------------
    // changeQuantity
    //------------------------
}
