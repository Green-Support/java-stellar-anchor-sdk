package org.stellar.anchor

import java.nio.file.Files
import java.nio.file.Path
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.stellar.anchor.config.Sep1Config.TomlType.*
import org.stellar.anchor.platform.config.PropertySep1Config
import org.stellar.anchor.platform.config.Sep1ConfigTest
import org.stellar.anchor.sep1.Sep1Service

class Sep1ServiceTest {
  lateinit var sep1: Sep1Service
  val stellarToml =
    """
          ACCOUNTS = [ "GAIXTJGANTERAERDZN72GJTAU4YYEFH2HZNKDDBWQKFY7W6LC4WLLVHV" ]
          VERSION = "0.1.0"
          SIGNING_KEY = "GCJIKEKFS5YQP6OE7E6AALGD53NKFOHO6G7TGSMAB4X264SIFDOIVHP5"
          NETWORK_PASSPHRASE = "Test SDF Network ; September 2015"
          
          WEB_AUTH_ENDPOINT = "https://paygod.godstabernacle.xyz:8080/auth"
          KYC_SERVER = "https://paygod.godstabernacle.xyz:8080/sep12"
          TRANSFER_SERVER_SEP0024 = "https://paygod.godstabernacle.xyz:8080/sep24"
          DIRECT_PAYMENT_SERVER = "https://paygod.godstabernacle.xyz:8080/sep31"
          ANCHOR_QUOTE_SERVER = "https://paygod.godstabernacle.xyz:8080/sep38"
          
          [[CURRENCIES]]
          code = "NATUREUSD"
          issuer = "GA3BJUBNOIHANBJEKZFSQTCRB5CUQ4GSENQHVC5QNZGGSK3ILAZS6ATK"
          status = "test"
          is_asset_anchored = false
          anchor_asset_type = "other"
          desc = "A fake anchored asset to use with this example anchor server."
          
          [DOCUMENTATION]
          ORG_NAME = "Stellar Development Foundation"
          ORG_URL = "https://paygod.godstabernacle.xyz"
          ORG_DESCRIPTION = "SEP 24 reference server."
          ORG_KEYBASE = "stellar.public"
          ORG_TWITTER = "StellarOrg"
          ORG_GITHUB = "stellar"
        """
      .trimIndent()

  @Test
  fun `test Sep1Service reading toml from inline string`() {

    val config = PropertySep1Config(true, STRING, stellarToml)
    sep1 = Sep1Service(config)
    assertEquals(sep1.stellarToml, stellarToml)
  }

  @Test
  fun `test Sep1Service reading toml from file`() {
    val config = PropertySep1Config(true, FILE, Sep1ConfigTest.getTestTomlAsFile())
    sep1 = Sep1Service(config)
    assertEquals(sep1.stellarToml, Files.readString(Path.of(Sep1ConfigTest.getTestTomlAsFile())))
  }

  @Test
  fun `test Sep1Service reading toml from url`() {
    val mockServer = MockWebServer()
    mockServer.start()
    val mockAnchorUrl = mockServer.url("").toString()
    mockServer.enqueue(MockResponse().setBody(stellarToml))

    val config = PropertySep1Config(true, URL, mockAnchorUrl)
    sep1 = Sep1Service(config)
    assertEquals(sep1.stellarToml, stellarToml)
  }
}
