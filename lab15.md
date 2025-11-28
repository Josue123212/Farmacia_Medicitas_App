











Alumno(s):	Josue Jonas Choquepuma Espinoza	Nota	
Grupo:		Ciclo: 
Criterio de Evaluación	Excelente (4pts)	Bueno (3pts)	Requiere mejora (2pts)	No acept. (0pts)	Puntaje Logrado
Sube commits por cada avance del laboratorio					6
Desarrolla la parte guiada del laboratorio y los ejercicios					8
Realiza la pregunta de observación					3
Es puntual y redacta el informe adecuadamente sin copias de otros autores					3
 
Laboratorio 14: Widgets
I.	Objetivo
●	Aprender a crear un widget de pantalla de inicio en Android utilizando Jetpack Glance. Implementar elementos básicos de interacción en el widget y personalizar su diseño.

II.	Seguridad
●	No ingresar con líquidos, ni comida al aula de Laboratorio.
●	Al culminar la sesión de laboratorio apagar correctamente la computadora y la pantalla, y ordenar las sillas utilizadas.

III.	Equipos y Materiales
●	Sistema Operativo Windows 10 o superior con conexión a la red del laboratorio
●	Android Studio o algún otro entorno de desarrollo donde se pueda correr aplicaciones móviles basadas en kotlin y jetpack compose.
DESARROLLO
Para la calificación de la parte guiada se considerará como puntaje que creen el repositorio de este laboratorio, y que realicen commits por cada cambio/nuevo archivo/modificación que realicen, siguiendo las buenas prácticas vistas en el laboratorio 4.
A partir de este laboratorio se solicita compartir también un video corto de los ejercicios funcionando, algo como esto:
ScreenRecording.mp4
Jetpack Preview es un framework compilado sobre el entorno de ejecución de Jetpack Compose que te permite desarrollar y diseñar widgets de apps con las APIs de Kotlin. Los widgets de la app son vistas de app en miniatura que se pueden incorporar en otras aplicaciones y recibir actualizaciones periódicas.




Ejercicio 1: Configuración de Glance
1.	Agrega la dependencia de Glance específica en el módulo de tu app en función del tipo de "de vista rápida" que deseas construir.
dependencies {
// For AppWidgets support
implementation ("androidx.glance:glance-appwidget:1.1.0")

// For interop APIs with Material 3
implementation ("androidx.glance:glance-material3:1.1.0")

// For interop APIs with Material 2
implementation ("androidx.glance:glance-material:1.1.0")

}


2.	Configura las siguientes opciones para asegurarte de que el compilador de Compose esté disponible para Vistazo
android {
   buildFeatures {
      compose true
   }

   composeOptions {
      kotlinCompilerExtensionVersion = "1.5"
   }

   kotlinOptions {
      jvmTarget = "1.8"
   }
}

3.	Registra el proveedor del widget de la app en el archivo AndroidManifest.xml. y el archivo de metadatos asociado
<receiver android:name=".SimpleWidget"
    android:exported="true">
    <intent-filter>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
    </intent-filter>
    <meta-data
        android:name="android.appwidget.provider"
        android:resource="@xml/my_app_widget_info" />
</receiver>
4.	Crea una clase llamada SimpleWidget e inserta este código

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class SimpleWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SimpleWidgetContent()
}

5.	Dentro de res/xml crea el archivo simple_widget_info.xml
<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
   android:minWidth="250dp"
   android:minHeight="100dp"
   android:updatePeriodMillis="0"
   android:initialLayout="@xml/simple_widget_layout"
   android:resizeMode="horizontal|vertical"
   android:widgetCategory="home_screen" />

6.	También crea el archivo simple_widget_layout.xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
   android:layout_width="match_parent"
   android:layout_height="match_parent">
</FrameLayout>

7.	Crea la clase SimpleWidgetContent e inserta el siguiente código:
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text

class SimpleWidgetContent : GlanceAppWidget() {

   override suspend fun provideGlance(context: Context, id: GlanceId) {

       provideContent {
           GlanceTheme {
               MyContent()
           }
       }
   }

   @Composable
   private fun MyContent() {
       Column(
           modifier = GlanceModifier.fillMaxSize()
               .background(GlanceTheme.colors.background),
           verticalAlignment = Alignment.Top,
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           Text(text = "¿A donde quieres dirigirte?", modifier = GlanceModifier.padding(12.dp))
           Row(horizontalAlignment = Alignment.CenterHorizontally) {
               Button(
                   text = "Página Principal",
                   onClick = actionStartActivity<MainActivity>()
               )
             
           }
       }
   }
}

La muestra de código anterior hace lo siguiente:
●	En el nivel superior Column, los elementos se colocan de forma vertical, uno después de cada uno. entre sí.
●	Column expande su tamaño para que coincida con el espacio disponible (mediante el GlanceModifier y alinea su contenido con la parte superior (verticalAlignment) y lo centra horizontalmente (horizontalAlignment).
●	El contenido de Column se define con la expresión lambda. El orden es importante.
●	El primer elemento de Column es un componente Text con 12.dp de con padding.
●	El segundo elemento es un Row, en el que los elementos se colocan horizontalmente uno detrás de otro, con un Button centrados de forma horizontal horizontalAlignment. La pantalla final depende del espacio disponible. La siguiente imagen es un ejemplo de cómo podría verse:
 

(comparte un video del funcionamiento)
https://drive.google.com/file/d/1chFTULMf3FfwWpntmDHLpZtaKaE3lARg/view?usp=sharing 



8.	Modifica tu aplicación con más de una vista, y añadele un botón al widget para que pueda iniciar la aplicación en alguna vista diferente al MainActivity (imagen de ejemplo)
 

Link git-hub;
https://github.com/Josue123212/Lab15-Moviles.git 

Ejercicio Final: 
1.	Revisa el figma documentación de Android UI Kit en referencia a los widgets.
2.	En base a esas buenas prácticas de diseño, crea un widget personalizado para alguna función de tu proyecto final, podría ser:
-	Algunos accesos directos a funciones importantes de tu aplicación
-	Información en tiempo real
-	Lista de las últimas acciones realizadas, etc
Se espera que cada integrante del proyecto debe crear su propio widget
(comparte un video del funcionamiento)
