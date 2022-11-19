import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import sise.sqe.Product;
import sise.sqe.ShoppingList;
import sise.sqe.Supermarket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, -500, -750, -100000}) // six numbers
    public void getDiscount_shouldThrowExceptionWhenNegativePrice(double price ){
        assertThrows(IllegalArgumentException.class, () -> shoppingList.getDiscount(price));

    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 12, 500.0})
    public void getDiscount_shouldGive_0_PercentWhenUnder_500(double price){
        assertEquals(1, shoppingList.getDiscount(price));
    }
    @ParameterizedTest
    @ValueSource(doubles = {500.1, 600.0, 750.0,})
    public void getDiscount_shouldGive_5_PercentWhenRange_500_750(double price){
        assertEquals(0.95, shoppingList.getDiscount(price));
    }

    @ParameterizedTest
    @ValueSource(doubles = {750.1, 800.0, 900.0, 1000.0,})
    public void getDiscount_shouldGive_10_PercentWhenRange_750_1000(double price){
        assertEquals(0.9, shoppingList.getDiscount(price));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.1, 20000.0, Double.MAX_VALUE})
    public void getDiscount_shouldGive_15_PercentWhenAbove_1000(double price){
        assertEquals(0.85, shoppingList.getDiscount(price));
    }

    //------------------------
    // priceWithDelivery
    //------------------------
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
    //------------------------
    // changeQuantity
    //------------------------
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
