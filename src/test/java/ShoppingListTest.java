import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import sise.sqe.Product;
import sise.sqe.ShoppingList;
import sise.sqe.Supermarket;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
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

//    @Spy
//    List<Product> products;
//
//    @InjectMocks
//    ShoppingList shoppingList;
//
//    @Test
//    public void testAddProduct() {
//        Supermarket supermarketMock = Mockito.mock(Supermarket.class);
//        ShoppingList shoppingList = new ShoppingList(supermarketMock);
//        Product p = new Product("p1", "p1", 5);
//
//        List<Product> expectedProducts = Arrays.asList(p);
//        List<Product> actualProducts = products;
//        assertEquals(expectedProducts, actualProducts);
//    }

    //------------------------
    // getMarketPrice
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
        Product noQuantityProduct1 = new Product("p1", "p1", 0);
        Product noQuantityProduct2 = new Product("p2", "p2", 0);
        Product noQuantityProduct3 = new Product("p3", "p3", 0);
        Product regularQuantityProduct1 = new Product("p4", "p4", 4);
        Product regularQuantityProduct2 = new Product("p5", "p5", 5);
        Product regularQuantityProduct3 = new Product("p6", "p6", 6);
        return Stream.of(
                //empty lists under all discount options:
                Arguments.of(null, null, 1.0, 0.0),
                Arguments.of(null, null, 0.95, 0.0),
                Arguments.of(null, null, 0.9, 0.0),
                Arguments.of(null, null, 0.85, 0.0),
                //shopping lists made of several items (all of them with zero price and zero quantity) under all discount options:
                Arguments.of(new Product[]{noQuantityProduct1, noQuantityProduct2, noQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 1.0, 0.0),
                Arguments.of(new Product[]{noQuantityProduct1, noQuantityProduct2, noQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 0.95, 0.0),
                Arguments.of(new Product[]{noQuantityProduct1, noQuantityProduct2, noQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 0.9, 0.0),
                Arguments.of(new Product[]{noQuantityProduct1, noQuantityProduct2, noQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 0.85, 0.0),
                //shopping lists made of several items (all of them with certain price but zero quantity) under all discount options:
                Arguments.of(new Product[]{noQuantityProduct1, noQuantityProduct2, noQuantityProduct3}, new double[]{4.0, 6.3, 11.7}, 1.0, 0.0),
                Arguments.of(new Product[]{noQuantityProduct1, noQuantityProduct2, noQuantityProduct3}, new double[]{2.5, 7.3, 9.0}, 0.95, 0.0),
                Arguments.of(new Product[]{noQuantityProduct1, noQuantityProduct2, noQuantityProduct3}, new double[]{8.9, 1.1, 1.0}, 0.9, 0.0),
                Arguments.of(new Product[]{noQuantityProduct1, noQuantityProduct2, noQuantityProduct3}, new double[]{10.10, 4.4, 1.99}, 0.85, 0.0),
                //shopping lists made of several items (all of them with certain quantity but zero price) under all discount options:
                Arguments.of(new Product[]{regularQuantityProduct1, regularQuantityProduct2, regularQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 1.0, 0.0),
                Arguments.of(new Product[]{regularQuantityProduct1, regularQuantityProduct2, regularQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 0.95, 0.0),
                Arguments.of(new Product[]{regularQuantityProduct1, regularQuantityProduct2, regularQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 0.9, 0.0),
                Arguments.of(new Product[]{regularQuantityProduct1, regularQuantityProduct2, regularQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 0.85, 0.0),
                //shopping lists made of several items with mixed features:
                Arguments.of(new Product[]{regularQuantityProduct1, noQuantityProduct1, regularQuantityProduct2, noQuantityProduct2}, new double[]{101.99, 98.5, 500.6, 333.2}, 0.85, 2474.316),
                Arguments.of(new Product[]{noQuantityProduct1, noQuantityProduct2, regularQuantityProduct1}, new double[]{15.3, 33.6, 200.0}, 0.9, 720.0),
                Arguments.of(new Product[]{regularQuantityProduct1, regularQuantityProduct2, regularQuantityProduct3}, new double[]{120.3, 207.0, 111.1}, 0.85, 1855.38),
                Arguments.of(new Product[]{regularQuantityProduct1, regularQuantityProduct2, regularQuantityProduct3, noQuantityProduct1}, new double[]{1.0, 2.2, 5.3, 66.0}, 1.0, 46.8),
                Arguments.of(new Product[]{regularQuantityProduct1, regularQuantityProduct2, regularQuantityProduct3}, new double[]{30.5, 20.6, 46.7}, 0.95, 479.94),
                Arguments.of(new Product[]{noQuantityProduct1, regularQuantityProduct1, regularQuantityProduct2, regularQuantityProduct3, noQuantityProduct2}, new double[]{5.4, 90.0, 50.3, 48.1, 22.0}, 0.9, 810.09)
        );
    }

    //------------------------
    // getDiscount
    //------------------------

    @Test
    public void testGetDiscountWithNegativePrice() {
        Supermarket supermarketMock = Mockito.mock(Supermarket.class);
        ShoppingList shoppingList = new ShoppingList(supermarketMock);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {shoppingList.getDiscount(-1);});

        assertEquals("Price cannot be negative", thrown.getMessage());
    }

    @Test
    public void testGetDiscountForFifteenPercent() {
        Supermarket supermarketMock = Mockito.mock(Supermarket.class);
        ShoppingList shoppingList = new ShoppingList(supermarketMock);

        assertEquals(0.85, shoppingList.getDiscount(1265.33));
    }

    @Test
    public void testGetDiscountForTenPercent() {
        Supermarket supermarketMock = Mockito.mock(Supermarket.class);
        ShoppingList shoppingList = new ShoppingList(supermarketMock);

        assertEquals(0.9, shoppingList.getDiscount(848.56));
    }

    @Test
    public void testGetDiscountForFivePercent() {
        Supermarket supermarketMock = Mockito.mock(Supermarket.class);
        ShoppingList shoppingList = new ShoppingList(supermarketMock);

        assertEquals(0.95, shoppingList.getDiscount(555.55));
    }

    @Test
    public void testGetDiscountForZeroPercent() {
        Supermarket supermarketMock = Mockito.mock(Supermarket.class);
        ShoppingList shoppingList = new ShoppingList(supermarketMock);

        assertEquals(1.0, shoppingList.getDiscount(1.1));
    }

    //------------------------
    // priceWithDelivery
    //------------------------

    //TODO: implement

    //------------------------
    // changeQuantity
    //------------------------

    //TODO: implement
}
