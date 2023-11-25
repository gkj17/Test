package cn.guankejian.server.second_part.bean

data class Person(val height:Double, val age:Int=0, val name:String="") {

  infix fun say(content:String){
    println("My name is ${content}, my height is ${height}")
  }
}

infix fun List<Person>.filterTallerThan(height:Double):List<Person>{
  return filter { it.height > height}
}