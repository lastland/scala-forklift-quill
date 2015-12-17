import io.getquill._

object Application {
  def main(args: Array[String]) {
    val users = quote {
      query[Users].map(_.name)
    }
    println(db.run(users))
  }
}
