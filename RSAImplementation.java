package rsaimplementation;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
/**
 *
 * @author Giuseppe Barretta
 */
public class RSAImplementation {    
    public static void main(String[] args) {
        BigInteger p, q, n, phi, e, d;
        String message, decryptedStr, msg1, msg2;
        byte[] encryptedBytes, decryptedBytes, signatureBytes, verificationBytes;
        Scanner kb = new Scanner(System.in);
        
        //contructors return a probable prime
        p = new BigInteger(1024, 100, new Random());
        q = new BigInteger(1024, 100, new Random());
        
        //n=p*q
        n = p.multiply(q);     
        
        //phi=(p-1)*(q-1)
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        
        //e should not be 1 or 2. start at 3
        e = new BigInteger("3");
        
        //loop until the gcd of phi and e equals 1
        while (!(phi.gcd(e).equals(BigInteger.ONE)))
        {
            //add 1 to check next number
            e = e.add(BigInteger.ONE);
        }   
        
        //mod inverse of phi
        d = e.modInverse(phi);
        
//        System.out.println("e: " + e);
//        System.out.println("d: " + d);
//        System.out.println("n: " + n);
        
        System.out.println("----------Homework Part 1, 2, 3----------");
        System.out.print("Please enter the message you would like to encrypt: ");
        message = kb.nextLine();
        System.out.println("");
        
        encryptedBytes = encryption(message.getBytes(), e, n).toByteArray();
        decryptedBytes = decryption(encryptedBytes, d, n).toByteArray();
        signatureBytes = signature(message.getBytes(), d, n).toByteArray();
        verificationBytes = verification(signatureBytes, e, n).toByteArray();
        decryptedStr = new String(decryptedBytes, StandardCharsets.US_ASCII);         

        System.out.println("Original message: " + message);
        System.out.println("Original byte message: " + Arrays.toString(message.getBytes()));
        System.out.println("Encrypted byte message: " + Arrays.toString(encryptedBytes));
        System.out.println("Signature: " + Arrays.toString(signatureBytes));
        System.out.println("Verification: " + Arrays.toString(verificationBytes));
        System.out.println("Decrypted message: " + decryptedStr);
        System.out.println("");
        
        //homomorphic check
        System.out.println("----------Homework Part 4: homomorphic check----------");       
        System.out.print("Please enter message 1: ");
        msg1 = kb.nextLine();
        System.out.print("Please enter message 2: ");
        msg2 = kb.nextLine();
        System.out.println("");
        
        BigInteger separate = encryption(msg1.getBytes(),e,n).multiply(encryption(msg2.getBytes(),e,n));
        BigInteger temp1 = new BigInteger(msg1.getBytes());
        BigInteger temp2 = new BigInteger(msg2.getBytes());
        BigInteger combined = encryption((temp1.multiply(temp2).toByteArray()),e,n);
        boolean equalityCheck = separate.equals(combined);
        
        System.out.println("The homomorphic property of RSA is verified: " + equalityCheck);
    }
 
    private static BigInteger encryption(byte[] message, BigInteger e, BigInteger n)
    {
        return new BigInteger(message).modPow(e, n);
    }

    private static BigInteger decryption(byte[] cipher, BigInteger d, BigInteger n)
    {
        return new BigInteger(cipher).modPow(d, n);
    }  
    
    private static BigInteger signature(byte[] message, BigInteger d, BigInteger n)
    {
        return new BigInteger(message).modPow(d, n);
    }   
    
    private static BigInteger verification(byte[] signature, BigInteger e, BigInteger n)
    {
        return new BigInteger(signature).modPow(e, n);
    } 
}
