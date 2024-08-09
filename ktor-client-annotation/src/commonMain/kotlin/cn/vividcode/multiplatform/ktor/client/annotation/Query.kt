package cn.vividcode.multiplatform.ktor.client.annotation

/**
 * 项目名称：vividcode-multiplatform
 *
 * 作者昵称：li-jia-wei
 *
 * 创建日期：2024/3/23 21:09
 *
 * 文件介绍：Query
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class Query(val name: String = "")