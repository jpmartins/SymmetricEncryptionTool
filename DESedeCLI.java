package pt.isel.cc;

/**
 * @author jpmartins
 *
 */
public final class DESedeCLI {

	public static void main(String[] args) {
		if (args.length >= 3) {
			DESede des = new DESede();
			String fileIn, fileKey, fileOut;

			if (args[1].equals("-g")) {
				fileKey = (String) args[2];
				des.generateKey(fileKey);
			} else if (args[1].equals("-c")) {
				if (args.length == 5) {
					fileIn = (String) args[2];
					fileKey = (String) args[3];
					fileOut = (String) args[4];
					des.cifrar(fileIn, fileKey, fileOut);
				} else {
					showError("Número inválido de parametros");
					System.exit(1);
				}
			} else if (args[1].equals("-d")) {
				if (args.length == 5) {
					fileIn = (String) args[2];
					fileKey = (String) args[3];
					fileOut = (String) args[4];
					des.decifra(fileIn, fileKey, fileOut);
				} else {
					showError("Número inválido de parametros");
					System.exit(2);
				}
			} else {
				showError(args[1]);
				System.exit(3);
			}
			System.out.println("Operação terminada com sucesso.");
		} else {
			showError(null);
			System.exit(4);
		}
	}

	private static void showError(String string) {
		System.out.print("Parametros inválidos");
		if (string != null)
			System.out.print(": " + string);
		System.out.println();//System.out.print(System.getProperty("line.separator"));
		System.out.println("Utilização:");
		System.out.println(" Gerar chaves: -g keyFileName");
		System.out.println(" Cifrar: -c fileToEncrypt keyFileName newEncryptFileName");
		System.out.println(" Decifrar: -d encryptFileName keyFileName newDecryptfileName");
	}

}
