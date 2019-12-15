package pt;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author jpmartins
 *
 */
public final class DESedeCLI {

	public static void main(String[] args) {
		try {
			if (args.length >= 2) {
				DESede des = new DESede();
				String fileIn, fileKey, fileOut;

				if (args[0].equals("-g")) {
					fileKey = (String) args[1];
					des.generateKey(fileKey);
				} else if (args[0].equals("-c")) {
					if (args.length == 4) {
						fileIn = (String) args[1];
						fileKey = (String) args[2];
						fileOut = (String) args[3];
						des.cifrar(fileIn, fileKey, fileOut);
					} else {
						showError("Número inválido de parametros");
						System.exit(1);
					}
				} else if (args[0].equals("-d")) {
					if (args.length == 4) {
						fileIn = (String) args[1];
						fileKey = (String) args[2];
						fileOut = (String) args[3];
						des.decifra(fileIn, fileKey, fileOut);
					} else {
						showError("Número inválido de parametros");
						System.exit(2);
					}
				} else {
					showError(args[0]);
					System.exit(3);
				}
				System.out.println("Operação terminada com sucesso.");
			} else {
				showError(null);
				System.exit(4);
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static void showError(String string) {
		System.out.print("Parametros inválidos");
		if (string != null)
			System.out.print(": " + string);
		System.out.println();
		System.out.println("Utilização:");
		System.out.println(" Gerar chaves: -g keyFileName");
		System.out.println(" Cifrar: -c fileToEncrypt keyFileName newEncryptFileName");
		System.out.println(" Decifrar: -d encryptFileName keyFileName newDecryptfileName");
	}

}
