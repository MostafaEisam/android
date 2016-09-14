package me.dong.exautovalue;

import org.junit.Test;

import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class MoneyUnitTest {

    @Test
    public void money_create() throws Exception {
        Money money = Money.create(Locale.KOREA, 1200L);

        assertTrue(Money.create(Locale.KOREA, 1200L).equals(money));
        assertEquals(1200L, money.amount());
        assertEquals(Currency.getInstance(Locale.KOREA), money.currency());
        assertEquals(Locale.KOREA, money.locale());
    }

    @Test
    public void money_displayStringt() throws Exception {
        Money money = Money.create(Locale.KOREA, 1200L);
        assertEquals(Currency.getInstance(Locale.KOREA).getSymbol() + "1200", money.displayString());
    }
}