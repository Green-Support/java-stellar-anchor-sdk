package org.stellar.anchor.model

import io.mockk.every
import io.mockk.mockk
import java.time.Instant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.stellar.anchor.sep24.PojoSep24Transaction
import org.stellar.anchor.sep24.Sep24TransactionBuilder
import org.stellar.anchor.sep24.Sep24TransactionStore

internal class Sep24TransactionTest {
  @Test
  fun `test the Sep24TransactionBuilder builds the Sep24Transaction correctly`() {
    val store = mockk<Sep24TransactionStore>()
    every { store.newInstance() } returns PojoSep24Transaction()
    val builder = Sep24TransactionBuilder(store)
    val instantNow = Instant.now()
    var txn =
      builder
        .transactionId("txnId")
        .completedAt(instantNow)
        .withdrawAnchorAccount("account")
        .memo("memo")
        .amountFee("20")
        .amountInAsset("NATUREUSD_In")
        .amountOutAsset("NATUREUSD_Out")
        .amountFeeAsset("NATUREUSD_Fee")
        .build()

    assertEquals(txn.transactionId, "txnId")
    assertEquals(txn.completedAt, instantNow)
    assertEquals(txn.withdrawAnchorAccount, "account")
    assertEquals(txn.memo, "memo")
    assertEquals(txn.amountFee, "20")
    assertEquals(txn.amountInAsset, "NATUREUSD_In")
    assertEquals(txn.amountOutAsset, "NATUREUSD_Out")
    assertEquals(txn.amountFeeAsset, "NATUREUSD_Fee")
  }
}
