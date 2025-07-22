<h1>iOS Weather App Clone - Compose</h1>

<p>A simple implementation of the iOS Weather app's UI for Android, built fully in Jetpack Compose.</p>

<h2>Building and Running the App</h2>
<p>To get this project up and running, follow these steps:</p>

<h3>Prerequisites</h3>
<ul>
  <li>Android Studio (latest stable version recommended)</li>
  <li>Android SDK (API Level 29 or higher, target SDK 34, compile SDK 35)</li>
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
  <li><strong>Get a Google Places API Key</strong></li>
</ol>
<p>This app uses the Google Places API to search for locations. You need to get an API key:</p>
<ul>
  <li>Go to the Google Cloud Console: <a href="https://console.cloud.google.com/" target="_blank" rel="noopener noreferrer">https://console.cloud.google.com/</a></li>
  <li>Create a new project or select an existing one.</li>
  <li>Enable the "Places API" for your project.</li>
  <li>Create API credentials (an API key).</li>
</ul>
<p><strong>Important:</strong> Restrict your API key to prevent unauthorized use. You can restrict it by Android app (package name and SHA-1 certificate fingerprint).</p>

<h4>To generate the SHA-1 fingerprint:</h4>
<ul>
  <li>Open your project in Android Studio.</li>
  <li>In the right-hand panel, click on <em>Gradle</em>.</li>
  <li>Navigate to <code>YourProjectName &gt; Tasks &gt; android &gt; signingReport</code>.</li>
  <li>Double-click <code>signingReport</code>. The SHA-1 fingerprint will be displayed in the "Run" window at the bottom of Android Studio.</li>
</ul>
<p>The package name for this project is <code>com.example.weatherappcompose</code>.</p>

<ol start="4">
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

<ol start="5">
  <li><strong>Build and Run the App</strong></li>
</ol>
