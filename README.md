# pfc-gmz
Notas:

Glassfish
 - ¿Qué config tocar? La server-config (no la default-config)
 - Configurar seguridad: 
	1) En Security, tildar "Default Principal To Role Mapping"
	2) En el Realm/Dominio file, ir a manage users, y crear usuarios: colocar Group "manager", de modo que coincida con el rol configurado en el web.xml

General
 - Convenciones de codificación/coding style: https://google.github.io/styleguide/javaguide.html
 - En las clases eliminar imports innecesarios
 - Javadoc: documentar todas las clases. Los métodos documentarlos con criterio. 
		Getters y setters no es necesario. Métodos con nombres autoexplicativos tampoco. 
		Es bueno usar @link, pero no en la primer sentencia. Omitir @author
		Recordar que el javadoc también se vé a la hora de codificar, cuando Netbeans nos sugiere métodos también muestra su documentación.
		Convenciones: http://blog.joda.org/2012/11/javadoc-coding-standards.html, https://google.github.io/styleguide/javaguide.html#s7-javadoc
 - Usar el logger. Sobretodo para excepciones. Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"mensaje");
 - Tareas pendientes ponerlas como comentario para no olvidar

Vista
 - Usar el archivo Bundle para los textos
 - Los atrbutos de las entidades se acceden mediante sus getters/setters. Los atributos siempre "private"
 - Usar los toString() de las entidades para mostrarlas en la vista
 - Evitar en lo posible style en el xhtml. Usar jsfcrud.css
 - Ante dudas o no funcionamiento consultar la docu de Primefaces o googlear

Controllers
	- Leer http://stackoverflow.com/questions/2090033/why-jsf-calls-getters-multiple-times : atento a los getters de los controllers
	- Se recomienda usar ViewScoped por sobre SessionScoped
	- Comunicar controladores: ver como se comunican los controladores paciente y tratamiento. 
	
Gestores
 - Consultas no específicas (reutilizables) ponerlas como NamedQuery en la entidades
