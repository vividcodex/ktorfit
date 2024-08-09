package cn.vividcode.multiplatform.ktor.client.annotation

/**
 * 项目名称：vividcode-multiplatform-ktor-client
 *
 * 作者昵称：li-jia-wei
 *
 * 创建日期：2024/5/14 22:01
 *
 * 文件介绍：HEAD
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class HEAD(
	val url: String,
	val auth: Boolean = false
)