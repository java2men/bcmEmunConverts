package j2m

import com.beust.klaxon.Klaxon
import j2m.models.BCMEnum
import j2m.models.Dictionary
import java.net.URL

actual class Sample {
    actual fun checkMe() = 42
}

actual object Platform {
    actual val name: String = "JVM"
}

/**
 * Data url
 */
val urlData = ".../enum.json..."

fun main() {

    var jsonData = URL(urlData).readText()
    println("$jsonData")
    var bcmEmum = Klaxon().parse<BCMEnum>(jsonData)

    var result = convert(bcmEmum!!)

    println(result)

}

/**
 * Convert BCMEnum to textual representation of java class
 * @param bcmEnum object @BCMEnum
 */
fun convert(bcmEnum: BCMEnum): String {
    var classJavaEnBcm = "public class EnBCM {\n\n"
    for (data in bcmEnum.dictionary) {
        classJavaEnBcm = classJavaEnBcm.plus(dataToClass(data)).plus("\n\n")
    }

    classJavaEnBcm = classJavaEnBcm.plus("}")

    return classJavaEnBcm
}

/**
 * Dictionary data in textual representation of java class
 * @param dic dictionary
 */
fun dataToClass(dic: Dictionary): String {
    var str = ""

    str = str.plus("/**\n * \n*/\npublic static final class ${generateClassName(dic.title)} { \n\n")
    str = str.plus("/**\n * Заголовок\n*/\npublic static final String title = \"${dic.title}\";\n\n")

    dic.data.forEach {
        var type = ""
        var value: Any? = ""
        if (it.id is Int || it.id == null) {
            type = "Integer"
            value = it.id
        } else if (it.id is String) {
            type = "String"
            value = "\"${it.id}\""
        }
        str = str.plus("/**\n* ${it.title}\n*/\npublic static final $type ${it.code} = $value; \n\n")

    }


    str = str.plus("}")

    return str
}

/**
 * Generate class name based on title
 * @param title dictionary title
 */
fun generateClassName(title: String): String {
    var splits: List<String> = title.split(".")
    var className = ""
    splits.forEach {
        className = className.plus(it.capitalize())
    }

    return className
}

