import gmaster.artwork.Art as GArt
import gmaster.artwork.Painting as GPaint
import gmaster.GroovyMaster

import jmaster.artwork.Art as JArt
import jmaster.artwork.ArtType
import jmaster.artwork.Painting as JPaint
import jmaster.JavaMaster

class MetaTester {

    static picassoWorks = ['Pablo Picasso',
                        ['Guernica', 1937],
                        ['Bull\'s Head', 1942, 45]
    ]
    static vanGogh = ['Vincent van Gogh',
                      ['The Starry Night', 1899],
                      ['The Potato Eaters', 1885, 45]
    ]
    static daVinciWorks = ['Leonardo da Vinci',
                        ['Mona Lisa', 1505],
                        ['The Virgin and the Laughing Child', 1472, 50],
                        ['The Last Supper', 1498]
    ]

    static main(argv) {
        ExpandoMetaClass.disableGlobally()

        def use = 112

        /*
         123
         └┼┼ Test method   (1 proxy,  2 expando)
          └┼ Test case     (1 direct, 2 indirect, 3 abstract)
           └ Test language (1 groovy, 2 java)
        */

        switch(use) {
            case 111: proxySample(GroovyMaster); break
            case 112: proxySample(JavaMaster); break
            case 121: proxySample(GPaint); break
            case 122: proxySample(JPaint); break
            case 131: proxySample(GArt); break
            case 132: proxySample(JArt); break
            case 211: expandSample(GroovyMaster); break
            case 212: expandSample(JavaMaster); break
            case 221: expandSample(GPaint); break
            case 222: expandSample(JPaint); break
            case 231: expandSample(GArt); break
            case 232: expandClass(GArt); break

            case 91: traceR(GroovyMaster); break
            case 92: traceR(JavaMaster); break
        }
    }

    static proxySample(Class c2Intercept) {
        println "\n---  Intercepting ${c2Intercept.getName()} class  ---\n"
        def gOrJ = (c2Intercept.getPackageName().contains('gmaster'))
                ? 'groovy'
                : 'java'
        def picasso = (gOrJ == 'groovy')
                ? new GroovyMaster(picassoWorks[0])
                : new JavaMaster(picassoWorks[0])

        def interceptor = new CallInterceptor()
        def proxy = ProxyMetaClass.getInstance(c2Intercept)
        proxy.interceptor = interceptor

        proxy.use {
            helper picasso, picassoWorks
            def daVinci = (gOrJ == 'groovy')
                    ? new GroovyMaster(daVinciWorks[0])
                    : new JavaMaster(daVinciWorks[0])
            helper(daVinci, daVinciWorks)
        }
    }

    static expandClass(Class c2Expand) {
        c2Expand.metaClass.invokeMethod = { String name, args ->
            def metaMethod = c2Expand.metaClass.getMetaMethod(name, args)
            println "---  EXPA    CAL: $name -> $args"
            def result = (metaMethod) ? metaMethod.invoke(delegate, args): "Method does not exist"
            println "---  EXPA    RES: $name <- $result"
            result
        }

        c2Expand.metaClass.'static'.invokeMethod = { String name, args ->
            def metaMethod = c2Expand.metaClass.getStaticMetaMethod(name, args)
            println "---  EXPA  STCAL: $name -> $args"
            def result = (metaMethod) ? metaMethod.invoke(delegate, args) : "Method does not exist"
            println "---  EXPA  STRES: $result"
            result
        }
    }

    static expandSample(Class c2Expand){
        def gOrJ = (c2Expand.getPackageName().contains('gmaster'))
                ? 'groovy'
                : 'java'

        println "\n---  Creating Artist: Picasso  ---\n"
        def picasso = (gOrJ == 'groovy')
                ? new GroovyMaster(picassoWorks[0])
                : new JavaMaster(picassoWorks[0])
        helper(picasso, picassoWorks)

        println "\n---  Expanding ${c2Expand.getName()} class  ---\n"
        expandClass(c2Expand)

        picasso.printArt()
        picasso.getClass().greetings()

        println "\n---  Creating Artist: Da Vinci  ---\n"
        def daVinci = (gOrJ == 'groovy')
                ? new GroovyMaster(daVinciWorks[0])
                : new JavaMaster(daVinciWorks[0])
        helper(daVinci, daVinciWorks)
    }

    static helper(artist, List artwork) {
        artwork.forEach{
            if (it.size() == 2) artist.createPainting(*it)
            if (it.size() == 3) artist.createSculpture(*it)
        }
        artist.printArt()
        artist.getClass().greetings()
    }

    static traceR(Class c2Test) {
        def master = c2Test.newInstance(daVinciWorks[0])

        def tracer = new TracingInterceptor(writer: new StringWriter())
        def proxy = ProxyMetaClass.getInstance(c2Test)
        proxy.interceptor = tracer

        master.metaClass = proxy
        helper(master, daVinciWorks)
        println "TRACEROUT:\n" + tracer.writer.toString()
    }
}

