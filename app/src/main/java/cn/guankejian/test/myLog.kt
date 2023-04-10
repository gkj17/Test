package cn.guankejian.test

import android.util.Log


var printLevel = Log.VERBOSE


fun Any?.logV() {
    if (printLevel <= Log.VERBOSE) {
        processMsgBody(toString(), Log.VERBOSE)
    }
}

fun Any?.logD() {
    if (printLevel <= Log.DEBUG) {
        processMsgBody(toString(), Log.DEBUG)
    }
}

fun Any?.logI() {
    if (printLevel <= Log.INFO) {
        processMsgBody(toString(), Log.INFO)
    }
}

fun Any?.logW() {
    if (printLevel <= Log.WARN) {
        processMsgBody(toString(), Log.WARN)
    }
}

fun Any?.logW(t: Throwable) {
    if (printLevel <= Log.WARN) {
        processMsgBody(toString(), Log.WARN)
    }
}

fun Any?.logWRuntimeException(msg: Any = "") {
    if (printLevel <= Log.WARN) {
        Log.w(logTag, msg.toString(), RuntimeException(msg.toString()))
    }
}

fun Any?.logE(detail: Boolean = false) {
    if (detail) {
        if (printLevel <= Log.ERROR) {
            processMsgBody(toString(), Log.ERROR)
        }
    } else {
        Log.e(logTag, toString())
    }
}

fun Any?.logE(t: Throwable) {
    if (printLevel <= Log.ERROR) {
        processMsgBody(toString(), Log.ERROR)
    }
}

fun Any?.logERuntimeException(msg: Any = "") {
    if (printLevel <= Log.ERROR) {
        Log.e(logTag, msg.toString(), RuntimeException(msg.toString()))
    }
}

private val logTag: String
    get() {
        val element = Thread.currentThread().stackTrace[4]
        return "MainMain (${element.fileName}:${element.lineNumber}) ${element.methodName}"
    }


private val TOP_BORDER =
    "╔═══════════════════════════════════════════════════════════════════════════════════════════════════"
private val LEFT_BORDER = "║ "
private val BOTTOM_BORDER =
    "╚═══════════════════════════════════════════════════════════════════════════════════════════════════"
private val MAX_LEN = 1000

private fun processMsgBody(msg: String, flag: Int) {
    printTop(flag)

    val lineCount = msg.length / MAX_LEN
    if (lineCount == 0) {
        printLog(flag, msg)
    } else {
        var index = 0
        var i = 0
        while (true) {
            printLog(flag, msg.substring(index, index + MAX_LEN))
            index += MAX_LEN
            if ((++i) >= lineCount)
                break
        }
    }
    printBottom(flag)
}


fun printLog(flag: Int, msg: String = "") {
    Log.println(flag, logTag, LEFT_BORDER + msg)
}

fun printBottom(flag: Int) {
    Log.println(flag, logTag, BOTTOM_BORDER)
}

fun printTop(flag: Int) {
    Log.println(flag, logTag, TOP_BORDER)
}
