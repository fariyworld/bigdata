package test

import scala.util.control.Breaks._
import java.text.MessageFormat
import java.awt.Point

object App01 {

  def main(args: Array[String]): Unit = {

    MessageFormat.format("This answer to {0} is {1}", "mace", 18.asInstanceOf[AnyRef])

    lazy val words = scala.io.Source.fromFile("D:/BONC/wordcount.txt").mkString
    val x = 1
    if (x >= 0) math.sqrt(x) else throw new IllegalArgumentException("x should not be negative")

    for (i <- 0 to 10 reverse) println(i)
    
    try {
      // 代码块

    } catch {
      // TODO: handle error 处理异常
      case ex: Exception => ex.printStackTrace()
    } finally {
      // TODO: handle finally clause 释放资源
    }

    var flag = true
    for (i <- 1 to 10 if flag) {
      println(i)
      if (i == 5) flag = false
    }

    var sum = 0
    breakable {
      for (i <- 0 until 10) {
        if (i == 5) {
          break
        }
        sum += i
      }
    }
    println("sum = " + sum)

    for (i <- 1 to 9; j <- 1 to i) {
      print(s"$j*$i=${i * j}\t")
      if (i == j) println()
    }

    for (i <- 1 to 9; j <- 1 to i if j != 9) {
      print(s"$j*$i=${i * j}\t")
      if (i == j) println()
    }

  }

  def addOuter(): Int = {
    var sum = 0
    def addInner(): Unit = {
      for (i <- 1 to 10) {
        if (i == 5) return
        sum += i
        printf("sum = %d\n", sum)
      }
    }
    addInner()
    sum
  }

  def abs(x: Double) = if (x >= 0) x else -x

  def sum(n: Int) = {
    var sum = 0
    for (i <- 1 to n) sum += i
    sum
  }

  def decorate(str: String, left: String = "[", right: String = "]") = left + str + right

  def sum(args: Int*) = {
    var sum = 0
    for (arg <- args) sum += arg
    sum
  }

  def recursiveSum(args: Int*): Int = {
    if (args.length == 0) 0
    else args.head + recursiveSum(args.tail: _*)
  }

  def sayHello(name: String): Unit = {
    println("hello " + name)
  }

  def sqrt(x: Double) = {
    if (x >= 0)
      math.sqrt(x)
//      throw new IllegalArgumentException("x should not be negative")
    else
      throw new IllegalArgumentException("x should not be negative")
  }
  
  
  def countdown(n: Int) {
    (0 to n).reverse.foreach(println(_))
  }
}