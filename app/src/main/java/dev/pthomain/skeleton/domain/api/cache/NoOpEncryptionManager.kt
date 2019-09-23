package dev.pthomain.skeleton.domain.api.cache

import uk.co.glass_software.android.mumbo.base.EncryptionManager
import uk.co.glass_software.android.mumbo.base.EncryptionManager.KeyPolicy.ANDROIDX

// EncryptionManager implementation that does nothing, needed to address issues with Robolectric
class NoOpEncryptionManager : EncryptionManager {
    override val isEncryptionAvailable = false
    override val keyPolicy = ANDROIDX

    override fun decrypt(
        toDecrypt: String?,
        dataTag: String,
        password: String?
    ) = null

    override fun decryptBytes(
        toDecrypt: ByteArray?,
        dataTag: String,
        password: String?
    ) = null

    override fun encrypt(
        toEncrypt: String?,
        dataTag: String,
        password: String?
    ) = null

    override fun encryptBytes(
        toEncrypt: ByteArray?,
        dataTag: String,
        password: String?
    ) = null

}
