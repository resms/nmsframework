package com.nms.util.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;


/**
 * 安全工具（加密解密）
 *
 * @author sam
 */
public final class SecUtil
{
    private static final Logger logger = LoggerFactory.getLogger(SecUtil.class);

    public static final int CR = 13; // <US-ASCII CR, carriage return (13)>
    public static final int LF = 10; // <US-ASCII LF, linefeed (10)>
    public static final int SP = 32; // <US-ASCII SP, space (32)>
    public static final int HT = 9;  // <US-ASCII HT, horizontal-tab (9)>

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset ASCII = Charset.forName("US-ASCII");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    private static final char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    private static final String CustomHexDigits = "NMS524@MP_OXQVDI";

    /**
     * 数字签名算法（指纹认证）
     */
    public static final String ALGORITHM_SHA = "SHA";
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA512 = "SHA-512";
    public static final String HMACSHA1 = "HmacSHA1";
    private static final int DEFAULT_HMACSHA1_KEYSIZE = 160; //RFC2401

    /**
     * 对称加密算法
     */
    public static final String DES_KEY_ALGORITHM = "DES";         //56bit=7byte
    public static final String DES_CIPHER_ALGORITHM_CBC = "DES/CBC/PKCS5Padding";
    public static final String DESede_KEY_ALGORITHM = "DESede";   //168bit=21byte
    public static final String DESede_CIPHER_ALGORITHM_CBC = "DESede/CBC/PKCS5Padding";
    public static final String AES_KEY_ALGORITHM = "AES";         //128bit=16byte
    public static final String AES_CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";


    private static final String DEFAULT_KEY1 = "HtTp://wWw.rEsMs.NeT";
    private static final byte[] DEFAULT_SALT = "Sam@2013!@#".getBytes(UTF_16LE);

    //128=16byte,192=24byte,256=32byte
//    private static final int DEFAULT_AES_KEYSIZE_BIT = 128;
    private static final int DEFAULT_AES_KEYSIZE_BYTE = 16;
    private static final int DEFAULT_AES_IVSIZE_BYTE = 16;

    /**
     * 非对称算法
     */
    public static final String DSA = "DSA";         //1024
    public static final String RSA = "RSA";         //2048

    static{
//        DEFAULT_AES_KEY = new byte[DEFAULT_AES_KEYSIZE_BYTE];
//        DEFAULT_AES_IV = new byte[DEFAULT_AES_IVSIZE_BYTE];
//
//        System.arraycopy(digest(DEFAULT_KEY1,SHA512,UTF_8),0,DEFAULT_AES_KEY,0,16);
//
//        System.arraycopy(digest(DEFAULT_KEY1 + DEFAULT_KEY2,MD5,UTF_8),0,DEFAULT_AES_IV,0, DEFAULT_AES_IVSIZE_BYTE);
    }

    //-- HMAC-SHA1 funciton begin --//

    /**
     * 使用HMAC-SHA1进行消息签名, 返回字节数组,长度为20字节.
     *
     * @param input 原始输入字符数组
     * @param key   HMAC-SHA1密钥
     */
    public static byte[] hmacSha1(final byte[] input, final byte[] key)
    {
        try
        {
            SecretKey secretKey = new SecretKeySpec(key, HMACSHA1);
            Mac mac = Mac.getInstance(HMACSHA1);
            mac.init(secretKey);
            return mac.doFinal(input);
        }
        catch (GeneralSecurityException e)
        {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节).
     * HMAC-SHA1算法对密钥无特殊要求, RFC2401建议最少长度为160位(20字节).
     */
    public static byte[] generateHmacSha1Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1);
            keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 校验HMAC-SHA1签名是否正确.
     *
     * @param expected 已存在的签名
     * @param input    原始输入字符串
     * @param key      密钥
     */
    public static boolean verifyHMacSha1(final byte[] expected, final byte[] input, final byte[] key)
    {
        byte[] actual = hmacSha1(input, key);
        return Arrays.equals(expected, actual);
    }

    //-- HMAC-SHA1 funciton end --//

    /**
     * 根据数字签名算法获得MessageDigest对象
     * @param algorithm 数字签名算法,MD5,SHA1,SHA256,SHA512
     * @return
     */
    public static MessageDigest getMessageDigest(final String algorithm)
    {
        if(null != algorithm && !algorithm.isEmpty())
        {
            try {
                if(MD5.equals(algorithm))
                    return MessageDigest.getInstance(MD5);
                else if(SHA1.equals(algorithm))
                    return MessageDigest.getInstance(SHA1);
                else if(SHA256.equals(algorithm))
                    return MessageDigest.getInstance(SHA256);
                else if(SHA512.equals(algorithm))
                    return MessageDigest.getInstance(SHA512);
                else if(ALGORITHM_SHA.equals(algorithm))
                    return MessageDigest.getInstance(ALGORITHM_SHA);
                else
                    return null;
            } catch (NoSuchAlgorithmException e) {
                logger.info("notFound MessageDigest by algorithm" + algorithm,e);
            }
        }
        else
        {
            logger.info("algorithm is null or empty");
        }
        return null;
    }

    /**
     * 数字签名
     *
     * @param src       待加密字符串
     * @param algorithm 算法，如：md5、sha
     * @param charset   字符集,不指定字符集请置为null
     * @return          签名后的字节数组
     */
    public static byte[] digest(final String src,final String algorithm, final Charset charset)
    {
        byte[] b = null;
        if(null != src && !src.isEmpty()) {
            MessageDigest md = getMessageDigest(algorithm);
            if(null == md) {
                return null;
            }
            if(charset == null)
                b = digest(src.getBytes(),algorithm,DEFAULT_SALT,1);
            else
                b = digest(src.getBytes(charset),algorithm,DEFAULT_SALT,1);
        }
        return b;
    }

    /**
     * 对输入流做数字签名
     *
     * @param input     输入流
     * @param algorithm 签名算法
     * @return          签名后的字节数组
     */
    public static byte[] digest(final InputStream input, final String algorithm)
    {
        try
        {
            MessageDigest md = getMessageDigest(algorithm);

            if(null == md) {
                return null;
            }

            int bufferLength = 4096;//一个内存页大小
            byte[] buffer = new byte[bufferLength];
            int read = input.read(buffer, 0, bufferLength);

            while (read > -1)
            {
                md.update(buffer, 0, read);
                read = input.read(buffer, 0, bufferLength);
            }

            return md.digest();
        }
        catch (IOException ex)
        {
            logger.error(ex.getMessage());
        }

        return null;
    }

    /**
     * 对字符串进行散列, 支持md5与sha1,sha256,sha512算法.
     *
     * @param input         待加密字节数组
     * @param algorithm     加密签名算法
     * @param salt          盐,佐料,通过该字节数组混淆签名结果
     * @param iterations    循环签名次数
     * @return              签名后的字节数组
     */
    private static byte[] digest(final byte[] input, final String algorithm, final byte[] salt, final int iterations)
    {
        if(null == input || null == algorithm)
            return null;

        MessageDigest md = getMessageDigest(algorithm);

        if(null == md) {
            return null;
        }

        if(null != salt)
            md.update(salt);

        byte[] result = md.digest(input);

        for (int i = 1; i < iterations; i++)
        {
            md.reset();
            result = md.digest(result);
        }

        return result;
    }

    /**
     * 生成随机的Byte[]作为salt.
     *
     * @param numBytes byte数组的大小
     */
    public static byte[] generateSalt(final int numBytes)
    {
        if(numBytes <= 0) {
            logger.info("numBytes argument must be a positive integer (1 or larger)");
            return null;
        }
        SecureRandom sr = new SecureRandom();
        byte[] bytes = new byte[numBytes];
        sr.nextBytes(bytes);
        return bytes;
    }

    /**
     * SHA1加密字符串，默认字符集为UTF-8
     *
     * @param plainText 待加密字符串
     * @return          SHA1加密后的16进制字符串
     */
    public static String sha1(String plainText)
    {
        return byteArray2HexString(digest(plainText, SHA1,UTF_8));
    }

    /**
     * 按指定字符集SHA1加密字符串
     *
     * @param plainText 待加密字符串
     * @param charset   字符集
     * @return          SHA1加密后的16进制字符串
     */
    public static String sha1(final String plainText, final Charset charset)
    {
        return byteArray2HexString(digest(plainText, SHA1,charset));
    }

    /**
     * 对输入字符串进行sha1散列.
     *
     * @param input 待加密字符串
     * @return      sha1加密后的字节数组
     */
    public static String sha1(final byte[] input)
    {
        return byteArray2HexString(digest(input, SHA1, null, 1));
    }

    /**
     * 对输入字符串进行sha1散列.
     *
     * @param input 待加密字符串
     * @return      sha1加密后的字节数组
     */
    public static byte[] sha1ByteArray(final byte[] input)
    {
        return digest(input, SHA1, null, 1);
    }

    /**
     *
     * @param input
     * @param salt
     * @return
     */
    public static byte[] sha1ByteArray(final byte[] input, final byte[] salt)
    {
        return digest(input, SHA1, salt, 1);
    }

    /**
     *
     * @param input
     * @param salt
     * @param iterations
     * @return
     */
    public static byte[] sha1ByteArray(final byte[] input, final byte[] salt, final int iterations)
    {
        return digest(input, SHA1, salt, iterations);
    }

	/**
	 * 对文件进行sha1散列.
     *
     * @param input 待加密输入流对象
     * @return      SHA1加密后的字节数组
	 */
	public static byte[] sha1(final InputStream input)
	{
		return digest(input, SHA1);
	}

	/**
	 * JAVA和.NET都可以识别的SHA1加密
	 * @param plainText 待加密字符串
	 * @return          SHA1加密后的16进制字符串
	 */
	public static String crossHash(final String plainText,final String algorithm)
	{
		final byte b[] = digest(plainText,algorithm, UTF_16LE);
		return byteArray2HexString(b);
	}

    public static byte[] crossHashByteArray(final String plainText,final String algorithm)
    {
        return digest(plainText,algorithm, UTF_16LE);
    }

    /**
     * SHA256加密字符串，默认字符集为UTF-8
     *
     * @param plainText 待加密字符串
     * @return          SHA256加密后的16进制字符串
     */
    public static String sha256(final String plainText)
    {
        return byteArray2HexString(digest(plainText, SHA256,UTF_8));
    }

    /**
     * 按指定字符集SHA256加密字符串
     *
     * @param plainText 待加密字符串
     * @param charset   字符集
     * @return          SHA256加密后的16进制字符串
     */
    public static String sha256(final String plainText,final Charset charset)
    {
        return byteArray2HexString(digest(plainText, SHA256,charset));
    }

    /**
     * 对输入字符串进行SHA256散列.
     *
     * @param input 待加密字符串
     * @return      SHA256加密后的字节数组
     */
    public static String sha256(final byte[] input)
    {
        return byteArray2HexString(digest(input, SHA256, null, 1));
    }

    /**
     * 对输入字符串进行SHA256散列.
     *
     * @param input 待加密字符串
     * @return      SHA256加密后的字节数组
     */
    public static byte[] sha256ByteArray(final byte[] input)
    {
        return digest(input, SHA256, null, 1);
    }

    /**
     *
     * @param input
     * @param salt
     * @return
     */
    public static byte[] sha256ByteArray(final byte[] input, final byte[] salt)
    {
        return digest(input, SHA256, salt, 1);
    }

    /**
     *
     * @param input
     * @param salt
     * @param iterations
     * @return
     */
    public static byte[] sha256ByteArray(final byte[] input, final byte[] salt, final int iterations)
    {
        return digest(input, SHA256, salt, iterations);
    }

    /**
     * 对文件进行sha1散列.
     *
     * @param input 待加密输入流对象
     * @return      SHA1加密后的字节数组
     */
    public static byte[] sha256(final InputStream input)
    {
        return digest(input, SHA256);
    }

    /**
     * MD5加密字符串
     *
     * @param plainText 待加密字符串
     * @return          MD5加密后的16进制字符串
     */
    public static String md5(final String plainText)
    {
        return byteArray2HexString(digest(plainText, MD5,UTF_8));
    }

    /**
     * MD5加密字符串
     * @param plainText 待加密字符串
     * @return          MD5加密后的字节数组
     */
    public static byte[] md5ByteArray(final String plainText)
    {
        return digest(plainText, MD5,UTF_8);
    }

    /**
     * 按指定字符集MD5加密字符串
     *
     * @param plainText 待加密字符串
     * @param charset   字符集
     * @return          MD5加密后的16进制字符串
     */
    public static String md5(final String plainText,final Charset charset)
    {
        return byteArray2HexString(digest(plainText, MD5,charset));
    }

    /**
     * 按自定字符集MD5加密字符串
     *
     * @param plainText 待加密字符串
     * @param charset   字符集
     * @return          MD5加密后的字节数组
     */
    public static byte[] md5ByteArray(final String plainText,final Charset charset)
    {
        return digest(plainText, MD5,charset);
    }

    /**
     * 对输入流进行md5散列.
     *
     * @param input 待加密输入流对象
     * @return      输入流的MD5字符串
     */
    public static String md5(final InputStream input)
    {
        return byteArray2HexString(digest(input, MD5));
    }

    /**
     * 对输入流进行md5散列.
     * @param input 待加密输入流对象
     * @return      输入流的MD5字节数组
     */
    public static byte[] md5ByteArray(final InputStream input)
    {
        return digest(input, MD5);
    }

	/**
	 * 自定义的MD5加密算法，做了简单的位移混淆
	 *
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	public static String cmd5(final String plainText)
	{
        if(null == plainText || plainText.isEmpty())
            return null;

		final byte[] bytes = plainText.getBytes(UTF_16LE);
		return cmd5(bytes, CustomHexDigits);
	}

	/**
	 * 做了简单混淆的MD5加密
	 *
	 * @param bytes     待加密的自己数组
	 * @param key16char 16个字符的密钥
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	private static String cmd5(final byte[] bytes, final String key16char)
	{
        if(null == bytes || null == key16char || bytes.length < 1 || key16char.isEmpty())
            return null;

		MessageDigest md = getMessageDigest(MD5);
        if(null == md)
            return null;

        md.update(DEFAULT_SALT);

		byte b, tmp[] = md.digest(bytes);

		char str[] = new char[32];
		int k = 0;
		for (int i = 0; i < 16; i++)
		{
			b = tmp[i];
			str[k++] = key16char.charAt(b >>> 4 & 0xf);
			str[k++] = key16char.charAt(b & 0xf);
		}

		return new String(str);
	}

    /**
     * Base64加密字符串，默认用UTF-8编码
     *
     * @param plainText 待加密字符串
     * @return          Base64加密字符串
     */
    public static String encodeBase64(final String plainText)
    {
        return encodeBase64(plainText,UTF_8);
    }

    /**
     * Base64解密字符串，默认用UTF-8编码
     *
     * @param dest 待解密字符串
     * @return     Base64加密字符串
     */
    public static String decodeBase64(final String dest)
    {
        if(null == dest)
            return null;
        if(dest.isEmpty())
            return "";
        return decodeBase64(dest,UTF_8);
    }

    /**
     * Base64加密字符串，手动指定编码格式
     *
     * @param plainText      待编码字符串
     * @param charset   字符集
     * @return          返回编码后的指定字符集或未指定字符集的Base64加密的字符串
     */
    public static String encodeBase64(final String plainText,final Charset charset)
    {
        if(null == plainText)
            return null;
        if(plainText.isEmpty())
            return "";

        if(null == charset)
            return Base64.encodeBase64String(plainText.getBytes());
        else
            return Base64.encodeBase64String(plainText.getBytes(charset));
    }

    /**
     * Base64解密字符串，手动指定编码格式
     *
     * @param dest      待解密字符串
     * @param charset   字符集
     * @return          Base64解密字符串
     */
    public static String decodeBase64(final String dest,final Charset charset)
    {
        if(null == dest)
            return null;
        if(dest.isEmpty())
            return "";

        byte[] b = null;
        b = Base64.decodeBase64(dest.getBytes(charset));

        String plaintext = new String(b, charset);
        return plaintext;
    }

    /**
     * Base64加密字节数组
     *
     * @param data  待加密字节数组
     * @return      Base64加密字节数组
     */
    public static byte[] encodeBase64(final byte[] data)
    {
        return Base64.encodeBase64(data);
    }

    /**
     * Base64解密字节数组
     *
     * @param data  待解密字节数组
     * @return      Base64解密字节数组
     */
    public static byte[] decodeBase64(final byte[] data)
    {
        return Base64.decodeBase64(data);
    }

    /**
     * Base64加密输入流
     *
     * @param in        待加密输入流对象
     * @param fileSize  文件大小
     * @return          Base64加密后的字节数组
     */
    public static byte[] encodeBase64(final InputStream in, final int fileSize)
    {
        Base64InputStream bi = new Base64InputStream(in);
        byte[] encryptText = new byte[fileSize];
        try
        {
            while (bi.read() != -1)
            {
                bi.read(encryptText);
            }
            encryptText = Base64.encodeBase64(encryptText);
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(),e);
        }
        return encryptText;
    }

    /**
     * Base64解密输入流
     *
     * @param in        待解密输入流
     * @param fileSize  文件大小
     * @return          Base64解密后的字节数组
     */
    public static byte[] decodeBase64(final InputStream in, final int fileSize)
    {
        Base64InputStream bi = new Base64InputStream(in);
        byte[] plainText = new byte[fileSize];
        try
        {
            while (bi.read() != -1)
            {
                bi.read(plainText);
            }
            plainText = Base64.decodeBase64(plainText);
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(),e);
        }
        return plainText;
    }

    /**
     * 生成非对称密钥，使用时注意公钥和私钥
     * DSA (1024)
     * RSA (2048)
     *
     * @param algorithm 非对称密钥算法，如：DSA,RSA
     * @return
     */
    public static KeyPair createKeyPair(final String algorithm)
    {

        KeyPair key = null;
        try
        {
            key = KeyPairGenerator.getInstance(algorithm).generateKeyPair();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return key;
    }

    /**
     * 生成对称密钥
     * AES (128)
     * DES (56)
     * DESede (168)
     *
     * @param algorithm 对称密钥算法，如：AES,DES,DESede
     * @return
     */
    public static SecretKey createSecretKey(final String algorithm)
    {

        SecretKey key = null;
        try
        {
            key = KeyGenerator.getInstance(algorithm).generateKey();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return key;
    }

    /**
     * 使用keySpec创建对称密钥
     *
     * @param algorithm
     * @param keySpec
     * @return
     */
    public static SecretKey createSecretKey(final String algorithm, final KeySpec keySpec)
    {

        SecretKey key = null;
        try
        {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            key = keyFactory.generateSecret(keySpec);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return key;
    }

    /**
     * 读取X509公钥
     *
     * @param algorithm
     * @return
     */
    public static PublicKey getPublicKey(final String algorithm, final KeySpec keySpec)
    {

        PublicKey pubKey = null;
        try
        {
            pubKey = KeyFactory.getInstance(algorithm).generatePublic(keySpec);
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return pubKey;
    }

    /**
     * 获得私钥
     *
     * @param algorithm
     * @param keySpec
     * @return
     */
    public static PrivateKey getPrivateKey(final String algorithm, final KeySpec keySpec)
    {
        PrivateKey priKey = null;
        try
        {
            priKey = KeyFactory.getInstance(algorithm).generatePrivate(keySpec);
        }
        catch (InvalidKeySpecException e)
        {
            logger.error(e.getMessage(),e);
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.error(e.getMessage(),e);
        }
        return priKey;
    }

    /**
     * 加密或解密字符串
     *
     * @param key       加解密Key
     * @param plainBytes 待加解密字节数组对象
     * @param opmode    加解密模式,Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @return          加密后的字节数组
     */
    public static byte[] doFinal(final Key key, final byte[] plainBytes, final int opmode)
    {

        byte[] ciphertext = null;
        try
        {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            // 操作模式（加密或解密）
            if (opmode == Cipher.ENCRYPT_MODE)
            {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            else if (opmode == Cipher.DECRYPT_MODE)
            {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            else
            {
                logger.info("notFound Operation mode");
                return null;
            }
            ciphertext = cipher.doFinal(plainBytes);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(),e);
        }

        return ciphertext;
    }

    /**
     * 得到加密或解密输入流
     *
     * @param key       加解密Key
     * @param is        待加解密输入流对象
     * @param opmode    加解密操作模式,Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @return          加解密后的CipherInputStream对象
     */
    public static CipherInputStream doFinal(final Key key, final InputStream is, final int opmode)
    {

        CipherInputStream cis = null;
        try
        {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            // 操作模式（加密或解密）
            if (opmode == Cipher.ENCRYPT_MODE)
            {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            else if (opmode == Cipher.DECRYPT_MODE)
            {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            else
            {
                logger.info("notFound Operation mode");
                return null;
            }

            cis = new CipherInputStream(is, cipher);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return cis;
    }

    /**
     * 得到加密或解密输出流
     *
     * @param key       加解密Key
     * @param os        待加解密输出流对象
     * @param opmode    加解密操作模式,Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @return          加解密后的CipherOutputStream对象
     */
    public static CipherOutputStream doFinal(final Key key, final OutputStream os,int opmode)
    {
        CipherOutputStream cos = null;
        try
        {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            // 操作模式（加密或解密）
            if (opmode == Cipher.ENCRYPT_MODE)
            {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            else if (opmode == Cipher.DECRYPT_MODE)
            {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            else
            {
                logger.info("notFound Operation mode");
                return null;
            }

            cos = new CipherOutputStream(os, cipher);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return cos;
    }

    /**
     * 验证数字签名
     *
     * @param publicKey
     * @param algorithm 签名算法
     * @param sign      私钥签名的signature
     * @param data      签名的数据
     * @return
     */
    public static boolean signVerify(final PublicKey publicKey, final String algorithm, final byte[] sign, final byte[] data)
    {

        boolean flag = false;
        try
        {
            Signature signagure = Signature.getInstance(algorithm);
            signagure.initVerify(publicKey);
            signagure.update(data);
            flag = signagure.verify(sign);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(),e);
        }

        return flag;
    }

    /**
     * 用私钥对信息进行数字签名
     *
     * @param privateKey
     * @param algorithm 签名算法
     * @param data      签名的数据
     * @return
     */
    public static byte[] sign(final PrivateKey privateKey, final String algorithm,final byte[] data)
    {
        byte[] sign = null;
        try
        {
            Signature signature = Signature.getInstance(algorithm);
            signature.initSign(privateKey);
            signature.update(data);
            sign = signature.sign();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(),e);
        }

        return sign;
    }

    /**
     * SHA数字签名
     *
     * @param src
     * @return
     * @throws Exception
     * @author panguixiang
     */
    public static byte[] shaDigest(final String src)
    {
        byte[] b = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_SHA);
            b = md.digest(src.getBytes());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return b;
    }

//    /**
//     * 字节数组转为最大写16进制字符串
//     *
//     * @param b
//     * @return
//     * @author panguixiang
//     */
//    public static String byteArray2HexString(byte[] b)
//    {
////        long time = System.currentTimeMillis();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < b.length; i++)
//        {
//            sb.append(Integer.toHexString((b[i] & 0xFF) | 0x100).substring(1,3));
//        }
////        System.out.println("time=" + (System.currentTimeMillis() - time));
//        return sb.toString();
//    }
    /**
     * 字节数组转为最大写16进制字符串
     *
     * @param b
     * @return
     * @author panguixiang
     */
    public static String byteArray2HexString(final byte[] b)
    {
//        long time = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++)
        {
            sb.append(hexDigits[b[i] >>> 4 & 0xF]);//取字节中高4位的数字转换，>>>为逻辑右移，将符号位一起右移
            sb.append(hexDigits[b[i] & 0xF]);       //取字节中低4位的数字转换
        }
//        System.out.println("time=" + (System.currentTimeMillis() - time));
        return sb.toString();
    }

    /**
     * 按默认密码加密
     *
     * @param plainText 待加密文本
     * @return          加密后的Base64编码文本
     */
    public static String encryptByDES(final String plainText)
    {
        if(null == plainText || plainText.isEmpty())
            return null;

        return Base64.encodeBase64String(encrypt(plainText.getBytes(UTF_16LE),DEFAULT_KEY1, DES_KEY_ALGORITHM));
    }

    /**
     * 按默认密码解密
     *
     * @param base64String  Base64加密文本
     * @return              解密后的文本
     */
    public static String decryptByDES(final String base64String)
    {
        if(null == base64String || base64String.isEmpty())
            return null;

        return new String(decrypt(Base64.decodeBase64(base64String),DEFAULT_KEY1, DES_KEY_ALGORITHM));
    }

    /**
     * 根据指定的密码加密
     *
     * @param plainText 待加密文本
     * @param password  密码文本
     * @return          加密后的Base64编码文本
     */
    public static String encryptByDES(final String plainText, final String password)
    {
        if(null == plainText || null == password || plainText.isEmpty() || password.isEmpty())
            return null;
        return Base64.encodeBase64String(encrypt(plainText.getBytes(UTF_16LE),password, DES_KEY_ALGORITHM));
    }

    /**
     * 根据指定的密码解密
     * @param base64String  Base64加密文本
     * @param password      密码文本
     * @return              解密后的文本
     */
    public static String decryptByDES(final String base64String, final String password)
    {
        if(null == base64String || null == password || base64String.isEmpty() || password.isEmpty())
            return null;

        return new String(decrypt(Base64.decodeBase64(base64String),password, DES_KEY_ALGORITHM));
    }

    /**
     * 按默认密码加密
     *
     * @param plainText 待加密文本
     * @return          加密后的Base64编码文本
     */
    public static String encryptByDESede(final String plainText)
    {
        if(null == plainText || plainText.isEmpty())
            return null;

        return Base64.encodeBase64String(encrypt(plainText.getBytes(UTF_16LE),DEFAULT_KEY1, DESede_KEY_ALGORITHM));
    }

    /**
     * 按默认密码解密
     *
     * @param base64String  Base64加密文本
     * @return              解密后的文本
     */
    public static String decryptByDESede(final String base64String)
    {
        if(null == base64String || base64String.isEmpty())
            return null;

        return new String(decrypt(Base64.decodeBase64(base64String),DEFAULT_KEY1, DESede_KEY_ALGORITHM));
    }

    /**
     * 根据指定的密码加密
     *
     * @param plainText 待加密文本
     * @param password  密码文本
     * @return          加密后的Base64编码文本
     */
    public static String encryptByDESede(final String plainText, final String password)
    {
        if(null == plainText || null == password || plainText.isEmpty() || password.isEmpty())
            return null;
        return Base64.encodeBase64String(encrypt(plainText.getBytes(UTF_16LE),password, DESede_KEY_ALGORITHM));
    }

    /**
     * 根据指定的密码解密
     * @param base64String  Base64加密文本
     * @param password      密码文本
     * @return              解密后的文本
     */
    public static String decryptByDESede(final String base64String, final String password)
    {
        if(null == base64String || null == password || base64String.isEmpty() || password.isEmpty())
            return null;

        return new String(decrypt(Base64.decodeBase64(base64String),password, DESede_KEY_ALGORITHM));
    }

    /**
     * 按默认密码加密
     *
     * @param plainText 待加密文本
     * @return          加密后的Base64编码文本
     */
    public static String encryptByAES(final String plainText)
    {
        if(null == plainText || plainText.isEmpty())
            return null;

        return Base64.encodeBase64String(encrypt(plainText.getBytes(UTF_16LE),DEFAULT_KEY1, AES_KEY_ALGORITHM));
    }

    /**
     * 按默认密码解密
     *
     * @param base64String  Base64加密文本
     * @return              解密后的文本
     */
    public static String decryptByAES(final String base64String)
    {
        if(null == base64String || base64String.isEmpty())
            return null;

        return new String(decrypt(Base64.decodeBase64(base64String),DEFAULT_KEY1, AES_KEY_ALGORITHM));
    }

    /**
     * 根据指定的密码加密
     *
     * @param plainText 待加密文本
     * @param password  密码文本
     * @return          加密后的Base64编码文本
     */
    public static String encryptByAES(final String plainText, final String password)
    {
        if(null == plainText || null == password || plainText.isEmpty() || password.isEmpty())
            return null;
        return Base64.encodeBase64String(encrypt(plainText.getBytes(UTF_16LE),password, AES_KEY_ALGORITHM));
    }

    /**
     * 根据指定的密码解密
     * @param base64String  Base64加密文本
     * @param password      密码文本
     * @return              解密后的文本
     */
    public static String decryptByAES(final String base64String, final String password)
    {
        if(null == base64String || null == password || base64String.isEmpty() || password.isEmpty())
            return null;

        return new String(decrypt(Base64.decodeBase64(base64String),password, AES_KEY_ALGORITHM));
    }

    /**
     * 使用AES加密原始字符串.
     *
     * @param input 原始输入字符数组
     * @param key   符合AES要求的密钥,16,24,32字节数组
     * @param iv    初始向量,16字节数组
     */
    public static byte[] encryptByAES(final byte[] input, final byte[] key, final byte[] iv)
    {
        if(null == input || input.length <= 0)
        {
            logger.info("input bytes is not null and input length > 0");
            return null;
        }

        if(null == key || key.length < DEFAULT_AES_KEYSIZE_BYTE) {
            logger.info("key is not null and key length is 128 or 192 or 256 bit");
            return null;
        }

        if(null == iv || iv.length != DEFAULT_AES_IVSIZE_BYTE)
        {
            logger.info("iv is not null and iv length must equals 16");
            return null;
        }
        return aes(input, key, iv, Cipher.ENCRYPT_MODE);
    }

    /**
     * 使用AES解密字符串, 返回原始字符串.
     *
     * @param input Hex编码的加密字符串
     * @param key   符合AES要求的密钥,16,24,32字节数组
     * @param iv    初始向量,16字节数组
     */
    public static String decryptByAES(final byte[] input, final byte[] key, final byte[] iv)
    {
        if(null == input || input.length <= 0)
        {
            logger.info("input bytes is not null and input length > 0");
            return null;
        }

        if(null == key || key.length < DEFAULT_AES_KEYSIZE_BYTE) {
            logger.info("key is not null and key length is 128 or 192 or 256 bit");
            return null;
        }

        byte[] decryptResult = aes(input, key, iv, Cipher.DECRYPT_MODE);
        return new String(decryptResult);
    }

    /**
     * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
     *
     * @param input 原始字节数组
     * @param key   符合AES要求的密钥,16,24,32字节数组
     * @param iv    初始向量,16字节数组
     * @param mode  Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     */
    private static byte[] aes(final byte[] input, final byte[] key, final byte[] iv, final int mode)
    {
        if(null == input || input.length <= 0)
        {
            logger.info("input bytes is not null and input length > 0");
            return null;
        }

        if(null == key || key.length < DEFAULT_AES_KEYSIZE_BYTE) {
            logger.info("key is not null and key length is 128 or 192 or 256 bit");
            return null;
        }
        if(mode == Cipher.ENCRYPT_MODE || mode == Cipher.DECRYPT_MODE) {
            try {
                SecretKey secretKey = new SecretKeySpec(key, AES_KEY_ALGORITHM);

                Cipher cipher;
                SecureRandom sr = new SecureRandom();
                if(null == iv)
                {
                    cipher = Cipher.getInstance(AES_KEY_ALGORITHM);
                    cipher.init(mode, secretKey,sr);
                }
                else
                {
                    if(iv.length != DEFAULT_AES_IVSIZE_BYTE) {
                        logger.info("iv is not null and iv length must be " + DEFAULT_AES_IVSIZE_BYTE);
                        return null;
                    }
                    cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM_CBC);
                    IvParameterSpec ivSpec = new IvParameterSpec(iv);
                    cipher.init(mode, secretKey, ivSpec,sr);
                }

                return cipher.doFinal(input);
            } catch (GeneralSecurityException e) {
                logger.error(e.getMessage());
            }
        }
        else
        {
            logger.info("notFound CipherMode,mode=" + mode);
        }
        return null;
    }

    /**
     * 加密综合方法，支持的算法包括DES,DESede,AES
     * @param input     待加密字节数组
     * @param key       加密Key对象
     * @param symmetricCipherAlgorithm 加密算法,DES,DESede,AES
     * @return          加密后的字节数组
     */
    public static byte[] encrypt(final byte[] input,final String key,final String symmetricCipherAlgorithm)
    {
        Cipher cipher = getCipher(key, symmetricCipherAlgorithm,Cipher.ENCRYPT_MODE);
        if(null == cipher)
            return null;
        try {
            return cipher.doFinal(input);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getMessage(),e);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 解密综合方法，支持的算法包括DES,DESede,AES
     * @param input     待加密字节数组
     * @param key       解密Key对象
     * @param symmetricCipherAlgorithm 解密算法,DES,DESede,AES
     * @return          解密后的字节数组
     */
    public static byte[] decrypt(final byte[] input,final String key,final String symmetricCipherAlgorithm)
    {
        if(null == input || null == key || symmetricCipherAlgorithm == null)
            return null;

        Cipher cipher = getCipher(key, symmetricCipherAlgorithm,Cipher.DECRYPT_MODE);
        if(null == cipher)
            return null;
        try {
            return cipher.doFinal(input);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getMessage(),e);
        } catch (BadPaddingException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public static Cipher getCipher(final String keyString, final String alg, final int opmode)
    {
        if(null == keyString || null == alg || opmode < 1)
            return null;

        String symmetricCipherAlgorithm = alg;
        if(null == symmetricCipherAlgorithm)
        {
            symmetricCipherAlgorithm = AES_KEY_ALGORITHM;
        }

        Cipher cipher = null;

        try {
            SecretKey secretKey = null;
            IvParameterSpec ivParameterSpec = null;
            SecureRandom sr = new SecureRandom();

            if (DES_KEY_ALGORITHM.equals(symmetricCipherAlgorithm)) {
                byte[] keyBytes = crossHashByteArray(keyString,MD5);
                byte[] key = new byte[8];
                byte[] iv = new byte[8];

                System.arraycopy(keyBytes, 0, key, 0, 8);
                System.arraycopy(keyBytes, 8, iv, 0, 8);

                secretKey = new SecretKeySpec(key, DES_KEY_ALGORITHM);
                ivParameterSpec = new IvParameterSpec(iv);

                cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM_CBC);
                cipher.init(opmode, secretKey, ivParameterSpec,sr);
            }
            else if(DESede_KEY_ALGORITHM.equals(symmetricCipherAlgorithm)){
                byte[] keyBytes = crossHashByteArray(keyString,SHA256);
                byte[] key = new byte[24];
                byte[] iv = new byte[8];

                System.arraycopy(keyBytes, 0, key, 0, 24);
                System.arraycopy(keyBytes, 0, iv, 0, 8);

                secretKey = new SecretKeySpec(key, DESede_KEY_ALGORITHM);
                ivParameterSpec = new IvParameterSpec(iv);

                cipher = Cipher.getInstance(DESede_CIPHER_ALGORITHM_CBC);
                cipher.init(opmode, secretKey, ivParameterSpec,sr);
            }
            else
            {
                //AES密钥16位长
                byte[] keyBytes = crossHashByteArray(keyString,SHA256);
                byte[] key = new byte[16];
                byte[] iv = new byte[16];

                System.arraycopy(keyBytes, 0, key, 0, 16);
                System.arraycopy(keyBytes, 0, iv, 0, 16);


                secretKey = new SecretKeySpec(key, AES_KEY_ALGORITHM);
                ivParameterSpec = new IvParameterSpec(iv);

                cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM_CBC);
                cipher.init(opmode, secretKey, ivParameterSpec, sr);
            }

        } catch(InvalidKeyException e){
            logger.error(e.getMessage(),e);
        } catch(InvalidAlgorithmParameterException e){
            logger.error(e.getMessage(),e);
        }
        catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(),e);
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage(),e);
        }
        return cipher;
    }

    /**
     * 根据指定的加密key和指定AES keySize生成AES加密Key字节数组
     * @param key       加密key字符串
     * @param keySize   AES KeySize，可选项为128,192,256 bit
     * @return          AES加密Key字节数组
     */
    public static byte[] generateAesKey(final String key,final int keySize)
    {
        if(null == key || key.isEmpty() || keySize < 128) {
            logger.info("key argument must be a not empty");
            return null;
        }

        StringBuffer keyBuff = new StringBuffer(key);
        byte[] bytes = null;
        //128bit
        if(keySize == 128) {
            bytes = new byte[16];
            System.arraycopy(digest(keyBuff.toString(), MD5, UTF_16LE), 0, bytes, 0, 16);
        }
        //192bit
        else if(keySize == 192) {
            bytes = new byte[24];
            System.arraycopy(digest(keyBuff.toString(), SHA256, UTF_16LE), 0, bytes, 0, 24);
        }
        //256bit
        else if(keySize == 256) {
            bytes = new byte[32];
            System.arraycopy(digest(keyBuff.toString(), SHA256, UTF_16LE), 0, bytes, 0, 32);
        }
        return bytes;
    }

    /**
     * 生成AES密钥
     * @param keysize AES秘钥大小，可选128,192,256 bit
     */
    public static byte[] generateAesKey(final int keysize)
    {
        try
        {
            if(keysize == 128 || keysize == 192 || keysize == 256) {

                KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_KEY_ALGORITHM);
                keyGenerator.init(keysize);
                SecretKey secretKey = keyGenerator.generateKey();
                return secretKey.getEncoded();
            }
            else
            {
                logger.info("notFount keySize,keySize=" + keysize);
            }
        }
        catch (GeneralSecurityException e)
        {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 生成随机向量,默认大小为cipher.getBlockSize(), 16字节.
     */
    public static byte[] generateAesIV()
    {
        byte[] bytes = new byte[DEFAULT_AES_IVSIZE_BYTE];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(bytes);
        return bytes;
    }

    public static byte[] generateAesIV(final String key)
    {
        final byte[] bytes = new byte[DEFAULT_AES_IVSIZE_BYTE];
        System.arraycopy(digest(key,MD5,UTF_16LE),0,bytes,0, DEFAULT_AES_IVSIZE_BYTE);
        return bytes;
    }

    private void KeyInfo(SecretKey secretKey)
    {
        /*if(null != secretKey)
        {
            System.out.println("密钥: " + secretKey);
            System.out.println("密钥长度: " + secretKey.getEncoded().length);
            System.out.println("密钥格式: " + secretKey.getFormat());
            System.out.println("密钥生成算法: " + secretKey.getAlgorithm());
        }*/
    }

    public static void main(String[] args)
    {
        String srcStr = "www.resms.net";


        byte[] data = SecUtil.sha1ByteArray(srcStr.getBytes(UTF_8));
        System.out.println(data.length);


        System.out.println("MD5                 :" + SecUtil.md5(srcStr));
        System.out.println("CustomMD5           :" + SecUtil.cmd5(srcStr));
        System.out.println("CrossMD5            :" + SecUtil.crossHash(srcStr,MD5));
        System.out.println("SHA1                :" + SecUtil.sha1(srcStr));
        System.out.println("crossSHA1           :" + SecUtil.crossHash(srcStr,SHA1));
        System.out.println("SHA256              :" + SecUtil.sha256(srcStr));
        System.out.println("SHA512              :" + digest(srcStr, SHA512,UTF_8).length);


        String aesBase64String = encryptByAES(srcStr);
        System.out.println("encryptByAES        :" + aesBase64String);
        System.out.println("decryptByAES        :" + decryptByAES(aesBase64String));


        String password = "wow";
        String iv = "jintiantianqihaoqinglang";
        String aesBase64String1 = encryptByAES(srcStr,password);
        System.out.println("aesEncrypt1         :" + aesBase64String1);
        System.out.println("aesDecrypt1         :" + decryptByAES(aesBase64String1,password));


        String desBase64String = encryptByDES(srcStr,password);
        System.out.println("desEncrypt          :" + desBase64String);
        System.out.println("desDecrypt          :" + decryptByDES(desBase64String,password));


        String encodeBase64String = Base64.encodeBase64String(encrypt(srcStr.getBytes(UTF_16LE),password, DESede_KEY_ALGORITHM));
        System.out.println("encodeBase64String  :" + encodeBase64String);
        System.out.println("decodeBase64String  :" + new String(decrypt(Base64.decodeBase64(encodeBase64String),password, DESede_KEY_ALGORITHM)));


        String cryptStr = SecUtil.encodeBase64(srcStr,UTF_8);
        System.out.println(cryptStr);
        System.out.println(SecUtil.decodeBase64(cryptStr,UTF_8));
    }
}
