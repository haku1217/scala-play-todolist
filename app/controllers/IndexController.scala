package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import daos.TodoDao
import javax.inject.{ Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms.{ mapping, text}
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc.{ Action, Controller }

case class TodoForm(action: String, content: String)

@Singleton
class IndexController @Inject()(val todoDao: TodoDao, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  val todoForm = Form (
    mapping(
      "action" -> text,
      "content" -> text
    )(TodoForm.apply)(TodoForm.unapply))

  def get = Action.async { implicit request =>
    todoForm.bindFromRequest.fold(
      formWithErrors => {
        todoDao.all().map(todos => Ok(views.html.index(formWithErrors, todos)))
      },
      todoData =>
    )
  }
}
