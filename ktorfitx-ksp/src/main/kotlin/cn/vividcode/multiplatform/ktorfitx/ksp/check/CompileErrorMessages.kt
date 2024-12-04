package cn.vividcode.multiplatform.ktorfitx.ksp.check

internal const val COMPILE_ERROR_BODY_SIZE = "%s 方法的参数列表中不允许使用多个 @Body 注解"

internal const val COMPILE_ERROR_BODY_TYPE = "%s 方法的参数列表中标记了 @Body 注解，但是未找到参数类型"

internal const val COMPILE_ERROR_NOT_FOUND_REQUEST_METHOD = "%s 方法缺少请求类型，请使用以下请求类型类型："

internal const val COMPILE_ERROR_MULTIPLE_REQUEST_METHOD = "%s 方法只允许使用一种请求方法，而你使用了 %s %d 个"

internal const val COMPILE_ERROR_URL_REGEX_MESSAGE = "%s 方法上 %s 注解的 url 参数格式错误"

internal const val COMPILE_USE_BOTH_BODY_AND_FORM = "%s 方法不能同时使用 @Body 和 @Form 注解"

internal const val COMPILE_ERROR_PATH_NOT_FOUND = "%s 方法上 %s 参数上的 @Path 注解的 name 参数没有在 url 上找到"

internal const val COMPILE_ERROR_PARAMETER_NOT_FOUND_ANNOTATION = "%s 方法上 %s 参数未使用任何功能注解"

internal const val COMPILE_ERROR_PARAMETER_MULTIPLE_ANNOTATIONS = "%s 方法上 %s 参数不允许同时使用 %s 多个注解"

internal const val COMPILE_ERROR_PARAMETER_VAR_NAME_FORMAT = "%s 方法上 %s 参数不符合小驼峰命名规则，建议修改为 %s"

internal const val COMPILE_ERROR_KTORFITX = "\nKtorfitx 编译期错误检查：%s"