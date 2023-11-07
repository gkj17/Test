package cn.guankejian.server

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MyRule:TestRule {
  override fun apply(base: Statement, description: Description): Statement {
    val methodName = description.methodName
    println("${methodName}开始测试")
    base?.evaluate()
    println("${methodName}结束测试")
    return base
  }
}