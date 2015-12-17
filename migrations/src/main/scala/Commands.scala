import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import com.liyaos.forklift.core.MigrationFilesHandler
import com.liyaos.forklift.core.MigrationCommands
import com.liyaos.forklift.core.MigrationCommandLineTool

trait QuillMigrationFilesHandler extends MigrationFilesHandler[Int] {
  def nameIsId(name: String) =
    name forall Character.isDigit

  def nameToId(name: String): Int =
    name.toInt

  def idShouldBeHandled(id: String, appliedIds: Seq[Int]) =
    if (appliedIds.isEmpty) id.toInt == 1
    else id.toInt <= appliedIds.max + 1

  def nextId = {
    val unhandled = new File(unhandledLoc)
    val ids = for {
      file <- unhandled.listFiles
      name <- getId(file.getName)
      if nameIsId(name)
    } yield nameToId(name)
    if (!ids.isEmpty) ids.max + 1 else 1
  }
}


trait QuillMigrationCommands extends MigrationCommands[Int, Unit]
    with QuillMigrationFilesHandler {
  this: QuillMigrationManager =>

  override def statusOp {
    val mf = migrationFiles(alreadyAppliedIds)
    if (!mf.isEmpty) {
      println("you still have unhandled migrations")
      println("use mg update to fetch these migrations")
    } else {
      val ny = notYetAppliedMigrations
      if( ny.size == 0 ) {
        println("your database is up-to-date")
      } else {
        println("your database is outdated, not yet applied migrations: "+notYetAppliedMigrations.map(_.id).mkString(", "))
      }
    }
  }

  override def previewOp = ???

  override def applyOp {
    val ids = notYetAppliedMigrations.map(_.id)
    println("applying migrations: " + ids.mkString(", "))
    up()
  }

  override def initOp {
    super.initOp
    init
  }

  override def resetOp {
    super.resetOp
    reset
  }
}

trait QuillMigrationCommandLineTool
    extends MigrationCommandLineTool[Int, Unit] {
  this: QuillMigrationCommands =>
}
