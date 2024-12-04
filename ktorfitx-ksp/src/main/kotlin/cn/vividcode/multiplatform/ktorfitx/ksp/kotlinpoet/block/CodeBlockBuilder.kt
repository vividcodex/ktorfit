package cn.vividcode.multiplatform.ktorfitx.ksp.kotlinpoet.block

import cn.vividcode.multiplatform.ktorfitx.ksp.check.checkWithPathNotFound
import cn.vividcode.multiplatform.ktorfitx.ksp.expends.isHttpOrHttps
import cn.vividcode.multiplatform.ktorfitx.ksp.expends.simpleName
import cn.vividcode.multiplatform.ktorfitx.ksp.kotlinpoet.ReturnTypes
import cn.vividcode.multiplatform.ktorfitx.ksp.model.model.*
import cn.vividcode.multiplatform.ktorfitx.ksp.model.structure.ClassStructure
import cn.vividcode.multiplatform.ktorfitx.ksp.model.structure.FunStructure
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import kotlin.reflect.KClass

/**
 * 项目名称：ktorfitx
 *
 * 作者昵称：li-jia-wei
 *
 * 创建日期：2024/8/14 17:24
 *
 * 文件介绍：CodeBlockBuilder
 */
internal class CodeBlockBuilder(
	private val classStructure: ClassStructure,
	private val funStructure: FunStructure,
	private val codeBlockKClass: KClass<out ClientCodeBlock>,
) {
	
	private val returnStructure = funStructure.returnStructure
	private val valueParameterModels = funStructure.valueParameterModels
	private val functionModels = funStructure.functionModels
	
	private companion object {
		private val exceptionClassNames = arrayOf(
			ClassName.bestGuess("kotlin.Exception"),
			ClassName.bestGuess("java.lang.Exception"),
		)
	}
	
	fun CodeBlock.Builder.buildCodeBlock() = with(getClientCodeBlock()) {
		buildExceptionCodeBlock {
			val apiModel = functionModels.first { it is ApiModel } as ApiModel
			val funName = apiModel.requestFunName
			val fullUrl = parseToFullUrl(apiModel.url)
			val isNeedClientBuilder = isNeedClientBuilder()
			buildClientCodeBlock(funName, fullUrl, isNeedClientBuilder) {
				val bearerAuth = functionModels.any { it is BearerAuthModel }
				if (bearerAuth) {
					buildBearerAuthCodeBlock()
				}
				val headersModel = functionModels.find { it is HeadersModel } as? HeadersModel
				val headerModels = valueParameterModels.filterIsInstance<HeaderModel>()
				if (headerModels.isNotEmpty() || headersModel != null) {
					buildHeadersCodeBlock(headersModel, headerModels)
				}
				val queryModels = valueParameterModels.filterIsInstance<QueryModel>()
				if (queryModels.isNotEmpty()) {
					buildQueriesCodeBlock(queryModels)
				}
				val formModels = valueParameterModels.filterIsInstance<FormModel>()
				if (formModels.isNotEmpty()) {
					buildFormsCodeBlock(formModels)
				}
				if (this@with is MockClientCodeBlock) {
					val pathModels = valueParameterModels.filterIsInstance<PathModel>()
					if (pathModels.isNotEmpty()) {
						buildPathsCodeBlock(pathModels)
					}
				}
				val bodyModel = valueParameterModels.find { it is BodyModel } as? BodyModel
				if (bodyModel != null) {
					UseImports += bodyModel.typeQualifiedName
					buildBodyCodeBlock(bodyModel)
				}
			}
		}
	}
	
	private fun CodeBlock.Builder.buildExceptionCodeBlock(
		builder: CodeBlock.Builder.() -> Unit,
	) {
		beginControlFlow(if (returnStructure.rawType != ReturnTypes.unitClassName) "return try" else "try")
		builder()
		val exceptionListenerModels = functionModels.filterIsInstance<ExceptionListenerModel>()
		exceptionListenerModels.forEach {
			UseImports += it.exceptionTypeName
			UseImports += it.listenerClassName
			nextControlFlow("catch (e: ${it.exceptionTypeName.simpleName})")
			beginControlFlow("with(${it.listenerClassName.simpleName})")
			val superinterfaceName = classStructure.superinterface.simpleName
			val funName = funStructure.funName
			addStatement("$superinterfaceName::$funName.onExceptionListener(e)")
			endControlFlow()
			if (it.returnTypeName == ReturnTypes.unitClassName) {
				buildExceptionReturnCodeBlock()
			}
		}
		if (exceptionListenerModels.all { it.exceptionTypeName !in exceptionClassNames }) {
			if (returnStructure.rawType == ReturnTypes.resultBodyClassName) {
				nextControlFlow("catch (e: Exception)")
			} else {
				nextControlFlow("catch (_: Exception)")
			}
			buildExceptionReturnCodeBlock()
		}
		endControlFlow()
	}
	
	private fun CodeBlock.Builder.buildExceptionReturnCodeBlock() {
		if (returnStructure.isNullable) {
			addStatement("null")
			return
		}
		when (returnStructure.rawType) {
			ReturnTypes.resultBodyClassName -> {
				addStatement("ResultBody.exception(e)")
			}
			
			ReturnTypes.byteArrayClassName -> {
				addStatement("ByteArray(0)")
			}
			
			ReturnTypes.stringClassName -> {
				addStatement("\"\"")
			}
		}
	}
	
	private fun getClientCodeBlock(): ClientCodeBlock {
		return when (this.codeBlockKClass) {
			HttpClientCodeBlock::class -> {
				HttpClientCodeBlock(classStructure.className, returnStructure)
			}
			
			MockClientCodeBlock::class -> {
				val mockModel = funStructure.functionModels.first { it is MockModel } as MockModel
				MockClientCodeBlock(classStructure.className, mockModel)
			}
			
			else -> error("不支持的类型")
		}
	}
	
	private fun parseToFullUrl(url: String): String {
		val pathModels = valueParameterModels.filterIsInstance<PathModel>()
		val initialUrl = if (url.isHttpOrHttps()) url else classStructure.apiStructure.url + url
		val fullUrl = pathModels.fold(initialUrl) { acc, it ->
			with(it.valueParameter) {
				this.checkWithPathNotFound(acc, it.name, funStructure.funName, it.varName)
			}
			acc.replace("{${it.name}}", "\${${it.varName}}")
		}
		return fullUrl
	}
	
	private fun isNeedClientBuilder(): Boolean {
		return valueParameterModels.any { it !is PathModel } || functionModels.any { it is BearerAuthModel || it is HeadersModel }
	}
}