# sailaway-android-unofficial
Unofficial Android app to track boats simulated by Sailaway game (http://sailaway.world)

Hi, this is a very simple Android app which tracks on a Google Map the position of your boats and their direction/speed.

In order to use this application you must follow these two simple steps:

 - Open https://console.developers.google.com/, enable Google Maps SDK for Android and then obtain an API Key. This Key must be placed in **res/values/google_maps_api.xml**
 - Obtain your USERNAME and API_KEY from the sample HTTP GET you can find at https://sailaway.world/myaccount.pl. Then insert this information in **SailawayService.java**
