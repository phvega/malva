package malva.java.text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import malva.TestCase;

public class SimpleDateFormatTest extends TestCase {
  public static void testParse() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss EEE", Locale.US);
    assertEquals(new Date(1288166400000L), sdf.parse("2010/10/27 10:00:00 Wed"));
  }

  public static void main(String[] args) throws Exception {
//    testParse();
  }
}