package me.dong.exautovalue;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.ryanharter.auto.value.parcel.ParcelAdapter;

import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dong on 2016-09-14.
 */
@AutoValue
public abstract class Money implements Parcelable {
    public abstract Currency currency();

    public abstract long amount();

    public abstract Locale locale();

    @ParcelAdapter(DateTypeAdapter.class)
    public abstract Date date();

    public static Money create(Locale locale, long amount) {
        return new AutoValue_Money(Currency.getInstance(locale), amount, locale, new Date());
    }

    public String displayString() {
        return currency().getSymbol() + amount();
    }

}
