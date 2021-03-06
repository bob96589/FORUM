package com.bob.webpush;


import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Subscription implements Cloneable {

	public Subscription() {
		// Add BouncyCastle as an algorithm provider
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	private String auth;

	private String key;

	private String endpoint;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getAuth() {
		return auth;
	}

	/**
	 * Returns the base64 encoded auth string as a byte[]
	 */
	public byte[] getAuthAsBytes() {
		return Base64.getDecoder().decode(getAuth());
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	/**
	 * Returns the base64 encoded public key string as a byte[]
	 */
	public byte[] getKeyAsBytes() {
		return Base64.getDecoder().decode(getKey());
	}

	/**
	 * Returns the base64 encoded public key as a PublicKey object
	 */
	public PublicKey getUserPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
		KeyFactory kf = KeyFactory.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
		ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
		ECPoint point = ecSpec.getCurve().decodePoint(getKeyAsBytes());
		ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);

		return kf.generatePublic(pubSpec);
	}

	@Override
	public Subscription clone() throws CloneNotSupportedException {
		return (Subscription) super.clone();
	}


}
