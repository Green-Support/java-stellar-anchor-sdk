package org.stellar.anchor.dto.sep38

import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.stellar.anchor.api.sep.AssetInfo
import org.stellar.anchor.api.sep.sep38.InfoResponse
import org.stellar.anchor.asset.DefaultAssetService

class InfoResponseTest {
  private lateinit var assets: List<AssetInfo>

  @BeforeEach
  fun setUp() {
    val rjas = DefaultAssetService.fromResource("test_assets.json")
    assets = rjas.listAllAssets()
    assertEquals(3, assets.size)
  }

  @AfterEach
  fun teardown() {
    clearAllMocks()
    unmockkAll()
  }

  @Test
  fun `test the InfoResponse construction`() {
    val infoResponse = InfoResponse(assets)
    assertEquals(3, infoResponse.assets.size)

    val assetMap = HashMap<String, InfoResponse.Asset>()
    infoResponse.assets.forEach { assetMap[it.asset] = it }
    assertEquals(3, assetMap.size)

    val stellarNATUREUSD =
      assetMap["stellar:NATUREUSD:GA3BJUBNOIHANBJEKZFSQTCRB5CUQ4GSENQHVC5QNZGGSK3ILAZS6ATK"]
    assertNotNull(stellarNATUREUSD)
    assertNull(stellarNATUREUSD!!.countryCodes)
    assertNull(stellarNATUREUSD.sellDeliveryMethods)
    assertNull(stellarNATUREUSD.buyDeliveryMethods)
    var wantAssets =
      listOf(
        "iso4217:USD",
        "stellar:NATURENGN:GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5"
      )
    assertTrue(stellarNATUREUSD.exchangeableAssetNames.containsAll(wantAssets))
    assertTrue(wantAssets.containsAll(stellarNATUREUSD.exchangeableAssetNames))

    val stellarNATURENGN =
      assetMap["stellar:NATURENGN:GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5"]
    assertNotNull(stellarNATURENGN)
    assertNull(stellarNATURENGN!!.countryCodes)
    assertNull(stellarNATURENGN.sellDeliveryMethods)
    assertNull(stellarNATURENGN.buyDeliveryMethods)
    wantAssets =
      listOf(
        "iso4217:USD",
        "stellar:NATUREUSD:GA3BJUBNOIHANBJEKZFSQTCRB5CUQ4GSENQHVC5QNZGGSK3ILAZS6ATK"
      )
    assertTrue(stellarNATURENGN.exchangeableAssetNames.containsAll(wantAssets))
    assertTrue(wantAssets.containsAll(stellarNATURENGN.exchangeableAssetNames))

    val fiatUSD = assetMap["iso4217:USD"]
    assertNotNull(fiatUSD)
    assertEquals(listOf("USA"), fiatUSD!!.countryCodes)
    val wantSellDeliveryMethod =
      AssetInfo.Sep38Operation.DeliveryMethod(
        "WIRE",
        "Send USD directly to the Anchor's bank account."
      )
    assertEquals(listOf(wantSellDeliveryMethod), fiatUSD.sellDeliveryMethods)
    val wantBuyDeliveryMethod =
      AssetInfo.Sep38Operation.DeliveryMethod(
        "WIRE",
        "Have USD sent directly to your bank account."
      )
    assertEquals(listOf(wantBuyDeliveryMethod), fiatUSD.buyDeliveryMethods)
    wantAssets =
      listOf(
        "stellar:NATURENGN:GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5",
        "stellar:NATUREUSD:GA3BJUBNOIHANBJEKZFSQTCRB5CUQ4GSENQHVC5QNZGGSK3ILAZS6ATK"
      )
    assertTrue(fiatUSD.exchangeableAssetNames.containsAll(wantAssets))
    assertTrue(wantAssets.containsAll(fiatUSD.exchangeableAssetNames))
  }
}
