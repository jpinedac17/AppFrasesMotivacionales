# Documentación de la Experiencia con IA

## Prompts Utilizados
Crea un proyecto de Android Studio en Java que tenga la siguiente funcionalidad:

  1. La app debe tener una **pantalla principal** con:
   - Un **TextView** grande y centrado para mostrar la frase motivacional.
   - Un **Button** debajo que diga "Generar frase motivacional".

  2. Al tocar el botón:
   - La app debe hacer una llamada HTTP GET a la API **gpalleschi/quotes_api** para obtener una frase aleatoria en español.
     - Endpoint: `https://programming-quotes-api.herokuapp.com/quotes/random?lang=es`
   - La respuesta será JSON, con la frase en un campo llamado `en` (el texto de la cita) y `author` (autor).
   - La app debe actualizar el **TextView** con la frase y el autor en formato:  
     `"“[Frase]” — [Autor]"`.

  3. La llamada HTTP debe ejecutarse **en un hilo separado** (por ejemplo, usando `AsyncTask`, `Thread` o `AsyncTaskLoader`) para no bloquear la interfaz.

  4. Debe manejar errores de conexión:
   - Si falla la petición, mostrar un Toast con el mensaje: `"No se pudo obtener la frase. Intenta de nuevo."`.

  5. La app debe usar **Android Studio Java** estándar (no Kotlin) y mínimo SDK 21.

  6. El diseño de la pantalla debe ser simple y limpio:
   - Fondo blanco.
   - TextView negro, fuente legible y centrada.
   - Button azul con texto blanco y borde redondeado.

  7. Genera **todo el código necesario**:
   - `MainActivity.java`
   - `activity_main.xml`
   - Configuración de permisos en `AndroidManifest.xml` (si es necesario para Internet)

  8. Agrega comentarios explicativos en el código para que sea fácil de entender.

  Genera el proyecto completo listo para ejecutar.

2. Corrección de errores en la API y utiliza la API api.quotable.io.

## Resultados Obtenidos
- **Código generado**: Se creó la estructura principal de la app, incluyendo `MainActivity.java`, layouts y el manejo de la API para obtener frases.
- **Errores**: Algunos problemas con la actualización de la interfaz y llamadas asíncronas a la API.
- **Mejoras**: Se implementó manejo de errores y carga de frases aleatorias correctamente en la interfaz.

## Problemas Encontrados y Cómo se Resolvían con IA
- **Problema**: La app no mostraba correctamente las frases al iniciar.
  - **Solución con IA**: Se utilizó un prompt para generar la función de carga inicial y refresco de frases.
- **Problema**: Manejo incorrecto de la conexión con la API externa.
  - **Solución con IA**: Se generó código sugerido para realizar llamadas HTTP correctamente y actualizar la interfaz.

## Reflexión Final
El uso de herramientas de IA como Claude Sonnet 4, como en nuestro caso, fue clave para acelerar el desarrollo, sugerir soluciones y corregir errores comunes. La experiencia permitió entender cómo la IA puede integrarse en el flujo de desarrollo móvil, agilizando tareas repetitivas y mejorando la eficiencia del proyecto.
