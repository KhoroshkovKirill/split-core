/**
 * Created by khoroshkovkirill on 29.04.17.
 */
//import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Test {
    @org.junit.Test
    public void splitNumberOne() throws IOException {
        Split splitObj = new Split(true, 0, 0, 4, "TestOne", "inFileTestOne.txt");
        String[] names = splitObj.writeInFiles();
        try {
            if (names.length != 3) {
                fail();
            }
            for (int i = 0; i < names.length; i++) {
                BufferedReader brf = new BufferedReader(new FileReader(names[i]));
                assertEquals(names[i], "TestOne" + (i + 1) + ".txt");
                assertEquals(brf.readLine(), "" + (i + 1));
            }
        }
        finally {
            for (int i = 0; i < names.length; i++) {
                new File(names[i]).delete();
            }
        }
    }

    @org.junit.Test
    public void splitNumberTwo() throws IOException {
        Split splitObj = new Split(false, 0, 0, 0, null, "inFileTestTwo.txt");
        String[] names = splitObj.writeInFiles();
        try {
            if (names.length != 2) {
                fail();
            }
            for (int i = 0; i < names.length; i++) {
                BufferedReader brf = new BufferedReader(new FileReader(names[i]));
                assertEquals("x" + (char)(i + 97) + ".txt", names[i]);
                assertEquals(brf.readLine(), "abcd");
            }
        }
        finally {
            for (int i = 0; i < names.length; i++) {
                new File(names[i]).delete();
            }
        }
    }

    @org.junit.Test
    public void splitNumberThree() throws IOException {
        Split splitObj = new Split(false, 0, 5, 0, "-", "inFileTestThree.txt");
        String[] names = splitObj.writeInFiles();
        try {
            if (names.length != 338) {
                fail();
            }
            int k = 0;
            for (char i = 'a'; (int)i <= (int)'m'; i++) {
                for (char j = 'a'; (int)j <= (int)'z'; j++) {
                    BufferedReader brf = new BufferedReader(new FileReader(names[i]));
                    assertEquals("inFileTestThree" + i + j + ".txt", names[k]);
                    k++;
                    assertEquals("ab" ,brf.readLine());
                    assertEquals("ab" ,brf.readLine());
                    if (brf.readLine() != null){
                        fail();
                    }
                }
            }
        } finally {
            for (int i = 0; i < names.length; i++) {
                new File(names[i]).delete();
            }
        }
    }

    @org.junit.Test
    public void splitIllegalArgumentOne() throws IOException {
        try {
            Split splitObj = new Split(true, 1, 1, 1, "fileThree", "Empty.txt");
            fail();
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
    }

    @org.junit.Test
    public void splitIllegalArgumentTwo() throws IOException {
        try {
            Split splitObj = new Split(true, 23, 0, 45, "fileThree", "Empty.txt");
            fail();
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
    }

    @org.junit.Test
    public void splitIllegalArgumentThree() throws IOException {
        try {
            Split splitObj = new Split(true, 0, 23, 86, "fileThree", "Empty.txt");
            fail();
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
    }

    @org.junit.Test
    public void splitIllegalArgumentFour() throws IOException {
        try {
            Split splitObj = new Split(true, 10, 22, 0, "fileThree", "Empty.txt");
            fail();
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
    }


}
