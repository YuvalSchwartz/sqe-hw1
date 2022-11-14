import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import sise.sqe.Product;
import sise.sqe.ShoppingList;
import sise.sqe.Supermarket;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ShoppingListTest {
//    static Supermarket supermarketMock = null;
//    static ShoppingList shoppingList;
//    static ShoppingList shoppingListSpy;
//    static Product bamba;

//    @BeforeAll
//    public static void init(){
//        supermarketMock = Mockito.mock(Supermarket.class);
//        shoppingList = new ShoppingList(supermarketMock);
//        shoppingListSpy = spy(shoppingList);
//        bamba = new Product("123", "Bamba", 1);
//    }

    //------------------------
    // Constructor
    //------------------------

    @Test
    public void testCreateShoppingListObject() {
        Supermarket supermarketMock = Mockito.mock(Supermarket.class);
        ShoppingList shoppingList = new ShoppingList(supermarketMock);
        assertNotNull(shoppingList);
    }

    //------------------------
    // addProduct
    //------------------------

    @ParameterizedTest
    @MethodSource("getMarketPriceParams")
    public void testAllCombinationsOfMarketPrices(Product[] productsArray, double[] productsPrices, double discount, double expected) {
        Supermarket supermarketMock = Mockito.mock(Supermarket.class);
        ShoppingList shoppingList = new ShoppingList(supermarketMock);
        ShoppingList shoppingListSpy = spy(shoppingList);
        if(productsArray != null) {
            for(int i = 0; i < productsArray.length; i++) {
                shoppingListSpy.addProduct(productsArray[i]);
                when(supermarketMock.getPrice(productsArray[i].getId())).thenReturn(productsPrices[i]);
            }
        }
        when(shoppingListSpy.getDiscount(anyDouble())).thenReturn(discount);

        assertEquals(expected, shoppingListSpy.getMarketPrice());
    }

    private static Stream<Arguments> getMarketPriceParams() {
        Product p1 = new Product("p1", "p1", 5);
        Product p2 = new Product("p2", "p2", 0);
        Product p3 = new Product("p3", "p3", 0);
        Product p4 = new Product("p4", "p4", 0);
        Product p5 = new Product("p5", "p5", 0);
        Product p6 = new Product("p6", "p6", 0);
        return Stream.of(
                Arguments.of(null, null, 1.0, 0.0),
                Arguments.of(null, null, 0.95, 0.0),
                Arguments.of(null, null, 0.9, 0.0),
                Arguments.of(null, null, 0.85, 0.0),
                Arguments.of(new Product[]{p1}, new double[]{0.0}, 1.0, 0.0),
                Arguments.of(new Product[]{p1}, new double[]{0.0}, 0.95, 0.0),
                Arguments.of(new Product[]{p1}, new double[]{0.0}, 0.9, 0.0),
                Arguments.of(new Product[]{p1}, new double[]{0.0}, 0.85, 0.0)
        );
    }

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
