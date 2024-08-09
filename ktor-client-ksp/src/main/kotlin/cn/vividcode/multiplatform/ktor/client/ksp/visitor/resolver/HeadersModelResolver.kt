package cn.vividcode.multiplatform.ktor.client.ksp.visitor.resolver

import cn.vividcode.multiplatform.ktor.client.annotation.Headers
import cn.vividcode.multiplatform.ktor.client.ksp.expends.getAnnotationByType
import cn.vividcode.multiplatform.ktor.client.ksp.model.model.HeadersModel
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

/**
 * 项目名称：vividcode-multiplatform-ktor-client
 *
 * 作者昵称：li-jia-wei
 *
 * 创建日期：2024/7/2 22:22
 *
 * 文件介绍：HeadersModelResolver
 */
@Suppress("unused")
internal data object HeadersModelResolver : FunctionModelResolver<HeadersModel> {
	
	private val headersRegex = "^([^:=]+)[:=]([^:=]+)$".toRegex()
	
	override fun KSFunctionDeclaration.resolve(): HeadersModel? {
		val headers = getAnnotationByType(Headers::class)?.let {
			it.headers.toSet() + it.header
		} ?: return null
		return headers.associate {
			val (name, value) = headersRegex.matchEntire(it)?.destructured
				?: error("${qualifiedName!!.asString()} 方法的 @Headers 格式错误")
			name.trim() to value.trim()
		}.let { HeadersModel(it) }
	}
}