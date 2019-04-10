package cseLab6;

import javax.xml.bind.DatatypeConverter;
import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.util.Base64;

public class digitalSignatureStartingCode {

    public static void main(String[] args) throws Exception {
//Read the text file and save to String data
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
        
        // System.out.println("Original short content: "+ shortData + '\n');
        // System.out.println("Original long content: "+ longData + '\n');

//TODO: generate a RSA keypair, initialize as 1024 bits, get public key and private key from this keypair.
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); keyGen.initialize(1024);
        KeyPair keyPair = keyGen.generateKeyPair();
        Key publicKey = keyPair.getPublic();
        Key privateKey = keyPair.getPrivate();


//TODO: Calculate message digest, using MD5 hash function
        MessageDigest shortMd = MessageDigest.getInstance("MD5");
        byte[] shortFileByte = shortData.getBytes();
        shortMd.update(shortFileByte);
        byte[] shortDigest = shortMd.digest();  
        
        MessageDigest longMd = MessageDigest.getInstance("MD5");
        byte[] longFileByte = longData.getBytes();
        longMd.update(longFileByte);
        byte[] longDigest = longMd.digest();  

//TODO: print the length of output digest byte[], compare the length of file smallSize.txt and largeSize.txt            
        String shortDigestString = Base64.getEncoder().encodeToString(shortDigest);
        System.out.println("Short digest: " + shortDigestString);
        System.out.println("Short digest length: " + shortDigest.length);
        
        String longDigestString = Base64.getEncoder().encodeToString(longDigest);
        System.out.println("Long digest: " + longDigestString);
        System.out.println("Long digest length: " + longDigest.length);

           
//TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as encrypt mode, use PRIVATE key.
	    Cipher EncryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    EncryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);

//TODO: encrypt digest message
	    byte[] shortEncryptedCipherText = EncryptCipher.doFinal(shortDigest);
	    System.out.println("Short encrypted digest length (byte): " + shortEncryptedCipherText.length);
	    byte[] longEncryptedCipherText = EncryptCipher.doFinal(longDigest); 
	    System.out.println("Long encrypted digest length (byte): " + longEncryptedCipherText.length);

//TODO: print the encrypted message (in base64format String using DatatypeConverter) 
	    String shortbase64format = Base64.getEncoder().encodeToString(shortEncryptedCipherText);
	    System.out.println("Short encrypted digest: " + shortbase64format);	   
	    String longbase64format = Base64.getEncoder().encodeToString(longEncryptedCipherText);
	    System.out.println("Long encrypted digest: " + longbase64format);


//TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as decrypt mode, use PUBLIC key.    
	    Cipher DecryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");   
        DecryptCipher.init(Cipher.DECRYPT_MODE, publicKey);  

//TODO: decrypt message
        byte[] shortDecryptedByte = DecryptCipher.doFinal(shortEncryptedCipherText);   
        byte[] longDecryptedByte = DecryptCipher.doFinal(longEncryptedCipherText);  

//TODO: print the decrypted message (in base64format String using DatatypeConverter), compare with origin digest 
        String shortDecryptedFile = Base64.getEncoder().encodeToString(shortDecryptedByte);       
        System.out.println("Short decrypted digest: " + shortDecryptedFile);        
        String longDecryptedFile = Base64.getEncoder().encodeToString(longDecryptedByte);       
        System.out.println("Long decrypted digest: " + longDecryptedFile);
        
    }

}