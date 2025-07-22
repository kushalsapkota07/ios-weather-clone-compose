<h1>iOS Weather App Clone - Compose</h1>

<p>A simple implementation of the iOS Weather app's UI for Android, built fully in Jetpack Compose.</p>

<h2>Building and Running the App</h2>
<p>To get this project up and running, follow these steps:</p>

<h3>Prerequisites</h3>
<ul>
  <li>Android Studio (latest stable version recommended)</li>
  <li>Android SDK (API Level 29 or higher, target SDK 34, compile SDK 35)</li>
  <li>Get Places API Key: <a href="https://console.cloud.google.com/" target="_blank" rel="noopener noreferrer">https://console.cloud.google.com/</a></li></li>
  <li>Weather Data is fetched using <a href="https://open-meteo.com/" target="_blank" rel="noopener noreferrer">https://open-meteo.com/ </li> which offer free usage (for individual use) without an api key.</li>
</ul>

<ol>
  <li><strong>Clone the Repository</strong></li>
</ol>
<pre><code>git clone https://github.com/kushalsapkota07/ios-weather-clone-compose.git
</code></pre>

<ol start="2">
  <li><strong>Open in Android Studio</strong></li>
</ol>

<ol start="3">
  <li><strong>Add Your API Key to <code>secrets.properties</code></strong></li>
</ol>
<p>To keep your API key secure, it should not be committed directly into your code.</p>
<ul>
  <li>In Android Studio, switch to the "Project" view.</li>
  <li>Right-click on the root project folder.</li>
  <li>Select <em>New &gt; File</em>.</li>
  <li>Name the new file <code>secrets.properties</code>.</li>
  <li>Add the following line to the <code>secrets.properties</code> file, replacing <code>YOUR_PLACES_API_KEY</code> with your actual API key:</li>
</ul>
<pre><code>KAP="YOUR_PLACES_API_KEY"
</code></pre>

<ol start="4">
  <li><strong>Build and Run the App</strong></li>
</ol>
