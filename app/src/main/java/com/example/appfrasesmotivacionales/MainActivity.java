package com.example.appfrasesmotivacionales;

import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class MainActivity extends AppCompatActivity {

    // Referencias a los elementos de la interfaz
    private TextView tvQuote;
    private Button btnGenerateQuote;

    private static final String TAG = "FrasesApp";

    // Array de frases motivacionales locales como solución principal
    private static final String[] FRASES_LOCALES = {
        "El éxito es la suma de pequeños esfuerzos repetidos día tras día.|Robert Collier",
        "No esperes por el momento perfecto, toma el momento y hazlo perfecto.|Desconocido",
        "Los límites solo existen en tu mente.|Desconocido",
        "El futuro pertenece a quienes creen en la belleza de sus sueños.|Eleanor Roosevelt",
        "La motivación es lo que te pone en marcha, el hábito es lo que te mantiene en movimiento.|Jim Ryun",
        "No dejes que lo que no puedes hacer interfiera con lo que puedes hacer.|John Wooden",
        "El único modo de hacer un gran trabajo es amar lo que haces.|Steve Jobs",
        "La perseverancia es el trabajo duro que haces después de cansarte del trabajo duro que ya hiciste.|Newt Gingrich",
        "Cree en ti mismo y todo lo que eres. Sabe que hay algo dentro de ti que es más grande que cualquier obstáculo.|Christian D. Larson",
        "El fracaso es simplemente la oportunidad de comenzar de nuevo, esta vez de forma más inteligente.|Henry Ford",
        "No te rindas. Las grandes cosas toman tiempo.|Desconocido",
        "La diferencia entre lo ordinario y lo extraordinario es ese pequeño extra.|Jimmy Johnson",
        "Tu única limitación eres tú mismo.|Desconocido",
        "Haz algo hoy que tu yo del futuro te agradezca.|Sean Patrick Flanery",
        "El camino hacia el éxito está en tomar acción masiva y determinada.|Tony Robbins"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar referencias a los elementos de la UI
        initializeViews();

        // Configurar el listener del botón
        setupButtonListener();

        // Configurar SSL para HTTPS
        setupSSL();
    }

    /**
     * Configurar SSL para permitir conexiones HTTPS
     */
    private void setupSSL() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            Log.e(TAG, "Error configurando SSL", e);
        }
    }

    /**
     * Inicializa las referencias a los elementos de la interfaz
     */
    private void initializeViews() {
        tvQuote = findViewById(R.id.tvQuote);
        btnGenerateQuote = findViewById(R.id.btnGenerateQuote);
    }

    /**
     * Configura el listener del botón para generar frases
     */
    private void setupButtonListener() {
        btnGenerateQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deshabilitar el botón mientras se carga
                btnGenerateQuote.setEnabled(false);
                btnGenerateQuote.setText("Cargando...");

                // Ejecutar la tarea asíncrona para obtener la frase
                new FetchQuoteTask().execute();
            }
        });
    }

    /**
     * Clase AsyncTask para obtener frases motivacionales
     */
    private class FetchQuoteTask extends AsyncTask<Void, Void, QuoteResult> {

        @Override
        protected QuoteResult doInBackground(Void... voids) {
            // PRIMERA PRIORIDAD: Intentar con API simple y confiable
            QuoteResult result = trySimpleAPI();
            if (result != null) {
                return result;
            }

            // SEGUNDA PRIORIDAD: Usar frases locales (SIEMPRE funciona)
            return getRandomLocalQuote();
        }

        /**
         * Intenta obtener una frase de una API simple
         */
        private QuoteResult trySimpleAPI() {
            try {
                // API simple y confiable
                String apiUrl = "https://api.quotable.io/random";
                Log.d(TAG, "Intentando obtener frase de: " + apiUrl);

                URL url = new URL(apiUrl);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                // Configurar conexión
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Android)");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Código de respuesta: " + responseCode);

                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "UTF-8")
                    );
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String jsonResponse = response.toString();
                    Log.d(TAG, "Respuesta JSON: " + jsonResponse);

                    // Parsear JSON
                    JSONObject json = new JSONObject(jsonResponse);
                    String content = json.getString("content");
                    String author = json.getString("author");

                    Log.d(TAG, "Frase obtenida de API: " + content);
                    return new QuoteResult(content, author);
                }

            } catch (Exception e) {
                Log.e(TAG, "Error al obtener frase de API: " + e.getMessage(), e);
            }

            return null;
        }

        /**
         * Obtiene una frase aleatoria local (SIEMPRE funciona)
         */
        private QuoteResult getRandomLocalQuote() {
            try {
                int randomIndex = (int) (Math.random() * FRASES_LOCALES.length);
                String selectedQuote = FRASES_LOCALES[randomIndex];
                String[] parts = selectedQuote.split("\\|");

                String quote = parts[0];
                String author = parts.length > 1 ? parts[1] : "Desconocido";

                Log.d(TAG, "Usando frase local: " + quote);
                return new QuoteResult(quote, author);

            } catch (Exception e) {
                Log.e(TAG, "Error obteniendo frase local", e);
                // Frase de emergencia absoluta
                return new QuoteResult(
                    "El éxito es la suma de pequeños esfuerzos repetidos día tras día",
                    "Robert Collier"
                );
            }
        }

        @Override
        protected void onPostExecute(QuoteResult result) {
            // Rehabilitar el botón
            btnGenerateQuote.setEnabled(true);
            btnGenerateQuote.setText("Generar frase motivacional");

            if (result != null && result.isValid()) {
                // Formatear y mostrar la frase
                displayQuote(result);
                Log.d(TAG, "Frase mostrada exitosamente");
            } else {
                // Esto nunca debería pasar, pero por seguridad
                Log.e(TAG, "Error: resultado nulo o inválido");
                showFallbackQuote();
            }
        }
    }

    /**
     * Muestra la frase en la interfaz
     */
    private void displayQuote(QuoteResult result) {
        String formattedQuote = "\"" + result.quote + "\"\n\n— " + result.author;
        tvQuote.setText(formattedQuote);
    }

    /**
     * Muestra una frase de respaldo de emergencia
     */
    private void showFallbackQuote() {
        String emergencyQuote = "\"El éxito es la suma de pequeños esfuerzos repetidos día tras día\"\n\n— Robert Collier";
        tvQuote.setText(emergencyQuote);
        Toast.makeText(this, "Mostrando frase motivacional", Toast.LENGTH_SHORT).show();
    }

    /**
     * Clase para almacenar el resultado de una cita
     */
    private static class QuoteResult {
        final String quote;
        final String author;

        QuoteResult(String quote, String author) {
            this.quote = quote != null ? quote.trim() : "";
            this.author = author != null ? author.trim() : "Desconocido";
        }

        boolean isValid() {
            return quote != null && !quote.isEmpty();
        }
    }
}