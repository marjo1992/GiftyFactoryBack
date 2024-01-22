```plantuml

class PERSONNE as "Personne" {
    - <color:red><u>id</u></color> : int -> identidiant unique
    <color:blue><u>name</u></color> : varchar 50 -> nom
    <color:blue><u>firstname</u></color> : varchar 50 -> prénom
    <color:blue><u>birthdate</u></color> : date -> date de naissance
    + <color:blue>owner</color> - <color:green>FK vers Utilisateur</color> : créateur de la personne / utilisateur
    responsable = elle validera les liens de la personnes tant que 
        la personne n'est pas associé à un utilisateur, elle peux modifier 
        la personne (nom prénom date de naissance), elle peux transmettre 
        sa "responsabilité"  à un autre utilisateur (modification à l'acceptation)
        (vide si responsable soi meme)
    + <color:blue>next_owner</color> - <color:green>FK vers Utilisateur</color> : nouveaux responsable demandé
    <color:blue>owner_asked_message</color> : message de demande de récupération de 
    --
    <u>Evolutions possibles : </u>
    <color:blue><u>gender</u></color> : genre fille / garçon
}



class CHANGE_OWNER as "Demande de changement de responsable" {
    - <color:red><u>id</u></color> : int -> identidiant unique
    + <color:blue><u>person_id</u></color> - <color:green>FK vers Personne</color> : personne sur laquelle la demande de changement de responsabilité est faite
    + <color:blue><u>user_requesting</u></color> - <color:green>FK vers Utilisateur</color> : utilisateur demandant la transmission de la responsabilité, ne peut être fait que par l'utilisateur qui correspond à la personne
    + <color:blue><u>user_requested</u></color> - <color:green>FK vers Utilisateur</color> : utilisateur  à qui la demande de transmission de la responsabilité est faite
    <color:blue><u>is_next_owner_requester</u></color> : boolean -> true si demande faite pour que le demandeur soit le prochain responsable, faux, si la demande est faite par les responsable actuel
    <color:blue><u>request_date</u></color> date de la demande d'invitation
    <color:blue><u>is_request_processed</u></color> : boolean -> demande traitée. FAUX tant que la demande n'est pas acceptée ou refusée
    <color:blue>request_message</color> : varchar 250 -> message de demande
    <color:blue>reject_message</color> : varchar 250 -> message de refus
}

CHANGE_OWNER "0..*" --> "1" PERSONNE : <color:blue>person_id</color>
CHANGE_OWNER "0..*" --> "1" UTILISATEUR : <color:blue>user_requesting</color>
CHANGE_OWNER "0..*" --> "1" UTILISATEUR : <color:blue>user_requested</color>

class PERSONNE_MERGE as "Fusion de personnes" {
    - <color:red><u>id</u></color> : int -> identidiant unique
    + <color:blue><u>?? fusion_sender ??</u></color> - <color:green>FK vers Personne</color> : personne sur laquelle la demande de fusion est faite
    + <color:blue><u>?? fusion_receiver ??</u></color> - <color:green>FK vers Personne</color> : personne vers laquelle la demande de fusion est faite
    <color:blue><u>send_date</u></color> date de la demande d'invitation
    + <color:blue>owner_after_merge</color> - <color:green>FK vers Utilisateur</color> : responsable de la personne après la fusion - si merge de 2 personnes sans aucun utilisateurs correspondant
    <color:blue><u>is_request_processed</u></color> : boolean -> demande traitée. FAUX tant que la fusion n'est pas acceptée ou refusée
    <color:blue>request_message</color> : varchar 250 -> message de demande
    <color:blue>reject_message</color> : varchar 250 -> message de refus
    ---
    Si la fusion est acceptée les objets personnes et tout ce qui est lié sont mergé sous un seul objet. Et la demande de fusion est supprimée.
    Si la fusion est refusée, is_request_processed passe à true, et le message de refus est renseigné
}
PERSONNE_MERGE "0..*" --> "1" PERSONNE
PERSONNE_MERGE "0..*" --> "1" PERSONNE
PERSONNE_MERGE "0..*" --> "0..1" UTILISATEUR

class UTILISATEUR as "Utilisateur" {
    - <color:red><u>person_id</u></color> - <color:green>FK vers Personne</color>
    <color:blue><u>mail</u></color> : varchar 50 -> adresse email
    <color:blue><u>password</u></color> : ??? -> mot de passe
    <color:blue>picture</color> varchar 200 -> url photo
    <color:blue>is_mail_confirmed</color> boolean -> Vrai si l'utilisateur à confirmer son adresse mail (par un mail de confirmation)
    <color:blue>mail_token</color>varchar -> string à retourner pour confirmer le mail
    <color:blue>is_himself_owner</color> boolean -> Faux dans le cas où un utilisateur crée son compte, mais sa "personne" est géré par un responsable, l'utilisateur deviendra actif quand le responsable lui transmettra sa responsabilité.
    ..
    **UK** mail
}

PERSONNE "0..*" --> "0..1" UTILISATEUR : <color:blue>owner</color>
PERSONNE "0..*" --> "0..1" UTILISATEUR : <color:blue>next_owner</color>
UTILISATEUR "0..1" --> "1" PERSONNE : correspond à

class LIEN as "Liens personnes" {
    - <color:red><u>?? invitation_sender ??</u></color> - <color:green>FK vers Personne</color> : créateur du lien
    - <color:red><u>?? invitation_receiver ??</u></color> - <color:green>FK vers Personne</color> : personne lié
    + <color:blue><u>?? user_inviting??</u><</color> - <color:green>FK vers Utilisateur</color> : personne qui a envoie l'invitation
    <color:blue><u>?? live_same_household ??</u></color> habite avec : oui / non
    <color:blue><u>sending date</u></color> date de la demande d'invitation
    <color:blue><u>message</u></color> message d'invitation : mettre qui vous êtes par rapport à cette personne, si pas d'utilisateur mais responsable que ça l'aide à choisir
    <color:blue>is_invite_accepted</color> : Boolean -> invitation accepté ? **null** = en attente ; **0** = refusé ; **1** = accepté
    + <color:blue>?? user_answering??</color> - <color:green>FK vers Utilisateur</color> : personne qui a accepté l'invitation
    --
    <u>Evolutions possibles : </u>
    <color:blue><u>?? titre lien ??</u></color>  ex : parrain, tante, papa, maman, amis ....
}

PERSONNE "0..*" <--> "0..*" PERSONNE
(PERSONNE, PERSONNE) --- LIEN
LIEN "0..*" --> "1" UTILISATEUR : <color:blue>user_inviting</color>
LIEN "0..*" --> "0..1" UTILISATEUR : <color:blue>user_answering</color>

class INVITATION as "Demande de lien entre personne" {
    - <color:red><u>id</u></color> : int -> identidiant unique
    + <color:blue><u>sender</u></color> - <color:green>FK vers Personne</color> : personne qui invite
    + <color:blue><u>receiver</u></color> - <color:green>FK vers Personne</color> : personne invitée
    <color:blue><u>send_date</u></color> date de la demande d'invitation
    <color:blue>reply_date</color> date de réponse à l'invitation (refus ou acceptation)
    <color:blue><u>is_request_processed</u></color> : boolean -> demande traitée. FAUX tant que la fusion n'est pas acceptée ou refusée
    <color:blue>request_message</color> : varchar 250 -> message de demande
    <color:blue>reject_message</color> : varchar 250 -> message de refus
    ---
    Si l'invitation est acceptée, la demande de lien est supprimée.
    Si l'invitation est refusée, is_request_processed passe à true, et le message de refus est renseigné
}
INVITATION "0..*" --> "1" PERSONNE
INVITATION "0..*" --> "1" PERSONNE


class LISTE as "Liste Cadeaux" {
    - <color:red><u>id</u></color> : identifiant
    + <color:blue><u>name</u></color> - <color:green>FK vers Personne</color> : nom (ex liste mariage, anniversaire 1 an, ...)
    + <color:blue><u>visibility</u></color> - <color:green>FK vers Visibilité</color> : visibilité  (lien ou liste persones spécifique)
    + <color:blue><u>creator</u></color> - <color:green>FK vers Utilisateur</color> : utilisateur créateur
    <color:blue><u>closing_date</u></color> : date de clôture de la liste (plus d'ajout possible)
    --
    <u>Evolutions possibles : </u>
    <color:blue><u>event</u></color> : défaut (liste de base d'une personne)mariage, noël, anniversaire, autre, baptême ... avec création d'une ENUM
    <color:blue>?? liste personnes qui peuvent voir la liste ??</color> : définir une liste de personne pourvant voir la liste, avec ajout d'une valeur de visibilité "SPECIFIC" ou "DEFINED"
    <color:blue>?? liste personnes pouvant ajouter des idées ??</color> : définir une liste de personne pourvant ajouter des idées à la liste, dans le cadre des personnes qui peuvent voir la liste
    <color:blue>categories (link table)</color> : Définir des catégories dans la liste pour "organiser" les idées dans la liste
}

enum VISIBILITY as "Visibilité" {
    **PRIVATE**
    **HOME**
    **CIRCLE**
    **PUBLIC**
}

LISTE "0..*" -> "1" VISIBILITY : <color:blue>visibility</color>
LISTE "0..*" ---> "1" UTILISATEUR : <color:blue>creator</color>


class LIST_RECEIVER as "Destinatires de la liste de cadeaux" {
    - <color:red><u>list_id</u></color> - <color:green>FK vers Liste</color>
    - <color:red><u>pesonne_id</u></color> - <color:green>FK vers Personne</color>
}

LISTE "0..*" --> "1..*" PERSONNE
(LISTE, PERSONNE) -- LIST_RECEIVER

class IDEE as "Idée Cadeaux" {
    - <color:red><u>id</u></color> : identifiant
    + <color:blue>list_id</color> - <color:green>FK vers Liste</color> : liste
    <color:blue><u>name</u></color> : nom
    <color:blue><u>url_link</u></color> : url lien
    <color:blue>price</color> : prix
    + <color:blue><u>status</u></color> - <color:green>FK vers Status idée</color> : statut
    + <color:blue><u>author</u></color> - <color:green>FK vers Personne</color> : auteur
    + <color:blue>booker</color> - <color:green>FK vers Personne</color> : auteur
    + <color:blue><u>visibility</u></color> - <color:green>FK vers Visibilité</color> : visibilité
}

enum STATUS_IDEA as "Status Idée Cadeaux" {
    **DRAFT** : brouillon = status initiale d'une idée
    **TO_OFFER** : à offrir = mis par la personne qui à ajoué l'idée
    **BOOKED** : réservée = mis par la personne qui offre
    **BOUGHT** : achteté / prêt à être offert = mis par la personne qui offre
    **OFEER** : offert = mis par la personne qui offre ou responsable liste
    **RECEIVED** : recu = status validé par le personne ou son responsable
}

IDEE "0..*" --> "0..1" LISTE : <color:blue>liste_id</color>
IDEE "0..*" -> "1" VISIBILITY : <color:blue>visibility</color>
IDEE "0..*" -> "1" PERSONNE : <color:blue>author</color>
IDEE "0..*" -> "1" PERSONNE : <color:blue>booker</color>
IDEE "0..*" -> "1" STATUS_IDEA : <color:blue>status</color>

class IDEA_RECEIVER as "Destinataires de l'idée cadeaux" {
    - <color:red><u>idea_id</u></color> - <color:green>FK vers Idée</color>
    - <color:red><u>pesonne_id</u></color> - <color:green>FK vers Personne</color>
}

IDEE "0..*" --> "1..*" PERSONNE
(IDEE, PERSONNE) --- IDEA_RECEIVER


class COMMENTAIRE_IDEE as "Commentaire idée" {
    - <color:red><u>id</u></color> : identifiant
    + <color:blue><u>idea_id</u></color> - <color:green>FK vers Idée</color> : idée
    + <color:blue><u>comment_id</u></color> - <color:green>FK vers Commentaire Idée</color> : commentaire auquel ce commentaire répond
    + <color:blue><u>author</u></color> - <color:green>FK vers Utilisateur</color> : auteur de l'avis
    <color:blue><u>is_duplicate</u></color> : Boolean -> 0 = pas doublon, 1 = est doublon
    + <color:blue>duplicate_idea_id</color> - <color:green>FK vers Idée</color> : si vote doublon, idée dont celle ci fait doublon
    <color:blue><u>date_creation</u></color> : Date -> date de création du commentaire
    <color:blue><u>comment</u></color> : varchar 250 -> commentaire
}

COMMENTAIRE_IDEE "0..*" --> "1" UTILISATEUR : <color:blue>author</color>
COMMENTAIRE_IDEE "0..*" --> "1" IDEE : <color:blue>idea_id</color>
COMMENTAIRE_IDEE "0..*" --> "1" COMMENTAIRE_IDEE : <color:blue>comment_id</color>


class AVIS_IDEE as "Avis idée" {
    - <color:red><u>id</u></color> : identifiant
    + <color:blue><u>idea_id</u></color> - <color:green>FK vers Idée</color> : idée
    + <color:blue><u>author</u></color> - <color:green>FK vers Utilisateur</color> : auteur de l'avis
    + <color:blue><u>vote</u></color> - <color:green>FK vers Vote Avis</color> : vote
    --
    **UK** idea_id + author
}

enum VOTE_AVIS as "Vote Avis" {
    **no opinion** : sans avis
    **good idea** : bonne idée
    **excellent idea** : excellente idée
    **not a fan of the idea** : pas fan de l'idée
    **idea to forget** : idée à oublier
    **already has** : a déjà
}

AVIS_IDEE "0..*" --> "1" UTILISATEUR : <color:blue>author</color>
AVIS_IDEE "0..*" --> "1" IDEE : <color:blue>idea_id</color>
AVIS_IDEE "0..*" -> "1" VOTE_AVIS : <color:blue>vote</color>
```

https://plantuml.com/fr/class-diagram
https://plantuml.com/fr/creole