import io.getquill.naming.SnakeCase
import io.getquill.source.jdbc.JdbcSource
import io.getquill.source.sql.idiom.MySQLDialect

object db extends JdbcSource[MySQLDialect, SnakeCase]
