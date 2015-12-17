This is a simple demonstration of using [Scala-Forklift](https://github.com/lastland/scala-forklift) to support database migrations on [Quill](https://github.com/getquill/quill). This demo still contains some hard code and unimplemented features, but it's sufficient to show how it works.

## How to Use

*Note* : Don't directly compile the project in your `sbt`. If you haven't run any of the commands listed below, it will fail.

Clone this repo. Edit the database configuration in `app/src/main/resources/application.conf` to your database settings. Once it's done, enter `sbt` in the project root. Type the following command in your sbt:

    > mg init
    [info] Running MyMigrations init
    [success] Total time: 1 s, completed

Then find the code in `migrations/src/main/scala/Migrations.scala`, uncomment the following code:

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

And comment out the following code:

    override def alreadyAppliedIds = List()

Back to your sbt, enter the following command:

    > mg update
    [info] Running MyMigrations update
    create link to ./migrations/src/main/scala/migrations/1.scala for /path-to-quill-example/./migrations/src_migrations/main/scala/1.scala
    [success] Total time: 0 s, completed
    > mg apply
    [info] Compiling 2 Scala sources to /path-to-quill-example/migrations/target/scala-2.11/classes...
    [info] Running MyMigrations apply
    applying migrations: 1
    [success] Total time: 1 s, completed

This will execute the migration defined in `migrations/src_migrations/main/scala/1.scala`:

    db.execute("create table `users` (`id` integer not null primary key, `name` varchar(255) not null)"))

Once the `users` table is created, you can run the application code:

    > app/run
    [info] Running Application
    List()
    [success] Total time: 0 s, completed

To add some data into this list, we need to execute the next migration defined in `migrations/src_migrations/main/scala/2.scala`:

    val insert = quote {
        (id: Int, name: String) => query[Users].insert(_.id -> id, _.name -> name)
    }
    db.run(insert).using(List((1, "Harry Potter"),
        (2, "Ron Weasley"), (3, "Hermione Granger")))

Apply the migration in your sbt:

    > mg update
    [info] Running MyMigrations update
    create link to ./migrations/src/main/scala/migrations/2.scala for /path-to-quill-example/./migrations/src_migrations/main/scala/2.scala
    [success] Total time: 0 s, completed
    > mg apply
    [info] Compiling 2 Scala sources to /path-to-quill-example/migrations/target/scala-2.11/classes...
    [info] /path-to-quill-example/migrations/src/main/scala/migrations/2.scala:12: INSERT INTO users (id,name) VALUES (?, ?)
    [info]       db.run(insert).using(List((1, "Harry Potter"),
    [info]             ^
    [info] Running MyMigrations apply
    applying migrations: 2
    [success] Total time: 2 s, completed

Then checkout the application:

    > app/run
    [info] Running Application
    List(Harry Potter, Ron Weasley, Hermione Granger)
    [success] Total time: 0 s, completde

## Challenges

Different with Slick, Quill uses probes to check if a database schema matches the Scala queries at compile time. This is why some code fragments in `Migrations.scala` has to be commentted out at first: `migrations` table was not created until `mg init` was executed.

This may not be a problem if this part of code was in the library, because it has already been compiled. However, this will still cause troubles when users want to extend the library.
