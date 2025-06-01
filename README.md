# System Wypożyczalni Rowerów

System wypożyczalni rowerów to aplikacja konsolowa napisana w Javie, która symuluje działanie miejskiego systemu wypożyczalni rowerów. Umożliwia zarządzanie użytkownikami, stacjami rowerowymi oraz procesami wypożyczania i zwracania rowerów.

## Spis treści

- [Funkcjonalności](#funkcjonalności)
- [Wymagania](#wymagania)
- [Uruchamianie aplikacji](#uruchamianie-aplikacji)
  - [Lokalnie (Maven)](#lokalnie-maven)
  - [Za pomocą Dockera](#za-pomocą-dockera)
- [Jak działa aplikacja](#jak-działa-aplikacja)
- [Architektura projektu](#architektura-projektu)
- [Budowanie projektu](#budowanie-projektu)
  - [Rola Maven](#rola-maven)
  - [Rola Docker](#rola-docker)
- [Technologie](#technologie)
- [Autorzy](#autorzy)

## Funkcjonalności

System oferuje następujące funkcjonalności:

1. **Rejestracja użytkowników** - dodawanie nowych użytkowników do systemu
2. **Przeglądanie stacji** - wyświetlanie wszystkich dostępnych stacji z informacjami o dostępnych rowerach
3. **Przeglądanie rowerów** - sprawdzanie dostępnych rowerów na konkretnej stacji
4. **Wypożyczanie rowerów** - użytkownicy mogą wypożyczyć rower z wybranej stacji
5. **Zwracanie rowerów** - zwrot roweru na dowolną stację w systemie
6. **Historia wypożyczeń** - przeglądanie historii wypożyczeń użytkownika

## Wymagania

Przed uruchomieniem aplikacji upewnij się, że masz zainstalowane:

- **Java Development Kit (JDK) 17** lub nowszy
- **Apache Maven 3.6** lub nowszy
- **Docker** (opcjonalnie, do uruchamiania w kontenerze)

### Sprawdzenie wersji

```bash
java -version
mvn -version
docker --version
```

## Uruchamianie aplikacji

### Lokalnie (Maven)

1. **Sklonuj repozytorium:**

   ```bash
   git clone <URL_REPOZYTORIUM>
   cd BikeRentalSystem
   ```

2. **Skompiluj projekt:**

   ```bash
   mvn clean compile
   ```

3. **Uruchom aplikację:**

   ```bash
   mvn exec:java -Dexec.mainClass="com.rental.ui.Main"
   ```

   Alternatywnie, możesz najpierw zbudować JAR i uruchomić go:

   ```bash
   mvn clean package
   java -cp target/classes com.rental.ui.Main
   ```

### Za pomocą Dockera

1. **Sklonuj repozytorium:**

   ```bash
   git clone https://github.com/Cl1ngz/bike-rental-system.git
   cd BikeRentalSystem
   ```

2. **Zbuduj obraz Docker:**

   ```bash
   docker build -t bike-rental .
   ```

3. **Uruchom kontener:**
   ```bash
   docker run -it bike-rental
   ```

## Uruchomienie przez Docker Hub

Jeśli nie chcesz budować obrazu lokalnie, możesz pobrać i uruchomić gotowy obraz z Docker Hub:

```bash
docker pull cl1ngz/bike-rental:latest
docker run -it -p 8080:8080 cl1ngz/bike-rental:latest
```

## Jak działa aplikacja

### Przepływ działania aplikacji

1. **Inicjalizacja systemu:**

   - Przy starcie aplikacji tworzone są przykładowe stacje: "Centrum", "Politechnika", "Rynek"
   - Do stacji dodawane są przykładowe rowery (B001-B006)
   - Rejestrowane są przykładowi użytkownicy

2. **Menu główne:**
   Aplikacja wyświetla interaktywne menu z opcjami:

   ```
   ===== MENU WYPOŻYCZALNI ROWERÓW =====
   1. Zarejestruj nowego użytkownika
   2. Wyświetl stacje
   3. Wyświetl dostępne rowery na stacji
   4. Wypożycz rower
   5. Zwróć rower
   6. Wyświetl historię wypożyczeń użytkownika
   0. Wyjdź
   ```

3. **Proces wypożyczania:**

   - Użytkownik podaje swoje ID i ID stacji
   - System sprawdza dostępność rowerów i czy użytkownik nie ma już aktywnego wypożyczenia
   - Rower zostaje przypisany do użytkownika i usunięty ze stacji
   - Tworzony jest obiekt `Rental` z czasem rozpoczęcia

4. **Proces zwracania:**
   - Użytkownik podaje ID roweru i ID stacji zwrotu
   - System sprawdza czy rower jest wypożyczony i czy stacja ma wolne miejsca
   - Rower zostaje dodany do stacji, wypożyczenie kończy się
   - Obliczany jest czas wypożyczenia i dodawany do historii

### Obsługa błędów

Aplikacja obsługuje różne sytuacje błędne:

- Próba wypożyczenia gdy użytkownik już ma aktywne wypożyczenie
- Brak dostępnych rowerów na stacji
- Próba zwrotu na pełną stację
- Nieprawidłowe ID użytkownika, stacji lub roweru

## Architektura projektu

Projekt wykorzystuje wzorzec **Model-View-Controller (MVC)** i składa się z następujących pakietów:

### `com.rental.model`

Zawiera klasy reprezentujące dane:

- **`User`** - użytkownik systemu z historią wypożyczeń
- **`Bike`** - pojedynczy rower z informacją o dostępności
- **`Station`** - stacja rowerowa z zarządzaniem rowerami
- **`Rental`** - wypożyczenie łączące użytkownika, rower i stacje

### `com.rental.service`

- **`BikeRentalSystem`** - główna logika biznesowa, zarządza wszystkimi operacjami

### `com.rental.ui`

- **`Main`** - interfejs użytkownika (konsola), obsługa menu i interakcji

### `com.rental.exception`

Niestandardowe wyjątki dla różnych sytuacji błędnych:

- `UserNotFoundException`
- `StationNotFoundException`
- `BikeNotFoundException`
- `NoBikesAvailableException`
- `StationFullException`
- `UserAlreadyRentingException`
- `NotRentingException`

### Kluczowe funkcjonalności

- **Zarządzanie stanem:** Każdy rower może być dostępny (na stacji) lub wypożyczony
- **Śledzenie czasu:** System automatycznie rejestruje czas wypożyczenia i zwrotu
- **Walidacja:** Sprawdzanie dostępności, pojemności stacji, uprawnień użytkownika
- **Historia:** Automatyczne prowadzenie historii wszystkich wypożyczeń

## Budowanie projektu

### Rola Maven

Maven zarządza cyklem życia projektu:

- **Zarządzanie zależnościami:** Automatycznie pobiera biblioteki Java
- **Kompilacja:** `mvn compile` - kompiluje kod źródłowy
- **Testowanie:** `mvn test` - uruchamia testy jednostkowe
- **Pakowanie:** `mvn package` - tworzy plik JAR
- **Czyszczenie:** `mvn clean` - usuwa skompilowane pliki

Kluczowe komendy:

```bash
mvn clean compile          # Czyści i kompiluje
mvn test                   # Uruchamia testy
mvn package               # Tworzy JAR
mvn exec:java -Dexec.mainClass="com.rental.ui.Main"  # Uruchamia aplikację
```

### Rola Docker

Docker umożliwia konteneryzację aplikacji:

- **Spójne środowisko:** Aplikacja działa identycznie na każdym systemie
- **Izolacja:** Kontener jest odizolowany od systemu hosta
- **Łatwe wdrożenie:** Jeden obraz można uruchomić wszędzie gdzie jest Docker

W tym projekcie `Dockerfile` wykorzystuje wieloetapowe budowanie (multi-stage builds), aby zoptymalizować rozmiar finalnego obrazu. Pierwszy etap używa Maven do zbudowania aplikacji, a drugi etap kopiuje tylko wynikowy plik JAR do lekkiego obrazu bazowego.

## Technologie

- **Java 17+** - język programowania
- **Maven** - narzędzie do budowania projektu
- **Docker** - konteneryzacja aplikacji

## Autorzy

- Cl1ngz
- Bakerinho
- xdNiceTea
