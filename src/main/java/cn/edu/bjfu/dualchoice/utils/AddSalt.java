package cn.edu.bjfu.dualchoice.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AddSalt {
    public static String hashPasswordWithSalt(String pwd)
    {
        byte[] FIXED_SALT = new byte[] { // ����һ���̶�����ֵ
                (byte) 0xA5, (byte) 0xB1, (byte) 0xC3, (byte) 0xD4,
                (byte) 0xE5, (byte) 0xF6, (byte) 0x07, (byte) 0x18,
                (byte) 0x29, (byte) 0x3A, (byte) 0x4B, (byte) 0x5C,
                (byte) 0x6D, (byte) 0x7E, (byte) 0x8F, (byte) 0x90
        };
        try {

            // ����һ��SHA-256��MessageDigestʵ��
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // ���������ֵ���й�ϣ
            md.update(FIXED_SALT);
            byte[] hashedPassword = md.digest(pwd.getBytes());

            // ���̶�����ֵ�͹�ϣֵ�ϲ���������ΪBase64
            byte[] saltAndHash = new byte[FIXED_SALT.length + hashedPassword.length];
            System.arraycopy(FIXED_SALT, 0, saltAndHash, 0, FIXED_SALT.length);
            System.arraycopy(hashedPassword, 0, saltAndHash, FIXED_SALT.length, hashedPassword.length);
            return Base64.getEncoder().encodeToString(saltAndHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not found", e);
        }
    }
}
