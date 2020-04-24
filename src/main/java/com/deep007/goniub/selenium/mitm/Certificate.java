package com.deep007.goniub.selenium.mitm;

import java.io.File;

import net.lightbody.bmp.mitm.CertificateAndKeySource;
import net.lightbody.bmp.mitm.KeyStoreFileCertificateSource;
import net.lightbody.bmp.mitm.RootCertificateGenerator;

public class Certificate {

	public static void genarateCertificate(String path, String password) {
		 // create a CA Root Certificate using default settings
	    RootCertificateGenerator rootCertificateGenerator = RootCertificateGenerator.builder().build();
	    rootCertificateGenerator.saveRootCertificateAsPemFile(new File(path + "/certificate.cer"));
	    rootCertificateGenerator.savePrivateKeyAsPemFile(new File(path + "/private-key.pem"), password);
	    // or save the certificate and private key as a PKCS12 keystore, for later use
	    rootCertificateGenerator.saveRootCertificateAndKey("PKCS12", new File(path + "/keystore.p12"), 
	            "privateKeyAlias", password);
	}
	
	public static CertificateAndKeySource getExistCertificateAndKeySource(String path, String password) {
		CertificateAndKeySource existingCertificateSource = 
	            new KeyStoreFileCertificateSource("PKCS12", new File(path + "/keystore.p12"), "privateKeyAlias", password);
		return existingCertificateSource;
	}
}
