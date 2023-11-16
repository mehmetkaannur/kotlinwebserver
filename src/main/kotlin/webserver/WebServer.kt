package webserver

import java.util.*

// write your web framework code here:

fun scheme(url: String): String = url.substringBefore(':')

fun host(url: String): String = url.substringAfter("//").substringBefore('/')

fun path(url: String): String = url.substringAfter(host(url)).substringBefore('?')

fun queryParams(url: String): List<Pair<String, String>> {
  val a = url.substringAfter('?').split('&').first().split('=').first()
  val b = url.substringAfter('?').split('&').first().split('=').last()
  val c = url.substringAfter('?').split('&').last().split('=').first()
  val d = url.substringAfter('?').split('&').last().split('=').last()
  return if (a == url && b == url && c == url && d == url) {
    emptyList()
  } else if (a == c && b == d) {
    listOf(Pair(a, b))
  } else {
    listOf(Pair(a, b), Pair(c, d))
  }
}

// http handlers for a particular website...
fun helloHandler(req: Request): Response {
  val style = req.url.substringAfterLast('=')
  var name = req.url.substringAfter('?').substringAfter('=').substringBefore('&')
  if (name == req.url) {
    name = "world"
  }
  val body = if (style == "shouting") {
    "HELLO, " + name.uppercase() + "!"
  } else {
    "Hello, " + name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + "!"
  }
  return Response(Status.OK, body)
}

fun route(req: Request): Response {
  val route = path(req.url)
  val home_checker = route.substringAfter('/').substringBefore('/')
  val hello = route.substringBefore('?')
  return if (home_checker.equals("")) {
    homePageHandler()
  } else if (home_checker.equals("computing")) {
    computingHandler()
  } else if (hello.equals("/say-hello")) {
    helloHandler(req)
  } else {
    otherHandler()
  }
}

fun homePageHandler(): Response = Response(Status.OK, "This is Imperial.")
fun computingHandler(): Response = Response(Status.OK, "This is DoC.")
fun otherHandler(): Response = Response(Status.NOT_FOUND)
