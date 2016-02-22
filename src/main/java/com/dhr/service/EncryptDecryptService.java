package com.dhr.service;

import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncryptDecryptService implements IEncryportDecryptor {

	private final TextEncryptor textEncryptor;

	@Autowired
	public EncryptDecryptService(TextEncryptor textEncryptor) {
	this.textEncryptor = textEncryptor;
	}
	
	/***
	 * Decrypts the encryptedMessage
	 */
	@Override
	public String decrypt(String encryptedMessage) {
	return textEncryptor.decrypt(encryptedMessage);
	}

	/***
	 * Encrypts message
	 */
	@Override
	public String encrypt(String message) {
	return textEncryptor.encrypt(message);
	}



}
