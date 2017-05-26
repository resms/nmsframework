/**
 * 
 */
package com.nms.util.security;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

/**
 * 公钥加密与解密
 * 
 * @author ServerManager
 * 
 */
public final class TestDHKey
{
	
	public static void main(String argv[])
	{
		try
		{
			final TestDHKey my = new TestDHKey();
			my.run();
		}
		catch(final Exception e)
		{
			System.err.println(e);
		}
	}
	
	/**
	 * 
	 */
	public TestDHKey()
	{
	// TODO Auto-generated constructor stub
	}
	
	private void run() throws Exception
	{
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		System.out.println("ALICE: 产生 DH 对 ...");
		final KeyPairGenerator aliceKpairGen = KeyPairGenerator.getInstance("DH");
		aliceKpairGen.initialize(512);
		final KeyPair aliceKpair = aliceKpairGen.generateKeyPair(); // 生成时间长
		// 张三(Alice)生成公共密钥 alicePubKeyEnc 并发送给李四(Bob) ,
		// 比如用文件方式,socket.....
		final byte[] alicePubKeyEnc = aliceKpair.getPublic().getEncoded();
		// bob接收到alice的编码后的公钥,将其解码
		final KeyFactory bobKeyFac = KeyFactory.getInstance("DH");
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(alicePubKeyEnc);
		final PublicKey alicePubKey = bobKeyFac.generatePublic(x509KeySpec);
		System.out.println("alice公钥bob解码成功");
		// bob必须用相同的参数初始化的他的DH KEY对,所以要从Alice发给他的公开密钥,
		// 中读出参数,再用这个参数初始化他的 DH key对
		// 从alicePubKye中取alice初始化时用的参数
		final DHParameterSpec dhParamSpec = ((DHPublicKey)alicePubKey).getParams();
		final KeyPairGenerator bobKpairGen = KeyPairGenerator.getInstance("DH");
		bobKpairGen.initialize(dhParamSpec);
		final KeyPair bobKpair = bobKpairGen.generateKeyPair();
		System.out.println("BOB: 生成 DH key 对成功");
		final KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DH");
		bobKeyAgree.init(bobKpair.getPrivate());
		System.out.println("BOB: 初始化本地key成功");
		// 李四(bob) 生成本地的密钥 bobDesKey
		bobKeyAgree.doPhase(alicePubKey,true);
		final SecretKey bobDesKey = bobKeyAgree.generateSecret("DES");
		System.out.println("BOB: 用alice的公钥定位本地key,生成本地DES密钥成功");
		// Bob生成公共密钥 bobPubKeyEnc 并发送给Alice,
		// 比如用文件方式,socket.....,使其生成本地密钥
		final byte[] bobPubKeyEnc = bobKpair.getPublic().getEncoded();
		System.out.println("BOB向ALICE发送公钥");
		// alice接收到 bobPubKeyEnc后生成bobPubKey
		// 再进行定位,使aliceKeyAgree定位在bobPubKey
		final KeyFactory aliceKeyFac = KeyFactory.getInstance("DH");
		x509KeySpec = new X509EncodedKeySpec(bobPubKeyEnc);
		final PublicKey bobPubKey = aliceKeyFac.generatePublic(x509KeySpec);
		System.out.println("ALICE接收BOB公钥并解码成功");
		;
		final KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DH");
		aliceKeyAgree.init(aliceKpair.getPrivate());
		System.out.println("ALICE: 初始化本地key成功");
		aliceKeyAgree.doPhase(bobPubKey,true);
		// 张三(alice) 生成本地的密钥 aliceDesKey
		final SecretKey aliceDesKey = aliceKeyAgree.generateSecret("DES");
		System.out.println("ALICE: 用bob的公钥定位本地key,并生成本地DES密钥");
		if(aliceDesKey.equals(bobDesKey))
		{
			System.out.println("张三和李四的密钥相同");
		}
		// 现在张三和李四的本地的deskey是相同的所以,完全可以进行发送加密,接收后解密,达到
		// 安全通道的的目的
		/*
		 * bob用bobDesKey密钥加密信息
		 */
		final Cipher bobCipher = Cipher.getInstance("DES");
		bobCipher.init(Cipher.ENCRYPT_MODE,bobDesKey);
		final String bobinfo = "这是李四的机密信息";
		System.out.println("李四加密前原文:" + bobinfo);
		final byte[] cleartext = bobinfo.getBytes();
		final byte[] ciphertext = bobCipher.doFinal(cleartext);
		/*
		 * alice用aliceDesKey密钥解密
		 */
		final Cipher aliceCipher = Cipher.getInstance("DES");
		aliceCipher.init(Cipher.DECRYPT_MODE,aliceDesKey);
		final byte[] recovered = aliceCipher.doFinal(ciphertext);
		System.out.println("alice解密bob的信息:" + (new String(recovered)));
		if(!java.util.Arrays.equals(cleartext,recovered))
		{
			throw new Exception("解密后与原文信息不同");
		}
		System.out.println("解密后相同");
	}
	
}
