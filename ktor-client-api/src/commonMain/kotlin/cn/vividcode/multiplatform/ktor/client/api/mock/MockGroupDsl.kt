package cn.vividcode.multiplatform.ktor.client.api.mock

import cn.vividcode.multiplatform.ktor.client.api.builder.KtorBuilderDsl

/**
 * 项目：vividcode-multiplatform-ktor-client
 *
 * 作者：li-jia-wei
 *
 * 创建：2024/7/1 下午1:34
 *
 * 介绍：MockGroup
 */
@KtorBuilderDsl
interface MockGroupDsl<T : Any> {
	
	var enabled: Boolean
	
	fun mock(block: MockDsl<T>.() -> Unit)
}

internal class MockGroupDslImpl<T : Any> : MockGroupDsl<T> {
	
	val mockModels = mutableMapOf<String, MockModel<*>>()
	
	override var enabled: Boolean = true
	
	override fun mock(block: MockDsl<T>.() -> Unit) {
		val mockDsl = MockDslImpl<T>().apply(block)
		if (mockDsl.enabled) {
			val mock = mockDsl.mock ?: error("${mockDsl.name} 的 mock 必须不为空")
			val delayRange = mockDsl.delay.range
			mockModels[mockDsl.name] = MockModel(delayRange, mock)
		}
		try {
		
		} catch (e: Exception) {
		
		} finally {
		
		}
	}
}