package helper;

import java.util.Base64;

public class Helper {
	public static String setAuthorizationBase64Encoded() {
		String authString = Constants.USER + ":" + Constants.PWD;
		byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		
		return authStringEnc;
	}
}
