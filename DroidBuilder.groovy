#!/usr/bin/env groovy
/**
 * DroidBuilder.groovy
 * Main entry point for the Droid software factory.
 */
println "🚀 Starting DroidBuilder..."

import groovy.xml.XmlParser

// ====================== CONFIG LOADING ======================

def loadConfigs(String buildDir = './build/', String optionsDir = './options/') {
    def parser = new XmlParser()
    def configs = [:]

    // Builder core definitions
    configs.appProcs  = parser.parseText(new File("${buildDir}APP_PROCS.xml").text)
    configs.appFuncs  = parser.parseText(new File("${buildDir}APP_FUNCS.xml").text)
    configs.appCodes  = parser.parseText(new File("${buildDir}APP_CODES.xml").text)

    // App-specific metadata
    configs.appNames          = parser.parseText(new File("${optionsDir}APP_NAMES.xml").text)
    configs.appTables         = parser.parseText(new File("${optionsDir}APP_TABLES.xml").text)
    configs.appEnums          = parser.parseText(new File("${optionsDir}APP_ENUMS.xml").text)

    println "✅ Loaded configs: ${configs.keySet()}"
    return configs
}

// ====================== HELPER FUNCTIONS ======================

def insertAppPackages(String theText, def configs) {
    def result = theText
    configs.appNames.appPackage.each { p ->
        result = result.replace(p.'@oldPackage' as String, p.'@newPackage' as String)
    }
    result
}

def insertAppNames(String theText, def configs) {
    def result = theText
    configs.appNames.appName.each { n ->
        result = result.replace(n.'@oldName' as String, n.'@newName' as String)
    }
    result
}

def insertAppCopyrights(String theText, def configs) {
    def result = theText
    configs.appNames.appCopyright.each { n ->
        def newCopyright = (n.'@newCopyright' as String)
            .replace('\\n', System.lineSeparator())

        result = result.replace(n.'@oldCopyright' as String, newCopyright)
    }
    result
}

def insertAppAuthors(String theText, def configs) {
    def result = theText
    configs.appNames.appAuthor.each { n ->
        result = result.replace(n.'@oldAuthor' as String, n.'@newAuthor' as String)
    }
    result
}

def applyTablePlaceholders(String text, def table) {
    text.replace("yyyyys", table.'@objPlural'    ?: "")
        .replace("Yyyyys", table.'@classPlural'  ?: "")
        .replace("yyyyy",  table.'@objName'      ?: "")
        .replace("Yyyyy",  table.'@className'    ?: "")
        .replace("YYYYY",  table.'@dbName'       ?: "")
        .replace("DDDDD",  table.'@displayName'  ?: "")
        .replace("yfyfy",  table.'@fileName'     ?: "")
}

def applyFieldPlaceholders(String text, def field) {
    text.replace("FFFFF", field.'@dbName'         ?: "")
        .replace("Fffff", field.'@classFieldName' ?: "")
        .replace("fffff", field.'@objFieldName'   ?: "")
        .replace("ttttt", field.'@fieldType'      ?: "")
        .replace("sssss", field.'@fieldSize'      ?: "")
        .replace("ddddd", field.'@scale'          ?: "")
        .replace("DFDFD", field.'@displayName'    ?: "")
        .replace("BFBFB", field.'@blobNameFld'    ?: "")
        .replace("FTFTF", field.'@format'         ?: "")
        .replace("CPCPC", field.'@classPlural'    ?: "")
        .replace("AAAAA", "&")
        .replace("LLLLL", "<")
        .replace("GGGGG", ">")
}

def cleanSkipAndDivs(String text) {
    def result = new StringBuilder()
    def titTat = "tit"
    text.eachLine { line ->
        def t = line.trim()
        if (t == "___skip___") return
        if (t == "___DIV_CLASS___") {
            result << (titTat == "tit"
                ? "<div class='portlet-section-alternate'>"
                : "<div class='portlet-section-body'>") << '\r\n'
            titTat = (titTat == "tit") ? "tat" : "tit"
        } else {
            result << line << '\r\n'
        }
    }
    result.toString()
}

def insertTableNames(String text, def configs) {
    def result = text
    for (n in configs.appTables.appTable) {
        result = result
            .replace("yyyyys", n.'@objPlural'   ?: "")
            .replace("Yyyyys", n.'@classPlural' ?: "")
            .replace("yyyyy",  n.'@objName'     ?: "")
            .replace("Yyyyy",  n.'@className'   ?: "")
            .replace("YYYYY",  n.'@dbName'      ?: "")
            .replace("yfyfy",  n.'@fileName'    ?: "")
            .replace("DDDDD",  n.'@displayName' ?: "")
    }
    result
}

def cleanSkipMarkers(String text) {
    def result = new StringBuilder()
    text.eachLine { line ->
        if (!line.trim().startsWith("___")) {
            result << line << '\r\n'
        }
    }
    result.toString()
}

// ====================== LIST / DISPLAY HELPERS ======================

/** Collect and sort fields marked with listFlag == "yes" */
def collectListFields(String theTable, def configs) {
    def sortClass = []
    for (tableNode in configs.appTables.appTable) {
        if (tableNode.'@className' != theTable) continue
        for (f in tableNode.appField) {
            if (f.'@listFlag' == "yes") {
                sortClass << [
                    theOrder : (f.'@listOrder' as int) ?: 0,
                    theType  : f.'@codeType',
                    theName  : f.'@classFieldName',
                    theFormat: f.'@format',
                    theData  : f.'@displayName'
                ]
            }
        }
    }
    sortClass.sort { it.theOrder }
    sortClass
}

/**
 * Builds a simple text-based list heading
 * (e.g. " ID Name Date ...")
 */
def loadListHeading(String theTable, def configs) {
    def fields = collectListFields(theTable, configs)
    if (fields.isEmpty()) return " "

    def theCode = new StringBuilder(" ")
    for (fieldData in fields) {
        theCode << " " << (fieldData.theData ?: "")
    }
    theCode.toString()
}

/**
 * Builds format string + argument list for list view formatting
 */
def loadListFields(String theTable, def configs) {
    def fields = collectListFields(theTable, configs)
    if (fields.isEmpty()) {
        return "\" \",\r\n "
    }

    def theTypeS = new StringBuilder("\" ")
    def theNameS = new StringBuilder(" ")

    for (fieldData in fields) {
        theTypeS << " "
        theNameS << ", "

        switch (fieldData.theType) {
            case "Date":
                theTypeS << "%s"
                theNameS << "formatDate.format(obj.get" << fieldData.theName << "())"
                break
            case "DateTime":
                theTypeS << "%s"
                theNameS << "dateTimeFormatter.format(obj.get" << fieldData.theName << "())"
                break
            case "Money":
                theTypeS << "%s"
                theNameS << "DecimalFormat.getCurrencyInstance().format(obj.get" << fieldData.theName << "())"
                break
            default:
                theTypeS << (fieldData.theFormat ?: "%s")
                theNameS << "obj.get" << fieldData.theName << "()"
        }
    }

    theTypeS << "\""
    theTypeS << '\r\n' << theNameS
    theTypeS.toString()
}

/**
 * Builds display format string + arguments for detail/view pages
 */
def loadDisplayFields(String theTable, def configs) {
    def fields = collectListFields(theTable, configs)
    if (fields.isEmpty()) {
        return "\"\",\r\n "
    }

    def theTypeS = new StringBuilder("\"")
    def theNameS = new StringBuilder(" ")
    boolean first = true

    for (fieldData in fields) {
        if (!first) {
            theTypeS << " "
            theNameS << ", "
        }
        first = false

        switch (fieldData.theType) {
            case "Date":
                theTypeS << "%s"
                theNameS << "formatDate.format(obj.get" << fieldData.theName << "())"
                break
            case "DateTime":
                theTypeS << "%s"
                theNameS << "dateTimeFormatter.format(obj.get" << fieldData.theName << "())"
                break
            case "Money":
                theTypeS << "%s"
                theNameS << "DecimalFormat.getCurrencyInstance().format(obj.get" << fieldData.theName << "())"
                break
            default:
                theTypeS << (fieldData.theFormat ?: "%s")
                theNameS << "obj.get" << fieldData.theName << "()"
        }
    }

    theTypeS << "\","
    theTypeS << '\r\n' << theNameS
    theTypeS.toString()
}

// ====================== CODE BLOCK GENERATOR ======================

/**
 * Core code block generator – processes templates from APP_CODES.xml
 * and applies field/table specific replacements + sequence logic.
 */
def createCode(String theTable, String theCodeKey, def configs) {
    def myReturn = '\r'
    def newLine  = '\n'
    def myCode   = new StringBuilder()

    // Sequence counters (reset per table in createMultiFilesProc)
    if (!binding.hasVariable('daoSetSeq'))      daoSetSeq      = 5
    if (!binding.hasVariable('providerSetSeq')) providerSetSeq = 5
    if (!binding.hasVariable('staticSetSeq'))   staticSetSeq   = 700

    for (c in configs.appCodes.appCode) {
        if (c.'@codeKey' != theCodeKey) continue

        def codeType = c.'@codeType'
        def baseTemplate = buildBaseTemplate(c, myReturn, newLine)
        def onceFlag = true

        for (tableNode in configs.appTables.appTable) {
            if (tableNode.'@className' != theTable) continue

            for (f in tableNode.appField) {
                if (!(codeType == "ALL" || codeType == "ONCE" || codeType == f.'@codeType')) continue
                if (codeType == "ONCE" && !onceFlag) continue

                def processed = processFieldTemplate(baseTemplate, f, configs)
                myCode << processed
                onceFlag = false
            }
        }
    }
    myCode.toString()
}

def buildBaseTemplate(def codeNode, String myReturn, String newLine) {
    def theCode = new StringBuilder()
    for (cc in codeNode.code) {
        def indentLevel = (cc.'@indent' as int) ?: 0
        def indent = " " * indentLevel
        theCode << indent << cc.text() << myReturn << newLine
    }
    theCode.toString()
}

def processFieldTemplate(String template, def f, def configs) {
    def oldCode = template

    // Enums
    if (oldCode.contains("___LOAD_ENUMS___")) {
        def insertEnums = createEnums(f.'@classFieldName', configs)
        oldCode = oldCode.replace("___LOAD_ENUMS___", insertEnums)
    }

    // Sequence counters
    oldCode = processSequenceCounters(oldCode)

    // Field placeholders
    oldCode = applyFieldPlaceholders(oldCode, f)

    // Test-data lines (stub – implement loadTestData if you need it)
    def finalCode = new StringBuilder()
    oldCode.eachLine { line ->
        if (line.contains("___TEST_DATA___") || line.contains("___TEST_DATA2___")) {
            // finalCode << loadTestData(line, f) << '\r\n'
            finalCode << line << '\r\n'   // keep original for now
        } else {
            finalCode << line << '\r\n'
        }
    }
    finalCode.toString()
}

def processSequenceCounters(String text) {
    def result = text

    if (result.contains("___DAO_SET_SEQ___")) {
        result = result.replace("___DAO_SET_SEQ___", daoSetSeq.toString())
        daoSetSeq++
    }
    if (result.contains("___PP_DAO_SET_SEQ___")) {
        result = result.replace("___PP_DAO_SET_SEQ___", daoSetSeq.toString())
        daoSetSeq++
    }
    if (result.contains("___PROVIDER_SET_SEQ___")) {
        result = result.replace("___PROVIDER_SET_SEQ___", providerSetSeq.toString())
        providerSetSeq++
    }
    if (result.contains("___PP_PROVIDER_SET_SEQ___")) {
        result = result.replace("___PP_PROVIDER_SET_SEQ___", providerSetSeq.toString())
        providerSetSeq++
    }
    if (result.contains("___STATIC_SET_SEQ___")) {
        result = result.replace("___STATIC_SET_SEQ___", staticSetSeq.toString())
        staticSetSeq++
    }
    if (result.contains("___PP_STATIC_SET_SEQ___")) {
        result = result.replace("___PP_STATIC_SET_SEQ___", staticSetSeq.toString())
        staticSetSeq++
    }
    if (result.contains("___PP_PP_STATIC_SET_SEQ___")) {
        result = result.replace("___PP_PP_STATIC_SET_SEQ___", staticSetSeq.toString())
        staticSetSeq++
    }
    if (result.contains("___PP_PP_PP_STATIC_SET_SEQ___")) {
        result = result.replace("___PP_PP_PP_STATIC_SET_SEQ___", staticSetSeq.toString())
        staticSetSeq++
    }
    result
}

def createEnums(String theField, def configs) {
    def myReturn = '\r'
    def newLine  = '\n'
    def theCode  = new StringBuilder()

    for (e in configs.appEnums.appEnum) {
        if (e.'@appField' != theField) continue

        def first = true
        for (ee in e.theEnum) {
            if (!first) theCode << "," << myReturn << newLine
            first = false
            theCode << " " << ee.'@classValue' << "(\"" << ee.'@codeValue' << "\")"
        }
        theCode << ";" << myReturn << newLine
        break
    }
    theCode.toString()
}

// ====================== MULTI-FILES PROC (Per Table) ======================

def createMultiFilesProc(String fileName, String templatePath, String outputDir, def configs) {
    def templateContent = new File("${templatePath}/${fileName}").text

    for (table in configs.appTables.appTable) {
        // Reset sequence counters for each table
        daoSetSeq      = 5
        providerSetSeq = 5
        staticSetSeq   = 700

        def className   = table.'@className'
        def newFileName = fileName
            .replace("Yyyyy", className)
            .replace("yfyfy", table.'@fileName' ?: "")

        println " 📄 Generating table file: ${newFileName}"

        def content = templateContent

        // Process all code blocks
        for (c in configs.appCodes.appCode) {
            def codeKey = c.'@codeKey'
            if (content.contains(codeKey)) {
                def insertCode = createCode(className, codeKey, configs)
                content = content.replace(codeKey, insertCode ?: "___skip___")
            }
        }

        // List / display helpers
        if (content.contains("___LOAD_LIST_HEADING___")) {
            content = content.replace("___LOAD_LIST_HEADING___", loadListHeading(className, configs))
        }
        if (content.contains("___LOAD_LIST_FIELDS___")) {
            content = content.replace("___LOAD_LIST_FIELDS___", loadListFields(className, configs))
        }
        if (content.contains("___LOAD_DISPLAY_FIELDS___")) {
            content = content.replace("___LOAD_DISPLAY_FIELDS___", loadDisplayFields(className, configs))
        }

        // Metadata + table placeholders
        content = insertAppCopyrights(content, configs) ?: content
        content = insertAppAuthors(content, configs)    ?: content
        content = insertAppNames(content, configs)      ?: content
        content = insertAppPackages(content, configs)   ?: content
        content = applyTablePlaceholders(content, table)
        content = cleanSkipAndDivs(content)

        new File("${outputDir}/${newFileName}").write(content)
    }
}

// ====================== MULTI-FUNCTION PROC ======================

def createFunc(String funcKey, def configs) {
    def theFunc = new StringBuilder()
    def myReturn = '\r'
    def newLine  = '\n'

    for (f in configs.appFuncs.appFunc) {
        if (f.'@funcKey' != funcKey) continue
        for (ff in f.func) {
            def indentLevel = (ff.'@indent' as int) ?: 0
            def indent = " " * indentLevel
            theFunc << indent << ff.text() << myReturn << newLine
        }
        break
    }

    theFunc.toString()
        .replace("LLLLL", "<")
        .replace("GGGGG", ">")
}

def createMultiFunctionProc(String fileName, String templatePath, String outputDir, def configs) {
    def templateContent = new File("${templatePath}/${fileName}").text
    def newFileName = insertAppNames(fileName, configs)
    newFileName = insertTableNames(newFileName, configs)

    def content = templateContent

    for (funcNode in configs.appFuncs.appFunc) {
        def funcKey  = funcNode.'@funcKey'
        def tblOrder = funcNode.'@tblOrder' ?: "normal"

        if (!content.contains(funcKey)) continue

        def insertFunc = createFunc(funcKey, configs)
        def allReplacements = new StringBuilder()

        def tableList = (tblOrder == "reverse")
            ? configs.appTables.appTable.reverse()
            : configs.appTables.appTable

        for (table in tableList) {
            def tableFunc = insertFunc

            // Expand any nested code keys
            for (c in configs.appCodes.appCode) {
                def codeKey = c.'@codeKey'
                if (tableFunc.contains(codeKey)) {
                    def insertCode = createCode(table.'@className', codeKey, configs)
                    if (insertCode) {
                        tableFunc = tableFunc.replace(codeKey, insertCode)
                    }
                }
            }

            tableFunc = applyTablePlaceholders(tableFunc, table)
            allReplacements << tableFunc
        }

        content = content.replace(funcKey, allReplacements.toString())
    }

    // Final metadata + cleanup
    content = insertAppCopyrights(content, configs) ?: content
    content = insertAppAuthors(content, configs)    ?: content
    content = insertAppNames(content, configs)      ?: content
    content = insertAppPackages(content, configs)   ?: content
    content = insertTableNames(content, configs)    ?: content
    content = cleanSkipMarkers(content)

    new File("${outputDir}/${newFileName}").write(content)
    println " ✅ Generated function template: ${newFileName}"
}

// ====================== MULTI-FILES-CODE-TYPE PROC ======================

def createMultiFilesCodeTypeProc(String fileName, String templatePath, String outputDir,
                                 String codeType, def configs) {

    def templateContent = new File("${templatePath}/${fileName}").text

    for (table in configs.appTables.appTable) {
        for (field in table.appField) {
            if (field.'@codeType' != codeType) continue

            def className   = table.'@className'
            def fieldName   = field.'@classFieldName'
            def newFileName = fileName
                .replace("Yyyyy", className)
                .replace("Fffff", fieldName)

            println " 📄 Generating ${codeType} file: ${newFileName}"

            def content = templateContent

            // M2M helpers (stubs – implement if needed)
            if (content.contains("___LOAD_LIST_HEADING_M2M___")) {
                content = content.replace("___LOAD_LIST_HEADING_M2M___",
                    loadListHeading(fieldName, configs))
            }
            // if (content.contains("___LOAD_LIST_FIELDS_M2M___")) { ... }
            // if (codeType == "Many2ManyOwner") { ... getMany2Many ... }

            content = insertAppCopyrights(content, configs) ?: content
            content = insertAppAuthors(content, configs)    ?: content
            content = insertAppNames(content, configs)      ?: content
            content = insertAppPackages(content, configs)   ?: content
            content = applyTablePlaceholders(content, table)
            content = applyFieldPlaceholders(content, field)
            content = cleanSkipAndDivs(content)

            new File("${outputDir}/${newFileName}").write(content)
        }
    }
}

// ====================== DEFAULT TEMPLATE PROCESSING ======================

def defaultCodeProc(String fileName, String templatePath, String outputDir, def configs) {
    def newFileName = insertAppNames(fileName, configs)
    def content = new File("${templatePath}/${fileName}").text

    content = insertAppCopyrights(content, configs) ?: content
    content = insertAppAuthors(content, configs)    ?: content
    content = insertAppNames(content, configs)      ?: content
    content = insertAppPackages(content, configs)   ?: content

    new File("${outputDir}/${newFileName}").write(content)
    println " 📝 Default processed: ${newFileName}"
}

// ====================== MAIN TEMPLATE PROCESSING ======================

void createApp(String templatePath, String appDir, def configs) {
    def fileList = new File(templatePath).list()?.toList() ?: []

    for (String i in fileList) {
        def inFile    = new File(templatePath, i)
        def targetDir = new File(appDir)

        if (inFile.directory) {
            println "📁 Processing directory: ${i}"

            String newDirName = i
            newDirName = insertAppPackages(newDirName, configs)
            newDirName = insertAppNames(newDirName, configs)

            def newAppDir = new File(targetDir, newDirName)
            newAppDir.mkdirs()

            createApp(inFile.absolutePath, newAppDir.absolutePath, configs)
        }
        else {
            println "📄 Processing file: ${i}"

            if (i.endsWithAny('.gif', '.jar', '.png', '.jpg', '.zip')) {
                // Binary – copy as-is (with possible name rewrite)
                def newImageName = insertAppNames(i, configs)
                new File(targetDir, newImageName).bytes = inFile.bytes
                println " 📦 Copied binary: ${newImageName}"
            }
            else {
                boolean processed = false

                for (p in configs.appProcs.appProc) {
                    if (p.'@fileName' != i) continue

                    def procName = p.'@procName'
                    switch (procName) {
                        case "createMultiFilesProc":
                            createMultiFilesProc(i, templatePath, appDir, configs)
                            processed = true
                            break
                        case "createMultiFunctionProc":
                            createMultiFunctionProc(i, templatePath, appDir, configs)
                            processed = true
                            break
                        case "createMultiFilesCodeTypeProc":
                            createMultiFilesCodeTypeProc(i, templatePath, appDir,
                                p.'@codeType', configs)
                            processed = true
                            break
                        default:
                            println " ⚠️ Unknown proc '${procName}' for ${i}"
                    }
                    
                }

                if (!processed) {
                    defaultCodeProc(i, templatePath, appDir, configs)
                }
            }
        }
    }
}

// ====================== EXECUTION ======================

def configs = loadConfigs()

def appDir = "./" + configs.appNames.appName[0].'@newName'
new File(appDir).mkdirs()

def templatePath = "./template"
createApp(templatePath, appDir, configs)

println "✅ DroidBuilder completed successfully!"
