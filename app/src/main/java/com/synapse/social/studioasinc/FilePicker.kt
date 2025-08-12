package com.synapse.social.studioasinc

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class FilePicker(
    private val registry: ActivityResultRegistry,
    private val lifecycleOwner: LifecycleOwner,
    private val onFileSelected: (Uri?) -> Unit
) {

    private lateinit var getContentLauncher: ActivityResultLauncher<String>
    private lateinit var openDocumentLauncher: ActivityResultLauncher<Array<String>>

    init {
        register()
    }

    private fun register() {
        getContentLauncher = registry.register("get_content", lifecycleOwner, ActivityResultContracts.GetContent()) { uri: Uri? ->
            onFileSelected(uri)
        }
        openDocumentLauncher = registry.register("open_document", lifecycleOwner, ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            onFileSelected(uri)
        }
    }

    fun pickFile(mimeType: String) {
        getContentLauncher.launch(mimeType)
    }

    fun openDocument(mimeTypes: Array<String>) {
        openDocumentLauncher.launch(mimeTypes)
    }
}
