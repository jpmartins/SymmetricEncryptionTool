package pt.isel.cc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * @author jpmartins
 * 
 * Tipo que utiliza JCA para fazer a cifra, decifra e geração de chaves DESede para (e de) ficheiro.
 */
public class DESede {
	/**
	 * @param fileKeys - nome do ficheiro para onde será gerada a chave. 
	 */
	public void generateKey(String fileKeys) {
		System.out.println("generatingKey");
		try (FileOutputStream fos= new FileOutputStream(fileKeys) ){ // Java 7 rules.	
			SecretKey key = KeyGenerator.getInstance("DESede").generateKey();
			// Gravar a chave no ficheiro.		
			fos.write(key.getEncoded());
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-2);
		}
	}
	
	/**
	 * @param fileIn - nome do ficheiro de onde será lida a mensagem a CIFRAR;
	 * @param fileKeys - nome do ficheiro que contem a chave;
	 * @param fileOut - nome do ficheiro para onde será escrito o conteudo do ficheiro fileIn CIFRADO.
	 */
	public void cifrar(String fileIn,String fileKeys,String fileOut) {	
		System.out.println("encrypt");
		try (FileOutputStream fos= new FileOutputStream(fileOut) ){ // Java 7 rules.	
			// recuperar a chave do ficheiro
			SecretKey key = getSecretKey(fileKeys);
			// iniciar o "cifrador" e uma stream que permite cifrar os dados para o ficheiro
			Cipher cip1 = Cipher.getInstance("DESede/ECB/NoPadding");
			CipherOutputStream c_out = new CipherOutputStream(fos,cip1); // cifrado
			cip1.init(Cipher.ENCRYPT_MODE,key);
			// efectuar a cifra - Java 7 rules.
			Files.copy(java.nio.file.Paths.get(fileIn), c_out);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(-3);
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			System.exit(-4);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			System.exit(-5);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-15);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			System.exit(-18);
		}
	}
	/**
	 * @param fileIn - nome do ficheiro de onde será lida a mensagem a CIFRADA;
	 * @param fileKeys - nome do ficheiro que contem a chave;
	 * @param fileOut - nome do ficheiro para onde será escrito o conteudo do ficheiro fileIn DECIFRADO.
	 */
	public void decifra(String fileIn,String fileKeys,String fileOut) {
		System.out.println("decrypt");
		try {
			// recuperar a chave do ficheiro
			SecretKey key = getSecretKey(fileKeys);
			// iniciar o "decifrador"
			Cipher cip2 = Cipher.getInstance("DESede/ECB/NoPadding");
			cip2.init(Cipher.DECRYPT_MODE,key);
			CipherInputStream in = new CipherInputStream(new FileInputStream(fileIn),cip2); // le do ficheiro cifrado e decifra
			// decifrar ficheiro, e grava o texto em claro no ficheiro fileOut
			new File(fileOut).delete();
			//Java 7 rules.
			Files.copy(in,java.nio.file.Paths.get(fileOut));
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			System.exit(-6);
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
			System.exit(-7);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			System.exit(-9);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-16);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			System.exit(-17);
		}
	}		

	private SecretKey getSecretKey(String fileKeys) throws FileNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		try (FileInputStream fis = new FileInputStream(fileKeys)){
			int n = fis.available();
	        byte[] k = new byte[n];
	        fis.read(k);
	        fis.close();
	        KeySpec ks = new DESedeKeySpec(k);
	        SecretKeyFactory kf  = SecretKeyFactory.getInstance("DESede");
	        return kf.generateSecret(ks);
		}
	}
	@SuppressWarnings("unused")
	private void copiarStream(InputStream in, OutputStream c_out) {
		// (*) Cifrar os dados do ficheiro input para o ficheiro output.
		byte[] b = new byte[64];
		try {
			while(in.read(b) > 0) // (*) lê dados do ficheiro
			{				 
				c_out.write(b); // (*) faz update(b) cifrado parte dos dados lidos usando o Cipher, e escrevendo-os no ficheiro fileOut
			}
			c_out.close(); // (*) faz dofinal() no Cipher
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-13);
		}
	}
}