# junit-testcases
Intuitive parameterised unit tests for Junit4

Junit4 has the ability to parameter your tests, but its at the class level, which makes it very annoying to structure your tests in a normal way.

### Enter `@TestCase`

Simply add the `@TestCase` attribute to your test method that you want to parameterize:

```java
import static org.junit.Assert.assertEquals;

import org.junit.runner.RunWith;

import com.reborn.junit.testcases.JUnit4WithTestCases;
import com.reborn.junit.testcases.TestCase;

@RunWith(JUnit4WithTestCases.class)
public class NumberTests {
    @Test
    @TestCase({"1", "1", "2"})
    @TestCase({"1", "2", "3"})
    @TestCase({"10", "10", "20"})
    public void test_addNumbers(int n1, int n2, int expected) {
        int result = n1 + n2;

        assertEquals(expected, result);
    }
}
```
Don't forget to also add the custom runner to the test class! (`@RunWith(JUnit4WithTestCases.class)`)

Notice that you still need the `@Test` attribute so the runner can find the test as normal.

#### Conversions
Most basic conversions from `String` parameters to primitive types are handled as expected but it also converted some Date types such as:
- `java.util.Date`
- `java.time.LocalDateTime`
- `java.time.LocalDate`
- `java.time.LocalTime`
- `java.time.Instant`

All conversions follow the ISO format (yyyy-MM-dd'T'HH:mm:ss)
