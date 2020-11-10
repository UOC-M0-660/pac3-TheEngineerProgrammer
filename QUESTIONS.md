# PARTE TEORICA

### Lifecycle

#### Explica el ciclo de vida de una Activity.

##### ¿Por qué vinculamos las tareas de red a los componentes UI de la aplicación?
Porque así si se destruye el componente UI asociado, no seguiría realizando la tarea. 
Esto se consigue mediante los diferentes scopes de las currutinas.
Por ejemplo con el lifecycleScope asocias un tarea (job) a una actividad. Si se destruye la actividad y aun no se ha terminado la tarea, ésta no seguiría. 
También podemos cancelarla manualmenta la tarea usando .cancel() si lo necesita.

##### ¿Qué pasaría si intentamos actualizar la recyclerview con nuevos streams después de que el usuario haya cerrado la aplicación?
No será posible, como hemos mencionado en la apartado anterior, la tarea será cancelada automáticamente.
Y si por capricho alguien decide usar GlobalScope en vez del lifecycleScope, entonces la tarea de descarga puede seguir realizándola incluso después de haber destruido la actividad pero no podrás actualizar el recyclerview.

##### Describe brevemente los principales estados del ciclo de vida de una Activity.
Los métodos que definen los principales estados de la actividad son:
onCreate()
onStart()
onResume()
    aquí estamos en el estado activo, donde la actividad está corriendo en el primer plano y tiene el foco
onPause()
    aquí estamos en el estado pausado, la actividad sigue siendo visible pero no tiene el foco.
onStop()
    aquí estamos en le estado parado, la actividad ya no es visible.
onDestroy()
    aquí ya lo destruimos la actividad.

También hay un método onRestart() que pasa del estado parado a onStart()
Para más información ver:
https://developer.android.com/guide/components/activities/activity-lifecycle?hl=es

---

### Paginación 

#### Explica el uso de paginación en la API de Twitch.

##### ¿Qué ventajas ofrece la paginación a la aplicación?
Las principales ventajas de la paginación son reducir el uso del ancho de banda de la red
y los recursos del sistema y aumentar la velocidad de nuestra aplicación.

##### ¿Qué problemas puede tener la aplicación si no se utiliza paginación?
No se podrá cargar más datos para mostrar. O tendrá que cargarse todos los datos a la vez, por lo que rarentizará gravemente la aplicación y consumiendo mucho más los recursos del sistema.

##### Lista algunos ejemplos de aplicaciones que usan paginación.
Twitter, Telegram, Whatsapp, cualquier aplicación de noticias o de chat.
