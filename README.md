# 🚀 Ogólny opis projektu
#### Ten projekt jest typowym przykładem zastosowania systemu Linux w systemach wbudowanych. Projekt bazuje na płytce STM32MP157-DK2, która ma wbudowany ekran dotykowy i działa na procesorze wielordzeniowym(2xCortexA7 i 1xCortexM4). Obraz systemu tworzony jest za pomocą narzędzia Yocto Project, a następnie kopiowany na uprzednio sformatowaną kartę SD, z której płytka uruchamia system Linux.
---
## ⚙️ Konfiguracja i Budowanie obrazu Yocto
Yocto Project to otwarte środowisko programistyczne służące do tworzenia własnych, dostosowanych dystrybucji systemu Linux dla urządzeń wbudowanych. Nie jest to gotowy system operacyjny, lecz zestaw narzędzi, skryptów i metadanych, które umożliwiają zbudowanie systemu dopasowanego do konkretnego sprzętu i wymagań projektu.

Do celów tego projektu została pobrana wersja Yocto z najnowszego LTS (Long Ttime Support) branchu - Scarthgap.

Podstawą działania Yocto są warstwy (layers), które zawierają pliki konfiguracyjne i przepisy budowy oprogramowania. Każda warstwa odpowiada za określony obszar systemu, na przykład wsparcie sprzętowe dla danej platformy, biblioteki graficzne lub dodatkowe pakiety. Niżej zostały wymienione komendy na działanie z warstwami.
```bash
# wyświetlić wszytkie warstwy.
bitbake-layers show-layers
# stworzyć własną warstwę.
bitbake-layers create-layer ${NameOfLayer}
# dodanie warstwy do configuracji obrazu.
bitbake-layers add-layer ${NameOfLayer}
```
Kluczowym elementem Yocto jest BitBake, narzędzie pełniące rolę systemu budowania, które analizuje wszystkie przepisy i zależności między nimi, a następnie kompiluje odpowiednie składniki. Przepisy (recipes) to pliki z rozszerzeniem .bb, które opisują sposób pobrania źródeł, ich konfiguracji, kompilacji i instalacji.

**WAŻNE**: przy każdym nowym uruchomieniu terminala należy uruchomić komendę source ```oe-init-build-env```, aby zainicjalizować zmienne środowiskowe. W przypadku nieobecności zostanie również utworzony katalog ```build``` wraz z podkatalogiem ```conf``` w jego wnętrzu. W katalogu ```conf``` zostaną utworzone
pliki konfiguracyjne: ```local.conf``` i ```bblayers.conf```. Ten reposytorium zawiera nasze pliki konfiguracyjne: ```bblayersconfExample.txt``` oraz ```localconfExample.txt```. Ich zawartość można skopiować do utworzonych plików i mieć skonfigurowany przykładowy projekt.

Aby uruchomić stworzenie obrazu trzeba odpalić komendę ```bitbake st-image-weston```. Mogą pojawić się błędy, ale one są raczej unikalne dla każdego systemu, na którym Linuks się kompiluje.

Po zakończeniu builda, pliki zawierające obraz systemu można znaleźć w lokalizacji ```/tmp-glibc/deploy/images/($MACHINE)```. Jeżeli w katalogu nie znajduje się plik obrazu ```.raw``` np.:```FlashLayout_sdcard_stm32mp157f-dk2-opteemin.raw```, należy go stworzyć za pomocą skryptu z katalogu ```/scripts```. 
Skrypt tworzy obraz na podstawie FlashLayoutu .tsv z katalogu ```/flashlayout_st-image-weston```. Przykładowo obraz można wygenerować następującą komendą:
```bash
sudo DEVICE=sdX 
./scripts/create_sdcard_from_flashlayout.sh 
./flashlayout_st-image-weston/opteemin/FlashLayout_sdcard_stm32mp157f-dk2-opteemin.tsv
```
, gdzie sdX to nośnik pamięci (należy sprawdzić komendą ```lsblk```). 
Po zakończeniu działania skrypt sam podpowie komendę do flashowania na nośnik:
To put this raw image on sdcard:
```bash
sudo dd if='./flashlayout_st-image-weston/opteemin/../../FlashLayout_sdcard_stm32mp157f-dk2-opteemin.raw' of=/dev/sdX bs=8M conv=fdatasync status=progress
