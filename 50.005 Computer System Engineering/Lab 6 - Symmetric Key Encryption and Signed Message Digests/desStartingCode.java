package cseLab6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import javax.crypto.*;

public class desStartingCode {
    public static void main(String[] args) throws Exception {
        String shortFileName = "C:\\Users\\Kevin\\Documents\\Virtual Machines\\Eclipse_Folder\\eclipse_space\\test1\\src\\cseLab6\\shorttext.txt";
        String longFileName = "C:\\Users\\Kevin\\Documents\\Virtual Machines\\Eclipse_Folder\\eclipse_space\\test1\\src\\cseLab6\\longtext.txt";
        
        String shortData = "";
        String longData = "";
        String shortLine;
        String longLine;
        
        BufferedReader shortBufferedReader = new BufferedReader( new FileReader(shortFileName));
        while((shortLine = shortBufferedReader.readLine())!=null){
            shortData = shortData +"\n" + shortLine;
        }
        BufferedReader longBufferedReader = new BufferedReader( new FileReader(longFileName));
        while((longLine = longBufferedReader.readLine())!=null){
            longData = longData +"\n" + longLine;
        }        
        
        System.out.println("Original short content: "+ shortData + '\n');
        System.out.println("Original long content: "+ longData + '\n');

//TODO: generate secret key using DES algorithm
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecretKey desKey = keyGen.generateKey();
//TODO: create cipher object, initialize the ciphers with the given key, choose encryption mode as DES
        Cipher encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");        
        encryptCipher.init(Cipher.ENCRYPT_MODE, desKey);
//TODO: do encryption, by calling method Cipher.doFinal().
        byte[] fileByte = shortData.getBytes();
        byte[] encryptedCipherText = encryptCipher.doFinal(fileByte);        
        byte[] longFileByte = longData.getBytes();
        byte[] longEncryptedCipherText = encryptCipher.doFinal(longFileByte);
//TODO: print the length of output encrypted byte[], compare the length of file smallSize.txt and largeSize.txt
        System.out.println("Short encrypted cipher length: " + encryptedCipherText.length);
        System.out.println("Long encrypted cipher length " + longEncryptedCipherText.length);
        System.out.println("Short encrypted cipher length == Long encrypted cipher length: " + encryptedCipherText.equals(longEncryptedCipherText) + '\n');
        
        // Question 2
        System.out.println("Short encrypted cipherText: " + encryptedCipherText + '\n');
        
//TODO: do format conversion. Turn the encrypted byte[] format into base64format String using DatatypeConverter        
        String base64format = Base64.getEncoder().encodeToString(encryptedCipherText);
//TODO: print the encrypted message (in base64format String format)
        System.out.println("Short encrypted cipher message: " + base64format + '\n');
//TODO: create cipher object, initialize the ciphers with the given key, choose decryption mode as DES
        Cipher decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");   
        decryptCipher.init(Cipher.DECRYPT_MODE, desKey);   
//TODO: do decryption, by calling method Cipher.doFinal().
        byte[] decryptedByte = decryptCipher.doFinal(encryptedCipherText);

//TODO: do format conversion. Convert the decrypted byte[] to String, using "String a = new String(byte_array);"
        String decryptedFile = new String(decryptedByte);          
        
//TODO: print the decrypted String text and compare it with original text
		System.out.println("Short decrypted message: " + decryptedFile + '\n');
		System.out.println("Short decrypted message == Original short content: " + decryptedFile.equals(shortData));
    }
}

