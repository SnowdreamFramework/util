package com.github.snowdream.util

import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap

class FileUtil private constructor() {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        /**
         * Get the MimeTypes for the url.

         * @param url the url or uri
         * *
         * @return MimeTypes
         */
        @JvmStatic fun getMimeTypeFromUrl(url: String): String {
            return getMimeTypeFromUrlByExtension(url)
        }

        /**
         * Get the MimeTypes for the url by extension.

         * @param url
         * *
         * @return
         */
        private fun getMimeTypeFromUrlByExtension(url: String): String {
            var mimeType = ""

            if (TextUtils.isEmpty(url)) {
                return mimeType
            }

            val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.encode(url))
            if (!TextUtils.isEmpty(extension)) {
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }

            return mimeType
        }
    }

}
