package learn.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ContextConfiguration
class BlogApplicationTests {

	@Test
	void contextLoads() {
		int sum = 2 + 2;
        assertEquals(4, sum, "Sum should be 4");
	}

}
