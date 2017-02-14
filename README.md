# **BlocNow**

Crea notas y pínchalas en tu tablón. Mira los tablones de tus amigos con sus notas públicas y habla con ellos mediante mensajes directos. ¿Quieres que se te notifique alguna en concreto? Elige la opción de notificar y se te enviará un correo. En las notas podrás introducir texto, imagenes o incluso audio.

## Descripción de la Web:


Descripción general


* Parte Pública: Entra a un tablón en el que solo podrás poner una nota que no se guardará y en la que no se podrá comentar.


* Parte Privada: Añade a tus amigos, habla con ellos, comenta en sus notas y comparte tu tablón. Coloca las notas como privadas o públicas. Tendrás la opción de que se te notifiquen las notas que elijas. El servidor gestionará el guardado de tablones, la subida de fotos y audio y las notificaciones mediante un correo electrónico.


## Lista de las principales entidades:
1. Usuario
2. Tablones: Público y privado
3. Notas
4. Comentarios a las notas
5. Mensajes directos entre amigos1

### Usuario
1. **name:** Nombre del usuario y loggin
2. **password:** Contraseña para el loggin (no funcional actualmente)
3. **tablonPrivado:** **Tabón** de notas que solo puede ver el propio usuario
4. **tablonPublico:** **Tablón** de notas que puede ver cualquier amigo
5. **amigos:** Lista de **Usuarios** que ha agregado el usuario como amigos
6. **mensajes:** Lista de todos los **Mensajes** que se ha escrito con todos los amigos

### Tablón
1. **userName:** Nombre del **Usuario** al que pertenece el tablón.
2. **privado:** Determina si el **Tablón** es privado o público.
3. **notas:** Lista de todas las **Notas** que contien el **Tablón"**.

### Nota
1. **contenido:** Texto que contiene la **Nota**
2. **tablon:** Referencia al **Tablón** al que pertenece la **Nota**
3. **comentarios:** Lista que contiene todos los **Comentarios** de la **Nota**

### Comentario
1. **usuario:** Nombre del **Usuario** al que pertenece el comentario
2. **contenido:** Texto que contiene el **Comentario**
3. **nota:** Referencia a la **Nota** a la que pertenece el comentario

### Mensaje
1. **contenido:** Texto que contiene el **Mensaje**
2. **emisor:** Referencia al **Usuario** que emitió el mensaje
3. **receptor:** Referencia al **Usuario** que recibió el mensaje
4. **isMio:** Determina si el mensaje fue emitido por el usuario actualmente logeado


##Capturas de pantalla

###Página de inicio

![Página de inicio](Capturas/Inicio.PNG)

Esta es la primera página que se visualiza al entrar en la que se puede crear una nota (la cual no se guardará ni se podrá comentar) y/o registrarte como usuario.


###Página de registro

![Página de registro](Capturas/PaginaRegistro.PNG)

Aqui introduces tu nombre de usuario y contraseña. Si el nombre ya existe entrarás a la página de ese usuario.


###Página de usuario

![Página de usuario](Capturas/PaginaUsuario.PNG)

En la página de usuario puedes ver arriba (en azul) tus amigos y el botón de añadirlos. Justo debajo tendras a la izquierda el tablon privado (nadie podrá ver esas notas) y el tablón público que es que muestras a los demás. Puedes pulsar una nota para ver su contenido y, en el caso de que sea pública, añadirle comentarios.


###Página de ver nota

![Página de Ver nota](Capturas/VisualizarNota&Coments.PNG)

La nota seleccionada sale en el centro de la pagina y justo debajo un area donde escribir tus comentarios. Tus amigos también pueden comentar al seleccionar la nota desde la pagina de "Ver amigo". Si en lugar de una nota pública estás viendo una privada no te saldrá la opción de comentar.


###Página de ver amigo 

![Página de Ver amigo](Capturas/VistaTablonAmigo.PNG)

Al pinchar en el nombre de un amigo te llevará a esta página que te enseña su tablón público (a la derecha) y los mensajes directos que teneis entre los dos (a la izquierda).


##Diagrama de navegación

![Diagrama de navegación](Capturas/DiagramaNavegacion.PNG)


##Diagrama UML con sus relciones

![UML](Capturas/UML.PNG)


## Grupo del proyecto:


- Jorge Sánchez Carlin. Correo: j.sanchezcarl@alumnos.urjc.es Cuenta GitHub: jorequis
- Jose Ignacio Gaitán Martín. Correo: ji.gaitan@alumnos.urjc.es Cuenta GitHub: NachoGM27
