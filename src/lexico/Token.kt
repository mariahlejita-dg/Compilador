package lexico

class Token {
    var lexema : String ?= null
    var fila : Int = 0
    var columna : Int = 0
    var columnaReal : Int = 0
    var categoria :Categoria ?= null

    constructor(lexema:String,fila:Int,columna:Int,categoria:Categoria){
        this.lexema = lexema
        this.fila = fila
        this.columna = columna
        this.categoria = categoria
    }

    constructor(lexema:String,fila:Int,columna:Int,columnaReal:Int,categoria:Categoria){
        this.lexema = lexema
        this.fila = fila
        this.columna = columna
        this.columnaReal = columnaReal
        this.categoria = categoria
    }

}