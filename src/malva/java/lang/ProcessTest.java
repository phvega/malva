package malva.java.lang;

import java.io.IOException;

import malva.TestCase;

public class ProcessTest extends TestCase {
  public static void testDestroy() throws Exception {
    final Process process = Runtime.getRuntime().exec("true");
    Thread waiter = new Thread(new Runnable() {
      @Override public void run() {
        process.destroy();
      }
    });

    waiter.start();
    waiter.join(100);
    if (waiter.isAlive())
      fail("Took too long to destroy process");
  }

  public static void testExitValue() {

    try {
      ProcessBuilder processBuilder = new ProcessBuilder("env");
      Process process = processBuilder.start();
      // Read all output after which the process will complete
      while (process.getInputStream().read() != -1);
      process.waitFor();
      assertEquals(0, process.exitValue());
    } catch (Exception e) {
      fail("Test failed: " + e.getMessage());
    }

    try {
      ProcessBuilder processBuilder = new ProcessBuilder("sleep", "10");
      final Process process = processBuilder.start();

      assertThrows(new Block() {
        @Override public void run() {
          process.exitValue();
        }
      }, IllegalThreadStateException.class);

      process.destroy();
      process.waitFor();
    } catch (Exception e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  public static void testGetErrorStream() {
    ProcessBuilder processBuilder = new ProcessBuilder("echo", "test");
    try {
      Process process = processBuilder.start();
      assertNotNull(process.getErrorStream());
      process.destroy();
    } catch (IOException e) {
      fail("Test failed: "  + e.getMessage());
    }
  }

  public static void testGetInputStream() {
    ProcessBuilder processBuilder = new ProcessBuilder("echo", "test");
    try {
      Process process = processBuilder.start();
      assertNotNull(process.getInputStream());
      process.destroy();
    } catch (IOException e) {
      fail("Test failed: "  + e.getMessage());
    }
  }

  public static void testGetOutputStream(){
    ProcessBuilder processBuilder = new ProcessBuilder("echo", "test");
    try {
      Process process = processBuilder.start();
      assertNotNull(process.getOutputStream());
      process.destroy();
    } catch (IOException e) {
      fail("Test failed: "  + e.getMessage());
    }
  }

  public static void testWaitFor() throws Exception {
    final Process process = Runtime.getRuntime().exec("true");
    Thread waiter = new Thread(new Runnable() {
      @Override public void run() {
        try {
          process.waitFor();
        } catch (Exception ignored) {
          /* Main thread will detected that we died */
        }
      }
    });

    waiter.start();
    waiter.join(100);
    if (waiter.isAlive())
      fail("Started process did not complete in time");
  }

  public static void testInterrupted()
  {
    try {
      // Check interrupted status is not cleared when process is started
      Thread.currentThread().interrupt();
      Process process = Runtime.getRuntime().exec("sleep 5");
      assertTrue(Thread.interrupted());

      // Check interrupted status is not cleared when destroy is called
      assertFalse(Thread.interrupted());
      Thread.currentThread().interrupt();
      process.destroy();
      assertTrue(Thread.interrupted());
    } catch (IOException e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  public static void main(String[] args) throws Exception {
    testDestroy();
//    testExitValue();
    testGetErrorStream();
    testGetInputStream();
    testGetOutputStream();
    testInterrupted();
    testWaitFor();
  }
}
