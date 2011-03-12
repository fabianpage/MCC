import sbt._
import java.io.File

// simple build tool does not find the so libraries automatically, 
// so we need to import them manually 

class MCCProject(info: ProjectInfo) extends DefaultProject(info) {
    // to specify new runJVMOptions we need to fork the execution    
    override def fork = Some(new ForkScalaRun {
        val (os, separator) = System.getProperty("os.name").split(" ")(0).toLowerCase match {
            case "linux" => "linux" -> ":"
            case "mac" => "macosx" -> ":"
            case "windows" => "windows" -> ";"
            case "sunos" => "solaris" -> ":"
            case x => x -> ":"
        }
        val asdf = ( "lib")
        
        override def runJVMOptions = super.runJVMOptions ++ Seq("-Djava.library.path=" + System.getProperty("java.library.path") + separator + asdf)
        override def scalaJars = Seq(buildLibraryJar.asFile, buildCompilerJar.asFile)
    })
    val lwjglRepo = "lwjglRepo" at "http://adterrasperaspera.com/lwjgl"
    val lwjgl = "org.lwjgl" % "lwjgl" % "2.7.1" 
    val lwjglUtil = "org.lwjgl" % "lwjgl-util" % "2.7.1"
    val lwjglNative = "org.lwjgl" % "lwjgl-native" % "2.7.1"
    
}

