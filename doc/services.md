# API back

lien valide = lien entre personne qui a était acceptée par la personne invitée

utilisateur actif = email confirmé + est responsable de sa propre personne

## /person services

### Rechercher une personne avec son nom, prénom et date de naissance (syntaxe exacte)

    GET /person/searchexact ? 
        name (optionel) 
        firstname (optionel) 
        birthdate (optionel)

**Contrôles d'autorisation d'appel :**
* /

**Contrôles :**
* Au moins le nom ou prénom doit être renseigné (au moins 1 lettre)

**Spécificités du service :**
Le service retourne une liste des personnes qui correspondent à la recherche, cette liste peut être vide s'il n'y a pas de correspondance (code retour 200)

**Retour :**
- 200 accepted 
- 400 si une des conditions de contrôles n'est pas rempli

### Rechercher les personne avec son nom, prénom (recherche large) : ??? merger avec le service de recherche de syntaxe exacte et juste retounée 2 liste (1 résultats exacten 1 résultats similaires)

    GET /person/searchsimilar ?
        name(optionel)
        firstname(optionel)


**Contrôles d'autorisation d'appel :**
* /

**Contrôles :**
Au moins le nom ou prénom doit être renseigné (au moins 1 lettre)

**Spécificités du service :**
Le service retourne une liste des personnes avec un nom / prénom similaire, c'est à dire recherche nom ou prénom qui contiennent la recherche sans majuscule et sans accent, cette liste peut être vide s'il n'y a pas de correspondance (retourne le nom prénom des responsables si pas d'utilisateur correspondant sur la personne)

**Retour :**
- 202 accepted 
- 400 si une des conditions de contrôles n'est pas rempli

### Créer une personne

    PUT /person/

**Contrôles d'autorisation d'appel :**
* utilisateur actif connecté

**Contrôles :**
* /

**Spécificités du service :**
* Le service crée la personne avec
  - utilisateur responsable : l'utilisateur connecté qui appel le service

**Retour :**
- 201 created
- 400 si une des conditions de contrôles n'est pas rempli


### Modifier une personne

    POST /person/{personId}

**Contrôles d'autorisation d'appel :**
* utilisateur actif connecté
* utilisateur correspondant à la personne OU utilisateur reponsable de la personne

**Contrôles :**
* /

**Spécificités du service :**
* Le service modifie les informations de la personne avec

**Retour :**
- 201 created
- 400 si une des conditions de contrôles n'est pas rempli

### FUSION

#### Récupérer les demandes de fusion associées à une personne

    GET /person/{personId}/merge

    - personId : id personne sur demandeuse de fusion

**Contrôles d'autorisation d'appel :**
* utilisateur actif connecté
* utilisateur correspondant OU utilisateur reponsable de la personne personId

**Contrôles :**
* /

**Spécificités du service :**
* Le service retourne les demandes de fusion associée à la personne:
  * celle en cours de demande et celle refusée
  * celle en tant qu'invité, celle en tant que demandeur

**Retour :**
- 202 accepted
- 400 si une des conditions de contrôles n'est pas rempli

#### Demande de fusion de deux "personnes"

    PUT /person/{personId}/merge/{personIdFusion}

    - personId : id personne sur demandeuse de fusion
    - personIdFusion : id personne invitée à fusionner

    paramètres :
        - prochain responsable (optionel)

**Contrôles d'autorisation d'appel :**
* utilisateur actif connecté
* utilisateur correspondant OU utilisateur reponsable de la personne personId

**Contrôles :**
* Il n'est pas possible de merger 2 personnes qui ont toutes les 2 un utilisateurs correspondant. Soit une des personnes en a un, soit aucune n'en a (elles sont toutes les deux contrôler par des responsable)
* Il n'est pas possible de merger 2 personnes qui n'ont pas le même nom + prénom + date de naissance
* Il n'est pas possible de demander une fusion avec une autre personnne, si une demande de fusion non traitée avec cette personne existe déjà
* Si les 2 personnes n'ont pas d'utilisateur correspondant, le prochain responsable doit être renseigné

**Spécificités du service :**
* Le service créé une entrée dans la table de fusion de personne
* Un mail est envoyé à l'utilisateur responsable/correspondant de la personne invitée à la fusion

**Retour :**
- 201 created
- 400 si une des conditions de contrôles n'est pas rempli


#### Fusion de 2 personnes

    POST /person/{personId}/merge/{mergeId}

    - personId : id personne invitée à la fusion
    - mergeId : id de la demande de fusion

**Contrôles d'autorisation d'appel :**
* utilisateur actif connecté
* utilisateur correspondant OU utilisateur reponsable de la personne receveur de la demande de fusion (personId)

**Contrôles :**
* Il n'est pas possible de merger 2 personnes qui ont toutes les 2 un utilisateurs correspondant. Soit une des personnes en a un, soit aucune n'en a (elles sont toutes les deux contrôler par des responsable)
* Une demande de fusion non traitée avec l'id mergeId existe avec comme invité personId

**Spécificités du service :**
* Le service fusionne les 2 personnes :
  * remplace toutes les occurences de la personne demandeuse avec l'id de la personne invitée à la fusion
  * une fois le remplacement fini, supprime la ligne de la personne demandeuse
* Si les 2 personnes n'ont pas d'utilisateur correspondant, l'utilisateur choisi comme nouveau responsable est assigné comme responsable
* Un mail est envoyé à l'utilisateur responsable/correspondant de la personne demandeuse de la fusion

**Retour :**
- 204 no content
- 400 si une des conditions de contrôles n'est pas rempli


#### Suppression d'une demande de fusion de deux "personnes"

    DELETE /person/{personId}/merge/{mergeId}

    - personId : id personne sur demandeuse de fusion
    - mergeId : id de la demande de fusion

**Contrôles d'autorisation d'appel :**
* utilisateur actif connecté
* utilisateur correspondant OU utilisateur reponsable de la personne demandeuse

**Contrôles :**
* Une demande de fusion avec l'id mergeId non traitée et avec ce demandeur doit existé

**Spécificités du service :**
* Le service supprime la demande de fusion

**Retour :**
- 204 no content
- 400 si une des conditions de contrôles n'est pas rempli


#### Refus d'une demande de fusion de deux "personnes"

    POST /person/{personId}/merge/{mergeId}/reject

    - personId : id personne sur invitée à faire une fusion
    - mergeId : id de la demande de fusion

    paramètres :
        - message de refus

**Contrôles d'autorisation d'appel :**
* utilisateur actif connecté
* utilisateur correspondant OU utilisateur reponsable de la personne invitée

**Contrôles :**
* Une demande de fusion avec l'id mergeId non traitée avec cet invité doit existé

**Spécificités du service :**
* Le service renseigne le message de refus de la demande de fusion, et met la demande comme traitée
* Un mail est envoyé à l'utilisateur responsable/correspondant de la personne demandeuse de la fusion

**Retour :**
- 204 no content
- 400 si une des conditions de contrôles n'est pas rempli

## /user services

### Créer un utilisateur sur une personne non existante

    PUT /user/
    
**Contrôles d'autorisation d'appel :**
* /

**Contrôles :**
* Il n'est pas possible de créer une personne qui a le même prénom + nom + date de naissance qu'une autre personne déjà existante.
* Il n'est pas possible de créer un utilisateur avec le même mail qu'un utilisateur existant
* Adresse mail valide (regex)
* Mot de passe minimum 6 caractères

**Spécificités du service :**
* Le service créé l'utilisateur et la personne.
* L'utilisateur est créé avec :
  - responsable de sa propre personne : VRAI
  - email confirmé : FAUX
  - un token aléatoire générée
* Un email de confirmation d'adresse mail est envoyé à l'adresse mail de l'utilisateur créé

**Retour :**
- 201 created
- 400 si une des conditions de contrôles n'est pas rempli


### Créer un utilisateur sur une personne existante

    PUT /user/{personId}

**Contrôles d'autorisation d'appel :**
* /

**Contrôles :**
* Il n'est pas possible de créer un utilisateur avec le même mail qu'un utilisteur existant
* La personne existe
* Il n'y a pas déjà de demande de responsabilité avec un utilisateur correspondant en cours sur la personne
* Adresse mail valide (regex)
* Mot de passe minimum 6 caractères

**Spécificités du service :**
* L'utilisateur est créé avec :
  - responsable de sa propre personne : FAUX
  - email confirmé : FAUX
  - un token aléatoire générée
* Renseigne sur la personne l'utilisateur demandant la responsabilité avec l'utisitateur créé.
* Un email est envoyé à l'utilisateur responsable de la personne actuellement.
* Un email de confirmation d'adresse mail est envoyé à l'adresse mail de l'utilisateur créé.

**Retour :**
- 201 created
- 400 si une des conditions de contrôles n'est pas rempli


### Confirmer l'adresse mail d'un utilisateur

    POST /user/confirm

    Paramètres :
      * token
      * mail

**Contrôles d'autorisation d'appel :**
* /

**Contrôles :**
* le mail correspond à un utilisateur dont le mail n'est pas confirmé
* le token correspond à celui de l'utilisateur correspondant au mail

**Spécificités du service :**
* L'utilisateur est mis à jour avec :
  - email confirmé : VRAI

**Retour :**
- 202 accepted
- 400 si une des conditions de contrôles n'est pas rempli


### Demande envoi mail de confirmation de l'adresse mail d'un utilisateur

    GET /user/confirm ? mail

**Contrôles d'autorisation d'appel :**
* /

**Contrôles :**
* le mail correspond à un utilisateur dont le mail n'est pas confirmé

**Spécificités du service :**
* Un email de confirmation d'adresse mail est envoyé à l'adresse mail de l'utilisateur créé.

**Retour :**
- 204 no content
- 400 si une des conditions de contrôles n'est pas rempli


### Récupérer son user (service pour se connecter)

    GET /user/me

    Paramètres :
      * mail
      * mot de passe

**Contrôles d'autorisation d'appel :**
* /

**Contrôles :**
* le mot de passe correspond à l'email
* le mail correspond à un celui d'un utilisateur actif

**Spécificités du service :**
* /

**Retour :**
- 204 no content
- 403 forbiden si le mot de passe ne correspond à l'email
- 400 bad request si le mail correspond à un celui d'un utilisateur actif (retourne message erreur en fonction si utilisateur en attente de récupération de sa personne, ou si email non confirmer)

## /list services

### Récupérer les listes d'une personne 

    GET /list/{personneId}/

**Contrôles d'autorisation d'appel :**
* utilisateur connecté actif

**Contrôles :**

**Spécificités du service :**
* Le service retourne toutes les listes d'une personne

**Retour :**
- 202 accepted

### Créer une liste d'idée cadeaux pour une personne

    PUT /list/

**Contrôles d'autorisation d'appel :**
* utilisateur connecté actif
* uniquement si la personne pour qui est la liste est lié par un lien valide à 
    - la personne correspondante de l'utilisateur qui appel le service
    - ou une des personnes pour lequel l'utilisateur est responsble  

**Contrôles :**

**Spécificités du service :**
* Le service créé la liste et la retourne

**Retour :**
- 201 created

### Modifier une liste d'idée cadeaux d'une personne

    POST /list/{listeId}/

**Contrôles d'autorisation d'appel :**
* utilisateur connecté actif
* utilisateur correspondant ou responsable de la personne créateur de la liste

**Contrôles :**

**Spécificités du service :**
* Le service modifie la liste et la retourne

**Retour :**
- 202 accepted

## /gift-idea services

### Créer une nouvelle idée cadeaux
    
    PUT /gift-idea

    - giftIdea : pas d'id, pas de status, pas de créateur, pas de date

**Contrôles d'autorisation d'appel :**
* utilisateur connecté actif
* uniquement si la personne pour qui est la liste est lié par un lien valide à 
    - la personne correspondante de l'utilisateur qui appel le service
    - ou une des personnes pour lequel l'utilisateur est responsble  
* s'il y a une liste choisi, elle doit être visible par la personne

**Contrôles :**

**Spécificités du service :**
L'idée cadeaux est créé avec :
* status = brouillon
* créateur = l'utilisateur qui appel le service

**Retour :**
- 201 created


### Modifier une idée cadeaux

    POST /gift-idea/{giftIdeaId}

**Contrôles d'autorisation d'appel :**
* utilisateur connecté actif
* utilisateur correspondant ou responsable de la personne créateur de l'idée
* s'il y a une liste choisi, elle doit être visible par la personne

**Contrôles :**

**Spécificités du service :**
* /

**Retour :**
- 202 accepted
   
### Modifier le status d'une idée cadeaux
    POST /gift-idea/{giftIdeaId}/status 
    
    - status : DRAFT / PUBLISHED / BOOKED / BOUGHT / OFFERED / RECEIVED
    - personne qui fait le changement

**Contrôles d'autorisation d'appel :**
* utilisateur connecté actif
* personne qui fait le changement doit avoir comme utilisateur correspondant ou comme responsable, l'utilisateur connecté
* l'idée cadeaux doit exister et être visible par l'utilisateur correspondant ou responsable de la personne pour qui est le cadeaux
* utilisateur correspondant ou responsable de la personne créateur de l'idée peux mettre les status suivant : PUBLISHED / DRAFT
* utilisateur correspondant ou responsable de la personne qui a l'idée cadeaux en visibilité : BOOKED
* utilisateur correspondant ou responsable de la personne qui a réservée l'idée cadeaux peut mettre les status suivant : BOUGHT / OFFERED
* utilisateur correspondant ou responsable de la personne qui a créé la liste d'idée cadeaux associée à l'idée apès la date de lévemenent: OFFERED
* utilisateur correspondant ou responsable de la personne pour qui est l'idée cadeaux peut mettre les status suivant : RECEIVED

**Contrôles :**
- Une idée cadeaux ne peux passer au status DRAFT, uniquement si elle était au status PUBLISHED
- Une idée cadeaux ne peux passer au status PUBLISHED, uniquement si n'était pas au status OFFERED ou RECEIVED
- Une idée cadeaux ne peux passer au status BOOKED, uniquement si elle était au status PUBLISHED
- Une idée cadeaux ne peux passer au status BOUGHT, uniquement si elle était au status BOOKED
- Une idée cadeaux ne peux passer au status OFFERED, uniquement si elle était au status BOUGHT ou, si c'est l'utilisateur correspondant ou responsable de la personne créateur de la liste qui change et alors le status était PUBLISHED / BOOKED ou BOUGHT
- Une idée cadeaux ne peux passer au status RECEIVED, uniquement si elle était au status OFFERED


**Spécificités du service :**
* /

**Retour :**
- 202 accepted

### Supprimer une idée cadeaux

    DELETE /gift-idea/{giftIdeaId}
    
**Contrôles d'autorisation d'appel :**
* utilisateur connecté actif
* utilisateur correspondant ou responsable de la personne créateur de l'idée
* l'idée doit être au status DRAFT ou PUBLISHED

**Contrôles :**

**Spécificités du service :**
* /

**Retour :**
- 204 no content

## /circle services

### Récupérer toutes les personnes dans le cercle avec le niveau
    GET /circle/{personId}

**Contrôles d'autorisation d'appel :**
* utilisateur connecté actif
* utilisateur correspondant ou reponsable de la personne

**Contrôles :**

**Spécificités du service :**
* /

**Retour :**
- 202 accepted

### Récupérer toutes les invitations en cours de demande pour une personne

    GET /circle/{personId}/pending

**Contrôles d'autorisation d'appel :**
* utilisateur connecté actif
* utilisateur correspondant ou reponsable de la personne

**Contrôles :**

**Spécificités du service :**
* Retourne les invitations en tant qu'invité et en tant qu'inviteur

**Retour :**
- 202 accepted

### Inviter quelqu'un

    GET /circle/{personId}/invite/{personIdInvited}

**Contrôles d'autorisation d'appel :**
* utilisateur connecté actif
* utilisateur correspondant ou reponsable de la personne personId

**Contrôles :**
* personId différents de personIdInvited
* il n'existe pas d'invitation en cours de demande pour entre ces personnes (en tant qu'invité ou inviteur)
* il n'y pas encore de lien entre ces deux personnes

**Spécificités du service :**
* La demande d'invitation est enregistrée
* Un mail est envoyé à l'invité

**Retour :**
- 202 accepted



#### Accepter une invitation

    POST /circle/{personId}/invite/{inviteId}

    - personId : id personne sur demandeuse
    - inviteId : id invitation 

**Contrôles d'autorisation d'appel :**
* utilisateur actif connecté
* utilisateur correspondant OU utilisateur reponsable de la personne receveur de la demande de lien personId

**Contrôles :**
* Une demande de lien avec l'id inviteId non traitée existe pour la personne personeId en tant qu'invité

**Spécificités du service :**
* Le service lie les 2 personnes :
  * une fois le lien fini, supprime la ligne de la personne demandeuse
* Un mail est envoyé à l'utilisateur responsable/correspondant de la personne demandeuse de lien

**Retour :**
- 204 no content
- 400 si une des conditions de contrôles n'est pas rempli


#### Suppression d'une demande de lien

    DELETE /circle/{personId}/invite/{inviteId}

    - personId : id personne sur demandeuse

**Contrôles d'autorisation d'appel :**
* utilisateur actif connecté
* utilisateur correspondant OU utilisateur reponsable de la personne demandeuse

**Contrôles :**
* Une demande de lien avec ce demandeur et cet invité doit existé

**Spécificités du service :**
* Le service supprime la demande de lien

**Retour :**
- 204 no content
- 400 si une des conditions de contrôles n'est pas rempli


#### Refus d'une invitation

    POST /circle/{personId}/invite/{inviteId}/reject

    - personId : id personne sur invité

    paramètres :
        - message de refus

**Contrôles d'autorisation d'appel :**
* utilisateur actif connecté
* utilisateur correspondant OU utilisateur reponsable de la personne invitée

**Contrôles :**
* La demande de lien avec l'id inviteId existe avec cet invité doit existé, est non traitée.

**Spécificités du service :**
* Le service renseigne le message de refus de la demande, et met la demande comme traitée
* Un mail est envoyé à l'utilisateur responsable/correspondant de la personne demandeuse

**Retour :**
- 204 no content
- 400 si une des conditions de contrôles n'est pas rempli