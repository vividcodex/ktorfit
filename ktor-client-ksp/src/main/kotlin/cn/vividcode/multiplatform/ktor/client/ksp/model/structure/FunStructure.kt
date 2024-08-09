package cn.vividcode.multiplatform.ktor.client.ksp.model.structure

import cn.vividcode.multiplatform.ktor.client.ksp.model.model.FunctionModel
import cn.vividcode.multiplatform.ktor.client.ksp.model.model.ParameterModel
import cn.vividcode.multiplatform.ktor.client.ksp.model.model.ValueParameterModel

/**
 * 项目名称：vividcode-multiplatform-ktor-client
 *
 * 作者昵称：li-jia-wei
 *
 * 创建日期：2024/7/1 15:48
 *
 * 文件介绍：FunStructure
 */
internal data class FunStructure(
	val funName: String,
	val returnStructure: ReturnStructure,
	val parameterModels: List<ParameterModel>,
	val functionModels: List<FunctionModel>,
	val valueParameterModels: List<ValueParameterModel>
)