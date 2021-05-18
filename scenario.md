# Obecný scénář aplikace.

Software, pro tajemníka ústavu, který bude spravovat pracovní štítky zamestnanců. Každý zaměstananec má úvazek na základě kolik předmětů učí. Každý předmět může mít několik skupin studentů. 


a) Předmět

b) Zaměstnanec

c) Skupina studentů

d) Pracovní štítek

# Technologie:

* Java
* SWING
* XML

# Funkcionalita:


>Světle šedá barva označuje nepovinou část.

## A) Počáteční vložení dat

1. Tajemník zadá všechny předměty a studijní skupiny.

2. Tajemník vzájemně propojí předměty se studijními skupinami.

3. Tajemník zadá všechny zaměstnance.

4. Tajemník v aplikaci automaticky (viz B) vygeneruje pracovní štítky (rozvrhové akce).

5. Tajemník přiřadí všechny štítky z poolu nepřiřazených štítků jednotlivým zaměstnancům.

## B) Automatizace aplikace

1. Podle počtu studentů a velikostí tříd u jednotlivých předmětů se vygenerují pracovní štítky. 

*Příklad: Pokud má skupina 30 studentů a předmět přednášku a cvičení s velikostí třídy 12, vygeneruje se 1 x pracovní štítek přednáška – počet studentů 30 a 3 x pracovní štítek cvičení – počet studentů 10.*

2. Přepočítání pří úbytku studentů)

*Příklad: Pokud ze 30 ubude studentů na 19. Zůstanou 2 x štítek cvičení s 10, 9 studenty. A jeden 1 x prázdný štítek s 0 studenty. Ten musím být označen, aby ho tajemník mohl odmazat a automaticky se za něj nepočítají žádné body do úvazku.*

3. Přepočítání při nárustu studentů)

*Příklad: Pokud počet studentů ze 30 naroste na 44. U třech předchozích cvičení stoupne počet z 10 na 11. A vygeneruje se nový štítek v poolu nepřiřazených štítků s počtem studentů 11.*


>2. Automaticky vygenerují štítky pro z, zk, kl. Kdo cvičí dává z, kl a kdo přednáší dává zk.


## C) Jak tajemník řídí úvazky

a) Neustále upřesňuje počty studentů ve skupinách.

b) Přesouvá štítky od zaměstnanců do poolu nezařazených štítků a zpátky, aby všechny štítky někoho měly, ale nikdo neměl příliš mnoho úvazkových bodů.

c) Mění velikost tříd u předmětů z 12 na 24, nebo naopak.

d) Vytváří mimořádné pracovní štítky, nespojené se žádným předmětem. To musí být na štítku viditelné.



> ## D) Předání informací o úvazku zaměstnancům
>
>a) Pomocí knihovny EPPlus 5 bych to zaměstnanci zapsal do toho úvazkového listu xls.
>
>EPPlus: https://github.com/EPPlusSoftware/EPPlus
>
>b) Pomocí FluentEmail nebo MailKit, bych to odeslal.
>
>FluentEmail: https://github.com/lukencode/FluentEmail​ 
>MailKit: https://github.com/jstedfast/MailKit
>
>c) Pomocí PaperCut, bych to celé otestoval.
>
>Papercut SMTP: https://github.com/ChangemakerStudios...
>
>Info, proč jsou tyhle knihovny super: https://www.youtube.com/watch?v=uS0hRJqamfU&list=PL0YTYqjOfcLG9nLk6pTcEAUGHEsLQCcFP&index=8
