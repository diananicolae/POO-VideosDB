# POO-VideosDB
## Nicolae Mihaela -Diana, 325CA

1. Structura

    Clasele au fost impartite in folderul sursa in functie de rolul lor:
    - pachetele actor, entertainment si user contin entitatile modelate (Actor, Video, Movie,
Serial, User);
    - pachetul actions contine clase individuale pentru fiecare actiune (Commands, Queries,
Recomms), containerul de date (Database), si clasa ProcessData in care are lor procesarea
propriu-zisa;
    - pachetul utils contine clase cu metode statice ce sunt utilizate la procesare.
    
    Pentru a putea lucra cu noile entitati, am creat in ProcessUtils functii de trasnfer pentru
listele din input. Noile liste sunt retinute intr-un repository in Database.
    
2. Rezolvarea cerintelor
    
    Am creat pentru fiecare tip de actiune o clasa individuala, in care am implementat tipurile
de actiuni ce pot fi exercitate asupra repository-ului, dand ca parametru doar instanta de 
ActionInputData, ce contine toate informatiile necesare procesarii.

    COMMANDS:
    - in fiecare metoda retinem instantele de User si Video cu care vom lucra, verificam daca comanda
poate si exercitata (daca nu, returnam error) si o executam;
    - pentru comanda rating, se vor trata separat cazurile pentru Movie si Serial, utilizand un Map
pentru a retine pentru fiecare user video-urile care au primit deja rating;
    - comenzile sunt explicate mai detaliat in comentarii.
    
    QUERIES:
    - pentru a usura munca in cadrul query-urilor, am realizat functii separate de filtrare pt Actors
si Videos in clasa ProcessUtils.
    - pentru majoritatea query-urilor am retinut datele intr-un Map, entry-urile continand datele dupa
care se va face apoi sortarea (ex: numele actorilor cu average-ul lor, titlul video-ului cu rating-ul
lui, etc.);
    - Map-ul este apoi sortat in ordinea ceruta si transformat intr-o lista cu ajutorul functiei
getListFromMap din ProcessUtils.

    RECOMMENDATIONS:
    - in fiecare metoda retinem instanta de user pt care vom face recomandarea, testand daca aceasta
poate fi aplicata(user-ul exista si, dupa caz, este premium);
    - filtram listele de video, pentru a elimina recomadarile necorespunzatoare, iar apoi sortam lista
in ordinea aplicabila pentru fiecare tip de recomandare, mai multe detalii aflandu-se in comentarii;
    
3. Procesarea

    Procesarea propriu-zisa are loc in casa ProcessData, ce contine mai multe switch-uri pentru a determina
ce metoda, din ce tip de actiune ar trebui apelata, folosind Action Type, Object Type si Criteria. Fiecare
metoda va returna un String message, cu ajutorul caruia va fi creat obiectul JSON ce va fi adaugat in
JSONArray-ul final, ce va fi printat la output.
 
    
