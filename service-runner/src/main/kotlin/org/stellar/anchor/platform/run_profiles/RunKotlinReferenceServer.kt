@file:JvmName("RunKotlinReferenceServer")

package org.stellar.anchor.platform.run_profiles

import kotlinx.coroutines.runBlocking
import org.stellar.anchor.platform.TestConfig
import org.stellar.anchor.platform.TestProfileExecutor
import org.stellar.anchor.platform.testProfileExecutor

fun main() = runBlocking {
  testProfileExecutor = TestProfileExecutor(TestConfig(profileName = "default"))
  registerShutdownHook(testProfileExecutor)
  testProfileExecutor.start(true) {
    it.env["run_docker"] = "false"
    it.env["run_all_servers"] = "false"
    it.env["run_kotlin_reference_server"] = "true"
  }
}
