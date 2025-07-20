package com.example.programfuncional.Data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.programfuncional.Data.model.Progreso
import com.example.programfuncional.Data.model.RutaAprendizaje
import com.example.programfuncional.Data.model.Tema
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [RutaAprendizaje::class, Tema::class, Progreso::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun temaDao(): TemaDAO
    abstract fun progresoDao(): ProgresoDAO
    abstract fun rutaDao(): RutaDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "program_funcional.db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    // Usamos la instancia que acabamos de construir
                                    INSTANCE?.let { database ->
                                        val rutaDao = database.rutaDao()
                                        val temaDao = database.temaDao()

                                        rutaDao.insertAll(
                                            listOf(
                                                RutaAprendizaje(1, "Teoría"),
                                                RutaAprendizaje(2, "Ejercicios"),
                                                RutaAprendizaje(3, "Quizzes")
                                            )
                                        )

                                        temaDao.insertAll(datosIniciales())
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace() // o Log.e("DB", "Error inicializando DB", e)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun datosIniciales(): List<Tema> {
            return listOf(
                Tema(
                    temaId = 1,
                    rutaId = 1,
                    nombre = "Introducción al modelo de programación funcional",
                    informacion = "La programación funcional es un paradigma de programación que se basa en el uso de funciones matemáticas puras.\n" +
                            "Se caracteriza por evitar efectos secundarios y el estado mutable, promoviendo la inmutabilidad y la transparencia referencial.\n" +
                            "En lugar de cambiar variables, se crean nuevas estructuras de datos y se utilizan funciones para transformar datos.\n" +
                            "Este paradigma facilita la paralelización y el razonamiento sobre el código mediante composiciones de funciones.",
                    ejemplos = "-- Definición de una función pura que suma dos números\n" +
                            "suma :: Int -> Int -> Int\n" +
                            "suma a b = a + b"
                ),
                Tema(
                    temaId = 2,
                    rutaId = 1,
                    nombre = "El tipo de datos",
                    informacion = "Los tipos de datos determinan qué valores puede almacenar una variable y cómo deben manipularse.\n" +
                            "Haskell es un lenguaje fuertemente tipado y estáticamente tipado, lo que significa que los tipos se verifican en tiempo de compilación.\n" +
                            "Existen tipos básicos como Int, Bool, Char, y tipos compuestos como listas, tuplas y tipos definidos por el usuario.\n" +
                            "El sistema de tipos ayuda a prevenir muchos errores comunes y facilita la escritura de programas más seguros.",
                    ejemplos = "-- Definiendo variables con tipo explícito\n" +
                            "edad :: Int\n" +
                            "edad = 20\n\n" +
                            "-- Tupla con diferentes tipos\n" +
                            "persona :: (String, Int)\n" +
                            "persona = (\"Ana\", 30)"
                ),
                Tema(
                    temaId = 3,
                    rutaId = 1,
                    nombre = "Funciones",
                    informacion = "En Haskell, las funciones son ciudadanos de primera clase y pueden pasarse como argumentos o devolver como resultado.\n" +
                            "Las funciones pueden ser anónimas (lambda) o definidas con nombre y pueden ser recursivas.\n" +
                            "La definición de funciones se basa en patrones que permiten escribir código claro y estructurado.\n" +
                            "Las funciones en Haskell no tienen efectos secundarios, lo que permite razonamiento matemático sobre ellas.",
                    ejemplos = "-- Función que multiplica dos números\n" +
                            "multiplica :: Int -> Int -> Int\n" +
                            "multiplica x y = x * y\n\n" +
                            "-- Función anónima que suma 1 a un valor\n" +
                            "\\x -> x + 1"
                ),
                Tema(
                    temaId = 4,
                    rutaId = 1,
                    nombre = "Intervalos",
                    informacion = "Los intervalos en Haskell representan secuencias consecutivas de elementos que pueden definirse de forma simple.\n" +
                            "Se crean usando la notación de rango, por ejemplo [1..10] crea una lista de enteros del 1 al 10.\n" +
                            "También se pueden definir con pasos, como [2,4..20] para la secuencia de números pares hasta 20.\n" +
                            "Los intervalos son muy útiles para la generación rápida de listas y para estructuras iterativas simples.",
                    ejemplos = "-- Intervalo de números del 1 al 10\n" +
                            "numeros :: [Int]\n" +
                            "numeros = [1..10]\n\n" +
                            "-- Intervalo de números pares del 2 al 20\n" +
                            "pares :: [Int]\n" +
                            "pares = [2,4..20]"
                ),
                Tema(
                    temaId = 5,
                    rutaId = 1,
                    nombre = "Operadores",
                    informacion = "Los operadores en Haskell son símbolos que representan funciones, por lo que se pueden usar como funciones normales o infijas.\n" +
                            "Algunos operadores comunes son +, -, *, / para aritmética, y ==, /=, <, > para comparaciones.\n" +
                            "Además, Haskell permite la definición de operadores personalizados con una precedencia y asociatividad específicas.\n" +
                            "Esto facilita escribir código expresivo y claro, acercándose a la notación matemática.",
                    ejemplos = "-- Operaciones aritméticas\n" +
                            "suma = 5 + 3\n" +
                            "producto = 4 * 7\n\n" +
                            "-- Uso de operadores personalizados\n" +
                            "infixl 6 <-->\n" +
                            "a <--> b = a + b"
                ),
                Tema(
                    temaId = 6,
                    rutaId = 1,
                    nombre = "Aplicaciones de las listas",
                    informacion = "Las listas son colecciones homogéneas y uno de los tipos de datos más utilizados en Haskell.\n" +
                            "Permiten manipular secuencias de elementos con funciones como map, filter y foldr/foldl para transformar y reducir listas.\n" +
                            "Gracias a la evaluación perezosa, las listas pueden ser infinitas y se procesan solo los elementos necesarios.\n" +
                            "Son fundamentales para la programación funcional debido a su simplicidad y poder expresivo.",
                    ejemplos = "-- Lista de números\n" +
                            "numeros = [1,2,3,4]\n\n" +
                            "-- Filtrar los números pares\n" +
                            "pares = filter even numeros\n\n" +
                            "-- Sumar todos los elementos\n" +
                            "suma = foldr (+) 0 numeros"
                ),
                Tema(
                    temaId = 7,
                    rutaId = 1,
                    nombre = "Árboles",
                    informacion = "Los árboles son estructuras de datos recursivas que consisten en nodos con hijos, útiles para representar información jerárquica.\n" +
                            "En Haskell se definen comúnmente con tipos algebraicos de datos, permitiendo manipular árboles de forma natural y recursiva.\n" +
                            "Son usados en búsquedas, ordenamientos, expresión de operaciones y estructuras más complejas.\n" +
                            "La recursividad facilita el procesamiento y transformación dentro de los árboles.",
                    ejemplos = "-- Definición de un árbol binario\n" +
                            "data Arbol a = Hoja a | Nodo (Arbol a) (Arbol a)\n\n" +
                            "-- Función para calcular número de nodos\n" +
                            "numNodos :: Arbol a -> Int\n" +
                            "numNodos (Hoja _) = 1\n" +
                            "numNodos (Nodo izq der) = 1 + numNodos izq + numNodos der"
                ),
                Tema(
                    temaId = 8,
                    rutaId = 1,
                    nombre = "Evaluación perezosa",
                    informacion = "La evaluación perezosa o lazy evaluation es una estrategia en la que las expresiones no se calculan hasta que su valor es realmente necesario.\n" +
                            "Esto permite definir estructuras de datos infinitas y mejorar rendimiento al evitar cálculos innecesarios.\n" +
                            "Haskell usa evaluación perezosa por defecto, haciendo posible trabajar con secuencias infinitas y pipelines eficientes.\n" +
                            "También ayuda a modularizar el código, separando generación y consumo de datos.",
                    ejemplos = "-- Secuencia infinita de números naturales\n" +
                            "naturales = [1..]\n\n" +
                            "-- Tomar los primeros 5 valores\n" +
                            "cincoPrimeros = take 5 naturales"
                )
            )
        }
    }
}

