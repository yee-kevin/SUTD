package cseLab6;

import java.lang.Object;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.BufferedImage;
import java.nio.*;
import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;

import org.evosuite.shaded.org.apache.commons.lang3.ArrayUtils;

import java.util.Base64;


public class desImageStartingCode {
    public static void main(String[] args) throws Exception{
        int image_width = 200;
        int image_length = 200;
        // read image file and save pixel value into int[][] imageArray
        BufferedImage img = ImageIO.read(new File("C:\\Users\\Kevin\\Documents\\Virtual Machines\\Eclipse_Folder\\eclipse_space\\test1\\src\\cseLab6\\traingle.bmp.jpeg"));
        image_width = img.getWidth();
        image_length = img.getHeight();
        // byte[][] imageArray = new byte[image_width][image_length];
        int[][] imageArray = new int[image_width][image_length];
        for(int idx = 0; idx < image_width; idx++) {
            for(int idy = 0; idy < image_length; idy++) {
            	// 32-bit integer, 3x8=24 bits used to store RGB component
                int color = img.getRGB(idx, idy);
                // Assign that particular pixel as the color
                imageArray[idx][idy] = color;            
            }
        } 
// TODO: generate secret key using DES algorithm
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecretKey desKey = keyGen.generateKey();

// TODO: Create cipher object, initialize the ciphers with the given key, choose encryption algorithm/mode/padding,
//you need to try both ECB and CBC mode, use PKCS5Padding padding method
        Cipher encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");        
        encryptCipher.init(Cipher.ENCRYPT_MODE, desKey);

        // define output BufferedImage, set size and format
        BufferedImage outImage = new BufferedImage(image_width,image_length, BufferedImage.TYPE_3BYTE_BGR);

        for(int idx = 0; idx < image_width; idx++) {
        // convert each column int[] into a byte[] (each_width_pixel)
        	// Each width now contains pixels for the entire column x 4
            byte[] each_width_pixel = new byte[4*image_length];
            for(int idy = 0; idy < image_length; idy++) {
                ByteBuffer dbuf = ByteBuffer.allocate(4);
                // Writes four bytes (32-bits) containing the given int value, in the current byte order, into this buffer at the current position, and then increments the position by four.
                // Returns this buffer
                dbuf.putInt(imageArray[idx][idy]);
                // Returns the byte array that backs this buffer
                byte[] bytes = dbuf.array();     
                // source array, source pos, dest array, dest pos, length
                // Puts the byte array of 4*column size into each_width_pixel 
                System.arraycopy(bytes, 0, each_width_pixel, idy*4, 4);
            }
       
// TODO: encrypt each column or row bytes 
            // Top-down to Bottom-up
            ArrayUtils.reverse(each_width_pixel);
            byte[] cipherPixelBytes = encryptCipher.doFinal(each_width_pixel);
            // Flip back the encrypted image
            ArrayUtils.reverse(cipherPixelBytes);

// TODO: convert the encrypted byte[] back into int[] and write to outImage (use setRGB)
            // setRGB - x coordinate, y coordinate, RBG value
            byte[] pixelsOut = new byte[4];
            for(int col = 0; col < image_length; col++) {
            	// Source: cipherPixelBytes
            	// Source pos: col*4 (4 for each column)
            	// dest array: pixelsOut
            	// dest pos: 0
            	// dest length: 4
            	System.arraycopy(cipherPixelBytes, col*4, pixelsOut, 0, 4);
            	ByteBuffer wrap4 = ByteBuffer.wrap(pixelsOut);
            	int rgbValue = wrap4.getInt();
            	outImage.setRGB(idx, col, rgbValue);
            }
            
            
        }
//write outImage into file
        ImageIO.write(outImage, "BMP", new File("C:\\Users\\Kevin\\Documents\\Virtual Machines\\Eclipse_Folder\\eclipse_space\\test1\\src\\cseLab6\\triangle_ButtomToTop.bmp.jpeg"));
    }
}