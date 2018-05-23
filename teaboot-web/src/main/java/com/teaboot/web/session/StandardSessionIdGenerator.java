package com.teaboot.web.session;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StandardSessionIdGenerator {
	private int sessionIdLength = 16;
	private final Queue<SecureRandom> randoms = new ConcurrentLinkedQueue<>();

	protected void getRandomBytes(byte bytes[]) {

		SecureRandom random = randoms.poll();
		if (random == null) {
			random = createSecureRandom();
		}
		random.nextBytes(bytes);
		randoms.add(random);
	}

	public String generateSessionId() {

		byte random[] = new byte[16];
		
		StringBuilder buffer = new StringBuilder(2 * sessionIdLength + 20);

		int resultLenBytes = 0;

		while (resultLenBytes < sessionIdLength) {
			getRandomBytes(random);
			for (int j = 0; j < random.length && resultLenBytes < sessionIdLength; j++) {
				byte b1 = (byte) ((random[j] & 0xf0) >> 4);
				byte b2 = (byte) (random[j] & 0x0f);
				if (b1 < 10)
					buffer.append((char) ('0' + b1));
				else
					buffer.append((char) ('A' + (b1 - 10)));
				if (b2 < 10)
					buffer.append((char) ('0' + b2));
				else
					buffer.append((char) ('A' + (b2 - 10)));
				resultLenBytes++;
			}
		}

		return buffer.toString();
	}

	private SecureRandom createSecureRandom() {
		SecureRandom result = null;
		try {
			result = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (result == null) {
			result = new SecureRandom();
		}
		result.nextInt();
		return result;
	}
}
