package com.keeghan.traidr.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun hashPassword(password: String): String {
    try {
        // Create a MessageDigest instance for SHA-256
        val digest = MessageDigest.getInstance("SHA-256")

        // Get the password bytes and compute the hash
        val bytes = password.toByteArray()
        val hashBytes = digest.digest(bytes)

        // Convert the hash bytes to a hex string
        val hexString = StringBuilder()
        for (i in hashBytes.indices) {
            val hex = Integer.toHexString(0xff and hashBytes[i].toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        // Handle the exception
        throw RuntimeException(e)
    }
}
