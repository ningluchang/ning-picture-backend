package org.example.yupicturebackend.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordEncryptor {
	// 定义使用的哈希算法,这里使用 SHA-256
	private static final String HASH_ALGORITHM = "SHA-256";

	/**
	 * 生成盐值
	 * @return 盐值的base64编码字符串
	 */
	public static String generateSalt(){
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	/**
	 * 使用盐值对密码进行加密
	 * @param password 明文密码
	 * @param salt 盐值的base64编码字符串
	 * @return 加密后的密码的base64编码字符串
	 */
	public static String encryptPassword(String password,String salt){
		try {
			// 将base64编码的盐值解码为字节数组
			byte[] saltBytes = Base64.getDecoder().decode(salt);
			// 获取指定算法的MessageDigest对象
			MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
			// 将盐值添加到摘要中
			md.update(saltBytes);
			// 将密码转换为字节数组
			byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
			// 对密码字节数组进行哈希计算
			byte[] hash = md.digest(passwordBytes);
			// 将哈希结果转换为base64编码的字符串并返回
			return Base64.getEncoder().encodeToString(hash);
		}catch (NoSuchAlgorithmException e){
			throw new RuntimeException("密码加密失败");
		}
	}

	/**
	 * 验证密码是否匹配
	 * @param rawPassword 明文密码
	 * @param encryptedPassword 加密后的密码的base64编码字符串
	 * @param salt 盐值的base64编码字符串
	 * @return 密码是否匹配
	 */
	public static boolean verifyPassword(String rawPassword,String encryptedPassword,String salt){
		// 对明文密码使用相同盐值进行加密
		String newEncryptedPassword = encryptPassword(rawPassword,salt);
		// 比较加密后的密码是否相同
		return newEncryptedPassword.equals(encryptedPassword);
	}
}
