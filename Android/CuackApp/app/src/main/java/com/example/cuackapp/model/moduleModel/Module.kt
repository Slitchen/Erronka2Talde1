package com.example.cuackapp.model.moduleModel

sealed class Module (
    val id: Number = 0,
    val name: String = "",
    val nameEus: String = "",
    val shorted: String = ""
) {

    data object Tutoria : Module(
        id = 1,
        name = "Tutoria",
        nameEus = "Tutoretza",
        shorted = "Tuto"
    )

    data object Guardia : Module(
        id = 2,
        name = "Guardia",
        nameEus = "Zaintza",
        shorted = "Guard"
    )

    data object Sistemas_Informaticos : Module(
        id = 3,
        name = "Sistemas Informaticos",
        nameEus = "Informatika-sistemak",
        shorted = "Sist.Inf"
    )

    data object BD : Module(
        id = 4,
        name = "Bases de datos",
        nameEus = "Datu-baseak",
        shorted = "BD"
    )

    data object Programacion : Module(
        id = 5,
        name = "Programación",
        nameEus = "Programazioa",
        shorted = "Prog"
    )

    data object Lenguajes_de_marcas : Module(
        id = 6,
        name = "Lenguajes de marcas",
        nameEus = "Markatzeko lengoaiak",
        shorted = "LMSGI" // O "Marcas"
    )

    data object Entornos_de_desarrollo : Module(
        id = 7,
        name = "Entornos de desarrollo",
        nameEus = "Garapen-inguruneak",
        shorted = "ED"
    )

    data object Acceso_a_datos : Module(
        id = 8,
        name = "Acceso a datos",
        nameEus = "Datu-atzipena",
        shorted = "AD"
    )

    data object Desarrollo_de_interfaces : Module(
        id = 9,
        name = "Desarrollo de interfaces",
        nameEus = "Interfazeen garapena",
        shorted = "DI"
    )

    data object PMDM : Module(
        id = 10,
        name = "Programación multimedia y dispositivos móviles",
        nameEus = "Multimedia-programazioa eta gailu mugikorrak",
        shorted = "PMDM"
    )

    data object PSP : Module(
        id = 11,
        name = "Programación de servicios y procesos",
        nameEus = "Zerbitzu eta prozesuen programazioa",
        shorted = "PSP"
    )

    data object SGE : Module(
        id = 12,
        name = "Sistemas de gestión empresarial",
        nameEus = "Enpresa-kudeaketako sistemak",
        shorted = "SGE"
    )

    data object EIE : Module(
        id = 13,
        name = "Empresa e Iniciativa Emprendedora",
        nameEus = "Enpresa eta ekimen sortzailea",
        shorted = "EIE"
    )

    data object Opt_Moviles : Module(
        id = 14,
        name = "OPT.moviles",
        nameEus = "Opt. Mugikorrak",
        shorted = "Opt.Mov"
    )

    data object LINQ : Module(
        id = 15,
        name = "LINQ",
        nameEus = "LINQ",
        shorted = "LINQ"
    )

    data object IPE : Module(
        id = 16,
        name = "Itinerarios personales",
        nameEus = "Ibilbide pertsonalak",
        shorted = "IPE"
    )

    data object Ingles : Module(
        id = 17,
        name = "Inglés técnico",
        nameEus = "Ingeles teknikoa",
        shorted = "Ing"
    )

    data object Digitalizacion : Module(
        id = 18,
        name = "Digitalización",
        nameEus = "Digitalizazioa",
        shorted = "Dig"
    )

    data object Sostenibilidad : Module(
        id = 19,
        name = "Sostenibilidad",
        nameEus = "Jasangarritasuna",
        shorted = "Sos"
    )
}