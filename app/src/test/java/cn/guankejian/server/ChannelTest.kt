package cn.guankejian.server

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import org.junit.Test

import kotlinx.coroutines.test.runTest
import kotlin.coroutines.coroutineContext

import kotlinx.coroutines.launch
class ChannelTest {
	@Test
	fun main() = runTest {
		val channel = Channel<Int>()
		launch(coroutineContext){
			repeat(10){
				println("Sending $it")
				channel.send(it)
			}
		}

		launch{
			repeat(10){
				System.err.println("Receive ${channel.receive()}")
			}///

	@Test
	fun main2() = runTest {
		val channel = Channel<Int>(2)
		launch(coroutineContext){
			repeat(6){
				delay(50)
				println("Sending $it")
				channel.send(it)
			}
		}

		launch{
			delay(20_000)
			repeat(6){
				System.err.println("Receive ${channel.receive()}")
			}
		}

	}
}