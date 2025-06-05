package com.apero.service.provider

import android.util.Base64
import com.apero.service.data.remote.model.SignatureData
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

internal actual class SignatureProvider actual constructor() {
    actual fun parse(
        apiKey: String,
        publicKey: String,
        timestamp: Long
    ): Result<SignatureData> = kotlin.runCatching {
        val signature = encryptSignature(timestamp, apiKey, publicKey)
        return@runCatching SignatureData(
            signature = signature,
            keyId = apiKey,
            timeStamp = timestamp
        )
    }

    private fun encryptSignature(timeStamp: Long, keyId: String, publicKeyStr: String): String {
        val publicKey = readKeySignature(publicKeyStr)
        val planText = "$timeStamp@@@$keyId"
        val cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val data = cipher.doFinal(planText.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(data, Base64.NO_WRAP)
    }

    private fun readKeySignature(publicKey: String): PublicKey? {
        return try {
            val key = getRawPublicKey(publicKey)
            val keyBytes = Base64.decode(key, Base64.NO_WRAP)
            val spec = X509EncodedKeySpec(keyBytes)
            val kf = KeyFactory.getInstance("RSA")
            kf.generatePublic(spec)
        } catch (e: NoSuchAlgorithmException) {
            null
        } catch (e: InvalidKeySpecException) {
            null
        } catch (e: NullPointerException) {
            null
        }
    }

    private fun getRawPublicKey(publicKey: String): String {
        return publicKey
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\r", "")
            .replace("\n", "")
    }
}