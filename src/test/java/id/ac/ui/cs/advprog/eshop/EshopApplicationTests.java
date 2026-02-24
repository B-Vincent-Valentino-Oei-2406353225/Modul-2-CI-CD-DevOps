package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EshopApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainRunsWithoutException() {
        assertDoesNotThrow(() ->
                EshopApplication.main(new String[]{"--spring.main.web-application-type=none"}));
    }
}
