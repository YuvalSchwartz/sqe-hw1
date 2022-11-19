import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import sise.sqe.Product;
import sise.sqe.ShoppingList;
import sise.sqe.Supermarket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.*;

public class ShoppingListTest {

    /*
        Method Naming Convention - When writing test methods, there are multiple approaches:

        should[action]
        Example: mailShouldBeSent or cartShouldGetCleared

        should[consequence]when[action]
        Example: shouldBanWhenEULAIsBroken

        Given[input]When[action]Then[consequence]
        Given_UserIsLoggedIn_When_SessionIsExpired_Then_LogoutUser

     */

    static Supermarket supermarketMock = null;
    static ShoppingList shoppingList;
    static ShoppingList shoppingListSpy;

    @BeforeEach
    public void init(){
        supermarketMock = Mockito.mock(Supermarket.class);
        shoppingList = new ShoppingList(supermarketMock);
        shoppingListSpy = spy(shoppingList);
    }

    //------------------------
    // Constructor tests
    //------------------------

    @Test
    public void testCreateShoppingListObject() {
        Supermarket supermarketMock = Mockito.mock(Supermarket.class);
        ShoppingList shoppingList = new ShoppingList(supermarketMock);
        assertNotNull(shoppingList);
    }

    //------------------------
    // addProduct tests
    //------------------------

    @ParameterizedTest
    @MethodSource("addProductParams")
    public void testAddProductWithCombinationsOfProductsAndLists(List<Product> productsListBeforeAdding, Product[] productsToAdd, int expectedResult) {
        Supermarket supermarketMock = Mockito.mock(Supermarket.class);
        ShoppingList shoppingList = new ShoppingList(supermarketMock);
        List<Product> spyProductsList = spy(new ArrayList<Product>(productsListBeforeAdding));
        Whitebox.setInternalState(shoppingList, "products", spyProductsList);
        for (Product product : productsToAdd)
            shoppingList.addProduct(product);

        verify(spyProductsList, times(expectedResult)).add(any(Product.class));
    }

    private static Stream<Arguments> addProductParams() {
        Product noQuantityProduct1 = new Product("p1", "p1", 0);
        Product noQuantityProduct2 = new Product("p2", "p2", 0);
        Product noQuantityProduct3 = new Product("p3", "p3", 0);
        Product noQuantityProduct4 = new Product("p4", "p4", 0);
        Product noQuantityProduct5 = new Product("p5", "p5", 0);
        Product noQuantityProduct6 = new Product("p6", "p6", 0);
        Product positiveQuantityProduct1 = new Product("p7", "p7", 1);
        Product positiveQuantityProduct2 = new Product("p8", "p8", 9);
        Product positiveQuantityProduct3 = new Product("p9", "p9", 14);
        Product positiveQuantityProduct4 = new Product("p10", "p10", 2);
        Product positiveQuantityProduct5 = new Product("p11", "p11", 2);
        Product positiveQuantityProduct6 = new Product("p12", "p12", 4);

        List<Product> zeroQuantityProductsList = new ArrayList<>();
        zeroQuantityProductsList.add(noQuantityProduct1);
        zeroQuantityProductsList.add(noQuantityProduct2);
        zeroQuantityProductsList.add(noQuantityProduct3);

        List<Product> positiveQuantityProductsList = new ArrayList<>();
        positiveQuantityProductsList.add(positiveQuantityProduct1);
        positiveQuantityProductsList.add(positiveQuantityProduct2);
        positiveQuantityProductsList.add(positiveQuantityProduct3);

        List<Product> mixedQuantityProductsList = new ArrayList<>();
        mixedQuantityProductsList.add(positiveQuantityProduct1);
        mixedQuantityProductsList.add(noQuantityProduct1);
        mixedQuantityProductsList.add(noQuantityProduct2);
        mixedQuantityProductsList.add(positiveQuantityProduct2);

        return Stream.of(
                //adding zero quantity products to empty list:
                Arguments.of(new ArrayList<>(), new Product[]{noQuantityProduct1, noQuantityProduct2, noQuantityProduct3}, 3),
                //adding positive quantity products to empty list:
                Arguments.of(new ArrayList<>(), new Product[]{positiveQuantityProduct1, positiveQuantityProduct2, positiveQuantityProduct3, positiveQuantityProduct4}, 4),
                //adding mixed quantity products to empty list:
                Arguments.of(new ArrayList<>(), new Product[]{positiveQuantityProduct1, noQuantityProduct1}, 2),
                //adding zero quantity products to non-empty list with zero quantity products:
                Arguments.of(zeroQuantityProductsList, new Product[]{noQuantityProduct4, noQuantityProduct5, noQuantityProduct6}, 3),
                //adding positive quantity products to non-empty list with zero quantity products:
                Arguments.of(zeroQuantityProductsList, new Product[]{positiveQuantityProduct1, positiveQuantityProduct2}, 2),
                //adding mixed quantity products to non-empty list with zero quantity products:
                Arguments.of(zeroQuantityProductsList, new Product[]{positiveQuantityProduct1, positiveQuantityProduct2, noQuantityProduct4, noQuantityProduct5}, 4),
                //adding zero quantity products to non-empty list with positive quantity products:
                Arguments.of(positiveQuantityProductsList, new Product[]{noQuantityProduct1}, 1),
                //adding positive quantity products to non-empty list with positive quantity products:
                Arguments.of(positiveQuantityProductsList, new Product[]{positiveQuantityProduct4, positiveQuantityProduct5}, 2),
                //adding mixed quantity products to non-empty list with positive quantity products:
                Arguments.of(positiveQuantityProductsList, new Product[]{positiveQuantityProduct3, noQuantityProduct3, noQuantityProduct4}, 3),
                //adding zero quantity products to non-empty list with mixed quantity products:
                Arguments.of(mixedQuantityProductsList, new Product[]{noQuantityProduct1}, 1),
                //adding positive quantity products to non-empty list with mixed quantity products:
                Arguments.of(mixedQuantityProductsList, new Product[]{positiveQuantityProduct3, positiveQuantityProduct4, positiveQuantityProduct5, positiveQuantityProduct6}, 4),
                //adding mixed quantity products to non-empty list with mixed quantity products:
                Arguments.of(mixedQuantityProductsList, new Product[]{positiveQuantityProduct3, positiveQuantityProduct4, noQuantityProduct3}, 3)
        );
    }

    //------------------------
    // getMarketPrice tests
    
    


    @ParameterizedTest
    @MethodSource("getMarketPriceParams")
    public void testAllCombinationsOfMarketPrices(Product[] productsArray, double[] productsPrices, double discount, double expectedResult) {
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

        assertEquals(expectedResult, shoppingListSpy.getMarketPrice());
    }

    private static Stream<Arguments> getMarketPriceParams() {
        Product noQuantityProduct1 = new Product("p1", "p1", 0);
        Product noQuantityProduct2 = new Product("p2", "p2", 0);
        Product noQuantityProduct3 = new Product("p3", "p3", 0);
        Product positiveQuantityProduct1 = new Product("p4", "p4", 4);
        Product positiveQuantityProduct2 = new Product("p5", "p5", 5);
        Product positiveQuantityProduct3 = new Product("p6", "p6", 6);
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
                Arguments.of(new Product[]{positiveQuantityProduct1, positiveQuantityProduct2, positiveQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 1.0, 0.0),
                Arguments.of(new Product[]{positiveQuantityProduct1, positiveQuantityProduct2, positiveQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 0.95, 0.0),
                Arguments.of(new Product[]{positiveQuantityProduct1, positiveQuantityProduct2, positiveQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 0.9, 0.0),
                Arguments.of(new Product[]{positiveQuantityProduct1, positiveQuantityProduct2, positiveQuantityProduct3}, new double[]{0.0, 0.0, 0.0}, 0.85, 0.0),
                //shopping lists made of several items with mixed features:
                Arguments.of(new Product[]{positiveQuantityProduct1, noQuantityProduct1, positiveQuantityProduct2, noQuantityProduct2}, new double[]{101.99, 98.5, 500.6, 333.2}, 0.85, 2474.316),
                Arguments.of(new Product[]{noQuantityProduct1, noQuantityProduct2, positiveQuantityProduct1}, new double[]{15.3, 33.6, 200.0}, 0.9, 720.0),
                Arguments.of(new Product[]{positiveQuantityProduct1, positiveQuantityProduct2, positiveQuantityProduct3}, new double[]{120.3, 207.0, 111.1}, 0.85, 1855.38),
                Arguments.of(new Product[]{positiveQuantityProduct1, positiveQuantityProduct2, positiveQuantityProduct3, noQuantityProduct1}, new double[]{1.0, 2.2, 5.3, 66.0}, 1.0, 46.8),
                Arguments.of(new Product[]{positiveQuantityProduct1, positiveQuantityProduct2, positiveQuantityProduct3}, new double[]{30.5, 20.6, 46.7}, 0.95, 479.94),
                Arguments.of(new Product[]{noQuantityProduct1, positiveQuantityProduct1, positiveQuantityProduct2, positiveQuantityProduct3, noQuantityProduct2}, new double[]{5.4, 90.0, 50.3, 48.1, 22.0}, 0.9, 810.09)
        );
    }

    //------------------------
    // getDiscount tests
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

    // priceWithDelivery tests
    @ParameterizedTest
    @ValueSource(ints = {-1, -500, -750, -100000})
    public void priceWithDelivery_shouldThrowExceptionWhenNegativeInput(int miles ){
        assertThrows(IllegalArgumentException.class, () -> shoppingList.priceWithDelivery(miles));

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, Integer.MAX_VALUE})
    public void priceWithDelivery_shouldReturn_0_WhenNoProducts(int miles){
        when(supermarketMock.calcDeliveryFee(miles, 0)).thenReturn(0.0);
        assertEquals(0,shoppingList.priceWithDelivery(miles));
    }
    @Test
    public void priceWithDelivery_shouldReturnPriceWhenMiles_0(){
        int miles = 0;
        Product p1 = new Product("12","Bamba",1);
        Product p2 = new Product("23","Bisli",1);
        shoppingList.addProduct(p1);
        shoppingList.addProduct(p2);

        when(supermarketMock.getPrice("12")).thenReturn(5.0);
        when(supermarketMock.getPrice("23")).thenReturn(6.0);
        when(supermarketMock.calcDeliveryFee(0, 2)).thenReturn(2.0 * miles);

        assertEquals(11, shoppingList.priceWithDelivery(miles));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15})
    public void priceWithDelivery_shouldReturnPricePlusFeeWhenMiles_0(int miles){

        Product p1 = new Product("12","Bamba",1);
        Product p2 = new Product("23","Bisli",1);
        shoppingList.addProduct(p1);
        shoppingList.addProduct(p2);

        when(supermarketMock.getPrice("12")).thenReturn(5.0);
        when(supermarketMock.getPrice("23")).thenReturn(6.0);
        when(supermarketMock.calcDeliveryFee(miles, 2)).thenReturn(2.0 * miles);

        assertEquals(11 + (2 * miles), shoppingList.priceWithDelivery(miles));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, Integer.MAX_VALUE})
    public void priceWithDelivery_shouldReturnFeeWhenNoProducts(int miles){
        when(supermarketMock.calcDeliveryFee(miles, 0)).thenReturn(miles * 1.0) ;
        assertEquals(miles,shoppingList.priceWithDelivery(miles));
    }
    // changeQuantity tests
    //------------------------

    //TODO: implement
    @ParameterizedTest
    @ValueSource(ints = {-1, -500, -750, -100000})
    public void changeQuantity_shouldThrowExceptionWhenNegativeInput(int quantity ){
        Product p1 = new Product("12","Bamba",1);
        Product p2 = new Product("23","Bisli",1);
        shoppingList.addProduct(p1);
        shoppingList.addProduct(p2);

        assertThrows(IllegalArgumentException.class, () -> shoppingList.changeQuantity(quantity, "12"));

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 100000})
    public void changeQuantity_shouldNotChangeWhenNoProduct(int quantity ){
        Product p1 = new Product("12","Bamba",1);
        Product p2 = new Product("23","Bisli",1);
        shoppingList.addProduct(p1);
        shoppingList.addProduct(p2);

        assertThrows(IllegalArgumentException.class, () -> shoppingList.changeQuantity(quantity, "45"));

    }

}
