import io.getquill._
import io.getquill.naming.SnakeCase
import io.getquill.source.jdbc.JdbcSource
import io.getquill.source.sql.idiom.MySQLDialect

object M2 {
  MyMigrations.migrations = MyMigrations.migrations :+ SimpleMigration(2)(
    (db: JdbcSource[MySQLDialect, SnakeCase]) => {
      val insert = quote {
        (id: Int, name: String) => query[Users].insert(_.id -> id, _.name -> name)
      }
      db.run(insert).using(List((1, "Harry Potter"),
        (2, "Ron Weasley"), (3, "Hermione Granger")))
    })
}
