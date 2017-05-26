package com.nms.util;

/**
 * Created by sam on 16-10-3.
 */
public class Hex {
    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String byte2Hex(final byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexString
     * @return
     */
    public static byte[] hex2Bytes(final String hexString) {
        if (hexString.length() < 1)
            return null;
        byte[] result = new byte[hexString.length()/2];
        for (int i = 0;i< hexString.length()/2; i++) {
            int high = Integer.parseInt(hexString.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexString.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
