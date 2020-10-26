

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class generador {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte[] b = new byte[400];
		try {
			FileOutputStream fr = new FileOutputStream("./data/100MiB.txt");
			fr.write(b, 0, b.length);
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
