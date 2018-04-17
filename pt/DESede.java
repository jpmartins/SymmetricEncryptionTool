package pt;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;

/**
 * @author jpmartins Tipo que utiliza JCA para fazer a cifra, decifra e geração
 *         de chaves DESede para (e de) ficheiro.
 */
public class DESede {
	/**
	 * @param fileKeys
	 *            - nome do ficheiro para onde será gerada a chave.
	 */
	public void generateKey(String fileKeys) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		System.out.println("generatingKey");
		try (FileOutputStream fos = new FileOutputStream(fileKeys)) {
			SecretKey key = KeyGenerator.getInstance("DESede").generateKey();
			// Gravar a chave no ficheiro.
			fos.write(key.getEncoded());
		}
	}

	/**
	 * @param fileIn
	 *            - nome do ficheiro de onde será lida a mensagem a CIFRAR;
	 * @param fileKeys
	 *            - nome do ficheiro que contem a chave;
	 * @param fileOut
	 *            - nome do ficheiro para onde será escrito o conteudo do ficheiro
	 *            fileIn CIFRADO.
	 */
	public void cifrar(String fileIn, String fileKeys, String fileOut) throws FileNotFoundException, IOException,
			InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
		System.out.println("encrypt");
		try (FileOutputStream fos = new FileOutputStream(fileOut)) {
			// recuperar a chave do ficheiro
			SecretKey key = getSecretKey(fileKeys);
			// iniciar o "cifrador" e uma stream que permite cifrar os dados para o ficheiro
			Cipher cip1 = Cipher.getInstance("DESede/ECB/NoPadding");
			try (CipherOutputStream c_out = new CipherOutputStream(fos, cip1)) { // cifrado
				cip1.init(Cipher.ENCRYPT_MODE, key);
				// efectuar a cifra - Java 7 rules.
				Files.copy(java.nio.file.Paths.get(fileIn), c_out);
			}
		}
	}

	/**
	 * @param fileIn
	 *            - nome do ficheiro de onde será lida a mensagem a CIFRADA;
	 * @param fileKeys
	 *            - nome do ficheiro que contem a chave;
	 * @param fileOut
	 *            - nome do ficheiro para onde será escrito o conteudo do ficheiro
	 *            fileIn DECIFRADO.
	 */
	public void decifra(String fileIn, String fileKeys, String fileOut)
			throws InvalidKeyException, FileNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException,
			IOException, NoSuchPaddingException {
		System.out.println("decrypt");
		// recuperar a chave do ficheiro
		SecretKey key = getSecretKey(fileKeys);
		// iniciar o "decifrador"
		Cipher cip2 = Cipher.getInstance("DESede/ECB/NoPadding");
		cip2.init(Cipher.DECRYPT_MODE, key);
		try (CipherInputStream in = new CipherInputStream(new FileInputStream(fileIn), cip2)) { // le do ficheiro
																								// cifrado e decifra
			// decifrar ficheiro, e grava o texto em claro no ficheiro fileOut
			if (new File(fileOut).delete())
				System.out.println("Deleted previous version of " + fileOut);
			Files.copy(in, java.nio.file.Paths.get(fileOut));
		}
	}

	private SecretKey getSecretKey(String fileKeys) throws FileNotFoundException, IOException, InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException {
		try (FileInputStream fis = new FileInputStream(fileKeys)) {
			int n = fis.available();
			byte[] k = new byte[n];
			fis.read(k); 
			KeySpec ks = new DESedeKeySpec(k);
			SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
			return kf.generateSecret(ks);
		}
	}
}