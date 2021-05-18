# Datové mapy

## Předmět
* Zkratka: String: AK8PO
* Název předmětu: String: Pokročilé programování
* Vyučující: String: Vařacha Pavel
* Kapacita přednáška: Int: 0
* Kapacita seminář: Int: 0
* Kapacita cvičení: Int: 15
* Počet týdnů: Int: 1 (u kombinovaného studia 1 jinak defaultně 14)
* Ukončení: Enum: Kl
* Kapacita tridy: Int: 30
* Jazyk: Enum: CZ
* Seznam skupin: List<Skupina>: množina refencí na **Skupina**

## Zaměstnanec
* Jméno: String: Pavel
* Přijmení: String: Vařacha
* Telefon: String: +420 576 035 186
* Email: String: varacha@utb.cz
* Úvazek: Double : 0 až 1
* Zamestnanec: Boolean: true
* Pracovní body: Int: 10
* Pracovní body bez EN: Int: 10
* Seznam štítků: List<Pracovní štítek> - množina refencí na **Pracovní štítek**

## Skupina
* Studium: Enum: Mgr
* Studijní obor: String: SWI
* Forma: Enum: K
* Semestr: Enum ZS
* Ročník: Int: 1
* Počet: Int: 35
* Jazyk: Enum: CZ

## Pracovní štítek
* Název: Stríng: Cvičení AP8PO 1
* Zaměstnanec: Zaměstanec: (Pavel Vařacha)
* Předmět: Předmět: (AP8PO)
* Jazy: Enum: CZ
* Počet bodů za pracovní štítek: Int: 10
* Počet studentů: Int: 30
* Druh výuky: Enum: přednáška (při ukončení je null)
* Ukončení: Enum: z (při druh výuky je null)
* Počet týdnů: Int: 14 (při ukončení je 0)
* Počet hodin: Int: 2 (při ukončené je 0)

## Váhy pracovních bodů
* Hodina přednášky: Double: 1.8
* Hodina přednášky: Double: 1,8
* Hodina cvičení: Double: 1,2
* Hodina semináře: Double: 1,2
* Hodina přednášky anglicky: Double: 2,4
* Hodina cvičení anglicky: Double: 1,8
* Hodina semináře anglicky: Double: 1,8
* Udělení zápočtu: Double: 0,2
* Udělení klasifikovaného zápočtu: Double: 0,3
* Udělení zkoušky: Double: 0,4
* Udělení zápočtu anglicky: Double: 0,2
* Udělení klasifikovaného zápočtu anglicky: Double: 0,3
* Udělení zkoušky: anglicky: Double: 0,4


