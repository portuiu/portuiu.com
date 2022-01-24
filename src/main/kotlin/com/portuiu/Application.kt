package com.portuiu

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*
import java.io.File


class LayoutTemplate: Template<HTML> {
    val header = Placeholder<FlowContent>()
    val htmlPage = Placeholder<FlowContent>()
    override fun HTML.apply() {
        head {
            link(rel = "stylesheet", href = "/css/styles.css", type = "text/css")
        }

        body {
            table("header-menu"){
                tbody{
                    tr {
                        td {
                            a("/") {
                              + "Blog"
                            }
                        }
                        td {
                            +" | "
                        }
                        td {
                            a("/about") {
                                +"About me"
                            }
                        }
                        td {
                            +" | "
                        }
                        td {
                            a("https://github.com/portuiu/portuiu.com") {
                                +"Repo"
                            }
                        }
                        td {
                            +" | "
                        }
                        td{
                            a("/wishlist"){
                                +"Wishlist"
                            }
                        }
                    }
                }
            }

            div("body-content") {
                h1 {
                    +"Content"
                }
                ul {
                    li {
                        a("/posts/report1.html"){
                           +"Random Forest, Trees, and Stumps"
                        }
                    }
                }
            }
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            get("/") {
                call.respondHtmlTemplate(LayoutTemplate()) {
                }
            }

            get("/posts/{post_id}"){
                val headerMenu = """<table class="header-menu"><tbody><tr><td><a href="/">Blog</a></td><td> | </td><td><a href="/about">About me</a></td><td> | </td><td><a href="https://github.com/portuiu/portuiu.com">Repo</a></td><td> | </td><td><a href="/wishlist">Wishlist</a></td></tr></tbody></table><style>table td{color:#fff}a,a:link{text-decoration:none;color:#7d1}a:active,a:hover{cursor:pointer;text-decoration:underline}.header-menu{border:0;border-spacing:20px;padding:0;border-collapse:separate;text-align:center;width:700px;margin:auto} </style>"""
                var reportContent = File("pages/"+call.parameters["post_id"]).readText(Charsets.UTF_8);
                val content = """<div><div class="notebook_content">""" + reportContent.substringAfter("""<div><div class="notebook_content">""").substringBefore("<style>.authTokens__title")
                reportContent = reportContent.replace(content, "");
                reportContent = reportContent.replace("</style><style>html { overflow: auto; }</style></body></html>", "</style><style>html { overflow: auto; }</style>" + content);
                reportContent = reportContent.replace("""<html lang="en" theme="idea">""", """<html lang="en" theme="dark-old">""");
                reportContent = reportContent.replace("""--primary-bg: #303030;""", """--primary-bg: #222222;""");
                reportContent = reportContent.replace("""body {
  font: 12px / 1.25 -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";""", "body {font: 10px 'JetBrains Mono', sans-serif;");
                reportContent = reportContent.replace("""<body data-route="PUBLISHING_PRINT" class="print-page page-print"><div id="MathJax_Message" style="display: none;"></div>""", """<body data-route="PUBLISHING_PRINT" class="print-page page-print"><div id="MathJax_Message" style="display: none;"></div>""" + headerMenu);
                reportContent = reportContent.replace(""":root[theme="dark-old"] {
  --action-color-1: #97e1fb;
  --action-color-2: #008dc0;
  --action-color-3: #00688e;""", """:root[theme="dark-old"] {
  --action-color-1: #77dd11;
  --action-color-2: #77dd11;
  --action-color-3: #77dd11;""");
                reportContent = reportContent.replace("""td {
  border: 1px solid var(--border-color);
  padding: 0.25em;
  min-width: 32px;
}""", "");
                reportContent = reportContent.replace(""".markdown-output {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
  font-size: 14px;
  line-height: 24px;
  color: var(--main-color-8);
  padding: 16px 16px 16px 16px;
}
.markdown-output h1,
.markdown-output h2,
.markdown-output h3,
.markdown-output h4 {
  line-height: normal;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
  color: var(--main-color-9);
  margin: 0 0 16px 0;
}""", """.markdown-output {
  font-family: 'JetBrains Mono', -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
  font-size: 14px;
  line-height: 24px;
  color: var(--main-color-8);
  padding: 16px 16px 16px 16px;
}
.markdown-output h1,
.markdown-output h2,
.markdown-output h3,
.markdown-output h4 {
  line-height: normal;
  font-family: 'JetBrains Mono', -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
  color: var(--main-color-9);
  margin: 0 0 16px 0;
}""");
                reportContent = reportContent.replace("""table {
  font-size: 12px;
  border-collapse: collapse;
  border: 1px solid var(--border-color);
}""", "");
                reportContent = reportContent.replace("""@font-face {
  font-family: 'JetBrains Mono';
  font-style: normal;
  font-weight: 400;
  font-display: fallback;
  src: local("JetBrains Mono dev Regular"), local("JetBrainsMonodev-Regular"), local("JetBrains Mono Regular"), local("JetBrainsMono-Regular"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Regular.woff2") format("woff2"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Regular.woff") format("woff");
}
@font-face {
  font-family: 'JetBrains Mono';
  font-style: italic;
  font-weight: 400;
  font-display: fallback;
  src: local("JetBrains Mono dev Italic"), local("JetBrainsMonodev-Italic"), local("JetBrains Mono Italic"), local("JetBrainsMono-Italic"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Italic.woff2") format("woff2"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Italic.woff") format("woff");
}
@font-face {
  font-family: 'JetBrains Mono';
  font-style: normal;
  font-weight: 500;
  font-display: fallback;
  src: local("JetBrains Mono dev Medium"), local("JetBrainsMonodev-Medium"), local("JetBrains Mono Medium"), local("JetBrainsMono-Medium"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Medium.woff2") format("woff2"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Medium.woff") format("woff");
}
@font-face {
  font-family: 'JetBrains Mono';
  font-style: italic;
  font-weight: 500;
  font-display: fallback;
  src: local("JetBrains Mono dev Medium Italic"), local("JetBrainsMonodev-Medium-Italic"), local("JetBrains Mono Medium Italic"), local("JetBrainsMono-Medium-Italic"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Medium-Italic.woff2") format("woff2"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Medium-Italic.woff") format("woff");
}
@font-face {
  font-family: 'JetBrains Mono';
  font-style: normal;
  font-weight: 700;
  font-display: fallback;
  src: local("JetBrains Mono dev Bold"), local("JetBrainsMonodev-Bold"), local("JetBrains Mono Bold"), local("JetBrainsMono-Bold"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Bold.woff2") format("woff2"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Bold.woff") format("woff");
}
@font-face {
  font-family: 'JetBrains Mono';
  font-style: italic;
  font-weight: 700;
  font-display: fallback;
  src: local("JetBrains Mono dev Bold Italic"), local("JetBrainsMonodev-Bold-Italic"), local("JetBrains Mono Bold Italic"), local("JetBrainsMono-Bold-Italic"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-Bold-Italic.woff2") format("woff2"), url("/assets/fonts/JetBrainsMono//JetBrainsMono-Bold-Italic.woff") format("woff");
}
@font-face {
  font-family: 'JetBrains Mono';
  font-style: normal;
  font-weight: 800;
  font-display: fallback;
  src: local("JetBrains Mono dev ExtraBold"), local("JetBrainsMonodev-ExtraBold"), local("JetBrains Mono ExtraBold"), local("JetBrainsMono-ExtraBold"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-ExtraBold.woff2") format("woff2"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-ExtraBold.woff") format("woff");
}
@font-face {
  font-family: 'JetBrains Mono';
  font-style: italic;
  font-weight: 800;
  font-display: fallback;
  src: local("JetBrains Mono dev ExtraBold Italic"), local("JetBrainsMonodev-ExtraBold-Italic"), local("JetBrains Mono ExtraBold Italic"), local("JetBrainsMono-ExtraBold-Italic"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-ExtraBold-Italic.woff2") format("woff2"), url("/assets/fonts/JetBrainsMono/JetBrainsMono-ExtraBold-Italic.woff") format("woff");
}""", """@font-face {
    font-family: 'JetBrains Mono';
    font-style: normal;
    font-weight: 100;
    src: url("../fonts/JetBrainsMono/ttf/JetBrainsMono-Thin.ttf") format("truetype");
    src: url("../fonts/JetBrainsMono/webfonts/JetBrainsMono-Thin.woff2") format("woff2");
}

@font-face {
    font-family: 'JetBrains Mono';
    font-style: normal;
    font-weight: 200;
    src: url("../fonts/JetBrainsMono/ttf/JetBrainsMono-ExtraLight.ttf") format("truetype");
    src: url("../fonts/JetBrainsMono/webfonts/JetBrainsMono-ExtraLight.woff2")
    format("woff2");
}
@font-face {
    font-family: 'JetBrains Mono';
    font-style: normal;
    font-weight: 300;
    src: url("/fonts/JetBrainsMono/ttf/JetBrainsMono-Light.ttf") format("truetype");
    src: url("/fonts/JetBrainsMono/webfonts/JetBrainsMono-Light.woff2") format("woff2");
}
@font-face {
    font-family: 'JetBrains Mono';
    font-style: normal;
    font-weight: 400;
    src: url("../fonts/JetBrainsMono/ttf/JetBrainsMono-Regular.ttf") format("truetype");
    src: url("../fonts/JetBrainsMono/webfonts/JetBrainsMono-Regular.woff2") format("woff2");
}
@font-face {
    font-family: 'JetBrains Mono';
    font-style: normal;
    font-weight: 500;
    src: url("../fonts/JetBrainsMono/ttf/JetBrainsMono-Medium.ttf") format("truetype");
    src: url("../fonts/JetBrainsMono/webfonts/JetBrainsMono-Medium.woff2") format("woff2");
}
@font-face {
    font-family: 'JetBrains Mono';
    font-style: normal;
    font-weight: 600;
    src: url("../fonts/JetBrainsMono/ttf/JetBrainsMono-SemiBold.ttf") format("truetype");
    src: url("../fonts/JetBrainsMono/webfonts/JetBrainsMono-SemiBold.woff2") format("woff2");
}
@font-face {
    font-family: 'JetBrains Mono';
    font-style: normal;
    font-weight: 700;
    src: url("../fonts/JetBrainsMono/ttf/JetBrainsMono-Bold.ttf") format("truetype");
    src: url("../fonts/JetBrainsMono/webfonts/JetBrainsMono-Bold.woff2") format("woff2");
}
@font-face {
    font-family: 'JetBrains Mono';
    font-style: normal;
    font-weight: 800;
    src: url("../fonts/JetBrainsMono/ttf/JetBrainsMono-ExtraBold.ttf") format("truetype");
    src: url("../fonts/JetBrainsMono/webfonts/JetBrainsMono-ExtraBold.woff2")
    format("woff2");
}""")
                call.respondText(reportContent, ContentType.Text.Html, HttpStatusCode.OK)
            }

            static("css") {
                files("css")
            }

            static("fonts") {
                files("fonts")
            }

            static("pages") {
                files("pages")
            }
        }
    }.start(wait = true)
}
