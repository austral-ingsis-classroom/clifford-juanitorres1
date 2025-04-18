package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.austral.ingsis.clifford.Filter;
import org.junit.jupiter.api.Test;

public class FilterTest {

  @Test
  void testValuesOrder() {
    Filter[] filters = Filter.values();
    assertArrayEquals(new Filter[] {Filter.ASC, Filter.DESC}, filters, "");
  }

  @Test
  void testValueOfValid() {
    assertEquals(Filter.ASC, Filter.valueOf("ASC"), "");
    assertEquals(Filter.DESC, Filter.valueOf("DESC"), "");
  }
}
