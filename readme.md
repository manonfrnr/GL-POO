# Projet Génie Logiciel : Chat en ligne

## Fonctionnalités techniques

* ✔ Serveur
* ✔ Client graphique
* ✔ Connexion avec login/mot de passe
* ✔ Groupe de discussion à plusieurs
* ✔ Messages privés
* ✔ Gestion de l'historique
* ✔ Possibilité de supprimer l'historiques

## Consignes respectées

* ✔ Utilisation de Trello : https://trello.com/b/ew34vy0v/gl-poo
* ✔ Utilisation de Github avec Gitflow : https://github.com/manonfrnr/GL-POO
* ✔ Design pattern : Factory
* ✔ Utilisation de JUnit (coverage de 60% sur le client et 80% sur le serveur)
* ✔ Utilisation de Maven pour l'automatisation du Build
* ✔ Méthode de gestion de projet : KANBAN
* ✔ Création d'une JavaDoc pour les fonctions réutilisables
* ✔ Diagrammes UML

## Comment l'utiliser

Il suffit de lancer dans un premier temps le serveur (l'executable est dans ServerChatRoom > target, il peut être regénéré avec le script Maven). On peut ensuite lancer le client graphique (l'executable est dans ChatClient > target, ou peut être également regénéré). Une fois le client lancé, il faut saisir un login et un mot de passe. Les 2 comptes disponibles sont "invit" et "test" (les mots de passes sont les mêmes que les identifiants). Une fois connecté, on peut double cliquer sur le nom de la personne avec qui on souhaite dialoguer ou entrer son nom en bas et appuyer sur Entrée. On peut rejoindre un groupe en tapan "#nom_du_group". Sur la fenêtre de tchat, on envoie un message en faisant "Entrée". On peut supprimer l'historique en faisant /delete comme message.

## Captures d'écrans de l'interface graphique

![Fenetre connexion](https://zupimages.net/up/20/25/ss5w.png)
Fenetre de connexion

![Fenetre liste utilisateurs](https://zupimages.net/up/20/25/raxc.png)
Fenetre de la liste des utilisateurs

![Fenetre de dialogue](https://zupimages.net/up/20/25/twks.png)
Fenetre de discussion

![Fenetre groupe](https://zupimages.net/up/20/25/ezud.png)
Fenetre de discussion en groupe
