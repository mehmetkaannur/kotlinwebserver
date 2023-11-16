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
  return if (a.equals(url) && b.equals(url) && c.equals(url) && d.equals(url)) {
    emptyList()
  } else if (a.equals(c) && b.equals(d)) {
    listOf(Pair(a, b))
  } else {
    listOf(Pair(a, b), Pair(c, d))
  }
}

// http handlers for a particular website...
fun helloHandler(req: Request): Response {
  val style = req.url.substringAfterLast('=')
  var name = req.url.substringAfter('?').substringAfter('=').substringBefore('&')
  if (name.equals(req.url)) {
    name = "world"
  }
  var body = ""
  if (style.equals("shouting")) {
    body = "HELLO, " + name.uppercase() + "!"
  } else {
    body =
      "Hello, " + name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + "!"
  }
  return Response(Status.OK, body)
}

fun route(req: Request): Response {
  val route = path(req.url)
  val home_checker = route.substringAfter('/').substringBefore('/')
  val hello = route.substringBefore('?')
  if (home_checker.equals("")) {
    return homePageHandler(req)
  }
  else if (home_checker.equals("computing")){
    return computingHandler(req)
  }
  else if (hello.equals("/say-hello")) {
    return helloHandler(req)
  }
  else {
    return otherHandler(req)
  }
}

fun homePageHandler(request: Request): Response = Response(Status.OK, "This is Imperial.")
fun computingHandler(request: Request): Response = Response(Status.OK, "This is DoC.")
fun otherHandler(request: Request): Response = Response(Status.NOT_FOUND)

