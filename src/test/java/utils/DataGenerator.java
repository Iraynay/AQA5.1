package utils;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@UtilityClass
public class DataGenerator {

    @UtilityClass

    public static class RequestForDelivery {
        public static UsersData generateInfo(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return new UsersData(faker.name().firstName(),
                    faker.name().lastName(),
                    faker.phoneNumber().phoneNumber(),
                    faker.address().city()
            );
        }
    }
}


