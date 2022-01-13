import java.io.File
import java.io.FileFilter
import java.lang.IllegalArgumentException
import kotlin.math.pow

var rootDir = File("gradle-module-project")
val defaultChildren = 2
val defaultDepth = 2
var nodeCount= 0

fun main(args: Array<String>) {
    val children: Int
    val depth: Int

    when (args.size) {
        in (2..3) -> {
            println("Using provided values (if valid)")
            children = args[0].toIntOrNull() ?: defaultChildren
            depth = args[1].toIntOrNull() ?: defaultDepth
        }
        else -> {
            println("Using default values")
            children = defaultChildren
            depth = defaultDepth
        }
    }
    rootDir = File((args.getOrNull(2) ?: rootDir).toString())

    println("\nGenerating Project Tree Structure")
    println("Children: $children")
    println("Depth: $depth")
    println("Modules locations: $rootDir\n")

    val files = mutableListOf<File>()
    val directories = mutableListOf<File>()

    if (rootDir.exists()) {

        val rootFiles = rootDir.listFiles().filter { it.isFile }
        val rootDirs = rootDir.listFiles(FileFilter { it.isDirectory })
        //directories.addAll(rootDir.listFiles(FileFilter { it.isDirectory }))

        rootFiles.forEach {
            if (it.name != "settings.gradle" &&
                it.name != "build.gradle" &&
                it.name != ".gradle"
            )
                files.add(it)
        }

        val regex = """^module([0-9]*)""".toRegex()
        rootDirs.forEach {
            if (!regex.matches(it.name) &&
                it.name != "build" &&
                it.name != "modules" &&
                it.name != ".gradle"
            )
                directories.add(it)
        }

        var illegalFiles = "\n["
        files.forEach { illegalFiles += " " + it.name + " " }
        illegalFiles += "]\n["
        directories.forEach { illegalFiles += " " + it.name + " " }
        illegalFiles += "]\n"
        println(illegalFiles)
    }

    check(files.isEmpty() && directories.isEmpty())
    check(rootDir.deleteRecursively())
    check(rootDir.mkdir())

    (0..depth).forEach{
        nodeCount += children.toDouble().pow(it).toInt()
    }
    createGradleBuild(rootDir)
    createModules(nodeCount, children)
    createSettingsGradle(rootDir, nodeCount)

    println("Reached the end without an error!")
}

fun createModules(nodeCount: Int, childCount: Int) {
    (1..nodeCount).forEach {node ->
        val parent: Int

        val offset = childCount - 2

        parent = if (node == 1) - 1
        else (node + offset).div(childCount)

        val children = mutableListOf<Int>()
        (0 until childCount).forEach {
            val child = node * childCount - offset + it
            if (child <= nodeCount)
                children.add(child)
        }

        createModule(node, parent, children)
    }
}

fun createModule(moduleNumber: Int, parent: Int, children: MutableList<Int>) {

    // create basic folder structure
    val moduleName = "module$moduleNumber"
    val modDir = File(rootDir, moduleName)
    modDir.mkdir()

    // create src/main/java
    val srcDir = File(modDir, "src")
    srcDir.mkdir()
    val mainDir = File(srcDir, "main")
    mainDir.mkdir()
    val javaDir = File(mainDir, "java")
    javaDir.mkdir()

    // Create moduleXX
    val pacDir = File(javaDir, "module$moduleNumber")
    pacDir.mkdir()

    // create build.gradle file
    File(modDir, "build.gradle").apply {
        appendText("mainClassName = 'module${moduleNumber}.Module$moduleNumber'\n")
        if (children.size > 0) appendText(createString(children, 0))
    }

    // create module-info file
    File(srcDir, "module-info.java").apply {
        appendText("module $moduleName {\n")
        // node opens itself to parent
        if (parent != -1)
            appendText("    exports $moduleName to module$parent;\n")
        // requires access to children
        if (children.size > 0)
            children.forEach {
                appendText("    requires module$it;\n")
            }
        appendText("}")
    }

    val importChildrenString: String = createString(children, 1)
    val callHiMethodsString:  String = createString(children, 2)

    File(pacDir, "Module${moduleNumber}.java").apply {
        appendText("""
package $moduleName;
$importChildrenString
public class Module$moduleNumber {

    private static final String NAME = "Module $moduleNumber";
    private static final String DAD = "${if (parent> 0) "Module $parent" else "None" }";

    public static void main(String[] args) {
        System.out.println("Hello World! I Am Module $moduleNumber!");
$callHiMethodsString
    }

    public static void hiDad$moduleNumber() {
$callHiMethodsString
        System.out.println("Hi Dad :) -> from child: [" + NAME + "] to father: [" + DAD + "]");
    }

}
"""
        )
    }
}

fun createGradleBuild(rootDir: File) {
    File(rootDir, "build.gradle").apply {
        createNewFile()
        appendText("""
            allprojects {
                apply plugin: 'java'
                group 'net.ddns.flep.gradle-jpms'
                version '1.0-SNAPSHOT'
                sourceCompatibility = 1.9
            }
            
            subprojects {
                apply plugin: 'application'
            }
            
            """.trimIndent()
        )
    }
}

fun createSettingsGradle(rootDir: File, modules: Int) =
    File(rootDir, "settings.gradle").apply {
        (1..modules).forEach { appendText("include 'module$it'\n") }
    }

fun createString(children: MutableList<Int>, type: Int): String =
    StringBuilder().apply {
        if ((children.size == 0) || type !in (0..2)) return ""

        val formatString: String = when(type) {
            0 -> {  // gradle dependencies
                append("\ndependencies {\n")
                "    compile project(':module%1\$s')\n"
            }
            1 -> {  // import children
                "import module%1\$s.Module%1\$s;\n"
            }
            2 -> {  // call hi methods
                "        Module%1\$s.hiDad%1\$s();\n"
            }
            else -> {
                println("Illegal type argument in createString() call")
                throw IllegalArgumentException()
            }
        }

        children.forEach { append(formatString.format(it)) }
        if (type == 0) append("}") // close gradle.build script
    }.toString()