package com.shd.codestudio.handlers

// import com.shd.codestudio.lsp.clang.ClangLanguageServer || planned for v..03
import android.content.Context
import com.shd.codestudio.lsp.api.ILanguageClient
import com.shd.codestudio.lsp.api.ILanguageServerRegistry
import com.shd.codestudio.lsp.java.JavaLanguageServer
import com.shd.codestudio.lsp.clang.ClangLanguageServer
import com.shd.codestudio.lsp.kotlin.KotlinLanguageServer
import com.shd.codestudio.lsp.xml.XMLLanguageServer

/** @author Akash Yadav */
object LspHandler {

  fun registerLanguageServers(context: Context) {
    ILanguageServerRegistry.getDefault().apply {
      getServer(JavaLanguageServer.SERVER_ID) ?: register(JavaLanguageServer())
      getServer(KotlinLanguageServer.SERVER_ID) ?: register(KotlinLanguageServer(context))
      getServer(ClangLanguageServer.SERVER_ID) ?: register(ClangLanguageServer(context))
      getServer(XMLLanguageServer.SERVER_ID) ?: register(XMLLanguageServer())
    }
  }

  fun connectClient(client: ILanguageClient) {
    ILanguageServerRegistry.getDefault().connectClient(client)
  }

  fun destroyLanguageServers(isConfigurationChange: Boolean) {
    if (isConfigurationChange) {
      return
    }
    ILanguageServerRegistry.getDefault().destroy()
  }
}
