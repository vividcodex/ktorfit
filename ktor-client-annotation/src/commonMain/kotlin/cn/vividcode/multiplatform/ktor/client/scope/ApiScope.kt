package cn.vividcode.multiplatform.ktor.client.scope

/**
 * 项目名称：vividcode-multiplatform-ktor-client
 *
 * 作者昵称：li-jia-wei
 *
 * 创建日期：2024/8/4 23:40
 *
 * 文件介绍：Api 作用域
 */
interface ApiScope {
	
	/**
	 * Api 作用域名称
	 */
	val name: String
		get() = this::class.simpleName!!
	
	/**
	 * Api 作用域声明，第一次使用打印，可通过配置关闭
	 */
	val declaration: String
		get() = "这是 ${this::class.simpleName!!}"
}

/**
 * 默认的 ApiScope
 */
object DefaultApiScope : ApiScope {
	
	override val name: String = "默认的接口作用域"
	
	override val declaration: String = "默认的接口作用域，不建议使用"
}