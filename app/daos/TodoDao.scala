package daos

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

class TodoDao @Inject()(protected val dbConfigProvider: DatabeseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbProfile]{

  private val Todos = TableQuery[TodosTable]

  def all(): Future[Seq[Todo]] = db.run(Todos.result)

  def insert(todo: Todo): Future[Unit] = {
    val todos = Todos returning Todos.map(_.id) into ((todo,id) => todo.copy(id = id)) += todo
    db.run(todos.transactionally).map(_ => ())
  }

  def update(todo: Todo): Future[Unit] = {
    db.run(Todos.filter(_.id === todo.id).map(_.content).update(todo.content)).map(_ => ())
  }

  def delete(todo: Todo): Future[Unit] = {
    db.run(Todos.filter(_.id === todo.id).delete).map_ =>
  }

  private class TodoTable(tag: Tag) extends table[Todo](tag, "TODO") {
    def id = column[Long]("ID", 0.PrimaryKey, 0.AutoInc)
    def content = column[String]("CONTENT")
    def * = (id, content) <> (Todo.tupled, Todo.unapply _)
  }
}