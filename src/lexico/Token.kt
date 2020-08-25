package lexico

/**
 * Clase que permite la abstracción de un token de lenguaje
 */
class Token {
    var lexema : String ?= null
    var fila : Int ?= null
    var columna : Int ?= null
    var columnaReal : Int ?= null
    var categoria :Categoria ?= null

    /**
     * Costructor del Token
     */
    constructor(lexema:String,fila:Int,columna:Int,categoria:Categoria){
        this.lexema = lexema
        this.fila = fila
        this.columna = columna
        this.categoria = categoria
    }

    /**
     * Constructor para errores
     */
    constructor(lexema:String,fila:Int,columna:Int,columnaReal:Int,categoria:Categoria){
        this.lexema = lexema
        this.fila = fila
        this.columna = columna
        this.columnaReal = columnaReal
        this.categoria = categoria
    }

}