package cn.vividcode.multiplatform.ktorfit.ksp.visitor.resolver

import cn.vividcode.multiplatform.ktorfit.annotation.Encrypt
import cn.vividcode.multiplatform.ktorfit.annotation.Header
import cn.vividcode.multiplatform.ktorfit.ksp.expends.getAnnotationByType
import cn.vividcode.multiplatform.ktorfit.ksp.model.EncryptInfo
import cn.vividcode.multiplatform.ktorfit.ksp.model.model.HeaderModel
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * 项目名称：vividcode-multiplatform-ktorfit
 *
 * 作者昵称：li-jia-wei
 *
 * 创建日期：2024/7/9 22:12
 *
 * 文件介绍：HeaderModelsResolver
 */
@Suppress("unused")
internal data object HeaderModelsResolver : ValueParameterModelResolver<HeaderModel> {
	
	private val encryptClassName by lazy { arrayOf(String::class.asClassName(), ByteArray::class.asClassName()) }
	
	override fun KSFunctionDeclaration.resolve(): List<HeaderModel> {
		return this.parameters.mapNotNull {
			val header = it.getAnnotationByType(Header::class) ?: return@mapNotNull null
			val varName = it.name!!.asString()
			val name = header.name.ifBlank {
				varName.replace("([a-z])([A-Z])".toRegex()) {
					"${it.groupValues[1]}-${it.groupValues[2]}"
				}.replaceFirstChar { it.uppercase() }
			}
			val encryptInfo = it.getAnnotationByType(Encrypt::class)?.let {
				EncryptInfo(it.encryptType, it.layer)
			}
			val className = (it.type.resolve().declaration as KSClassDeclaration).toClassName()
			check(encryptInfo == null || className in encryptClassName) {
				"${className.simpleName} 不允许使用 @Encrypt 注解"
			}
			HeaderModel(name, varName, encryptInfo)
		}
	}
}