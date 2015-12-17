import io.getquill.naming.SnakeCase
import io.getquill.source.jdbc.JdbcSource
import io.getquill.source.sql.idiom.MySQLDialect

object M1 {
  MyMigrations.migrations = MyMigrations.migrations :+ SimpleMigration(1)(
    (db: JdbcSource[MySQLDialect, SnakeCase]) =>
    db.execute("create table `users` (`id` integer not null primary key, `name` varchar(255) not null)"))
}
