package com.khb.mvvmmygithub.http

/**
 *创建时间：2019/11/8
 *编写人：kanghb
 *功能描述：密封类是一个抽象类。
密封类的所有子类都要么在密封类中，要么跟密封类在同一文件中。
密封类子类的子类，则可以在任何位置。
跟 when 表达式配合使用时，如果能覆盖所有情况，则无需再添加 else 语句。（对于下述代码 Errors 类去掉 sealed，则 when 表达式必须使用 else 语句。）
 */
sealed class Errors : Throwable() {
    object NetworkError : Errors()
    object EmptyInputError : Errors()
    object EmptyResultError : Errors()
    object SingleError : Errors()
}