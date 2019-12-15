package pt;


import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import pt.DESede;

public class TestAll {

	@Test
	public void testKeyGen() {
		System.out.println("testKeyGen");
		try {
			if(new File("tempKeyJuintTest").delete()) System.out.println("previous tempKeyJuintTest deleted");
			new DESede().generateKey("tempKeyJuintTest");
			assertTrue(new File("tempKeyJuintTest").exists());
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			fail("Error");
		}
	}
	
	@Test
	public void testEncDec() {
		System.out.println("testEncDec");
		DESede impl = new DESede();
		String fileKeys = "tempKeyJuintTest";
		String fileIn = fileKeys;
		String fileEncOut = fileIn+"Enc";
		String fileDecOut = fileIn+"Dec";
		try {
			impl.generateKey(fileKeys);
		} catch (NoSuchAlgorithmException | IOException e) {
		}
		try{
			impl.cifrar(fileIn, fileKeys, fileEncOut);
			assertTrue(new File(fileEncOut).exists());
		}catch (Exception e) {
			e.printStackTrace();
			fail("Error");
		}
		try{
			impl.decifra(fileEncOut, fileKeys, fileDecOut);
			assertTrue(new File(fileDecOut).exists());
		}catch (Exception e) {
			e.printStackTrace();
			fail("Error");
		}
		try {
			byte[] f1 = Files.readAllBytes(FileSystems.getDefault().getPath(fileKeys));
			byte[] f2 = Files.readAllBytes(FileSystems.getDefault().getPath(fileDecOut));
			assertTrue(Arrays.equals(f1, f2));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Error");
		}
	}
}
