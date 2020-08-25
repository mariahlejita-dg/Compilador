package lexico

class AnalizadorLexico {
    var codigoFuente : String ?= null
    var caracterActual : Char ?= null
    var finCodigo : Char ?= null
    var posicionActual : Int = 0
    var columnaActual : Int = 0
    var filaActual : Int = 0
    var tablaSimbolos = ArrayList<Token>()
    var tablaErrores = ArrayList<Token>()

    constructor(codigo : String){
        this.codigoFuente = codigo
        this.caracterActual = codigoFuente!!.get(0)
        this.tablaErrores = ArrayList()
        this.tablaSimbolos = ArrayList()
        this.finCodigo = '0'
    }

    fun analizar(){

    }

    /**
     * Metodo para devolver el proceso de metodos de predicado
     * @param {posición hasta donde devolverse} posInicial
     */
    fun hacerBactracking(posInicial:Int) {
        if (posicionActual === codigoFuente!!.length - 1) {
            caracterActual = finCodigo

        } else {
            if (caracterActual === '\n') {
                filaActual++
                columnaActual = 0

            } else {
                columnaActual++;
            }
            posicionActual++;
            caracterActual = codigoFuente!!.get(posInicial);

        }
    }

    /**
     * Metodo que continua con el siguiente caracter del codigo fuente
     */
    fun obtenerSiguienteCaracter(){
        if (posicionActual === codigoFuente!!.length - 1) {
            caracterActual = finCodigo

        } else {
            if (caracterActual === '\n') {
                filaActual++;
                columnaActual = 0;

            } else {
                columnaActual++;
            }
            posicionActual++;
            caracterActual = codigoFuente!!.get(posicionActual);

        }
    }
    /**
     * Metodo para devolver el proceso de metodos de predicado
     * @param {posición hasta donde devolverse} posInicial
     */
    fun hacerBacktracking(posInicial:Int) {
        posicionActual = posInicial
        caracterActual = codigoFuente!!.get(posicionActual)
    }
    /**
     * Metodo que almacena un codigo desconocido del sistema
     * @param {* El lexema que se almacenará} lexema
     * @param {* Fila donde inicio el simbolo} fila
     * @param {* Columna donde inicio el simbolo} columna
     * @param {* categoría del simbolo} categoria
     */
    fun almacenarSimbolo(lexema:String,fila:Int,columna:Int,categoria: Categoria){
        tablaSimbolos.add(Token(lexema,fila,columna, categoria))
    }

    /**
     * Metodo que almacena un codigo desconocido del sistema
     * @param {* El lexema de error} lexema
     * @param {* La fila donde origino el error} fila
     * @param {* La columna donde se origino el error} columna
     * @param {*} posicionInicial
     */
    fun reportarError(lexema: String,fila: Int,columna: Int,posicionInicial:Int){
        tablaErrores.add(Token(lexema,fila,columna,posicionInicial-fila,Categoria.DESCONOCIDO))
    }
    /**
     * Metodo para determinar si lo ingresado en codigo es un numero entero
     */
    fun esEntero():Boolean{
        var palabra = ""
        var filaInicio = filaActual
        var columnaInicio = columnaActual
        var guardarPos = posicionActual

        if (caracterActual!!.isDigit()) {
            palabra += caracterActual

            obtenerSiguienteCaracter()
            while (caracterActual!!.isDigit()) {
                palabra += caracterActual
                obtenerSiguienteCaracter()
            }

            if (caracterActual === ',') {
                posicionActual = guardarPos
                caracterActual = codigoFuente!!.get(posicionActual)
                return false
            }
            almacenarSimbolo(palabra, filaInicio, columnaInicio, Categoria.ENTERO)
            return true
        }
        return false
    }
    /**
     * Metodo para determinar si lo ingresado en codigo es un numero real
     */
    fun esReal():Boolean{
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual!!.isDigit()) {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual!!.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual === ',') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual!!.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    while (caracterActual!!.isDigit()) {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return false
                }
            }
            almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.REAL)

            return true
        }
        if (caracterActual === ',') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual!!.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                while (caracterActual!!.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
            } else {
                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                return false
            }
            almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.REAL)
            return true
        }
        return false
    }


    /**
     * Metodo que se encarga de determinar los operadores aritmeticos
     */
    fun esOperadorAritmetico():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == '.') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '+' || caracterActual == '-' || caracterActual == '*' || caracterActual == '/' || caracterActual == '^'
                    || caracterActual == '%') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.OPERADOR_ARITMETICO)
                return true

            } else {
                hacerBacktracking(posicionInicial)
            }
        }
        return false
    }

    /**
     * Metodo que determina si un operador es logico
     */
    fun esOperadorLogico():Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == '.') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            return if (caracterActual == '&' || caracterActual == '|' || caracterActual == '!') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.OPERADOR_LOGICO)
                true
            } else {
                hacerBacktracking(posicionInicial)
                false
            }
        }
        return false
    }
    /**
     * Metodo que determina si lo ingresado es Incremento
     */
    fun esIncremento():Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual
        if (caracterActual == ':') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '+') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                return if (caracterActual == '+') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.INCREMENTO)
                    true
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    false
                }
            } else {
                hacerBacktracking(posicionInicial)
            }
        }
        return false
    }

    /**
     * Metodo que determina si lo ingresado es Decremento
     */
    fun esDecremento():Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual
        if (caracterActual == ':') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '-') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                return if (caracterActual == '-') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.DECREMENTO)
                    true
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    false
                }
            } else {
                hacerBacktracking(posicionInicial)
            }
        }
        return false
    }

    /**
     * Metodo que se encarga de determinar si lo ingresado es un operador relacional
     */
    fun operadorRelacional(): Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual
        //Reconoce si lo ingresado es el operador relacional menor que
        if (caracterActual === '<') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual === '<') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.OPERADOR_RELACIONAL)
                return true
            } else {
                hacerBacktracking(posicionInicial)
            }

        }
        //Reconoce si lo ingresado es el operador mayor que
        if (caracterActual == '>') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual === '>') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.OPERADOR_RELACIONAL)
                return true
            } else {
                hacerBacktracking(posicionInicial)
            }
        }
        // Reconoce si lo ingresado es el operador relacional mayor igual que, menor igual que, igual igual a y diferente de
        if (caracterActual === ':') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual === '=' || caracterActual === '<' || caracterActual === '>' || caracterActual === '¬') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.OPERADOR_RELACIONAL)
                return true
            } else {
                hacerBacktracking(posicionInicial)
            }

        }
        return false

    }
    /**
     * Metodo que determina si lo ingresado es un operador de asignacion
     */
    fun esOperadorAsignacion():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual === ':') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == ':' || caracterActual == '+' || caracterActual == '-' || caracterActual == '*' || caracterActual == '/') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.OPERADOR_ASIGNACION)
                return true
            } else {
                hacerBacktracking(posicionInicial)
            }
        }
        return false

    }

    /**
     * Metodo que determina si lo ingresado es un agrupador
     */
    fun esAgrupador():Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == '.') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '{' || caracterActual == '[' || caracterActual == '(') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.AGRUPADORES_APERTURA)
                return true
            } else if (caracterActual == '}' || caracterActual == ']' || caracterActual == ')') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.AGRUPADORES_CIERRE)
                return true
            } else {
                hacerBacktracking(posicionInicial)
            }
        }
        return false
    }
    /**
     * Metodo que determina si lo ingresado es fin de sentencia
     */
    fun esFinSentencia():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == '%') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.FINCODIGO)
            return true
        }
        return false
    }
    /**
     * Metodo que determina si lo ingresado es el separador
     */
    fun esSeparador(): Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == ';') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.SEPARADOR_SENTENCIA)
            return true
        }
        return false
    }

    /**
     * Metodo que determina si lo ingresado es un punto
     */
    fun esPunto():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == '.') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            return if (caracterActual == '.') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PUNTO)
                true
            } else {
                hacerBacktracking(posicionInicial)
                false
            }
        }
        return false
    }

    /**
     * Metodo que determina si lo ingresado son dos puntos
     */
    fun esDosPuntos():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == '.') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            return if (caracterActual == ':') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.DOS_PUNTOS)
                true
            } else {
                hacerBacktracking(posicionInicial)
                false
            }
        }
        return false
    }
    /**
     * Metodo que determina si lo ingresado es comentario de linea
     */
    fun esComentarioLinea():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == '-') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '-') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                while (caracterActual != '\n') {
                    if (caracterActual === finCodigo) {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)

                    } else {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                    }
                }
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.COMENTARIO_LINEA)
                return true

            } else {
                hacerBacktracking(posicionInicial)
                return false
            }

        }
        return false
    }

    /**
     * Metodo que determina si lo ingresado es un comentario de bloque
     */
    fun esComentarioBloque():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == '_') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '<') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                while (caracterActual != '>') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == finCodigo) {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                }
                if (caracterActual == '>') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    return if (caracterActual == '_') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.COMENTARIO_BLOQUE)
                        true
                    } else if (caracterActual === finCodigo) {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        true
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        true
                    }
                }
            } else if (caracterActual === finCodigo) {
                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                return true
            }
        }
        return false
    }

    /**
     * Metodo que determina si lo ingresado es una cadena de caracteres
     */
    fun esCadenaCaracteres():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == '_') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual != '_') {
                if (caracterActual == '|') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (!(caracterActual == '_' || caracterActual == 'n' || caracterActual == 't' ||
                                    caracterActual == 'f' || caracterActual == 'b' || caracterActual == '\\' ||
                                    caracterActual == 'r' || caracterActual == '|')) {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    } else {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        continue
                    }
                } else if (caracterActual == finCodigo) {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                } else {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
            }
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.CADENA_CARACTERES)
            return true
        }
        return false
    }

    /**
     * Metodo que determina si lo ingresado es un caracter
     */
    fun esCaracter():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == '"') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '|') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual === '"' || caracterActual == 'n' || caracterActual == 't' || caracterActual == 'f' ||
                        caracterActual == 'b' || caracterActual == '\\' || caracterActual == 'r' || caracterActual == '|') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == '"') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.CARACTER)
                        return true
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)

                }
            } else if (caracterActual == finCodigo) {
                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                return true
            } else if (caracterActual != '"') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == '"') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.CARACTER)
                    return true
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                }
            } else {
                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                return true
            }
        }
        return false

    }

    /**
     * Metodo que determina si lo ingresado es el simbolo de concatenacion
     */
    fun esConcatenar():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual === '>') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual === '<') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.CONCATENACION)
                return true
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }

    /**
     * Metodo que determina si lo ingresado es el nombre de clase
     */
    fun esIdentificadorClase():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual === '$') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            return if (caracterActual!!.isUpperCase()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                while (caracterActual!!.isUpperCase()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }

                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.IDENTIFICADOR_CLASE)
                true
            } else {
                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                true
            }
        }
        return false
    }

    /**
     * Metodo que determina si lo ingresado es nombre de metodo
     */
    fun esIdentificadorMetodo():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == '!') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            return if (caracterActual!!.isLowerCase()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                while (caracterActual!!.isLowerCase()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }

                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.IDENTIFICADOR_METODO)
                true
            } else {
                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                true
            }

        }
        return false
    }

    /**
     * Metodo que determina si lo ingresado es una variable
     */
    fun esIdentificadorVariable():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual === '#') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            return if (caracterActual!!.isLowerCase()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                while (caracterActual!!.isLowerCase()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.IDENTIFICADOR_VARIABLE)
                true
            } else {
                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                true
            }
        }
        return false
    }
    /**
     * Metodo que identifica la palabra reservada CLASE
     */
    fun esPalabraReservadaClase():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'C') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'L') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'A') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'S') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        return if (caracterActual == 'E') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                            true
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
    /**
     * Metodo que identifica la palabra reservada PAQUETE
     */
    fun esPalabraReservadaPaquete():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'P') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'A') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'Q') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'U') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'E') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            if (caracterActual == 'T') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                return if (caracterActual == 'E') {
                                    lexema += caracterActual
                                    obtenerSiguienteCaracter()
                                    almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                    true
                                } else {
                                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                    true
                                }
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                return true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    hacerBacktracking(posicionInicial)
                    return false
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
    /**
     * Metodo que identifica la palabra reservada PRIVADO
     */
    fun esPalabraReservadaPrivado():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'P') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'R') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'I') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'V') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'A') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            if (caracterActual == 'D') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                return if (caracterActual == 'O') {
                                    lexema += caracterActual
                                    obtenerSiguienteCaracter()
                                    almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                    true
                                } else {
                                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                    true
                                }
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                return true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                }
            } else {
                hacerBacktracking(posicionInicial)
            }
        }
        return false
    }
    /**
     * Metodo que identifica la palabra reservada PUBLICO
     */
    fun esPalabraReservadaPublico():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'P') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'U') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'B') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'L') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'I') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            if (caracterActual == 'C') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                return if (caracterActual == 'O') {
                                    lexema += caracterActual
                                    obtenerSiguienteCaracter()
                                    almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                    true
                                } else {
                                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                    false
                                }
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                return true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }

        }
        return false
    }
    /**
     * Metodo que identifica la palabra reservada RETORNO
     */
    fun esPalabraReservadaRetorno():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'R') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'E') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'T') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'O') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'R') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            if (caracterActual == 'N') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                return if (caracterActual == 'O') {
                                    lexema += caracterActual
                                    obtenerSiguienteCaracter()
                                    almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                    true
                                } else {
                                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                    true
                                }
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                return true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    hacerBacktracking(posicionInicial)
                    return false
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
    /**
     * Metodo que identifica si lo ingresado e la palabra reservada MIENTRAS
     */
    fun esPalabraReservadaMientras():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'M') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'I') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'E') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'N') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'T') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            if (caracterActual == 'R') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                if (caracterActual == 'A') {
                                    lexema += caracterActual
                                    obtenerSiguienteCaracter()
                                    return if (caracterActual == 'S') {
                                        lexema += caracterActual
                                        obtenerSiguienteCaracter()
                                        almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                        true
                                    } else {
                                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                        true
                                    }
                                } else {
                                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                    return true
                                }
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                return true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }

        }
        return false
    }
    /**
     * Metodo que identifica si lo ingresado e la palabra reservada PARA
     */
    fun esPalabraReservadaPara():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'P') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'A') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'R') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    return if (caracterActual == 'A') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                        true
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        true
                    }
                } else {
                    hacerBacktracking(posicionInicial)
                    return false
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
    /**
     * Metodo que identifica si lo ingresado e la palabra reservada SI
     */
    fun esPalabraReservadaSi():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual === 'S') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            return if (caracterActual === 'I') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                true
            } else {
                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                true
            }
        }
        return false
    }
    /**
     * Metodo que determina si lo ingresado es la palabra reservada LEER
     */
    fun esPalabraReservadaLeer():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'L') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'E') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'E')
                    lexema += caracterActual
                obtenerSiguienteCaracter()
                return if (caracterActual == 'R') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                    true
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    true
                }
            } else {
                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
            }
        }
        return false
    }
    /**
     * Metodo que identifica la palabra reservada REAL
     */
    fun esPalabraReservadaReal():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'R') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'E') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'A') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    return if (caracterActual == 'L') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                        true
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        true
                    }
                } else {
                    hacerBacktracking(posicionInicial)
                    return false
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
    /**
     * Metodo que identifica la palabra reservada ENTERO
     */
    fun esPalabraReservadaEntero():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'E') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'N') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'T') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'E') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'R') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            return if (caracterActual == 'O') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                true
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
    /**
     * Metodo que determina la palabra reservada CADENA
     */
    fun esPalabraReservadaCadena():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'C') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'A') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'D') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'E') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'N') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            return if (caracterActual == 'A') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                true
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
    /**
     * Metodo que determina si la palabra reservada IMPRIMIR
     */
    fun esPalabraReservadaImprimir():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'I') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'M') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'P') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'R') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'I') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            if (caracterActual == 'M') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                if (caracterActual == 'I') {
                                    lexema += caracterActual
                                    obtenerSiguienteCaracter()
                                    return if (caracterActual == 'R') {
                                        lexema += caracterActual
                                        obtenerSiguienteCaracter()
                                        almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                        true
                                    } else {
                                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                        true
                                    }
                                } else {
                                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                    return true
                                }
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                return true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    hacerBacktracking(posicionInicial)
                    return false
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
    /**
     * Metodo que determina si la palabra reservada IMPORTAR
     */
    fun esPalabraReservadaImportar():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'I') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'M') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'P') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'O') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'R') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            if (caracterActual == 'T') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                if (caracterActual == 'A') {
                                    lexema += caracterActual
                                    obtenerSiguienteCaracter()
                                    return if (caracterActual === 'R') {
                                        lexema += caracterActual
                                        obtenerSiguienteCaracter()
                                        almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                        true
                                    } else {
                                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                        true
                                    }
                                } else {
                                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                    return true
                                }
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                return true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
    /**
     * Metodo que determina si la palabra reservada BOOLEANO
     */
    fun esPalabraReservadaBooleano():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'B') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'O') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == 'O') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'L') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'E') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            if (caracterActual == 'A') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                if (caracterActual == 'N') {
                                    lexema += caracterActual
                                    obtenerSiguienteCaracter()
                                    return if (caracterActual == 'O') {
                                        lexema += caracterActual
                                        obtenerSiguienteCaracter()
                                        almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                        true
                                    } else {
                                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                        true
                                    }
                                } else {
                                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                    return true
                                }
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                return true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
    /**
     * Metodo que determina si la palabra reservada NO-RETORNO
     */
    fun esPalabraReservadaNoRetorno():Boolean {
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual

        if (caracterActual == 'N') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'O') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == '-') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == 'R') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'E') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            if (caracterActual == 'T') {
                                lexema += caracterActual
                                obtenerSiguienteCaracter()
                                if (caracterActual == 'O') {
                                    lexema += caracterActual
                                    obtenerSiguienteCaracter()
                                    if (caracterActual == 'R') {
                                        lexema += caracterActual
                                        obtenerSiguienteCaracter()
                                        if (caracterActual == 'N') {
                                            lexema += caracterActual
                                            obtenerSiguienteCaracter()
                                            return if (caracterActual == 'O') {
                                                lexema += caracterActual
                                                obtenerSiguienteCaracter()
                                                almacenarSimbolo(lexema, filaInicial, columnaInicial, Categoria.PALABRA_RESERVADA)
                                                true
                                            } else {
                                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                                true
                                            }
                                        } else {
                                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                            return true
                                        }
                                    } else {
                                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                        return true
                                    }
                                } else {
                                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                    return true
                                }
                            } else {
                                reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                                return true
                            }
                        } else {
                            reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                            return true
                        }
                    } else {
                        reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                        return true
                    }
                } else {
                    reportarError(lexema, filaInicial, columnaInicial, posicionInicial)
                    return true
                }
            } else {
                hacerBacktracking(posicionInicial)
                return false
            }
        }
        return false
    }
}