Es wurden 5 Virtuelle Maschinen verwendet (auf vier lief Ubuntu Server 10.04.4, auf der letzten Ubuntu Xubuntu, Version ist mir leider entfallen).
Auf allen kam als Java Environment:
java -version
java version "1.6.0_24"
OpenJDK Runtime Environment (IcedTea6 1.11.5) (6b24-1.11.5-0ubuntu1~10.04.2)
OpenJDK 64-Bit Server VM (build 20.0-b12, mixed mode)
zum einsatz.

Konfiguration:
10.0.2.10 - DiningPhilosophers.jar (DiningPhilosopherRMI.java als Main)
10.0.2.11 - DiningPhilosophers.jar (DiningPhilosopherRMI.java als Main)
10.0.2.12 - DiningPhilosophers.jar (DiningPhilosopherRMI.java als Main)
10.0.2.13 - DiningPhilosophers.jar (DiningPhilosopherRMI.java als Main)
Auf der letzten VM lief
10.0.2.14 - ViewerImplementation.java als Main und 1700 als Viewer-Port

bei allen wurde als VM-Argument "-Djava.rmi.server.hostname=IP_DER_JWEILIGEN_VM" mit angegeben.

Die Screenshots im selbem Ordner tragen eindeutige Bezeichnungen und repräsentieren den Programmablauf.

Um Verklemmungsfreiheit zu garantieren wird bei dem letzten Seat ein flag gesetzt welches die Forks in vertauschter Reihenfolge zurückliefert.
Wenn neue Sitze hinzukommen werden Vorgänger und Nachfolger des Sitzes blockiert bis die Synchronisation abgeschlossen ist.

Die DiningPhilosopherRMI.java liest Eingaben von System.in.
Mögliche Kommandos:
create Table - erstellt den Master/Server
create Seats - erstellt eine später abgefragte Anzahl an Sitzen
create Philosopher - erstellt eine angegeben Anzahl an Philosophen
show usable seats - zeigt alle verfügbaren Sitze beim Table an (wird auf dem Rechner auf dem der Table läuft auf System.out ausgegeben)
(weiter Kommandos in DiningPhilosopherRMI.java zu finden)

Bei allen Kommandos wird nicht auf korrekte Eingabe geprüft -> Falscheingabe führt zum Abbruch des Threads/des Programms.