import io.getquill._
import io.getquill.naming.SnakeCase
import io.getquill.source.jdbc.JdbcSource
import io.getquill.source.sql.idiom.MySQLDialect
import com.liyaos.forklift.core.Migration
import com.liyaos.forklift.core.MigrationManager

case class SimpleMigration(val id: Int)(
  f: JdbcSource[MySQLDialect, SnakeCase] => Unit)
    extends Migration[Int, Unit] {
  override def up = { f(db) }
}

trait QuillMigrationManager extends MigrationManager[Int, Unit] {
  override def init = {
    db.execute("create table `migrations` (`id` integer not null primary key)")
  }

  // uncomment the following line once you run `init`
  /*
  case class Migrations(id: Int)
  lazy val lookupMigration = quote {
    query[Migrations].map(_.id)
  }
  lazy val applyMigration = quote {
    (id: Int) => query[Migrations].insert(_.id -> id)
  }

  override def alreadyAppliedIds = {
    db.run(lookupMigration)
  }

  def latest =
    if (alreadyAppliedIds.isEmpty) None
    else Some(alreadyAppliedIds.last)

  override protected def up(migrations: Iterator[Migration[Int, Unit]]) {
    for (m <- migrations) {
      m.up
      db.run(applyMigration).using(List(m.id))
    }
  }
   */

  // comment the following line once you run `init`
  override def alreadyAppliedIds = List()

  override def reset = {
    db.execute("drop table `migrations`")
    db.execute("drop table `users`")
  }
}

object MyMigrations extends App
    with QuillMigrationManager
    with QuillMigrationCommands
    with QuillMigrationCommandLineTool {
  MigrationSummary
  execCommands(args.toList)
}
